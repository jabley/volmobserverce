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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/MenuTestCase.java,v 1.3 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 31-Mar-2003  Sumit       VBM:2003032714
 * 22-Apr-2003  Allan       VBM:2003041604 - Changed createTestableElement to
 *                          to return a PAPIElement.
 * 22-Apr-2003  Allan       VBM:2003041506 - Renamed createTestableElement() to
 *                          createTestablePAPIElement().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import com.volantis.mcs.context.TestMarinerPageContext;

/**
 * Tests the Menu PAPI element.
 */
public class MenuTestCase extends MenuItemCollectorTestCaseAbstract {

    public void testAddMenuItem() {
//todo: uncomment this test, or repair it, or defunct it after enhanced menus has been fully implemented
/*
        MenuAttributes gAttributes =
            new com.volantis.mcs.papi.MenuAttributes();
        gAttributes.setId("testid");
        gAttributes.setStyleClass("myClass");
        super.doTestAddMenuItem(gAttributes);
*/        
    }

    /**
     * This test needs to override the MarinerPageContext.pushElement method to
     * do nothing. If put in the main TestMarinerPageContext class this might
     * break another test elsewhere
     */
    class MenuTestCaseMarinerPageContext extends TestMarinerPageContext {
        public void pushElement (PAPIElement element) {
            //do nothing
        }
    };
     
    protected TestMarinerPageContext getPageContext() {
        return new MenuTestCaseMarinerPageContext();
    }

    protected PAPIElement createTestablePAPIElement() {
        return new MenuElement();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
