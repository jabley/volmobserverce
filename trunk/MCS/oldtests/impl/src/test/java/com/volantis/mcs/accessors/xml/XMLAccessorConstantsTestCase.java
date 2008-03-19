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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/accessors/xml/XMLAccessorConstantsTestCase.java,v 1.2 2002/08/28 15:07:21 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Aug-02    Ian             VBM:2002081303 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.accessors.xml;

import junit.framework.*;

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;

/**
 * This class unit tests the XMLACCESSORConstants class.
 */
public class XMLAccessorConstantsTestCase
    extends TestCase {

  public XMLAccessorConstantsTestCase(String name) {
    super(name);
  }

  /**
   * This method tests the constructors for
   * the XMLAccessorConstants class.
   */
  public void testConstructors() {
  }

  public void testConvertibleImageAssetElement() {
    Assert.assertEquals("public XMLAccessorConstants.CONVERTIBLE_IMAGE_ASSET_ELEMENT incorrect value.",
                        XMLAccessorConstants.CONVERTIBLE_IMAGE_ASSET_ELEMENT,
                        "convertibleImageAsset");
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/1	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
