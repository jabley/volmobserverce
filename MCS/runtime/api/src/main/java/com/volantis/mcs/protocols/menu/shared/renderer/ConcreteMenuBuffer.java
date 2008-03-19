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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.html.menu.HTMLMenuOrientationSeparatorArbitrator;
import com.volantis.mcs.protocols.menu.shared.MenuSeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.separator.shared.DefaultSeparatorManager;
import com.volantis.mcs.protocols.separator.shared.UseDeferredSeparatorArbitrator;

/**
 * A concrete implementation of a MenuBuffer.
 */
public class ConcreteMenuBuffer implements MenuBuffer {
    /**
     * The <code>OutputBuffer</code> wrapped by this class.
     *
     * @supplierCardinality 1
     */
    private final OutputBuffer outputBuffer;

    /**
     * The orientation separator.
     */
    private final SeparatorRenderer orientationSeparator;

    /**
     * The item group separator manager.
     */
    private SeparatorManager separatorManager;

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param outputBuffer
     *         The OutputBuffer that is wrapped by this object. May not be
     *         null.
     * @param orientationSeparator
     *         The separator to use for orientation.
     */
    public ConcreteMenuBuffer(
            OutputBuffer outputBuffer,
            SeparatorRenderer orientationSeparator) {

        if (outputBuffer == null) {
            throw new IllegalArgumentException("outputBuffer may not be null");
        }

        this.outputBuffer = outputBuffer;
        this.orientationSeparator = orientationSeparator;
    }

    // javadoc inherited
    public OutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    // Javadoc inherited.
    public SeparatorManager getSeparatorManager() {
        // Create the separator manager lazily.
        if (separatorManager == null) {
            separatorManager = createSeparatorManager();
            if (separatorManager == null) {
                throw new IllegalStateException
                        ("createItemGroupSeparatorManager must not return null");
            }
        }

        return separatorManager;
    }

    /**
     * Create the separator manager for handling separators between groups.
     * @return The separator manager, may not be null.
     */
    private SeparatorManager createSeparatorManager() {

        // todo Allow the following to be customised.

        SeparatorArbitrator groupArbitrator
                = UseDeferredSeparatorArbitrator.INSTANCE;

        SeparatorArbitrator orientationArbitrator
                = HTMLMenuOrientationSeparatorArbitrator.INSTANCE;

        SeparatorManager orientationManager = new DefaultSeparatorManager(
                outputBuffer, orientationArbitrator);

        return new MenuSeparatorManager(outputBuffer,
                                        groupArbitrator,
                                        orientationSeparator,
                                        orientationManager);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4164/4	pduffin	VBM:2004050404 Fixed problems with test cases, specifically those caused by ConcreteMenuBuffer throwing an UnsupportedOperationException

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 21-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/2	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
