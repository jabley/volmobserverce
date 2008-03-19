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

import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link DeviceStyles}.
 */
public class DeviceStylesBuilder {

    /**
     * The defaults to use.
     */
    private final Defaults defaults;

    /**
     * The pseudo style entity for which the styles are being built.
     */
    private final PseudoStyleEntity pseudoStyleEntity;

    /**
     * The builder for the {@link DeviceValues}.
     */
    private DeviceValuesBuilder valuesBuilder;

    /**
     * The possibly null list of nested builders.
     */
    private List nestedBuildersList;

    /**
     * Initialise.
     *
     * @param defaults          The defaults.
     * @param pseudoStyleEntity The pseudo style entity.
     */
    private DeviceStylesBuilder(
            Defaults defaults, PseudoStyleEntity pseudoStyleEntity) {
        this.defaults = defaults;
        this.pseudoStyleEntity = pseudoStyleEntity;
    }

    /**
     * Initialise.
     *
     * @param defaults The defaults.
     */
    public DeviceStylesBuilder(Defaults defaults) {
        this(defaults, null);
    }

    /**
     * Get the builder for the nested styles for the specified entity.
     *
     * @param entity The pseudo style entity.
     * @return The builder.
     */
    public DeviceStylesBuilder getNestedBuilder(PseudoStyleEntity entity) {
        DeviceStylesBuilder builder;
        if (nestedBuildersList == null) {
            nestedBuildersList = new ArrayList();
            builder = new DeviceStylesBuilder(defaults, entity);
            nestedBuildersList.add(builder);
        } else {
            builder = findStylesBuilder(entity);
            if (builder == null) {
                builder = new DeviceStylesBuilder(defaults, entity);
                nestedBuildersList.add(builder);
            }
        }

        return builder;
    }

    /**
     * Get the builder for the values.
     *
     * @return The values builder.
     */
    public DeviceValuesBuilder getValuesBuilder() {
        if (valuesBuilder == null) {
            valuesBuilder = new DeviceValuesBuilder(defaults.getDefaultValue());
        }
        return valuesBuilder;
    }

    /**
     * Get the constructed styles.
     *
     * @return The constructed styles.
     */
    public DeviceStyles getStyles() {

        List nestedStylesList = null;
        if (nestedBuildersList != null) {
            for (int i = 0; i < nestedBuildersList.size(); i++) {
                DeviceStylesBuilder builder = (DeviceStylesBuilder)
                        nestedBuildersList.get(i);
                DeviceStyles nested = builder.getStyles();
                if (nested != null) {
                    if (nestedStylesList == null) {
                        nestedStylesList = new ArrayList();
                    }
                    nestedStylesList.add(nested);
                }
            }
        }

        DeviceStyles styles;
        if (valuesBuilder == null && nestedStylesList == null) {
            if (pseudoStyleEntity == null) {
                styles = defaults.getDefaultStyles();
            } else {
                styles = null;
            }
        } else {
            DeviceValues values;
            if (valuesBuilder == null) {
                values = defaults.getDefaultValues();
            } else {
                values = valuesBuilder.getValues();
            }

            DeviceStylesImpl[] nestedStyles;
            if (nestedStylesList == null || nestedStylesList.isEmpty()) {
                nestedStyles = null;
            } else {
                nestedStyles = new DeviceStylesImpl[nestedStylesList.size()];
                nestedStylesList.toArray(nestedStyles);
            }

            styles = new DeviceStylesImpl(pseudoStyleEntity, values,
                    nestedStyles, defaults);
        }

        return styles;
    }

    /**
     * Find the nested styles associated with the specified entity.
     *
     * @param entity The entity whose associated style is to be found.
     * @return The nested styles, or null if they could not be found.
     */
    private DeviceStylesBuilder findStylesBuilder(PseudoStyleEntity entity) {
        for (int i = 0; i < nestedBuildersList.size(); i++) {
            DeviceStylesBuilder styles = (DeviceStylesBuilder)
                    nestedBuildersList.get(i);
            if (styles.pseudoStyleEntity.equals(entity)) {
                return styles;
            }
        }

        return null;
    }
}
