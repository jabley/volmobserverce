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

package com.volantis.mcs.policies.impl.variants.chart;

import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaData;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.MetaDataVisitor;

public class ChartMetaDataImpl
        extends AbstractMetaData
        implements ChartMetaData {

    private final ChartType chartType;
    private final int heightHint;
    private final int widthHint;
    private final AxisMetaData xAxis;
    private final AxisMetaData yAxis;

    public ChartMetaDataImpl(ChartMetaDataBuilder builder) {
        chartType = builder.getChartType();
        heightHint = builder.getHeightHint();
        widthHint = builder.getWidthHint();

        AxisMetaDataBuilder axisBuilder;
        if ((axisBuilder = builder.getXAxisBuilder()) == null) {
            xAxis = null;
        } else {
            xAxis = axisBuilder.getAxisMetaData();
        }
        if ((axisBuilder = builder.getYAxisBuilder()) == null) {
            yAxis = null;
        } else {
            yAxis = axisBuilder.getAxisMetaData();
        }
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return getChartMetaDataBuilder();
    }

    public ChartMetaDataBuilder getChartMetaDataBuilder() {
        return new ChartMetaDataBuilderImpl(this);
    }

    public void accept(MetaDataVisitor visitor) {
        visitor.visit(this);
    }

    public ChartType getChartType() {
        return chartType;
    }

    public int getHeightHint() {
        return heightHint;
    }

    public int getWidthHint() {
        return widthHint;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.CHART;
    }

    public AxisMetaData getXAxis() {
        return xAxis;
    }

    public AxisMetaData getYAxis() {
        return yAxis;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ChartMetaDataImpl) ?
                equalsSpecific((ChartMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ChartMetaDataImpl other) {
        return super.equalsSpecific(other) &&
                equals(chartType, other.chartType) &&
                equals(heightHint, other.heightHint) &&
                equals(widthHint, other.widthHint) &&
                equals(xAxis, other.xAxis) &&
                equals(yAxis, other.yAxis);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, chartType);
        result = hashCode(result, heightHint);
        result = hashCode(result, widthHint);
        result = hashCode(result, xAxis);
        result = hashCode(result, yAxis);
        return result;
    }
}
