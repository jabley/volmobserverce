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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/accessors/LayoutBuilderTestCase.java,v 1.2 2002/12/17 08:54:59 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Dec-02    Allan           VBM:2002120615 - Testcase for LayoutBuilder.
 * 10-Dec-02    Allan           VBM:2002121017 - FormatType mapping test moved
 *                              to FormatMapperTestCase.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.accessors;

import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.LayoutFactory;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.FormatTypeMapper;
import com.volantis.mcs.layouts.common.LayoutType;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class unit test the LayoutBuilderclass.
 */
public class LayoutBuilderTestCase
        extends TestCase {

    private LayoutFactory[] factories;
    private Map nameToClassMap;
    private HashMap nameToLayoutType;

    /**
     * Set up for tests.
     */
    public void setUp() {
        factories = new LayoutFactory[1];
        factories[0] = new RuntimeLayoutFactory();

        nameToClassMap = new HashMap(15);
        nameToClassMap.put("ColumnIteratorPane", ColumnIteratorPane.class);
        nameToClassMap.put("DissectingPane", DissectingPane.class);
        nameToClassMap.put("Form", Form.class);
        nameToClassMap.put("FormFragment", FormFragment.class);
        nameToClassMap.put("Fragment", Fragment.class);
        nameToClassMap.put("Grid", Grid.class);
        nameToClassMap.put("Pane", Pane.class);
        nameToClassMap.put("Region", Region.class);
        nameToClassMap.put("Replica", Replica.class);
        nameToClassMap.put("RowIteratorPane", RowIteratorPane.class);
        nameToClassMap.put("Segment", Segment.class);
        nameToClassMap.put("SegmentGrid", SegmentGrid.class);
        nameToClassMap.put("SpatialFormatIterator",
                           SpatialFormatIterator.class);
        nameToClassMap.put("TemporalFormatIterator",
                           TemporalFormatIterator.class);

        nameToLayoutType = new HashMap();
        nameToLayoutType.put("ColumnIteratorPane", LayoutType.CANVAS);
        nameToLayoutType.put("DissectingPane", LayoutType.CANVAS);
        nameToLayoutType.put("Form", LayoutType.CANVAS);
        nameToLayoutType.put("FormFragment", LayoutType.CANVAS);
        nameToLayoutType.put("Fragment", LayoutType.CANVAS);
        nameToLayoutType.put("Grid", LayoutType.CANVAS);
        nameToLayoutType.put("Pane", LayoutType.CANVAS);
        nameToLayoutType.put("Region", LayoutType.CANVAS);
        nameToLayoutType.put("Replica", LayoutType.CANVAS);
        nameToLayoutType.put("RowIteratorPane", LayoutType.CANVAS);
        nameToLayoutType.put("Segment", LayoutType.MONTAGE);
        nameToLayoutType.put("SegmentGrid", LayoutType.MONTAGE);
        nameToLayoutType.put("SpatialFormatIterator",
                           LayoutType.CANVAS);
        nameToLayoutType.put("TemporalFormatIterator",
                           LayoutType.CANVAS);
    }

    /**
     * Tear down set up items.
     */
    public void tearDown() {
        factories = null;
        nameToClassMap = null;
    }

    /**
     * This method tests the method public void createLayout ( String,String )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void testCreateLayout()
            throws Exception {

        for(int i=0; i<factories.length; i++) {
            LayoutFactory factory = factories[i];
            LayoutBuilder builder = new LayoutBuilder(factory);
            builder.createLayout(LayoutType.CANVAS);
            Layout layout = builder.getLayout();
            assertNotNull(layout);
        }

    }

    /**
     * This method tests the method public void pushFormat ( String,int )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void testPushFormat()
            throws Exception {

        for(int i=0; i<factories.length; i++) {
            LayoutFactory factory = factories[i];
            Iterator iterator = nameToClassMap.keySet().iterator();
            while(iterator.hasNext()) {
                String name = (String) iterator.next();
                LayoutBuilder builder = new LayoutBuilder(factory);
                builder.createLayout(getLayoutTypeForFormat(name));
                builder.pushFormat(name, 0);
                builder.setFormatAttribute(FormatConstants.NAME_ATTRIBUTE,
                                           "name");
                if("Grid".equals(name) || "SegmentGrid".equals(name)) {
                    builder.setFormatAttribute(Grid.COLUMNS_ATTRIBUTE, "1");
                    builder.setFormatAttribute(Grid.ROWS_ATTRIBUTE, "1");
                }
                builder.formatAttributesRead();
                Layout layout = builder.getLayout();
                Format root = layout.getRootFormat();
                assertEquals("Factory was " + factory + ": ",
                             nameToClassMap.get(name),
                             root.getClass());
                assertEquals("Factory was " + factory + ": ",
                             FormatTypeMapper.getFormatType(name),
                             root.getFormatType());
            }
        }
    }

    private LayoutType getLayoutTypeForFormat(String name) {
        return (LayoutType) nameToLayoutType.get(name);
    }

    /**
     * This method tests the method public void setAttribute ( String,String )
     * for the com.volantis.mcs.accessors.LayoutBuilder class. Specifically
     * this tests that setting the format type works properly
     */
    public void testSetAttributeForType()
            throws Exception {

        for(int i=0; i<factories.length; i++) {
            LayoutFactory factory = factories[i];
            Iterator iterator = nameToClassMap.keySet().iterator();
            while(iterator.hasNext()) {
                String name = (String) iterator.next();
                if("Layout".equals(name)) {
                    continue;
                }
                LayoutBuilder builder = new LayoutBuilder(factory);
                builder.createLayout(getLayoutTypeForFormat(name));
                builder.pushFormat(name, 0);
                builder.setFormatAttribute("type", name);
                Layout layout = builder.getLayout();
                Format root = layout.getRootFormat();
                assertEquals("Factory was " + factory + ": ",
                             FormatTypeMapper.getFormatType(name),
                             root.getFormatType());
            }
        }
    }

    public LayoutBuilderTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestConstructors() {
        //
        // Test public LayoutBuilder ( LayoutFactory ) constructor
        //
        Assert.fail("public LayoutBuilder ( LayoutFactory ) not tested.");
    }

    /**
     * This method tests the method public Layout getLayout ( )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestGetLayout()
            throws Exception {
        //
        // Test public Layout getLayout ( ) method.
        //
        Assert.fail("public Layout getLayout ( ) not tested.");
    }

    /**
     * This method tests the method public String getName ( )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestGetName()
            throws Exception {
        //
        // Test public String getName ( ) method.
        //
        Assert.fail("public String getName ( ) not tested.");
    }

    /**
     * This method tests the method public String getDeviceName ( )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestGetDeviceName()
            throws Exception {
        //
        // Test public String getDeviceName ( ) method.
        //
        Assert.fail("public String getDeviceName ( ) not tested.");
    }


    /**
     * This method tests the method public void createSubComponent ( String,int )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestCreateSubComponent()
            throws Exception {
        //
        // Test public void createSubComponent ( String,int ) method.
        //
        Assert.fail("public void createSubComponent ( String,int ) not tested.");
    }

    /**
     * This method tests the method public void popFormat ( )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestPopFormat()
            throws Exception {
        //
        // Test public void popFormat ( ) method.
        //
        Assert.fail("public void popFormat ( ) not tested.");
    }

    /**
     * This method tests the method public void attributesRead ( )
     * for the com.volantis.mcs.accessors.LayoutBuilder class.
     */
    public void notestAttributesRead()
            throws Exception {
        //
        // Test public void attributesRead ( ) method.
        //
        Assert.fail("public void attributesRead ( ) not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Feb-04	3203/1	ianw	VBM:2004022411 Removed GUI package from MCS

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
