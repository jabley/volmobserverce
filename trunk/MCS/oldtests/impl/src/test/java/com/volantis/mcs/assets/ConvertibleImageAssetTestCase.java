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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/assets/ConvertibleImageAssetTestCase.java,v 1.3 2002/09/09 11:08:04 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Aug-02    Ian             VBM:2002081303 - Created.
 * 06-Sep-02    Ian             VBM:2002081307 - Changed tests to reflect 
 *                              change in identity for ConvertibleImageAsset.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.assets;

import junit.framework.*;

import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.ConvertibleImageAssetIdentity;
import java.lang.Object;
import java.lang.String;

public class ConvertibleImageAssetTestCase
    extends TestCase {

  public ConvertibleImageAssetTestCase(String name) {
    super(name);
  }

  /**
   * This method tests the constructors for
   * the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testConstructors() {

    //
    // Test public ConvertibleImageAsset(ConvertibleImageAssetIdentity) constructor
    //
    String initialName="MyAsset";
    int initialX=100;
    int initialY=200;
    int initialDepth=32;
    int initialRendering=ConvertibleImageAsset.COLOR;
    int initialEncoding=ConvertibleImageAsset.JPEG;
    String initialAssetGroup="MyAssetGroup";
    String initialValue="MyValue";

    ConvertibleImageAssetIdentity convertibleImageAssetIdentity =
        new ConvertibleImageAssetIdentity(initialName);
    
    ConvertibleImageAsset convertibleImageAsset=new ConvertibleImageAsset(convertibleImageAssetIdentity);

    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(com.volantis.mcs.assets.ConvertibleImageAssetIdentity) name not equal.",
                        initialName,
                        convertibleImageAsset.getName());
    
    //
    // Test public ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) constructor
    //
    convertibleImageAsset=new ConvertibleImageAsset(initialName,
        initialX,
        initialY,
        initialDepth,
        initialRendering,
        initialEncoding,
        initialAssetGroup,
        initialValue);
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) name not equal.",
                        initialName,
                        convertibleImageAsset.getName());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) pixelsX not equal.",
                        initialX,
                        convertibleImageAsset.getPixelsX());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) pixelsY not equal.",
                        initialY,
                        convertibleImageAsset.getPixelsY());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) pixelDepth not equal.",
                        initialDepth,
                        convertibleImageAsset.getPixelDepth());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) rendering not equal.",
                        initialRendering,
                        convertibleImageAsset.getRendering());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) encoding not equal.",
                        initialEncoding,
                        convertibleImageAsset.getEncoding());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) encoding not equal.",
                        initialAssetGroup,
                        convertibleImageAsset.getAssetGroupName());
    Assert.assertEquals("public com.volantis.mcs.assets.ConvertibleImageAsset(java.lang.String,int,int,int,int,int,java.lang.String,java.lang.String) encoding not equal.",
                        initialValue,
                        convertibleImageAsset.getValue());
    //
    // Test public ConvertibleImageAsset(String) constructor
    //
    convertibleImageAsset=new ConvertibleImageAsset(initialName);

    Assert.assertEquals("public ConvertibleImageAsset(String) name not equal.",
                        initialName,
                        convertibleImageAsset.getName());
    Assert.assertEquals("public ConvertibleImageAsset(java.lang.String) rendering not default.",
                        ConvertibleImageAsset.COLOR,
                        convertibleImageAsset.getRendering());
    Assert.assertEquals("public ConvertibleImageAsset(java.lang.String) encoding not default.",
                        ConvertibleImageAsset.BMP,
                        convertibleImageAsset.getEncoding());
    //
    // Test public com.volantis.mcs.assets.ConvertibleImageAsset() constructor
    //
    //Assert.fail("public com.volantis.mcs.assets.ConvertibleImageAsset() not tested.");
  }

  /**
   * This method tests the method equals
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testEquals()
      throws Exception {

    //
    // Test public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) method.
    //
    String initialName="MyAsset";
    int initialX=100;
    int initialY=200;
    int initialDepth=32;
    int initialRendering=ConvertibleImageAsset.COLOR;
    int initialEncoding=ConvertibleImageAsset.JPEG;
    String initialAssetGroup="MyAssetGroup";
    String initialValue="MyValue";

    ConvertibleImageAsset asset1=new ConvertibleImageAsset(initialName,
        initialX,
        initialY,
        initialDepth,
        initialRendering,
        initialEncoding,
        initialAssetGroup,
        initialValue);

    ConvertibleImageAsset asset2=new ConvertibleImageAsset(initialName,
        initialX,
        initialY,
        initialDepth,
        initialRendering,
        initialEncoding,
        initialAssetGroup,
        initialValue);

    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (1).",
                      asset1.equals(asset2));
    asset2.setName("ardvark");
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) name is equal.",
                      !asset1.equals(asset2));
    asset2.setName(initialName);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (2).",
                      asset1.equals(asset2));
    asset2.setPixelsX(50);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) pixelsX is equal.",
                      !asset1.equals(asset2));
    asset2.setPixelsX(initialX);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (3).",
                      asset1.equals(asset2));
    asset2.setPixelsY(50);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) pixelsY is equal.",
                      !asset1.equals(asset2));
    asset2.setPixelsY(initialY);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (4).",
                      asset1.equals(asset2));
    asset2.setPixelDepth(4);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) pixelDepth is equal.",
                      !asset1.equals(asset2));
    asset2.setPixelDepth(initialDepth);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (5).",
                      asset1.equals(asset2));
    asset2.setRendering(ConvertibleImageAsset.MONOCHROME);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) rendering is equal.",
                      !asset1.equals(asset2));
    asset2.setRendering(initialRendering);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (6).",
                      asset1.equals(asset2));
    asset2.setEncoding(ConvertibleImageAsset.PNG);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) encoding is equal.",
                      !asset1.equals(asset2));
    asset2.setEncoding(initialEncoding);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (7).",
                      asset1.equals(asset2));
    asset2.setAssetGroupName("ardvark");
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) assetGroup is equal.",
                      !asset1.equals(asset2));
    asset2.setAssetGroupName(initialAssetGroup);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (8).",
                      asset1.equals(asset2));
    asset2.setValue("ardvark");
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) value is equal.",
                      !asset1.equals(asset2));
    asset2.setValue(initialValue);
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) not equal (9).",
                      asset1.equals(asset2));
    Assert.assertTrue("public boolean com.volantis.mcs.assets.ConvertibleImageAsset.equals(java.lang.Object) alien object  is equal.",
                      !asset1.equals(new Object()));

  }

  /**
   * This method tests the method setPixelsX
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testSetPixelsX()
      throws Exception {
    //
    // Test public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelsX(int) method.
    //

    ConvertibleImageAsset convertibleImageAsset=new
        ConvertibleImageAsset("Ardvark");
    convertibleImageAsset.setPixelsX(732);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelsX(int) get not equal to set.",
                        732,
                        convertibleImageAsset.getPixelsX());
  }

  /**
   * This method tests the method setPixelsY
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testSetPixelsY()
      throws Exception {
    //
    // Test public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelsY(int) method.
    //
    ConvertibleImageAsset convertibleImageAsset=new
        ConvertibleImageAsset("Ardvark");
    convertibleImageAsset.setPixelsY(732);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelsY(int) get not equal to set.",
                        732,
                        convertibleImageAsset.getPixelsY());
  }

  /**
   * This method tests the method setPixelDepth
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testSetPixelDepth()
      throws Exception {
    //
    // Test public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelDepth(int) method.
    //
    ConvertibleImageAsset convertibleImageAsset=new
        ConvertibleImageAsset("Ardvark");
    convertibleImageAsset.setPixelDepth(732);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setPixelDepth(int) get not equal to set.",
                        732,
                        convertibleImageAsset.getPixelDepth());
  }

  /**
   * This method tests the method setEncoding
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testSetEncoding()
      throws Exception {
    //
    // Test public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) method.
    //
    ConvertibleImageAsset convertibleImageAsset=new
        ConvertibleImageAsset("Ardvark");
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.BMP);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (1).",
                        ConvertibleImageAsset.BMP,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.GIF);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (2).",
                        ConvertibleImageAsset.GIF,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.JPEG);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (3).",
                        ConvertibleImageAsset.JPEG,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.PJPEG);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (4).",
                        ConvertibleImageAsset.PJPEG,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.PNG);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (5).",
                        ConvertibleImageAsset.PNG,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.WBMP);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (6).",
                        ConvertibleImageAsset.WBMP,
                        convertibleImageAsset.getEncoding());
    convertibleImageAsset.setEncoding(ConvertibleImageAsset.INVALID_ENCODING);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setEncoding(int) get not equal to set (7).",
                        ConvertibleImageAsset.INVALID_ENCODING,
                        convertibleImageAsset.getEncoding());

  }

  /**
   * This method tests the method setRendering
   * for the com.volantis.mcs.assets.ConvertibleImageAsset class.
   */
  public void testSetRendering()
      throws Exception {
    //
    // Test public void com.volantis.mcs.assets.ConvertibleImageAsset.setRendering(int) method.
    //
    ConvertibleImageAsset convertibleImageAsset=new
        ConvertibleImageAsset("Ardvark");
    convertibleImageAsset.setRendering(ConvertibleImageAsset.COLOR);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setRendering(int) get not equal to set (1).",
                        ConvertibleImageAsset.COLOR,
                        convertibleImageAsset.getRendering());
    convertibleImageAsset.setRendering(ConvertibleImageAsset.MONOCHROME);
    Assert.assertEquals("public void com.volantis.mcs.assets.ConvertibleImageAsset.setRendering(int) get not equal to set (2).",
                        ConvertibleImageAsset.MONOCHROME,
                        convertibleImageAsset.getRendering());
    try {
      convertibleImageAsset.setRendering(732);
      Assert.fail("public void com.volantis.mcs.assets.ConvertibleImageAsset.setRendering(int) illegal argument not thrown.");
    } catch (IllegalArgumentException e) {

    }
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
