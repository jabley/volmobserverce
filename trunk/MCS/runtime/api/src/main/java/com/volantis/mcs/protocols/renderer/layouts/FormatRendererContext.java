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

package com.volantis.mcs.protocols.renderer.layouts;

import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DeviceLayoutRenderer;
import com.volantis.mcs.protocols.NDimensionalContainer;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.shared.layouts.IteratedFormatInstanceCounter;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;

/**
 * Encapsulates all the contextual information needed when rendering format
 * contents.
 *
 * @mock.generate 
 */
public interface FormatRendererContext {

    LayoutModule getLayoutModule();

    OutputBufferStack getOutputBufferStack();

    /**
     * Push a new {@link DeviceLayoutContext} on the stack.
     *
     * @param context The {@link DeviceLayoutContext} to store
     */
    void pushDeviceLayoutContext(DeviceLayoutContext context);

    DeviceLayoutContext getDeviceLayoutContext();

    void popDeviceLayoutContext();

    /**
     * Gets the container for format instances for a particular format within
     * the current context.
     *
     * @param format the format to return the container for.
     * @return the container for format instances.
     */
    public NDimensionalContainer getFormatInstancesContainer(Format format);

    /**
     * Sets the current format index.
     *
     * <p>This method is used within the device layout renderer to control the
     * current format instance instance during format iteration.</p>
     *
     * @param index The index to set
     */
    void setCurrentFormatIndex(NDimensionalIndex index);

    /**
     * Gets the format instance for a particular format and index.
     * <p>
     * This represents the format "instance" at this index. For example,
     * if the format is a pane and it is contained within a single spatial
     * iterator, then requesting such an object for the pane with an index of
     * [2] would return the third "instance" of the pane within the spatial
     * iterator.
     *
     * @param format The object whose FormatInstance is required.
     * @param index the index of the format instance which is required.
     * @return The FormatInstance which was allocated for the specified format.
     */
    FormatInstance getFormatInstance(Format format, NDimensionalIndex index);

    /**
     * Get an object that will count the maximum number of instances used
     * from within a specific point in the format hierarchy.
     *
     * @return A maximum instance counter.
     */
    IteratedFormatInstanceCounter getInstanceCounter();

    /**
     * Get the inclusion path.
     *
     * @return The inclusion path.
     */
    String getInclusionPath();

    boolean isFragmentationSupported();

    FormFragment getCurrentFormFragment();

    Fragment getCurrentFragment();

    /**
     * Get the {@link FragmentLinkWriter} that must be used to write links to
     * fragments within this context.
     *
     * @return The {@link FragmentLinkWriter}.
     */
    FragmentLinkWriter getFragmentLinkWriter();

    void beginNestedLayout(DeviceLayoutContext context);

    void endNestedLayout(DeviceLayoutContext context);

    DeviceLayoutRenderer getDeviceLayoutRenderer();

    RuntimeDeviceLayout getDeviceLayout();

    SegmentLinkWriter getSegmentLinkWriter();

    void renderFormat(final FormatInstance instance)
            throws RendererException;

    FormatStylingEngine getFormatStylingEngine();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
