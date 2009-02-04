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
package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.XMLDebugStyles;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StylePropertySet;

import java.io.IOException;
import java.io.Writer;

public class StyledDocumentWriter
        extends XMLDocumentWriter {

    private final XMLDebugStyles stylesDebugger;
    private final String styleAttributeName;

    public StyledDocumentWriter(
            Writer writer,
            StylePropertySet interestingProperties,
            boolean onlyExplicitlySpecified) {
        super(writer);
        stylesDebugger = new XMLDebugStyles(interestingProperties,
                onlyExplicitlySpecified);
        styleAttributeName = "style";
    }

    public StyledDocumentWriter(Writer writer, String styleAttributeName) {
        super(writer);
        stylesDebugger = new XMLDebugStyles(null, false);
        this.styleAttributeName = styleAttributeName;
    }

    @Override
    protected final void outputAttributes(
            Element element,
            CharacterEncoder encoder) throws IOException {
        super.outputAttributes(element, encoder);

        Styles styles = element.getStyles();
        String stylesString = stylesDebugger.output(styles, "");
        if (stylesString != null && stylesString.length() > 0) {
            writer.write(" " +
                    styleAttributeName +
                    "='");
            writer.write(stylesString);
            writer.write("'");
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 ===========================================================================
*/
