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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.impl;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionScope;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.Variable;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.SimpleNodeValue;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.SimpleDateTimeValue;
import com.volantis.xml.expression.atomic.temporal.SimpleDateValue;
import com.volantis.xml.expression.atomic.temporal.SimpleDurationValue;
import com.volantis.xml.expression.atomic.temporal.SimpleTimeValue;
import com.volantis.xml.expression.atomic.temporal.TimeValue;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.expression.impl.functions.FunctionTableBuilderImpl;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.SimpleSequence;
import com.volantis.xml.namespace.ExpandedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Partial implementation of the ExpressionFactory interface designed to handle
 * the common implementations of the various value types and other factored
 * classes.
 */
public abstract class SimpleExpressionFactory
        extends ExpressionFactory
        implements Serializable {

    public FunctionTableBuilder createFunctionTableBuilder() {
        return new FunctionTableBuilderImpl();
    }

    // javadoc inherited
    public BooleanValue createBooleanValue(boolean value) {
        if (value) {
            return BooleanValue.TRUE;
        } else {
            return BooleanValue.FALSE;
        }
    }

    // javadoc inherited
    public StringValue createStringValue(String value) {
        return new SimpleStringValue(this, value);
    }

    // javadoc inherited
    public DoubleValue createDoubleValue(double value) {
        return new SimpleDoubleValue(this, value);
    }

    // javadoc inherited
    public IntValue createIntValue(int value) {
        return new SimpleIntValue(this, value);
    }

    // javadoc inherited
    public DateTimeValue createDateTimeValue(String dateTime) {
        return new SimpleDateTimeValue(this, dateTime);
    }

    // javadoc inherited
    public DateTimeValue createDateTimeValue(Calendar calendar) {
        return new SimpleDateTimeValue(this, calendar);
    }

    // javadoc inherited
    public DateTimeValue createDateTimeValue(int year,int month, int day,
                                             int hour, int minute,
                                             int second, int millisecond,
                                             TimeZone timezone) {
        return new SimpleDateTimeValue(this, year, month, day,
                                       hour, minute, second, millisecond,
                                        timezone);
    }

    // javadoc inherited
    public DurationValue createDurationValue(String duration) {
        return new SimpleDurationValue(this, duration);
    }

    // javadoc inherited
    public DurationValue createDurationValue(long durationInMillis) {
        return new SimpleDurationValue(this, durationInMillis);
    }

    // javadoc inherited
    public DurationValue createDurationValue(boolean positive, int years,
                                             int months, int days,
                                             int hours, int minutes,
                                             int seconds, int milliseconds) {
        return new SimpleDurationValue(this, positive, years, months, days,
                                       hours,  minutes, seconds, milliseconds);
    }

    // javadoc inherited
    public DateValue createDateValue(String date) {
        return new SimpleDateValue(this, date);
    }
    
    // javadoc inherited
    public DateValue createDateValue(Calendar calendar) {
        return new SimpleDateValue(this, calendar);
    }

    // javadoc inherited
    public TimeValue createTimeValue(String time) {
        return new SimpleTimeValue(this, time);
    }
    
    // javadoc inherited
    public TimeValue createTimeValue(Calendar calendar) {
        return new SimpleTimeValue(this, calendar);
    }

    // javadoc inherited
    public TimeValue createTimeValue(int hours, int minutes, 
                                     int seconds, int millis) {
        return new SimpleTimeValue(this, hours, minutes, seconds, millis);
    }

    // javadoc inherited
    public Sequence createSequence(Item[] items) {
        if ((items == null) || (items.length == 0)) {
            return Sequence.EMPTY;
        } else {
            return new SimpleSequence(this, items);
        }
    }

    // javadoc inherited
    public ExpressionScope createExpressionScope(
            ExpressionScope enclosingScope) {
        return new SimpleExpressionScope(this, enclosingScope);
    }

    // javadoc inherited
    public Variable createVariable(ExpandedName name,
                                   Value value) {
        return new SimpleVariable(this, name, value);
    }
    
    // javadoc inherited
    public NodeValue createNodeValue(final Node node) {
        return new SimpleNodeValue(this, node);
    }

    // javadoc inherited
    public Sequence createSequence(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return Sequence.EMPTY;
        }
        final int length = nodeList.getLength();
        final Item[] items = new Item[length];
        for (int i = 0; i < length; i++) {
            items[i] = createNodeValue(nodeList.item(i));
        }
        return new SimpleSequence(this, items);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
