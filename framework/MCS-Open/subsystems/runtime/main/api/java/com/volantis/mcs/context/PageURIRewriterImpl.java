/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import java.net.URI;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import com.volantis.mcs.integration.PageURLType;

/**
 * The implementation of the URIRewriter to be used 
 * within the MarinerPageContext.
 * 
 * It maintains a dynamic stack (chain) of URIRewriters.
 */
public class PageURIRewriterImpl implements PageURIRewriter {
    private Stack rewritersStack;
    
    /**
     * Construct a new instance of the URI rewriter.
     */
    public PageURIRewriterImpl() {
        this.rewritersStack = new Stack();
    }
    
    // Javadoc inherited
    public URI rewrite(URI uri, PageURLType type) {
        Iterator iterator = rewritersStack.iterator();
        
        while (iterator.hasNext()) {
            PageURIRewriter rewriter = (PageURIRewriter) iterator.next();
            
            uri = rewriter.rewrite(uri, type);
        }
        
        return uri;
    }

    // Javadoc inherited
    public boolean willPossiblyRewrite(PageURLType type) {
        // If there's no rewriters on the stack, this one for sure
        // will not rewrite the URL. Returns false immediately.
        if (rewritersStack.isEmpty()) {
            return false;
        }

        // If there are some rewriters on the stack, look for the first
        // one which will possibly rewrite.
        Iterator iterator = rewritersStack.iterator();
        
        while (iterator.hasNext()) {
            PageURIRewriter rewriter = (PageURIRewriter) iterator.next();
            
            // If any of the rewriters will possibly rewrite,
            // then return true immediately.
            if (rewriter.willPossiblyRewrite(type)) {
                return true;
            }
        }
        
        // We are here, because none of the rewriters would possibly rewrite.
        // It's safe to return false now.
        return false;
    }

    /**
     * Pushes the specified pageURLRewriter onto the stack.
     * 
     * @param rewriter The rewriter to push.
     */
    public void pushRewriter(PageURIRewriter rewriter) {
        rewritersStack.push(rewriter);
    }

    /**
     * Pops a pageURLRewriter from the stack.
     * 
     * @param expectedRewriter The expected pageURLRewriter to pop.
     * @throws EmptyStackException if the stack is empty.
     * @throws IllegalStateException if the expectedRewriter is not null,
     * and is not the one on the top of the stack. 
     */
    public void popRewriter(PageURIRewriter expectedRewriter) {
        // Pop rewriter from the stack.
        // If the stack is empty, the EmptyStackException will be thrown.
        PageURIRewriter rewriter = (PageURIRewriter) rewritersStack.pop();
        
        if (expectedRewriter != null) {
            if (rewriter != expectedRewriter) {
                throw new IllegalStateException("URIRewriterImpl stack:"
                        + " Expected " + expectedRewriter
                        + " popped " + rewriter);
            }
        }
    }
}
