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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to TableBodyElement.
 */
public class TableBodyAttributes extends WidgetAttributes {
    /**
     * The Load attributes, used by this TableBody Widget.
     */
    private LoadAttributes loadAttributes;
    
    /**
     * The XHTML2 TableBody element attributes, on which this TableBody widget
     * is based.
     */
    private com.volantis.mcs.protocols.TableBodyAttributes xhtml2Attributes;

    /**
     * Attribute indicating how many pages should be cached by the widget.
     * Value -1 means no caching.
     */
    private int cachedPagesCount = 0;

    /**
     * @return Returns the loadAttributes.
     */
    public LoadAttributes getLoadAttributes() {
        return loadAttributes;
    }

    /**
     * @param loadAttributes The loadAttributes to set.
     */
    public void setLoadAttributes(LoadAttributes loadAttributes) {
        this.loadAttributes = loadAttributes;
    }

    /**
     * Returns the XHTML2 TableBody element attributes, on which this TableBody
     * widget is based.
     * 
     * @return Returns the xhtml2Attributes.
     */
    public com.volantis.mcs.protocols.TableBodyAttributes getXHTML2Attributes() {
        return xhtml2Attributes;
    }

    /**
     * Sets the XHTML2 TableBody element attributes, on which this TableBody
     * widget is based.
     * 
     * @param xhtml2Attributes The xhtml2Attributes to set.
     */
    public void setXHTML2Attributes(com.volantis.mcs.protocols.TableBodyAttributes xhtml2Attributes) {
        this.xhtml2Attributes = xhtml2Attributes;
    }

    /**
     * @return Returns the cachedPagesCount.
     */
    public int getCachedPagesCount() {
        return cachedPagesCount;
    }

    /**
     * @param cachedPagesCount The cachedPagesCount to set.
     */
    public void setCachedPagesCount(int cachedPagesCount) {
        this.cachedPagesCount = cachedPagesCount;
    }
}
