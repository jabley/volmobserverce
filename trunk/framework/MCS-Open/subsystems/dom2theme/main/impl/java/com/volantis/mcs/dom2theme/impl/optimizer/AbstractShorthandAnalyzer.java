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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertySet;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.mcs.themes.MutableStyleProperties;

import java.util.Iterator;
import java.util.Collections;

/**
 * Base for all the {@link ShorthandAnalyzer}s.
 */
public abstract class AbstractShorthandAnalyzer
        implements ShorthandAnalyzer {

    /**
     * An empty property set.
     */
    private static final MutableStylePropertySet EMPTY =
            new EmptyMutablePropertySet();

    private final StyleProperty[] properties;

    protected AbstractShorthandAnalyzer(StyleProperty[] properties) {
        this.properties = properties;
    }

    // Javadoc inherited.
    public void updateShorthand(MutableStyleProperties outputValues) {
        updateShorthand(outputValues, EMPTY, EMPTY);
    }

    /**
     * An empty property set that can have properties removed from it but
     * not added.
     */
    private static class EmptyMutablePropertySet
            implements MutableStylePropertySet {

        public void add(StyleProperty property) {
            throw new UnsupportedOperationException();
        }

        public void add(StylePropertySet set) {
            throw new UnsupportedOperationException();
        }

        public void addAll() {
            throw new UnsupportedOperationException();
        }

        public void remove(StyleProperty property) {
        }

        public void clear() {
        }

        public ImmutableStylePropertySet createImmutableStylePropertySet() {
            throw new UnsupportedOperationException();
        }

        public MutableStylePropertySet createMutableStylePropertySet() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(StyleProperty property) {
            return false;
        }

        public StyleProperty next(StyleProperty property) {
            return END;
        }

        public IterationAction iterateStyleProperties(
                StylePropertyIteratee iteratee) {
            return IterationAction.CONTINUE;
        }

        public Iterator stylePropertyIterator() {
            return Collections.EMPTY_LIST.iterator();
        }
    }

    // Javadoc inherited.
    public void removeProperties(MutableStylePropertySet individualProperties) {
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            individualProperties.remove(property);
        }
    }
}
