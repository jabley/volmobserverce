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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.BlockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.FetchAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.BlockContainerAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.EffectBlockAttributes;
import com.volantis.mcs.protocols.widgets.internal.renderers.EffectBlockDefaultRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Container widget suitable for HTML protocols.
 */
public class BlockDefaultRenderer extends WidgetDefaultRenderer {

    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.SHOW,
            ActionName.HIDE,
            ActionName.CLEAR_CONTENT,
            ActionName.SHOW_CONTENT,
            ActionName.HIDE_CONTENT,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.STATUS,
            PropertyName.CONTENT_STATUS,
            PropertyName.CONTENT,
            PropertyName.DISPLAYED_CONTENT,
            
            PropertyName.LOAD,
            PropertyName.FETCH,
            PropertyName.REFRESH,
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.HIDING,
            EventName.HIDDEN,
            EventName.SHOWING,
            EventName.SHOWN,
            EventName.CONTENT_HIDING,
            EventName.CONTENT_HIDDEN,
            EventName.CONTENT_SHOWING,
            EventName.CONTENT_SHOWN,
        };
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(EffectBlockDefaultRenderer.class);

    private EffectBlockAttributes outerEffectBlockAttributes;

    private EffectBlockAttributes innerEffectBlockAttributes;

    private BlockContainerAttributes blockContainerAttributes;

    private Stack blockIdStack = new Stack();

    private boolean containsBlockContent;

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        // If protocol does not support Framework Client, render the div element only.
        if (!isWidgetSupported(protocol)) {
            return;
        }

        require(WidgetScriptModules.BASE_BB_BLOCK, protocol, attributes);
        
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        // Open outer EffectBlock widget, which will be used to show/hide this
        // Block widget.
        outerEffectBlockAttributes = new EffectBlockAttributes();
        
        outerEffectBlockAttributes.copy(attributes);
        
        outerEffectBlockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(outerEffectBlockAttributes)
            .renderOpen(protocol, outerEffectBlockAttributes);

        // Open inner EffectBlock widget, which will be used to show/hide the
        // content of this Block widgets.
        innerEffectBlockAttributes = new EffectBlockAttributes();
        
        innerEffectBlockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        innerEffectBlockAttributes.setStyles(StylingFactory.getDefaultInstance()
                .createInheritedStyles(
                        protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), DisplayKeywords.BLOCK));
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(innerEffectBlockAttributes)
            .renderOpen(protocol, innerEffectBlockAttributes);

        // Open BlockContainer widget, which will be used to change the content
        // of this Block widget.
        blockContainerAttributes = new BlockContainerAttributes();
        
        blockContainerAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        blockContainerAttributes.setStyles(StylingFactory.getDefaultInstance()
                .createInheritedStyles(
                        protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), DisplayKeywords.BLOCK));
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContainerAttributes)
            .renderOpen(protocol, blockContainerAttributes);
        
        openingBlock(attributes.getId());
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        // require AJAX script module
        BlockAttributes blockAttributes = (BlockAttributes)attributes;
        if ( blockAttributes.getRefreshAttributes() != null || blockAttributes.getLoadAttributes() != null || blockAttributes.getFetchAttributes() != null ) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }
        
        closingBlock();
        
        // If this Block contains a BlockContent child element,
        // the inner effect block should be rendered with Display.BLOCK style,
        // and not Display.NONE.
        if (!containsBlockContent) {
            innerEffectBlockAttributes.getStyles().getPropertyValues()
                .setComputedAndSpecifiedValue(StylePropertyDetails.DISPLAY, DisplayKeywords.NONE);
        }
        
        // Close BlockContent, BlockContainer, EffectBlock and Div elements.
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockContainerAttributes)
            .renderClose(protocol, blockContainerAttributes);
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(innerEffectBlockAttributes)
            .renderClose(protocol, innerEffectBlockAttributes);

        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(outerEffectBlockAttributes)
            .renderClose(protocol, outerEffectBlockAttributes);
        
        // Render JavaScript
        StringBuffer buffer = new StringBuffer();
        
        if (attributes.getId() != null) {
            buffer.append("$RW(")
                .append(createJavaScriptString(attributes.getId()))
                .append(",");
            
            addCreatedWidgetId(attributes.getId());
        }
        
        buffer.append("new Widget.Block(")
            .append(createJavaScriptWidgetReference(outerEffectBlockAttributes.getId()))
            .append(",")
            .append(createJavaScriptWidgetReference(innerEffectBlockAttributes.getId()))
            .append(",")
            .append(createJavaScriptWidgetReference(blockContainerAttributes.getId()));
        
        addUsedWidgetId(outerEffectBlockAttributes.getId());
        
        addUsedWidgetId(innerEffectBlockAttributes.getId());
        
        addUsedWidgetId(blockContainerAttributes.getId());
        
        // initialize and add action widget if exists inside XDIME element
        
               
        boolean delimited = false;
        
        buffer.append(",{");        
        if(((BlockAttributes)attributes).getFetchAttributes() != null) {

            require(WidgetScriptModules.BASE_BB_FETCH, protocol, attributes);
            
            // render javascript constructor 
            FetchAttributes fetchAttrs = ((BlockAttributes)attributes).getFetchAttributes(); 
            buffer.append("fetch: new Widget.BlockFetch({src: '");
            buffer.append(fetchAttrs.getSrc() + "'");
            
            // add localization of installed MCS
            buffer.append(", pageBase : '/").append(protocol.getMarinerPageContext()
                    .getVolantisBean().getPageBase()).append("'");
            
            if(fetchAttrs.getTransformation() != null) {
                buffer.append(", transformation: '");
                
                MarinerPageContext pageContext = protocol.getMarinerPageContext();                
                // get MarinerURL to page with xsl                
                MarinerURL marinerURL = pageContext.getRequestURL(true);
                URI path = null;
                try {
                    path = new URI(marinerURL.getExternalForm());
                } catch (URISyntaxException e) {
                    throw new IllegalStateException("URI to xsl transformation template is invalid");
                }                
                
                URI fullPath = path.resolve(fetchAttrs.getTransformation());                                                
                buffer.append(fullPath.toString() + "'");                                  
            }

            if(fetchAttrs.getService() != null) {
                buffer.append(", service: '");
                buffer.append(fetchAttrs.getService() + "'");                
            }                       
            
            if(fetchAttrs.getWhen() != null) {
                buffer.append(", when: '");
                buffer.append(fetchAttrs.getWhen() + "'");                
            }

            if(fetchAttrs.getTransformCache() != null) {
                buffer.append(", transformCache: '");
                buffer.append(fetchAttrs.getTransformCache() + "'");                
            }

            if(fetchAttrs.getTransformCompile() != null) {
                buffer.append(", transformCompile: '");
                buffer.append(fetchAttrs.getTransformCompile() + "'");                
            }
            
            buffer.append("})");            
            delimited = true;
        }

        if(((BlockAttributes)attributes).getLoadAttributes() != null) {

            require(WidgetScriptModules.BASE_BB_LOAD, protocol, attributes);

            if(delimited) {
                buffer.append(",");
            }
            LoadAttributes loadAttrs = ((BlockAttributes)attributes).getLoadAttributes(); 
            buffer.append("load: new Widget.BlockLoad({src: '");
            buffer.append(loadAttrs.getSrc() + "'");
            
            if(loadAttrs.getWhen() != null) {
                buffer.append(", when: '");
                buffer.append(loadAttrs.getWhen() + "'");                
            }
            buffer.append("})");                        
            delimited = true;
        }

        if(((BlockAttributes)attributes).getRefreshAttributes() != null) {

            require(WidgetScriptModules.BASE_BB_REFRESH, protocol, attributes);

            if(delimited) {
                buffer.append(",");
            }
            RefreshAttributes refreshAttrs = ((BlockAttributes)attributes).getRefreshAttributes(); 
            buffer.append("refresh: new Widget.BlockRefresh({src: '");
            buffer.append(refreshAttrs.getSrc() + "'");
            
            if(refreshAttrs.getInterval() != null) {
                buffer.append(", interval: '");
                buffer.append(refreshAttrs.getInterval() + "'");                
            }
            buffer.append("})");                        
            delimited = true;
        }
                
        buffer.append("})");
                
        // Render closing parentheses for Widget.Register invokation
        if (attributes.getId() != null) {
            buffer.append(")");                  
        }
        
        // Write JavaScript Container to DOM.
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    // Javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    // Javadoc inherited
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }    
    
    // Javadoc inherited
    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }
    
    private void openingBlock(String id) {
        blockIdStack.push(id);
        
        containsBlockContent = false;
    }
    private String closingBlock() {
        return (String) blockIdStack.pop();
    }
    protected String openingBlockContent() {
        String blockId = blockIdStack.isEmpty() ? null : (String) blockIdStack.peek();
        
        blockIdStack.push(null);
        
        return blockId;
    }
    protected String closingBlockContent() {
        blockIdStack.pop();

        String blockId = blockIdStack.isEmpty() ? null : (String) blockIdStack.peek();
 
        // If BlockContent in closing directly within the Block widget,
        // indicate that flag on following flag, so that in renderClose()
        // method some action may be taken.
        if (blockId != null) {
            containsBlockContent = true;
        }
        
        return blockId;
    }
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return isWidgetSupported(protocol);
    }
}
