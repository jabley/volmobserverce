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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.mcs.accessors.xml.jdom.XMLLayoutAttributeTranslations;
import com.volantis.mcs.layouts.AbstractGrid;
import com.volantis.mcs.layouts.BorderColourAttribute;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Column;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.DestinationAreaAttribute;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.FrameBorderAttribute;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.HeightAttributes;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.MontageLayout;
import com.volantis.mcs.layouts.OptimizationLevelAttribute;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.StyleAttributes;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.WidthAttributes;
import com.volantis.mcs.layouts.DirectionAttribute;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Custom marshaller and unmarshaller for the layoutFormat element.
 */
public class MarshallLayoutFormat
    implements IMarshaller, IUnmarshaller, IAliasable, XMLAccessorConstants {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MarshallLayoutFormat.class);

    /**
     * The namespaces to be used.
     */
    private static final String URL_MARLIN =
            PolicySchemas.MARLIN_LPDM_CURRENT.getNamespaceURL();
    private static final String ELEMENT_LAYOUT_FORMAT = "layoutFormat";
    private static final String ELEMENT_COLUMN_ITERATOR_PANE_FORMAT = "columnIteratorPaneFormat";
    private static final String ELEMENT_FORM_FORMAT = "formFormat";
    private static final String ELEMENT_FRAGMENT_FORMAT = "fragmentFormat";
    private static final String ELEMENT_GRID_FORMAT = "gridFormat";
    private static final String ELEMENT_PANE_FORMAT = "paneFormat";
    private static final String ELEMENT_REGION_FORMAT = "regionFormat";
    private static final String ELEMENT_ROW_ITERATOR_PANE_FORMAT = "rowIteratorPaneFormat";
    private static final String ELEMENT_SPATIAL_FORMAT_ITERATOR = "spatialFormatIterator";
    private static final String ELEMENT_TEMPORAL_FORMAT_ITERATOR = "temporalFormatIterator";
    private static final String ELEMENT_DISSECTING_PANE_FORMAT = "dissectingPaneFormat";
    private static final String ELEMENT_EMPTY_FORMAT = "emptyFormat";
    private static final String ELEMENT_FORM_FRAGMENT_FORMAT = "formFragmentFormat";
    private static final String ELEMENT_REPLICA_FORMAT = "replicaFormat";
    private static final String ELEMENT_GRID_FORMAT_COLUMNS = "gridFormatColumns";
    private static final String ELEMENT_GRID_FORMAT_ROW = "gridFormatRow";
    private static final String ELEMENT_GRID_FORMAT_COLUMN = "gridFormatColumn";
    private static final String ELEMENT_SEGMENT_GRID_FORMAT = "segmentGridFormat";
    private static final String ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS = "segmentGridFormatColumns";
    private static final String ELEMENT_SEGMENT_GRID_FORMAT_COLUMN = "segmentGridFormatColumn";
    private static final String ELEMENT_SEGMENT_GRID_FORMAT_ROW = "segmentGridFormatRow";
    private static final String ELEMENT_SEGMENT_FORMAT = "segmentFormat";

