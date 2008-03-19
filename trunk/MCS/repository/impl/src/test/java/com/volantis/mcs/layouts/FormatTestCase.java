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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatTestCase.java,v 1.3 2003/03/11 16:14:18 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 01-Nov-02    Allan           VBM:2002103107 - A testcase for Format.
 * 11-Mar-03    Chris W         VBM:2003031106 - Added testGetChildAt. Removed
 *                              notestGetChildAt.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class unit test the Formatclass.
 */
public class FormatTestCase
        extends TestCase {

    Format format;

    /**
     * Set up the layout for this testcase.
     */
    public void setUp() {
        format = new Pane(new CanvasLayout());
    }

    /**
     * Tear down everything that was set up.
     */
    public void tearDown() {
        format = null;
    }

    public FormatTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the method public void setName ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testSetName()
            throws Exception {

        format.setName("name");
        assertEquals("name", format.getName());
    }

    /**
     * This method tests the method public String getName ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testGetName()
            throws Exception {

        format.setName("name");
        assertEquals("name", format.getName());
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.layouts.Format class.
     */
    public void notestConstructors() {
        //
        // Test public Format ( int,Layout,FormatProperties ) constructor
        //
        Assert.fail(
                "public Format ( int,Layout,FormatProperties ) not tested.");
        //
        // Test public Format ( int,Layout ) constructor
        //
        Assert.fail("public Format ( int,Layout ) not tested.");
    }

    /**
     * This method tests the method public void removeDefaultAttributes ( Format )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestRemoveDefaultAttributes()
            throws Exception {
        //
        // Test public void removeDefaultAttributes ( Format ) method.
        //
        Assert.fail(
                "public void removeDefaultAttributes ( Format ) not tested.");
    }

//    /**
//     * This method tests the method public int hashCode ( )
//     * for the com.volantis.mcs.layouts.Format class.
//     */
//    public void notestHashCode()
//            throws Exception {
//        //
//        // Test public int hashCode ( ) method.
//        //
//        Assert.fail("public int hashCode ( ) not tested.");
//    }

//    /**
//     * This method tests the method public boolean equals ( Object )
//     * for the com.volantis.mcs.layouts.Format class.
//     */
//    public void notestEquals()
//            throws Exception {
//        //
//        // Test public boolean equals ( Object ) method.
//        //
//        Assert.fail("public boolean equals ( Object ) not tested.");
//    }

    /**
     * This method tests the method public String toString ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestToString()
            throws Exception {
        //
        // Test public String toString ( ) method.
        //
        Assert.fail("public String toString ( ) not tested.");
    }

    /**
     * This method tests the method public void setAttribute ( String,Object )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetAttribute()
            throws Exception {
        //
        // Test public void setAttribute ( String,Object ) method.
        //
        Assert.fail("public void setAttribute ( String,Object ) not tested.");
    }

    /**
     * This method tests the method public int getColumns ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetColumns()
            throws Exception {
        //
        // Test public int getColumns ( ) method.
        //
        Assert.fail("public int getColumns ( ) not tested.");
    }

    /**
     * This method tests the method public String getFormatType ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetFormatType()
            throws Exception {
        //
        // Test public String getFormatType ( ) method.
        //
        Assert.fail("public String getFormatType ( ) not tested.");
    }

    /**
     * This method tests the method public [Ljava.lang.String; getUserAttributes ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetUserAttributes()
            throws Exception {
        //
        // Test public [Ljava.lang.String; getUserAttributes ( ) method.
        //
        Assert.fail(
                "public [Ljava.lang.String; getUserAttributes ( ) not tested.");
    }

    /**
     * This method tests the method public [Ljava.lang.String; getPersistentAttributes ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetPersistentAttributes()
            throws Exception {
        //
        // Test public [Ljava.lang.String; getPersistentAttributes ( ) method.
        //
        Assert.fail(
                "public [Ljava.lang.String; getPersistentAttributes ( ) not tested.");
    }

    /**
     * This method tests the method public [Ljava.lang.String; getDefaultAttributes ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetDefaultAttributes()
            throws Exception {
        //
        // Test public [Ljava.lang.String; getDefaultAttributes ( ) method.
        //
        Assert.fail(
                "public [Ljava.lang.String; getDefaultAttributes ( ) not tested.");
    }

    /**
     * This method tests the method public void setInstance ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetInstance()
            throws Exception {
        //
        // Test public void setInstance ( int ) method.
        //
        Assert.fail("public void setInstance ( int ) not tested.");
    }

    /**
     * This method tests the method public int getInstance ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetInstance()
            throws Exception {
        //
        // Test public int getInstance ( ) method.
        //
        Assert.fail("public int getInstance ( ) not tested.");
    }

    /**
     * This method tests the method public int getNumChildren ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetNumChildren()
            throws Exception {
        //
        // Test public int getNumChildren ( ) method.
        //
        Assert.fail("public int getNumChildren ( ) not tested.");
    }

    /**
     * This method tests the method public void setDeviceLayout ( Layout )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetDeviceLayout()
            throws Exception {
        //
        // Test public void setDeviceLayout ( Layout ) method.
        //
        Assert.fail("public void setDeviceLayout ( Layout ) not tested.");
    }

    /**
     * This method tests the method public Layout getDeviceLayout ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetDeviceLayout()
            throws Exception {
        //
        // Test public Layout getDeviceLayout ( ) method.
        //
        Assert.fail("public Layout getDeviceLayout ( ) not tested.");
    }

    /**
     * This method tests the method public void setParent ( Format )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetParent()
            throws Exception {
        //
        // Test public void setParent ( Format ) method.
        //
        Assert.fail("public void setParent ( Format ) not tested.");
    }

    /**
     * This method tests the method public Format getParent ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetParent()
            throws Exception {
        //
        // Test public Format getParent ( ) method.
        //
        Assert.fail("public Format getParent ( ) not tested.");
    }

    /**
     * This method tests the method public Fragment getEnclosingFragment ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetEnclosingFragment()
            throws Exception {
        //
        // Test public Fragment getEnclosingFragment ( ) method.
        //
        Assert.fail("public Fragment getEnclosingFragment ( ) not tested.");
    }

    /**
     * This method tests the method public Fragment getFragment ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetFragment()
            throws Exception {
        //
        // Test public Fragment getFragment ( ) method.
        //
        Assert.fail("public Fragment getFragment ( ) not tested.");
    }

    /**
     * This method tests the method public Form getEnclosingForm ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetEnclosingForm()
            throws Exception {
        //
        // Test public Form getEnclosingForm ( ) method.
        //
        Assert.fail("public Form getEnclosingForm ( ) not tested.");
    }

    /**
     * This method tests the method public FormFragment getEnclosingFormFragment ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetEnclosingFormFragment()
            throws Exception {
        //
        // Test public FormFragment getEnclosingFormFragment ( ) method.
        //
        Assert.fail(
                "public FormFragment getEnclosingFormFragment ( ) not tested.");
    }

    /**
     * This method tests the method public Form getForm ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetForm()
            throws Exception {
        //
        // Test public Form getForm ( ) method.
        //
        Assert.fail("public Form getForm ( ) not tested.");
    }

    /**
     * This method tests the method public void setBorderWidth ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetBorderWidth()
            throws Exception {
        //
        // Test public void setBorderWidth ( String ) method.
        //
        Assert.fail("public void setBorderWidth ( String ) not tested.");
    }

    /**
     * This method tests the method public String getBorderWidth ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetBorderWidth()
            throws Exception {
        //
        // Test public String getBorderWidth ( ) method.
        //
        Assert.fail("public String getBorderWidth ( ) not tested.");
    }

    /**
     * This method tests the method public void setCellPadding ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetCellPadding()
            throws Exception {
        //
        // Test public void setCellPadding ( String ) method.
        //
        Assert.fail("public void setCellPadding ( String ) not tested.");
    }

    /**
     * This method tests the method public String getCellPadding ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetCellPadding()
            throws Exception {
        //
        // Test public String getCellPadding ( ) method.
        //
        Assert.fail("public String getCellPadding ( ) not tested.");
    }

    /**
     * This method tests the method public void setCellSpacing ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetCellSpacing()
            throws Exception {
        //
        // Test public void setCellSpacing ( String ) method.
        //
        Assert.fail("public void setCellSpacing ( String ) not tested.");
    }

    /**
     * This method tests the method public String getCellSpacing ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetCellSpacing()
            throws Exception {
        //
        // Test public String getCellSpacing ( ) method.
        //
        Assert.fail("public String getCellSpacing ( ) not tested.");
    }

    /**
     * This method tests the method public void setHorizontalAlignment ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetHorizontalAlignment()
            throws Exception {
        //
        // Test public void setHorizontalAlignment ( String ) method.
        //
        Assert.fail(
                "public void setHorizontalAlignment ( String ) not tested.");
    }

    /**
     * This method tests the method public String getHorizontalAlignment ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetHorizontalAlignment()
            throws Exception {
        //
        // Test public String getHorizontalAlignment ( ) method.
        //
        Assert.fail("public String getHorizontalAlignment ( ) not tested.");
    }

    /**
     * This method tests the method public void setVerticalAlignment ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetVerticalAlignment()
            throws Exception {
        //
        // Test public void setVerticalAlignment ( String ) method.
        //
        Assert.fail("public void setVerticalAlignment ( String ) not tested.");
    }

    /**
     * This method tests the method public String getVerticalAlignment ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetVerticalAlignment()
            throws Exception {
        //
        // Test public String getVerticalAlignment ( ) method.
        //
        Assert.fail("public String getVerticalAlignment ( ) not tested.");
    }

    /**
     * This method tests the method public void setHeight ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetHeight()
            throws Exception {
        //
        // Test public void setHeight ( String ) method.
        //
        Assert.fail("public void setHeight ( String ) not tested.");
    }

    /**
     * This method tests the method public String getHeight ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetHeight()
            throws Exception {
        //
        // Test public String getHeight ( ) method.
        //
        Assert.fail("public String getHeight ( ) not tested.");
    }

    /**
     * This method tests the method public void setWidth ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetWidth()
            throws Exception {
        //
        // Test public void setWidth ( String ) method.
        //
        Assert.fail("public void setWidth ( String ) not tested.");
    }

    /**
     * This method tests the method public String getWidth ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetWidth()
            throws Exception {
        //
        // Test public String getWidth ( ) method.
        //
        Assert.fail("public String getWidth ( ) not tested.");
    }

    /**
     * This method tests the method public void setWidthUnits ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetWidthUnits()
            throws Exception {
        //
        // Test public void setWidthUnits ( String ) method.
        //
        Assert.fail("public void setWidthUnits ( String ) not tested.");
    }

    /**
     * This method tests the method public String getWidthUnits ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetWidthUnits()
            throws Exception {
        //
        // Test public String getWidthUnits ( ) method.
        //
        Assert.fail("public String getWidthUnits ( ) not tested.");
    }

    /**
     * This method tests the method public String getBackgroundComponent ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetBackgroundComponent()
            throws Exception {
        //
        // Test public String getBackgroundComponent ( ) method.
        //
        Assert.fail("public String getBackgroundComponent ( ) not tested.");
    }

    /**
     * This method tests the method public void setBackgroundComponent ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetBackgroundComponent()
            throws Exception {
        //
        // Test public void setBackgroundComponent ( String ) method.
        //
        Assert.fail(
                "public void setBackgroundComponent ( String ) not tested.");
    }

    /**
     * This method tests the method public String getBackgroundComponentType ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetBackgroundComponentType()
            throws Exception {
        //
        // Test public String getBackgroundComponentType ( ) method.
        //
        Assert.fail("public String getBackgroundComponentType ( ) not tested.");
    }

    /**
     * This method tests the method public void setBackgroundComponentType ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetBackgroundComponentType()
            throws Exception {
        //
        // Test public void setBackgroundComponentType ( String ) method.
        //
        Assert.fail(
                "public void setBackgroundComponentType ( String ) not tested.");
    }

    /**
     * This method tests the method public void attributesHaveBeenSet ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestAttributesHaveBeenSet()
            throws Exception {
        //
        // Test public void attributesHaveBeenSet ( ) method.
        //
        Assert.fail("public void attributesHaveBeenSet ( ) not tested.");
    }

    /**
     * This method tests the method public void childrenHaveBeenCreated ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestChildrenHaveBeenCreated()
            throws Exception {
        //
        // Test public void childrenHaveBeenCreated ( ) method.
        //
        Assert.fail("public void childrenHaveBeenCreated ( ) not tested.");
    }

    /**
     * This method tests the method public SimpleAttributeContainer createSubComponent ( String,int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestCreateSubComponent()
            throws Exception {
        //
        // Test public SimpleAttributeContainer createSubComponent ( String,int ) method.
        //
        Assert.fail(
                "public SimpleAttributeContainer createSubComponent ( String,int ) not tested.");
    }

    /**
     * This method tests the method public void subComponentInitialised ( SimpleAttributeContainer )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSubComponentInitialised()
            throws Exception {
        //
        // Test public void subComponentInitialised ( SimpleAttributeContainer ) method.
        //
        Assert.fail(
                "public void subComponentInitialised ( SimpleAttributeContainer ) not tested.");
    }

    /**
     * This method tests the method public Enumeration getSubComponentInfo ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetSubComponentInfo()
            throws Exception {
        //
        // Test public Enumeration getSubComponentInfo ( ) method.
        //
        Assert.fail("public Enumeration getSubComponentInfo ( ) not tested.");
    }

    /**
     * This method tests the method public void setChildAt ( Format,int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestSetChildAt()
            throws Exception {
        //
        // Test public void setChildAt ( Format,int ) method.
        //
        Assert.fail("public void setChildAt ( Format,int ) not tested.");
    }

    /**
     * This method tests the method public Format getChildAt ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testGetChildAt()
            throws Exception {
        SpatialFormatIterator sfi = new SpatialFormatIterator(
                new CanvasLayout());
        format.setParent(sfi);

        try {
            sfi.setChildAt(format, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        assertSame("wrong child", format, sfi.getChildAt(0));
        try {
            assertNull("child 1 should be null", sfi.getChildAt(1));
            assertNull("child 2 should be null", sfi.getChildAt(2));
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("should not throw ArrayIndexOutOfBoundsException "
                 + e.getMessage());
        }
    }

    /**
     * This method tests the method public void insertChildAt ( Format,int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestInsertChildAt()
            throws Exception {
        //
        // Test public void insertChildAt ( Format,int ) method.
        //
        Assert.fail("public void insertChildAt ( Format,int ) not tested.");
    }

    /**
     * This method tests the method public void emptyChildAt ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestEmptyChildAt()
            throws Exception {
        //
        // Test public void emptyChildAt ( int ) method.
        //
        Assert.fail("public void emptyChildAt ( int ) not tested.");
    }

    /**
     * This method tests the method public void removeChildAt ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestRemoveChildAt()
            throws Exception {
        //
        // Test public void removeChildAt ( int ) method.
        //
        Assert.fail("public void removeChildAt ( int ) not tested.");
    }

    /**
     * This method tests the method public boolean visit ( FormatVisitor,Object )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestVisit()
            throws Exception {
        //
        // Test public boolean visit ( FormatVisitor,Object ) method.
        //
        Assert.fail(
                "public boolean visit ( FormatVisitor,Object ) not tested.");
    }

    /**
     * This method tests the method public boolean visitChildren ( FormatVisitor,Object )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestVisitChildren()
            throws Exception {
        //
        // Test public boolean visitChildren ( FormatVisitor,Object ) method.
        //
        Assert.fail(
                "public boolean visitChildren ( FormatVisitor,Object ) not tested.");
    }

    /**
     * This method tests the method public [Ljava.util.List; getAttributeGroupings ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void notestGetAttributeGroupings()
            throws Exception {
        //
        // Test public [Ljava.util.List; getAttributeGroupings ( ) method.
        //
        Assert.fail(
                "public [Ljava.util.List; getAttributeGroupings ( ) not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
