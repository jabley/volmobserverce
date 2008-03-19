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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting;

/**
 * This interface is used to obtain appropriate implementations of {@link Report}
 * extension interfaces at runtime. This is the service provided by OSGi. For
 * pre OSGi code use the {@link ReportingTransactionFactory}.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 */
public interface ReportService {

    /**
     * Return an instance of the specified Report interface. The class provided
     * must be an interface that extends Report, adding appropriately named
     * metric modifier methods.
     *
     * @param clazz the class representing the Report interface. Must not be
     *              null
     * @return an instance of a class implementing the provided interface
     *
     * @throws IllegalArgumentException if the specified class isn't an
     *                                  interface extending (directly or
     *                                  indirectly) the Report interface
     */
    public Report createReport(Class clazz);

    /**
     * Return an instance of the specified Report interface. The class provided
     * must be an interface that extends Report, adding appropriately named
     * metric modifier methods.
     *
     * @param clazz         the class representing the Report interface. Must
     *                      not be null
     * @param transactionID an ID to bind to this transaction
     * @return an instance of a class implementing the provided interface
     *
     * @throws IllegalArgumentException if the specified class isn't an
     *                                  interface extending (directly or
     *                                  indirectly) the Report interface
     */
    public Report createReport(Class clazz, String transactionID);

    /**
     * Return an instance of the DynamicReport Report interface. The binding
     * provided is a key to get report handler.
     *
     * @param binding binding key for report handler
     * @return an instance of a class implementing the DynamicReport interface
     *
     * @throws IllegalArgumentException if the specified class isn't an
     *                                  interface extending (directly or
     *                                  indirectly) the Report interface
     */
    public DynamicReport createDynamicReport(String binding);

    /**
     * Return an instance of the DynamicReport Report interface. The binding
     * provided is a key to get report handler.
     *
     * @param binding       binding key for report handler
     * @param transactionID an ID to bind to this transaction
     * @return an instance of a class implementing the DynamicReport interface
     *
     * @throws IllegalArgumentException if the specified class isn't an
     *                                  interface extending (directly or
     *                                  indirectly) the Report interface
     */
    public DynamicReport createDynamicReport(String binding,
                                             String transactionID);

}
