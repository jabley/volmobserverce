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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/MarinerXmlBooleanConverter.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a Converter for the 
 *                              handling boolean values in mariner-config.xml 
 *                              file.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.beanutils.ConversionException;
import our.apache.commons.beanutils.Converter;


/**
 * A {@link our.apache.commons.beanutils.Converter} for the handling boolean 
 * values in mcs-config.xml file. 
 * <p>
 * Implemented using the "Copy and Paste" pattern on 
 * {@link our.apache.commons.beanutils.converters.BooleanConverter} with 
 * "enabled" and "disabled" added as values.
 * <p>
 * Real fix would be to add a BooleanValues class, get BooleanConverter and 
 * BooleanArrayConverter to use it, then register "enabled" and "disabled"
 * with it, and send it in to Apache as a patch.  
 */
public final class MarinerXmlBooleanConverter implements Converter {


    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link our.apache.commons.beanutils.Converter} that will throw 
     * a {@link our.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public MarinerXmlBooleanConverter() {

        this.defaultValue = null;
        this.useDefault = false;

    }


    /**
     * Create a {@link our.apache.commons.beanutils.Converter} that will 
     * return the specified default value if a conversion error occurs.
     *
     * @param defaultValue The default value to be returned
     */
    public MarinerXmlBooleanConverter(Object defaultValue) {

        this.defaultValue = defaultValue;
        this.useDefault = true;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The default value specified to our Constructor, if any.
     */
    private Object defaultValue = null;


    /**
     * Should we return the default value on conversion errors?
     */
    private boolean useDefault = true;


    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     *
     * @exception our.apache.commons.beanutils.ConversionException if 
     *  conversion cannot be performed successfully
     */
    public Object convert(Class type, Object value) {

        if (value == null) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }

        if (value instanceof Boolean) {
            return (value);
        }

        try {
            String stringValue = value.toString();
            if (stringValue.equalsIgnoreCase("yes") ||
                stringValue.equalsIgnoreCase("enabled") ||
                stringValue.equalsIgnoreCase("y") ||
                stringValue.equalsIgnoreCase("true") ||
                stringValue.equalsIgnoreCase("on") ||
                stringValue.equalsIgnoreCase("1")) {
                return (Boolean.TRUE);
            } else if (stringValue.equalsIgnoreCase("no") ||
                       stringValue.equalsIgnoreCase("disabled") ||
                       stringValue.equalsIgnoreCase("n") ||
                       stringValue.equalsIgnoreCase("false") ||
                       stringValue.equalsIgnoreCase("off") ||
                       stringValue.equalsIgnoreCase("0")) {
                return (Boolean.FALSE);
            } else if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(stringValue);
            }
        } catch (ClassCastException e) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(e);
            }
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
