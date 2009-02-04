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
package com.volantis.mcs.xdime;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.ListenerEvent;
import com.volantis.mcs.context.ListenerEventRegistry;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.PageURIRewriter;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.xdime.events.CommonEvents;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.styling.Styles;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.ElementOutputState;


/**
 * Generic super class holding functionality common to all XDIME elements that
 * could be (but are not necessarily) styled.
 * <p>
 * NOTE: this class needs refactoring and in particular has a very misleading
 * name. We need to avoid misusing inheritance for element classes the way that
 * was done for PAPI. In this case I think we should do something like factor
 * all the stying code in this class into StylingStrategy, and then merge this
 * this class into its parent.
 *
 * @todo refactor this class to make sense as above
 * see http://mantis:8080/mantis/Mantis_View.jsp?mantisid=2006062313
 */
public abstract class StylableXDIMEElement extends XDIMEElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
            StylableXDIMEElement.class);
    /**
     * The common events which apply for all "visible" XDIME 2 elements.
     */
    private static final CommonEvents COMMON_EVENTS = new CommonEvents();

    /**
     * The attributes to pass to the protocol methods.
     */
    protected MCSAttributes protocolAttributes;

    /**
     * The styling strategy determines if and how this element should be
     * styled.
     */
    private final StylingStrategy stylingStrategy;

    /**
     * The data handling strategy determines how any data (character data or
     * markup) encountered while processing this element should be handled.
     */
    private DataHandlingStrategy dataHandlingStrategy;

    /**
     * Indicates whether the element was suppressed or skipped.
     */
    private boolean suppressedOrSkippedElement;

    private Styles styles;

    /**
     * Indicates thiis element force protocol processing for a non display
     * element.
     */
    private boolean forced;

    /**
     * Maps XDIME 2 events into protocol events.
     */
    protected final EventMapper eventMapper;

    /**
     * Flag to prevent attributes being initialised twice.
     * <p>
     * This is a bit of a hack and is presumably only needed because of the
     * nasty inheritance structure of XDIME elements. Once we refactor to
     * delegation this kind of thing should disappear.
     */
    private boolean attributesInitialised;

    /**
     * Determines whether this element is currently suppressing output, and if
     * not, calculates where the output should appear. Calling apply and revert
     * will ensure that the current container in the page context is correct.
     * This should always be used via the accessor method; the accessor ensures
     * that the state has been created.
     */
    private ElementOutputState state;

    public StylableXDIMEElement(
            ElementType type, StylingStrategy stylingStrategy,
            XDIMEContextInternal context) {
        this(type, stylingStrategy,
                IgnoreDataStrategy.getDefaultInstance(), context);
    }

    public StylableXDIMEElement(
            ElementType type, StylingStrategy stylingStrategy,
            DataHandlingStrategy dataHandlingStrategy,
            XDIMEContextInternal context) {
        super(type, context, ValidationStrategy.VALIDATE);

        this.stylingStrategy = stylingStrategy;
        this.dataHandlingStrategy = dataHandlingStrategy;

        // Create a new event mapper to translate the listener events into
        // protocol events.
        eventMapper = new EventMapper(type);

        // Register the common events on all styled elements.
        // We do this because the common events are all mouse related and this
        // is a quick and easy way to determine which elements are visible and
        // therefore accessible via a mouse.
        // If this turns out to not be a good choice in future then we could
        // add the common events on an element by element or some other basis.
        if (stylingStrategy == StyledStrategy.STRATEGY) {
            COMMON_EVENTS.registerEvents(eventMapper);
        }
    }

    protected StylingEngine getStylingEngine(XDIMEContextInternal context) {
        MarinerPageContext pageContext = getPageContext(context);
        return pageContext.getStylingEngine();
    }

    protected boolean forceProcessing(final XDIMEContextInternal context,
                                      final XDIMEAttributes attributes)
            throws XDIMEException {

        return false;
    }

    // Javadoc inherited
    public XDIMEResult doElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // Initialise the protocol attributes from the xdime attributes,
        // if they have not already been initialised (sigh).
        initialiseAttributes(context, attributes);

        styleElementStart(context, attributes);

        XDIMEResult result;

        // Configure the ElementOutputStateBuilder with the styles which are
        // used to calculate whether this element should generate or suppress
        // output markup (and if generating, where the markup should appear).
        outputStateBuilder = createElementOutputStateBuilder(
                context, attributes);

        // Apply this output state - ensures that the page context is
        // configured to output markup to the correct container.
        FormattingResult formattingResult = getOutputState().apply();

        dataHandlingStrategy.handleData(context);

        forced = false;

        if (formattingResult == FormattingResult.PROCESS ||
            forceProcessing(context, attributes)) {

            result = callOpenOnProtocol(context, attributes);
            if (result == XDIMEResult.SKIP_ELEMENT_BODY) {
                suppressedOrSkippedElement = true;
            }
            forced = true;
        } else if (formattingResult == FormattingResult.SKIP) {
            result = XDIMEResult.SKIP_ELEMENT_BODY;
            suppressedOrSkippedElement = true;
        } else if (formattingResult == FormattingResult.SUPPRESS) {
            result = XDIMEResult.PROCESS_ELEMENT_BODY;
            suppressedOrSkippedElement = true;
        } else {
            throw new IllegalStateException(
                    "Unknown formatting result type: " + formattingResult);
        }

        return result;
    }

    protected ElementOutputStateBuilder createElementOutputStateBuilder(
            XDIMEContextInternal context, XDIMEAttributes attributes) {

        ElementOutputState parentState;
        if (parent == null) {
            parentState = null;
        } else {
            parentState = parent.getOutputState();
        }

        return new ElementOutputStateBuilderImpl(context,
                parentState, attributes.getValue("",
                XDIMEAttribute.ID.toString()), styles,
                suppressSkipForDisplayNoneStyle(),
                isElementAtomic());
    }

    protected void styleElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) {

        if (styles == null) {
            StylingEngine stylingEngine = getStylingEngine(context);
            stylingStrategy.startElement(stylingEngine, getNamespace(),
                    getTagName(), (Attributes) attributes);
            styles = stylingStrategy.getStyles(stylingEngine);
            MCSAttributes protocolAttributes = getProtocolAttributes();

            // the protocol attributes should only be null for elements that do not
            // cause markup to be generated e.g. XForms model elements
            if (protocolAttributes != null) {
                protocolAttributes.setStyles(styles);
            }
        }
    }

    // Javadoc inherited
    public XDIMEResult doElementEnd(XDIMEContextInternal context)
            throws XDIMEException {

        // Call the protocol if the element has not been suppressed.
        ElementOutputState state = getOutputState();
        if ((!suppressedOrSkippedElement && !state.isSuppressing()) ||
                (!suppressedOrSkippedElement && forced)){
            callCloseOnProtocol(context);
        }
        dataHandlingStrategy.stopHandlingData(context);

        stylingStrategy.endElement(
                getStylingEngine(context), getNamespace(), getTagName());

        state.revert();

        return XDIMEResult.CONTINUE_PROCESSING;
    }

    /**
     * Accessor method used to get the Page Context.
     *
     * @param context       XDIMEContext from which to retrieve the
     *                      {@link MarinerPageContext}
     * @return MarinerPageContext associated with this request
     */
    protected static MarinerPageContext getPageContext(XDIMEContextInternal context) {

        return ContextInternals.getMarinerPageContext(
                context.getInitialRequestContext());
    }
    
    /**
     * Rewrites specified URL of specified type with page URL rewriter.
     * 
     * @param context The XDIMEContext from which to retrieve the rewriter.
     * @param url The URL to rewrite.
     * @param urlType The type of the URL to rewrite.
     * @return rewritten URL.
     */
    protected static String rewriteURLWithPageURLRewriter(XDIMEContextInternal context, String url, PageURLType urlType) {
        MarinerRequestContext requestContext = context.getInitialRequestContext();
        
        ApplicationContext applicationContext = ContextInternals.getApplicationContext(requestContext);
        
        PageURLRewriter rewriter = applicationContext.getPageURLRewriter();
        
        if (rewriter != null) {
            // Convert URL string to MarinerURL instance.
            MarinerURL marinerURL = new MarinerURL(url);
            
            // Rewrite the MarinerURL instance.
            marinerURL = rewriter.rewriteURL(requestContext, marinerURL, 
                    PageURLDetailsFactory.createPageURLDetails(urlType));
            
            // Convert back to URL string.
            url = marinerURL.getExternalForm();
        }

        return url;
    }

    /**
     * Accessor method used to get the Protocol.
     *
     * @param context       XDIMEContext from which to retrieve the
     *                      {@link VolantisProtocol}
     * @return VolantisProtocol associated with this request
     */
    protected VolantisProtocol getProtocol(XDIMEContextInternal context) {
        return getPageContext(context).getProtocol();
    }

    /**
     * Copy those attributes common to all elements from the XDIME Attributes
     * to the Protocol attribute object. The common attributes (as defined by
     * the XDIME-CP specification) are:
     * <ul>
     * <li>the unqualified core attributes - id, class and title</li>
     * <li>the unqualified hypertext attributes - href and access</li>
     * </ul>
     * <P/>
     * However:
     * <ul>
     * <li>We don't need to copy the class attribute across as it will be used
     * when styling and the information it provides will be encapsulated in
     * the {@link com.volantis.styling.Styles} on the element.</li>
     * </ul>
     *
     * @param context
     * @param attributes    xdime attributes whose values should be used to

     */
    protected void initialiseCommonAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        final String id = getAttribute(XDIMEAttribute.ID, attributes);
        if (id != null) {
            protocolAttributes.setId(id);
            initialiseEventAttributes(context, id);
        }

        protocolAttributes.setTitle(
                getAttribute(XDIMEAttribute.TITLE, attributes));        
        protocolAttributes.setHref(
                getHrefAttribute(context, attributes));
    }

    /**
     * Initialise the protocol event attributes.
     * <p>
     * These are initialised indirectly by looking up the the XML Events
     * 'listener' and 'handler' elements via this elements 'id' attribute.
     *
     * @param context the XDIME context.
     * @param id the id of this element, used to look up events.
     */
    private void initialiseEventAttributes(XDIMEContextInternal context,
            final String id) {

        // todo: later: maybe this should do nothing if we are not in XDIME 2
        // mode. Otherwise XDIMECP might accidentally support events.
        // Detecting the mode is a bit tricky unfortunately with the current
        // structure in the general case.

        final MarinerPageContext pageContext = getPageContext(context);

        final EventAttributes eventAttributes =
                protocolAttributes.getEventAttributes(true);
        eventMapper.setEventAttributes(eventAttributes);

        // Iterate over any event listeners registered as observers this
        // element,
        ListenerEventRegistry listenerEventRegistry =
                pageContext.getListenerEventRegistry();
        Iterator iterator = listenerEventRegistry.getListenersById(id);
        if (iterator != null) {
            while (iterator.hasNext()) {
                ListenerEvent listenerEvent = (ListenerEvent)
                        iterator.next();

                // And add each listener's event into the protocol's event
                // attributes.
                eventMapper.mapEventToAttributes(listenerEvent);
            }
        }
    }

    /**
     * Responsible for initialising any protocol attributes which are specific
     * to a particular XDIME element implementation.
     * <p/>
     * NB: The default implementation of does nothing, so this method should be
     * overidden by subclasses which need to initialise element specific
     * attributes.
     *
     * @param context       in which this element is being processed
     * @param attributes    from which to initialise the protocol attributes
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        // do nothing by default, override if specific behaviour is required
    }

    /**
     * Initialise this element's protocol attributes using the supplied
     * XDIMEAttributes.
     *
     * @param context       in which this element is being processed
     * @param attributes    xdime attributes from which to initialise the
     */
    protected void initialiseAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        if (!attributesInitialised) {
            if (protocolAttributes != null) {
                // not all elements should cause markup to be output
                initialiseCommonAttributes(context, attributes);
                initialiseElementSpecificAttributes(context, attributes);
            }
            attributesInitialised = true;
        }
    }

    /**
     * Retrieve the {@link MCSAttributes} instance associated with this
     * xdime element.
     * @return MCSAttributes associated with this element
     */
    public MCSAttributes getProtocolAttributes() {
        return protocolAttributes;
    }

    /**
     * Call the appropriate open method on the protocol object.
     * <p/>
     * NB: This method does nothing by default and should be overridden to
     * provide any specialised behaviour that is required.
     *
     * @param context Context used to access the protocol.
     * @param attributes
     * @throws XDIMEException
     * @return XDIMEResult to indicate if the element should be processed or
     *         not.
     */
     protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        // do nothing by default, override if specific behaviour is required
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }


    /**
     * Call the appropriate close method on the protocol object.
     * <p/>
     * NB: This method does nothing by default and should be overridden to
     * provide any specialised behaviour that is required.
     *
     * @param context Context used to access the protocol.
     */
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        // do nothing by default, override if specific behaviour is required
    }

    /**
     * Returns the character data that has been encountered while processing
     * this element.
     *
     * @return String the collected character data
     */
    public String getCharData() {
        return dataHandlingStrategy.getCharacterData();
    }

    // Javadoc inherited.
    public ElementOutputState getOutputState() {
        if (state == null) {
            // todo Remove this extra check as it is only here to prevent tests
            // todo from breaking.
            if (outputStateBuilder == null) {
                outputStateBuilder = new ElementOutputStateBuilderImpl(
                        context, null, null, styles, 
                        suppressSkipForDisplayNoneStyle(),
                        isElementAtomic());
            }
            state = outputStateBuilder.createElementOutputState();
        }
        return state;
    }
    
    /**
     * Returns the value of the 'href' attribute ready to be set on the
     * protocolAttributes.
     * 
     * @param context The current XDIME context.
     * @param attributes The XDIME attributes to retrieve 'href'. 
     * @return The LinkAssetReference.
     * @throws XDIMEException
     */
    private LinkAssetReference getHrefAttribute(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        // Prepare a local field, which will store the LinkAssetReference
        // with the URL from the 'href' attribute.
        LinkAssetReference linkAssetReference = null;

        // Read the href attribute
        String url = getAttribute(XDIMEAttribute.HREF, attributes);

        // Do the actual processing, if the 'href' attribute is specified.
        if (url != null) {
            // Prepare the asset resolver and page URI rewriter for later use.
            AssetResolver assetResolver = getProtocol(context).getMarinerPageContext()
                    .getAssetResolver();
            PageURIRewriter uriRewriter = getProtocol(context).getMarinerPageContext()
                    .getPageURIRewriter();

            if (!uriRewriter.willPossiblyRewrite(PageURLType.ANCHOR)) {
                // If the URL is not to be rewritten using page URI rewriter
                // for which the current state of the page context is important,
                // simply create a LinkAssetReference in standard way.
                // The URL will be rewritten by PageURLRewriter when the 
                // getUrl() method gets called.
                linkAssetReference = new LiteralLinkAssetReference(url, assetResolver,
                        PageURLType.ANCHOR);

            } else {
                // If the URL possibly is to be rewritten using page URI
                // rewriter, rewrite the URL in-place, since at the moment the
                // getUrl() method would be invoked, the page context would 
                // change and the page URI rewriter would return different
                // result than expected.
                url = assetResolver.rewriteURLWithPageURLRewriter(url, PageURLType.ANCHOR);

                try {
                    url = uriRewriter.rewrite(new URI(url), PageURLType.ANCHOR).toString();
                } catch (URISyntaxException e) {
                    throw new XDIMEException("Invalid URI: " + url, e);
                }

                // Create an instance of LinkAssetReference with already
                // rewritten URL.
                linkAssetReference = new LiteralLinkAssetReference(url);
            }
        }

        return linkAssetReference;
    }
    
    /**
     * A flag indicating, that processing of elements with display:none style
     * should not be skipped. By default it returns false, but may be
     * overwritten for custom element subclasses.
     */
    protected boolean suppressSkipForDisplayNoneStyle() {
        return false;
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 02-Dec-05	10447/5	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 12-Oct-05	9673/11	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/9	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/7	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9562/5	pabbott	VBM:2005092011 Add XHTML2 Object element

 30-Sep-05	9637/2	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 21-Sep-05	9128/4	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/2	pabbott	VBM:2005071114 Add XHTML 2 elements

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
