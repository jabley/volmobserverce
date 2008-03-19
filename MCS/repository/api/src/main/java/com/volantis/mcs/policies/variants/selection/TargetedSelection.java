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
package com.volantis.mcs.policies.variants.selection;

import com.volantis.mcs.policies.variants.Variant;

import java.util.List;

/**
 * Selection criteria that targets a {@link Variant} at specific devices, or
 * categories.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="deviceReferences">
 * <td align="right" valign="top" width="1%"><b>device&nbsp;references</b></td>
 * <td>the unmodifiable list of {@link DeviceReference} instances.</td>
 * </tr>
 *
 * <tr id="categoryReferences">
 * <td align="right" valign="top" width="1%"><b>category&nbsp;references</b></td>
 * <td>the unmodifiable list of {@link CategoryReference} instances.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see TargetedSelectionBuilder
 * @since 3.5.1
 */
public interface TargetedSelection
        extends Selection {

    /**
     * Get a new builder instance for {@link TargetedSelection}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link TargetedSelectionBuilder#getTargetedSelection()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    TargetedSelectionBuilder getTargetedSelectionBuilder();

    /**
     * Getter for the <a href="#deviceReferences">device references</a> property.
     * @return Value of the <a href="#deviceReferences">device references</a>
     * property.
     */
    List getDeviceReferences();

    /**
     * Getter for the <a href="#categoryReferences">category references</a> property.
     * @return Value of the <a href="#categoryReferences">category references</a>
     * property.
     */
    List getCategoryReferences();
}
