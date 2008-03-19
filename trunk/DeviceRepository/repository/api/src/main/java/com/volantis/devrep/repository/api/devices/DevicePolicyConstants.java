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
 * $Header: /src/voyager/com/volantis/mcs/devices/DevicePolicyConstants.java,v 1.13 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Sep-01    Paul            VBM:2001083114 - Created.
 * 29-Oct-01    Paul            VBM:2001102901 - DevicePolicyConstants has
 *                              moved from utilities package to devices
 *                              package.
 * 20-Nov-01    Pether          VBM:2001102303 - Added the constant
 *                              EMULATE_WML_HORIZONTAL_TAG.
 * 19-Dec-01    Paul            VBM:2001120506 - Added SUPPORTS_JAVASCRIPT.
 * 29-Jan-02    Allan           VBM:2001121703 - Added FALLBACK_POLICY_NAME
 *                              constant.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-May-02    Paul            VBM:2002042203 - Added constants for maximum
 *                              wml deck and html page sizes.
 * 07-May-02    Adrian          VBM:2002042302 - Added CACHE_FILE_EXT
 * 07-May-02    Allan           VBM:2002040804 - Added style sheet version
 *                              constants.
 * 06-Jun-02    Byron           VBM:2002051303 - Added CSS_INLINE_IMPORT item
 *                              which is used to determine whether a device
 *                              permits the use of the @import statement within
 *                              an inline style.
 * 18-Jun-02    Steve           VBM:2002040807 - Added CSS_MULTICLASS_SUPPORT
 *                              which determines the amount of support that
 *                              a device has for multiple attribute and
 *                              selector classes.
 * 28-Jun-02    Paul            VBM:2002051302 - Added constants for the
 *                              allowable values of the CSS_MULTICLASS_SUPPORT
 *                              policy.
 * 21-Mar-03    Phil W-S        VBM:2003031910 - Added the
 *                              STYLESHEET_LOCATION_* members to provide policy
 *                              value names and added STYLESHEET_RENDER_* to
 *                              provide the enumeration values associated with
 *                              the new policy values.
 * 29-Mar-03    Phil W-S        VBM:2002111502 - Add SUPPORTS_LINK_DIALLING and
 *                              the associated LINK_DIALLING_INFO* definitions.
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.api.devices;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;

/**
 * This interface contains constants which should be used to access device
 * policy values.
 */
public interface DevicePolicyConstants {

    /**
     * Device policy value indicating full support for the specified policy
     */
    public static final String FULL_SUPPORT_POLICY_VALUE = "full";

    /**
     * Device policy value indicating no support for the specified policy
     */
    public static final String NO_SUPPORT_POLICY_VALUE = "none";

    /**
     * The policy key for the fallback device.
     */
    public static final String FALLBACK_POLICY_NAME = "fallback";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml big tag.
     */
    public static final String EMULATE_WML_BIG_TAG
            = "protocol.wml.emulate.bigTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml b(old) tag.
     */
    public static final String EMULATE_WML_BOLD_TAG
            = "protocol.wml.emulate.boldTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml card title.
     */
    public static final String EMULATE_WML_CARD_TITLE
            = "protocol.wml.emulate.cardTitle";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml em(phasize) tag.
     */
    public static final String EMULATE_WML_EMPHASIZE_TAG
            = "protocol.wml.emulate.emphasizeTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml i(talic) tag.
     */
    public static final String EMULATE_WML_ITALIC_TAG
            = "protocol.wml.emulate.italicTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml link highlighting.
     */
    public static final String EMULATE_WML_LINK_HIGHLIGHTING
            = "protocol.wml.emulate.linkHighlighting";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml small tag.
     */
    public static final String EMULATE_WML_SMALL_TAG
            = "protocol.wml.emulate.smallTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml strong tag.
     */
    public static final String EMULATE_WML_STRONG_TAG
            = "protocol.wml.emulate.strongTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml u(nderline) tag.
     */
    public static final String EMULATE_WML_UNDERLINE_TAG
            = "protocol.wml.emulate.underlineTag";

