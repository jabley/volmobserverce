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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.renderers;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DeviceLayoutRegionContent;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentImageAssetReference;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.TabsContext;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TabsAttributes;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.themes.properties.VisibilityKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.localization.MessageLocalizer;

/**
 * Default renderer for tabs. Includes all rendering functions for tabs, tab
 * elements and tab::mcs-label pseudo-element.
 */
public class TabsDefaultRenderer extends WidgetDefaultRenderer implements
        TabsRenderer {

    /**
     * Used for message localization.
     */
    private static final MessageLocalizer exceptionLocalizer = LocalizationFactory
            .createMessageLocalizer(ValidateDefaultRenderer.class);

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Stack of currently processed Tabs widgets
     */
    private Stack contextsStack;

    /**
     * RegionInstnces for labels and tab's content renderer
     */

    // javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        // Create new context for tabs widget
        TabsContext context = pushContext();

        // Save page context necessary for current buffer changes
        // Label and tabs buffers are created
        context.initPageContext(((TabsAttributes) attributes).getPageContext());
    }

    // javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        // Ending this tabs widget - pop context
        popContext();
    }

    // javadoc inherited
    public void renderTabsOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

    	TabsContext context = getCurrentContext();
    	
        if (!isWidgetSupported(protocol)) {
            // pop labels deviceLayoutContext and tab's contents
            // deviceLayoutContext
            // fallback widget:tab renderer will save output to main buffer
            context.getPageContext().popDeviceLayoutContext();
            context.getPageContext().popDeviceLayoutContext();
            // Don't render anything specific for the tabs element
            return;
        }

        // Require all JavaScript libraries.
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-tabs.mscr", protocol);

        // Generate an ID, if it's not already there.
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        DOMOutputBuffer mainBuffer = getCurrentBuffer(protocol);

        // Create main table for the tabs
        Styles style = attributes.getStyles();

        style.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE);

        Element tabsTableElem = mainBuffer.openStyledElement("table", style);
        tabsTableElem.setAttribute("id", attributes.getId());
        tabsTableElem.setAttribute("border", "0");
        tabsTableElem.setAttribute("cellspacing", "0");
        setElementLocked(protocol, tabsTableElem);

        // set layout on stack for widget:tab's contents
        MarinerPageContext pageContext = context.getPageContext();
        pageContext.pushContainerInstance(getContentsRegionInstance());
    }

    // javadoc inherited
    public void renderTabsClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            // Don't render anything specific for the tabs element
            // Context is used in fallback mode as well, as extractAltText
            // uses it (i.e. the saved page context) in policy resolving
            return;
        }

        TabsContext context = getCurrentContext();

        // pop labels deviceLayoutContext
        DeviceLayoutContext labelsLayoutToPop = context.getPageContext()
                .getDeviceLayoutContext();
        RegionContent labelsRegionContent = new DeviceLayoutRegionContent(
                labelsLayoutToPop);
        context.getPageContext().popDeviceLayoutContext();

        // pop tab's content RegionInstance
        context.getPageContext().popContainerInstance(getContentsRegionInstance());

        DOMOutputBuffer mainBuffer = getCurrentBuffer(protocol);

        Styles contentsTrStyle = StylingFactory.getDefaultInstance()
        	.createInheritedStyles(
        		protocol.getMarinerPageContext().getStylingEngine()
        		.getStyles(), DisplayKeywords.TABLE_ROW);
                
        // Create tr element for labels in main buffer        
        Element labelsTrElem = mainBuffer.openStyledElement("tr", contentsTrStyle);
        
        labelsTrElem.setAttribute("id", protocol.getMarinerPageContext()
                .generateUniqueFCID());
        // set in order to label occupy only area for content
        labelsTrElem.setAttribute("style", "height: 1px");

        setElementLocked(protocol, labelsTrElem);

        // get main RegionInstance
        ContainerInstance containingInstance = context.getPageContext()
                .getCurrentContainerInstance();

        containingInstance.getCurrentBuffer().transferContentsFrom(
                getLabelsRegionInstance().getCurrentBuffer());
        
        renderTabStripFiller(mainBuffer);
        // Increase colspan for tab strip filler
        context.addColspan(1);

        // Close labels tr
        mainBuffer.closeElement("tr");

        // Create tr element for contents in main buffer                
        Element contentsTrElem = mainBuffer.openStyledElement("tr", contentsTrStyle);
        
        contentsTrElem.setAttribute("id", protocol.getMarinerPageContext()
                .generateUniqueFCID());
        setElementLocked(protocol, contentsTrElem);

        // Create td element with proper style
        Styles contentsTdStyle = StylingFactory.getDefaultInstance()
        	.createInheritedStyles(
                 protocol.getMarinerPageContext().getStylingEngine()        		
        		.getStyles(), DisplayKeywords.TABLE_CELL);
        
        Element contentsTdElem = mainBuffer.openStyledElement("td",
                contentsTdStyle);
        String contentId = protocol.getMarinerPageContext()
                .generateUniqueFCID();
        context.setContentId(contentId);
        contentsTdElem.setAttribute("id", contentId);
        contentsTdElem.setAttribute("colspan", "" + context.getColspan());
        setElementLocked(protocol, contentsTdElem);
        contentsTdElem.getStyles().getPropertyValues()
                .setComputedAndSpecifiedValue(
                        StylePropertyDetails.VERTICAL_ALIGN,
                        VerticalAlignKeywords.TOP);

        // Insert tab contents buffer to main buffer
        // mainBuffer.addOutputBuffer(tabsBuffer);

        // pop tab's contents deviceLayoutContext
        DeviceLayoutContext contentsLayoutToPop = context.getPageContext()
                .getDeviceLayoutContext();
        RegionContent contentsRegionContent = new DeviceLayoutRegionContent(
                contentsLayoutToPop);
        context.getPageContext().popDeviceLayoutContext();

        // add contentsRegionContent to main RegionInstance
        containingInstance.getCurrentBuffer().transferContentsFrom(
                getContentsRegionInstance().getCurrentBuffer());

        // Close contents tr element
        mainBuffer.closeElement("td");
        mainBuffer.closeElement("tr");

        // Close tabs table element in main buffer
        mainBuffer.closeElement("table");

        // Check if any tab was specified as active, if not set
        // the first one as active
        if (null == context.getActiveTabId()) {
            context.setActiveTabId(context.getFirstTabId());
        }

        // Write script
        String[] scriptArrays = generateScripts();
        StringBuffer textToScript = new StringBuffer("Widget.register("
                + createJavaScriptString(attributes.getId()) + ",new Widget.Tabs("
                + createJavaScriptString(attributes.getId()) + ",");
        for (int i = 0; i < scriptArrays.length; i++) {
            textToScript.append("[" + scriptArrays[i] + "],");
        }
        textToScript.append("{activeTabId:" 
                + createJavaScriptString(context.getActiveTabId()) + ","
                + "contentId:" + createJavaScriptString(context.getContentId()));

        Styles styleTable = attributes.getStyles();
        StyleValue heightValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.HEIGHT);
        StyleValue positionValue = styleTable.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.POSITION);
        if (heightValue != null && positionValue != null) {
            if (heightValue.getStandardCSS().equals("100%")
                    && positionValue.getStandardCSS().equals("absolute")) {
                textToScript.append(", fullScreen: true }));");
            } else {
                textToScript.append(", fullScreen: false }));");
            }
        } else {
            textToScript.append(", fullScreen: false }));");
        }

        try {
            writeStartupScriptElement(mainBuffer, textToScript.toString());
        } catch (IOException e) {
            throw new ProtocolException(exceptionLocalizer
                    .format("write-failed"), e);
        }
    }

    // javadoc inherited
    public void renderTabOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            renderFallbackTabOpen((DOMProtocol) protocol,
                    (TabAttributes) attributes);
            return;
        }

        // Output buffer will be tabs buffer, not main output buffer
        TabsContext context = getCurrentContext();
        DOMOutputBuffer tabsBuffer = 
            (DOMOutputBuffer) getContentsRegionInstance().getCurrentBuffer();

        // Generate an ID, if it's not already there.
        if (null == attributes.getId()) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        context.registerNewTab().tab = (TabAttributes) attributes;

        // copy border-top style from tab element to border-bottom in label
        // (:mcs-label pseudo class)
        Styles tabXdimeStyles = attributes.getStyles();

        StyleValue borderTopColor = tabXdimeStyles.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.BORDER_TOP_COLOR);
        StyleValue borderTopStyle = tabXdimeStyles.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.BORDER_TOP_STYLE);
        StyleValue borderTopWidth = tabXdimeStyles.getPropertyValues()
                .getSpecifiedValue(StylePropertyDetails.BORDER_TOP_WIDTH);

        if (borderTopWidth != null) {
            tabXdimeStyles.getNestedStyles(PseudoElements.MCS_LABEL)
                    .getPropertyValues().setComputedAndSpecifiedValue(
                            StylePropertyDetails.BORDER_BOTTOM_COLOR,
                            borderTopColor);
            tabXdimeStyles.getNestedStyles(PseudoElements.MCS_LABEL)
                    .getPropertyValues().setComputedAndSpecifiedValue(
                            StylePropertyDetails.BORDER_BOTTOM_STYLE,
                            borderTopStyle);
            tabXdimeStyles.getNestedStyles(PseudoElements.MCS_LABEL)
                    .getPropertyValues().setComputedAndSpecifiedValue(
                            StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                            borderTopWidth);
        }

        // Prepare styles for tab content; copy, as we remove :mcs-label
        // pseudo element, which is later used
        Styles tabStyles = attributes.getStyles().copy();
                
        if (null != tabStyles) {
            tabStyles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.BORDER_TOP_WIDTH,
                    StyleValueFactory.getDefaultInstance().getLength(null, 0,
                            LengthUnit.PX));
            
            tabStyles.removeNestedStyles(PseudoElements.MCS_LABEL);
        }

        // Create div element for tab contents
        Element tabElement = openDivElement(tabStyles, tabsBuffer);        
        tabElement.setAttribute("id", attributes.getId());
        tabElement.setAttribute("style", "display:block;visibility:hidden;position:absolute;z-index:-1;");
        setElementLocked(protocol, tabElement);

        // Check if first tab, if yes then register the id in the context
        if (null == context.getFirstTabId()) {
            context.setFirstTabId(attributes.getId());
        }

        // Check for active tab
        StyleValue initialState = attributes.getStyles().getPropertyValues()
                .getComputedValue(StylePropertyDetails.MCS_INITIAL_STATE);

        if (StyleKeywords.ACTIVE == initialState
                && null == context.getActiveTabId()) {
            context.setActiveTabId(attributes.getId());
        }

        // Process tab label
        int tdsRendered = renderTabLabel((DOMProtocol) protocol,
                (TabAttributes) attributes);
        context.addColspan(tdsRendered);

    }

    // javadoc inherited
    public void renderTabClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            renderFallbackTabClose((DOMProtocol) protocol,
                    (TabAttributes) attributes);
            return;
        }

        DOMOutputBuffer tabsBuffer = 
        	(DOMOutputBuffer) getContentsRegionInstance().getCurrentBuffer();

        // Close tab's div element inside tabs buffer
        closeDivElement(tabsBuffer);
    }

    /**
     * Renders tab element in fallback mode. Called on tag open.
     * 
     * @param protocol
     * @param attributes
     *            are tab attributes
     * @throws ProtocolException
     */
    private void renderFallbackTabOpen(DOMProtocol protocol,
            TabAttributes tabAttributes) throws ProtocolException {

        TabsContext context = getCurrentContext();
        // for fallback tabs buffer is a main buffer
        DOMOutputBuffer tabsBuffer = (DOMOutputBuffer) context.getPageContext()
                .getCurrentOutputBuffer();

        // Get content value specified for ::mcs-label
        Styles tabStyles = tabAttributes.getStyles().copy();
        Styles labelStyles = (null == tabStyles ? null : tabStyles
                .getNestedStyles(PseudoElements.MCS_LABEL));
        StyleValue labelContent = (null == labelStyles ? null : labelStyles
                .getPropertyValues().getSpecifiedValue(
                        StylePropertyDetails.CONTENT));

        // Open label p
        Element labelElement = tabsBuffer.openStyledElement("p", labelStyles);

        // Render label content
        if (null != labelContent) {
            StyleValue contentToRender = getSingleStyleValue(labelContent);
            // If content is an image, we have to extract the alternative text
            // and render it instead
            if (contentToRender instanceof StyleComponentURI) {
                String altText = extractAltText(protocol,
                        (StyleComponentURI) contentToRender);
                contentToRender = STYLE_VALUE_FACTORY.getString(null, altText);
            }

            // Render label contents here
            renderLabelContents(protocol, contentToRender, true);
        }

        // Close label p
        tabsBuffer.closeElement(labelElement);

        // Open a div element with styles as specified to tab element
        tabsBuffer.openStyledElement("div", tabAttributes.getStyles());

        // Remove styles specific to mcs-label pseudoelement
        labelStyles.removeNestedStyles(StatefulPseudoClasses.ACTIVE);
        labelStyles.getPropertyValues().clearPropertyValue(
                StylePropertyDetails.CONTENT);

        // Remove styles specific to tab element
        tabAttributes.getStyles().removeNestedStyles(PseudoElements.MCS_LABEL);
        tabAttributes.getStyles().getPropertyValues().clearPropertyValue(
                StylePropertyDetails.MCS_INITIAL_STATE);

        return;
    }

    /**
     * Renders tab element in fallback mode. Called on tag close.
     * 
     * @param protocol
     * @param attributes
     *            are tab attributes
     * @throws ProtocolException
     */
    private void renderFallbackTabClose(DOMProtocol protocol,
            TabAttributes tabAttributes) {

        TabsContext context = getCurrentContext();
        // for fallback tabs buffer is a main buffer
        DOMOutputBuffer tabsBuffer = (DOMOutputBuffer) context.getPageContext()
                .getCurrentOutputBuffer();

        // Close div element opened for tab contents
        tabsBuffer.closeElement("div");

    }

    /**
     * Renders tab label according to attributes of tab tag
     * 
     * @param protocol
     * @param attributes
     *            are attributes of the tab tag
     * @return number of TD tags rendered (between 1 and 3)
     */
    private int renderTabLabel(DOMProtocol protocol, TabAttributes attributes)
            throws ProtocolException {

        // We will always render the TD with tab label, and sometimes
        // two extra margins
        int tdsRendered = 1;

        TabsContext context = getCurrentContext();

        context.getPageContext().pushContainerInstance(getLabelsRegionInstance());
        DOMOutputBuffer labelsBuffer = 
            (DOMOutputBuffer) getLabelsRegionInstance().getCurrentBuffer();

        Styles tabStyles = attributes.getStyles();
        // Get content value specified for ::mcs-label
                
        Styles inactiveLabelStyles = (null == tabStyles ? null : tabStyles
        	.getNestedStyles(PseudoElements.MCS_LABEL));
        
        StyleValue inactiveLabelContent = (null == inactiveLabelStyles ? null
                : inactiveLabelStyles.getPropertyValues().getSpecifiedValue(
                        StylePropertyDetails.CONTENT));

        // Get content value specified for ::mcs-label:active
        Styles activeLabelStyles = (null == inactiveLabelStyles ? null
                : inactiveLabelStyles
                        .getNestedStyles(StatefulPseudoClasses.ACTIVE));
        StyleValue activeLabelContent = (null == activeLabelStyles ? null
                : activeLabelStyles.getPropertyValues().getSpecifiedValue(
                        StylePropertyDetails.CONTENT));

        // If no content was specified for active label,
        // use content from inactive label
        activeLabelContent = (null == activeLabelContent ? inactiveLabelContent
                : activeLabelContent);

        // If no content was specified for ::mcs-label, use default empty string
        if (null == inactiveLabelContent) {
            inactiveLabelContent = STYLE_VALUE_FACTORY.getString(null, "");
            activeLabelContent = inactiveLabelContent;
        } else {
            // Normalize content values - they are provided as a list
            inactiveLabelContent = getSingleStyleValue(inactiveLabelContent);
            activeLabelContent = getSingleStyleValue(activeLabelContent);
        }
        // Check if image labels are used
        context.getCurrentTabAndLabel().usesImageLabels = 
        	(inactiveLabelContent instanceof StyleComponentURI);

        // Prepare styles for labels
        inactiveLabelStyles.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.WHITE_SPACE, WhiteSpaceKeywords.NOWRAP);
        inactiveLabelStyles.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE_CELL);
        // Remove copied styles specified on tab::mcs-label
        inactiveLabelStyles.getPropertyValues().clearPropertyValue(
                StylePropertyDetails.CONTENT);
        inactiveLabelStyles.removeNestedStyles(PseudoElements.MCS_LABEL);
        inactiveLabelStyles.removeNestedStyles(StatefulPseudoClasses.ACTIVE);

        if (null != activeLabelStyles) {
            activeLabelStyles.getPropertyValues()
                    .setComputedAndSpecifiedValue(
                            StylePropertyDetails.WHITE_SPACE,
                            WhiteSpaceKeywords.NOWRAP);
            activeLabelStyles.getPropertyValues().clearPropertyValue(
                    StylePropertyDetails.CONTENT);
            activeLabelStyles.removeNestedStyles(PseudoElements.MCS_LABEL);
            context.getCurrentTabAndLabel().activeLabelStyles = activeLabelStyles;
        }

        // Rendering starts here
        Element tdLeftMargin = null;
        Element tdRightMargin = null;

        // Generate left margin td (if necessary)
        if (!context.getCurrentTabAndLabel().usesImageLabels) {
            tdLeftMargin = renderLabelMarginTds(labelsBuffer,
                    inactiveLabelStyles, StylePropertyDetails.MARGIN_LEFT);
        }
        tdsRendered += (null == tdLeftMargin ? 0 : 1);

        // Create a td with styles specified for tab label
        String labelId = protocol.getMarinerPageContext().generateUniqueFCID();
        Element labelTdElement = labelsBuffer.openStyledElement("td",
                inactiveLabelStyles);
        setElementLocked(protocol, labelTdElement);
        labelTdElement.setAttribute("id", labelId);
        context.getCurrentTabAndLabel().labelTdId = labelId;

        // Open div inside td with no styles
        Styles labelDivStyles = StylingFactory.getDefaultInstance()
                .createStyles(null);
        Element labelDivElement = labelsBuffer.openStyledElement("div",
                labelDivStyles);
        setElementLocked(protocol, labelDivElement);

        // Set div width according to label width
        applyWidthToChild(inactiveLabelStyles.getPropertyValues()
                .getComputedValue(StylePropertyDetails.WIDTH), labelDivElement
                .getStyles());

        // Create styles for anchor
        Styles anchorStyles = StylingFactory.getDefaultInstance().createStyles(
                null);
        anchorStyles.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
                MCSTextUnderlineStyleKeywords.NONE);
        anchorStyles.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.COLOR,
                StyleValueFactory.getDefaultInstance().getInherit());
        anchorStyles.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.BOTTOM);

        // Open anchor element with created styles
        Element anchorElement = labelsBuffer.openStyledElement("a",
                anchorStyles);
        anchorElement.setAttribute("href", "javascript:void(null)");
        setElementLocked(protocol, anchorElement);

        String inactiveId = renderLabelContents(protocol, inactiveLabelContent,
                true);

        // Only write second content if it's different than the first one
        String activeId = inactiveId;
        if (inactiveLabelContent != activeLabelContent) {
            activeId = renderLabelContents(protocol, activeLabelContent, false);
        }
        context.getCurrentTabAndLabel().inactiveContentId = inactiveId;
        context.getCurrentTabAndLabel().activeContentId = activeId;

        labelsBuffer.closeElement(anchorElement);
        labelsBuffer.closeElement(labelDivElement);
        labelsBuffer.closeElement(labelTdElement);

        // Generate right margin td (if image labels not used)
        if (!context.getCurrentTabAndLabel().usesImageLabels) {
            tdRightMargin = renderLabelMarginTds(labelsBuffer,
                    inactiveLabelStyles, StylePropertyDetails.MARGIN_RIGHT);
        }
        tdsRendered += (null == tdRightMargin ? 0 : 1);

        // In the end, we remove margin specified on tab label, as it has
        // already been rendered in margin TDs and we don't want the margins
        // rendered in CSS
        if (!context.getCurrentTabAndLabel().usesImageLabels) {
            inactiveLabelStyles.getPropertyValues().clearPropertyValue(
                    StylePropertyDetails.MARGIN_LEFT);
            inactiveLabelStyles.getPropertyValues().clearPropertyValue(
                    StylePropertyDetails.MARGIN_RIGHT);
            if (null != activeLabelStyles) {
                activeLabelStyles.getPropertyValues().clearPropertyValue(
                        StylePropertyDetails.MARGIN_LEFT);
                activeLabelStyles.getPropertyValues().clearPropertyValue(
                        StylePropertyDetails.MARGIN_RIGHT);
            }
        }

        // Also, if image labels are used, set vertical-align:bottom for td
        if (context.getCurrentTabAndLabel().usesImageLabels) {
            labelTdElement.getStyles().getPropertyValues()
                    .setComputedAndSpecifiedValue(
                            StylePropertyDetails.VERTICAL_ALIGN,
                            VerticalAlignKeywords.BOTTOM);
        }

        context.getPageContext().popContainerInstance(getLabelsRegionInstance());

        return tdsRendered;
    }

    /**
     * Renders margin table cell for tab label
     * 
     * @param buffer
     * @param styles
     *            are styles of tab label, to get the margin value and copy
     *            bottom border style from
     * @param property
     *            is the property from which width of margin cell is taken
     *            (should be StylePropertyDetails.MARGIN_LEFT or
     *            StylePropertyDetails.MARGIN_RIGHT)
     * @return rendered table cell element, or null
     */
    private Element renderLabelMarginTds(DOMOutputBuffer buffer, Styles styles,
            StyleProperty property) {

        Element marginTd = null;
        if (null == styles) {
            return null;
        }
        StyleValue marginValue = styles.getPropertyValues().getComputedValue(
                property);

        // Only render the cell if width is percentage or absolute
        if (null != marginValue
                && (StyleValueType.PERCENTAGE == marginValue
                        .getStyleValueType() || StyleValueType.LENGTH == marginValue
                        .getStyleValueType())) {

            // Open margin cell with no styles
            Styles nullStyles = StylingFactory.getDefaultInstance()
                    .createStyles(null);
            marginTd = buffer.openStyledElement("td", nullStyles);
            marginTd.getStyles().getPropertyValues()
                    .setComputedAndSpecifiedValue(StylePropertyDetails.WIDTH,
                            marginValue);
            // Copy label's bottom border to the margin TD
            copyBorderBottomStyle(styles, marginTd.getStyles());

            // Open div inside cell
            Styles nullDivStyles = StylingFactory.getDefaultInstance()
                    .createStyles(null);
            Element marginDiv = openDivElement(nullDivStyles, buffer);
            marginDiv.getStyles().getPropertyValues()
                    .setComputedAndSpecifiedValue(
                            StylePropertyDetails.VISIBILITY,
                            VisibilityKeywords.HIDDEN);
            marginDiv.getStyles().getPropertyValues()
                    .setComputedAndSpecifiedValue(
                            StylePropertyDetails.FONT_SIZE,
                            StyleValueFactory.getDefaultInstance().getLength(
                                    null, 1.0, LengthUnit.PX));

            // If margin is specified in percents, the div inside td must take
            // 100%
            // Otherwise, set div to absolutely specified width
            applyWidthToChild(marginValue, marginDiv.getStyles());

            // Write fake text and close tags
            buffer.writeText(".");
            buffer.closeElement(marginDiv);
            buffer.closeElement(marginTd);
        }
        return marginTd;
    }

    /**
     * Writes label content to an element using current protocol buffer
     * 
     * @param protocol
     *            is the current protocol
     * @param content
     *            is value of content style property
     * @param visible
     *            specified if the rendered contents should be initially visible
     * @return id of element in which contents are written
     * @throws ProtocolException
     */
    private String renderLabelContents(DOMProtocol protocol,
            StyleValue content, boolean visible) throws ProtocolException {

        DOMOutputBuffer buffer = (DOMOutputBuffer) protocol
                .getMarinerPageContext().getCurrentOutputBuffer();

        if (content instanceof StyleString) {
            // Writing text label inside a span, in case we support switching
            // text labels in active/inactive states
            Element spanElem = buffer.openElement("span");

            protocol.getInserter().insertPreservingExistingContent(spanElem,
                    content);

            // Set id
            String spanId = protocol.getMarinerPageContext()
                    .generateUniqueFCID();
            spanElem.setAttribute("id", spanId);
            buffer.closeElement(spanElem);

            if (!visible) {
                spanElem.setAttribute("style", "display:none");
            }

            return spanId;

        } else if (content instanceof StyleComponentURI) {
            // Write image component label
            StyleComponentURI component = (StyleComponentURI) content;

            // Insert the image using protocol inserter
            protocol.getInserter().insertPreservingExistingContent(
                    buffer.getCurrentElement(), content);
            // Set id
            Element insertedImage = (Element) buffer.getCurrentElement()
                    .getTail();

            if (null == insertedImage) {
                throw new ProtocolException(exceptionLocalizer.format(
                        "missing-policy-failure", content.toString()));
            }

            String imgId = protocol.getMarinerPageContext()
                    .generateUniqueFCID();
            insertedImage.setAttribute("id", imgId);

            // Set required styles
            Styles styles = insertedImage.getStyles();
            if (null == styles) {
                styles = StylingFactory.getDefaultInstance().createStyles(null);
                insertedImage.setStyles(styles);
            }
            styles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.DISPLAY, DisplayKeywords.INLINE);
            styles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.VERTICAL_ALIGN,
                    VerticalAlignKeywords.BOTTOM);

            if (!visible) {
                insertedImage.setAttribute("style", "display:none");
            }

            // Extract from policy and set alt text for image
            String altText = extractAltText(protocol, component);
            insertedImage.setAttribute("alt", altText);

            return imgId;

        } else {
            throw new ProtocolException(exceptionLocalizer
                    .format("widget-tab-label-incorrect-content-type"));
        }
    }

    /**
     * Render tab strip filler - a table cell with width:10000px
     * 
     * @param buffer
     * @param styles
     *            are styles of the last label in the tab strip, used to copy
     *            bottom border to the filler
     */
    private void renderTabStripFiller(DOMOutputBuffer buffer) {
        // As usual, new td with empty styles
        Styles fillerStyle = StylingFactory.getDefaultInstance().createStyles(
                null);
        Element filler = buffer.openStyledElement("td", fillerStyle);
        // Set width to 10000px
        StyleValue bigWidth = StyleValueFactory.getDefaultInstance().getLength(
                null, 10000.0, LengthUnit.PX);
        filler.getStyles().getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.WIDTH, bigWidth);
        
        // set default border for TD filler element (last TD in Tabs-Strip)
        filler.getStyles().getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_COLOR, StyleColorNames.BLACK);
        filler.getStyles().getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_WIDTH, 
                StyleValueFactory.getDefaultInstance().getLength(null, 1.0, LengthUnit.PX));
        filler.getStyles().getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_STYLE, BorderStyleKeywords.SOLID);

        // Create inner div
        Styles divStyle = StylingFactory.getDefaultInstance()
                .createStyles(null);
        Element divInFiller = buffer.openStyledElement("div", divStyle);
        divInFiller.getStyles().getPropertyValues()
                .setComputedAndSpecifiedValue(StylePropertyDetails.VISIBILITY,
                        VisibilityKeywords.HIDDEN);
        divInFiller.getStyles().getPropertyValues()
                .setComputedAndSpecifiedValue(
                        StylePropertyDetails.FONT_SIZE,
                        StyleValueFactory.getDefaultInstance().getLength(null,
                                1.0, LengthUnit.PX));
        divInFiller.getStyles().getPropertyValues()
                .setComputedAndSpecifiedValue(
                        StylePropertyDetails.WIDTH,
                        StyleValueFactory.getDefaultInstance().getPercentage(
                                null, 100.0));
        buffer.writeText(".");
        buffer.closeElement(divInFiller);
        buffer.closeElement(filler);
    }

    /**
     * Extracts alternative text from a policy. The text should be specified as
     * an alternate policy of type text.
     * 
     * @param component
     *            is the image component with text alternative specified
     * @return contents of alternative text in plain encoding, or empty string
     *         if no alt text is provided
     */
    private String extractAltText(DOMProtocol protocol,
            StyleComponentURI component) throws ProtocolException {

        // Get asset and policy resolvers, resolve component policy
        TabsContext context = getCurrentContext();
        PolicyReferenceResolver referenceResolver = context.getPageContext()
                .getPolicyReferenceResolver();
        AssetResolver assetResolver = context.getPageContext()
                .getAssetResolver();
        RuntimePolicyReference policyReference = referenceResolver
                .resolvePolicyExpression(component.getExpression());

        // Create image asset from the policy and get fallback
        DefaultComponentImageAssetReference imageAsset = new DefaultComponentImageAssetReference(
                policyReference, assetResolver);
        TextAssetReference textAsset = imageAsset.getTextFallback();

        // If e.g policy file is missing and as consequence textAsset == null,
        // throw suitable exception
        if (null == textAsset) {
            throw new ProtocolException(exceptionLocalizer.format(
                    "missing-policy-failure", policyReference.getName()));
        }

        // Return text in plain encoding, or empty string
        String altText = textAsset.getText(TextEncoding.PLAIN);
        if (null == altText) {
            altText = "";
        }

        return altText;
    }

    /**
     * Sets width style in child styles according to style value provided If the
     * value is absolute, then it copies it. If it is a percentage - it sets
     * width to 100%.
     * 
     * @param parent
     *            is the style value to apply to child
     * @param child
     *            are child styles, to be modified
     */
    private void applyWidthToChild(StyleValue parent, Styles child) {
        if (null != parent) {
            if (StyleValueType.PERCENTAGE == parent.getStyleValueType()) {
                parent = StyleValueFactory.getDefaultInstance().getPercentage(
                        null, 100.0);
            }
            child.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.WIDTH, parent);
        }
    }

    /**
     * Copies bottom border styles from one styles collection to another
     * 
     * @param from
     * @param to
     */
    private void copyBorderBottomStyle(Styles from, Styles to) {
        to.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                from.getPropertyValues().getComputedValue(
                        StylePropertyDetails.BORDER_BOTTOM_COLOR));
        to.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_STYLE,
                from.getPropertyValues().getComputedValue(
                        StylePropertyDetails.BORDER_BOTTOM_STYLE));
        to.getPropertyValues().setComputedAndSpecifiedValue(
                StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                from.getPropertyValues().getComputedValue(
                        StylePropertyDetails.BORDER_BOTTOM_WIDTH));
    }

    /**
     * Retrieves the first StyleValue from a list of style values. <i>content</i>
     * attribute is specified as a list.
     * 
     * @param content
     *            is the style value of type StyleList
     * @return first style value in the list
     * @throws ProtocolException
     *             if content is not of type StyleList
     */
    private StyleValue getSingleStyleValue(StyleValue content)
            throws ProtocolException {

        if (content instanceof StyleList) {
            StyleList contentList = (StyleList) content;
            if (contentList.getList().isEmpty()) {
                throw new ProtocolException(exceptionLocalizer
                        .format("widget-tab-label-empty-content"));
            }
            return (StyleValue) contentList.getList().get(0);
        } else {
            return content;
        }
    }

    /**
     * Returns an instance of tabs context from the top of the stack.
     * 
     * @throws EmptyStackException
     *             if stack is empty.
     */
    public TabsContext getCurrentContext() {
        return (TabsContext) getContextsStack().peek();
    }

    /**
     * Creates and returns new instance of TabsContext.
     * 
     * @return The TabsContext.
     */
    private TabsContext createContext() {
        return new TabsContext();
    }

    /**
     * Returns stack of TabsContext contexts.
     * 
     * @return The stack of TabsContext contexts.
     */
    private Stack getContextsStack() {
        if (contextsStack == null) {
            contextsStack = new Stack();
        }
        return contextsStack;
    }

    /**
     * Pushes and returns new TabsContext context, making it current.
     * 
     * @returns The new current context.
     */
    private TabsContext pushContext() {
        return (TabsContext) getContextsStack().push(createContext());
    }

    /**
     * Pops an instance of TabsContext from the stack.
     * 
     * @throws EmptyStackException
     *             if stack is empty.
     */
    private void popContext() {
        getContextsStack().pop();
    }

    /**
     * Generates JavaScript strings to be written to the page. Uses current
     * context to get the needed information.
     */
    private String[] generateScripts() {
        // Prepare buffers
        final int SCRIPT_ARRAYS_COUNT = 6;
        StringBuffer[] buffers = new StringBuffer[SCRIPT_ARRAYS_COUNT];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new StringBuffer(30);
        }

        TabsContext context = getCurrentContext();
        List tabAndLabelAttrs = context.getTabAndLabelAttributesList();
        Iterator it = tabAndLabelAttrs.listIterator();

        while (it.hasNext()) {
            TabsContext.TabWithLabelPair entry = (TabsContext.TabWithLabelPair) it
                    .next();

            // Label and tab ids
            buffers[0].append("," + createJavaScriptString(entry.labelTdId));
            buffers[1].append("," + createJavaScriptString(entry.tab.getId()));

            // AJAX attributes
            if (null == entry.tab.getLoadAttributes()) {
                buffers[2].append("," + createJavaScriptString(""));
            } else {
                buffers[2].append("," + createJavaScriptString(entry.tab.getLoadAttributes().getSrc()));
            }

            // Extract styles for label in active state
            StylesExtractor extractor = new StylesExtractor(
                    entry.activeLabelStyles);
            buffers[3].append("," + extractor.getJavaScriptStyles());

            buffers[4].append(entry.usesImageLabels ? ", true" : ", false");

            buffers[5].append(",[" + createJavaScriptString(entry.inactiveContentId) + ","
                    + createJavaScriptString(entry.activeContentId) + "]");

        }

        String[] scriptArrays = new String[6];
        for (int i = 0; i < buffers.length; i++) {
            scriptArrays[i] = buffers[i].toString().substring(1);
        }
        return scriptArrays;
    }

    /**
     * @return Returns the labelsRegionInstance.
     */
    public RegionInstance getLabelsRegionInstance() {
        return getCurrentContext().getLabelsRegionInstance();
    }

    /**
     * @param labelsRegionInstance
     *            The labelsRegionInstance to set.
     */
    public void setLabelsRegionInstance(RegionInstance labelsRegionInstance) {
    	getCurrentContext().setLabelsRegionInstance(labelsRegionInstance);
    }

    /**
     * @return Returns the contentsRegionInstance.
     */
    public RegionInstance getContentsRegionInstance() {
        return getCurrentContext().getContentsRegionInstance();
    }

    /**
     * @param contentsRegionInstance
     *            The contentsRegionInstance to set.
     */
    public void setContentsRegionInstance(RegionInstance contentsRegionInstance) {
    	getCurrentContext().setContentsRegionInstance(contentsRegionInstance);
    }
}
