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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message.store;

/**
 * Represents an exception that occurred when working with the MessageStore
 * in MPS.  It is a specific exception class that allows the various HTTP
 * repsonse codes to be wrapped so that they can be propogated out of methods
 * that otherwise return values.  It also permits a generic error, and allows
 * user-defined creations with its various constructors.
 */
public class MessageStoreException extends Exception {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";


    /**
     * The error code that this instance of a MessageStoreException is holding.
     * This indicates the error that occurred and can be compared against the
     * various static instances provided in the {@link MessageStoreMessageEnumeration}
     * class.
     */
    protected MessageStoreMessageEnumeration errorCode;

    /**
     * Create a default <code>MessageStoreException</code> that represents
     * an unknown exception.
     */
    public MessageStoreException() {
        // Default to an unknown exception
        errorCode = MessageStoreMessageEnumeration.UNKNOWN_EXCEPTION;
    }

   /**
    * Create a <code>MessageStoreException</code> that represents the error
    * code specified and also contains an descriptive string about the
    * exception.
    *
    * @param msg         A description of the exception problem
    * @param errorCode The error that this exception instance is representing.
    */
   public MessageStoreException(String msg,
                                MessageStoreMessageEnumeration errorCode) {
       super(msg);
       this.errorCode = errorCode;
   }

    /**
     * Retrieve the error code contained in this exception.  This can be used
     * to compare to values defined in other HTTP handling code such as the
     * <code>java.net.*</code>, <code>javax.servlet.*</code>, and
     * <code>org.apache.commons.HttpClient.*</code> packages.
     *
     * @return The error code in the exception.
     */
    public int getErrorCode() {
        return errorCode.getValue();
    }

    // JavaDoc inherited
    public boolean equals(Object o) {
        boolean isEqual = false;

        if (this == o) {
            // The same object - it has to be equal :-)
            isEqual = true;
        } else {
            // Check to see if the class not null and is of the correct type
            if (o != null && o.getClass().equals(MessageStoreException.class)) {
                // It is so try the superclass...
                isEqual = super.equals(o);
                if (isEqual) {
                    // ...and if that's equal, check the error code for equality
                    MessageStoreException mse = (MessageStoreException) o;
                    if (errorCode.equals(mse.errorCode)) {
                        isEqual = true;
                    }
                }
            }
        }

        return isEqual;
    }

    // JavaDoc inherited
    public int hashCode() {
        return errorCode.hashCode();
    }

    // JavaDoc inherited
    public String toString() {
        return super.toString() +
                "\nMessageStoreException errorCode = " + errorCode.getValue();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	818/3	pcameron	VBM:2005062305 Fixed some message localisations for exception throwing

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
