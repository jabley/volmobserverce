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

package com.volantis.mcs.policies.variants.chart;

import com.volantis.mcs.policies.PolicyFactory;

/**
 * Builder of {@link AxisMetaData} instances.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see AxisMetaData
 * @see ChartMetaDataBuilder
 * @see PolicyFactory#createAxisBuilder()
 * @since 3.5.1
 */
public interface AxisMetaDataBuilder {

    /**
     * Get the built {@link AxisMetaData}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link AxisMetaData}.
     */
    AxisMetaData getAxisMetaData();

    /**
     * Setter for the <a href="AxisMetaData.html#interval">interval</a> property.
     *
     * @param interval New value of the
     *                 <a href="AxisMetaData.html#interval">interval</a> property.
     */
    void setInterval(int interval);

    /**
     * Getter for the <a href="AxisMetaData.html#interval">interval</a> property.
     *
     * @return Value of the <a href="AxisMetaData.html#interval">interval</a>
     *         property.
     */
    int getInterval();

    /**
     * Setter for the <a href="AxisMetaData.html#title">title</a> property.
     *
     * @param title New value of the
     *              <a href="AxisMetaData.html#title">title</a> property.
     */
    void setTitle(String title);

    /**
     * Getter for the <a href="AxisMetaData.html#title">title</a> property.
     *
     * @return Value of the <a href="AxisMetaData.html#title">title</a>
     *         property.
     */
    String getTitle();
}
