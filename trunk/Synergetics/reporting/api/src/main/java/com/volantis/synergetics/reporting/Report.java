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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting;

/**
 * A given report is composed of two mandatory events: {@link #start} and
 * {@link #stop}. Optional {@link #update} events can be generated at any time
 * between start and stop.
 *
 * <p>This base interface supports this reporting lifecycle but doesn't include
 * any "metrics" with the reported events (other than time).</p>
 *
 * <p>A "metric" is essentially a parameter to be included in the event output.
 * There are various forms of "metric" that have specific meaning, as
 * follows:</p>
 *
 * <table border="1"> <tr> <th>Form</th> <th>Meaning</th> </tr> <tr>
 * <td>Counter</td> <td>An integer or floating point numeric value that is a
 * monotonically incremented non-negative value, up to its maximum possible
 * value at which point it wraps round to zero and starts again. This is used
 * for such things are bytes processed etc. where it makes sense to accumulate
 * (or sum up) the values over the report's lifecycle. The counter is assumed
 * to be incremented during the lifecycle of the report by the application. The
 * reported metric could be statistically manipulated by the reporting
 * back-end.</td> </tr> <tr> <td>Gauge</td> <td>An integer or floating point
 * (positive or negative) numeric value. This is used for such things as memory
 * used where it doesn't make sense to accumulate the values. The reported
 * metric could still be statistically manipulated by the reporting
 * back-end.</td> </tr> <tr> <td>Numeric ID</td> <td>An integer numeric value
 * used as an ID, e.g. product code. It makes no sense to perform arithmetic
 * operations on this type of value.</td> </tr> <tr> <td>String</td> <td>A
 * value for things such as part numbers or names or messages.</td> </tr>
 * </table>
 *
 * In order to include metrics into a report, a custom interface must be
 * derived (directly or via another custom interface [hierarchy]) from this
 * interface that includes specially named modifier ("set") methods. The method
 * naming convention is:
 *
 * <table border="1"> <tr> <th>Name</th> <th>Arg</th> <th>Description</th>
 * </tr> <tr> <td>set''Name''Counter</td> <td>int<br/>long<br/>float</td>
 * <td>Sets the ''Name''d counter's value using the monotonically increasing
 * value given.</td> </tr> <tr> <td>set''Name''Gauge</td>
 * <td>int<br/>long<br/>float</td> <td>Sets the ''Name''d gauge's value.</td>
 * </tr> <tr> <td>set''Name''Id</td> <td>int<br/>long</td> <td>Sets the
 * ''Name''d ID's value.</td> </tr> <tr> <td>set''Name''String</td>
 * <td>String</td> <td>Sets the ''Name''d string's value. This value may be
 * null.</td> </tr> </table>
 *
 * In each case, the name is an arbitrary (usually camel case with initial cap)
 * string.
 *
 * <p>Methods not conforming to this convention will cause a runtime exception
 * to be thrown if invoked.</p>
 *
 * <p>Metrics may be set to initial values at any time prior to invocation of
 * the {@link #start} method.</p>
 *
 * <p><strong>Note</strong>: this class must never be implemented in user code.
 * Custom reports must be declared as interfaces that extend this interface
 * (directly or indirectly). The reporting runtime will automatically provide
 * appropriate implementations of all of these report interfaces.</p>
 * 
 * @volantis-api-include-in InternalAPI
 * @volantis-api-exclude-from PublicAPI
 */
public interface Report {

    /**
     * Records the start of the report, generating a start event from any
     * defined metrics.
     * 
     * @volantis-api-include-in PublicAPI
     */
    public void start();

    /**
     * Generates an update event from any defined metrics.
     *
     * @param message a description of the update.
     * 
     * @volantis-api-include-in PublicAPI
     */
    public void update(String message);

    /**
     * Records the end of the report, generating a stop event from any defined
     * metrics and the given parameters.
     *
     * @param status  the termination status for the report. Must not be null
     * @param message arbitrary text to associated with the stop event. May be
     *                null
     *                
     * @volantis-api-include-in PublicAPI
     */
    public void stop(Status status, String message);

    /**
     * Return the transaction ID associated with this reporting object
     *
     * @return the transaction ID associated with this reporting object
     * 
     * @volantis-api-include-in PublicAPI
     */
    public String getTransactionID();
}
