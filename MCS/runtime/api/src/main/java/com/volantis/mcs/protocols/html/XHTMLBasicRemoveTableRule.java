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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.trans.TransTable;

/**
 * A remove table rule for XHTMLBasic protocols.
 * <p>
 * Since XHTMLBasic has no "stylistic" attributes on table ala HTML3.2/4/0, all
 * we check for is that there is a single column. If so, we consider it has no
 * visual impact and allow it to be removed.
 */
public class XHTMLBasicRemoveTableRule implements RemoveTableRule {

    // Javadoc inherited
    public boolean canRemoveTable(TransTable transTable) {

        // todo: presumably should also check for class and id attributes here?
        // otherwise we will throw able tables which have styles associated

        return transTable.getCols()==1;
    }
}
