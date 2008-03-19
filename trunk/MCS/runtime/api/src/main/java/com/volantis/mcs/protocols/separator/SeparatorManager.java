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

package com.volantis.mcs.protocols.separator;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Manages separators, including maintaining any required state.
 *
 * <p>Instances of this are responsible for managing the state associated with
 * a single set of separators. e.g. Menus have three sets of separators, one
 * for creating the orientation of the menu, one for item groups and one for
 * orientation of an item. These are orthogonal in terms of their state,
 * although there are interactions between the managers. This means that there
 * would need to be three managers for a menu that uses all three sets of
 * separators.</p>
 *
 * <p>Each manager is associated with a single OutputBuffer, but each
 * OutputBuffer may have a number of managers, as in the case for menus
 * described above.</p>
 *
 * <p>The decision as to whether a separator should be rendered, or which one
 * should be rendered if there are two to choose from is performed by the
 * {@link SeparatorArbitrator} associated with this manager.</p>
 *
 * <p>See <a href="package.html">package.html</a> for more details as to how
 * the interfaces in this package work together.</p>
 *
 * @mock.generate
 */
public interface SeparatorManager {

    /**
     * Get the OutputBuffer with which this manager is associated.
     * @return The associated OutputBuffer.
     */
    public OutputBuffer getOutputBuffer();

    /**
     * Get the {@link SeparatorArbitrator} associated with this instance.
     *
     * @return The arbiter, may not be null.
     */
    public SeparatorArbitrator getArbitrator();

    /**
     * Queue a separator.
     *
     * <p>The separator will not be rendered immediately. Instead it will be
     * stored internally inside the manager and rendering delayed until later.
     * At some point in future the arbitrator will be consulted as to what
     * should be done with this separator.</p>
     *
     * @param separator The separator queue.
     * @throws RendererException If there was a problem rendering the separator.
     */
    public void queueSeparator(SeparatorRenderer separator)
        throws RendererException;

    /**
     * Indicates that some content is about to be written.
     *
     * <p>This method should be invoked immediately before some content is
     * written to the target buffer. This will cause any deferred renderers to
     * be rendered.</p>
     *
     * <p>The arbiter is still consulted on whether the deferred separator
     * should be rendered.</p>
     *
     * @param content An indication of the content that will be written, may
     * not be null.
     * 
     * @throws RendererException If there was a pending separator and it had a
     * problem rendering the code.
     */
    public void beforeContent(SeparatedContent content)
        throws RendererException;

    /**
     * Finish rendering any pending separators.
     *
     * <p>This will ensure that any pending separators will have been written
     * out.</p>
     *
     * @throws RendererException If there was a problem rendering the separator.
     */
    public void flush()
        throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
