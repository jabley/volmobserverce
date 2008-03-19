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

package com.volantis.styling.device;

import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClassSet;

/**
 * The set of styles that would be used by the target device.
 */
public interface DeviceStyles {

    /**
     * The values for the associated styleable entity.
     *
     * <p>This method never returns null, if the device does not have any
     * explicit values set for this styles then a set of default values will
     * be returned.</p>
     *
     * @return The values for the associated styleable entity.
     */
    DeviceValues getValues();

    /**
     * Get the standard CSS representation of these styles, uses the syntax for
     * the 'style' attribute.
     *
     * @return The standard CSS representation.
     */
    String getStandardCSS();

    /**
     * Find the nested styles associated with the specified entity.
     *
     * <p>This method never returns null, if the device does not have any
     * explicit styles set for the entity then a set of default styles will
     * be returned.</p>
     *
     * @param entity The entity whose associated style is to be found.
     * @return The nested styles.
     */
    DeviceStyles getNestedStyles(PseudoStyleEntity entity);

    /**
     * Get a set of styles that contains a composite of all the values from
     * the nested styles that match the supplied pseudo classes.
     *
     * <p>A nested styles matches if it is associated with a set of pseudo
     * classes and that set is a subset of the ones supplied. e.g. if the
     * supplied set is :link:hover then a pseudo class set of :link, or :hover,
     * or :link:hover will match but :visited will not. Similarly if the
     * supplied set is :link then only :link will match, :link:hover will
     * not.</p>
     *
     * <p>The purpose of this matching criteria is to ensure that if the device
     * has a rule for one pseudo class then any combination of other pseudo
     * classes that match that may also be affected.</p>
     *
     * <p>The composite values are created by adding the values from each
     * matching styles to the array in order from low to high
     * specificity.</p>
     *
     * @param pseudoClasses The pseudo classes to match.
     * @return The composite styles.
     */
    DeviceStyles getMatchingStyles(StatefulPseudoClassSet pseudoClasses);
}
