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

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.chart.AxisMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;

public class AxisMetaDataBuilderImpl
        extends AbstractBuilder
        implements AxisMetaDataBuilder, Validatable {

    private AxisMetaData axisMetaData;

    private int interval;

    private String title;

    public AxisMetaDataBuilderImpl(AxisMetaData axisMetaData) {
        if (axisMetaData != null) {
            this.axisMetaData = axisMetaData;
            interval = axisMetaData.getInterval();
            title = axisMetaData.getTitle();
        }
    }

    public AxisMetaDataBuilderImpl() {
        this(null);
    }

    public AxisMetaData getAxisMetaData() {
        if (axisMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            axisMetaData = new AxisMetaDataImpl(this);
        }

        return axisMetaData;
    }

    protected Object getBuiltObject() {
        return getAxisMetaData();
    }

    protected void clearBuiltObject() {
        axisMetaData = null;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        if (!equals(this.interval, interval)) {
            stateChanged();
        }

        this.interval = interval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!equals(this.title, title)) {
            stateChanged();
        }

        this.title = title;
    }

    public void validate(ValidationContext context) {

        if (interval < 0) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.AXIS_INTERVAL_RANGE,
                            new Integer(interval)));
        }
        if (title == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.WARNING,
                    context.createMessage(
                            PolicyMessages.AXIS_TITLE_UNSPECIFIED));
        }
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AxisMetaDataBuilderImpl) ?
                equalsSpecific((AxisMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AxisMetaDataBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(interval, other.interval) &&
                equals(title, other.title);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, interval);
        result = hashCode(result, title);
        return result;
    }
}