    /**
     * This is a prefix which is used to access all values relating to the
     * emulation of the wml horizontal tag.
     */
    public static final String EMULATE_WML_HORIZONTAL_TAG
            = "protocol.wml.emulate.horizontal";

    /**
     * This constant can be used to check if the device honours the
     * align attribute on paragraph tags when mode="nowrap"
     */
    public static final String DEVICE_HONOURS_ALIGN_WHEN_MODE_NOWRAP
            = "x-element.p.honours.attribute.align.when.modenowrap";

    /**
     * This constant can be used to check whether the device supports JavaScript.
     */
    public static final String SUPPORTS_JAVASCRIPT = "javascript";
    
    
    /**
     * This constant can be used to check whether the device supports Client Framework.
     */
    public static final String SUPPORTS_VFC = "supports.client.framework";
        
    /**
     * The non-boolean value that the {@link #SUPPORTS_VFC} policy
     * can take. The boolean values are handled via standard string to boolean
     * processing and are therefore not listed here.
     */
    public static final String SUPPORTS_VFC_DEFAULT = "default";

    /**
     * This constant can be used to check since which version 
     * of Client Framework the device is supported
     */
    public static final String SUPPORTS_VFC_SINCE = "supports.client.framework.since";

    /**
     * Default value for {@link #SUPPORTS_VFC_SINCE} policy
     */
    public static final String SUPPORTS_VFC_SINCE_DEFAULT = "none";

    /**
     * This constant can be used to check whether the device 
     * supports Framework Client installer.
     */
    public static final String SUPPORTS_VFC_INSTALLER = "supports.client.framework.installer";
        
    /**
     * The non-boolean value that the {@link #SUPPORTS_VFC_INSTALLER} policy
     * can take. The boolean values are handled via standard string to boolean
     * processing and are therefore not listed here.
     */
    public static final String SUPPORTS_VFC_INSTALLER_DEFAULT = "default";
    
    /**
     * The maximum size of a WML deck.
     */
    public static final String MAX_WML_DECK_SIZE = "maxwmldeck";

    /**
     * Whether or not device supports WMLC
     */
    public static final String WMLC_SUPPORTED = "wmlcsupport";

    /**
     * The maximum size of a HTML page.
     */
    public static final String MAX_HTML_PAGE_SIZE = "maxhtmlpage";

    /**
     * Whether the device supports link dialling.
     */
    public static final String SUPPORTS_LINK_DIALLING =
            "dial.link.supported";

    /**
     * The link dialling information associated with the device.
     */
    public static final String LINK_DIALLING_INFO =
            "dial.link.info";

    /**
     * The type of link dialling information that is associated with the
     * device.
     */
    public static final String LINK_DIALLING_INFO_TYPE =
            "dial.link.info.type";

    /**
     * Indicates, if found as the LINK_DIALLING_INFO_TYPE, that no link
     * dialling information is associated with the device.
     */
    public static final String LINK_DIALLING_INFO_TYPE_NONE =
            "none";

    /**
     * Indicates, if found as the LINK_DIALLING_INFO_TYPE, that the link
     * dialling information associated with the device is to be used as a
     * prefix for the full telephone number.
     */
    public static final String LINK_DIALLING_INFO_TYPE_PREFIX =
            "prefix";

    /**
     * The StyleSheetVersion policy and its possible values.
     */
    public static final String STYLE_SHEET_VERSION = "ssversion";
    public static final String CSS1 = "CSS1";
    public static final String CSS2 = "CSS2";
    public static final String CSS_MOBILE_PROFILE1 = "CSSMobileProfile1";
    public static final String CSS_WAP = "WCSS";

    /**
     * Collection of the supported style sheet versions.
     */
    public static final Collection SUPPORTED_STYLE_SHEET_VERSIONS =
            Collections.unmodifiableCollection(Arrays.asList(new String [] {
                CSS1, CSS2, CSS_MOBILE_PROFILE1, CSS_WAP
            }));

    /**
     * The file extension for the current device for page caching.
     */
    public static final String CACHE_FILE_EXT = "cachefileext";


