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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.Segment;

import java.util.Collection;
import java.util.HashSet;

/**
 * A visitor for identifying the list of valid values for default fragment
 * or segment for a given layout.
 */
public class DefaultTargetFormatVisitor extends FormatVisitorAdapter {
    /**
     * Get a collection of valid names for the specified target type in the
     * layout provided.
     *
     * @param layout The layout to search for valid names
     * @param type The type of target to search for
     * @return A collection of valid names for the specified target
     */
    public Collection getValidNames(Layout layout, DefaultTargetType type) {
        DefaultTargetCollector descriptor =
                new DefaultTargetCollector(type);
        Format format = layout.getRootFormat();
        if (format != null) {
            format.visit(this, descriptor);
        }
        return descriptor.getValues();
    }

    // Javadoc inherited.
    public boolean visit(Fragment fragment, Object object)
            throws FormatVisitorException {
        DefaultTargetCollector descriptor = (DefaultTargetCollector) object;
        if (descriptor.isFragment()) {
            addIfNotEmpty(fragment.getName(), descriptor);
        }
        return super.visit(fragment, object);
    }

    // Javadoc inherited.
    public boolean visit(Segment segment, Object object) {
        DefaultTargetCollector descriptor = (DefaultTargetCollector) object;
        if (descriptor.isSegment()) {
            addIfNotEmpty(segment.getName(), descriptor);
        }
        return super.visit(segment, object);
    }

    /**
     * Add a name to the specified collector if it is not empty (null or the
     * empty string).
     *
     * @param name The name to add
     * @param collector The collector to which it should be added
     */
    private void addIfNotEmpty(String name, DefaultTargetCollector collector) {
        if (name != null && !"".equals(name)) {
            collector.addValue(name);
        }
    }

    /**
     * A data structure for collecting the valid values for a specified type of
     * default target.
     */
    private class DefaultTargetCollector {
        /**
         * The target type to collect.
         */
        private DefaultTargetType targetType;

        /**
         * A set of valid values for the specified target type.
         */
        private Collection validValues = new HashSet();

        /**
         * Create a default target collector for the specified type.
         *
         * @param type The type of default target to collect
         */
        public DefaultTargetCollector(DefaultTargetType type) {
            targetType = type;
        }

        /**
         * Returns true if collecting fragments.
         *
         * @return True if collecting fragments
         */
        public boolean isFragment() {
            return targetType == DefaultTargetType.FRAGMENT;
        }

        /**
         * Returns true if collecting segments.
         *
         * @return True if collecting segments
         */
        public boolean isSegment() {
            return targetType == DefaultTargetType.SEGMENT;
        }

        /**
         * Add a valid value for the associated target type.
         *
         * @param newValue The value to add
         */
        public void addValue(String newValue) {
            validValues.add(newValue);
        }

        /**
         * Get all values collected so far.
         *
         * @return A collection of all default values collected so far
         */
        public Collection getValues() {
            return validValues;
        }
    }
}
