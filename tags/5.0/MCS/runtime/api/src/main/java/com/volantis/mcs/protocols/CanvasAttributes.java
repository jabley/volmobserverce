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
 * $Header: /src/voyager/com/volantis/mcs/protocols/CanvasAttributes.java,v 1.18 2002/08/21 16:57:14 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 27-Jul-01    Paul            VBM:2001072603 - Cleaned up.
 * 05-Sep-01    Allan           VBM:2001090308 - Added initialFocus attribute.
 * 25-Oct-01    Pether          VBM:2001102402 - Added type attribute.
 * 02-Nov-01    Paul            VBM:2001102403 - Removed type attribute, it is
 *                              not needed by the protocol.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the methods more
 *                              consistent.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 06-Aug-02    Paul            VBM:2002073008 - Added support for overlay
 *                              attribute.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added support for WML onevent 
 *                              handlers
 * 06-Aug-02    Paul            VBM:2002080509 - Removed on... attributes as
 *                              these are all part of EventAttributes.
 * 21-Aug-02    Mat             VBM:2002081508 - Added uaContext attribute.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

public class CanvasAttributes
        extends MCSAttributes {

    private String initialFocus;

    /**
     * Flag which indicates whether the canvas should be treated as an overlay.
     */
    private boolean overlay;
    private String pageTitle;
    private String uaContext;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public CanvasAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("canvas");

        initialFocus = null;
        overlay = false;
        pageTitle = null;
        uaContext = "current";
    }

    /**
     * Set the value of the initialFocus property.
     *
     * @param initialFocus The new value of the initialFocus property.
     */
    public void setInitialFocus(String initialFocus) {
        this.initialFocus = initialFocus;
    }

    /**
     * Get the value of the initialFocus property.
     *
     * @return The value of the initialFocus property.
     */
    public String getInitialFocus() {
        return initialFocus;
    }

    /**
     * Set the value of the overlay property.
     *
     * @param overlay The new value of the overlay property.
     */
    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }

    /**
     * Get the value of the overlay property.
     *
     * @return The value of the overlay property.
     */
    public boolean isOverlay() {
        return overlay;
    }

    /**
     * Set the value of the pageTitle property.
     *
     * @param pageTitle The new value of the pageTitle property.
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * Get the value of the pageTitle property.
     *
     * @return The value of the pageTitle property.
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * Get the value of uaContext.
     *
     * @return value of uaContext.
     */
    public String getUaContext() {
        return uaContext;
    }

    /**
     * Set the value of uaContext.
     *
     * @param uaContext Value to assign to uaContext.
     */
    public void setUaContext(String uaContext) {
        this.uaContext = uaContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