    /**
     * Used to extract a boolean device policy value, if it is true then the
     * device supports @import inside an inline style, otherwise it does not.
     */
    public static final String CSS_INLINE_IMPORT = "css.inline.import";

    /**
     * The level of multi class support for a device
     */
    public static final String CSS_MULTICLASS_SUPPORT = "css.multiclass.support";

    public static final String CSS_MULTICLASS_SUPPORT_NONE = "none";
    public static final String CSS_MULTICLASS_SUPPORT_ATTRIBUTE = "attribute";
    public static final String CSS_MULTICLASS_SUPPORT_SELECTOR = "selector";

    /**
     * The policy value name used to check to see if the device supports css.
     */
    public static final String SUPPORTS_CSS = "stylesheets";

    /**
     * The policy value name used to access the location of device stylesheets.
     */
    public static final String STYLESHEET_LOCATION_DEVICE =
            "protocol.stylesheet.location.device";

    /**
     * The policy value name used to access the location of layout stylesheets.
     */
    public static final String STYLESHEET_LOCATION_LAYOUT =
            "protocol.stylesheet.location.layout";

    /**
     * The policy value name used to access the location of theme stylesheets.
     */
    public static final String STYLESHEET_LOCATION_THEME =
            "protocol.stylesheet.location.theme";

    /**
     * An enumeration value option for one of
     * {@link #STYLESHEET_LOCATION_DEVICE}, {@link #STYLESHEET_LOCATION_THEME}
     * or {@link #STYLESHEET_LOCATION_LAYOUT}.
     */
    public static final String STYLESHEET_RENDER_DEFAULT = "default";

    /**
     * An enumeration value option for one of
     * {@link #STYLESHEET_LOCATION_DEVICE}, {@link #STYLESHEET_LOCATION_THEME}
     * or {@link #STYLESHEET_LOCATION_LAYOUT}.
     */
    public static final String STYLESHEET_RENDER_INTERNAL = "internal";

    /**
     * An enumeration value option for one of
     * {@link #STYLESHEET_LOCATION_DEVICE}, {@link #STYLESHEET_LOCATION_THEME}
     * or {@link #STYLESHEET_LOCATION_LAYOUT}.
     */
    public static final String STYLESHEET_RENDER_EXTERNAL = "external";

    /**
     * The policy value name used to access the default output charset for a
     * device.
     */
    public static final String DEFAULT_OUTPUT_CHARSET =
            "output.charset.default";

    /**
     * The policy value name used to access the forced output charset for a
     * device.
     */
    public static final String FORCED_OUTPUT_CHARSET =
            "output.charset.forced";

    /**
     * The policy value name used to access the WCSS validation style support.
     */
    public static final String SUPPORTS_WCSS_VALIDATION_STYLES =
            "protocol.wcss.validation.styles";

    /**
     * Name of a boolean device policy which indicates if the device supports
     * the display of accesskey numbers automagically.
     */
    public static final String
            SUPPORTS_WML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY =
            "protocol.wml.accesskey.numdisplay";

    /**
     * Name of a boolean device policy which indicates if the (X)HTML device
     * supports the display of accesskey numbers automagically.
     */
    public static final String
            SUPPORTS_XHTML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY =
            "protocol.xhtml.accesskey.numdisplay";


    /**
     * The tri-state device policy that indicates whether or not the device
     * supports the accesskey (or shortcut) attribute.
     */
    public static final String ACCESSKEY_SUPPORTED =
            "protocol.supports.accesskey";

    /**
     * The non-boolean value that the {@link #ACCESSKEY_SUPPORTED} policy can
     * take. The boolean values are handled via standard string to boolean
     * processing and are therefore not listed here.
     */
    public static final String ACCESSKEY_SUPPORT_DEFAULT =
            "default";

    /**
     * UAProf Tables Capable policy value.
     */
    public static final String UAPROF_TABLES_CAPABLE = "UAProf.TablesCapable";

    /**
     * Name of the string device policy which provides a device-specific mime
     * type for use in response content type specifications.
     */
    public static final String PROTOCOL_MIME_TYPE =
            "protocol.content.type";

