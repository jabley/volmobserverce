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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderWidgetRenderer;
import com.volantis.mcs.xdime.DataHandlingStrategy;
import com.volantis.mcs.xdime.IgnoreDataStrategy;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Abstract superclass for all styled XForms element classes.
 */
public abstract class StyledXFormsElement extends XFormsElement {

    /**
     * Initialize a new instance using the given parameters.
     */
    protected StyledXFormsElement(
            ElementType type, XDIMEContextInternal context) {
        this(type, IgnoreDataStrategy.getDefaultInstance(), context);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param type                  type of the element
     * @param dataHandlingStrategy  determines how any data (character data or
     *                              markup) encountered while processing this
     * @param context
     */
    protected StyledXFormsElement(
            ElementType type,
            DataHandlingStrategy dataHandlingStrategy,
            XDIMEContextInternal context) {
        super(StyledStrategy.STRATEGY, type, dataHandlingStrategy, context);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // Set the caption containers. This implementation is for styled xform
        // elements that are not controls (i.e. item, value and label) but
        // should be overridden by the control implementations
        ((SelectOption) protocolAttributes).setCaptionContainerInstance(
                getPageContext(context).getCurrentContainerInstance());
    }

    /**
     * Convenience method for accessing FieldExpander renderer
     */
    protected FieldExpanderWidgetRenderer getFieldExpanderRenderer(XDIMEContextInternal context) 
            throws ProtocolException {
        
        WidgetModule widgetModule = getProtocol(context).getWidgetModule();
        if (null != widgetModule) {
            return widgetModule.getFieldExpanderRenderer();
        }
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 10-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/3	emma	VBM:2005092807 Adding tests for XForms emulation

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 09-Sep-05	9415/2	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
