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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/MenuItemCollectorTestCaseAbstract.java,v 1.5 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 31-Mar-2003  Sumit       VBM:2003032714 - Abstract test for all element 
 *                          tests that implement MenuItemCollector
 * 22-Apr-2003  Allan       VBM:2003041604 - Modified to extend 
 *                          AbstractElementImplTestAbstract. Modified
 *                          doTestAddMenuItem to use createTestableElement
 *                          to get the element.
 * 22-Apr-2003  Allan       VBM:2003041506 - doTestAddMenuItem() updated to use
 *                          createTestablePAPIElement() - which is the new name
 *                          for createTestableElement().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.impl.AbstractElementImpl;
import com.volantis.mcs.papi.impl.AbstractElementImplTestAbstract;
import com.volantis.mcs.papi.impl.MenuItemElementImpl;
import com.volantis.mcs.protocols.MenuItem;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

/**
 * Abstract test for all element tests that implement MenuItemCollector
 */
abstract public class MenuItemCollectorTestCaseAbstract
    extends AbstractElementImplTestAbstract {
    
    /** 
     * The context required to be used in this test
     */
    protected MarinerRequestContext requestContext;
    protected TestMarinerPageContext pageContext;

    /**
     * @param collector
     */

    protected void setUp() throws Exception {
        super.setUp();
        requestContext = getRequestContext();
        pageContext = getPageContext();
        pageContext.setCurrentPane(new Pane(new CanvasLayout()));
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);
    }

    protected TestMarinerPageContext getPageContext() {
        return (new TestMarinerPageContext() {
            Stack privateStack = new Stack();
            public void popElement(PAPIElement expectedElement) {
                privateStack.pop();
            }

            public void pushElement(PAPIElement element) {
                privateStack.push(element);
            }
        });
        
    }


    protected MarinerRequestContext getRequestContext() {
        return new TestMarinerRequestContext();
    }

    protected void doTestAddMenuItem(PAPIAttributes gAttributes) {
        MenuItemElementImpl menuItemElement = new MenuItemElementImpl();
        MenuItemAttributes attributes =
            new com.volantis.mcs.papi.MenuItemAttributes();
        attributes.setHref("test://mytest");
        AbstractElementImpl element = (AbstractElementImpl)createTestablePAPIElement();
        try {
            element.elementStart(requestContext, gAttributes);
            pageContext.setCurrentElement(element);
            menuItemElement.elementStart(requestContext, attributes);
        } catch (PAPIException papE) {
            fail(papE.toString());
        }
        
        // Cast the element to a ProtocolAttributesContainer and get the protocol attributes
        // and cast those to a MenuItemCollector. 
        com.volantis.mcs.protocols.MenuItemCollector mAttrs =
            (com.volantis.mcs.protocols.MenuItemCollector) 
            ((MenuItemCollectorContainer)element).getMenuItemCollector();
        Collection c = mAttrs.getItems();
        Iterator itr = c.iterator();
        MenuItem menuItem = (MenuItem) itr.next();
        assertTrue(menuItem.getHref().equals("test://mytest"));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 ===========================================================================
*/
