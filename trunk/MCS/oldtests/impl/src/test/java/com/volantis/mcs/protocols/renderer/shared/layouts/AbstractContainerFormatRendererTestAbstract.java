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
package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererSelector;

/**
 * Abstract base for extension by tests of
 * {@link FormatRenderer} specializations.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public abstract class AbstractContainerFormatRendererTestAbstract
        extends AbstractFormatRendererTestAbstract {
    /**
     * Creates a pane with no content.
     * @return Format A Pane with default configuration.
     */
    protected Format createPane() {
        return createPane(false, null);
    }

    /**
     * Creates a pane, optionally creating an instance for it for a specified
     * index, and writing dummy content to ensure that it does not show as
     * empty.
     * @param content True if content is to be written
     * @param index The index, which can be null if no content is written
     * @return The pane specified by the arguments
     */
    protected Format createPane(boolean content, NDimensionalIndex index) {
        Pane p = new Pane((CanvasLayout) layout);
        setFormatInstance(p);
        if (content) {
            PaneInstance pi =
                    (PaneInstance) dlContext.getFormatInstance(p, index);
            pi.getCurrentBuffer().writeText("non-empty");
        }
        return p;
    }

    /**
     * Sets the format instance value for a specified format as the current
     * format count for the device layout, then increments that value.
     * @param fmt The format for which the instance property should be set
     */
    protected void setFormatInstance(Format fmt) {
        int instance = layout.getFormatCount();
        fmt.setInstance(instance++);
        layout.setFormatCount(instance);
    }

    /**
     * The default selector for format renderers.
     */
    protected FormatRendererSelector defaultSelector =
            new DefaultFormatRendererSelector();

    /**
     * Factory method used to construct a concrete implementation instance.
     *
     * @return the new concrete implementation instance
     */
    protected abstract FormatRenderer createFormatRenderer();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/6	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 06-Dec-04	6383/1	philws	VBM:2004120206 Provide base infrastructure for FormatRenderer mechanism

 ===========================================================================
*/
