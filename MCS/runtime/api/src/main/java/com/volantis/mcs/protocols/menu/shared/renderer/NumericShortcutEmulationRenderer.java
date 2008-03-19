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

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * This provides a means of emulating numeric shortcuts when rendering links.
 * If an instance of this class is required in a situation where emulation
 * is not required or is not supported then a null value is acceptable.
 *
 * <p>
 * <strong>Usage:</strong>
 * <br/>
 * In a render method which has an instance of
 * <code>NumericEmulationRenderer<code> provided the general pattern of usage
 * would be something akin to:
 * <br/>
 * <pre>
 * ...
 * public void openTag(...) {
 *      // Numeric emulation as the emulation object is not null
 *      if (emulation != null) {
 *          // start the emulation wrapping element
 *          emulation.start(dom);
 *      }
 *
 *      ... own render code ...
 *
 *      // Numeric emulation as the emulation object is not null
 *      if (emulation != null) {
 *          // Add the dummy shortcut as an accesskey value
 *          setAttribute(element, "accesskey", emulation.getShortcut());
 *      }
 *
 *      ... any more attributes etc. ...
 *
 *      // Add the shortcut prefix
 *      if (emulation != null) {
 *          emulation.outputPrefix(dom);
 *      }
 * }
 *
 * ...
 *
 * public void closeTag(...) {
 *
 *      ... own render code ...
 *
 *      // Numeric emulation as the emulation object is not null
 *      if (emulation != null) {
 *          emulation.end(dom);
 *      }
 * }
 * ...
 * </pre>
 * </p>
 */
public interface NumericShortcutEmulationRenderer {

    /**
     * Used to start the numeric emulation rendering.  Called before any
     * specific render output is produced to ensure the numeric emulation
     * encloses the custom rendering.  For more information see the usage
     * details in the class JavaDoc of 
     * {@link NumericShortcutEmulationRenderer}.
     *
     * @param buffer The buffer to output the emulation rendering to.
     */
    public void start(DOMOutputBuffer buffer);

    /**
     * Used to end the numeric emulation rendering.  Called after any
     * specific render output is produced to ensure the numeric emulation
     * encloses the custom rendering.  For more information see the usage
     * details in the class JavaDoc of 
     * {@link NumericShortcutEmulationRenderer}.
     *
     * @param buffer The buffer to output the emulation rendering to.
     */
    public void end(DOMOutputBuffer buffer);

    /**
     * Provides the shortcut value that this renderer uses to indicate
     * the shortcut key to use.
     *
     * @return The shortcut value to use as an attribute in client code.
     */
    public TextAssetReference getShortcut();

    /**
     * Outputs the prefix to the buffer provided.  This prefix should match
     * the shortcut set so that the visual display matches the behaviour
     * of the generated markup.  This will not output any prefix if the device
     * determines it is not necessary.
     *
     * @param buffer The buffer to output the emulation rendering to.
     */
    public void outputPrefix(DOMOutputBuffer buffer);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/4	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 27-Apr-04	4025/5	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/3	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 ===========================================================================
*/
