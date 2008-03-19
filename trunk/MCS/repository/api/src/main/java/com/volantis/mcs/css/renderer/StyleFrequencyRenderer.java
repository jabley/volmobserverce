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

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.values.FrequencyUnit;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Render a style value representing a frequency.
 */
public class StyleFrequencyRenderer {

    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleFrequencyRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleFrequencyRenderer ();
    }

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static StyleFrequencyRenderer getSingleton () {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleFrequencyRenderer () {
    }

    /**
     * Render a StyleFrequency.
     * @param value the StyleFrequency to render
     * @param context the RendererContext within which to render the StyleFrequency
     */
    public void render(StyleFrequency value, RendererContext context)
        throws IOException {

        PrintWriter writer = context.getPrintWriter();

        double number = value.getNumber();
        FrequencyUnit unit = value.getUnit();

        // If rounding it to an int and back again does not change the
        // number then just write it out as an int.
        int intNumber = (int) number;
        if (intNumber == number) {
            writer.print(intNumber);
        } else {
            writer.print(number);
        }

        writer.write(unit.toString());
    }
}
