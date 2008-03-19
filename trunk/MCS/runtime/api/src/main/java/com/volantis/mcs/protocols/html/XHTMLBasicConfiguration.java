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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicConfiguration.java,v 1.4 2003/02/18 14:00:39 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to encapsulate
 *                              XHTMLBasic configuration information.
 * 08-Nov-02    Byron           VBM:2002110516 - Created
 * 11-Dec-02    Adrian          VBM:2002121116 - Overload createRule to take 
 *                              either a ClassSelector or ElementSelector 
 *                              because previously it had been called with the 
 *                              UniversalSelector but added it as an optional 
 *                              selector instead of element selector. 
 * 13-Feb-03    Byron           VBM:2003021309 - Modified constructor by
 *                              populating assetURLLocations list.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.sgml.SGMLDTDBuilder;
import com.volantis.mcs.dom.sgml.ElementModel;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.themes.StylePropertyDetails;

import java.util.Iterator;

/**
 * This class extends the default dom outputter and does some WML specific
 * things. Actually they are probably more general than that but only wml
 * needs them now.
 */
public class XHTMLBasicConfiguration
  extends ProtocolConfigurationImpl {

    /**
     * Initialise.
     *
     * @param supportsDissection Specifies whether the protocol supports
     * dissection.
     */
    protected XHTMLBasicConfiguration(boolean supportsDissection) {
        super(supportsDissection);

        alwaysEmptyElements.add ("base");
        alwaysEmptyElements.add ("br");
        alwaysEmptyElements.add ("img");
        alwaysEmptyElements.add ("input");
        alwaysEmptyElements.add ("hr");
        alwaysEmptyElements.add ("link");
        alwaysEmptyElements.add ("meta");

        assetURLLocations.add("a", "href");
        assetURLLocations.add("input", "src");
        assetURLLocations.add("img", "src");
        assetURLLocations.add("object", "movie");
        assetURLLocations.add("object", "src");
        assetURLLocations.add("object", "href");
        assetURLLocations.add("object", "filename");
        assetURLLocations.add("link", "href");

        importantProperties.add(StylePropertyDetails.BACKGROUND_COLOR);
        importantProperties.add(StylePropertyDetails.BACKGROUND_IMAGE);
        importantProperties.add(StylePropertyDetails.BORDER_BOTTOM_WIDTH);
        importantProperties.add(StylePropertyDetails.BORDER_LEFT_WIDTH);
        importantProperties.add(StylePropertyDetails.BORDER_RIGHT_WIDTH);
        importantProperties.add(StylePropertyDetails.BORDER_TOP_WIDTH);
        importantProperties.add(StylePropertyDetails.COLOR);
        importantProperties.add(StylePropertyDetails.HEIGHT);
        importantProperties.add(StylePropertyDetails.MARGIN_BOTTOM);
        importantProperties.add(StylePropertyDetails.MARGIN_LEFT);
        importantProperties.add(StylePropertyDetails.MARGIN_RIGHT);
        importantProperties.add(StylePropertyDetails.MARGIN_TOP);
        importantProperties.add(StylePropertyDetails.PADDING_BOTTOM);
        importantProperties.add(StylePropertyDetails.PADDING_LEFT);
        importantProperties.add(StylePropertyDetails.PADDING_RIGHT);
        importantProperties.add(StylePropertyDetails.PADDING_TOP);
        importantProperties.add(StylePropertyDetails.TEXT_ALIGN);
        importantProperties.add(StylePropertyDetails.VERTICAL_ALIGN);
        importantProperties.add(StylePropertyDetails.WHITE_SPACE);
        importantProperties.add(StylePropertyDetails.WIDTH);

        hrefRuleSet = new com.volantis.mcs.protocols.href.HtmlRuleSet();
        highlightRuleSet = new com.volantis.mcs.protocols.highlight.HtmlRuleSet();        
        cornersRuleSet = new com.volantis.mcs.protocols.corners.CornersRuleSet();        

        populateBlockyElements();

        populateInvalidFormLinkParents();

        setCanSupportFileUpload(true);
    }

    /**
      * Elements defined as being blocky, per XHTML 1.0. Initially all grabbed
      * from here:
      *
      * http://www.w3.org/TR/xhtml1/dtds.html#a_dtd_XHTML-1.0-Strict
      *
      * and search for Block level elements
      */
    private void populateBlockyElements() {
        blockyElements.add("h1");
        blockyElements.add("h2");
        blockyElements.add("h3");
        blockyElements.add("h4");
        blockyElements.add("h5");
        blockyElements.add("h6");
        
        blockyElements.add("ul");
        blockyElements.add("ol");
        blockyElements.add("dl");
        
        blockyElements.add("pre");
        blockyElements.add("hr");
        blockyElements.add("blockquote");
        blockyElements.add("address");
        
        blockyElements.add("p");
        blockyElements.add("div");
        blockyElements.add("fieldset");
        blockyElements.add("table");
        
        blockyElements.add("form");
        
        // td and th aren't specified as part of the XHTML DTD, but intuitively
        // do seem to belong here.
        blockyElements.add("td");
        blockyElements.add("th");
        
        // Add these two due to malformed item in test case. Test case may be
        // wrong, or it may be representative of items seen in the wild; e.g. a
        // lot of pages that the transcoder hits written by Japanese authors do
        // not have a <body/> element.
        blockyElements.add("head");
        blockyElements.add("body");
    }

    private void populateInvalidFormLinkParents() {
        invalidFormLinkParents.put("table", "tr"); 
        invalidFormLinkParents.put("tr", "td");
    }

    /**
     * Initialise.
     */
    public XHTMLBasicConfiguration() {
        this(true);
    }

    // Javadoc inherited.
    public String[] getPermittedChildren() {
        String[] permittedChildren = new String[]{
            "a", "br", "cite", "code", "dfn", "em",
            "kbd", "samp", "strong", "var",
        };

        // Explain differences from html3_2 version...
        // the following element are defined in the text extension modile
        // are so are not included in this core list.
        //"b", "big", "i",  "small", "sub", "sup","tt",

        // "applet" is part of the deprecated applet module and so is not
        // required for xhtml basic

        // the forms module is not included in the core set of modules
        // "input", "select", "textarea",

        // the img module is not included in the core set of modules
        // "img",

        // the client side image map module is not included in the core set
        // of modules
        // "map"

        // the legacy module is deprecated and not included in the core set
        // of modules
        // "u", "strike", "font",  "script",
        return permittedChildren;
    }

    protected DTD createDTD(InternalDevice device) {

        SGMLDTDBuilder builder = new SGMLDTDBuilder();

        populateSGMLDTDBuilder(builder, device);

        return builder.buildDTD();
    }

    protected void populateSGMLDTDBuilder(
            SGMLDTDBuilder builder, InternalDevice device) {

        // we need to get the maximum line length in chars so we can give
        // it to the writer
        int maximumLineLength = getMaximumLineLength(device);
        builder.setMaximumLineLength(maximumLineLength);

        addCDATAElements(device, builder);

        for (Iterator i = alwaysEmptyElements.iterator(); i.hasNext();) {
            String element = (String) i.next();
            builder.setElementModel(element, ElementModel.EMPTY);
        }
        
        addContentModel(builder, device);
    }

    protected void addCDATAElements(
            InternalDevice device, SGMLDTDBuilder builder) {
        
        // we need to know how the device treats the content of the
        // STYLE element
        String styleContent = getXElementStyleContent(device);

        // we need to know how the device treats the content of the
        // SCRIPT element
        String scriptContent = getXElementScriptContent(device);

        // The default value (or null when the old device repository is used)
        // means that the code behaves exactly as it did before. On the device
        // which treat STYLE/SCRIPT element as CDATA this will cause it to stop
        // encoding the content and make it work.
        if (!DevicePolicyConstants.X_ELEMENT_STYLE_CONTENT_DEFAULT.equals(styleContent)
           || !DevicePolicyConstants.X_ELEMENT_SCRIPT_CONTENT_DEFAULT.equals(scriptContent)) {
            // todo this should probably check each element individually and
            // todo add them in separately rather than do it altogether.

            String[] elements = new String[] {"script","style"};
            builder.setElementModel(elements,  ElementModel.CDATA);
        }
    }

    /**
     * Get the content of the STYLE element for the requesting device.
     * @return the content of the STYLE element for the requesting
     *         device.
     * @param device
     */
    protected String getXElementStyleContent(InternalDevice device) {
        return getPolicyValue(device,
                DevicePolicyConstants.X_ELEMENT_STYLE_CONTENT,
                DevicePolicyConstants.X_ELEMENT_STYLE_CONTENT_DEFAULT);
    }

    private String getPolicyValue(
            InternalDevice device, String policy, String defaultValue) {
        String value = device.getPolicyValue(policy);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Get the content of the SCRIPT element for the requesting device.
     * @return the content of the SCRIPT element for the requesting
     *         device.
     * @param device
     */
    protected String getXElementScriptContent(InternalDevice device) {
        return getPolicyValue(device,
                DevicePolicyConstants.X_ELEMENT_SCRIPT_CONTENT,
                DevicePolicyConstants.X_ELEMENT_SCRIPT_CONTENT_DEFAULT);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 29-Jun-05	8552/6	pabbott	VBM:2005051902 JIBX Theme accessors

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 ===========================================================================
*/
