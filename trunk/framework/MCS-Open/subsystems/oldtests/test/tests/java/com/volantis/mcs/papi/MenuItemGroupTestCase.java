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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/MenuItemGroupTestCase.java,v 1.5 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 31-Mar-2003  Sumit       VBM:2003032714 - Tests the new MenuItemGroup 
 *                          element
 * 22-Apr-2003  Allan       VBM:2003041604 - Changed createTestableElement() to
 *                          return a PAPIElement.
 * 22-Apr-2003  Allan       VBM:2003041506 - Renamed createTestableElement() to
 *                          createTestablePAPIElement().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;


/**
 * Tests the functionality of the MnueItemGroup papi element
 */
public class MenuItemGroupTestCase extends MenuItemCollectorTestCaseAbstract {

    public void testAddMenuItem() {
//todo: uncomment this test, or repair it, or defunct it after enhanced menus has been fully implemented
/*
        MenuItemGroupAttributes gAttributes =
            new com.volantis.mcs.papi.MenuItemGroupAttributes();
        gAttributes.setId("testid");
        gAttributes.setStyleClass("myClass");
        pageContext.setCurrentElement(new MenuElement());
        super.doTestAddMenuItem(gAttributes);
*/
    }

    public void testElementStart() {
//todo: uncomment this test, or repair it, or defunct it after enhanced menus has been fully implemented
/*
        MenuItemGroupElement element = new MenuItemGroupElement();
        MenuItemGroupAttributes attributes =
            new com.volantis.mcs.papi.MenuItemGroupAttributes();
        attributes.setId("testid");
        attributes.setStyleClass("myClass");
        pageContext.setCurrentElement(new MenuElement());
        try {
            element.elementStart(requestContext, attributes);
        } catch (PAPIException papE) {
            fail(papE.toString());
        }
        com.volantis.mcs.protocols.MenuItemGroupAttributes mAttrs =
            (com.volantis.mcs.protocols.MenuItemGroupAttributes) element
                .getMenuItemCollector();
        assertTrue("myClass".equals(mAttrs.getStyleClass()));
*/
    }

    protected PAPIElement createTestablePAPIElement() {
        return new MenuItemGroupElement();
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
