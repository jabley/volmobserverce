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
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.menu.model.Titled;

/**
 * Because the {@link Titled} interface is used in contexts where an
 * abstract implementation cannot be used (we would require Java to support
 * multiple inheritance), this interface is introduced to provide an abstracted
 * way of setting the title on implementations of <code>Titled</code>.
 */
public interface MutableTitled extends Titled {
    /**
     * Sets the title associated with this entity.
     *
     * @param title The title to use.
     */
    public void setTitle(String title);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Mar-04	3491/1	philws	VBM:2004031912 Add handling of title to Menu Label

 ===========================================================================
*/