    /**
     * Name of the select device policy which indicates how a protocol should
     * behave, in terms of MIME packaging of responses.
     */
    public static final String MIME_PACKAGING_MODE =
            "protocol.use.MIME.packaging";

    /**
     * The tri-state device policy that indicates whether or not the device
     * supports nested tables.
     */
    public static final String NESTED_TABLE_SUPPORT = "nested.tables.support";

    /**
     * Constant for the name of the policy used to define
     * device specific META name-value pair attributes.
     */
    public static final String FIXED_META_VALUES =
            "protocol.html.fixedmetavalues";

    /**
     * The non-boolean value that the {@link #NESTED_TABLE_SUPPORT} policy
     * can take. The boolean values are handled via standard string to boolean
     * processing and are therefore not listed here.
     */
    public static final String NESTED_TABLE_SUPPORT_DEFAULT = "default";

    /**
     * The prefix for experimental policies.
     *
     * <p>These are not available at runtime to customer content.</p>
     */
    public static final String EXPERIMENTAL_POLICY_PREFIX = "x-";

    /**
     * The actual height of the device display in pixels.
     */
    final String ACTUAL_HEIGHT_IN_PIXELS = "fullpixelsy";

    /**
     * The actual width of the device display in pixels.
     */
    final String ACTUAL_WIDTH_IN_PIXELS = "fullpixelsx";

    /**
     * The actual height of the device display in millimetres.
     */
    final String ACTUAL_HEIGHT_IN_MM = "height";

    /**
     * The actual width of the device display in millimetres.
     */
    final String ACTUAL_WIDTH_IN_MM = "width";

    /**
     * The usable height of the device display in pixels.
     */
    final String USABLE_HEIGHT_IN_PIXELS = "pixelsy";

    /**
     * The usable width of the device display in pixels.
     */
    final String USABLE_WIDTH_IN_PIXELS = "pixelsx";

    /**
     * The aspect ratio of pixel width to pixel height.
     */
    final String PIXEL_ASPECT_RATIO = "UAProf.PixelAspectRatio";

    /**
     * The number of bits per pixel. A value of -1 means that the value is
     * unknown or that no specific value can be assumed.
     */
    final String PIXEL_DEPTH = "pixeldepth";

    /**
     * The number of entries in the display colour palette.
     */
    final String PALETTE = "numpalette";

    /**
     * The display color rendering mode. The valid values are:
     * <ul>
     * <li>palette: display uses a palette of colours</li>
     * <li>rgb: display is colour and uses direct rgb representation</li>
     * <li>grayscale: display shows gray shades rather than colour</li>
     * </ul>
     */
    final String RENDER_MODE = "rendermode";

    /**
     * The value of thie policy determines the maximum number of characters
     * permitted in a line before a line break will be introduced when the
     * MCS DOM is output.
     */
    public static final String MAXIMUM_LINE_CHARS = "maxlinechars";

    /**
     * Device policy to control how css widths are emulated for tables.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_TABLE =
            "protocol.css.emulate.width.table";

    /**
     * Value of "none" for above device policy.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_TABLE__NONE = "none";

    /**
     * Value of "as a style attribute" for above device policy.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_TABLE__STYLE_ATTRIBUTE
            = "inline CSS";

    /**
     * Value of "as a width attribute" for above device policy.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_TABLE__WIDTH_ATTRIBUTE
            = "HTML attribute";

    /**
     * Device policy to control how css widths precentage values are emulated
     * for tables.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE =
            "protocol.css.emulate.width.percentage.table";

    /**
     * Value of "none" for above device policy.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE__NONE =
            "none";

    /**
     * Value of "using device width" for above device policy.
     */
    public static final String PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE__DEVICE_WIDTH =
            "pixels";

    final String WML_IMAGE_NOSAVE = "x-protocols.wml.img.nosave";

    final String WML_IMAGE_NOSAVE__ALT_NO_SAVE = "alt-no-save";

    /**
     * Constant for the policy that determines if whitespace and/or
     * non blocking space should be added to inline styling open elements.
     */ 
    public static final String FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS =
            "protocol.whitespace.inlinestyling.open";

