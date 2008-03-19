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
 * $Header: /src/voyager/testsuite/integration/com/volantis/integration/mcs/protocols/voicexml/VolantisProtocolIntegrationTestAbstract.java,v 1.2 2003/04/30 08:35:40 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Allan           VBM:2003040303 - Created. Abstract super class
 *                              for all protocol integration testcases.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException to
 *                              MethodInvoker.invoke() declaration.
 * 17-Apr-03    Byron           VBM:2003032608 - Temporarily remove the test
 *                              case(s).
 * 17-Apr-03    Byron           VBM:2003040302 - Fleshed out numerous test
 *                              cases obtained from VolantisProtocol. Test case
 *                              place holders for non-write methods added too.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * 29-Apr-03    Byron           VBM:2003042812 - Removed commented out code and
 *                              added getExpectedDoImplicitValueResult.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.integration.mcs.protocols.voicexml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AltTextAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.CustomMarkupAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.DivideHintAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.MontageAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolIntegrationTestHelper;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.SubscriptAttributes;
import com.volantis.mcs.protocols.SuperscriptAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the mother of all Volantis Protocol integration tests.
 *
 * @todo the names of the testWriteOpenXXX (and their associated
 *            getXXXExpectedResult) methods should be renamed to
 *            testWriteOpenCloseXXX since they test the open and close
 *            operation in one test.
 */
