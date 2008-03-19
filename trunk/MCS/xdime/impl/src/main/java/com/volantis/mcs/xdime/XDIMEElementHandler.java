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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMap;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;
import com.volantis.mcs.xdime.initialisation.XDIME2Populator;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.xml.sax.Attributes;

/**
 * This factory creates instances of {@link XDIMEElement} and
 * {@link XDIMEAttributes} which have been populated using the supplied
 * parameters.
 */
public class XDIMEElementHandler {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XDIMEElementHandler.class);

    /**
     * The default instance.
     */
    private static final XDIMEElementHandler DEFAULT_INSTANCE;

    static {
        ElementFactoryMapBuilder builder = new ElementFactoryMapBuilder();
        ElementFactoryMapPopulator populator = new XDIME2Populator();
        populator.populateMap(builder);
        ElementFactoryMap factoryMap = builder.buildFactoryMap();
        DEFAULT_INSTANCE = new XDIMEElementHandler(factoryMap);
    }

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static XDIMEElementHandler getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private final ElementFactoryMap factoryMap;

    public XDIMEElementHandler(ElementFactoryMap factoryMap) {
        this.factoryMap = factoryMap;
    }

    /**
     * Creates an appropriate {@link XDIMEElement} for this element.
     *
     * @param elementType the element
     * @param context the context within which the element will be used.
     * @return an XDIMEElement
     * @throws XDIMEException if the namespace is unrecognised
     */
    public XDIMEElement createXDIMEElement(
            ElementType elementType,
            XDIMEContextInternal context) throws XDIMEException {

        XDIMEElement element = null;
        ElementFactory factory = factoryMap.getElementFactory(elementType);
        if (factory == null) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                    "unknown-element", elementType));
        } else {
            element = factory.createElement(context);
        }
        return element;
    }

    /**
     * Create an XDIMEAttributes instance and initialize it from the SAX
     * attributes.
     *
     * @param elementType
     * @param saxAttributes     The sax attributes with which to initialize
     *                          this object
     * @return The newly created instance of the appropriate XDIMEAttributes
     *         class.
     * @throws XDIMEException if there was a problem creating the attributes.
     */
    public XDIMEAttributes createXDIMEAttributes(
            ElementType elementType, Attributes saxAttributes)
            throws XDIMEException {

        XDIMEAttributes attributes = new XDIMEAttributesImpl(elementType);
        initializeAttributes(saxAttributes, attributes);
        return attributes;
    }

    /**
     * Initialize the {@link XDIMEAttributes} from the SAX {@link Attributes}.
     * This method should be overridden by subclasses if different
     * initialization is required.
     *
     * @param saxAttributes used to initialise the {@link XDIMEAttributes}
     * @param attributes the {@link XDIMEAttributes} to initialize
     */
    protected void initializeAttributes(Attributes saxAttributes,
                                        XDIMEAttributes attributes) {
        int length = saxAttributes.getLength();

        for (int i = 0; i < length; i++) {
            String name = saxAttributes.getLocalName(i);
            if ("selid".equals(name)) {
                name = "id";
            }
            attributes.setValue(saxAttributes.getURI(i), name,
                    saxAttributes.getValue(i));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
