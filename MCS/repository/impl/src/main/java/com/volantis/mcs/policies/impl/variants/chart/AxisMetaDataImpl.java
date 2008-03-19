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

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.chart.AxisMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;

public class AxisMetaDataImpl
        extends EqualsHashCodeBase
        implements AxisMetaData {

    private final int interval;

    private final String title;

    public AxisMetaDataImpl(AxisMetaDataBuilder builder) {
        interval = builder.getInterval();
        title = builder.getTitle();
    }

    public AxisMetaDataBuilder getAxisMetaDataBuilder() {
        return new AxisMetaDataBuilderImpl(this);
    }

    public int getInterval() {
        return interval;
    }

    public String getTitle() {
        return title;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AxisMetaDataImpl) ?
                equalsSpecific((AxisMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AxisMetaDataImpl other) {
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
