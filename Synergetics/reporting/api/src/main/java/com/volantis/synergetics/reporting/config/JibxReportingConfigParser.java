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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.config;

import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * The default ReportingConfig parser.
 */
public class JibxReportingConfigParser {


    // javadoc inherited
    public ReportingConfig parse(InputStream inputStream) {
        try {
            IBindingFactory bfact = BindingDirectory.getFactory(
                ReportingConfig.class);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();

            return (ReportingConfig) uctx.unmarshalDocument(inputStream, null);
        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }

    // javadoc inherited
    public void write(ReportingConfig config, OutputStream stream) {
        try {
            IBindingFactory bfact = BindingDirectory
                .getFactory(ReportingConfig.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            mctx.setIndent(4, null, ' ');
            mctx.marshalDocument(config, "UTF-8", null, stream);
        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }
}