    /**
     * Constant for the policy that determines if whitespace and/or
     * non blocking space should be added to inline styling closing elements.
     */
    public static final String FIX_FOR_CLOSING_INLINE_STYLING_ELEMENTS =
            "protocol.whitespace.inlinestyling.close";

    /**
     * Constant for the policy that determines if whitespace and/or
     * non blocking space should be added to the open anchor element.
     */
    public static final String FIX_FOR_OPEN_ANCHOR_ELEMENT =
            "protocol.whitespace.anchor.open";

    /**
     * Constant for the policy that determines if whitespace and/or
     * non blocking space should be added to the closing anchor element.
     */
    public static final String FIX_FOR_CLOSING_ANCHOR_ELEMENT =
            "protocol.whitespace.anchor.close";

    /**
     * Constant indicating that no white space fix strategy is required, ie,
     * the browser in use honours spacing on styling elements.
     */
    public static final String NO_WHITESPACE_FIXING = "none";

    /**
     * Constant to specify that a whitespace fixing strategy will add
     * whitespace to both the inside and outside of an element.
     */
    public static final String WHITESPACE_INSIDE_AND_OUTSIDE =
            "SpaceInsideAndOutside";

    /**
     * Constant to specify that a whitespace fixing strategy will add
     * whitespace immediatley after the open element and before the closing
     * element.
     */
    public static final String WHITESPACE_INSIDE = "WHITESPACE_INSIDE";

    /**
     * Constant to specify that a whitespace fixing strategy will add
     * whitespace outside the start and end element.
     */
    public static final String NON_BREAKING_SPACE_OUTSIDE =
            "NON_BREAKING_SPACE_OUTSIDE";

    /**
     * Constant to specify that a whitespace fixing strategy will add
     * whitespace outside the start and end element.
     */
    public static final String SPACE_AND_NON_BREAKING_SPACES_OUTSIDE =
            "SpaceAndNonBreakingSpacesOutside";

    /**
     * Indicates whether or not device supports align attribute on td element
     */
    public static final String X_ELEMENT_TD_SUPPORTS_ALIGN_ATTRIBUTE =
            "x-element.td.supports.attribute.align";

    /**
     * Default value for support of table-cell alignment
     */
    public static final String X_ELEMENT_TD_SUPPORTS_ALIGN_ATTRIBUTE_DEFAULT =
            "default";

    /**
     * Describes how the device treats the content of the STYLE element
     */
    public static final String X_ELEMENT_STYLE_CONTENT =
            "x-element.style.content";
    
    /**
     * Default value for the content of the STYLE element
     */
    public static final String X_ELEMENT_STYLE_CONTENT_DEFAULT =
            "default";
    
    /**
     * Describes how the device treats the content of the SCRIPT element
     */
    public static final String X_ELEMENT_SCRIPT_CONTENT =
            "x-element.script.content";

    /**
     * Default value for the content of the SCRIPT element
     */
    public static final String X_ELEMENT_SCRIPT_CONTENT_DEFAULT =
            "default";
    
    /**
     * Indicates whether or not device supports center element
     */
    public static final String X_ELEMENT_CENTER_SUPPORTED =
            "x-element.center.supported";

    /**
     * Default value for support of center element
     */
    public static final String X_ELEMENT_CENTER_SUPPORTED_DEFAULT =
            "default";

    /**
     * Indicates whether or not the device supports the wap css extension
     * property: -wap-marquee-direction
     */
    public static final String WAP_MARQUEE_DIRECTION_SUPPORTED =
            "x-css.properties.-wap-marquee-direction.support";

    /**
     * Indicates whether or not the device supports the wap css extension
     * property: -wap-marquee-repetition
     */
    public static final String WAP_MARQUEE_REPETITION_SUPPORTED =
            "x-css.properties.-wap-marquee-direction.support";

    /**
     * Indicates whether or not the device supports the wap css extension
     * property: -wap-marquee-style
     */
    public static final String WAP_MARQUEE_STYLE_SUPPORTED =
            "x-css.properties.-wap-marquee-direction.support";

    /**
     * Indicates whether or not the device supports the wap css extension
     * shorthand: -wap-marquee
     */
    public static final String WAP_MARQUEE_SHORTHAND_SUPPORTED =
            "x-css.shorthands.-wap-marquee.support";

