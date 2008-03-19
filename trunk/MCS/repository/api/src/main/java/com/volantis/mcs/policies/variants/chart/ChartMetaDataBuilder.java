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
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;

/**
 * Builder of {@link ChartMetaData} instances.
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
 * @see ChartMetaData
 * @see AxisMetaDataBuilder
 * @see PolicyFactory#createChartMetaDataBuilder()
 * @since 3.5.1
 */
public interface ChartMetaDataBuilder
        extends MetaDataBuilder {

    /**
     * Get the built {@link ChartMetaData}.
     *
     * <p>Returns a newly created instance the first time it is called and
     * if the state has changed since the last time this method was called,
     * otherwise it returns the same instance as the previous call.</p>
     *
     * @return The built {@link ChartMetaData}.
     */
    ChartMetaData getChartMetaData();

    /**
     * Setter for the <a href="ChartMetaData.html#chartType">chart type</a>
     * property.
     *
     * @param chartType New value of the
     *                  <a href="ChartMetaData.html#chartType">chart type</a>
     *                  property.
     */
    void setChartType(ChartType chartType);

    /**
     * Getter for the <a href="ChartMetaData.html#chartType">chart type</a>
     * property.
     *
     * @return Value of the
     *         <a href="ChartMetaData.html#chartType">chart type</a> property.
     */
    ChartType getChartType();

    /**
     * Setter for the <a href="ChartMetaData.html#widthHint">width hint</a>
     * property.
     *
     * @param widthHint New value of the
     *                  <a href="ChartMetaData.html#widthHint">width hint</a>
     *                  property.
     */
    void setWidthHint(int widthHint);

    /**
     * Getter for the <a href="ChartMetaData.html#widthHint">width hint</a>
     * property.
     *
     * @return Value of the
     *         <a href="ChartMetaData.html#widthHint">width hint</a> property.
     */
    int getWidthHint();

    /**
     * Setter for the <a href="ChartMetaData.html#heightHint">height hint</a>
     * property.
     *
     * @param heightHint New value of the
     *                   <a href="ChartMetaData.html#heightHint">height hint</a>
     *                   property.
     */
    void setHeightHint(int heightHint);

    /**
     * Getter for the <a href="ChartMetaData.html#heightHint">height hint</a>
     * property.
     *
     * @return Value of the
     *         <a href="ChartMetaData.html#heightHint">height hint</a>
     *         property.
     */
    int getHeightHint();

    /**
     * Getter for the builder of the
     * <a href="ChartMetaData.html#xAxis">x axis</a> property.
     *
     * @return Builder of the <a href="ChartMetaData.html#xAxis">x axis</a>
     *         property.
     */
    AxisMetaDataBuilder getXAxisBuilder();

    /**
     * Setter for the builder of the
     * <a href="ChartMetaData.html#xAxis">x axis</a> property.
     *
     * @param xAxis New builder of the
     *              <a href="ChartMetaData.html#xAxis">x axis</a> property.
     */
    void setXAxisBuilder(AxisMetaDataBuilder xAxis);

    /**
     * Getter for the builder of the
     * <a href="ChartMetaData.html#yAxis">y axis</a> property.
     *
     * @return Builder of the <a href="ChartMetaData.html#yAxis">y axis</a>
     *         property.
     */
    AxisMetaDataBuilder getYAxisBuilder();

    /**
     * Setter for the builder of the
     * <a href="ChartMetaData.html#yAxis">y axis</a> property.
     *
     * @param yAxis New builder of the
     *              <a href="ChartMetaData.html#yAxis">y axis</a> property.
     */
    void setYAxisBuilder(AxisMetaDataBuilder yAxis);
}
