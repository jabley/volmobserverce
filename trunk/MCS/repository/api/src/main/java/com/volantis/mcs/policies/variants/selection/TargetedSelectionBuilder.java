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

import java.util.List;

/**
 * Builder of {@link TargetedSelection} instances.
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
 * <td>the modifiable list of {@link DeviceReference} that is used to build
 * the unmodifiable <a href="TargetedSelection.html#deviceReferences">device
 * references</a>.</td>
 * </tr>
 *
 * <tr id="categoryReferences">
 * <td align="right" valign="top" width="1%"><b>category&nbsp;references</b></td>
 * <td>the modifiable list of {@link CategoryReference} that is used to build
 * the unmodifiable
 * <a href="TargetedSelection.html#categoryReferences">category references</a>.
 * </td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public interface TargetedSelectionBuilder
        extends SelectionBuilder {

    /**
     * Get the built {@link TargetedSelection}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link TargetedSelection}.
     */
    TargetedSelection getTargetedSelection();

    /**
     * Add a {@link DeviceReference} containing the specified name to the
     * <a href="#deviceReferences">device references</a> list.
     *
     * @param deviceName The name of the device.
     */
    void addDevice(String deviceName);

    /**
     * Getter for the <a href="#deviceReferences">device references</a>
     * property.
     *
     * @return Value of the <a href="#deviceReferences">device references</a>
     *         property.
     */
    List getModifiableDeviceReferences();

    /**
     * Add a {@link CategoryReference} containing the specified name to the
     * <a href="#categoryReferences">category references</a> list.
     *
     * @param categoryName The name of the category.
     */
    void addCategory(String categoryName);

    /**
     * Getter for the <a href="#categoryReferences">category references</a>
     * property.
     *
     * @return Value of the
     *         <a href="#categoryReferences">category references</a>
     *         property.
     */
    List getModifiableCategoryReferences();
}
