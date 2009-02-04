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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatScope;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.runtime.RepositoryObjectAdapter;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuntimeLayoutAdapter
        extends RepositoryObjectAdapter
        implements RuntimeDeviceLayout {


    /**
     * The underlying {@link com.volantis.mcs.layouts.Layout}
     */
    private final Layout layout;

    /**
     * The compiled style sheet for the layout.
     */
    private final CompiledStyleSheet compiledStyleSheet;

    /**
     * Map from container names to enclosing fragments with container indices.
     *
     * The values are lists of DeviceLayoutActivator.ContainerPosition objects.
     */
    private final Map containerNameToFragments;

    /**
     * Initialise.
     *
     * @param name
     * @param layout
     * @param compiledStyleSheet
     * @param containerNameToFragments
     */
    public RuntimeLayoutAdapter(
            String name,
            Layout layout,
            CompiledStyleSheet compiledStyleSheet,
            Map containerNameToFragments) {
        super(name);

        this.layout = layout;
        this.compiledStyleSheet = compiledStyleSheet;
        this.containerNameToFragments = containerNameToFragments;
    }

    // Javadoc inherited.
    public String getDefaultFragmentName () {
        return layout.getDefaultFragmentName();
    }

    // Javadoc inherited.
    public String getDefaultSegmentName () {
        return layout.getDefaultSegmentName();
    }

    // Javadoc inherited.
    public String getDestinationLayout () {
        return layout.getDestinationLayout();
    }

    // Javadoc inherited.
    public int getFormatCount () {
        return layout.getFormatCount();
    }

    // Javadoc inherited.
    public FormatScope getFormatScope() {
        return layout.getFormatScope();
    }

    // Javadoc inherited.
    public String getLayoutGroupName () {
        return layout.getLayoutGroupName();
    }

    // Javadoc inherited.
    public Format getRootFormat () {
        return layout.getRootFormat();
    }

    // Javadoc inherited.
    public Format retrieveFormat(String name, FormatType formatType) {
        return layout.retrieveFormat(name, formatType);
    }

    // Javadoc inherited.
    public CompiledStyleSheet getCompiledStyleSheet() {
        return compiledStyleSheet;
    }

    // javadoc inherited
    public List getEnclosingFragments(final String containerName) {
        List fragments = (List) containerNameToFragments.get(containerName);
        if (fragments == null) {
            fragments = Collections.EMPTY_LIST;
        }
        return fragments;
    }
}
