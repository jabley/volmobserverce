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

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.metadata.MetaData;

/**
 * The {@link MetaData} for {@link VariantType#CHART} variants.
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
 * <tr id="chartType">
 * <td align="right" valign="top" width="1%"><b>chart&nbsp;type</b></td>
 * <td>the type of chart that will be created.</td>
 * </tr>
 *
 * <tr id="widthHint">
 * <td align="right" valign="top" width="1%"><b>width&nbsp;hint</b></td>
 * <td>the width of the generated chart as a percentage of the target device's
 * width. This must be within the range 0 to 100 inclusive.</td>
 * </tr>
 *
 * <tr id="heightHint">
 * <td align="right" valign="top" width="1%"><b>height&nbsp;hint</b></td>
 * <td>the height of the generated chart as a percentage of the target device's
 * height. This must be within the range 0 to 100 inclusive.</td>
 * </tr>
 *
 * <tr id="xAxis">
 * <td align="right" valign="top" width="1%"><b>x&nbsp;axis</b></td>
 * <td>information about the X axis.</td>
 * </tr>
 *
 * <tr id="yAxis">
 * <td align="right" valign="top" width="1%"><b>y&nbsp;axis</b></td>
 * <td>information about the y axis.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ChartMetaDataBuilder
 * @see AxisMetaData
 * @see ChartType
 * @since 3.5.1
 */
public interface ChartMetaData
        extends MetaData {

    /**
     * Get a new builder instance for {@link ChartMetaData}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link ChartMetaDataBuilder#getChartMetaData()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    ChartMetaDataBuilder getChartMetaDataBuilder();

    /**
     * Getter for the <a href="#chartType">chart type</a> property.
     * @return Value of the <a href="#chartType">chart type</a>
     * property.
     */
    ChartType getChartType();

    /**
     * Getter for the <a href="#widthHint">width hint</a> property.
     * @return Value of the <a href="#widthHint">width hint</a>
     * property.
     */
    int getWidthHint();

    /**
     * Getter for the <a href="#heightHint">height hint</a> property.
     * @return Value of the <a href="#heightHint">height hint</a>
     * property.
     */
    int getHeightHint();

    /**
     * Getter for the <a href="#xAxis">x axis</a> property.
     * @return Value of the <a href="#xAxis">x axis</a>
     * property.
     */
    AxisMetaData getXAxis();

    /**
     * Getter for the <a href="#yAxis">y axis</a> property.
     * @return Value of the <a href="#yAxis">y axis</a>
     * property.
     */
    AxisMetaData getYAxis();
}
