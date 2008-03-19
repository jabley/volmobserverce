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

package com.volantis.cache.group;



/**
 * Builds instances of {@link Group}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="maxCount">
 * <td align="right" valign="top" width="1%"><b>max count</b></td>
 * <td>The maximum number of entries allowed in the cache. This must be greater
 * than 0.</td>
 * </tr>
 *
 * </table>
 *
 * @mock.generate
 */
public interface GroupBuilder {

    /**
     * Getter for the <a href="#maxCount">max count</a> property.
     *
     * @return Value of the <a href="CacheBuilder.html#maxCount">max count</a>
     *         property.
     */
    int getMaxCount();

    /**
     * Setter for the <a href="#maxCount">max count</a> property.
     *
     * @param maxCount New value of the
     *                 <a href="CacheBuilder.html#maxCount">max count</a>
     *                 property.
     */
    void setMaxCount(int maxCount);
}
