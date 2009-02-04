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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.html.menu.DeprecatedEventAttributeUpdater;
import com.volantis.mcs.protocols.html.menu.DeprecatedExternalShortcutRenderer;

/**
 * A class allows client objects to locate objects which can generate protocol 
 * specific markup output.
 * <p>
 * Aka the markup output module ;-).
 *
 * todo This should probably be renamed to be something like ProtocolModules
 * todo and should allow access to the different modules supported by the
 * todo protocol. The methods should be moved into an appropriate module.,
 * todo Possible modules are TextModule (containing div and span), ImageModule
 * todo containing image, LinkModule containing anchor, and EventModule
 * todo containing event updater.
 *
 * todo All of the methods implemented by referenced objects should probably
 * todo take some context object in future so that they do not have to be
 * todo created for each instance of the protocol.
 */ 
public interface DeprecatedOutputLocator {

    /**
     * Returns an object for generating &lt;div&gt; output.
     */ 
    DeprecatedDivOutput getDivOutput();
    
    /**
     * Returns an object for generating &lt;span&gt; output.
     */ 
    DeprecatedSpanOutput getSpanOutput();
    
    /**
     * Returns an object for generating &lt;img&gt; output.
     */ 
    DeprecatedImageOutput getImageOutput();
    
    /**
     * Returns an object for generating &lt;a&gt;/&lt;anchor&gt; output.
     */ 
    DeprecatedAnchorOutput getAnchorOutput();

    /**
     * Returns an object for generating &lt;br&gt; output.
     */
    DeprecatedLineBreakOutput getLineBreakOutput();

    /**
     * Returns an object for updating event attributes.
     */
    DeprecatedEventAttributeUpdater getEventAttributeUpdater();

    /**
     * Returns an object for supporting external shortcut renderers.
     */
    DeprecatedExternalShortcutRenderer getExternalShortcutRenderer();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 ===========================================================================
*/
