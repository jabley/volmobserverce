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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.configuration.impl;

import com.volantis.map.ics.configuration.Configuration;
import com.volantis.map.ics.configuration.ConfigurationParser;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;


/**
 * DefaultConfigurationParser
 */
public class DefaultConfigurationParser implements ConfigurationParser {


    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            DefaultConfigurationParser.class);

    protected DefaultConfigurationParser() {
        // hide the constructor
    }

    // javadoc inherited
    public void marshal(Configuration config, OutputStream stream) {
        try {
            IBindingFactory bfact = BindingDirectory
                .getFactory(DefaultConfiguration.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            mctx.setIndent(4, null, ' ');
            mctx.marshalDocument(config, "UTF-8", null, stream);
        } catch (JiBXException e) {
            throw new RuntimeException(
                EXCEPTION_LOCALIZER.format("marshal-failure"), e);
        }
    }

    // javadoc inherited
    public Configuration unmarshal(InputStream stream) {
        try {
            IBindingFactory bfact = BindingDirectory
                .getFactory(DefaultConfiguration.class);
            IUnmarshallingContext uctx =
                bfact.createUnmarshallingContext();

            return (Configuration) uctx.unmarshalDocument(stream, null);
        } catch (JiBXException e) {
            throw new RuntimeException(
                EXCEPTION_LOCALIZER.format("unmarshal-failure"), e);
        }
    }

    // javadoc inherited
    public Configuration createDefaultConfiguration() {
        return new DefaultConfiguration();
    }

}