public abstract class VolantisProtocolIntegrationTestAbstract
        extends TestCaseAbstract {

    /**
     * Default property values defined to avoid setting illegal property values
     * during <code>provideAttributes</code>.
     */
    private static final Map DEFAULT_PROPERTY_VALUES_MAP = new HashMap();

    static {
        DEFAULT_PROPERTY_VALUES_MAP.put("align", "center");
        DEFAULT_PROPERTY_VALUES_MAP.put("bgColor", "transparent");
        DEFAULT_PROPERTY_VALUES_MAP.put("height", "auto");
        DEFAULT_PROPERTY_VALUES_MAP.put("noWrap", "nowrap");
        DEFAULT_PROPERTY_VALUES_MAP.put("VAlign", "baseline");
        DEFAULT_PROPERTY_VALUES_MAP.put("width", "auto");
    }

    protected InternalDevice internalDevice;


    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();
    }

    /**
     * @see ProtocolIntegrationTestHelper#provideAttributes(java.lang.Class)
     */
    public final static MCSAttributes provideAttributes(
            final Class attributesClass)
            throws InstantiationException, IllegalAccessException,
                    InvocationTargetException {

        return ProtocolIntegrationTestHelper.provideAttributes(attributesClass,
                StylesBuilder.getDeprecatedStyles(), DEFAULT_PROPERTY_VALUES_MAP);
    }

    /**
     * Get the protocol that will be tested.
     * @return The protocol to test.
     */
    protected abstract VolantisProtocol getProtocol();

    /**
     * Return an allocated output buffer for the given protocol.
     *
     * @param  protocol the current protocol.
     * @return          an allocated output buffer for the given protocol.
     */
    private OutputBuffer getOutputBuffer(VolantisProtocol protocol) {
        return protocol.getOutputBufferFactory().createOutputBuffer();
    }

    /**
     * This method tests the method public void writeOpenBody ( BodyAttributes
     * ) for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenBody() throws Exception {
        final BodyAttributes attributes =
                (BodyAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(BodyAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenBody(attributes);
                protocol.writeCloseBody(attributes);
            }
        };
        String expecting = getExpectedWriteOpenBodyResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }


    /**
     * This method tests the method public void writeOpenCanvas (
     * CanvasAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenCanvas() throws Exception {
        final CanvasAttributes attributes =
                (CanvasAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(CanvasAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenCanvas(attributes);
            }
        };
        String expecting = getExpectedWriteOpenCanvasResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenInclusion (
     * CanvasAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     * <p>
     * NOTE: write Close removed
     */
    public void testWriteOpenInclusion() throws Exception {
        final CanvasAttributes attributes =
                (CanvasAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(CanvasAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenInclusion(attributes);
                protocol.writeCloseInclusion(attributes);
            }
        };
        String expecting = getExpectedWriteOpenInclusionResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenMontage ( MontageAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenMontage() throws Exception {
        final MontageAttributes attributes =
                (MontageAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(MontageAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenMontage(attributes);
                protocol.writeCloseMontage(attributes);
            }
        };
        String expecting = getExpectedWriteOpenMontageResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeProtocolString ( )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void notestWriteProtocolString() throws Exception {
        final VolantisProtocol protocol = getProtocol();

        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        final Document document = domFactory.createDocument();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() {
                protocol.writeProtocolString(document);
            }
        };
        String expecting = getExpectedWriteProtocolStringResult();
        ProtocolIntegrationTestHelper.doTestWithoutUsingDOMComparison(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenStyle ( StyleAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenStyle() throws Exception {
        final StyleAttributes attributes =
                (StyleAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(StyleAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() {
                protocol.writeOpenStyle(getOutputBuffer(protocol), attributes);
                protocol.writeCloseStyle(getOutputBuffer(protocol), attributes);
            }
        };
        String expecting = getExpectedWriteOpenStyleResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSlide ( SlideAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSlide() throws Exception {
        final SlideAttributes attributes =
                (SlideAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SlideAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() {
                protocol.writeOpenSlide(attributes);
                protocol.writeCloseSlide(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSlideResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSpatialFormatIterator
     * ( SpatialFormatIteratorAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSpatialFormatIterator() throws Exception {
        final SpatialFormatIteratorAttributes attributes =
                (SpatialFormatIteratorAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SpatialFormatIteratorAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSpatialFormatIterator(attributes);
                protocol.writeCloseSpatialFormatIterator(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSpatialFormatIteratorResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void
     * writeOpenSpatialFormatIteratorRow ( SpatialFormatIteratorAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSpatialFormatIteratorRow() throws Exception {
        final SpatialFormatIteratorAttributes attributes =
                (SpatialFormatIteratorAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SpatialFormatIteratorAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSpatialFormatIteratorRow(attributes);
                protocol.writeCloseSpatialFormatIteratorRow(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSpatialFormatIteratorRowResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void
     * writeOpenSpatialFormatIteratorChild ( SpatialFormatIteratorAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSpatialFormatIteratorChild() throws Exception {
        final SpatialFormatIteratorAttributes attributes =
                (SpatialFormatIteratorAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SpatialFormatIteratorAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSpatialFormatIteratorChild(attributes);
                protocol.writeCloseSpatialFormatIteratorChild(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSpatialFormatIteratorChildResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenColumnIteratorPane (
     * ColumnIteratorPaneAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     * <p>
     * NOTE: Close method removed.
     */
    public void testWriteOpenColumnIteratorPane() throws Exception {
        final ColumnIteratorPaneAttributes attributes =
                (ColumnIteratorPaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ColumnIteratorPaneAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        attributes.setPane(new Pane(new CanvasLayout()));

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenColumnIteratorPane(attributes);
                protocol.writeCloseColumnIteratorPane(attributes);
            }
        };
        String expecting = getExpectedWriteOpenColumnIteratorPaneResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void
     * writeOpenColumnIteratorPaneElement ( ColumnIteratorPaneAttributes ) for
     * the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenColumnIteratorPaneElement() throws Exception {
        final ColumnIteratorPaneAttributes attributes =
                (ColumnIteratorPaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ColumnIteratorPaneAttributes.class);

        attributes.setPane(new Pane(new CanvasLayout()));
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenColumnIteratorPaneElement(attributes);
                protocol.writeCloseColumnIteratorPaneElement(attributes);
            }
        };
        String expecting =
                getExpectedWriteOpenColumnIteratorPaneElementResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void
     * writeColumnIteratorPaneElementContents ( OutputBuffer ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteColumnIteratorPaneElementContents() throws Exception {

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeColumnIteratorPaneElementContents(
                        getOutputBuffer(protocol));
            }
        };
        String expecting = getExpectedWriteColumnIteratorPaneElementContentsResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenDissectingPane (
     * DissectingPaneAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenDissectingPane() throws Exception {
        final DissectingPaneAttributes attributes =
                (DissectingPaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DissectingPaneAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenDissectingPane(attributes);
                protocol.writeCloseDissectingPane(attributes);
            }
        };
        String expecting = getExpectedWriteOpenDissectingPaneResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenForm ( FormAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     *
     * NOTE: Close method removed.
     */
    public void testWriteOpenForm() throws Exception {
        final FormAttributes attributes =
                (FormAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(FormAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                if (protocol instanceof DOMProtocol) {
                    MarinerPageContext context = protocol.getMarinerPageContext();
                    FormInstance formInstance = (FormInstance) context.
                            getFormatInstance(attributes.getForm(), NDimensionalIndex.ZERO_DIMENSIONS);
                    ((DOMOutputBuffer) formInstance.getContentBuffer(true)).
                            saveInsertionPoint();
                }
                protocol.writeOpenForm(attributes);
                protocol.writeCloseForm(attributes);
            }
        };
        String expecting = getExpectedWriteOpenFormResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeFormPreamble (
     * OutputBuffer ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteFormPreamble() throws Exception {
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeFormPreamble(getOutputBuffer(protocol));
            }
        };
        String expecting = getExpectedWriteFormPreambleResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeFormPostamble (
     * OutputBuffer ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteFormPostamble() throws Exception {
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeFormPostamble(getOutputBuffer(protocol));
            }
        };
        String expecting = getExpectedWriteFormPostambleResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenGrid (GridAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenGrid() throws Exception {
        final GridAttributes attributes =
                (GridAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(GridAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenGrid(attributes);
                protocol.writeCloseGrid(attributes);
            }
        };
        String expecting = getExpectedWriteOpenGridResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

     /**
     * This method tests the method public void writeOpenGridChild ( GridChildAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenGridChild() throws Exception {
        final GridChildAttributes attributes =
                (GridChildAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(GridChildAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenGridChild(attributes);
                protocol.writeCloseGridChild(attributes);
            }
        };
        String expecting = getExpectedWriteOpenGridChildResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

     /**
     * This method tests the method public void writeOpenGridRow (
     * GridRowAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenGridRow() throws Exception {
        final GridRowAttributes attributes =
                (GridRowAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(GridRowAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenGridRow(attributes);
                protocol.writeCloseGridRow(attributes);
            }
        };
        String expecting = getExpectedWriteOpenGridRowResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

     /**
     * This method tests the method public void writeOpenLayout (
     * LayoutAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     *
     * NOTE: Close method removed
     */
    public void testWriteOpenLayout() throws Exception {
        final LayoutAttributes attributes =
                (LayoutAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(LayoutAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {

                attributes.setDeviceLayoutContext(
                        protocol.getMarinerPageContext().getDeviceLayoutContext());
                protocol.writeOpenLayout(attributes);
                protocol.writeCloseLayout(attributes);
            }
        };
        String expecting = getExpectedWriteOpenLayoutResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenPane ( PaneAttributes
     * ) for the com.volantis.mcs.protocols.VolantisProtocol class.
     * <p>
     * NOTE: Close method removed
     */
    public void testWriteOpenPane() throws Exception {
        final PaneAttributes attributes =
                (PaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(PaneAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {

                attributes.setPane(new Pane(new CanvasLayout()));
                protocol.writeOpenPane(attributes);
                protocol.writeClosePane(attributes);
            }
        };
        String expecting = getExpectedWriteOpenPaneResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writePaneContents ( OutputBuffer )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWritePaneContents() throws Exception {
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writePaneContents(getOutputBuffer(protocol));
            }
        };
        String expecting = getExpectedWritePaneContentsResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenRowIteratorPane ( RowIteratorPaneAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     *
     * NOTE: Close method removed.
     */
    public void testWriteOpenRowIteratorPane() throws Exception {
        final RowIteratorPaneAttributes attributes =
                (RowIteratorPaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(RowIteratorPaneAttributes.class);

        attributes.setPane(new Pane(new CanvasLayout()));
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenRowIteratorPane(attributes);
                protocol.writeCloseRowIteratorPane(attributes);
            }
        };
        String expecting = getExpectedWriteOpenRowIteratorPaneResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenRowIteratorPaneElement ( RowIteratorPaneAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenRowIteratorPaneElement() throws Exception {
        final RowIteratorPaneAttributes attributes =
                (RowIteratorPaneAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(RowIteratorPaneAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenRowIteratorPaneElement(attributes);
                protocol.writeCloseRowIteratorPaneElement(attributes);
            }
        };
        String expecting = getExpectedWriteOpenRowIteratorPaneElementResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void
     * writeRowIteratorPaneElementContents ( OutputBuffer ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteRowIteratorPaneElementContents() throws Exception {
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {

                protocol.writeRowIteratorPaneElementContents(
                        getOutputBuffer(protocol));
            }
        };
        String expecting = getExpectedWriteRowIteratorPaneElementContentsResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSegment ( SegmentAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSegment() throws Exception {
        final SegmentAttributes attributes =
                (SegmentAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SegmentAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSegment(attributes);
                protocol.writeCloseSegment(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSeqmentResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSegmentGrid (
     * SegmentGridAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSegmentGrid() throws Exception {
        final SegmentGridAttributes attributes =
                (SegmentGridAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SegmentGridAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSegmentGrid(attributes);
                protocol.writeCloseSegmentGrid(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSegmentGridResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenAnchor (
     * AnchorAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     *
     * NOTE: Close method removed.
     */
    public void testWriteOpenAnchor() throws Exception {
        final AnchorAttributes attributes =
                (AnchorAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(AnchorAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenAnchor(attributes);
                protocol.writeCloseAnchor(attributes);
            }
        };
        String expecting = getExpectedWriteOpenAnchorResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);

    }

    /**
     * This method tests the method public void writeDefaultSegmentLink ( AnchorAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteDefaultSegmentLink() throws Exception {
        final AnchorAttributes attributes =
                (AnchorAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(AnchorAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeDefaultSegmentLink(attributes);
            }
        };
        String expecting = getExpectedWriteDefaultSegmentLinkResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeFragmentLink ( FraglinkAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteFragmentLink() throws Exception {
        final FraglinkAttributes attributes =
                (FraglinkAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(FraglinkAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeFragmentLink(attributes);
            }
        };
        String expecting = getExpectedWriteFragmentLinkResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenAddress (
     * AddressAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenAddress() throws Exception {
        final AddressAttributes attributes =
                (AddressAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(AddressAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenAddress(attributes);
                protocol.writeCloseAddress(attributes);
            }
        };
        String expecting = getExpectedWriteOpenAddressResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

     /**
     * This method tests the method public void writeOpenBlockQuote (
     * BlockQuoteAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenBlockQuote() throws Exception {
        final BlockQuoteAttributes attributes =
                (BlockQuoteAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(BlockQuoteAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenBlockQuote(attributes);
                protocol.writeCloseBlockQuote(attributes);
            }
        };
        String expecting = getExpectedWriteOpenBlockQuoteResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenDiv ( DivAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenDiv() throws Exception {
        final DivAttributes attributes =
                (DivAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DivAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenDiv(attributes);
                protocol.writeCloseDiv(attributes);
            }
        };
        String expecting = getExpectedWriteOpenDivResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenHeading1 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading1()
            throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        attributes.setTitle("Heading Title");
        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading1(attributes);
                protocol.writeCloseHeading1(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading1Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenHeading2 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading2() throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading2(attributes);
                protocol.writeCloseHeading2(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading2Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenHeading3 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading3() throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading3(attributes);
                protocol.writeCloseHeading3(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading3Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenHeading4 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading4() throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading4(attributes);
                protocol.writeCloseHeading4(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading4Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenHeading5 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading5() throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading5(attributes);
                protocol.writeCloseHeading5(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading5Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }


    /**
     * This method tests the method public void writeOpenHeading6 (
     * HeadingAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenHeading6() throws Exception {
        final HeadingAttributes attributes =
                (HeadingAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HeadingAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenHeading6(attributes);
                protocol.writeCloseHeading6(attributes);
            }
        };
        String expecting = getExpectedWriteOpenHeading6Result();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeHorizontalRule (
     * HorizontalRuleAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteHorizontalRule() throws Exception {
        final HorizontalRuleAttributes attributes =
                (HorizontalRuleAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(HorizontalRuleAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeHorizontalRule(attributes);
            }
        };
        String expecting = getExpectedWriteHorizontalRuleResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenParagraph (
     * ParagraphAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenParagraph() throws Exception {
        final ParagraphAttributes attributes =
                (ParagraphAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ParagraphAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenParagraph(attributes);
                protocol.writeCloseParagraph(attributes);
            }
        };
        String expecting = getExpectedWriteOpenParagraphResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }


    /**
     * This method tests the method public void writeOpenPre ( PreAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenPre() throws Exception {
        final PreAttributes attributes =
                (PreAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(PreAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenPre(attributes);
                protocol.writeClosePre(attributes);
            }
        };
        String expecting = getExpectedWriteOpenPreResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenDefinitionData (
     * DefinitionDataAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenDefinitionData() throws Exception {
        final DefinitionDataAttributes attributes =
                (DefinitionDataAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DefinitionDataAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenDefinitionData(attributes);
                protocol.writeCloseDefinitionData(attributes);
            }
        };
        String expecting = getExpectedWriteOpenDefinitionDataResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

     /**
     * This method tests the method public void writeOpenDefinitionList (
     * DefinitionListAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenDefinitionList() throws Exception {
        final DefinitionListAttributes attributes =
                (DefinitionListAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DefinitionListAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenDefinitionList(attributes);
                protocol.writeCloseDefinitionList(attributes);
            }
        };
        String expecting = getExpectedWriteOpenDefinitionListResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenDefinitionTerm (
     * DefinitionTermAttributes ) for the
     * com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenDefinitionTerm() throws Exception {
        final DefinitionTermAttributes attributes =
                (DefinitionTermAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DefinitionTermAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenDefinitionTerm(attributes);
                protocol.writeCloseDefinitionTerm(attributes);
            }
        };
        String expecting = getExpectedWriteOpenDefinitionTermResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenListItem (
     * ListItemAttributes ) for the com.volantis.mcs.protocols.VolantisProtocol
     * class.
     */
    public void testWriteOpenListItem() throws Exception {
        final ListItemAttributes attributes =
                (ListItemAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ListItemAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenListItem(attributes);
                protocol.writeCloseListItem(attributes);
            }
        };
        String expecting = getExpectedWriteOpenListItemResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenOrderedList ( OrderedListAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenOrderedList() throws Exception {
        final OrderedListAttributes attributes =
                (OrderedListAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(OrderedListAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenOrderedList(attributes);
                protocol.writeCloseOrderedList(attributes);
            }
        };
        String expecting = getExpectedWriteOpenOrderedListResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenUnorderedList ( UnorderedListAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenUnorderedList() throws Exception {
        final UnorderedListAttributes attributes =
                (UnorderedListAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(UnorderedListAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenUnorderedList(attributes);
                protocol.writeCloseUnorderedList(attributes);
            }
        };
        String expecting = getExpectedWriteOpenUnorderedListResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTable ( TableAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTable() throws Exception {
        final TableAttributes attributes =
                (TableAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(TableAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTable(attributes);
                protocol.writeCloseTable(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableBody ( TableBodyAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableBody() throws Exception {
        final TableBodyAttributes attributes =
                (TableBodyAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(TableBodyAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableBody(attributes);
                protocol.writeCloseTableBody(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableBodyResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableDataCell ( TableCellAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableDataCell() throws Exception {
        final TableCellAttributes attributes = (TableCellAttributes)
            provideAttributes(TableCellAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableDataCell(attributes);
                protocol.writeCloseTableDataCell(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableDataCellResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableFooter ( TableFooterAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableFooter() throws Exception {
        final TableFooterAttributes attributes = (TableFooterAttributes)
            provideAttributes(TableFooterAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableFooter(attributes);
                protocol.writeCloseTableFooter(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableFooterResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableHeader ( TableHeaderAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableHeader() throws Exception {
        final TableHeaderAttributes attributes = (TableHeaderAttributes)
            provideAttributes(TableHeaderAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableHeader(attributes);
                protocol.writeCloseTableHeader(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableHeaderResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableHeaderCell ( TableCellAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableHeaderCell() throws Exception {
        final TableCellAttributes attributes = (TableCellAttributes)
            provideAttributes(TableCellAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableHeaderCell(attributes);
                protocol.writeCloseTableHeaderCell(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableHeaderCellResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenTableRow ( TableRowAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenTableRow() throws Exception {
        final TableRowAttributes attributes = (TableRowAttributes)
            provideAttributes(TableRowAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenTableRow(attributes);
                protocol.writeCloseTableRow(attributes);
            }
        };
        String expecting = getExpectedWriteOpenTableRowResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenBig ( BigAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenBig() throws Exception {
        final BigAttributes attributes =
                (BigAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(BigAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenBig(attributes);
                protocol.writeCloseBig(attributes);
            }
        };
        String expecting = getExpectedWriteOpenBigResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenBold ( BoldAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenBold() throws Exception {
        final BoldAttributes attributes =
                (BoldAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(BoldAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenBold(attributes);
                protocol.writeCloseBold(attributes);
            }
        };
        String expecting = getExpectedWriteOpenBoldResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenCite ( CiteAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenCite() throws Exception {
        final CiteAttributes attributes =
                (CiteAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(CiteAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenCite(attributes);
                protocol.writeCloseCite(attributes);
            }
        };
        String expecting = getExpectedWriteOpenCiteResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenCode ( CodeAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenCode() throws Exception {
        final CodeAttributes attributes =
                (CodeAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(CodeAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenCode(attributes);
                protocol.writeCloseCode(attributes);
            }
        };
        String expecting = getExpectedWriteOpenCodeResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenEmphasis ( EmphasisAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenEmphasis() throws Exception {
        final EmphasisAttributes attributes =
                (EmphasisAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(EmphasisAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenEmphasis(attributes);
                protocol.writeCloseEmphasis(attributes);
            }
        };
        String expecting = getExpectedWriteOpenEmphasisResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenItalic ( ItalicAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenItalic() throws Exception {
        final ItalicAttributes attributes =
                (ItalicAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ItalicAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenItalic(attributes);
                protocol.writeCloseItalic(attributes);
            }
        };
        String expecting = getExpectedWriteOpenItalicResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenKeyboard ( KeyboardAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenKeyboard() throws Exception {
        final KeyboardAttributes attributes =
                (KeyboardAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(KeyboardAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenKeyboard(attributes);
                protocol.writeCloseKeyboard(attributes);
            }
        };
        String expecting = getExpectedWriteOpenKeyboardResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeLineBreak ( LineBreakAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteLineBreak() throws Exception {
        final LineBreakAttributes attributes =
                (LineBreakAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(LineBreakAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeLineBreak(attributes);
            }
        };
        String expecting = getExpectedWriteLineBreakResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenMonospaceFont ( MonospaceFontAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenMonospaceFont() throws Exception {
        final MonospaceFontAttributes attributes =
                (MonospaceFontAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(MonospaceFontAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenMonospaceFont(attributes);
                protocol.writeCloseMonospaceFont(attributes);
            }
        };
        String expecting = getExpectedWriteOpenMonospaceFontResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSample ( SampleAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSample() throws Exception {
        final SampleAttributes attributes =
                (SampleAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SampleAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSample(attributes);
                protocol.writeCloseSample(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSampleResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSmall ( SmallAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSmall() throws Exception {
        final SmallAttributes attributes =
                (SmallAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SmallAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSmall(attributes);
                protocol.writeCloseSmall(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSmallResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);

    }

    /**
     * This method tests the method public void writeOpenSpan ( SpanAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSpan() throws Exception {
        final SpanAttributes attributes =
                (SpanAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SpanAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSpan(attributes);
                protocol.writeCloseSpan(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSpanResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenStrong ( StrongAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenStrong() throws Exception {
        final StrongAttributes attributes =
                (StrongAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(StrongAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenStrong(attributes);
                protocol.writeCloseStrong(attributes);
            }
        };
        String expecting = getExpectedWriteOpenStrongResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenSubscript ( SubscriptAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSubscript() throws Exception {
        final SubscriptAttributes attributes =
                (SubscriptAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SubscriptAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSubscript(attributes);
                protocol.writeCloseSubscript(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSubscriptResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void WriteOpenSuperscript ( SuperscriptAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenSuperscript() throws Exception {
        final SuperscriptAttributes attributes =
                (SuperscriptAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(SuperscriptAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenSuperscript(attributes);
                protocol.writeCloseSuperscript(attributes);
            }
        };
        String expecting = getExpectedWriteOpenSuperscriptResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenUnderline ( UnderlineAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenUnderline() throws Exception {
        final UnderlineAttributes attributes =
                (UnderlineAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(UnderlineAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenUnderline(attributes);
                protocol.writeCloseUnderline(attributes);
            }
        };
        String expecting = getExpectedWriteOpenUnderlineResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeDivideHint ( DivideHintAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteDivideHint() throws Exception {
        final DivideHintAttributes attributes =
                (DivideHintAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(DivideHintAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeDivideHint(attributes);
            }
        };
        String expecting = getExpectedWriteDivideHintResult();
        ProtocolIntegrationTestHelper.doTestWithoutUsingDOMComparison(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeImage ( ImageAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteImage() throws Exception {
        final ImageAttributes attributes =
                (ImageAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ImageAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeImage(attributes);
            }
        };
        String expecting = getExpectedWriteImageResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeMeta ( MetaAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     * @todo fix this test
     */
    public void noTestWriteMeta() throws Exception {
        final MetaAttributes attributes =
                (MetaAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(MetaAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeMeta(attributes);
            }
        };
        String expecting = getExpectedWriteMetaResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeOpenNoScript ( NoScriptAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenNoScript() throws Exception {
        final NoScriptAttributes attributes =
                (NoScriptAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(NoScriptAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenNoScript(attributes);
                protocol.writeCloseNoScript(attributes);
            }
        };
        String expecting = getExpectedWriteOpenNoScriptResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }


    /**
     * This method tests the method public void writeOpenScript ( ScriptAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteOpenScript() throws Exception {
        final ScriptAttributes attributes =
                (ScriptAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(ScriptAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenScript(attributes);
                protocol.writeCloseScript(attributes);
            }
        };
        String expecting = getExpectedWriteOpenScriptResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void doImplicitValue ( XFImplicitAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testDoImplicitValue() throws Exception {
        final XFImplicitAttributes attributes =
                (XFImplicitAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(XFImplicitAttributes.class);

        XFFormAttributes formAttributes = new XFFormAttributes();
        Form form = new Form(new CanvasLayout());
        attributes.setFormAttributes(formAttributes);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.doImplicitValue(attributes);
            }
        };
        String expecting = getExpectedDoImplicitValueResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * @return the expected result for the doImplicitValue method.
     */
    protected abstract String getExpectedDoImplicitValueResult();

    /**
     * This method tests the method public void writeOpenElement ( CustomMarkupAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     *
     * NOTE: Close method removed.
     */
    public void testWriteOpenElement() throws Exception {
        final CustomMarkupAttributes attributes =
                (CustomMarkupAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(CustomMarkupAttributes.class);
        Map map = new HashMap();
        map.put("key", "value");
        attributes.setAttributes(map);

        final VolantisProtocol protocol = getProtocol();

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeOpenElement(attributes);
                protocol.writeCloseElement(attributes);
            }
        };
        String expecting = getExpectedWriteOpenElementResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    /**
     * This method tests the method public void writeAltText ( AltTextAttributes )
     * for the com.volantis.mcs.protocols.VolantisProtocol class.
     */
    public void testWriteAltText() throws Exception {
        final AltTextAttributes attributes =
                (AltTextAttributes) ProtocolIntegrationTestHelper.
                provideAttributes(AltTextAttributes.class);

        final VolantisProtocol protocol = getProtocol();

        // Because alt text is now an object, provideAttributes will not
        // automatically initialize it
        attributes.setAltText(new LiteralTextAssetReference("altText"));

        MethodInvoker invoker = new MethodInvoker() {
            public void invoke() throws Exception {
                protocol.writeAltText(attributes);
            }
        };
        String expecting = getExpectedWriteAltTextResult();
        ProtocolIntegrationTestHelper.doTest(expectations, protocol, invoker, expecting);
    }

    // ---------------------
    //  Getter methods
    // ---------------------
    /**
     * @return the expected result for the WriteOpenBody method.
     */
    protected abstract String getExpectedWriteOpenBodyResult();

    /**
     * @return the expected result for the writeOpenCanvas method.
     */
    protected abstract String getExpectedWriteOpenCanvasResult();

    /**
     * @return the expected result for the WriteOpenInclusion method.
     */
    protected abstract String getExpectedWriteOpenInclusionResult();

    /**
     * @return the expected result for the WriteLink method.
     */
    protected abstract String getExpectedWriteLinkResult();

    /**
     * @return the expected result for the WriteOpenMontage method.
     */
    protected abstract String getExpectedWriteOpenMontageResult();

    /**
     * @return the expected result for the WriteProtocolString method.
     */
    protected abstract String getExpectedWriteProtocolStringResult();

    /**
     * @return the expected result for the WriteOpenStyle method.
     */
    protected abstract String getExpectedWriteOpenStyleResult();

    /**
     * @return the expected result for the WriteOpenSlide method.
     */
    protected abstract String getExpectedWriteOpenSlideResult();

    /**
     * @return the expected result for the WriteOpenSpatialFormatIterator method.
     */
    protected abstract String getExpectedWriteOpenSpatialFormatIteratorResult();

    /**
     * @return the expected result for the WriteOpenSpatialFormatIteratorRow method.
     */
    protected abstract String getExpectedWriteOpenSpatialFormatIteratorRowResult();

    /**
     * @return the expected result for the WriteOpenSpatialFormatIteratorChild method.
     */
    protected abstract String getExpectedWriteOpenSpatialFormatIteratorChildResult();

    /**
     * @return the expected result for the WriteOpenColumnIteratorPane method.
     */
    protected abstract String getExpectedWriteOpenColumnIteratorPaneResult();

    /**
     * @return the expected result for the WriteColumnIteratorPaneElementContents method.
     */
    protected abstract String getExpectedWriteColumnIteratorPaneElementContentsResult();

    /**
     * @return the expected result for the WriteOpenDissectingPane method.
     */
    protected abstract String getExpectedWriteOpenDissectingPaneResult();

    /**
     * @return the expected result for the WriteOpenForm method.
     */
    protected abstract String getExpectedWriteOpenFormResult();

    /**
     * @return the expected result for the WriteFormPreamble method.
     */
    protected abstract String getExpectedWriteFormPreambleResult();

    /**
     * @return the expected result for the WriteFormPostamble method.
     */
    protected abstract String getExpectedWriteFormPostambleResult();

    /**
     * @return the expected result for the WriteOpenGrid method.
     */
    protected abstract String getExpectedWriteOpenGridResult();

    /**
     * @return the expected result for the WriteOpenGridChild method.
     */
    protected abstract String getExpectedWriteOpenGridChildResult();

    /**
     * @return the expected result for the WriteOpenGridRow method.
     */
    protected abstract String getExpectedWriteOpenGridRowResult();

    /**
     * @return the expected result for the WriteOpenLayout method.
     */
    protected abstract String getExpectedWriteOpenLayoutResult();

    /**
     * @return the expected result for the WriteOpenPane method.
     */
    protected abstract String getExpectedWriteOpenPaneResult();

    /**
     * @return the expected result for the WritePaneContents method.
     */
    protected abstract String getExpectedWritePaneContentsResult();

    /**
     * @return the expected result for the WriteOpenRowIteratorPane method.
     */
    protected abstract String getExpectedWriteOpenRowIteratorPaneResult();

    /**
     * @return the expected result for the WriteOpenRowIteratorPaneElement method.
     */
    protected abstract String getExpectedWriteOpenRowIteratorPaneElementResult();

    /**
     * @return the expected result for the WriteRowIteratorPaneElementContents method.
     */
    protected abstract String getExpectedWriteRowIteratorPaneElementContentsResult();

    /**
     * @return the expected result for the WriteOpenSeqment method.
     */
    protected abstract String getExpectedWriteOpenSeqmentResult();

    /**
     * @return the expected result for the WriteOpenSegmentGrid method.
     */
    protected abstract String getExpectedWriteOpenSegmentGridResult();

    /**
     * @return the expected result for the WriteDefaultSegmentLink method.
     */
    protected abstract String getExpectedWriteDefaultSegmentLinkResult();

    /**
     * @return the expected result for the WriteFragmentLink method.
     */
    protected abstract String getExpectedWriteFragmentLinkResult();

    /**
     * @return the expected result for the WriteSSIInclude method.
     */
    protected abstract String getExpectedWriteSSIIncludeResult();

    /**
     * @return the expected result for the WriteSSIConfig method.
     */
    protected abstract String getExpectedWriteSSIConfigResult();

    /**
     * @return the expected result for the WriteOpenAddress method.
     */
    protected abstract String getExpectedWriteOpenAddressResult();

    /**
     * @return the expected result for the WriteOpenBlockQuote method.
     */
    protected abstract String getExpectedWriteOpenBlockQuoteResult();

    /**
     * @return the expected result for the WriteCloseDiv method.
     */
    protected abstract String getExpectedWriteOpenDivResult();

    /**
     * @return the expected result for the WriteOpenHeading3 method.
     */
    protected abstract String getExpectedWriteOpenHeading3Result();

    /**
     * @return the expected result for the WriteOpenHeading4 method.
     */
    protected abstract String getExpectedWriteOpenHeading4Result();

    /**
     * @return the expected result for the WriteOpenHeading5 method.
     */
    protected abstract String getExpectedWriteOpenHeading5Result();

    /**
     * @return the expected result for the WriteOpenHeading6 method.
     */
    protected abstract String getExpectedWriteOpenHeading6Result();

    /**
     * @return the expected result for the WriteCloseHeading6 method.
     */
    protected abstract String getExpectedWriteCloseHeading6Result();

    /**
     * @return the expected result for the WriteHorizontalRule method.
     */
    protected abstract String getExpectedWriteHorizontalRuleResult();

    /**
     * @return the expected result for the WriteOpenParagraph method.
     */
    protected abstract String getExpectedWriteOpenParagraphResult();

    /**
     * @return the expected result for the WriteOpenPre method.
     */
    protected abstract String getExpectedWriteOpenPreResult();

    /**
     * @return the expected result for the writeOpenListItem method.
     */
    protected abstract String getExpectedWriteOpenListItemResult();

    /**
     * @return the expected result for the WriteWriteOpenSuperscript method.
     */
    protected abstract String getExpectedWriteOpenSuperscriptResult();

    /**
     * @return the expected result for the WriteOpenUnderline method.
     */
    protected abstract String getExpectedWriteOpenUnderlineResult();

    /**
     * @return the expected result for the WriteDivideHint method.
     */
    protected abstract String getExpectedWriteDivideHintResult();

    /**
     * @return the expected result for the WriteImage method.
     */
    protected abstract String getExpectedWriteImageResult();

    /**
     * @return the expected result for the WriteMeta method.
     */
    protected abstract String getExpectedWriteMetaResult();

    /**
     * @return the expected result for the WriteAudio method.
     */
    protected abstract String getExpectedWriteAudioResult();

    /**
     * @return the expected result for the WriteOpenNoScript method.
     */
    protected abstract String getExpectedWriteOpenNoScriptResult();

    /**
     * @return the expected result for the WriteOpenScript method.
     */
    protected abstract String getExpectedWriteOpenScriptResult();

    /**
     * @return the expected result for the WriteOpenElement method.
     */
    protected abstract String getExpectedWriteOpenElementResult();

    /**
     * @return the expected result for the WriteAltText method.
     */
    protected abstract String getExpectedWriteAltTextResult();

    /**
     * @return the expected result for the writeOpenHeading1 method.
     */
    protected abstract String getExpectedWriteOpenHeading1Result();

    /**
     * @return the expected result for the writeOpenHeading2 method.
     */
    protected abstract String getExpectedWriteOpenHeading2Result();

    /**
     * @return the expected result for the writeOpenSmall method.
     */
    protected abstract String getExpectedWriteOpenSmallResult();

    /**
     * @return the expected result for the writeCloseAnchor method.
     */
    protected abstract String getExpectedWriteOpenAnchorResult();

    /**
     * @return the expected result for the writeCloseBig method.
     */
    protected abstract String getExpectedWriteCloseBigResult();

    /**
     * @return the expected result for the writeOpenBold method.
     */
    protected abstract String getExpectedWriteOpenBoldResult();

    /**
     * @return the expected result for the writeOpenCite method.
     */
    protected abstract String getExpectedWriteOpenCiteResult();

    /**
     * @return the expected result for the writeOpenCode method.
     */
    protected abstract String getExpectedWriteOpenCodeResult();

    /**
     * @return the expected result for the writeOpenColumnIteratorPaneElement
     * method.
     */
    protected abstract String
            getExpectedWriteOpenColumnIteratorPaneElementResult();

    /**
     * @return the expected result for the writeOpenDefinitionList method.
     */
    protected abstract String getExpectedWriteOpenDefinitionListResult();

    /**
     * @return the expected result for the writeOpenDefinitionData method.
     */
    protected abstract String getExpectedWriteOpenDefinitionDataResult();

    /**
     * @return the expected result for the writeOpenDefinitionTerm method.
     */
    protected abstract String getExpectedWriteOpenDefinitionTermResult();

    /**
     * @return the expected result for the writeOpenUnorderedList method.
     */
    protected abstract String getExpectedWriteOpenUnorderedListResult();

    /**
     * @return the expected result for the writeOpenTable method.
     */
    protected abstract String getExpectedWriteOpenTableResult();

    /**
     * @return the expected result for the writeOpenTableBody method.
     */
    protected abstract String getExpectedWriteOpenTableBodyResult();

    /**
     * @return the expected result for the writeOpenTableDataCell method.
     */
    protected abstract String getExpectedWriteOpenTableDataCellResult();

    /**
     * @return the expected result for the writeOpenOrderedList method.
     */
    protected abstract String getExpectedWriteOpenOrderedListResult();

    /**
     * @return the expected result for the writeOpenTableFooter method.
     */
    protected abstract String getExpectedWriteOpenTableFooterResult();

    /**
     * @return the expected result for the writeOpenTableHeader method.
     */
    protected abstract String getExpectedWriteOpenTableHeaderResult();

    /**
     * @return the expected result for the writeOpenTableHeaderCell method.
     */
    protected abstract String getExpectedWriteOpenTableHeaderCellResult();

    /**
     * @return the expected result for the writeOpenTableRow method.
     */
    protected abstract String getExpectedWriteOpenTableRowResult();

    /**
     * @return the expected result for the writeOpenBig method.
     */
    protected abstract String getExpectedWriteOpenBigResult();

    /**
     * @return the expected result for the writeOpenEmphasis method.
     */
    protected abstract String getExpectedWriteOpenEmphasisResult();

    /**
     * @return the expected result for the writeOpenItalic method.
     */
    protected abstract String getExpectedWriteOpenItalicResult();

    /**
     * @return the expected result for the writeOpenKeyboard method.
     */
    protected abstract String getExpectedWriteOpenKeyboardResult();

    /**
     * @return the expected result for the writeLineBreak method.
     */
    protected abstract String getExpectedWriteLineBreakResult();

    /**
     * @return the expected result for the writeOpenMonospaceFont method.
     */
    protected abstract String getExpectedWriteOpenMonospaceFontResult();

    /**
     * @return the expected result for the writeOpenSample method.
     */
    protected abstract String getExpectedWriteOpenSampleResult();

    /**
     * @return the expected result for the writeOpenSpan method.
     */
    protected abstract String getExpectedWriteOpenSpanResult();

    /**
     * @return the expected result for the writeOpenStrong method.
     */
    protected abstract String getExpectedWriteOpenStrongResult();

    /**
     * @return the expected result for the writeOpenSubscript method.
     */
    protected abstract String getExpectedWriteOpenSubscriptResult();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10638/1	emma	VBM:2005120505 Forward port: Generated XHTML was invalid - had no head tag but had head content

 06-Dec-05	10623/1	emma	VBM:2005120505 Generated XHTML was invalid: missing head tag but head content

 22-Aug-05	9298/6	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9348/3	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 19-Aug-05	9245/3	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 ===========================================================================
*/
