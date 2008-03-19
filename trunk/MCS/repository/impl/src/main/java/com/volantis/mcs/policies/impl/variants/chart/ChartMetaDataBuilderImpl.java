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

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.InternalBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.impl.validation.ValidationHelper;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.AxisMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaData;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;

public class ChartMetaDataBuilderImpl
        extends AbstractMetaDataBuilder
        implements ChartMetaDataBuilder {

    private ChartMetaData chartMetaData;
    private ChartType chartType;
    private int heightHint;
    private int widthHint;
    private AxisMetaDataBuilder xAxisBuilder;
    private AxisMetaDataBuilder yAxisBuilder;

    public ChartMetaDataBuilderImpl(ChartMetaData chartMetaData) {
        if (chartMetaData != null) {
            this.chartMetaData = chartMetaData;
            chartType = chartMetaData.getChartType();
            heightHint = chartMetaData.getHeightHint();
            widthHint = chartMetaData.getWidthHint();
            AxisMetaData xAxis = chartMetaData.getXAxis();
            if (xAxis != null) {
                this.xAxisBuilder = xAxis.getAxisMetaDataBuilder();
                changedNestedBuilder(null, (InternalBuilder) xAxisBuilder);
            }
            AxisMetaData yAxis = chartMetaData.getYAxis();
            if (yAxis != null) {
                this.yAxisBuilder = yAxis.getAxisMetaDataBuilder();
                changedNestedBuilder(null, (AbstractBuilder) yAxisBuilder);
            }
        }
    }

    public ChartMetaDataBuilderImpl() {
        this(null);
    }

    protected void clearBuiltObject() {
        chartMetaData = null;
    }

    public MetaData getMetaData() {
        return getChartMetaData();
    }

    public ChartMetaData getChartMetaData() {
        if (chartMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            chartMetaData = new ChartMetaDataImpl(this);
        }

        return chartMetaData;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        if (!equals(this.chartType, chartType)) {
            stateChanged();
        }

        this.chartType = chartType;
    }

    public int getHeightHint() {
        return heightHint;
    }

    public void setHeightHint(int heightHint) {
        if (!equals(this.heightHint, heightHint)) {
            stateChanged();
        }

        this.heightHint = heightHint;
    }

    public int getWidthHint() {
        return widthHint;
    }

    public void setWidthHint(int widthHint) {
        if (!equals(this.widthHint, widthHint)) {
            stateChanged();
        }

        this.widthHint = widthHint;
    }

    public void validate(ValidationContext context) {
        Step step;

        // widthHint
        step = context.pushPropertyStep(PolicyModel.WIDTH_HINT);
        ValidationHelper.checkPercentage(context, sourceLocation, widthHint,
                PolicyMessages.WIDTH_HINT_RANGE);
        context.popStep(step);

        // heightHint
        step = context.pushPropertyStep(PolicyModel.HEIGHT_HINT);
        ValidationHelper.checkPercentage(context, sourceLocation, heightHint,
                PolicyMessages.HEIGHT_HINT_RANGE);
        context.popStep(step);

        // chartType
        step = context.pushPropertyStep(PolicyModel.CHART_TYPE);
        if (chartType == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(
                            PolicyMessages.CHART_TYPE_UNSPECIFIED));
        }
        context.popStep(step);

        // todo handle steps properly.
        if (xAxisBuilder == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.WARNING,
                    context.createMessage(PolicyMessages.X_AXIS_UNSPECIFIED));
        } else {
            ((Validatable) xAxisBuilder).validate(context);
        }

        if (yAxisBuilder == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.WARNING,
                    context.createMessage(PolicyMessages.Y_AXIS_UNSPECIFIED));
        } else {
            ((Validatable) yAxisBuilder).validate(context);
        }
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.CHART;
    }

    public AxisMetaDataBuilder getXAxisBuilder() {
        return xAxisBuilder;
    }

    public void setXAxisBuilder(AxisMetaDataBuilder xAxisBuilder) {
        if (!equals(this.xAxisBuilder, xAxisBuilder)) {
            changedNestedBuilder((InternalBuilder) this.xAxisBuilder,
                    (InternalBuilder) xAxisBuilder);
        }

        this.xAxisBuilder = xAxisBuilder;
    }

    public boolean jibxHasXAxis() {
        return xAxisBuilder != null;
    }

    public AxisMetaDataBuilder getYAxisBuilder() {
        return yAxisBuilder;
    }

    public void setYAxisBuilder(AxisMetaDataBuilder yAxisBuilder) {
        if (!equals(this.yAxisBuilder, yAxisBuilder)) {
            changedNestedBuilder((InternalBuilder) this.yAxisBuilder,
                    (InternalBuilder) yAxisBuilder);
        }

        this.yAxisBuilder = yAxisBuilder;
    }

    public boolean jibxHasYAxis() {
        return yAxisBuilder != null;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ChartMetaDataBuilderImpl) ?
                equalsSpecific((ChartMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ChartMetaDataBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(chartType, other.chartType) &&
                equals(heightHint, other.heightHint) &&
                equals(widthHint, other.widthHint) &&
                equals(xAxisBuilder, other.xAxisBuilder) &&
                equals(yAxisBuilder, other.yAxisBuilder);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, chartType);
        result = hashCode(result, heightHint);
        result = hashCode(result, widthHint);
        result = hashCode(result, xAxisBuilder);
        result = hashCode(result, yAxisBuilder);
        return result;
    }
}
