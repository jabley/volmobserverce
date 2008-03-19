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
package com.volantis.mcs.accessors.xml.jdom;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * This class provides translations of attibute names and their values between,
 * on the one hand, the various repository objects and, on the other, the new
 * (ie version 3 forwards) schema.  The attribute names and values used by the
 * repository objects are derived from the old DTD.
 * <p>
 * This class creates substantial internal data structures and it is preferable
 * that an instance is created as a long lived member of classes that require its
 * services.  It is initially designed to be used by XMLAccessorHelper working with
 * JDOMXMLDeviceLayoutAccessor to provide import/export services.
 */
public class XMLLayoutAttributeTranslations {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(XMLLayoutAttributeTranslations.class);

    /**
     * Create a new instance.  Note: performs moderately extensive initialisation
     * of internal data structures.
     */
    private static XMLLayoutAttributeTranslations instance =
            new XMLLayoutAttributeTranslations();

    public static XMLLayoutAttributeTranslations getInstance() {
        return instance;
    }

    /**
     * the set of attributes which have choice values
     */
    private HashMap choiceAttributes;

    /**
     * the legal attributes on each element
     */
    private HashMap element2attribute;

    /**
     * This internal class provides translation relevant information for each
     * element in the v 3.0 schema seen by the accessor.
     */
    protected class TranslationElementInfo {

        /**
         * The translator for write operaions.  (Writing to v3 xml)
         */
        private final WriteNameTranslator writeNameTranslator;

        /**
         * The translator for read operations.  (Reading from v3 xml)
         */
        private final ReadNameTranslator readNameTranslator;

        /**
         * Construct a new instance from a fully constructed
         * AggregatingNameTable
         * @param attributeTranslations
         */
        public TranslationElementInfo (AggregatingNameTable attributeTranslations) {
            writeNameTranslator = new WriteNameTranslator();
            readNameTranslator = new ReadNameTranslator();
            attributeTranslations.registerAll(writeNameTranslator);
            attributeTranslations.registerAll(readNameTranslator);
        }

        /**
         * Get the write translator
         * @return The translator
         */
        public WriteNameTranslator getWriteNameTranslator() {
            return writeNameTranslator;
        }
    }

    private XMLLayoutAttributeTranslations() {
        initialiseMappings();
    }

    private NameTable dissectingPaneLinkOrder;
    private NameTable fragmentLinkOrder;

