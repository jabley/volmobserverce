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


package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.WapInputFormatRenderer;
import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;

import java.io.IOException;
import java.io.Writer;

/**
 * The runtime implementation of {@link WapInputFormatRenderer}. This
 * specialisation renders the value in the protocol specific manner.
 */
public class RuntimeWapInputFormatRenderer extends WapInputFormatRenderer {

    public void renderValue(StyleValue value, RendererContext context)
            throws IOException {

        if (value instanceof StyleString) {
            Writer writer = context.getWriter();
            ValidationHelper validationHelper =
                ((RuntimeRendererContext)context)
                    .getRuntimeRendererProtocolConfiguration()
                    .getValidationHelper();

            String validValue =
                    validationHelper.createTextInputFormat(
                            ((StyleString)value).getString());
            writer.write("\"");
            writer.write(validValue);
            writer.write("\"");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