//    private static final String ATTR_GROUP_NAME = "groupName";
//    private static final String ATTR_DESTINATION_LAYOUT = "destinationLayout";
    private static final String ATTR_DEFAULT_FRAGMENT = "defaultFragment";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_ALLOW_RESET = "allowReset";
    private static final String ATTR_NEXT_LINK_STYLE_CLASS = "nextLinkStyleClass";
    private static final String ATTR_NEXT_LINK_POSITION = "nextLinkPosition";
    private static final String ATTR_NEXT_LINK_TEXT = "nextLinkText";
    private static final String ATTR_PREVIOUS_LINK_STYLE_CLASS = "previousLinkStyleClass";
    private static final String ATTR_PREVIOUS_LINK_POSITION = "previousLinkPosition";
    private static final String ATTR_PREVIOUS_LINK_TEXT = "previousLinkText";
    private static final String ATTR_LINK_TEXT = "linkText";
    private static final String ATTR_BACK_LINK_TEXT = "backLinkText";
    private static final String ATTR_LINK_STYLE_CLASS = "linkStyleClass";
    private static final String ATTR_SHOW_PEER_LINKS = "showPeerLinks";
    private static final String ATTR_FRAGMENT_LINK_ORDER = "fragmentLinkOrder";
    private static final String ATTR_ROWS = "rows";
    private static final String ATTR_COLUMNS = "columns";
    private static final String ATTR_BACKGROUND_COLOR = "backgroundColor";
    private static final String ATTR_BACKGROUND_COMPONENT = "backgroundComponent";
    private static final String ATTR_BACKGROUND_COMPONENT_TYPE = "backgroundComponentType";
    private static final String ATTR_BORDER_WIDTH = "borderWidth";
    private static final String ATTR_CELL_PADDING = "cellPadding";
    private static final String ATTR_CELL_SPACING = "cellSpacing";
    private static final String ATTR_HEIGHT = "height";
    private static final String ATTR_HORIZONTAL_ALIGNMENT = "horizontalAlignment";
    private static final String ATTR_VERTICAL_ALIGNMENT = "verticalAlignment";
    private static final String ATTR_WIDTH = "width";
    private static final String ATTR_WIDTH_UNITS = "widthUnits";
    private static final String ATTR_OPTIMIZATION_LEVEL = "optimizationLevel";
    private static final String ATTR_STYLE_CLASS = "styleClass";
    private static final String ATTR_DIRECTIONALITY = "directionality";
    private static final String ATTR_INDEXING_DIRECTION = "indexingDirection";
    private static final String ATTR_ROW_COUNT = "rowCount";
    private static final String ATTR_ROW_STYLE_CLASSES = "rowStyleClasses";
    private static final String ATTR_COLUMN_COUNT = "columnCount";
    private static final String ATTR_COLUMN_STYLE_CLASSES = "columnStyleClasses";
    private static final String ATTR_ALIGN_CONTENT = "alignContent";
    private static final String ATTR_CLOCK_VALUES = "clockValues";
    private static final String ATTR_CELLS = "cells";
    private static final String ATTR_CELL_COUNT = "cellCount";
    private static final String ATTR_DEFAULT_SEGMENT = "defaultSegment";
    private static final String ATTR_BORDER_COLOR = "borderColor";
    private static final String ATTR_FRAME_BORDER = "frameBorder";
    private static final String ATTR_FRAME_SPACING = "frameSpacing";
    private static final String ATTR_FILTER_ON_KEYBOARD_USABILITY = "filterOnKeyboardUsability";
    private static final String ATTR_DESTINATION_AREA = "destinationArea";
    private static final String ATTR_NEXT_LINK_SHORTCUT = "nextLinkShortcut";
    private static final String ATTR_PREVIOUS_LINK_SHORTCUT = "previousLinkShortcut";
    private static final String ATTR_MAX_CONTENT_SIZE = "maxContentSize";
    private static final String ATTR_SHARD_LINK_ORDER = "shardLinkOrder";
    private static final String ATTR_SOURCE_FORMAT_NAME = "sourceFormatName";
    private static final String ATTR_SOURCE_FORMAT_TYPE = "sourceFormatType";
    private static final String ATTR_MARGIN_WIDTH = "marginWidth";
    private static final String ATTR_MARGIN_HEIGHT = "marginHeight";
    private static final String ATTR_RESIZE = "resize";
    private static final String ATTR_SCROLLING = "scrolling";

    private static final XMLLayoutAttributeTranslations translations =
        XMLLayoutAttributeTranslations.getInstance();

    /**
     * Default constructor used by JIBX.
     */
    public MarshallLayoutFormat() {
    }

    /**
     * Constructor used by JIBX to set the structure name, namespace URI
     * and namespace index.
     *
     * Note: Ignored.
     *
     * @param uri namespace URI
     * @param index namespace index
     * @param name XML element or mapping name
     */
    public MarshallLayoutFormat(final String uri, final int index,
                                final String name) {
    }

    // javadoc inherited
    public boolean isExtension(final int index) {
        return false;
    }

    // javadoc inherited
    public void marshal(final Object object, final IMarshallingContext ictx)
        throws JiBXException {

        // make sure the parameters are as expected
        if (!(object instanceof Layout)) {
            throw new JiBXException(EXCEPTION_LOCALIZER.format(
                "invalid-object-type", object.getClass()));
        } else if (ictx instanceof MarshallingContext) {
            final Layout layout = (Layout) object;
            final MarshallingContext ctx = (MarshallingContext) ictx;
            final int lpdmNamespaceIndex = getNamespaceIndex(ctx, URL_MARLIN);
            marshalDeviceLayout(lpdmNamespaceIndex, ctx, layout);
        }
    }

    // javadoc inherited
    public boolean isPresent(final IUnmarshallingContext ictx)
        throws JiBXException {

        return ictx.isAt(URL_MARLIN, ELEMENT_LAYOUT_FORMAT);
    }

    // javadoc inherited
    public Object unmarshal(final Object object,
                            final IUnmarshallingContext ictx)
        throws JiBXException {

        Layout layout;
        if (ictx instanceof UnmarshallingContext) {

            UnmarshallingContext ctx = (UnmarshallingContext) ictx;
            layout = unmarshalDeviceLayout(ctx);
        } else {
            throw new IllegalArgumentException("UnmarshallingContext expected," +
                " but " + ictx.getClass() + " is found.");
        }


        return layout;
    }

    /**
     * Marshals an attribute with the specified element name, attribute name and
     * value.
     *
     * Before marshalling it translates the value form old (2.9) to new version.
     *
     * @param ctx the marshalling context
     * @param elementName - element name
     * @param attributeName - attribute name
     * @param value - value
     * @throws JiBXException if the marshalling was unsuccessful
     */
    private void marshalAttribute(final MarshallingContext ctx,
                                  final String elementName,
                                  final String attributeName,
                                  final String value)
        throws JiBXException {

        final String newValue = translations.translateOldToNewAttributeValue(
            elementName, attributeName, value);
        if (newValue != null) {
            ctx.attribute(0, attributeName, newValue);
        }
    }

    /**
     * Unmarshals an optional attribute.
     * If the attribute is missing then null is returned.
     *
     * After unmarshalling it translates the value form new to old (2.9) version.
     *
     * @param ctx the unmarshalling context
     * @param elementName - element name
     * @param attributeName - attribute name
     */
    private String unmarshalAttribute(final UnmarshallingContext ctx,
                                      final String elementName,
                                      final String attributeName) {

        final String value = ctx.attributeText("", attributeName, null);
        // Translate to the old version iff a value is present
        return value == null ? null :
                translations.translateNewToOldAttributeValue(
            elementName, attributeName, value);
    }

    /**
     * Marshals a Layout.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param layout device layout to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalDeviceLayout(final int namespaceIndex,
                                     final MarshallingContext ctx,
                                     final Layout layout)
        throws JiBXException {

        if (layout.isCanvas()) {
            marshalCanvasLayout(namespaceIndex, ctx, layout);
        } else {
            marshalMontageLayout(namespaceIndex, ctx, layout);
        }
    }

    /**
     * Marshals a deviceLayoutCanvasFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param layout device layout to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalCanvasLayout(final int namespaceIndex,
                                     final MarshallingContext ctx,
                                     final Layout layout)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex,
            Layout.CANVAS_TYPE_ELEMENT_NAME);

        final String defaultFragmentName = layout.getDefaultFragmentName();
        if (defaultFragmentName != null) {
            marshalAttribute(ctx, Layout.CANVAS_TYPE_ELEMENT_NAME,
                ATTR_DEFAULT_FRAGMENT, defaultFragmentName);
        }

        ctx.closeStartContent();

        final Format rootFormat = layout.getRootFormat();

        marshalCanvasFormatContent(namespaceIndex, rootFormat, ctx);

        ctx.endTag(namespaceIndex, Layout.CANVAS_TYPE_ELEMENT_NAME);
    }

    /**
     * Marshals a columnIteratorPaneFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param columnIteratorPane pane to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalColumnIteratorPaneFormat(
            final int namespaceIndex,
            final MarshallingContext ctx,
            final ColumnIteratorPane columnIteratorPane)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex,
            ELEMENT_COLUMN_ITERATOR_PANE_FORMAT);
        marshalNonDissectingPaneAttributes(
            ctx, ELEMENT_COLUMN_ITERATOR_PANE_FORMAT, columnIteratorPane);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the NonDissectingPaneAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param pane the pane that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalNonDissectingPaneAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final Pane pane)
        throws JiBXException {

        marshalAllPaneAttributes(ctx, elementName, pane);
        marshalAdditionalNonDissectablePaneAndGridAttributes(
            ctx, elementName, pane);
        marshalDestinationAreaAttribute(ctx, elementName, pane);
    }

    /**
     * Marshals the AllPaneAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param pane the pane that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalAllPaneAttributes(final MarshallingContext ctx,
                                          final String elementName,
                                          final Pane pane)
        throws JiBXException {

        marshalRequiredName(ctx, elementName, pane);
        marshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, pane);
        marshalFilterKeyboardUsabilityAttribute(ctx, elementName, pane);
    }

    /**
     * Marshals the filterOnKeyboardUsability attribute.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param pane the pane that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalFilterKeyboardUsabilityAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final Pane pane)
        throws JiBXException {

        final String filterKeyboardUsability = pane.getFilterKeyboardUsability();
        if (filterKeyboardUsability != null) {
            marshalAttribute(ctx, elementName, ATTR_FILTER_ON_KEYBOARD_USABILITY,
                filterKeyboardUsability);
        }
    }

    /**
     * Marshals the destinationArea attribute.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param destinationAreaAttribute the DestinationAreaAttribute that
     * contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalDestinationAreaAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final DestinationAreaAttribute destinationAreaAttribute)
        throws JiBXException {

        final String destinationArea =
            destinationAreaAttribute.getDestinationArea();
        if (destinationArea != null) {
            marshalAttribute(
                ctx, elementName, ATTR_DESTINATION_AREA, destinationArea);
        }
    }

    /**
     * Marshals the paneFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param pane the Pane to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalPaneFormat(final int namespaceIndex,
                                   final MarshallingContext ctx,
                                   final Pane pane)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_PANE_FORMAT);
        marshalNonDissectingPaneAttributes(ctx, ELEMENT_PANE_FORMAT, pane);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the regionFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param region the Region to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalRegionFormat(final int namespaceIndex,
                                     final MarshallingContext ctx,
                                     final Region region)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_REGION_FORMAT);
        marshalRequiredName(ctx, ELEMENT_REGION_FORMAT, region);
        marshalDestinationAreaAttribute(ctx, ELEMENT_REGION_FORMAT, region);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the rowIteratorPaneFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param rowIteratorPane the RowIteratorPane to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalRowIteratorPaneFormat(
            final int namespaceIndex,
            final MarshallingContext ctx,
            final RowIteratorPane rowIteratorPane)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_ROW_ITERATOR_PANE_FORMAT);
        marshalNonDissectingPaneAttributes(
            ctx, ELEMENT_ROW_ITERATOR_PANE_FORMAT, rowIteratorPane);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the formFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param form the Form to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalFormFormat(final int namespaceIndex,
                                   final MarshallingContext ctx,
                                   final Form form)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_FORM_FORMAT);
        marshalRequiredName(ctx, ELEMENT_FORM_FORMAT, form);
        ctx.closeStartContent();

        marshalCanvasFormatContent(namespaceIndex, form.getChildAt(0), ctx);

        ctx.endTag(namespaceIndex, ELEMENT_FORM_FORMAT);
    }

    /**
     * Marshals the CanvasFormatContent group.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param format the Format to marshal
     * @param ctx marshalling context
     * @throws JiBXException if something went wrong
     */
    private void marshalCanvasFormatContent(final int namespaceIndex,
        final Format format,
        final MarshallingContext ctx)
        throws JiBXException {

        if (format == null) {
            marshalEmptyFormat(namespaceIndex, ctx);

            // Handle the Pane classes, starting with the subclasses
        } else if (format instanceof ColumnIteratorPane) {
            marshalColumnIteratorPaneFormat(
                namespaceIndex, ctx, (ColumnIteratorPane)format);
        } else if (format instanceof RowIteratorPane) {
            marshalRowIteratorPaneFormat(
                namespaceIndex, ctx, (RowIteratorPane)format);
        } else if (format instanceof DissectingPane) {
            marshalDissectingPane(
                namespaceIndex, ctx, (DissectingPane)format);
        } else if (format instanceof Pane) {
            marshalPaneFormat(namespaceIndex, ctx, (Pane)format);

            // Handle the Iterator classes
        } else if (format instanceof SpatialFormatIterator) {
            marshalSpatialFormatIterator(namespaceIndex, ctx,
                (SpatialFormatIterator)format);
        } else if (format instanceof TemporalFormatIterator) {
            marshalTemporalFormatIterator(namespaceIndex, ctx,
                (TemporalFormatIterator)format);

            // Handle the rest in any order
        } else if (format instanceof Form) {
            marshalFormFormat(namespaceIndex, ctx, (Form)format);
        } else if (format instanceof FormFragment) {
            marshalFormFragmentFormat(
                namespaceIndex, ctx, (FormFragment)format);
        } else if (format instanceof Fragment) {
            marshalFragmentFormat(namespaceIndex, ctx, (Fragment)format);
        } else if (format instanceof Grid) {
            marshalGridFormat(namespaceIndex, ctx, (Grid)format);
        } else if (format instanceof Region) {
            marshalRegionFormat(namespaceIndex, ctx, (Region)format);
        } else if (format instanceof Replica) {
            marshalReplicaFormat(namespaceIndex, ctx, (Replica)format);
        } else {
            throw new IllegalStateException(
                "Unexpected format type: " + format.getClass());
        }
    }

    /**
     * Marshals the dissectingPaneFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param dissectingPane the DissectingPane to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalDissectingPane(final int namespaceIndex,
                                       final MarshallingContext ctx,
                                       final DissectingPane dissectingPane)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_DISSECTING_PANE_FORMAT);
        marshalDissectingPaneAttributes(ctx, dissectingPane);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the DissectingPaneAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param dissectingPane the DissectingPane that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalDissectingPaneAttributes(
            final MarshallingContext ctx, final DissectingPane dissectingPane)
        throws JiBXException {

        marshalAllPaneAttributes(
            ctx, ELEMENT_DISSECTING_PANE_FORMAT, dissectingPane);
        final String nextShardLinkText = dissectingPane.getNextShardLinkText();
        if (nextShardLinkText != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_TEXT, nextShardLinkText);
        }
        final String nextShardLinkClass = dissectingPane.getNextShardLinkClass();
        if (nextShardLinkClass != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_STYLE_CLASS, nextShardLinkClass);
        }
        final String nextShardShortcut = dissectingPane.getNextShardShortcut();
        if (nextShardShortcut != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_SHORTCUT, nextShardShortcut);
        }
        final String previousShardLinkText =
            dissectingPane.getPreviousShardLinkText();
        if (previousShardLinkText != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_TEXT, previousShardLinkText);
        }
        final String previousShardLinkClass =
            dissectingPane.getPreviousShardLinkClass();
        if (previousShardLinkClass != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_STYLE_CLASS, previousShardLinkClass);
        }
        final String previousShardShortcut =
            dissectingPane.getPreviousShardShortcut();
        if (previousShardShortcut != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_SHORTCUT, previousShardShortcut);
        }
        final String maximumContentSize =
            dissectingPane.getMaximumContentSize();
        if (maximumContentSize != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_MAX_CONTENT_SIZE, maximumContentSize);
        }
        final String shardLinkOrder = dissectingPane.getShardLinkOrder();
        if (shardLinkOrder != null) {
            marshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_SHARD_LINK_ORDER, shardLinkOrder);
        }
    }

    /**
     * Marshals the replicaFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param replica the Replica to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalReplicaFormat(final int namespaceIndex,
                                      final MarshallingContext ctx,
                                      final Replica replica)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_REPLICA_FORMAT);
        marshalRequiredName(ctx, ELEMENT_REPLICA_FORMAT, replica);

        final String replicant = replica.getReplicant();
        if (replicant != null) {
            marshalAttribute(ctx, ELEMENT_REPLICA_FORMAT,
                ATTR_SOURCE_FORMAT_NAME, replicant);
        }
        final String replicantType = replica.getReplicantTypeString();
        if (replicantType != null) {
            marshalAttribute(ctx, ELEMENT_REPLICA_FORMAT,
                ATTR_SOURCE_FORMAT_TYPE, replicantType);
        }
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the RequiredName attribute group.
     * <p>
     * NOTE: this group may be removed soon since the name is no longer
     * required by the schema.
     *
     * @param ctx marshalling context
     * @param elementName the XML elementName
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalRequiredName(final MarshallingContext ctx,
                                     final String elementName,
                                     final Format format)
        throws JiBXException {

        marshalAttribute(ctx, elementName, ATTR_NAME, format.getName());
    }

    /**
     * Marshals the formFragmentFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param formFragment the FormFragment to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalFormFragmentFormat(final int namespaceIndex,
                                           final MarshallingContext ctx,
                                           final FormFragment formFragment)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_FORM_FRAGMENT_FORMAT);

        marshalRequiredName(ctx, ELEMENT_FORM_FRAGMENT_FORMAT, formFragment);
        final String reset = formFragment.getReset();
        if (reset != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_ALLOW_RESET,
                    reset);
        }
        final String nextLinkStyleClass = formFragment.getNextLinkStyleClass();
        if (nextLinkStyleClass != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_NEXT_LINK_STYLE_CLASS, nextLinkStyleClass);
        }
        final String nextLinkPosition = formFragment.getNextLinkPosition();
        if (nextLinkPosition != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_NEXT_LINK_POSITION, nextLinkPosition);
        }
        final String nextLinkText = formFragment.getNextLinkText();
        if (nextLinkText != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_NEXT_LINK_TEXT, nextLinkText);
        }
        final String previousLinkStyleClass =
            formFragment.getPreviousLinkStyleClass();
        if (previousLinkStyleClass != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_PREVIOUS_LINK_STYLE_CLASS, previousLinkStyleClass);
        }
        final String previousLinkPosition = formFragment.getPreviousLinkPosition();
        if (previousLinkPosition != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_PREVIOUS_LINK_POSITION, previousLinkPosition);
        }
        final String previousLinkText = formFragment.getPreviousLinkText();
        if (previousLinkText != null) {
            marshalAttribute(ctx, ELEMENT_FORM_FRAGMENT_FORMAT,
                ATTR_PREVIOUS_LINK_TEXT, previousLinkText);
        }

        ctx.closeStartContent();

        marshalCanvasFormatContent(
            namespaceIndex, formFragment.getChildAt(0), ctx);

        ctx.endTag(namespaceIndex, ELEMENT_FORM_FRAGMENT_FORMAT);
    }

    /**
     * Marshals the fragmentFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param fragment the Fragment to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalFragmentFormat(final int namespaceIndex,
                                       final MarshallingContext ctx,
                                       final Fragment fragment)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_FRAGMENT_FORMAT);
        marshalRequiredName(ctx, ELEMENT_FRAGMENT_FORMAT, fragment);
        final String linkText = fragment.getLinkText();
        if (linkText != null) {
            marshalAttribute(
                ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_LINK_TEXT, linkText);
        }
        final String backLinkText = fragment.getBackLinkText();
        if (backLinkText != null) {
            marshalAttribute(
                ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_BACK_LINK_TEXT, backLinkText);
        }
        final String linkStyleClass = fragment.getLinkClass();
        if (linkStyleClass != null) {
            marshalAttribute(ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_LINK_STYLE_CLASS,
                linkStyleClass);
        }
        final String peerLink = fragment.getPeerLink();
        if (peerLink != null) {
            marshalAttribute(ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_SHOW_PEER_LINKS,
                    peerLink);
        }
        final String fragmentLinkOrder = fragment.getFragmentLinkOrder();
        if (fragmentLinkOrder != null) {
            marshalAttribute(ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_FRAGMENT_LINK_ORDER,
                    fragmentLinkOrder);
        }

        ctx.closeStartContent();

        marshalCanvasFormatContent(namespaceIndex, fragment.getChildAt(0), ctx);

        ctx.endTag(namespaceIndex, ELEMENT_FRAGMENT_FORMAT);
    }

    /**
     * Marshals the gridFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param grid the GridFragment to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalGridFormat(final int namespaceIndex,
                                   final MarshallingContext ctx,
                                   final Grid grid)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_GRID_FORMAT);
        marshalGridAttributes(ctx, ELEMENT_GRID_FORMAT, grid);

        ctx.closeStartContent();

        final int columnCount = grid.getColumns();
        if (columnCount > 0) {
            marshalGridFormatColumns(namespaceIndex, ctx, grid);
        }
        final int rowCount = grid.getRows();
        for (int i = 0; i < rowCount; i++) {
            marshalGridFormatRow(namespaceIndex, ctx, grid, i, columnCount);
        }

        ctx.endTag(namespaceIndex, ELEMENT_GRID_FORMAT);
    }

    /**
     * Marshals the gridFormatColumns element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param grid the Grid that contains the columns
     * @throws JiBXException if something went wrong
     */
    private void marshalGridFormatColumns(final int namespaceIndex,
                                          final MarshallingContext ctx,
                                          final Grid grid)
        throws JiBXException {

        ctx.startTag(namespaceIndex, ELEMENT_GRID_FORMAT_COLUMNS);

        final int columnCount = grid.getColumns();
        for (int i = 0; i < columnCount; i++) {
            marshalGridFormatColumn(namespaceIndex, ctx, grid, i);
        }

        ctx.endTag(namespaceIndex, ELEMENT_GRID_FORMAT_COLUMNS);
    }

    /**
     * Marshals the gridFormatColumn element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param grid the Grid that contains the columns
     * @throws JiBXException if something went wrong
     */
    private void marshalGridFormatColumn(final int namespaceIndex,
                                         final MarshallingContext ctx,
                                         final Grid grid,
                                         final int index)
        throws JiBXException {

        final Column column = grid.getColumn(index);

        ctx.startTagAttributes(namespaceIndex, ELEMENT_GRID_FORMAT_COLUMN);
        marshalWidthPixelsOrPercentAttributes(
            ctx, ELEMENT_GRID_FORMAT_COLUMN, column);
        marshalStyleClassAttribute(ctx, ELEMENT_GRID_FORMAT_COLUMN, column);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the WidthPixelsOrPercentAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param attributes the WidthAttributes object that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalWidthPixelsOrPercentAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final WidthAttributes attributes)
        throws JiBXException {

        final String width = attributes.getWidth();
        if (width != null) {
            marshalAttribute(ctx, elementName, ATTR_WIDTH, width);
        }
        final String widthUnits = attributes.getWidthUnits();
        if (widthUnits != null) {
            marshalAttribute(ctx, elementName, ATTR_WIDTH_UNITS, widthUnits);
        }
    }

    /**
     * Marshals the StyleClassAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param attributes the StyleAttributes object that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalStyleClassAttribute(final MarshallingContext ctx,
                                            final String elementName,
                                            final StyleAttributes attributes)
        throws JiBXException {

        final String styleClass = attributes.getStyleClass();
        if (styleClass != null) {
            marshalAttribute(ctx, elementName, ATTR_STYLE_CLASS, styleClass);
        }
    }

    /**
     * Marshals the StyleClassAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param attributes the StyleAttributes object that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalDirectionAttribute(final MarshallingContext ctx,
                                           final String elementName,
                                           final DirectionAttribute attributes)
        throws JiBXException {

        final String directionality = attributes.getDirectionality();
        if (directionality != null) {
            marshalAttribute(ctx, elementName, ATTR_DIRECTIONALITY, directionality);
        }
    }


    /**
     * Marshals the gridFormatRow element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param grid the Grid that contains the row
     * @param rowIndex the index of the current row
     * @param columnCount the number of columns
     * @throws JiBXException if something went wrong
     */
    private void marshalGridFormatRow(final int namespaceIndex,
                                      final MarshallingContext ctx,
                                      final Grid grid,
                                      final int rowIndex,
                                      final int columnCount)
        throws JiBXException {

        final Row row = grid.getRow(rowIndex);
        ctx.startTagAttributes(namespaceIndex, ELEMENT_GRID_FORMAT_ROW);
        marshalHeightPixelsOnlyAttribute(ctx, ELEMENT_GRID_FORMAT_ROW, row);
        marshalStyleClassAttribute(ctx, ELEMENT_GRID_FORMAT_ROW, row);
        ctx.closeStartContent();
        for (int i = 0; i < columnCount; i++) {
            final Format child = grid.getChildAt(rowIndex, i);
            marshalCanvasFormatContent(namespaceIndex, child, ctx);
        }
        ctx.endTag(namespaceIndex, ELEMENT_GRID_FORMAT_ROW);
    }

    /**
     * Marshals the GridAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param grid the Grid that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalGridAttributes(final MarshallingContext ctx,
                                       final String elementName,
                                       final Grid grid)
        throws JiBXException {

        marshalOptionalName(ctx, elementName, grid);
        marshalGridDimensionAttributes(ctx, elementName, grid);
        marshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, grid);
        marshalAdditionalNonDissectablePaneAndGridAttributes(
            ctx, elementName, grid);
        marshalStyleClassAttribute(ctx, elementName, grid);
        marshalDirectionAttribute(ctx, elementName, grid);
    }

    /**
     * Marshals the OptionalName attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalOptionalName(final MarshallingContext ctx,
                                     final String elementName,
                                     final Format format)
        throws JiBXException {

        final String name = format.getName();
        if (name != null) {
            marshalAttribute(ctx, elementName, ATTR_NAME, name);
        }
    }

    /**
     * Marshals the GridDimensionAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param grid the Grid that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalGridDimensionAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final AbstractGrid grid)
        throws JiBXException {

        final String rows = grid.getRowsString();
        if (rows != null) {
            marshalAttribute(ctx, elementName, ATTR_ROWS, rows);
        }
        final String columns = grid.getColumnsString();
        if (columns != null) {
            marshalAttribute(ctx, elementName, ATTR_COLUMNS, columns);
        }
    }

    /**
     * Marshals the AllPaneAndGridAndIteratorAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalAllPaneAndGridAndIteratorAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final Format format)
        throws JiBXException {

        marshalBackgroundColorAttribute(ctx, elementName,format);
        marshalBackgroundComponentAttributes(ctx, elementName,format);
        marshalBorderWidthAttribute(ctx, elementName,format);
        marshalCellPaddingAttribute(ctx, elementName,format);
        marshalCellSpacingAttribute(ctx, elementName,format);
        marshalHeightPixelsOnlyAttribute(ctx, elementName,format);
        marshalHorizontalAlignmentAttribute(ctx, elementName,format);
        marshalVerticalAlignmentAttribute(ctx, elementName,format);
        marshalWidthPixelsOrPercentAttributes(ctx, elementName,format);
    }

    /**
     * Marshals the BackgroundColorAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalBackgroundColorAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final Format format)
        throws JiBXException {

        final String backgroundColour = format.getBackgroundColour();
        if (backgroundColour != null) {
            marshalAttribute(
                ctx, elementName, ATTR_BACKGROUND_COLOR, backgroundColour);
        }
    }

    /**
     * Marshals the BackgroundComponentAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalBackgroundComponentAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final Format format)
        throws JiBXException {

        final String backgroundComponent = format.getBackgroundComponent();
        if (backgroundComponent != null) {
            marshalAttribute(ctx, elementName, ATTR_BACKGROUND_COMPONENT,
                backgroundComponent);
        }
        final String backgroundComponentType =
            format.getBackgroundComponentType();
        if (backgroundComponentType != null) {
            marshalAttribute(ctx, elementName, ATTR_BACKGROUND_COMPONENT_TYPE,
                backgroundComponentType);
        }
    }

    /**
     * Marshals the BorderWidthAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalBorderWidthAttribute(final MarshallingContext ctx,
                                             final String elementName,
                                             final Format format)
        throws JiBXException {

        final String borderWidth = format.getBorderWidth();
        if (borderWidth != null) {
            marshalAttribute(ctx, elementName, ATTR_BORDER_WIDTH, borderWidth);
        }
    }

    /**
     * Marshals the CellPaddingAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalCellPaddingAttribute(final MarshallingContext ctx,
                                             final String elementName,
                                             final Format format)
        throws JiBXException {

        final String cellPadding = format.getCellPadding();
        if (cellPadding != null) {
            marshalAttribute(ctx, elementName, ATTR_CELL_PADDING, cellPadding);
        }
    }

    /**
     * Marshals the CellSpacingAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalCellSpacingAttribute(final MarshallingContext ctx,
                                             final String elementName,
                                             final Format format)
        throws JiBXException {

        final String cellSpacing = format.getCellSpacing();
        if (cellSpacing != null) {
            marshalAttribute(ctx, elementName, ATTR_CELL_SPACING, cellSpacing);
        }
    }

    /**
     * Marshals the HeightPixelsOnlyAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalHeightPixelsOnlyAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final HeightAttributes format)
        throws JiBXException {

        final String height = format.getHeight();
        if (height != null) {
            marshalAttribute(ctx, elementName, ATTR_HEIGHT, height);
        }
    }

    /**
     * Marshals the HorizontalAlignmentAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalHorizontalAlignmentAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final Format format)
        throws JiBXException {

        final String horizontalAlignment = format.getHorizontalAlignment();
        if (horizontalAlignment != null) {
            marshalAttribute(ctx, elementName, ATTR_HORIZONTAL_ALIGNMENT,
                horizontalAlignment);
        }
    }

    /**
     * Marshals the VerticalAlignmentAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalVerticalAlignmentAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final Format format)
        throws JiBXException {

        final String verticalAlignment = format.getVerticalAlignment();
        if (verticalAlignment != null) {
            marshalAttribute(ctx, elementName, ATTR_VERTICAL_ALIGNMENT,
                verticalAlignment);
        }
    }

    /**
     * Marshals the AdditionalNonDissectablePaneAndGridAttributes attribute
     * group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param optLevelAttribute the OptimizationLevelAttribute that contains the
     * values
     * @throws JiBXException if something went wrong
     */
    private void marshalAdditionalNonDissectablePaneAndGridAttributes(
            final MarshallingContext ctx,
            final String elementName,
            final OptimizationLevelAttribute optLevelAttribute)
        throws JiBXException {

        marshalOptimizationLevelAttribute(ctx, elementName, optLevelAttribute);
    }

    /**
     * Marshals the BackgroundColorAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param optLevelAttribute the OptimizationLevelAttribute that contains the
     * values
     * @throws JiBXException if something went wrong
     */
    private void marshalOptimizationLevelAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final OptimizationLevelAttribute optLevelAttribute)
        throws JiBXException {

        final String optimizationLevel =
            optLevelAttribute.getOptimizationLevel();
        if (optimizationLevel != null) {
            marshalAttribute(ctx, elementName, ATTR_OPTIMIZATION_LEVEL,
                optimizationLevel);
        }
    }

    /**
     * Marshals the spatialFormatIterator element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param spatialFormatIterator the SpatialFormatIterator to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalSpatialFormatIterator(
            final int namespaceIndex,
            final MarshallingContext ctx,
            final SpatialFormatIterator spatialFormatIterator)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_SPATIAL_FORMAT_ITERATOR);
        marshalSpatialFormatIteratorAttributes(ctx, spatialFormatIterator);

        ctx.closeStartContent();

        marshalCanvasFormatContent(
            namespaceIndex, spatialFormatIterator.getChildAt(0), ctx);

        ctx.endTag(namespaceIndex, ELEMENT_SPATIAL_FORMAT_ITERATOR);
    }

    /**
     * Marshals the SpatialFormatIteratorAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param spatialFormatIterator the SpatialFormatIterator that contains the
     * values
     * @throws JiBXException if something went wrong
     */
    private void marshalSpatialFormatIteratorAttributes(
            final MarshallingContext ctx,
            final SpatialFormatIterator spatialFormatIterator)
        throws JiBXException {

        marshalFormatIteratorAttributes(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, spatialFormatIterator);
        marshalOptimizationLevelAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
            spatialFormatIterator);
        marshalStyleClassAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
            spatialFormatIterator);
        marshalDirectionAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
            spatialFormatIterator);

        final String indexingDirection =
            spatialFormatIterator.getIndexingDirection();
        if (indexingDirection != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
                ATTR_INDEXING_DIRECTION, indexingDirection);
        }
        final String rowsFlexibility = spatialFormatIterator.getRowsFlexibility();
        if (rowsFlexibility != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ROWS,
                    rowsFlexibility);
        }
        final String rowCount = spatialFormatIterator.getRowCount();
        if (rowCount != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ROW_COUNT,
                rowCount);
        }
        final String columnsFlexibility = spatialFormatIterator.getColumnsFlexibility();
        if (columnsFlexibility != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_COLUMNS,
                    columnsFlexibility);
        }
        final String rowStyleClasses =
            spatialFormatIterator.getRowStyleClassesAttribute();
        if (rowStyleClasses != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
                ATTR_ROW_STYLE_CLASSES, rowStyleClasses);
        }
        final String columnCount = spatialFormatIterator.getColumnCount();
        if (columnCount != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_COLUMN_COUNT,
                columnCount);
        }
        final String alignContent = spatialFormatIterator.getAlignContent();
        if (alignContent != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ALIGN_CONTENT,
                    alignContent);
        }
        final String columnStyleClasses =
            spatialFormatIterator.getColumnStyleClassesAttribute();
        if (columnStyleClasses != null) {
            marshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
                ATTR_COLUMN_STYLE_CLASSES, columnStyleClasses);
        }
    }

    /**
     * Marshals the FormatIteratorAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param format the Format that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalFormatIteratorAttributes(final MarshallingContext ctx,
                                                 final String elementName,
                                                 final Format format)
        throws JiBXException {

        marshalRequiredName(ctx, elementName, format);
        marshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, format);
    }

    /**
     * Marshals the temporalFormatIterator element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param temporalFormatIterator the TemporalFormatIterator to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalTemporalFormatIterator(
            final int namespaceIndex,
            final MarshallingContext ctx,
            final TemporalFormatIterator temporalFormatIterator)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_TEMPORAL_FORMAT_ITERATOR);
        marshalTemporalFormatIteratorAttributes(ctx, temporalFormatIterator);
        ctx.closeStartContent();

        marshalCanvasFormatContent(
            namespaceIndex, temporalFormatIterator.getChildAt(0), ctx);

        ctx.endTag(namespaceIndex, ELEMENT_TEMPORAL_FORMAT_ITERATOR);

    }

    /**
     * Marshals the TemporalFormatIteratorAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param temporalFormatIterator the TemporalFormatIterator that contains
     * the values
     * @throws JiBXException if something went wrong
     */
    private void marshalTemporalFormatIteratorAttributes(
            final MarshallingContext ctx,
            final TemporalFormatIterator temporalFormatIterator)
        throws JiBXException {

        marshalFormatIteratorAttributes(
            ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, temporalFormatIterator);

        final String clockValues = temporalFormatIterator.getClockValues();
        if (clockValues != null) {
            marshalAttribute(ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR,
                ATTR_CLOCK_VALUES, clockValues);
        }
        final String cells = temporalFormatIterator.getCells();
        if (cells != null) {
            marshalAttribute(ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, ATTR_CELLS,
                cells);
        }
        final String cellCount = temporalFormatIterator.getCellCountString();
        if (cellCount != null) {
            marshalAttribute(ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, ATTR_CELL_COUNT,
                cellCount);
        }
    }

