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
package com.volantis.mcs.protocols;


import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRenderer;
import com.volantis.mcs.dissection.Dissector;
import com.volantis.mcs.protocols.trans.StyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashMap;

/**
 * This class encapsulates information about the protocol.
 * <p>
 * At the moment this class is hard coded for each different protocol but
 * in future when the device contains a lot more protocol specific policy
 * values this class should be used to contain information about the
 * protocol configuration for each device.
 * </p>
 * <p>
 * There will have to be specialisations of this for each family of protocols
 * as not all of the device values will apply to all of the protocol families
 * and those that don't should be ignored.
 * </p>
 * <p>
 * Although there will be one instance of this for each device references to
 * this cannot be stored in the InternalDevice object itself as that belongs in a
 * different package. There are a couple of ways of handling this, one is to
 * have a RuntimeDevice which extends (or wraps InternalDevice) and which can contain
 * a reference to an instance of this, another is to add a mechanism
 * to store arbitrary information in a InternalDevice object and yet another is to
 * maintain a separate table which maps from device name to instances of this
 * class.
 * </p>
 * <p>
 * Creating an instance of this should be done by ProtocolConfigurator
 * classes. There should be one instance per protocol family which knows
 * about the information which applies just to that family and a general
 * instance which knows about common information.
 * </p>
 * <p>
 * When a ProtocolConfiguration class is needed for a particular device and
 * does not yet exist then the right instance of the ProtocolConfigurator
 * class would be selected. It would then be passed the InternalDevice and would
 * return an instance of the specialisation of the ProtocolConfiguration
 * class for the particular family.
 * </p>
 * <p>
 * Selecting the correct instance of the ProtocolConfigurator could simply
 * be done by asking the protocol as a single instance of every protocol
 * has already been created.
 * </p>
 * <p>
 * The retrieval of the ProtocolConfiguration class should be done by the
 * protocol as part of its initialisation. It should not be done in the
 * constructor as that is only ever called once as all the other instances
 * are cloned.
 * </p>
 * <p>
 * Over time those fields in the protocols which control the protocol
 * behaviour should be moved out of the protocol and into the correct
 * specialisation of this class. The reason for this are two fold. Firstly,
 * most of those fields are fixed for every instance of the protocol and so
 * having it in the protocol wastes a little space and time (for cloning).
 * Secondly, as we do more and more testing we may find that those fields
 * are device dependent and having them here makes it so much easier to add
 * them to the device and will not impact the protocols at all.
 * </p>
 * <p>
 * This class should not contain any properties which are specific to a
 * particular protocol family, it should only contain general properties which
 * apply across all families.
 * </p>
 *
 * @mock.generate
 */
public interface ProtocolConfiguration extends TransformationConfiguration {

    /**
     * Get the value of the emptyElementRequiresSpace property.
     *
     * @return The value of the emptyElementRequiresSpace property.
     */
    public boolean getEmptyElementRequiresSpace();

    /**
     * This method is called with the name of an element and returns true if
     * it is always empty and false otherwise.
     *
     * @param name The name of an element.
     * @return True if the element is empty and false otherwise.
     */
    public boolean isElementAlwaysEmpty(String name);

    /**
     * This method is called with the name of an element and returns true if
     * the element is atomic and should not be split and false if it can be.
     *
     * @param name The name of an element.
     * @return True if the element is atomic and false if it can be split.
     */
    public boolean isElementAtomic(String name);

    /**
     * Finds candidate element attribute values which could be an asset URL
     * and submits them to the given PackageResources object.
     *
     * @param element          the DOM element to be checked for candidate attributes
     * @param packageResources the PackageResources instance to which candidate
     *                         asset URLs should be submitted
     */
    public void addCandidateElementAssetURLs(Element element,
                                             PackageResources packageResources);


    public ValidationHelper getValidationHelper();

    public Dissector getDissector();

    /**
     * Get the default style value associated with a given style property
     * on a given element
     *
     * @param elementType
     * @param property
     * @return String - default style value
     *         todo refactor to use data structure supporting all elements
     */
    public boolean isElementAttributeDefaultStyle(String elementType,
                                                  StyleProperty property,
                                                  StyleValue styleValue);

    /**
     * Return true if the element is blocky, otherwise false. By 'blocky', we
     * mean that it has a visual effect when rendered; e.g. a &lt;div/&gt;
     * element will have a line-break rendered before and after the content.
     *
     * See <a href="http://www.w3.org/TR/xhtml1/dtds.html#a_dtd_XHTML-1.0-Strict">the XHTML 1.0 DTD</a>
     * and search for Block level elements
     *
     * @param name the element name - not null.
     * @return true if the element is 'blocky', otherwise false.
     */
    public boolean isElementBlocky(String name);

    /**
     * Does the protocol support events
     *
     * @return boolean
     */
    public boolean supportsEvents();

    public int getSupportedGeneralEvents();

