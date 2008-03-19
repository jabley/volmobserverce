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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.bms;

/**
 * <p>Interface defining the result from a
 * {@link MessageService#process(SendRequest,Session)} operation.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface Failures {

    /**
     * Return an array of Recipients which the message could not be delivered
     * to. Not null, but may be empty (zero-length). Clients can use the
     * {@link #isEmpty()} utility method to see if there are any failures which
     * they may wish to handle.
     *
     * @return a non-null array of Recipients.
     */
    public Recipient[] getRecipients();

    /**
     * Add a Recipient to the list of ones that failed.
     *
     * @param failure a Recipient to which the message could not be sent.
     */
    public void add(Recipient failure);

    /**
     * Return true if there were no Recipients to which the message could not
     * be sent, otherwise false.
     *
     * @return a boolean.
     */
    public boolean isEmpty();
}
