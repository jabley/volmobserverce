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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.MediaAgentException;
import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.context.ListenerEventRegistry;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.debug.DebugTimer;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.impl.CanvasElementImpl;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.ActivatedLayoutContent;
import com.volantis.mcs.runtime.layouts.LayoutContentActivator;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorImpl;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.MCSLayoutKeywords;
import com.volantis.mcs.utilities.PolicyException;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEMode;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.events.EventRegistrar;
import com.volantis.mcs.xdime.events.EventsTable;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.styling.Styles;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.util.Stack;

/**
 * XHTML V2 Body element object.
 */
public class BodyElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(BodyElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(BodyElement.class);

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Defines the DOM Level 2 load and unload event for body. Note that the
     * DOM Level 2 load event does not specifically mention body but this is
     * assumed to be an oversight.
     */
    private static final EventsTable events = new EventsTable() {
        public void registerEvents(EventRegistrar registrar) {
            registrar.registerEvent("load", EventConstants.ON_LOAD);
            registrar.registerEvent("unload", EventConstants.ON_UNLOAD);
        }
    };

    /**
     * the html element within which this body element is placed
     */
    private HtmlElement htmlElement;

    /**
     * The attributes for the canvas that we write out in XDIME 2 mode.
     */
    private CanvasAttributes canvasAttributes;

    /**
     * A timer object for timing the processing of this element if in debug
     * mode.
     */
    private DebugTimer debugTimer;

    /**
     * The container instance used as the main container
     */
    private RegionInstance anonymousRegionInstance;

    public BodyElement(XDIMEContextInternal context) {
        super(XHTML2Elements.BODY, StyledStrategy.STRATEGY, context);
        protocolAttributes = new DivAttributes();
        canvasAttributes = new CanvasAttributes();

        events.registerEvents(eventMapper);
    }

    // Javadoc inherited
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        MarinerPageContext pageContext = getPageContext(context);

        XDIMEResult result;

        // We store the canvas initialisation data on the parent html
        // element. So get a reference to the parent html element.
        Stack elementStack = ((XDIMEContextImpl)context).getStack();

        //test that the html element has been added to the element stack
        if (elementStack.size() < 2) {
            Object html = elementStack.peek();
            if (!(html instanceof HtmlElement)) {
                logger.error("html-not-found");
                throw new IllegalStateException("No html element found");
            } else {
                htmlElement = (HtmlElement) html;
            }
        } else {
            htmlElement = (HtmlElement)
                    elementStack.get(elementStack.size() - 2);
        }

        // Push onto the stack the theme style sheet(s) that this element and
        // all it's content depends on, before the main processing for this
        // element begins. This must be done in order, least specific first.
        // NOTE: the parent HTML element is not being styled.
        // NOTE: this is done for both XDIME 2 and CP since CP supports inline
        // style sheets (only).
        final StylingEngine stylingEngine = pageContext.getStylingEngine();
        htmlElement.getThemeStyleSheets().pushAll(stylingEngine);

        // If we are in XDIME 2 mode
        if (htmlElement.getXDIMEMode() == XDIMEMode.XDIME2) {

            // Initialise the protocol attributes before we normally would
            // as the code below depends on it. Yuck. We really need to
            // refactor xdime 2 elements to avoid inheritance!
            initialiseAttributes(context, attributes);

            // Move any events from the the div to the canvas.
            // This means that the body specific events such as onload work.
            // todo: later: avoid writing out a div in xdime-2 mode
            EventAttributes events =
                    protocolAttributes.getEventAttributes(false);
            canvasAttributes.setEventAttributes(events);

            openCanvas(context, attributes);
        }
        // else, we are in XDIMECP mode, so do not open the canvas as this
        // will be done by the containing cdm:canvas element.

        result = super.exprElementStart(context, attributes);
        return result;
    }

    /**
     * open the canvas which will contain all of the body element contents
     * @param context
     * @param attributes
     * @throws XDIMEException
     */
    private void openCanvas(XDIMEContextInternal context,
            XDIMEAttributes attributes)
            throws XDIMEException {

        MarinerPageContext pageContext = getPageContext(context);

        if (logger.isDebugEnabled()) {
            debugTimer = new DebugTimer();
            debugTimer.beginTimer();

            // Log the URL of the requested page.
            String relativeRequestURL = pageContext.getRelativeRequestURL();
            logger.debug("Requested page is " + relativeRequestURL);

            // Log the pure URL.
            logger.debug("Processed request is "
                    + pageContext.getPureRequestURL().getExternalForm());
        }

        // Push the anonymous region that all other content is added to in.
        // This will be used as the default if no other layout can be found.
        // This has to happen before we attempt to do styling as the theme may
        // have a layout which will require a containing region. It must also
        // be done before the MPC.initialise method is called as that needs to
        // store the theme style sheets from the HTML element in the device
        // layout context.
        pushAnonymousRegion(pageContext);

        // Extract any layout which is managed by the parent html
        // element. If no layout is found the anonymous layout will be used.
        String layoutName = htmlElement.getLayoutName();
        if (logger.isDebugEnabled()) {
            logger.debug("Body layout: " + layoutName);
        }

        // Do the start styling for our element now rather than wait for the
        // superclass to do it, as we need the styles now.
        styleElementStart(context, attributes);

        // Get the property values for this element and push them into
        // the format styling engine.
        Styles styles = protocolAttributes.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        // If the layoutName is not null it means a layout has been specified
        // using either the link element or the mcs-project file. In order to
        // get the layout and container references to be updated in a
        // consistant way the layout name will be specified as one of the
        // styles property values. when the
        // StylableXDIMEElement#updateLayoutAndContainerReferences is called
        // the value correct layout will be used and a suppressing container
        // will be placed between the anonymous layout and the specified layout
        if (layoutName != null) {

            StyleValue styleLayout = propertyValues.getComputedValue(
                                    StylePropertyDetails.MCS_LAYOUT);

            //if a layout has been specified using the styles for the body
            //and the link element or the mcs-project.xml file an error is 
            //thrown as this is not allowed.
            if (styleLayout != null &&
                    styleLayout != MCSLayoutKeywords.CURRENT) {
                throw new IllegalStateException(
                        exceptionLocalizer.format("invalid-body-layout"));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("The MCS_LAYOUT value specified using " +
                            "the link element is being set on the body " +
                            "styles");
                }
            }

            propertyValues.setComputedValue(
                StylePropertyDetails.MCS_LAYOUT,
                STYLE_VALUE_FACTORY.getString(null, layoutName));
        }

        // Store the style properties into the device layout context to allow
        // it to inherit from them.
        ImmutablePropertyValues immutablePropertyValues =
                propertyValues.createImmutablePropertyValues();
        DeviceLayoutContext dlc = pageContext.getDeviceLayoutContext();
        dlc.setInheritableStyleValues(immutablePropertyValues);

        try {
            // Initialise the canvas, no layout is passed in as that will be
            // pushed on by the parent class.
            pageContext.initialise(false, false, null, null,
                    htmlElement.getThemeStyleSheets(), null);
            pageContext.pushCanvasType(CanvasElementImpl.CANVAS_TYPE_MAIN);
            pageContext.updateFragmentationState();

            // Open the canvas page.
            canvasAttributes.setStyles(styles);
            canvasAttributes.setPageTitle(htmlElement.getTitle());
            VolantisProtocol protocol = getProtocol(context);
            protocol.setWriteHead(true);
            protocol.openCanvasPage(canvasAttributes);
        } catch (PolicyException e) {
            logger.error("unexpected-exception", e);
            throw new XDIMEException(e);
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw new XDIMEException(e);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new XDIMEException(e);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", getTagName()), e);
        }

        // The previous call to protocol.openCanvas will push the page and body
        // output buffers onto the stack, hiding the output buffer for the
        // anonymous region so push that on again. This needs to be done to
        // ensure that by default content from inside the body element
        // goes into the region and not into the body buffer.
        OutputBuffer anonymousBuffer =
                anonymousRegionInstance.getCurrentBuffer();
        pageContext.pushOutputBuffer(anonymousBuffer);
    }

    /**
     * add an anonymous region to the page context
     * @param pageContext
     * @throws XDIMEException
     */
    private void pushAnonymousRegion(MarinerPageContext pageContext)
            throws XDIMEException {
        try {
            // Create a layout containing a simple region.
            RuntimeDeviceLayout anonymousLayout = createAnonymousRegionLayout();

            // Create a context for the layout and push it onto the page context.
            DeviceLayoutContext deviceLayoutContext = new DeviceLayoutContext();
            deviceLayoutContext.setMarinerPageContext(pageContext);
            deviceLayoutContext.setDeviceLayout(anonymousLayout);
            deviceLayoutContext.initialise();
            pageContext.pushDeviceLayoutContext(deviceLayoutContext);

            // Get an instance of the layout's region.
            Region region = (Region) anonymousLayout.getRootFormat();
            anonymousRegionInstance = (RegionInstance)
                    pageContext.getFormatInstance(region,
                            NDimensionalIndex.ZERO_DIMENSIONS);

            // Push the region instance onto the container stack.
            pageContext.pushContainerInstance(anonymousRegionInstance);
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw new XDIMEException(e);
        }
    }

    /**
     * create an anonymous region
     * @return
     * @throws RepositoryException
     */
    private RuntimeDeviceLayout createAnonymousRegionLayout()
            throws RepositoryException {

        // todo: later: layout builder is from the pre-xdime accessors.
        // We probably should not be using it. We can read one in from internal
        // xml file in a static initialiser the same way we load default
        // stylesheets.
        LayoutBuilder builder = new LayoutBuilder(
                new RuntimeLayoutFactory());

        // here the anonymous layout device name is being used to specifically
        // identify the region as the anonymous one. This is then used when
        // creating the anonymous region to allow it not to specify a name.
        builder.createLayout(LayoutType.CANVAS);
        builder.setAnonymous(true);
        builder.pushFormat(FormatType.REGION.getTypeName(), 0);
        builder.setAttribute(FormatConstants.WIDTH_ATTRIBUTE, "100");
        builder.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                             FormatConstants.WIDTH_UNITS_VALUE_PERCENT);
        builder.attributesRead();
        builder.popFormat();
        Layout layout = builder.getLayout();

        InternalPolicyFactory policyFactory = (InternalPolicyFactory)
                PolicyFactory.getDefaultInstance();
        InternalLayoutContentBuilder layoutContent =
                policyFactory.createLayoutContentBuilder();
        layoutContent.setLayout(layout);

        StyleSheetActivator styleSheetActivator =
                new StyleSheetActivatorImpl(null, null);

        // Activate it to turn it into a runtime device layout
        LayoutContentActivator activator = new LayoutContentActivator();
        final ActivatedLayoutContent activatedLayoutContent =
                activator.activateLayoutContent(styleSheetActivator,
                        (InternalLayoutContent) layoutContent.getContent());
        RuntimeDeviceLayout runtimeLayout = new RuntimeLayoutAdapter(
                "<anonymous>", layout,
                activatedLayoutContent.getCompiledStyleSheet(),
                activatedLayoutContent.getContainerNameToFragments());

        return runtimeLayout;
    }

    //Javadoc Inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        try {
            VolantisProtocol protocol = getProtocol(context);
            protocol.writeOpenDiv((DivAttributes)protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    //Javadoc Inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);
        
        protocol.writeCloseDiv((DivAttributes)protocolAttributes);

    }

    // Javadoc inherited
    public XDIMEResult exprElementEnd(XDIMEContextInternal context)
            throws XDIMEException {

        MarinerPageContext pageContext = getPageContext(context);

        // do this first so that current pane is not surpressed.
        XDIMEResult result = super.exprElementEnd(context);
        
        // XForms controls can only appear in the body element, so at this
        // point we know we've found all of them. Now generate any implicit
        // elements for the last forms model and register the complete form
        // descriptors with the session context.
        final VolantisProtocol protocol = getProtocol(context);

        final XFormBuilder xFormBuilder = context.getXFormBuilder();

        xFormBuilder.generateImplicitElements(protocol);

        xFormBuilder.registerFormDescriptors(protocol);

        ListenerEventRegistry eventRegistry =
                pageContext.getListenerEventRegistry();
        // Fail if the page has event listeners which were unused.
        eventRegistry.complete();

        // Render Widget closure.
        WidgetModule module = protocol.getWidgetModule();
        
        if (module != null) {
            try {
                module.renderClose(protocol);
            } catch (ProtocolException e) {
                logger.error("rendering-error", getTagName(), e);

                throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", getTagName()), e);
            }
        }
        
        // Wait for MediaAgent to complete all its requests and invoke all
        // pending callbacks.
        waitForMediaAgentCompletion(context);
        
        if (htmlElement.getXDIMEMode() == XDIMEMode.XDIME2) {

            closeCanvas(context);

        }

        // Remove the theme style sheets associated with this element.
        // This must be done in reverse order of addition.
        // Note that this is done for both XDIME 2 and CP since CP supports
        // inline style sheets (only).
        final StylingEngine stylingEngine = pageContext.getStylingEngine();
        htmlElement.getThemeStyleSheets().popAll(stylingEngine);

        return result;
    }

    /**
     * if the page is a xdime-2 page the canvas being used to hold the body
     * contents needs to be closed.
     * @param context
     * @throws XDIMEException
     */
    private void closeCanvas(XDIMEContextInternal context)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);
        MarinerPageContext pageContext = getPageContext(context);
        try {
            // Pop the anonymous region buffer that was pushed on at the
            // end of the openCanvas() method.
            OutputBuffer anonymousBuffer =
                    anonymousRegionInstance.getCurrentBuffer();
            pageContext.popOutputBuffer(anonymousBuffer);

            pageContext.endPhase1BeginPhase2();

            protocol.closeCanvasPage(canvasAttributes);

            //pageContext.popCanvasType();

            // Pop the device layout context created from the anonymous layout.
            pageContext.popDeviceLayoutContext();

            // This has to happen after close canvas page so that the
            // protocol's page and body buffers are closed properly.
            // TODO: refactor closeCanvasPage to split out page rendering
            // so that we can pop this before page rendering takes place.
            pageContext.popContainerInstance(anonymousRegionInstance);

            // TODO: add page tracking functionality via consequence VBM.
            // see PageElementImpl.exprElementEnd
            // It is public API (via JMX) and would need to be enhanced
            // significantly to make sense for XDIME 2.

            if (logger.isDebugEnabled()) {
                String relativeRequestURL = pageContext.getRelativeRequestURL();
                debugTimer.stopTimer(relativeRequestURL+ " page runtime = ");
            }

        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new XDIMEException(e);
        } catch (ProtocolException e) {
            logger.error("unexpected-exception", e);
            throw new XDIMEException(e);
        }
    }
    
    /**
     * Waits until MediaAgent processes all its requests, and invokes all
     * pending callbacks.
     * 
     * @param context An XDIME context
     * @throws XDIMEException
     */
    private void waitForMediaAgentCompletion(XDIMEContextInternal context)
            throws XDIMEException {
        // Get an instance of MediaAgent.
        MarinerPageContext pageContext = getPageContext(context);
        
        MediaAgent mediaAgent = pageContext.getMediaAgent(false);
        
        // Do nothing, if there's no MediaAgent instance associated with page
        // context.
        if (mediaAgent != null) {
            try {
                mediaAgent.waitForComplete();
            } catch (MediaAgentException e) {
                logger.error("rendering-error", getTagName(), e);
    
                throw new XDIMEException(exceptionLocalizer.format(
                        "rendering-error", getTagName()), e);
            }
        }        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10679/1	ianw	VBM:2005120605 Back out background-color inheritance and make xdimecp body output div

 07-Dec-05	10659/1	ianw	VBM:2005120605 Back out background-color inheritance and make xdimecp body output div

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