    /**
     * Create all the data structures for context sensitive (element specific)
     * attribute translations.
     */
    private void initialiseMappings() {

        choiceAttributes = new HashMap();
        // NOTE: choice attributes are not currently keyed per element.
        // This means that it is open to problems where different elements have
        // different definitions of the same attribute. Luckily this does not
        // affect the choice attributes often, but it does affect the "rows"
        // and "columns" attribute. This can be seen in hack code present in
        // the method which uses this data structure.

        /*
        <xs:attribute name="backgroundComponentType" use="optional">
            <xs:annotation>
                <xs:documentation>The type of the background component.</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="image"/>
                    <xs:enumeration value="dynamic-visual"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable backgroundComponentType = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.BACKGROUND_COMPONENT_TYPE_IMAGE,
                        "image");

                // Argh - this is a mess.
                // The old gui uses "Dynamic Visual"
                // The runtime uses "Dynamic Visual" or "DynamicVisual"
                // The format uses "DynamicVisual" as it's default value.
                // So, we use a hack, making sure it accomodates both
                // migration and import/export.

                // Translate the default old value to the new value (only).
                // This works only in forward direction, as it's entry
                // in the reverse lookup table will be replaced by the next
                // name registered with the same value... which is...
                r.registerNameTranslation(
                        "Dynamic Visual",
                        "dynamic-visual");
                // Translate between the runtime old value and the new value.
                // This works in both forward and reverse direction.
                r.registerNameTranslation(
                        FormatConstants.BACKGROUND_COMPONENT_TYPE_DYNAMIC_VISUAL,
                        "dynamic-visual");
            }
        };
        choiceAttributes.put("backgroundComponentType", backgroundComponentType);

        /*
        <xs:attribute name="heightUnits" type="PixelsOrPercentType" use="optional">
            <xs:annotation>
                <xs:documentation>The unit part of the height. Default: percent</xs:documentation>
            </xs:annotation>
        </xs:attribute>
        <xs:simpleType name="PixelsOrPercentType">
            <xs:restriction base="xs:string">
                <xs:enumeration value="pixels"/>
                <xs:enumeration value="percent"/>
            </xs:restriction>
        </xs:simpleType>
        */
        NameTable heightUnitsPixelsOrPercentType = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.HEIGHT_UNITS_VALUE_PERCENT, "percent");
                r.registerNameTranslation(
                        FormatConstants.HEIGHT_UNITS_VALUE_PIXELS, "pixels");
            }
        };
        choiceAttributes.put("heightUnits", heightUnitsPixelsOrPercentType);

        /*
        <xs:attribute name="horizontalAlignment" use="optional">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="left"/>
                    <xs:enumeration value="center"/>
                    <xs:enumeration value="right"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable horizontalAlignment = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_LEFT,
                        "left");
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER,
                        "center");
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT,
                        "right");
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_START,
                        "start");
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_END,
                        "end");
            }
        };
        choiceAttributes.put("horizontalAlignment", horizontalAlignment);

        /*
        <xs:attribute name="optimizationLevel" use="optional">
            <xs:annotation>
                <xs:documentation>Specifies whether the representation of this format can be optimized.</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:NCName">
                    <xs:enumeration value="never"/>
                    <xs:enumeration value="little-impact"/>
                    <xs:enumeration value="always"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable optimizationLevel = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.OPTIMIZATION_LEVEL_VALUE_NEVER,
                        "never");
                r.registerNameTranslation(
                        FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT,
                        "little-impact");
                r.registerNameTranslation(
                        FormatConstants.OPTIMIZATION_LEVEL_VALUE_ALWAYS,
                        "always");
            }
        };
        choiceAttributes.put("optimizationLevel", optimizationLevel);

        /*
        <xs:attribute name="verticalAlignment" use="optional">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="top"/>
                    <xs:enumeration value="center"/>
                    <xs:enumeration value="bottom"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable verticalAlignment = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.VERTICAL_ALIGNMENT_VALUE_TOP,
                        "top");
                r.registerNameTranslation(
                        FormatConstants.VERTICAL_ALIGNMENT_VALUE_CENTER,
                        "center");
                r.registerNameTranslation(
                        FormatConstants.VERTICAL_ALIGNMENT_VALUE_BOTTOM,
                        "bottom");
            }
        };
        choiceAttributes.put("verticalAlignment", verticalAlignment);

        /*
        <xs:attribute name="widthUnits" type="PixelsOrPercentType" use="optional">
            <xs:annotation>
                <xs:documentation>The unit part of the width. Default: percent</xs:documentation>
            </xs:annotation>
        </xs:attribute>
        <xs:simpleType name="PixelsOrPercentType">
            <xs:restriction base="xs:string">
                <xs:enumeration value="pixels"/>
                <xs:enumeration value="percent"/>
            </xs:restriction>
        </xs:simpleType>
         */
        NameTable widthUnitsPixelsOrPercentType = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.WIDTH_UNITS_VALUE_PERCENT, "percent");
                r.registerNameTranslation(
                        FormatConstants.WIDTH_UNITS_VALUE_PIXELS, "pixels");
            }
        };
        choiceAttributes.put("widthUnits", widthUnitsPixelsOrPercentType);

        /*
        <xs:attribute name="indexingDirection" use="optional">
            <xs:annotation>
                <xs:documentation>Specifies the order in which the format iterator will insert format contents into the effective layout. Default: across-down</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="across-down"/>
                    <xs:enumeration value="down-across"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable indexingDirection = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation("AcrossDown", "across-down");
                r.registerNameTranslation("DownAcross", "down-across");
            }
        };
        choiceAttributes.put("indexingDirection", indexingDirection);

        /*
        FOR SPATIAL FORMAT ITERATOR (NOT GRID)
        <xs:attribute name="rows" use="required">
            <xs:annotation>
                <xs:documentation>Specifies whether the effective layout created by the spatial format iterator has a fixed or variable number of rows.</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="fixed"/>
                    <xs:enumeration value="variable"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        NameTable formatIteratorRowsColumnsCells = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                // Argh - this is a mess.
                // The old gui uses "variable" and "fixed"
                // The runtime uses "fixed" and !"fixed" to be variable
                // The format uses "Variable" as it's default value.
                // So, we use a hack, making sure it accomodates both
                // migration and import/export.

                // Translate the default old value to the new value (only).
                // This one works only in forward direction, as it's entry
                // in the reverse lookup table will be replaced by the next
                // name registered with the same value... which is...
                r.registerNameTranslation("Variable", "variable");
                // Translate between the bodgy old values and the new values.
                // These two work in both forward and reverse direction.
                r.registerNameTranslation("variable", "variable");
                r.registerNameTranslation("fixed", "fixed");
            }
        };
        choiceAttributes.put("rows", formatIteratorRowsColumnsCells);

        /*
        FOR SPATIAL FORMAT ITERATOR (NOT GRID)
        <xs:attribute name="columns" use="required">
            <xs:annotation>
                <xs:documentation>Specifies whether the effective layout created by the spatial format iterator has a fixed or variable number of columns.</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="fixed"/>
                    <xs:enumeration value="variable"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        choiceAttributes.put("columns", formatIteratorRowsColumnsCells);

        /*
        <xs:attribute name="cells" use="required">
            <xs:annotation>
                <xs:documentation>Specifies whether the effective layout created by the temporal format iterator has a fixed or variable number of cells.</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="fixed"/>
                    <xs:enumeration value="variable"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
         */
        choiceAttributes.put("cells", formatIteratorRowsColumnsCells);

        /*
        <xs:attributeGroup name="ScrollingAttribute">
            <xs:annotation>
                <xs:documentation>Specifies whether scroll bars should be generated around the frame for the format.</xs:documentation>
            </xs:annotation>
            <xs:attribute name="scrolling" use="optional">
                <xs:simpleType>
                    <xs:restriction base="xs:NCName">
                        <xs:enumeration value="auto"/>
                        <xs:enumeration value="always"/>
                        <xs:enumeration value="never"/>
                    </xs:restriction>
                </xs:simpleType>
            </xs:attribute>
        </xs:attributeGroup>
         */
        NameTable scrolling = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.SCROLLING_VALUE_AUTOMATIC, "auto");
                r.registerNameTranslation(
                        FormatConstants.SCROLLING_VALUE_YES, "always");
                r.registerNameTranslation(
                        FormatConstants.SCROLLING_VALUE_NO, "never");
            }
        };
        choiceAttributes.put("scrolling", scrolling);

        /*
        <xs:attribute name="sourceFormatType" use="required">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="grid"/>
                    <xs:enumeration value="pane"/>
                    <xs:enumeration value="form"/>
                    <xs:enumeration value="region"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
        */
        NameTable sourceFormatType = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatType.GRID.getTypeName(), "grid");
                r.registerNameTranslation(
                        FormatType.PANE.getTypeName(), "pane");
                r.registerNameTranslation(
                        FormatType.FORM.getTypeName(), "form");
                r.registerNameTranslation(
                        FormatType.REGION.getTypeName(), "region");
            }
        };
        choiceAttributes.put("sourceFormatType", sourceFormatType);

        /*
        <xs:simpleType name="FormFragmentLinkPositionType">
            <xs:restriction base="xs:string">
                <xs:enumeration value="before"/>
                <xs:enumeration value="after"/>
            </xs:restriction>
        </xs:simpleType>
        */
        NameTable formFragmentLinkPositionType = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation("true", "after");
                r.registerNameTranslation("false", "before");
            }
        };
        choiceAttributes.put("nextLinkPosition", formFragmentLinkPositionType);
        choiceAttributes.put("previousLinkPosition", formFragmentLinkPositionType);


        /*
        DISSECTING PANE
        <xs:attribute name="linkOrder" use="optional">
            <xs:annotation>
                <xs:documentation>The maximum content size of the dissecting pane. Dissection will occur if the content size exceeds this value. Default: next-first</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="next-first"/>
                    <xs:enumeration value="previous-first"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
        */
        dissectingPaneLinkOrder = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.PREV_FIRST, "previous-first");
                r.registerNameTranslation(
                        FormatConstants.NEXT_FIRST, "next-first");
            }
        };
        choiceAttributes.put("shardLinkOrder", dissectingPaneLinkOrder);

        /*
        FRAGMENT
        <xs:attribute name="linkOrder" use="optional">
            <xs:annotation>
                <xs:documentation>Default: peers-first</xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction base="xs:NCName">
                    <xs:enumeration value="parent-first"/>
                    <xs:enumeration value="peers-first"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
        */
        fragmentLinkOrder = new NameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.PARENT_FIRST, "parent-first");
                r.registerNameTranslation(
                        FormatConstants.PEERS_FIRST, "peers-first");
            }
        };
        choiceAttributes.put("fragmentLinkOrder", fragmentLinkOrder);

        // HACK: choiceAttributes cannot handle reverse translating the
        // same values to diffent values because it doesn't utilise the
        // lhs attribute name. So, link order is currently handled with
        // hack code in the translation methods below.

        // now assemble the valid attributes as nearly as possible
        // in the same way that the schema does - although we can't
        // keep the declarations in the same order.

        AggregatingNameTable gridDimensionAttributesTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        Grid.ROWS_ATTRIBUTE, "rows");
                r.registerNameTranslation(
                        Grid.COLUMNS_ATTRIBUTE, "columns");
            }
        };

        AggregatingNameTable borderColorAttributeTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.BORDER_COLOUR_ATTRIBUTE,
                        "borderColor");
            }
        };

        AggregatingNameTable borderWidthAttributeTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.BORDER_WIDTH_ATTRIBUTE,
                        "borderWidth");
            }
        };

        AggregatingNameTable frameBorderAttributeTable = new AggregatingNameTable()
        {
            public void registerNames (NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.FRAME_BORDER_ATTRIBUTE,
                        "frameBorder");
            }
        };


        AggregatingNameTable frameSpacingAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.FRAME_SPACING_ATTRIBUTE,
                        "frameSpacing");
            }
        };

        AggregatingNameTable requiredNameTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.NAME_ATTRIBUTE,
                        "name");
            }
        };

        AggregatingNameTable destinationAreaAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.DESTINATION_AREA_ATTRIBUTE,
                        "destinationArea");
            }
        };

        AggregatingNameTable marginAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.MARGIN_HEIGHT_ATTRIBUTE,
                        "marginHeight");
                r.registerNameTranslation(
                        FormatConstants.MARGIN_WIDTH_ATTRIBUTE,
                        "marginWidth");
            }
        };

        AggregatingNameTable resizeAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.RESIZE_ATTRIBUTE,
                        "resize");
            }
        };

        AggregatingNameTable scrollingAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.SCROLLING_ATTRIBUTE,
                        "scrolling");
            }
        };

        AggregatingNameTable filterKeyboardUsabilityAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
                        "filterOnKeyboardUsability");
            }
        };

        AggregatingNameTable optionalNameTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.NAME_ATTRIBUTE,
                        "name");
            }
        };


        AggregatingNameTable backgroundColorAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
                        "backgroundColor");
            }
        };

        AggregatingNameTable backgroundComponentAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                // this doesn't look right (duplicate target?), so I've
                // disabled it. Feel free to enable it (and document why) if
                // I was wrong.