    /**
     * Indicates whether or not the device supports the wap css extension to
     * the display keywords: -wap-marquee
     */
    public static final String WAP_MARQUEE_DISPLAY_SUPPORTED =
            "x-css.properties.display.keyword.-wap-marquee.support";

    /**
     * Indicates media supported by the device.
     */
    public static final String CSS_MEDIA_SUPPORTED =
            "x-css.media.supported";

    /**
     * Indicates maximum client frame rate.
     */
    public static final String CLIENT_FRAME_RATE_MAX =
            "x-client-frame.rate.max";

    /**
     * Indicates that the device supports the use of meta elements with
     * the "refresh" property.
     */
    public static final String X_ELEMENT_SUPPORTS_META_REFRESH =
            "x-element.meta.property.refresh.support";

    /**
     * Indicates that the device supports the wap "timer" element for
     * handling timed refreshes
     */
    public static final String X_ELEMENT_SUPPORTS_WAP_TIMER =
            "x-element.timer.support";
    
    /**
     * Indicates whether or not the device adds a line break near an a element.
     * The policy can have values of none, default (which mean no line breaks
     * are added), before or after (the line break is inserted before or after
     * the a element).
     */
    public static final String X_ELEMENT_A_BREAKS_LINE =
            "x-element.a.breaks.line";
 
    /**
     * Indicates whether or not the device can support links with mixed content
     * (i.e. text and nested elements) in the body. This policy uses the
     * standard convention of none|full|default values.
     */
    public static final String X_ELEMENT_A_SUPPORTS_MIXED_CONTENT =
            "x-element.a.supports.mixed.content";        
            
    /**
     * The default CSS rules that the device applies. 
     */
    String DEFAULT_CSS = "x-css.default.rules";
    String DEFAULT_DISPLAY_CSS = "address,blockquote,body,dd,div,dl,dt,fieldset,form," +
                    "frame,frameset,h1,h2,h3,h4,h5,h6,noframes,ol,p,ul,center," +
                    "dir,hr,menu,pre {display:block}\n" +
                    "li {display: list-item}\n" +
                    "head {display: none}\n" +
                    "table {display: table}\n" +
                    "tr {display: table-row}\n" +
                    "thead {display: table-header-group}\n" +
                    "tbody {display: table-row-group}\n" +
                    "tfoot {display: table-footer-group}\n" +
                    "col {display: table-column}\n" +
                    "colgroup {display: table-column-group}\n" +
                    "td,th {display: table-cell}\n" +
                    "caption {display: table-caption}\n";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10803/5	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10799/1	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 12-Dec-05	10791/1	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/3	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/1	ianw	VBM:2005071309 Ported forward meta changes

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/1	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 25-Nov-05	9708/4	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 17-Nov-05	10330/1	pabbott	VBM:2005110907 Honour align with mode=nospace

 24-Oct-05	9565/9	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/4	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 26-Sep-05	9593/2	adrianj	VBM:2005092209 Hide experimental device policies from customer code

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 12-Jul-05	8990/1	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 16-Feb-05	6129/9	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/6	matthew	VBM:2004102019 yet another supermerge

 27-Jan-05	6129/4	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/2	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic - rework

 13-Sep-04	5511/1	philws	VBM:2004091003 Port MIME packaging control from 3.2.2

 13-Sep-04	5495/1	philws	VBM:2004091003 Policy-based MIME packaging mode

 27-Apr-04	4063/1	steve	VBM:2004040103 Allow WML/WMLC at a device level

 27-Apr-04	3712/1	steve	VBM:2004040103 Allow WML/WMLC at a device level

 10-Mar-04	3374/1	byron	VBM:2004030807 Specify which Character Set to use if multiple available

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 13-Oct-03	1542/1	allan	VBM:2003101101 HTML_iMode table handling patched from Proteus2

 12-Oct-03	1540/1	allan	VBM:2003101101 Fixes for implementation review

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2
protocols

 05-Sep-03	1321/1	adrian	VBM:2003082111 added wcss input validation for xhtmlmobile

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
