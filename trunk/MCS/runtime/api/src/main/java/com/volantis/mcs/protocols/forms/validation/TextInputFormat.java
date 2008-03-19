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

package com.volantis.mcs.protocols.forms.validation;

public class TextInputFormat {

    /**
     * The MCS specific format used to specify a validation string for a
     * text input field.
     */
    private final String format;

    /**
     * Indicates whether the format allows the field to be empty.
     *
     * <p>This information can be inferred from the format above, it is
     * separated in order to allow it to be easily queried and also in one
     * particular case it is overridden by the protocol, although I am not
     * sure that is correct.</p>
     */
    private final boolean emptyOk;

    public TextInputFormat(String pattern, boolean emptyOk) {
        this.format = pattern;
        this.emptyOk = emptyOk;
    }

    public String getFormat() {
        return format;
    }

    public boolean isEmptyOk() {
        return emptyOk;
    }
}
