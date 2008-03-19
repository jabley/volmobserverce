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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.jibx.JiBXBase;

public class CSSStyleSheetImpl
        extends JiBXBase
        implements CSSStyleSheet {

    private String css;

    private CSSParserMode parserMode;

    public void validate(ValidationContext context) {
    }

    public String getCSS() {
        return css;
    }

    public void setCSS(String css) {
        this.css = css;
    }

    public CSSParserMode getParserMode() {
        return parserMode;
    }

    public void setParserMode(CSSParserMode parserMode) {
        this.parserMode = parserMode;
    }
}
