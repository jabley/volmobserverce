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

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Wizard widget element.
 */
public class WizardElement extends WidgetElement implements Launchable {
    
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
                   
    /**
     * The container instance used as the main container
     */
    private RegionInstance anonymousRegionInstance;    
            
    /**
     * Creates and returns new instance of WizardElement, 
     * initalised with empty attributes.
     * @param context
     */
    public WizardElement(XDIMEContextInternal context) {
        super(WidgetElements.WIZARD, context);
        protocolAttributes = new WizardAttributes();
    }

    /**
     *  Add id and type for all dismiss elements in popup content
     */
    public void addLaunch(LaunchAttributes attrs) {
        ((WizardAttributes)protocolAttributes).addLaunch(attrs);                
    }

    /**
     * Get id attriobute from wizard element
     * 
     * @return id wizard
     */
    public String getWidgetId() {
        return protocolAttributes.getId();
    }
    
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        ((WizardAttributes)protocolAttributes).setCancelDialog(attributes.getValue("","cancel-dialog"));
    }
    
    // Javadoc inherited
    public XDIMEResult doElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        MarinerPageContext pageContext = getPageContext(context);
        
        // creating anonymous region instance
        // and pushing it into buffer
        anonymousRegionInstance = createAnonymousRegion(pageContext);    
        pageContext.pushContainerInstance(anonymousRegionInstance);
        OutputBuffer outputBuffer =
        	anonymousRegionInstance.getCurrentBuffer();
        pageContext.pushOutputBuffer(outputBuffer);
        
        return super.doElementStart(context, attributes);
    }    

    // Javadoc inherited
    public XDIMEResult doElementEnd(XDIMEContextInternal context)
            throws XDIMEException {

        MarinerPageContext pageContext = getPageContext(context);

        // do this first so that current pane is not surpressed.
        XDIMEResult result = super.doElementEnd(context);
        
        // Pop the anonymous region buffer that was pushed on at the
        // end of the doElementStart() method.
        pageContext.popContainerInstance(anonymousRegionInstance);      

        OutputBuffer anonymousBuffer =
                anonymousRegionInstance.getCurrentBuffer();
        pageContext.popOutputBuffer(anonymousBuffer);
        
        DeviceLayoutContext layoutToPop = pageContext.getDeviceLayoutContext();        
        
        pageContext.popDeviceLayoutContext();                

        ContainerInstance containingInstance =
            pageContext.getCurrentContainerInstance();

        // buffer from annonymous region must be merged
        // with current buffer from instance
        containingInstance.getCurrentBuffer().transferContentsFrom(
        		this.anonymousRegionInstance.getCurrentBuffer());
        return result;
    }        
    

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
    throws XDIMEException {
        
        WidgetModule widgetModule =  getWidgetModule(context);        
        // Do nothing if widgets are not supported at all
        if (null == widgetModule){
            return XDIMEResult.SKIP_ELEMENT_BODY;       
        }
                        
        try {        
            WizardRenderer wizardRenderer = widgetModule.getWizardRenderer();
            if (null == wizardRenderer){
                return XDIMEResult.SKIP_ELEMENT_BODY;       
            }            
            wizardRenderer.renderOpen(getProtocol(context), (WidgetAttributes)protocolAttributes);            
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
    
            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }    
        return XDIMEResult.PROCESS_ELEMENT_BODY;       
    }    
}
