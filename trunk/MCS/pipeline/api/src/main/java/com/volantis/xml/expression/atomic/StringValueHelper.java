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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * A class that provides help when various data types must be converted to
 * string values.
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-exclude-from InternalAPI
 */
public class StringValueHelper {
    /**
     * The symbols that should be used when rendering string representations of
     * numeric values. This is explicitly defined to ensure that the output
     * format is always consistent with XML Schema's data type lexical
     * representations, irrespective of the JVM's default locale.
     *
     * <p>Note that this is static and thus must be cloned before use (since
     * {@link java.text.DecimalFormat} clones the symbols when given them
     * nothing need be done in these cases - other cases must be managed
     * explicitly).</p>
     *
     * <p>Use of this symbol table is only important, in relation to
     * DecimalFormat, if the pattern includes the use of any symbols (e.g.
     * decimal point) rather than simple digit placeholders.</p>
     */
    public static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS =
            new DecimalFormatSymbols(Locale.ENGLISH);
}
