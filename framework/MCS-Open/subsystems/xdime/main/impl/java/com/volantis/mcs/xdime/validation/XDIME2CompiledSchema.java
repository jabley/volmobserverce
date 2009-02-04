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

package com.volantis.mcs.xdime.validation;

import com.volantis.mcs.xdime.events.XMLEventsSchema;
import com.volantis.mcs.xdime.gallery.GallerySchema;
import com.volantis.mcs.xdime.mcs.MCSSchema;
import com.volantis.mcs.xdime.ticker.TickerResponseSchema;
import com.volantis.mcs.xdime.ticker.TickerSchema;
import com.volantis.mcs.xdime.widgets.WidgetResponseSchema;
import com.volantis.mcs.xdime.widgets.WidgetSchema;
import com.volantis.mcs.xdime.xforms.XFormsSchema;
import com.volantis.mcs.xdime.xhtml2.XHTML2Schema;
import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.compiler.SchemaCompiler;
import com.volantis.mcs.xml.schema.compiler.SchemaCompilerFactory;
import com.volantis.synergetics.UndeclaredThrowableException;

public class XDIME2CompiledSchema {

    private static final SchemaCompilerFactory SCHEMA_COMPILER_FACTORY =
            SchemaCompilerFactory.getDefaultInstance();

    private static final CompiledSchema COMPILED_SCHEMA;
    static {
        try {
            SchemaCompiler compiler = SCHEMA_COMPILER_FACTORY.createSchemaCompiler();

            XHTML2Schema xhtml2 = new XHTML2Schema();
            XFormsSchema xforms = new XFormsSchema(xhtml2);
            WidgetSchema widget = new WidgetSchema(xhtml2, xforms);
            WidgetResponseSchema response = new WidgetResponseSchema(xhtml2, widget);
            TickerSchema ticker = new TickerSchema(xhtml2, widget);
            TickerResponseSchema tickerResponse = new TickerResponseSchema(xhtml2,
                    response);
            GallerySchema gallery = new GallerySchema(xhtml2, widget, response);
            MCSSchema MCS = new MCSSchema(xhtml2);
            XMLEventsSchema events = new XMLEventsSchema();
          
            compiler.addSchema(xhtml2);
            compiler.addSchema(xforms);
            compiler.addSchema(widget);
            compiler.addSchema(ticker);
            compiler.addSchema(gallery);
            compiler.addSchema(MCS);
            compiler.addSchema(events);
          
            // Should probably be kept separate.
            compiler.addSchema(response);
            compiler.addSchema(tickerResponse);

            COMPILED_SCHEMA = compiler.getCompiledSchema();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UndeclaredThrowableException(e);
        }
    }

    public static final CompiledSchema getCompiledSchema() {
        return COMPILED_SCHEMA;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
