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

package com.volantis.styling.debug;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertySet;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A writer that can be used to write debugging information relating to styling.
 */
public class DebugStylingWriter {

    private StringBuffer buffer = new StringBuffer();
    private final LogDispatcher logger;
    private final StylePropertySet interestingProperties;

    /**
     * Initialise.
     *
     * @param logger The underlying logger.
     */
    public DebugStylingWriter(LogDispatcher logger,
                              StylePropertySet interestingProperties) {
        if (interestingProperties == null) {
            MutableStylePropertySet mutable = new MutableStylePropertySetImpl();
            mutable.addAll();
            interestingProperties = mutable;
        }

        this.interestingProperties = interestingProperties;
        this.logger = logger;
    }

    public DebugStylingWriter print(String s) {
        buffer.append(s);
        return this;
    }

    public DebugStylingWriter print(Debuggable debuggable) {
        debuggable.debug(this);
        return this;
    }

    public DebugStylingWriter print(int i) {
        buffer.append(i);
        return this;
    }

    public DebugStylingWriter print(Object o) {
        buffer.append(o);
        return this;
    }

    public DebugStylingWriter print(StyleProperty property, StyleValue value,
                                    String separator) {

        if (value != null && interestingProperties.contains(property)) {
            buffer.append(property.getName());
            buffer.append(": ");
            buffer.append(value.getStandardCSS());
            buffer.append(separator);
        }

        return this;
    }

    public DebugStylingWriter newline() {
        /* To avoid error when commiting
        Checking for unconditional logger.debug() statements...
        */
        if (logger.isDebugEnabled()) {
            logger.debug(buffer.toString());
        }
        buffer.setLength(0);
        return this;
    }

    public DebugStylingWriter flush() {
        if(buffer.length() > 0) {
            newline();
        }
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