//    /**
//     * Marshals the DeviceLayoutFormatAttributes attribute group.
//     *
//     * @param ctx marshalling context
//     * @param elementName the XML element name
//     * @param layout the Layout that contains the values
//     * @throws JiBXException if something went wrong
//     */
//    private void marshalDeviceLayoutFormatAttributes(
//            final MarshallingContext ctx,
//            final String elementName,
//            final Layout layout)
//        throws JiBXException {
//
//        marshalAttribute(ctx, elementName, ATTR_DEVICE_NAME,
//            layout.getDeviceName());
//        final String layoutGroupName = layout.getLayoutGroupName();
//        if (layoutGroupName != null) {
//            marshalAttribute(ctx, elementName, ATTR_GROUP_NAME, layoutGroupName);
//        }
//        final String destinationLayout = layout.getDestinationLayout();
//        if (destinationLayout != null) {
//            marshalAttribute(ctx, elementName, ATTR_DESTINATION_LAYOUT,
//                destinationLayout);
//        }
//    }

    /**
     * Marshals the deviceLayoutMontageFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param layout the Layout to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalMontageLayout(final int namespaceIndex,
                                      final MarshallingContext ctx,
                                      final Layout layout)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex,
            Layout.MONTAGE_TYPE_ELEMENT_NAME);

        final String defaultSegment = layout.getDefaultSegmentName();
        if (defaultSegment != null) {
            marshalAttribute(ctx, Layout.MONTAGE_TYPE_ELEMENT_NAME,
                ATTR_DEFAULT_SEGMENT, defaultSegment);
        }
        ctx.closeStartContent();

        final Format rootFormat = layout.getRootFormat();
        if (rootFormat == null) {
            marshalEmptyFormat(namespaceIndex, ctx);
        } else {
            marshalSegmentGridFormat(
                namespaceIndex, ctx, (SegmentGrid)rootFormat);
        }

        ctx.endTag(namespaceIndex, Layout.MONTAGE_TYPE_ELEMENT_NAME);
    }

    /**
     * Marshals the segmentGridFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param segmentGrid the SegmentGrid to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalSegmentGridFormat(final int namespaceIndex,
                                          final MarshallingContext ctx,
                                          final SegmentGrid segmentGrid)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT);
        marshalGridDimensionAttributes(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        marshalBorderColorAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        marshalBorderWidthAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        marshalFrameBorderAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        marshalFrameSpacingAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        ctx.closeStartContent();

        final int columnCount = segmentGrid.getColumns();
        if (columnCount > 0) {
            marshalSegmentGridFormatColumns(namespaceIndex, ctx, segmentGrid);
        }
        final int rowCount = segmentGrid.getRows();
        for (int i = 0; i < rowCount; i++) {
            marshalSegmentGridFormatRow(
                namespaceIndex, ctx, segmentGrid, i, columnCount);
        }

        ctx.endTag(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT);
    }

    /**
     * Marshals the segmentGridFormatColumns element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param segmentGrid the SegmentGrid that contains the columns
     * @throws JiBXException if something went wrong
     */
    private void marshalSegmentGridFormatColumns(final int namespaceIndex,
                                                 final MarshallingContext ctx,
                                                 final SegmentGrid segmentGrid)
        throws JiBXException {

        ctx.startTag(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS);

        final int columnCount = segmentGrid.getColumns();
        for (int i = 0; i < columnCount; i++) {
            marshalSegmentGridFormatColumn(namespaceIndex, ctx, segmentGrid, i);
        }

        ctx.endTag(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS);
    }

    /**
     * Marshals the segmentGridFormatColumn element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param segmentGrid the SegmentGrid that contains the columns
     * @throws JiBXException if something went wrong
     */
    private void marshalSegmentGridFormatColumn(final int namespaceIndex,
                                                final MarshallingContext ctx,
                                                final SegmentGrid segmentGrid,
                                                final int index)
        throws JiBXException {

        final Column column = segmentGrid.getColumn(index);
        ctx.startTagAttributes(namespaceIndex,
            ELEMENT_SEGMENT_GRID_FORMAT_COLUMN);
        marshalWidthPixelsOrPercentAttributes(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT_COLUMN, column);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the segmentGridFormatRow element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param segmentGrid the SegmentGrid that contains the row
     * @param rowIndex the index of the row
     * @param columnCount the number of columns
     * @throws JiBXException if something went wrong
     */
    private void marshalSegmentGridFormatRow(final int namespaceIndex,
                                             final MarshallingContext ctx,
                                             final SegmentGrid segmentGrid,
                                             final int rowIndex,
                                             final int columnCount)
        throws JiBXException {

        final Row row = segmentGrid.getRow(rowIndex);
        ctx.startTagAttributes(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT_ROW);
        marshalHeightPixelsOnlyAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT_ROW, row);
        ctx.closeStartContent();
        for (int i = 0; i < columnCount; i++) {
            final Format child = segmentGrid.getChildAt(rowIndex, i);
            if (child != null) {
                marshalMontageFormatContent(namespaceIndex, child, ctx);
            } else {
                marshalEmptyFormat(namespaceIndex, ctx);
            }
        }
        ctx.endTag(namespaceIndex, ELEMENT_SEGMENT_GRID_FORMAT_ROW);
    }

    /**
     * Marshals the MontageFormatContent group.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param format the Format to marshal
     * @param ctx marshalling context
     * @throws JiBXException if something went wrong
     */
    private void marshalMontageFormatContent(final int namespaceIndex,
                                             final Format format,
                                             final MarshallingContext ctx)
        throws JiBXException {

        if (format == null) {
            marshalEmptyFormat(namespaceIndex, ctx);
        } else if (format instanceof Segment) {
            marshalSegmentFormat(namespaceIndex, ctx, (Segment) format);
        } else if (format instanceof SegmentGrid) {
            marshalSegmentGridFormat(namespaceIndex, ctx, (SegmentGrid) format);
        } else {
            throw new IllegalStateException(
                "Unexpected format type: " + format.getClass());
        }
    }

    /**
     * Marshals the segmentFormat element.
     *
     * @param namespaceIndex the index for the lpdm namespace
     * @param ctx marshalling context
     * @param segment the Segment to marshal
     * @throws JiBXException if something went wrong
     */
    private void marshalSegmentFormat(final int namespaceIndex,
                                      final MarshallingContext ctx,
                                      final Segment segment)
        throws JiBXException {

        ctx.startTagAttributes(namespaceIndex, ELEMENT_SEGMENT_FORMAT);
        marshalRequiredName(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        marshalBorderColorAttribute(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        marshalFrameBorderAttribute(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        marshalMarginAttributes(ctx, segment);
        marshalResizeAttribute(ctx, segment);
        marshalScrollingAttribute(ctx, segment);
        ctx.closeStartEmpty();
    }

    /**
     * Marshals the MarginAttributes attribute group.
     *
     * @param ctx marshalling context
     * @param segment the Segment that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalMarginAttributes(final MarshallingContext ctx,
                                         final Segment segment)
        throws JiBXException {

        final String marginWidth = segment.getMarginWidth();
        if (marginWidth != null) {
            marshalAttribute(
                ctx, ELEMENT_SEGMENT_FORMAT, ATTR_MARGIN_WIDTH, marginWidth);
        }
        final String marginHeight = segment.getMarginHeight();
        if (marginHeight != null) {
            marshalAttribute(
                ctx, ELEMENT_SEGMENT_FORMAT, ATTR_MARGIN_HEIGHT, marginHeight);
        }
    }

    /**
     * Marshals the ResizeAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param segment the Segment that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalResizeAttribute(final MarshallingContext ctx,
                                        final Segment segment)
        throws JiBXException {

        final String resize = segment.getResize();
        if (resize != null) {
            marshalAttribute(ctx, ELEMENT_SEGMENT_FORMAT, ATTR_RESIZE, resize);
        }
    }

    /**
     * Marshals the ScrollingAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param segment the Segment that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalScrollingAttribute(final MarshallingContext ctx,
                                           final Segment segment)
        throws JiBXException {

        final String scrolling = segment.getScrolling();
        if (scrolling != null) {
            marshalAttribute(
                ctx, ELEMENT_SEGMENT_FORMAT, ATTR_SCROLLING, scrolling);
        }
    }

    /**
     * Marshals the emptyFormat element.
     *
     * @param namespaceIndex the index of the lpdm namespace
     * @param ctx marshalling context
     * @throws JiBXException if something went wrong
     */
    private void marshalEmptyFormat(final int namespaceIndex,
                                    final MarshallingContext ctx)
        throws JiBXException {

        ctx.startTag(namespaceIndex, ELEMENT_EMPTY_FORMAT);
        ctx.endTag(namespaceIndex, ELEMENT_EMPTY_FORMAT);
    }

    /**
     * Marshals the BorderColorAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param borderColourAttribute the BorderColourAttribute that contains the
     * values
     * @throws JiBXException if something went wrong
     */
    private void marshalBorderColorAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final BorderColourAttribute borderColourAttribute)
        throws JiBXException {

        final String borderColour = borderColourAttribute.getBorderColour();
        if (borderColour != null) {
            marshalAttribute(ctx, elementName, ATTR_BORDER_COLOR, borderColour);
        }
    }

    /**
     * Marshals the FrameBorderAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param frameBorderAttribute the FrameBorderAttribute that contains the
     * values
     * @throws JiBXException if something went wrong
     */
    private void marshalFrameBorderAttribute(
            final MarshallingContext ctx,
            final String elementName,
            final FrameBorderAttribute frameBorderAttribute)
        throws JiBXException {

        final String frameBorder = frameBorderAttribute.getFrameBorder();
        if (frameBorder != null) {
            marshalAttribute(ctx, elementName, ATTR_FRAME_BORDER, frameBorder);
        }
    }

    /**
     * Marshals the FrameSpacingAttribute attribute group.
     *
     * @param ctx marshalling context
     * @param elementName the XML element name
     * @param segmentGrid the SegmentGrid that contains the values
     * @throws JiBXException if something went wrong
     */
    private void marshalFrameSpacingAttribute(final MarshallingContext ctx,
                                              final String elementName,
                                              final SegmentGrid segmentGrid)
        throws JiBXException {

        final String frameSpacing = segmentGrid.getFrameSpacing();
        if (frameSpacing != null) {
            marshalAttribute(ctx, elementName, ATTR_FRAME_SPACING, frameSpacing);
        }
    }

    /**
     * Returns the namespace index stored in the MarshallingContext.
     *
     * @param ctx - the marshalling context
     * @param url - the namespace url to search for
     * @return the namespace index assigned to the specified namespace url
     * @throws IllegalArgumentException if the namespace cannot be found.
     */
    private int getNamespaceIndex(final MarshallingContext ctx,
                                  final String url) {

        final String[] namespaces = ctx.getNamespaces();
        for (int i = 0; i < namespaces.length; i++)
        {
            if (namespaces[i].equals(url)) {
                return i;
            }
        }
        throw new IllegalArgumentException(
            "Cannot find index for namespace: " + url);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Layout unmarshalDeviceLayout(final UnmarshallingContext ctx)
        throws JiBXException {

        if (ctx.isAt(URL_MARLIN, Layout.CANVAS_TYPE_ELEMENT_NAME)) {
            return unmarshalCanvasLayout(ctx);
        } else if (ctx.isAt(URL_MARLIN, Layout.MONTAGE_TYPE_ELEMENT_NAME)) {
            return unmarshalMontageLayout(ctx);
        } else {
            ctx.throwStartTagNameError(URL_MARLIN,
                Layout.CANVAS_TYPE_ELEMENT_NAME + " or " +
                Layout.MONTAGE_TYPE_ELEMENT_NAME);

            // Should never get here.
            throw new IllegalStateException();
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Layout unmarshalMontageLayout(final UnmarshallingContext ctx)
        throws JiBXException {

        // pre-read device name to avoid using deprecated constructor
//        final String deviceName = unmarshalRequiredAttribute(
//            ctx, Layout.MONTAGE_TYPE, ATTR_DEVICE_NAME);
        final MontageLayout montageLayout = new MontageLayout();

        final String defaultSegment = unmarshalAttribute(
            ctx, Layout.MONTAGE_TYPE, ATTR_DEFAULT_SEGMENT);
        if (defaultSegment != null) {
            montageLayout.setDefaultSegmentName(defaultSegment);
        }

        ctx.parsePastStartTag(
            URL_MARLIN, Layout.MONTAGE_TYPE_ELEMENT_NAME);

        final Format format;
        if (ctx.isAt(URL_MARLIN, SEGMENT_GRID_FORMAT_ELEMENT)) {
            format = unmarshalSegmentGridFormat(ctx, montageLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_EMPTY_FORMAT)) {
            format = unmarshalEmptyFormat(ctx);
        } else {
            ctx.throwStartTagNameError(URL_MARLIN,
                SEGMENT_GRID_FORMAT_ELEMENT + " or " + ELEMENT_EMPTY_FORMAT);
            throw new IllegalStateException("Cannot happen.");
        }

        montageLayout.setRootFormat(format);

        ctx.parsePastEndTag(URL_MARLIN, Layout.MONTAGE_TYPE_ELEMENT_NAME);

        return montageLayout;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Format unmarshalSegmentGridFormat(final UnmarshallingContext ctx,
                                              final MontageLayout montageLayout)
        throws JiBXException {

        final SegmentGrid segmentGrid = new SegmentGrid(montageLayout);
        unmarshalGridDimensionAttributes(
                ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        unmarshalBorderColorAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        unmarshalBorderWidthAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        unmarshalFrameBorderAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        unmarshalFrameSpacingAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT, segmentGrid);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT);

        segmentGrid.attributesHaveBeenSet();

        if (ctx.isAt(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS)) {
            unmarshalSegmentGridFormatColumns(ctx, segmentGrid);
        }
        for (int i = 0; ctx.isAt(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_ROW); i++) {
            unmarshalSegmentGridFormatRow(ctx, montageLayout, segmentGrid, i);
        }

        segmentGrid.setDeviceLayout(montageLayout);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT);

        return segmentGrid;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalBorderColorAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final BorderColourAttribute borderColourAttribute) {

        final String borderColour =
            unmarshalAttribute(ctx, elementName, ATTR_BORDER_COLOR);
        if (borderColour != null) {
            borderColourAttribute.setBorderColour(borderColour);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalFrameBorderAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final FrameBorderAttribute frameBorderAttribute) {

        final String frameBorder =
            unmarshalAttribute(ctx, elementName, ATTR_FRAME_BORDER);
        if (frameBorder != null) {
            frameBorderAttribute.setFrameBorder(frameBorder);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalFrameSpacingAttribute(final UnmarshallingContext ctx,
                                                final String elementName,
                                                final SegmentGrid segmentGrid) {

        final String frameSpacing =
            unmarshalAttribute(ctx, elementName, ATTR_FRAME_SPACING);
        if (frameSpacing != null) {
            segmentGrid.setFrameSpacing(frameSpacing);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalSegmentGridFormatColumns(
            final UnmarshallingContext ctx, final SegmentGrid segmentGrid)
        throws JiBXException {

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS);

        for (int i = 0; ctx.isAt(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMN); i++) {
            unmarshalSegmentGridFormatColumn(ctx, segmentGrid, i);
        }

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMNS);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalSegmentGridFormatColumn(final UnmarshallingContext ctx,
                                                  final SegmentGrid segmentGrid,
                                                  final int index)
        throws JiBXException {

        final Column column = segmentGrid.getColumn(index);
        unmarshalWidthPixelsOrPercentAttributes(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT_COLUMN, column);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMN);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_COLUMN);

    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalSegmentGridFormatRow(final UnmarshallingContext ctx,
                                               final MontageLayout montageLayout,
                                               final SegmentGrid segmentGrid,
                                               final int index)
        throws JiBXException {

        final Row row = segmentGrid.getRow(index);
        unmarshalHeightPixelsOnlyAttribute(
            ctx, ELEMENT_SEGMENT_GRID_FORMAT_ROW, row);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_ROW);
        final int base = index * segmentGrid.getColumns();
        for (int i = 0; !ctx.isEnd(); i++) {
            unmarshalMontageFormatContent(
                segmentGrid, ctx, montageLayout, base + i);
        }
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT_ROW);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalMontageFormatContent(final Format format,
                                               final UnmarshallingContext ctx,
                                               final MontageLayout montageLayout,
                                               final int childIndex)
        throws JiBXException {

        final Format childFormat;
        if (ctx.isAt(URL_MARLIN, ELEMENT_EMPTY_FORMAT)) {
            childFormat = unmarshalEmptyFormat(ctx);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_SEGMENT_FORMAT)) {
            childFormat = unmarshalSegmentFormat(ctx, montageLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_SEGMENT_GRID_FORMAT)) {
            childFormat = unmarshalSegmentGridFormat(ctx, montageLayout);
        } else {
            ctx.throwStartTagNameError(URL_MARLIN,
                ELEMENT_COLUMN_ITERATOR_PANE_FORMAT + " or " +
                ELEMENT_DISSECTING_PANE_FORMAT + " or " +
                ELEMENT_EMPTY_FORMAT + " or " + ELEMENT_FORM_FORMAT + " or " +
                ELEMENT_FORM_FRAGMENT_FORMAT + " or " +
                ELEMENT_FRAGMENT_FORMAT + " or " + ELEMENT_GRID_FORMAT + " or " +
                ELEMENT_PANE_FORMAT + " or " + ELEMENT_REGION_FORMAT + " or " +
                ELEMENT_REPLICA_FORMAT + " or " +
                ELEMENT_ROW_ITERATOR_PANE_FORMAT + " or " +
                ELEMENT_SPATIAL_FORMAT_ITERATOR + " or " +
                ELEMENT_TEMPORAL_FORMAT_ITERATOR);
            throw new IllegalStateException("Cannot happen.");
        }
        if (childFormat != null) {
            childFormat.setParent(format);
            try {
                format.setChildAt(childFormat, childIndex);
            } catch (LayoutException e) {
                throw new JiBXException("Error occured during setting child.", e);
            }
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Format unmarshalSegmentFormat(final UnmarshallingContext ctx,
                                          final MontageLayout montageLayout)
        throws JiBXException {

        final Segment segment = new Segment(montageLayout);
        unmarshalRequiredName(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        unmarshalBorderColorAttribute(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        unmarshalFrameBorderAttribute(ctx, ELEMENT_SEGMENT_FORMAT, segment);
        unmarshalMarginAttributes(ctx, segment);
        unmarshalResizeAttribute(ctx, segment);
        unmarshalScrollingAttribute(ctx, segment);

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SEGMENT_FORMAT);

        try {
            segment.attributesHaveBeenSet();
        } catch (LayoutException e) {
            throw new JiBXException("Error after setting attributes for segment");
        }

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SEGMENT_FORMAT);

        return segment;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalMarginAttributes(final UnmarshallingContext ctx,
                                           final Segment segment) {

        final String marginWidth =
            unmarshalAttribute(ctx, ELEMENT_SEGMENT_FORMAT, ATTR_MARGIN_WIDTH);
        if (marginWidth != null) {
            segment.setMarginWidth(marginWidth);
        }
        final String marginHeight =
            unmarshalAttribute(ctx, ELEMENT_SEGMENT_FORMAT, ATTR_MARGIN_HEIGHT);
        if (marginHeight != null) {
            segment.setMarginHeight(marginHeight);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalResizeAttribute(final UnmarshallingContext ctx,
                                          final Segment segment) {

        final String resize =
            unmarshalAttribute(ctx, ELEMENT_SEGMENT_FORMAT, ATTR_RESIZE);
        if (resize != null) {
            segment.setResize(resize);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalScrollingAttribute(final UnmarshallingContext ctx,
                                             final Segment segment) {

        final String scrolling =
            unmarshalAttribute(ctx, ELEMENT_SEGMENT_FORMAT, ATTR_SCROLLING);
        if (scrolling != null) {
            segment.setScrolling(scrolling);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Layout unmarshalCanvasLayout(final UnmarshallingContext ctx)
        throws JiBXException {

        // pre-read device name to avoid using depricated constructor
//        final String deviceName = unmarshalRequiredAttribute(
//            ctx, Layout.CANVAS_TYPE, ATTR_DEVICE_NAME);
        final CanvasLayout canvasLayout = new CanvasLayout();

        final String defaultFragment = unmarshalAttribute(
            ctx, Layout.CANVAS_TYPE, ATTR_DEFAULT_FRAGMENT);
        if (defaultFragment != null) {
            canvasLayout.setDefaultFragmentName(defaultFragment);
        }

        ctx.parsePastStartTag(URL_MARLIN, Layout.CANVAS_TYPE_ELEMENT_NAME);

        final Format format = unmarshallCanvasFormatContent(ctx, canvasLayout);
        canvasLayout.setRootFormat(format);

        if (format != null) {
            format.setDeviceLayout(canvasLayout);
        }

        ctx.parsePastEndTag(URL_MARLIN, Layout.CANVAS_TYPE_ELEMENT_NAME);

        return canvasLayout;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private ColumnIteratorPane unmarshalColumnIteratorPaneFormat(
            final UnmarshallingContext ctx, final CanvasLayout canvasLayout)
        throws JiBXException {

        final ColumnIteratorPane columnIteratorPane =
            new ColumnIteratorPane(canvasLayout);
        ctx.pushTrackedObject(columnIteratorPane);
        unmarshalNonDissectingPaneAttributes(
            ctx, ELEMENT_COLUMN_ITERATOR_PANE_FORMAT, columnIteratorPane);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_COLUMN_ITERATOR_PANE_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_COLUMN_ITERATOR_PANE_FORMAT);
        ctx.popObject();
        return columnIteratorPane;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalNonDissectingPaneAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final Pane pane) {

        unmarshalAllPaneAttributes(ctx, elementName, pane);
        unmarshalAdditionalNonDissectablePaneAndGridAttributes(
            ctx, elementName, pane);
        unmarshalDestinationAreaAttribute(ctx, elementName, pane);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalAllPaneAttributes(final UnmarshallingContext ctx,
                                            final String elementName,
                                            final Pane pane) {

        unmarshalRequiredName(ctx, elementName, pane);
        unmarshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, pane);
        unmarshalFilterKeyboardUsabilityAttribute(ctx, elementName, pane);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalFilterKeyboardUsabilityAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final Pane pane) {

        final String filterKeyboardUsability = unmarshalAttribute(
            ctx, elementName, ATTR_FILTER_ON_KEYBOARD_USABILITY);
        if (filterKeyboardUsability != null) {
            pane.setFilterKeyboardUsability(filterKeyboardUsability);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalDestinationAreaAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final DestinationAreaAttribute destinationAreaAttribute) {

        final String destinationArea = unmarshalAttribute(
                ctx, elementName, ATTR_DESTINATION_AREA);
        if (destinationArea != null) {
            destinationAreaAttribute.setDestinationArea(destinationArea);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Pane unmarshalPaneFormat(final UnmarshallingContext ctx,
                                     final CanvasLayout canvasLayout)
        throws JiBXException {

        final Pane pane = new Pane(canvasLayout);
        ctx.pushTrackedObject(pane);
        unmarshalNonDissectingPaneAttributes(ctx, ELEMENT_PANE_FORMAT, pane);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_PANE_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_PANE_FORMAT);
        ctx.popObject();
        return pane;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Region unmarshalRegionFormat(final UnmarshallingContext ctx,
                                         final CanvasLayout canvasLayout)
        throws JiBXException {

        final Region region = new Region(canvasLayout);
        ctx.pushTrackedObject(region);
        unmarshalRequiredName(ctx, ELEMENT_REGION_FORMAT, region);
        unmarshalDestinationAreaAttribute(ctx, ELEMENT_REGION_FORMAT, region);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_REGION_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_REGION_FORMAT);
        ctx.popObject();
        return region;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private RowIteratorPane unmarshalRowIteratorPaneFormat(
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout)
        throws JiBXException {

        final RowIteratorPane rowIteratorPane =
            new RowIteratorPane(canvasLayout);
        ctx.pushTrackedObject(rowIteratorPane);
        unmarshalNonDissectingPaneAttributes(
            ctx, ELEMENT_ROW_ITERATOR_PANE_FORMAT, rowIteratorPane);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_ROW_ITERATOR_PANE_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_ROW_ITERATOR_PANE_FORMAT);
        ctx.popObject();
        return rowIteratorPane;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Form unmarshalFormFormat(final UnmarshallingContext ctx,
                                     final CanvasLayout canvasLayout)
        throws JiBXException {

        final Form form = new Form(canvasLayout);
        ctx.pushTrackedObject(form);
        unmarshalRequiredName(ctx, ELEMENT_FORM_FORMAT, form);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_FORM_FORMAT);

        unmarshalChildFormat(form, ctx, canvasLayout, 0);
        List formFragments = new ArrayList();
        findFormFragmentFormat(form, formFragments);
        Iterator it = formFragments.iterator();
        while (it.hasNext()) {
            form.addFormFragment((FormFragment) it.next());
        }
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_FORM_FORMAT);
        ctx.popObject();
        return form;
    }

    /**
     * Searches recursively given format. All form fragments are
     * stored in a list.
     *  
     * @param element format element
     * @param formFragments list of form fragments
     */
    private void findFormFragmentFormat(final Format element, List 
                                        formFragments) {
        if (element == null) {
            return;
        }
        for (int i = 0; i < element.getNumChildren(); i++) {
            Format child = element.getChildAt(i);
            if (child instanceof FormFragment) {
                formFragments.add(child);
            } else {
                findFormFragmentFormat(child, formFragments);
            }
        }
    }
    
    private void unmarshalChildFormat(final Format format,
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout,
            final int childIndex)
        throws JiBXException {

        final Format childFormat = unmarshallCanvasFormatContent(ctx,
                canvasLayout);
        if (childFormat != null) {
            childFormat.setParent(format);
            try {
                format.setChildAt(childFormat, childIndex);
            } catch (LayoutException e) {
                throw new JiBXException("Error occured during setting child.", e);
            }
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Format unmarshallCanvasFormatContent(
            final UnmarshallingContext ctx, final CanvasLayout canvasLayout)
            throws JiBXException {

        final Format childFormat;
        if (ctx.isAt(URL_MARLIN, ELEMENT_COLUMN_ITERATOR_PANE_FORMAT)) {
            childFormat = unmarshalColumnIteratorPaneFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_DISSECTING_PANE_FORMAT)) {
            childFormat = unmarshalDissectingPaneFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_EMPTY_FORMAT)) {
            childFormat = unmarshalEmptyFormat(ctx);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_FORM_FORMAT)) {
            childFormat = unmarshalFormFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_FORM_FRAGMENT_FORMAT)) {
            childFormat = unmarshalFormFragmentFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_FRAGMENT_FORMAT)) {
            childFormat = unmarshalFragmentFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_GRID_FORMAT)) {
            childFormat = unmarshalGridFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_PANE_FORMAT)) {
            childFormat = unmarshalPaneFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_REGION_FORMAT)) {
            childFormat = unmarshalRegionFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_REPLICA_FORMAT)) {
            childFormat = unmarshalReplicaFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_ROW_ITERATOR_PANE_FORMAT)) {
            childFormat = unmarshalRowIteratorPaneFormat(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_SPATIAL_FORMAT_ITERATOR)) {
            childFormat = unmarshalSpatialFormatIterator(ctx, canvasLayout);
        } else if (ctx.isAt(URL_MARLIN, ELEMENT_TEMPORAL_FORMAT_ITERATOR)) {
            childFormat = unmarshalTemporalFormatIterator(ctx, canvasLayout);
        } else {
            childFormat = null;
        }
        return childFormat;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private DissectingPane unmarshalDissectingPaneFormat(
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout)
        throws JiBXException {

        final DissectingPane dissectingPane = new DissectingPane(canvasLayout);
        ctx.pushTrackedObject(dissectingPane);
        unmarshalDissectingPaneAttributes(ctx, dissectingPane);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_DISSECTING_PANE_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_DISSECTING_PANE_FORMAT);
        ctx.popObject();
        return dissectingPane;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalDissectingPaneAttributes(
            final UnmarshallingContext ctx,
            final DissectingPane dissectingPane) {

        unmarshalAllPaneAttributes(
            ctx, ELEMENT_DISSECTING_PANE_FORMAT, dissectingPane);
        final String nextShardLinkText =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_TEXT);
        if (nextShardLinkText != null) {
            dissectingPane.setNextShardLinkText(nextShardLinkText);
        }
        final String nextShardLinkClass =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_STYLE_CLASS);
        if (nextShardLinkClass != null) {
            dissectingPane.setNextShardLinkClass(nextShardLinkClass);
        }
        final String nextShardShortcut =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_NEXT_LINK_SHORTCUT);
        if (nextShardShortcut != null) {
            dissectingPane.setNextShardShortcut(nextShardShortcut);
        }
        final String previousShardLinkText =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_TEXT);
        if (previousShardLinkText != null) {
            dissectingPane.setPreviousShardLinkText(previousShardLinkText);
        }
        final String previousShardLinkClass =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_STYLE_CLASS);
        if (previousShardLinkClass != null) {
            dissectingPane.setPreviousShardLinkClass(previousShardLinkClass);
        }
        final String previousShardShortcut =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_PREVIOUS_LINK_SHORTCUT);
        if (previousShardShortcut != null) {
            dissectingPane.setPreviousShardShortcut(previousShardShortcut);
        }
        final String maximumContentSize =
            unmarshalAttribute(ctx, ELEMENT_DISSECTING_PANE_FORMAT,
                ATTR_MAX_CONTENT_SIZE);
        if (maximumContentSize != null) {
            dissectingPane.setMaximumContentSize(maximumContentSize);
        }
        final String shardLinkOrder = unmarshalAttribute(ctx,
            ELEMENT_DISSECTING_PANE_FORMAT, ATTR_SHARD_LINK_ORDER);
        if (shardLinkOrder != null) {
            dissectingPane.setShardLinkOrder(shardLinkOrder);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry.
     */
    private Replica unmarshalReplicaFormat(final UnmarshallingContext ctx,
                                           final CanvasLayout canvasLayout)
        throws JiBXException {

        final Replica replica = new Replica(canvasLayout);
        ctx.pushTrackedObject(replica);
        unmarshalRequiredName(ctx, ELEMENT_REPLICA_FORMAT, replica);
        final String replicant = unmarshalAttribute(
            ctx, ELEMENT_REPLICA_FORMAT, ATTR_SOURCE_FORMAT_NAME);
        if (replicant != null) {
            replica.setReplicant(replicant);
        }
        final String sourceFormatType = unmarshalAttribute(
            ctx, ELEMENT_REPLICA_FORMAT, ATTR_SOURCE_FORMAT_TYPE);
        if (sourceFormatType != null) {
            replica.setReplicantTypeString(sourceFormatType);
        }
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_REPLICA_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_REPLICA_FORMAT);
        ctx.popObject();
        return replica;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private FormFragment unmarshalFormFragmentFormat(
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout)
        throws JiBXException {

        final FormFragment formFragment = new FormFragment(canvasLayout);
        ctx.pushTrackedObject(formFragment);
        unmarshalRequiredName(ctx, ELEMENT_FORM_FRAGMENT_FORMAT, formFragment);
        final String allowReset = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_ALLOW_RESET);
        if (allowReset != null) {
            formFragment.setReset(allowReset);
        }
        final String nextLinkStyleClass = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_NEXT_LINK_STYLE_CLASS);
        if (nextLinkStyleClass != null) {
            formFragment.setNextLinkStyleClass(nextLinkStyleClass);
        }
        final String nextLinkPosition = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_NEXT_LINK_POSITION);
        if (nextLinkPosition != null) {
            formFragment.setNextLinkPosition(nextLinkPosition);
        }
        final String nextLinkText = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_NEXT_LINK_TEXT);
        if (nextLinkText != null) {
            formFragment.setNextLinkText(nextLinkText);
        }
        final String previousLinkStyleClass = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_PREVIOUS_LINK_STYLE_CLASS);
        if (previousLinkStyleClass != null) {
            formFragment.setPreviousLinkStyleClass(previousLinkStyleClass);
        }
        final String previousLinkPosition = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_PREVIOUS_LINK_POSITION);
        if (previousLinkPosition != null) {
            formFragment.setPreviousLinkPosition(previousLinkPosition);
        }
        final String previousLinkText = unmarshalAttribute(
            ctx, ELEMENT_FORM_FRAGMENT_FORMAT, ATTR_PREVIOUS_LINK_TEXT);
        if (previousLinkText != null) {
            formFragment.setPreviousLinkText(previousLinkText);
        }

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_FORM_FRAGMENT_FORMAT);

        unmarshalChildFormat(formFragment, ctx, canvasLayout, 0);

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_FORM_FRAGMENT_FORMAT);

        ctx.popObject();
        
        return formFragment;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Format unmarshalEmptyFormat(final UnmarshallingContext ctx)
        throws JiBXException {

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_EMPTY_FORMAT);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_EMPTY_FORMAT);
        return null;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     * <p>
     * NOTE: this group may be removed soon since the name is no longer
     * required by the schema.
     */
    private void unmarshalRequiredName(final UnmarshallingContext ctx,
                                       final String elementName,
                                       final Format format) {
        final String name = unmarshalAttribute(ctx, elementName, ATTR_NAME);
        if (name != null) {
            format.setName(name);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Fragment unmarshalFragmentFormat(final UnmarshallingContext ctx,
                                             final CanvasLayout canvasLayout)
        throws JiBXException {

        final Fragment fragment = new Fragment(canvasLayout);
        ctx.pushTrackedObject(fragment);
        unmarshalRequiredName(ctx, ELEMENT_FRAGMENT_FORMAT, fragment);
        final String linkText =
            unmarshalAttribute(ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_LINK_TEXT);
        if (linkText != null) {
            fragment.setLinkText(linkText);
        }
        final String backLinkText = unmarshalAttribute(
            ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_BACK_LINK_TEXT);
        if (backLinkText != null) {
            fragment.setBackLinkText(backLinkText);
        }
        final String linkStyleClass = unmarshalAttribute(
            ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_LINK_STYLE_CLASS);
        if (linkStyleClass != null) {
            fragment.setLinkClass(linkStyleClass);
        }
        final String showPeerLinks = unmarshalAttribute(
            ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_SHOW_PEER_LINKS);
        if (showPeerLinks != null) {
            fragment.setPeerLinks(showPeerLinks);
        }
        final String fragmentLinkOrder = unmarshalAttribute(
            ctx, ELEMENT_FRAGMENT_FORMAT, ATTR_FRAGMENT_LINK_ORDER);
        if (fragmentLinkOrder != null) {
            fragment.setFragmentLinkOrder(fragmentLinkOrder);
        }

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_FRAGMENT_FORMAT);

        unmarshalChildFormat(fragment, ctx, canvasLayout, 0);

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_FRAGMENT_FORMAT);
        ctx.popObject();

        return fragment;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private Grid unmarshalGridFormat(final UnmarshallingContext ctx,
                                     final CanvasLayout canvasLayout)
        throws JiBXException {

        final Grid grid = new Grid(canvasLayout);
        ctx.pushTrackedObject(grid);
        unmarshalGridAttributes(ctx, ELEMENT_GRID_FORMAT, grid);

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_GRID_FORMAT);

        grid.attributesHaveBeenSet();

        if (ctx.isAt(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMNS)) {
            unmarshalGridFormatColumns(ctx, grid);
        }
        for (int i = 0; i < grid.getRows() && ctx.isAt(URL_MARLIN, ELEMENT_GRID_FORMAT_ROW); i++) {
            unmarshalGridFormatRow(ctx, canvasLayout, grid, i);
        }

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_GRID_FORMAT);
        ctx.popObject();

        return grid;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalGridAttributes(final UnmarshallingContext ctx,
                                         final String elementName,
                                         final Grid grid) {

        unmarshalOptionalName(ctx, elementName, grid);
        unmarshalGridDimensionAttributes(ctx, elementName, grid);
        unmarshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, grid);
        unmarshalAdditionalNonDissectablePaneAndGridAttributes(
            ctx, elementName, grid);
        unmarshalStyleClassAttribute(ctx, elementName, grid);
        unmarshalDirectionAttribute(ctx, elementName, grid);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalOptionalName(final UnmarshallingContext ctx,
                                       final String elementName,
                                       final Format format) {

        final String name = unmarshalAttribute(ctx, elementName, ATTR_NAME);
        if (name != null) {
            format.setName(name);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalGridDimensionAttributes(
            final UnmarshallingContext ctx,
            String elementName, final AbstractGrid grid) {

        String rows = unmarshalAttribute(ctx, elementName, ATTR_ROWS);
        grid.setRows(rows);

        String columns = unmarshalAttribute(ctx, elementName, ATTR_COLUMNS);
        grid.setColumns(columns);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalAllPaneAndGridAndIteratorAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        unmarshalBackgroundColorAttribute(ctx, elementName, format);
        unmarshalBackgroundComponentAttributes(ctx, elementName, format);
        unmarshalBorderWidthAttribute(ctx, elementName, format);
        unmarshalCellPaddingAttribute(ctx, elementName, format);
        unmarshalCellSpacingAttribute(ctx, elementName, format);
        unmarshalHeightPixelsOnlyAttribute(ctx, elementName, format);
        unmarshalHorizontalAlignmentAttribute(ctx, elementName, format);
        unmarshalVerticalAlignmentAttribute(ctx, elementName, format);
        unmarshalWidthPixelsOrPercentAttributes(ctx, elementName, format);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalBackgroundColorAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        final String backgroundColour =
            unmarshalAttribute(ctx, elementName, ATTR_BACKGROUND_COLOR);
        if (backgroundColour != null) {
            format.setBackgroundColour(backgroundColour);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalBackgroundComponentAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        final String backgroundComponent =
            unmarshalAttribute(ctx, elementName, ATTR_BACKGROUND_COMPONENT);
        if (backgroundComponent != null) {
            format.setBackgroundComponent(backgroundComponent);
        }
        final String backgroundComponentType = unmarshalAttribute(
            ctx, elementName, ATTR_BACKGROUND_COMPONENT_TYPE);
        if (backgroundComponentType != null) {
            format.setBackgroundComponentType(backgroundComponentType);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalBorderWidthAttribute(final UnmarshallingContext ctx,
                                               final String elementName,
                                               final Format format) {

        final String borderWidth =
            unmarshalAttribute(ctx, elementName, ATTR_BORDER_WIDTH);
        if (borderWidth != null) {
            format.setBorderWidth(borderWidth);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalCellPaddingAttribute(final UnmarshallingContext ctx,
                                               final String elementName,
                                               final Format format) {

        final String cellPadding =
            unmarshalAttribute(ctx, elementName, ATTR_CELL_PADDING);
        if (cellPadding != null) {
            format.setCellPadding(cellPadding);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalCellSpacingAttribute(final UnmarshallingContext ctx,
                                               final String elementName,
                                               final Format format) {

        final String cellSpacing =
            unmarshalAttribute(ctx, elementName, ATTR_CELL_SPACING);
        if (cellSpacing != null) {
            format.setCellSpacing(cellSpacing);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalHeightPixelsOnlyAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final HeightAttributes format) {

        final String height = unmarshalAttribute(ctx, elementName, ATTR_HEIGHT);
        if (height != null) {
            format.setHeight(height);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalHorizontalAlignmentAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        final String horizontalAlignment =
            unmarshalAttribute(ctx, elementName, ATTR_HORIZONTAL_ALIGNMENT);
        if (horizontalAlignment != null) {
            format.setHorizontalAlignment(horizontalAlignment);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalVerticalAlignmentAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        final String verticalAlignment =
            unmarshalAttribute(ctx, elementName, ATTR_VERTICAL_ALIGNMENT);
        if (verticalAlignment != null) {
            format.setVerticalAlignment(verticalAlignment);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalWidthPixelsOrPercentAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final WidthAttributes format) {

        final String width = unmarshalAttribute(ctx, elementName, ATTR_WIDTH);
        if (width != null) {
            format.setWidth(width);
        }
        final String widthUnits =
            unmarshalAttribute(ctx, elementName, ATTR_WIDTH_UNITS);
        if (widthUnits != null) {
            format.setWidthUnits(widthUnits);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalAdditionalNonDissectablePaneAndGridAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final OptimizationLevelAttribute optLevelAttribute) {

        unmarshalOptimizationLevelAttribute(ctx, elementName, optLevelAttribute);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalOptimizationLevelAttribute(
            final UnmarshallingContext ctx,
            final String elementName,
            final OptimizationLevelAttribute attribute) {

        final String optimizationLevel =
            unmarshalAttribute(ctx, elementName, ATTR_OPTIMIZATION_LEVEL);
        if (optimizationLevel != null) {
            attribute.setOptimizationLevel(optimizationLevel);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalStyleClassAttribute(final UnmarshallingContext ctx,
                                              final String elementName,
                                              final StyleAttributes attributes) {

        final String styleClass =
            unmarshalAttribute(ctx, elementName, ATTR_STYLE_CLASS);
        if (styleClass != null) {
            attributes.setStyleClass(styleClass);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalDirectionAttribute(final UnmarshallingContext ctx,
                                              final String elementName,
                                              final DirectionAttribute attribute) {

        final String direction =
            unmarshalAttribute(ctx, elementName, ATTR_DIRECTIONALITY);
        if (direction != null) {
            attribute.setDirectionality(direction);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalGridFormatColumns(final UnmarshallingContext ctx,
                                            final Grid grid)
        throws JiBXException {

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMNS);

        for (int i = 0; i < grid.getColumns() && ctx.isAt(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMN); i++) {
            unmarshalGridFormatColumn(ctx, grid, i);
        }

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMNS);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalGridFormatColumn(final UnmarshallingContext ctx,
                                           final Grid grid,
                                           final int index)
        throws JiBXException {

        final Column column = grid.getColumn(index);
        unmarshalWidthPixelsOrPercentAttributes(
            ctx, ELEMENT_GRID_FORMAT_COLUMN, column);
        unmarshalStyleClassAttribute(
            ctx, ELEMENT_GRID_FORMAT_COLUMN, column);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMN);
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_GRID_FORMAT_COLUMN);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalGridFormatRow(final UnmarshallingContext ctx,
                                        final CanvasLayout canvasLayout,
                                        final Grid grid,
                                        final int index)
        throws JiBXException {

        final Row row = grid.getRow(index);
        unmarshalHeightPixelsOnlyAttribute(ctx, ELEMENT_GRID_FORMAT_ROW, row);
        unmarshalStyleClassAttribute(ctx, ELEMENT_GRID_FORMAT_ROW, row);
        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_GRID_FORMAT_ROW);
        final int base = index * grid.getColumns();
        for (int i = 0; !ctx.isEnd(); i++) {
            unmarshalChildFormat(
                grid, ctx, canvasLayout, base + i);
        }
        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_GRID_FORMAT_ROW);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private SpatialFormatIterator unmarshalSpatialFormatIterator(
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout)
        throws JiBXException {

        final SpatialFormatIterator spatialFormatIterator =
            new SpatialFormatIterator(canvasLayout);
        ctx.pushTrackedObject(spatialFormatIterator);
        unmarshalSpatialFormatIteratorAttributes(ctx, spatialFormatIterator);

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_SPATIAL_FORMAT_ITERATOR);

        unmarshalChildFormat(
            spatialFormatIterator, ctx, canvasLayout, 0);

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_SPATIAL_FORMAT_ITERATOR);
        ctx.popObject();

        return spatialFormatIterator;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalSpatialFormatIteratorAttributes(
            final UnmarshallingContext ctx,
            final SpatialFormatIterator spatialFormatIterator) {

        unmarshalFormatIteratorAttributes(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, spatialFormatIterator);
        unmarshalOptimizationLevelAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, spatialFormatIterator);
        unmarshalStyleClassAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, spatialFormatIterator);
        unmarshalDirectionAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, spatialFormatIterator);

        final String indexingDirection = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_INDEXING_DIRECTION);
        if (indexingDirection != null) {
            spatialFormatIterator.setIndexingDirection(indexingDirection);
        }
        final String rows = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ROWS);
        if (rows != null) {
            spatialFormatIterator.setRowsFlexibility(rows);
        }
        final String rowCount = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ROW_COUNT);
        if (rowCount != null) {
            spatialFormatIterator.setRows(rowCount);
        }
        final String rowStyleClasses = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ROW_STYLE_CLASSES);
        if (rowStyleClasses != null) {
            spatialFormatIterator.setRowStyleClassesAttribute(rowStyleClasses);
        }
        final String columns = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_COLUMNS);
        if (columns != null) {
            spatialFormatIterator.setColumnsFlexibility(columns);
        }
        final String columnCount = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_COLUMN_COUNT);
        if (columnCount != null) {
            spatialFormatIterator.setColumns(columnCount);
        }
        final String columnStyleClasses =
            unmarshalAttribute(ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR,
                ATTR_COLUMN_STYLE_CLASSES);
        if (columnStyleClasses != null) {
            spatialFormatIterator.setColumnStyleClassesAttribute(
                columnStyleClasses);
        }
        final String alignContent = unmarshalAttribute(
            ctx, ELEMENT_SPATIAL_FORMAT_ITERATOR, ATTR_ALIGN_CONTENT);
        if (alignContent != null) {
            spatialFormatIterator.setAlignContent(alignContent);
        }
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalFormatIteratorAttributes(
            final UnmarshallingContext ctx,
            final String elementName,
            final Format format) {

        unmarshalRequiredName(ctx, elementName, format);
        unmarshalAllPaneAndGridAndIteratorAttributes(ctx, elementName, format);
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private TemporalFormatIterator unmarshalTemporalFormatIterator(
            final UnmarshallingContext ctx,
            final CanvasLayout canvasLayout)
        throws JiBXException {

        final TemporalFormatIterator temporalFormatIterator =
            new TemporalFormatIterator(canvasLayout);
        ctx.pushTrackedObject(temporalFormatIterator);
        unmarshalTemporalFormatIteratorAttributes(ctx, temporalFormatIterator);

        ctx.parsePastStartTag(URL_MARLIN, ELEMENT_TEMPORAL_FORMAT_ITERATOR);

        unmarshalChildFormat(
            temporalFormatIterator, ctx, canvasLayout, 0);

        ctx.parsePastEndTag(URL_MARLIN, ELEMENT_TEMPORAL_FORMAT_ITERATOR);
        ctx.popObject();

        return temporalFormatIterator;
    }

    /**
     * Unmarshals the corresponding XML Schema entry
     */
    private void unmarshalTemporalFormatIteratorAttributes(
            final UnmarshallingContext ctx,
            final TemporalFormatIterator temporalFormatIterator) {

        unmarshalFormatIteratorAttributes(
            ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, temporalFormatIterator);

        final String clockValues = unmarshalAttribute(
            ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, ATTR_CLOCK_VALUES);
        if (clockValues != null) {
            temporalFormatIterator.setClockValues(clockValues);
        }
        final String cells = unmarshalAttribute(
            ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, ATTR_CELLS);
        if (cells != null) {
            temporalFormatIterator.setCells(cells);
        }
        final String cellCount = unmarshalAttribute(
            ctx, ELEMENT_TEMPORAL_FORMAT_ITERATOR, ATTR_CELL_COUNT);
        if (cellCount != null) {
            temporalFormatIterator.setCellCount(cellCount);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/2	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 03-Oct-05	9590/1	schaloner	VBM:2005092204 Updated formatCount in each format in DeviceLayout

 02-Oct-05	9652/7	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 ===========================================================================
*/
