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

package com.volantis.mcs.jsp.tags;

import com.volantis.mcs.servlet.MarinerServletRequestContext;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * Tag that provides access to the <code>marinerRequestContext</code> variable
 * just as the old canvas tag did.
 */
public class PageTag
        extends TagSupport
        implements TryCatchFinally {

    /**
     * The context that was created for use within the body of this tag.
     */
    private MarinerServletRequestContext context;

    /**
     * Includes body in out stream.
     *
     * @return {@link #EVAL_BODY_INCLUDE}
     */
    public int doStartTag() throws JspException {
        // Create and initialise a MarinerJspRequestContext for the current
        // request.
        try {
            ServletRequest request = pageContext.getRequest();
            context = new MarinerServletRequestContext(
                    pageContext.getServletContext(), request,
                    pageContext.getResponse());

            // Store the context in the PageContext so it can be accessed from
            // inside the page.
            pageContext.setAttribute("marinerRequestContext", context);

        } catch (Exception e) {
            JspTagException wrapper = new JspTagException(
                    "Could not create request context");
            wrapper.initCause(e);
            throw wrapper;
        }

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Rethrows throwable.
     *
     * @param throwable The throwable to throw.
     * @throws Throwable that was supplied.
     */
    public void doCatch(Throwable throwable) throws Throwable {
        throw throwable;
    }

    /**
     * Finally ensure that the context is released.
     */
    public void doFinally() {
        if (context != null) {
            context.release();
            context = null;
        }
    }
}
