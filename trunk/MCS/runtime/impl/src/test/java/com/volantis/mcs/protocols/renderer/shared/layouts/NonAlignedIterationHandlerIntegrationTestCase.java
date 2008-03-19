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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class NonAlignedIterationHandlerIntegrationTestCase
        extends TestCaseAbstract {

    protected RuntimeDeviceLayout createRuntimeDeviceLayout()
        throws Exception {

        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());

        // Create the layout.
        builder.createLayout(LayoutType.CANVAS);

        // Push spatial
        builder.pushFormat(FormatType.SPATIAL_FORMAT_ITERATOR.getTypeName(), 0);
        builder.setAttribute(SpatialFormatIterator.ROW_STYLE_CLASSES, "r1 r2");
        builder.setAttribute(SpatialFormatIterator.COLUMN_STYLE_CLASSES, "c1 c2 c3");
        builder.attributesRead();

        // Push grid
        builder.pushFormat(FormatType.GRID.getTypeName(), 0);
        builder.setAttribute(Grid.COLUMNS_ATTRIBUTE, "3");
        builder.setAttribute(Grid.ROWS_ATTRIBUTE, "2");
        builder.attributesRead();

        // Push panes.
        for (int i = 0; i < 6; i += 1) {
            builder.pushFormat(FormatType.PANE.getTypeName(), i);
            builder.setAttribute(FormatConstants.NAME_ATTRIBUTE, "Pane" + i);
            builder.attributesRead();
             builder.popFormat();
        }

        // Pop grid
        builder.popFormat();

        // Pop spatial
        builder.popFormat();

        Layout layout = builder.getLayout();

        RuntimeDeviceLayout runtimeDeviceLayout = 
                RuntimeDeviceLayoutTestHelper.activate(layout);

        return runtimeDeviceLayout;
    }

    public void notestStyling()
            throws Exception {

        // Create and activate the runtime device layout.
        RuntimeDeviceLayout runtimeDeviceLayout = createRuntimeDeviceLayout();
        SpatialFormatIterator spatialFormatIterator = (SpatialFormatIterator)
                runtimeDeviceLayout.getRootFormat();

        // Initialise the device layout and page contexts.
        DeviceLayoutContext deviceLayoutContext = new DeviceLayoutContext();
//        deviceLayoutContext.setStylingFactory(StylingFactory.getDefaultInstance());
//        deviceLayoutContext.setFormatStylingEngine(
//                new FormatStylingEngineImpl(new StylingEngine() {
//
//            public void startElement(
//                    String namespace, String localName, Attributes attributes) {
//            }
//
//            public Styles getStyles() {
//                return null;
//            }
//
//            public void endElement(String namespace, String localName) {
//            }
//
//            public void pushStyleSheet(
//                    CompiledStyleSheet styleSheet, StyleSheetMerger merger) {
//            }
//
//            public void popStyleSheet(CompiledStyleSheet styleSheet) {
//            }
//
//                    public void pushPropertyValues(ImmutablePropertyValues propertyValues) {
//                    }
//
//                    public void popPropertyValues(ImmutablePropertyValues propertyValues) {
//                    }
//                }));

//        final DOMProtocol protocol = new DOMProtocol() {
//            public String defaultMimeType() {
//                return null;
//            }
//        };
//
//        MarinerPageContext marinerPageContext = new MarinerPageContext() {
//            public VolantisProtocol getProtocol() {
//                return protocol;
//            }
//        };
//        marinerPageContext.pushDeviceLayoutContext(deviceLayoutContext);
//        protocol.setMarinerPageContext(marinerPageContext);
//
//        deviceLayoutContext.setMarinerPageContext(marinerPageContext);
//        deviceLayoutContext.setDeviceLayout(runtimeDeviceLayout);
//        deviceLayoutContext.initialise();

//        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
//
//        FragmentLinkWriter fragmentLinkWriter = new DefaultFragmentLinkWriter(
//                deviceLayoutContext, marinerPageContext);
//        FormatRendererContext formatRendererContext =
//                new FormatRendererContextImpl(
//                        deviceLayoutContext, fragmentLinkWriter,
//                        stylingFactory);

//        NonAlignedIterationHandler handler = new NonAlignedIterationHandler(
//                formatRendererContext, spatialFormatIterator);

//        NDimensionalIndex index = new NDimensionalIndex(new int[0]);
//        SpatialFormatIteratorInstance instance =
//                new SpatialFormatIteratorInstance(index);
//        instance.setFormat(spatialFormatIterator);
//        instance.setDeviceLayoutContext(deviceLayoutContext);
//
//        NDimensionalIndex childIndex = index.addDimension();
//
//        LayoutAttributesFactory layoutAttributesFactory =
//                new LayoutAttributesFactoryImpl();
//
//        DeviceLayoutRenderer renderer =
//                new DeviceLayoutRenderer(layoutAttributesFactory);
//        renderer.renderLayout(deviceLayoutContext);

//        CoordinateConverter coordinateConverter =
//                new HorizontalCoordinateConverter(3, 2);
//
//        SpatialIterationParameters parameters = new SpatialIterationParameters(
//                childIndex, coordinateConverter);
//
//        SpatialIteratorProcessor processor =
//                new SpatialIteratorProcessor(layoutAttributesFactory);
//        processor.process(instance, formatRendererContext, handler, parameters);

//        Document document = protocol.getDocument();
//        DebugStyledDocument debugStyledDocument = new DebugStyledDocument(null);
//        String output = debugStyledDocument.debug(document);
//        System.out.println("Generated " + output);
//        assertXMLEquals("Document", "", output);
    }

    public void testDUMMY() {

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10449/4	geoff	VBM:2005110803 MCS35: Export issue with textAsset contradicts import & GUI and throws exception

 28-Nov-05	10449/1	geoff	VBM:2005110803 MCS35: Export issue with textAsset contradicts import & GUI and throws exception

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
