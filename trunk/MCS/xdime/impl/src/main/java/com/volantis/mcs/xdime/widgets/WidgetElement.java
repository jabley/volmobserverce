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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.ActivatedLayoutContent;
import com.volantis.mcs.runtime.layouts.LayoutContentActivator;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivatorImpl;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.StylingStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 *	Base class for all Widget elements. Implements common methods 
 *
 */
public abstract class WidgetElement extends StylableXDIMEElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(WidgetElement.class);
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(WidgetElement.class);

    public WidgetElement(
            ElementType type, StylingStrategy stylingStrategy,
            XDIMEContextInternal context) {
        super(type, stylingStrategy, context);
    }

    public WidgetElement(ElementType type, XDIMEContextInternal context) {
        this(type, StyledStrategy.STRATEGY, context);
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        
        try {
            WidgetRenderer widgetRenderer = getWidgetRenderer(context);
            if (null == widgetRenderer){
                // Do fallback if widget is not supported by the protocol
                return doFallbackOpen(context, attributes);        
            }
            widgetRenderer.renderOpen(getProtocol(context), (WidgetAttributes)protocolAttributes);
            if (!widgetRenderer.shouldRenderContents(getProtocol(context), (WidgetAttributes)protocolAttributes)) {
                return XDIMEResult.SKIP_ELEMENT_BODY; 
            }
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }    
        return XDIMEResult.PROCESS_ELEMENT_BODY;       
     }
    
     public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
         try {
             WidgetRenderer widgetRenderer = getWidgetRenderer(context);
             if (null == widgetRenderer){
                 // Do fallback if widget is not supported by the protocol
                 doFallbackClose(context);
                 return;       
             }
             widgetRenderer.renderClose(getProtocol(context), (WidgetAttributes)protocolAttributes);
         } catch (ProtocolException e) {
             logger.error("rendering-error", getTagName(), e);

             throw new XDIMEException(exceptionLocalizer.format(
                 "rendering-error", getTagName()), e);
         }            // TODO: do we need to do anything here?
      }

    /**
     * Fallback behaviour for non-HTML protocols that do not support widgets 
     * 
     * Default implementation does nothing and returns PROCESS_ELEMENT_BODY.
     * Derived classes may override thismethod to provide their own fallback,
     * but it must be protocol independent (the actual rendering must be 
     * delegated to the protocol) 
     */
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        // By default do nothing and process body
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * Fallback behaviour for non-HTML protocols that do not support widgets
     * 
     * Default implementation does nothing.
     * Derived classes may override thismethod to provide their own fallback,
     * but it must be protocol independent (the actual rendering must be 
     * delegated to the protocol)
     */
    protected void doFallbackClose(XDIMEContextInternal context) {
        // By default do nothing 
    }

    /**
     * Convenience method for getting Widget Module implementation
     * associated with currently used protocol 
     */
    protected WidgetModule getWidgetModule(XDIMEContextInternal context) {
        return getProtocol(context).getWidgetModule();        
    }
    
    /**
     * Convenience method for getting Widget Renderer implementation
     * associated with currently used protocol 
     * 
     * @throws ProtocolException 
     */
    protected WidgetRenderer getWidgetRenderer(XDIMEContextInternal context) 
        throws ProtocolException {
        
        // First get the widget module, which is a part of protocol 
        // responsible for rendering widgets 
        WidgetModule widgetModule =  getWidgetModule(context);
        
        // No renderer if no module 
        if (null == widgetModule){
            return null;
        }

        // Get renderer for this particular widget
        return widgetModule.getWidgetRenderer(protocolAttributes);
    }

    // javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        // do nothing by default, override if specific behaviour is required
    }    
    
    protected OutputBuffer createOutputBuffer(XDIMEContextInternal context) {
        return getProtocol(context).
            getOutputBufferFactory().createOutputBuffer();
    }     
    
    
    /**
     * Create anonymaousRegion in page context without pushing returned 
     * region to cointainer stack. 
     * Returned region instanceshould be push onto the container stack
     * 
     * @param pageContext
     * @return RegionInstance created Region Instance
     * @throws XDIMEException
     */ 
     protected RegionInstance createAnonymousRegion(MarinerPageContext pageContext)
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
             RegionInstance anonymousRegionInstance = (RegionInstance)
                     pageContext.getFormatInstance(region,
                             NDimensionalIndex.ZERO_DIMENSIONS);
         
             return anonymousRegionInstance;
             
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
     protected RuntimeDeviceLayout createAnonymousRegionLayout()
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

    // javadoc inherited
	protected boolean suppressSkipForDisplayNoneStyle() {
		return true;
	}
     
     
    
}
