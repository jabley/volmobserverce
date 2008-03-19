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

package com.volantis.xml.expression;

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.NodeValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.TimeValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;

import java.util.Calendar;
import java.util.TimeZone;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A class for creating expression specific objects.
 *
 * <p><strong>Note</strong>: it is not possible to pass a null value 
 * to the overloaded {@link #createSequence} functions without an explicit
 * cast. However, it is better to use {@link Sequence.EMPTY} to obtain an
 * empty sequence. The same issue exists for the methods 
 * {@link #createDateTimeValue}, {@link #createDateValue} and 
 * {@link #createTimeValue}.</p>
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public abstract class ExpressionFactory {
    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.xml.expression.impl.jxpath.JXPathExpressionFactory",
                        ExpressionFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static ExpressionFactory getDefaultInstance() {
        return (ExpressionFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    public abstract FunctionTableBuilder createFunctionTableBuilder();

    /**
     * Permits a parser to be created to allow various input sources to be
     * parsed to generate an expression instance that can then be evaluated
     * within a given context.
     *
     * @return an {@link ExpressionParser} that can be used to parse input
     *         sources into {@link Expression} instances
     */
    public abstract ExpressionParser createExpressionParser();

    /**
     * Creates and returns an expression context instance for use in evaluating
     * expressions.
     *
     * @param environmentInteractionTracker the environment interaction tracker
     *                                      to be used and published by the new
     *                                      context
     * @param namespacePrefixTracker        the namespace prefix tracker to be
     *                                      used and published by the new
     *                                      context
     * @return an {@link ExpressionContext} instance
     */
    public abstract ExpressionContext createExpressionContext(
            EnvironmentInteractionTracker environmentInteractionTracker,
            NamespacePrefixTracker namespacePrefixTracker);

    /**
     * Creates and returns an expression context instance for use in evaluating
     * expressions.
     *
     * @param environmentInteractionTracker the environment interaction tracker
     *                                      to be used and published by the new
     *                                      context
     * @param namespacePrefixTracker        the namespace prefix tracker to be
     *                                      used and published by the new
     *                                      context
     * @return an {@link ExpressionContext} instance
     */
//    public abstract ExpressionContext createExpressionContext(
//            ExpressionConfiguration configuration,
//            EnvironmentInteractionTracker environmentInteractionTracker,
//            NamespacePrefixTracker namespacePrefixTracker);

    /**
     * Create a {@link BooleanValue} that wraps the specified Java boolean
     * value.
     *
     * <p>As there are only ever two different values of a boolean this method
     * does not actually have to create a new object every time, it could
     * return a reference to the same object for each of the possible boolean
     * values.</p>
     *
     * @param value The Java boolean value.
     * @return A {@link BooleanValue} that wraps the specified Java boolean.
     */
    public abstract BooleanValue createBooleanValue(boolean value);

    /**
     * Create a {@link StringValue} that wraps the specified Java string.
     *
     * @param value The string to wrap,
     * @return A {@link StringValue} that wraps the specified Java string.
     */
    public abstract StringValue createStringValue(String value);

    /**
     * Create a {@link DoubleValue} that wraps the specified Java double.
     *
     * @param value The double to wrap,
     * @return A {@link DoubleValue} that wraps the specified Java double.
     */
    public abstract DoubleValue createDoubleValue(double value);

    /**
     * Create a {@link IntValue} that wraps the specified Java int.
     *
     * @param value The int to wrap,
     * @return A {@link IntValue} that wraps the specified Java int.
     */
    public abstract IntValue createIntValue(int value);

    /**
     * Create a {@link DateTimeValue} that wraps the specified string
     * representation of the date and time
     *
     * @param dateTime the String to wrap
     * @return A {@link DateTimeValue} that wraps the specified string.
     */
    public abstract DateTimeValue createDateTimeValue(String dateTime);

    /**
     * Create a {@link DateTimeValue} that wraps the specified calendar
     * representation of the date and time
     *
     * @param calendar the calendar to base this dateTime on
     * @return A {@link DateTimeValue} that wraps the specified calendar.
     */
    public abstract DateTimeValue createDateTimeValue(Calendar calendar);

    /**
     * Create a {@link DateTimeValue} that wraps the specified years, months,
     * days, hours, minutes, seconds, milliseconds and timezone. Timezone may
     * be null
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     * @param timezone the timezone. May be null
     * @return A {@link DateTimeValue} created from the specified parameters
     */
    public abstract DateTimeValue createDateTimeValue(
        int year, int month, int day,
        int hour, int minute,
        int second, int millisecond,
        TimeZone timezone);

    /**
     * Create a {@link DurationValue} that wraps the specified string
     * representation of the date and time
     *
     * @param duration the String to wrap
     * @return A {@link DurationValue} that wraps the specified string.
     */
    public abstract DurationValue createDurationValue(String duration);

    /**
     * Create a {@link DurationValue} that wraps the specified milliseconds
     * representation of the date and time
     *
     * @param durationInMillis the duration specified as milliseconds
     * @return A {@link DurationValue} that wraps the specified duration.
     */
    public abstract DurationValue createDurationValue(long durationInMillis);

    /**
     * Create a {@link DurationValue} that wraps the specified years, months,
     * days, hours
     *
     * @param positive true if the duration represents a positive difference
     * between dateTimes
     * @param years the absolute number of years in the duration(unbounded)
     * @param months the absolute number of years in the duration (unbounded)
     * @param days the absolute number of days in the duration (unbounded)
     * @param hours the absolute number of hours in the duration (unbounded)
     * @param minutes the absolute number of minutes in the duration
     * (unbounded)
     * @param seconds the absolute number of seconds in the duration
     * (unbounded)
     * @param milliseconds the absolute number of milliseconds in the duration
     * (unbounded)
     * @return a Duration value representing the specified time fragments
     */
    public abstract DurationValue createDurationValue(
        boolean positive, int years,
        int months, int days, int hours,
        int minutes, int seconds,
        int milliseconds);

    /**
     * Create a {@link TimeValue} that wraps the specified string
     * representation of the time
     *
     * @param time the String to wrap
     * @return A {@link TimeValue} that wraps the specified string.
     */
    public abstract TimeValue createTimeValue(String time);
    
    /**
     * Create a {@link TimeValue} that wraps the specified {@link Calendar}
     * representation of the time.
     *
     * @param time the {@link Calendar} to wrap
     * @return A {@link TimeValue} that wraps the specified calendar.
     */
    public abstract TimeValue createTimeValue(Calendar calendar);

    /**
     * Create a {@link TimeValue} based on the specified hours, minutes,
     * seconds and milliseconds.
     *
     * @param hours the hour
     * @param minutes the minutes
     * @param seconds the seconds
     * @param millis the milliseconds
     * @return A {@link TimeValue} created from the specified arguments
     */
    public abstract TimeValue createTimeValue(int hours, int minutes,
                                              int seconds, int millis);

    /**
     * Create a {@link DateValue} that wraps the specified string
     * representation of the date
     *
     * @param date the String to wrap
     * @return A {@link DateValue} that wraps the specified string.
     */
    public abstract DateValue createDateValue(String date);

    /**
     * Create a {@link DateValue} that wraps the specified {@link Calendar}
     * representation of the date.
     *
     * @param calendar the {@link Calendar} to wrap
     * @return A {@link DateValue} that wraps the specified calendar.
     */
    public abstract DateValue createDateValue(Calendar calendar);

    /**
     * Create a {@link Sequence} that contains the items in the specified
     * array.
     *
     * <p>The returned object does not have a reference to the array so
     * modifications to the array after invoking this method do not affect the
     * contents of the sequence.</p>
     *
     * @param items The array of items that belong in the sequence.
     * @return A {@link Sequence} that contains the specified items.
     */
    public abstract Sequence createSequence(Item[] items);

    /**
     * Create an {@link ExpressionScope} based on the given enclosing scope.
     *
     * @param enclosingScope the enclosing scope. May be null if the new scope
     *                       is to be top-level
     * @return An {@link ExpressionScope} based on the given enclosing scope.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public abstract ExpressionScope createExpressionScope(
            ExpressionScope enclosingScope);

    /**
     * Create a {@link Variable} with the given name and value.
     *
     * @param name  The name of the variable.
     * @param value The value of the variable.
     * @return A {@link Variable} with the given name and value.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public abstract Variable createVariable(ExpandedName name,
                                            Value value);
    
    /**
     * Create a {@link NodeValue} that wraps the specified W3C
     * Node.
     *
     * @param node the Node to wrap
     * @return A {@link NodeValue} that wraps the specified Node.
     */
    public abstract NodeValue createNodeValue(final Node node);

    /**
     * Create a {@link Sequence} that contains the Nodes in the specified
     * NodeList.
     *
     * <p>The returned object does not have a reference to the NodeList so
     * modifications to the NodeList after invoking this method do not affect
     * the contents of the sequence.</p>
     *
     * @param nodeList the list of nodes from which the sequence is to be
     *                 created.
     * @return A {@link Sequence} that contains the Nodes from the NodeList as
     *         {@link NodeValue} instances.
     */
    public abstract Sequence createSequence(NodeList nodeList);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