    /**
     * Describes if a protocol supports the use of the SubScript element
     *
     * @return true iff the device does support the SubScript element
     */
    public boolean getCanSupportSubScriptElement();

    /**
     * Describes if a protocol supports the use of the SuperScript element
     *
     * @return true iff the device does support the SuperScript element
     */
    public boolean getCanSupportSuperScriptElement();

    /**
     * determines if the protocol supports javascript
     *
     * @return
     */
    public boolean supportsJavaScript();

    /**
     * Return the ruleset used by the Href transformer to handle href
     * attributes on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHrefTransformationRules();

    /**
     * Return the ruleset used by the Highlight transformer to handle mcs-effect:highlight
     * style properties on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHighlightTransformationRules();

    /**
     * Return the ruleset used by the Corners transformer to handle mcs-border-radius
     * style properties on XHTML2 elements.
     *
     * @return
     */
    public HashMap getCornersTransformationRules();

    /**
     * Return the object which describes the CSS version that the protocol
     * supports.
     *
     * @return the CSS version for the protocol.
     */
    public CSSVersion getCssVersion();

    /**
     * Get the style element configuration object.
     *
     * @return the style elemenet configuration object.
     */
    StyleEmulationElementConfiguration
            getStyleEmulationElementConfiguration();

    /**
     * Get the DeviceCapabilityManager
     *
     * @return
     */
    public DeviceCapabilityManager getDeviceCapabilityManager();

    ExtractorConfiguration getExtractorConfiguration();


    /**
     * returns true if FrameworkClient is supported by given protocol in current
     * MCS version and false otherwise. 
     */
    public boolean isFrameworkClientSupported();

    /**
     * returns true if FrameworkClient is supported by given protocol
     * and false otherwise.
     */        
    public boolean getFrameworkClientSupported();
    
    /**
     * Set version of CFW since from this protocol supports client framework.
     */
    public void setFrameworkClientSupportedSince(String frameworkClientSupportedSince);
    
    /**
     * Returns version of CFW since from this protocol supports client framework.
     */
    public String getFrameworkClientSupportedSince();
    
    /**
     * Get device pixelsx flag  for given internal device
     *
     * @return
     */
    public int getDevicePixelsX();

    /**
     * returns true if we support file uploads for this protocol.
     *
     * @return true if uploads are supported.
     */
    public boolean isFileUploadSupported();

    /**
     * Get the {@link StyleEmulationPropertiesRenderer} which is used to
     * emulate any styles that are not supported by the device.
     *
     * @return StyleEmulationPropertiesRenderer
     */
    StyleEmulationPropertiesRenderer
            getStyleEmulationPropertyRendererSelector();

    /**
     * Create a new {@link StyleEmulationElementConfiguration}, configure it,
     * and assign
     */
    void createStyleEmulationElements();

    /**
     * Return the array of element names that are valid children of stylistic
     * elements (i.e. b, i) for this protocol. Used when configuring this
     * protocol's {@link StyleEmulationElementConfiguration}.
     *
     * @return String[] array of element names that are valid children of
     *         stylistic elements
     */
    String[] getPermittedChildren();

    /**
     * Returns target media types for generated CSS.
     * Value null means that no target media types are specified.
     * Returned value is a comma separated list of media types,
     * as documented in HTML specification, ie "handheld,print".
     *
     * @return The target media types for generated CSS.
     */
    String getCSSMedia();

    /**
     * Returns maximum frame rate available on client device.
     * Value Double.POSITIVE_INFINITY means no frame rate limit.
     *
     * @return The maximum frame rate.
     */
    double getMaxClientFrameRate();

    /**
     * Return true if the given element is not a valid form link parent, false
     * otherwise.
     *
     * @param parentName    name of the effective parent element for a form link
     * @return true if the given element is not a valid form link parent, false
     * otherwise
     */
    boolean isInvalidFormLinkParent(String parentName);

    /**
     * Not every element can contain a form link (i.e. a link between form
     * fragments. In which case a child element which can contain a form link
     * (or which provides a path to an element that can) should be inserted.
     * <p/>
     * NB: This may not be a one step process. For example, in XHTMLBasic,
     * table cannot contain a form link so a child tr should be inserted.
     * However a tr also cannot contain a form link, so a child td should be
     * inserted. The td can contain a form link, so our work is done.
     *
     * @param elementName   for which to retrieve the child element which can
     *                      contain a form link
     * @return String name of an element which either can contain a form link,
     * or which provides a path to one that can.
     */
    String getChildForInvalidFormLinkElement(String elementName);

    /**
     * Get the DTD for the markup supported by this protocol.
     *
     * @return The DTD, may be null.
     */
    DTD getDTD();

    /**
     * Get x-browser viewport virtual support for given device
     *
     * @return
     */
    public String getViewportVirtualSupport();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/12	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/7	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/4	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9540/5	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 21-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 15-Sep-05	9472/8	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/6	ibush	VBM:2005090808 Add default styling for sub/sup elements

 ===========================================================================
*/
