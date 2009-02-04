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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.device;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.io.Reader;

/**
 * The compiler for device style sheets.
 */
public class DeviceCSSCompiler
        implements CSSCompiler {

    /**
     * The CSS parser.
     */
    private final CSSParser parser;

    /**
     * The style sheet compiler.
     */
    private final StyleSheetCompiler compiler;

    /**
     * Initialise.
     *
     * @param parser  The parser.
     * @param outlook The outlook on the device information.
     */
    public DeviceCSSCompiler(CSSParser parser, DeviceOutlook outlook) {
        this.parser = parser;
        compiler = new DeviceStyleSheetCompiler(outlook);
    }

    // Javadoc inherited.
    public CompiledStyleSheet compile(Reader input, String url) {
        StyleSheet sheet = parser.parseStyleSheet(input, url);

        return compiler.compileStyleSheet(sheet);
    }
}
