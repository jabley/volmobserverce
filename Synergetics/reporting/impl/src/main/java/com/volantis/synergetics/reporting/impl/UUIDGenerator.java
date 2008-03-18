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
package com.volantis.synergetics.reporting.impl;


/**
 * Return an ID as a String that is less then 32 characters long. This is
 * a very poor UUID generator. It needs replacing (and 32 characters is not a
 * hard limit)
 */
public class UUIDGenerator implements TransactionIDGenerator {

    private static long ID = System.currentTimeMillis() * 100000;

    private static final Object lock = new Object();

    public String getTransactionID() {
        String result = null;
        synchronized (lock) {
            result = Long.toString(ID);
            ID = ID + 13;
        }

        return result;
    }
}