//                r.registerNameTranslation(
//                        "BackgroundImage",
//                        "backgroundComponent");
                r.registerNameTranslation(
                        FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
                        "backgroundComponent");
                r.registerNameTranslation(
                        FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
                        "backgroundComponentType");
            }
        };

        AggregatingNameTable cellPaddingAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.CELL_PADDING_ATTRIBUTE,
                        "cellPadding");
            }
        };

        AggregatingNameTable cellSpacingAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.CELL_SPACING_ATTRIBUTE,
                        "cellSpacing");
            }
        };

        AggregatingNameTable heightPixelsOnlyAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.HEIGHT_ATTRIBUTE, "height");
            }
        };

        AggregatingNameTable horizontalAlignmentAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
                        "horizontalAlignment");
            }
        };

        AggregatingNameTable verticalAlignmentAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
                        "verticalAlignment");
            }
        };

        AggregatingNameTable widthPixelsOrPercentAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.WIDTH_ATTRIBUTE,
                        "width");
                r.registerNameTranslation(
                        FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                        "widthUnits");
            }
        };

        AggregatingNameTable optimizationLevelAttributeTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
                        "optimizationLevel");
            }
        };

        AggregatingNameTable allPaneAndGridAndIteratorAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {}
            };
        allPaneAndGridAndIteratorAttributesTable.addAll(backgroundColorAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(backgroundComponentAttributesTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(borderWidthAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(cellPaddingAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(cellSpacingAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(heightPixelsOnlyAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(horizontalAlignmentAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(verticalAlignmentAttributeTable);
        allPaneAndGridAndIteratorAttributesTable.addAll(widthPixelsOrPercentAttributesTable);

        AggregatingNameTable allPaneAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {}
        };
        allPaneAttributesTable.addAll(requiredNameTable);
        allPaneAttributesTable.addAll(allPaneAndGridAndIteratorAttributesTable);
        allPaneAttributesTable.addAll(filterKeyboardUsabilityAttributeTable);

        AggregatingNameTable styleClassAttributesTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {}
        };

        AggregatingNameTable styleClassTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.STYLE_CLASS,
                        "styleClass");
            }
        };
        styleClassAttributesTable.addAll(styleClassTable);

        AggregatingNameTable additionalNonDissectablePaneAndGridAttributesTable  =
                new AggregatingNameTable() {
                    public void registerNames(NameTranslationRegistrar r) { }
                };
        additionalNonDissectablePaneAndGridAttributesTable.addAll(optimizationLevelAttributeTable);

        AggregatingNameTable nonDissectingPaneAttributesTable  =
                new AggregatingNameTable() {
                    public void registerNames(NameTranslationRegistrar r) { }
                };
        nonDissectingPaneAttributesTable.addAll(allPaneAttributesTable);
        nonDissectingPaneAttributesTable.addAll(additionalNonDissectablePaneAndGridAttributesTable);
        nonDissectingPaneAttributesTable.addAll(destinationAreaAttributeTable);

        AggregatingNameTable dissectingPaneAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE,
                        "nextLinkText");
                r.registerNameTranslation(
                        FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
                        "nextLinkStyleClass");
                r.registerNameTranslation(
                        FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
                        "nextLinkShortcut");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE,
                        "previousLinkText");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
                        "previousLinkStyleClass");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
                        "previousLinkShortcut");
                r.registerNameTranslation(
                        FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE,
                        "maxContentSize");
                r.registerNameTranslation(
                        FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE,
                        "shardLinkOrder");
            }
        };
        dissectingPaneAttributesTable.addAll(allPaneAttributesTable);

        AggregatingNameTable gridAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        gridAttributesTable.addAll(optionalNameTable);
        gridAttributesTable.addAll(gridDimensionAttributesTable);
        gridAttributesTable.addAll(allPaneAndGridAndIteratorAttributesTable);
        gridAttributesTable.addAll(additionalNonDissectablePaneAndGridAttributesTable );
        gridAttributesTable.addAll(styleClassAttributesTable);

        AggregatingNameTable formatIteratorAttributesTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        formatIteratorAttributesTable.addAll(requiredNameTable);
        formatIteratorAttributesTable.addAll(allPaneAndGridAndIteratorAttributesTable);

        AggregatingNameTable spatialFormatIteratorAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.ITERATOR_2D_INDEXING_DIR_ATTRIBUTE,
                        "indexingDirection");
                r.registerNameTranslation(
                        FormatConstants.ITERATOR_ROWS_ATTRIBUTE,
                        "rows");
                r.registerNameTranslation(
                        FormatConstants.ITERATOR_ROW_COUNT_ATTRIBUTE,
                        "rowCount");
                r.registerNameTranslation(
                        FormatConstants.ITERATOR_COLUMNS_ATTRIBUTE,
                        "columns");
                r.registerNameTranslation(
                        FormatConstants.ITERATOR_COLUMN_COUNT_ATTRIBUTE,
                        "columnCount");
                r.registerNameTranslation(
                        SpatialFormatIterator.ROW_STYLE_CLASSES,
                        "rowStyleClasses");
                r.registerNameTranslation(
                        SpatialFormatIterator.COLUMN_STYLE_CLASSES,
                        "columnStyleClasses");
                r.registerNameTranslation(
                        SpatialFormatIterator.SPATIAL_ITERATOR_ALIGN_CONTENT,
                        "alignContent");
            }
        };
        spatialFormatIteratorAttributesTable.addAll(formatIteratorAttributesTable);
        spatialFormatIteratorAttributesTable.addAll(styleClassAttributesTable);
        spatialFormatIteratorAttributesTable.addAll(
                optimizationLevelAttributeTable);

        AggregatingNameTable temporalFormatIteratorAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES,
                        "clockValues");
                r.registerNameTranslation(
                        TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,
                        "cells");
                r.registerNameTranslation(
                        TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,
                        "cellCount");
            }
        };
        temporalFormatIteratorAttributesTable.addAll(formatIteratorAttributesTable);

        AggregatingNameTable deviceLayoutFormatAttributesTable  = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                // NOTE: deviceName translated externally (?)
                r.registerNameTranslation(
                        Layout.LAYOUT_GROUP_NAME_ATTRIBUTE,
                        "groupName");
                r.registerNameTranslation(
                        Layout.DESTINATION_LAYOUT_ATTRIBUTE,
                        "destinationLayout");
            }
        };


        // and add the valid attributes to elements
        element2attribute = new HashMap();
        AggregatingNameTable elementNameTable;

        // columnIteratorPaneFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(nonDissectingPaneAttributesTable);
        element2attribute.put("columnIteratorPaneFormat",
                new TranslationElementInfo(elementNameTable));

        // deviceLayoutFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
                        "defaultFragment");
            }
        };
        elementNameTable.addAll(deviceLayoutFormatAttributesTable);
        element2attribute.put("deviceLayoutCanvasFormat",
                new TranslationElementInfo(elementNameTable));

        // paneFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(nonDissectingPaneAttributesTable);

        element2attribute.put("paneFormat",
                new TranslationElementInfo(elementNameTable));

        // segmentGridFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
        /* Note: marlin-rpdm.dtd has attributes for gridRows and gridColumns
           that, in effect, appear on both gridFormat and segmentGridFormat but
           the information they represent is transformed into segmentGridRow and
           segmentGridFormatColumns sub elements in the schema and effectively
           disappear.  This is the subject of special processing in both
           migration and accessors.
         */
            }
        };
        elementNameTable.addAll(gridDimensionAttributesTable);
        elementNameTable.addAll(borderColorAttributeTable);
        elementNameTable.addAll(borderWidthAttributeTable);
        elementNameTable.addAll(frameBorderAttributeTable);
        elementNameTable.addAll(frameSpacingAttributeTable);

        element2attribute.put("segmentGridFormat",
                new TranslationElementInfo(elementNameTable));

        // gridFormatColumn
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(widthPixelsOrPercentAttributesTable);
        element2attribute.put("segmentGridFormatColumn",
                new TranslationElementInfo(elementNameTable ));

        // gridFormatRow -
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
            }
        };
        elementNameTable.addAll(heightPixelsOnlyAttributeTable);
        element2attribute.put("segmentGridFormatRow",
                new TranslationElementInfo(elementNameTable ));

        // regionFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(requiredNameTable);
        elementNameTable.addAll(destinationAreaAttributeTable);
        element2attribute.put("regionFormat",
                new TranslationElementInfo(elementNameTable));

        // formFragmentFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.RESET_ATTRIBUTE,
                        "allowReset");
                r.registerNameTranslation(
                        FormatConstants.NEXT_LINK_STYLE_ATTRIBUTE,
                        "nextLinkStyleClass");
                r.registerNameTranslation(
                        FormatConstants.NEXT_LINK_POSITION_ATTRIBUTE,
                        "nextLinkPosition");
                r.registerNameTranslation(
                        FormatConstants.NEXT_LINK_TEXT_ATTRIBUTE,
                        "nextLinkText");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_LINK_STYLE_ATTRIBUTE,
                        "previousLinkStyleClass");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_LINK_POSITION_ATTRIBUTE,
                        "previousLinkPosition");
                r.registerNameTranslation(
                        FormatConstants.PREVIOUS_LINK_TEXT_ATTRIBUTE,
                        "previousLinkText");
            }
        };
        elementNameTable.addAll (requiredNameTable);
        element2attribute.put("formFragmentFormat",
                new TranslationElementInfo(elementNameTable));

        // fragmentFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        FormatConstants.LINK_TEXT_ATTRIBUTE,
                        "linkText");
                r.registerNameTranslation(
                        FormatConstants.BACK_LINK_TEXT_ATTRIBUTE,
                        "backLinkText");
                r.registerNameTranslation(
                        FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE,
                        "linkStyleClass");
                r.registerNameTranslation(
                        FormatConstants.PEER_LINK_ATTRIBUTE,
                        "showPeerLinks");
                r.registerNameTranslation(
                        FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE,
                        "fragmentLinkOrder");
            }
        };
        elementNameTable.addAll(requiredNameTable);
        element2attribute.put("fragmentFormat",
                new TranslationElementInfo(elementNameTable));

        // rowIteratorPaneFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(nonDissectingPaneAttributesTable);
        element2attribute.put("rowIteratorPaneFormat",
                new TranslationElementInfo(elementNameTable));

        // dissectingPaneFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(dissectingPaneAttributesTable);
        element2attribute.put("dissectingPaneFormat",
                new TranslationElementInfo(elementNameTable));

        // replicaFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        Replica.REPLICANT_ATTRIBUTE, "sourceFormatName");
                r.registerNameTranslation(
                        Replica.REPLICANT_TYPE_ATTRIBUTE, "sourceFormatType");
            }
        };
        elementNameTable.addAll(requiredNameTable);
        element2attribute.put("replicaFormat",
                new TranslationElementInfo(elementNameTable));

        // gridFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(gridAttributesTable);
        elementNameTable.addAll(allPaneAndGridAndIteratorAttributesTable);
        element2attribute.put("gridFormat",
                new TranslationElementInfo(elementNameTable));

        // gridFormatColumn
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(widthPixelsOrPercentAttributesTable);
        elementNameTable.addAll(styleClassAttributesTable);
        element2attribute.put("gridFormatColumn",
                new TranslationElementInfo(elementNameTable));

        // gridFormatRow
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(heightPixelsOnlyAttributeTable);
        elementNameTable.addAll(styleClassAttributesTable);
        element2attribute.put("gridFormatRow",
                new TranslationElementInfo(elementNameTable));

        // formFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(requiredNameTable );
        element2attribute.put("formFormat",
                new TranslationElementInfo(elementNameTable));

        // segmentFormat
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(requiredNameTable);
        elementNameTable.addAll(borderColorAttributeTable);
        elementNameTable.addAll(frameBorderAttributeTable);
        elementNameTable.addAll(marginAttributesTable);
        elementNameTable.addAll(resizeAttributesTable);
        elementNameTable.addAll(scrollingAttributeTable);
        element2attribute.put("segmentFormat",
                new TranslationElementInfo(elementNameTable));

        // spatialFormatIterator
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(spatialFormatIteratorAttributesTable);
        element2attribute.put("spatialFormatIterator",
                new TranslationElementInfo(elementNameTable));

        // temporalFormatIterator
        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) { }
        };
        elementNameTable.addAll(temporalFormatIteratorAttributesTable);
        element2attribute.put("temporalFormatIterator",
                new TranslationElementInfo(elementNameTable));

        elementNameTable = new AggregatingNameTable() {
            public void registerNames(NameTranslationRegistrar r) {
                r.registerNameTranslation(
                        Layout.DEFAULT_SEGMENT_NAME_ATTRIBUTE,
                        "defaultSegment");
            }
        };
        elementNameTable.addAll(deviceLayoutFormatAttributesTable);
        element2attribute.put("deviceLayoutMontageFormat",
                new TranslationElementInfo(elementNameTable));

    }

    /**
     * Check if the specifed attribute is one of the two QuotedComponentReference types:
     * QuotedComponentReferenceOrStyleClassType and QuotedComponentReferenceOrLiteralTextType.
     * These type can optionally contain component references inside curly braces.
     *
     * @param elementName
     * @param attributeName
     * @return boolean flag
     */
    public boolean attributeIsQuotedComponentType ( String elementName, String attributeName ) {
        boolean result = false;
        if (
                attributeName.equals("linkText") ||
                attributeName.equals("backLinkText") ||
                attributeName.equals("nextLinkStyleClass") ||
                attributeName.equals("nextLinkText") ||
                attributeName.equals("previousLinkStyleClass") ||
                attributeName.equals("previousLinkText") ||
                attributeName.equals("linkText") ||
                attributeName.equals("backLinkText") ) {

            result = true;

        }
        return result;
    }
    /**
     * Check that the specified value is a legal policy name / policy reference.
     * A legal name consists of letters,digits,'_','-','.','/'
     * @param val
     * @return true if val was legal.  false if illegal.
     */
    public boolean valueIsValidPolicyReference ( String  val ) {

        boolean result = true;

        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);

            if ( ! (Character.isLetterOrDigit(ch) || ch == '_' || ch == '-' || ch == '.' || ch == '/') ) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Translate a new (v3.0) attribute value the corresponding old one.
     *
     * @param elementName  the element name, in new (3.0) format
     * @param attributeName the attribute name, in new (3.0) format
     * @param value the attribute value to be translated
     * @return the translated value , or null if none could be found.
     */
    public String translateNewToOldAttributeValue (String elementName,
                                       String attributeName, String value) {
        String result = null;

        // in the old format the value of ClockValues is comma separated not
        // space separated.
        if (attributeName.equals("clockValues")) {
            if (value.indexOf(" ") > -1) {
                StringTokenizer tokenizer = new StringTokenizer(value, " ");
                StringBuffer buf = new StringBuffer();
                while (tokenizer.hasMoreElements()) {
                    String token = (String) tokenizer.nextElement();
                    buf.append(token.trim());
                    if (tokenizer.hasMoreElements()) {
                        buf.append(", ");
                    }
                }
                result = buf.toString();
            } else {
                result = value;
            }
        } else {
            // Hmm. Maybe this is creating just a bit too much garbage?
            result = translateNormalAttributeValue(elementName, attributeName,
                    value, new ReadNameTranslator());
        }

        return result;
    }

    /**
     * Translate an old internal attribute name to a new schema attribute name
     * @param elementName The name of the element (new format) to which the
     * attribue belongs
     * @param attributeName The old attribute name to translate
     * @return the new attribute name, or null if no translation can be found.
     */
    public String translateOldToNewAttributeName (String elementName, String attributeName) {

        // NOTE: this code is almost identical to the NewToOld version...
        String result = null;
        TranslationElementInfo info = (TranslationElementInfo) element2attribute.get(elementName);
        if (info == null) {
            logger.error("element-info-null", new Object[]{elementName});
        } else {
            result = info.getWriteNameTranslator().translate(attributeName);
        }
        if (logger.isDebugEnabled()) {
            logger.info("attribute-translated", new Object[]{attributeName, elementName, result});
        }
        return result;

    }

    /**
     * Translate a version 2.9 attribute value to its v 3 equivalent
     * @param elementName the element name, in new (3.0) format
     * @param attributeName the attribute name, in new (3.0) format
     * @param value the value to be translated
     * @return the translated value, or null if none could be found.
     */
    public String translateOldToNewAttributeValue (String elementName,
                                                   String attributeName, String value ) {
        String result = null;

        // the value of clock values needs special conversion - the list has to be
        // space separated rather than comma separated
        if ( attributeName.equals("clockValues")) {
            if (value.indexOf(",") > -1 ) {
                StringTokenizer tokenizer = new StringTokenizer(value, ",");
                StringBuffer buf = new StringBuffer ();
                while (tokenizer.hasMoreElements()) {
                    String token = (String) tokenizer.nextElement();
                    buf.append(token.trim());
                    if (tokenizer.hasMoreElements()) {
                        buf.append(" ");
                    }
                }

                result = buf.toString();
            } else {
                result = value;
            }
        } else {
            // Hmm. Maybe this is creating just a bit too much garbage?
            result = translateNormalAttributeValue(elementName, attributeName,
                    value, new WriteNameTranslator());
        }

        return result;
    }

    /**
     * Translate a "normal" attribute value (i.e. one which does not require
     * special processing).
     * <p>
     * Note that attribute value translations always require the new (3.0)
     * element and attribute names to key off, since the underlying data
     * structures have only these new (3.0) values.
     * <p>
     * This may be problematic if we are translating from new (3.0) to old
     * (2.9) and the attribute value translation is being performed on
     * attributes which themselves have been translated, and if the translated
     * attribute names are ambiguous in the old (2.9) form and have
     * different types. Or something like that. Hopefully this will never
     * occur but we need to be vigilant...
     *
     * One of the values that was had special handling was linkOrder. This
     * element name has been updated to fragmentLinkOrder and shardLinkOrder
     * so that we can differentiate between the next-first/previous-first and
     * parent-first/peers-first contained attributes.
     *
     * @param elementName the element name, in new (3.0) format
     * @param attributeName the attribute name, in new (3.0) format
     * @param value the value to be translated
     * @param translator a freshly minted translator of the type required.
     * @return the translated value, or null if none could be found.
     */
    private String translateNormalAttributeValue(String elementName,
            String attributeName, String value, NameTranslator translator) {

        String result = null;

        NameTable table = (NameTable) choiceAttributes.get(attributeName);

        // Filter out any attributes matches on elements which are not allowed
        // I.e. same attribute name but different value type.
        if (table != null) {
            if (!elementName.equals(XMLAccessorConstants.
                    SPATIAL_FORMAT_ITERATOR_ELEMENT) &&
                    (attributeName.equals("rows") ||
                        attributeName.equals("columns"))) {
                table = null;
            }
        }

        // Translate the value.
        if (table != null) {
            table.registerNames(translator);
            result = translator.translate(value);

            // If this value is invalid, then there will be no translation for
            // it. In this case normally we would just thow it away, but then
            // we couldn't validate it, so now we keep the invalid value. Yes
            // this is bodgy but I couldn't think of a better way.
            if (result == null) {
                result = value;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Translated <" + elementName + " " +
                        attributeName + "='" + value + "' to '" + result + "'");
            }
        } else {
            // if not, no translation should be required
            result = value;
        }
        return result;

    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	6387/1	adrianj	VBM:2004120205 Added content align attribute to Spatial Iterator format

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 26-Oct-04	5977/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 26-Oct-04	5954/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 12-Oct-04	5769/4	byron	VBM:2004100805 Support style classes on grids and spatial format iterators: Accessors - rework issues

 11-Oct-04	5769/2	byron	VBM:2004100805 Support style classes on grids and spatial format iterators: Accessors

 14-Jul-04	4871/1	adrianj	VBM:2003112107 Added optimisation attribute to spatial format iterators

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 26-Feb-04	3223/1	byron	VBM:2004022306 Layout Editor: Issues with Dissecting Pane

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 29-Jan-04	2754/1	tony	VBM:2004011205 removed FormatTranslator and minor fixes

 23-Jan-04	2620/12	tony	VBM:2004011306 de singletonised PolicyNameTranslator and misc cleanups

 23-Jan-04	2620/10	tony	VBM:2004011306 resolved merge conflicts

 21-Jan-04	2620/7	tony	VBM:2004011306 now definitely outputs the correct component and policy translations in migration

 19-Jan-04	2620/2	tony	VBM:2004011306 fixed translation, validation and logging of component/policy names in migration

 22-Jan-04	2696/7	geoff	VBM:2004012101 Import/Export: XML: Layouts: do attribute value translations per attribute

 22-Jan-04	2696/5	geoff	VBM:2004012101 Import/Export: XML: Layouts: do attribute value translations per attribute

 20-Jan-04	2687/1	geoff	VBM:2004011605 Import:Export: sourceFormatType missing from element replicaFormat.

 13-Jan-04	2519/1	andy	VBM:2004010811 moved gridFormat migration bug fix to new Translator class

 12-Jan-04	2304/7	tony	VBM:2003121708 fixed several accessor bugs and integrated migrate30/accessor translations

 09-Jan-04	2343/1	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (import works now)

 07-Jan-04	2304/1	tony	VBM:2003121708 temp for handover to GS

 ===========================================================================
*/
