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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * Simple default implementation class of FragmentLinkWriter.
 */
public class DefaultFragmentLinkWriter implements FragmentLinkWriter {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
           LocalizationFactory.createLogger(DefaultFragmentLinkWriter.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        FragmentRenderer.class);


    /**
     * The page context to which this fragment writer applies.
     */
    private final MarinerPageContext pageContext;

    public DefaultFragmentLinkWriter(MarinerPageContext pageContext) {

        this.pageContext = pageContext;
    }

    // Javadoc inherited
    public void writeFragmentLink(
            FormatRendererContext formatRendererContext,
            Fragment source,
            Fragment destination,
            boolean isInList,
            boolean toEnclosing)
            throws IOException, RendererException {

        String fragmentName = destination.getName();

        // Create an anchor tag for this fragment
        FraglinkAttributes a = new FraglinkAttributes();

        MarinerURL rootPageURL = pageContext.getRootPageURL(true);

        String inclusionPath =
                pageContext.getDeviceLayoutContext().getInclusionPath();

        // Get the key to the current fragmentation state.
        int currentKey = pageContext.getFragmentationIndex();

        if (toEnclosing) {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating link to enclosing fragment");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating link to enclosed fragment");
            }
        }

        String requestValue;
        PageGenerationCache pageGenerationCache
                = pageContext.getPageGenerationCache();

        RuntimeDeviceLayout deviceLayout =
                pageContext.getDeviceLayout();
        String defaultFragmentName = deviceLayout.getDefaultFragmentName();
        boolean isDefault = true;
        if (defaultFragmentName == null
                || !fragmentName.equals(defaultFragmentName)) {
            isDefault = false;
        }

        // If we are generating a link to the enclosing fragment and this is
        // the top inclusion then we do not need to add a vfrag value to the
        // URL.
        if (toEnclosing && inclusionPath == null && isDefault) {
            if (logger.isDebugEnabled()) {
                logger.debug("Returning to default top level fragment");
            }
            requestValue = null;
        } else {

            // The change to apply to the fragmentation state.
            FragmentationState.Change change;

            // If the enclosing fragment is the default fragment for the layout
            // then discard the information about it, this reduces the number
            // of possible fragmentation states for a particular layout which
            // reduces the memory usage.

            change = new FragmentationState.FragmentChange(inclusionPath,
                                                           fragmentName,
                                                           toEnclosing,
                                                           isDefault);

            if (logger.isDebugEnabled()) {
                logger.debug("Change to be applied is " + change);
            }

            int changeIndex = pageGenerationCache.
                    getFragmentationStateChangeIndex(change);

            requestValue = PageGenerationCache.
                    makeFragmentChangeSpecifier(currentKey, changeIndex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("URL before is "
                         + rootPageURL.getExternalForm());
        }

        if (requestValue == null) {
            rootPageURL.removeParameter(URLConstants.FRAGMENTATION_PARAMETER);
        } else {
            rootPageURL.setParameterValue(URLConstants.FRAGMENTATION_PARAMETER,
                                          requestValue);
        }

        // Get the URLRewriter to use to encode session information in the
        // URL and use it.
        MarinerRequestContext requestContext = pageContext.getRequestContext();
        URLRewriter sessionURLRewriter = pageContext.getSessionURLRewriter();
        MarinerURL sessionURL
                = sessionURLRewriter.mapToExternalURL(requestContext,
                                                      rootPageURL);

        // perform any URL rewriting that may be required by an external plugin
        PageURLRewriter urlRewriter = pageContext.getVolantisBean().
                getLayoutURLRewriter();
        MarinerURL externalURL =
                urlRewriter.rewriteURL(pageContext.getRequestContext(),
                        sessionURL,
                        PageURLDetailsFactory.createPageURLDetails(
                                PageURLType.FRAGMENT));

        String absoluteLink = externalURL.getExternalForm();
        if (logger.isDebugEnabled()) {
            logger.debug("URL after Rewriting is " + absoluteLink);
        }

        a.setInList(isInList);
        a.setHref(absoluteLink);
        a.setTagName("a");

        // Calculate the style class for this link.
        // We can either use the new link class defined on the source fragment
        // or, if that is not present we should fall back to the old hardcoded
        // "fraglinks" class.
        // Note that we don't use the destination fragment link class, as this
        // would mean that each fragment link on a page would appear in a
        // different style - yuck!
        String styleClass = source.getLinkClass();
        if (styleClass == null) {
            styleClass = "fraglinks";
        }

        // Get the styles associated with the fragment.
        FormatStylingEngine formatStylingEngine =
                formatRendererContext.getFormatStylingEngine();
        Styles styles = formatStylingEngine.startStyleable(source, styleClass);
        a.setStyles(styles);

        if (pageContext.getPageTagId() != null) {
            a.setId(pageContext.getPageTagId());
        }
        a.setName(fragmentName);

        final OutputBuffer linkBuffer = getLinkBuffer(destination, toEnclosing);

        a.setLinkText(linkBuffer);

        VolantisProtocol protocol = pageContext.getProtocol();
        try {
            protocol.writeFragmentLink(a);
        } catch (ProtocolException pe) {
            throw new RendererException(
                    exceptionLocalizer.format("renderer-error", destination),
                    pe);
        }
    
        formatStylingEngine.endStyleable(source);
    }

    /**
     * Returns the to or from link for the fragment based on the direction
     * specified.
     *
     * Never returns null.
     *
     * Note: this method was left package private for easier testing only.
     *
     * @param fragment the fragment to be used
     * @param toEnclosing the direction
     * @return the output buffer containing the link
     */
    OutputBuffer getLinkBuffer(final Fragment fragment,
                               final boolean toEnclosing) {
        String linkText = null;
        OutputBuffer linkBuffer = null;
        FragmentInstance fragmentContext = (FragmentInstance) pageContext.
            getDeviceLayoutContext().getCurrentFormatInstance(fragment);

        // Check whether to use linkText or backLinkText.
        if (null != fragmentContext) {
            if (toEnclosing) {
                linkBuffer = fragmentContext.getLinkFromBuffer();
                if (linkBuffer == null) {
                    linkText = fragmentContext.getLinkFromText();
                }

                // To maintain backwards compabillity get linkToText
                // if not back link text is specified.
                if ((linkBuffer == null || linkBuffer.isEmpty()) &&
                     (linkText == null || linkText.length() == 0)) {
                    linkBuffer = fragmentContext.getLinkToBuffer();
                    if (linkBuffer == null) {
                        linkText = fragmentContext.getLinkToText();
                    }
                }
            } else {
                linkBuffer = fragmentContext.getLinkToBuffer();
                if (linkBuffer == null) {
                    linkText = fragmentContext.getLinkToText();
                }
            }
        }

        // Extract project to resolve policy expressions against.
        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        TextAssetReference textReference =
                resolver.resolveQuotedTextExpression(linkText);

        // Retrive the text from asset and set the
        // linkText if textReference is a text asset.
        if (textReference != null) {
            linkText = textReference.getText(TextEncoding.PLAIN);

            linkBuffer =
                pageContext.getDeviceLayoutContext().allocateOutputBuffer();
            linkBuffer.writeText(linkText);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("LinkText = " + linkText);
        }
        if (linkBuffer == null) {
            linkBuffer =
                pageContext.getDeviceLayoutContext().allocateOutputBuffer();
        }
        if (linkBuffer.isEmpty()) {
            linkBuffer.writeText(fragment.getName());
        }
        return linkBuffer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 04-Oct-05	9719/1	geoff	VBM:2005100402 Fix Fragmentation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 17-Feb-05	6957/3	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/3	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
