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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleList;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A post-processor for applying a nested post-processor to all elements of a
 * style list.
 */
public class StyleListPostProcessor extends AbstractPostProcessor {
    /**
     * The nested post-processor.
     */
    private StyleValuePostProcessor processor;

    /**
     * Create a post-processor for applying the specified post-processor to
     * all elements of a style list.
     *
     * @param processor The processor to apply
     */
    public StyleListPostProcessor(StyleValuePostProcessor processor) {
        this.processor = processor;
    }

    // Javadoc inherited
    public StyleValue postProcess(final StyleValue value) {
        StyleValue processed = value;
        if (value instanceof StyleList) {
            StyleList list = (StyleList) value;
            List values = list.getList();
            List processedValues = new ArrayList();
            boolean changed = false;
            Iterator it = values.iterator();
            while (it.hasNext()) {
                StyleValue listValue = (StyleValue) it.next();
                StyleValue listValueProcessed = processor.postProcess(listValue);
                processedValues.add(listValueProcessed);
                if (listValue != listValueProcessed) {
                    changed = true;
                }
            }
            if (changed) {
                StyleList processedList =
                    STYLE_VALUE_FACTORY.getList(processedValues);
                processed = processedList;
            }
        }
        return processed;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 ===========================================================================
*/
