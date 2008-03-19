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

package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderedContent;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.separator.shared.DefaultSeparatorManager;

/**
 * Manages separators at the level of the menus.
 *
 * <p>This manages separators around menu item groups and orientation (between
 * menu items) separators but does not manage separators within a menu
 * item.</p>
 *
 * <p>This manager extends rather than changes the behaviour of its base class.
 * This change affects two places, the rendering of separators and the handling
 * or content.</p>
 *
 * <p>This class was created to support the following behaviour.</p>
 *
 * <p>When a menu item is rendered it must cause the following effects:</p>
 * <ol>
 *   <li>Render the queued menu item group separator, if any.</li>
 *   <li>Render an orientation separator, if necessary.</li>
 *   <li>Render the menu item markup itself.</li>
 * </ol>
 *
 * <p>When the menu item group separator is rendered it must cause the
 * following effects:</p>
 * <ol>
 *   <li>Render an orientation separator, if necessary.</li>
 *   <li>Render the menu item group separator markup itself.</li>
 * </ol>
 *
 * <p>This class is responsible, at least in part, for all but the last step in
 * each sequence. It delegates the management of the orientation to another
 * manager. The following table shows how the required actions are
 * supported.</p>
 * <br/>
 * <table border="1">
 *   <thead>
 *     <th>Action</th>
 *     <th>Method</th>
 *     <th>Behaviour</th>
 *   </thead>
 *
 *   <tr>
 *     <td>Queue group separator</td>
 *     <td>{@link #queueSeparator}</td>
 *     <td>Default behaviour.</td>
 *   </tr>
 *
 *   <tr>
 *     <td>Before rendering menu item content</td>
 *     <td>{@link #beforeContent}</td>
 *     <td>After performing default behaviour (which causes any queued
 *         separators to be rendered) it queues the orientation separator with
 *         the orientation manager and informs it that content is about to be
 *         written. This will trigger the orientation manager to render the
 *         separator if appropriate, as determined by the arbitrator.</td>
 *   </tr>
 * </table>
 */
public class MenuSeparatorManager
        extends DefaultSeparatorManager {

    /**
     * The manager responsible for orientation separators.
     */
    private final SeparatorManager orientationManager;

    /**
     * The separator used for creating the required orientation.
     */
    private final SeparatorRenderer orientationSeparator;

    /**
     * Initialise.
     * @param outputBuffer The output buffer that this is managing.
     * @param arbitrator The arbitrator responsible for making decisions about
     * the item group separators.
     * @param orientationSeparator The separator to use that defines the
     * orientation.
     * @param orientationManager The manager responsible for orientation
     * separators.
     */
    public MenuSeparatorManager(OutputBuffer outputBuffer,
                                SeparatorArbitrator arbitrator,
                                SeparatorRenderer orientationSeparator,
                                SeparatorManager orientationManager) {
        super(outputBuffer, arbitrator);

        // Initialise the orientation manager and related fields.
        this.orientationSeparator = orientationSeparator;
        this.orientationManager = orientationManager;
    }

    /**
     * Override to interact with the orientation manager.
     */
    public void beforeContent(SeparatedContent content)
            throws RendererException {
        super.beforeContent(content);

        // Queue up a separator in the orientation manager.
        orientationManager.queueSeparator(orientationSeparator);

        // Inform the orientation manager that some content is about to be
        // written to the output buffer.
        orientationManager.beforeContent(content);
    }

    /**
     * Override to interact with the orientation manager.
     */
    protected void renderSeparator(OutputBuffer outputBuffer,
                                   SeparatorRenderer separator)
            throws RendererException {

        // Make sure that there is an orientation separator queued up in the
        // orientation manager.
        orientationManager.queueSeparator(orientationSeparator);

        // As far as the orientation manager is concerned the separator that
        // is about to be written should be treated as content.

        // todo- The content type is possibly dependent on the type of the
        // todo- separator. If that is needed then this class should be
        // todo- customised with an object to make the choice.
        orientationManager.beforeContent(MenuItemRenderedContent.IMAGE);

        super.renderSeparator(outputBuffer, separator);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
