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
package com.volantis.mcs.dom2theme.impl;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.StyledDocumentLogger;
import com.volantis.mcs.dom2theme.StyledDOMStyleAttributeRenderer;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;

/**
 * Default implementation of {@link StyledDOMStyleAttributeRenderer}
 * Responsible for converting styles to 'style' attribute of element 
 */
public class DefaultStyledDOMStyleAttributeRenderer implements
        StyledDOMStyleAttributeRenderer {
    
    /**
     * Optimize styled document
     */
    private StyledDOMOptimizer optimizer;
    
    /**
     * Rewrite style as 'style' attribute value
     */
    private OutputStyledElementIteratee rewriter;
    
    /**
     * Default constructor with rewriter and optimizer
     * @param rewriter
     * @param optimizer
     */
    public DefaultStyledDOMStyleAttributeRenderer(
            OutputStyledElementIteratee rewriter,
            StyledDOMOptimizer optimizer){
        this.optimizer = optimizer;
        this.rewriter = rewriter;
    }
    

    // javadoc inherited
    public void renderStyleAttributes(Document styledDom) {
        StyledDocumentLogger.logDocument(styledDom);
        OutputStyledElementList elementList = optimizer.optimize(styledDom);        
        elementList.iterate(this.rewriter);
    }
    
}
