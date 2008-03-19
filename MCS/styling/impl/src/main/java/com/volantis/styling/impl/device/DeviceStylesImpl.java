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

package com.volantis.styling.impl.device;

import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.SparsePropertyValueArray;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;

/**
 * The device specific styles implementation.
 */
public class DeviceStylesImpl
        implements DeviceStyles {

    /**
     * The style entity with which this is associated its container.
     *
     * <p>This is only set if this is contained within another styles
     * object.</p>
     */
    private final PseudoStyleEntity pseudoStyleEntity;

    /**
     * The values.
     */
    private final DeviceValues values;

    /**
     * The array of nested styles.
     */
    private final DeviceStylesImpl[] nestedStyles;

    /**
     * The defaults to use.
     */
    private final Defaults defaults;

    /**
     * Initialise.
     *
     * @param pseudoStyleEntity The pseudo style entity, will be null if this
     *                          is for a concrete element.
     * @param values            The values.
     * @param nestedStyles      The array of nested styles.
     * @param defaults          The defaults to use if no value could be found.
     */
    public DeviceStylesImpl(PseudoStyleEntity pseudoStyleEntity, DeviceValues values,
                            DeviceStylesImpl[] nestedStyles, Defaults defaults) {
        this.pseudoStyleEntity = pseudoStyleEntity;
        this.values = values;
        this.nestedStyles = nestedStyles;
        this.defaults = defaults;
    }

    // Javadoc inherited.
    public DeviceValues getValues() {
        return values;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        appendStandardCSS(buffer, "");
        return buffer.toString();
    }

    // Javadoc inherited.
    public DeviceStyles getNestedStyles(PseudoStyleEntity entity) {
        if (nestedStyles != null) {
            for (int i = 0; i < nestedStyles.length; i++) {
                DeviceStylesImpl styles = nestedStyles[i];
                if (styles.pseudoStyleEntity.equals(entity)) {
                    return styles;
                }
            }
        }

        // Could not find a matching nested styles so return the default.
        return defaults.getDefaultStyles();
    }

    // Javadoc inherited.
    public DeviceStyles getMatchingStyles(
            StatefulPseudoClassSet pseudoClasses) {

        if (nestedStyles != null) {
            PropertyValueArray array = null;
            for (int i = 0; i < nestedStyles.length; i++) {
                DeviceStylesImpl deviceStyles =  nestedStyles[i];
                PseudoStyleEntity entity = deviceStyles.pseudoStyleEntity;
                if (entity instanceof StatefulPseudoClassSet) {
                    StatefulPseudoClassSet other =
                            (StatefulPseudoClassSet) entity;
                    if (other.isSubSetOf(pseudoClasses)) {
                        if (array == null) {
                            array = new SparsePropertyValueArray();
                        }
                        DeviceValuesImpl values = (DeviceValuesImpl)
                                deviceStyles.values;
                        values.array.override(array);
                    }
                }
            }

            if (array != null) {
                DeviceValuesImpl values = new DeviceValuesImpl(array,
                        defaults.getDefaultValue());

                return new DeviceStylesImpl(pseudoClasses,
                        values, null, defaults);
            }
        }

        // Could not find a matching nested styles so return the default.
        return defaults.getDefaultStyles();
    }

    /**
     * Append the standard CSS to the buffer.
     *
     * @param buffer     The buffer to update.
     * @param pseudoPath The CSS representation of the path to these styles.
     */
    private void appendStandardCSS(StringBuffer buffer, String pseudoPath) {
        if (values != null) {
            if (pseudoStyleEntity != null) {
                buffer.append("\n")
                        .append(pseudoPath)
                        .append(pseudoStyleEntity.getCSSRepresentation())
                        .append(" {");
            } else if (nestedStyles != null) {
                buffer.append("{");
            }

            values.appendStandardCSS(buffer);

            if (pseudoStyleEntity != null) {
                buffer.append("}");
            } else if (nestedStyles != null) {
                buffer.append("}");
            }
        }

        if (nestedStyles != null) {
            if (pseudoStyleEntity != null) {
                pseudoPath += pseudoStyleEntity.getCSSRepresentation();
            }
            for (int i = 0; i < nestedStyles.length; i++) {
                DeviceStylesImpl styles = nestedStyles[i];

                styles.appendStandardCSS(buffer, pseudoPath);
            }
        }
    }
}
