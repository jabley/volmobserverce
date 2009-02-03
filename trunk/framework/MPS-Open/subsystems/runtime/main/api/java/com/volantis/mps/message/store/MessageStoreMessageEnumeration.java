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

import java.util.HashMap;

/**
 * A typesafe enumeration of error codes that can be generated when using
 * the MPS Message Store Servlet (MSS).  The value contained in this
 * enumeration provide all HTTP error codes as these are likely to occur
 * within the MSS environment.  An unknown type is also defined, along with
 * the ability for new entries to be added.
 */
public class MessageStoreMessageEnumeration {
    /**
     * The set of all literals. Keyed on the internal string version of the
     * enumeration, mapping to the MessageStoreMessageEnumeration equivalent.
     *
     * <p>NB: This static member *must* appear before the enumeration literals
     * for this to work. If it does not, the access of this variable within
     * the literal construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".</p>
     *
     * @associates <{MessageStoreMessageEnumeration}>
     * @supplierRole entries
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static final HashMap entries = new HashMap();


    /*
     * These could have been defined to reference the HttpServletResponse
     * codes but that would have created a dependency for this class that
     * is not really necessary.
     */

    /* ======================================================================
     * INFORMATION CODES
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration CONTINUE
            = new MessageStoreMessageEnumeration(100);
    public static final MessageStoreMessageEnumeration SWITCHING_PROTOCOLS
            = new MessageStoreMessageEnumeration(101);

    /* ======================================================================
     * SUCCESS CODES
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration SUCCESS
            = new MessageStoreMessageEnumeration(200);
    public static final MessageStoreMessageEnumeration CREATED
            = new MessageStoreMessageEnumeration(201);
    public static final MessageStoreMessageEnumeration ACCEPTED
            = new MessageStoreMessageEnumeration(202);
    public static final MessageStoreMessageEnumeration NO_CONTENT
            = new MessageStoreMessageEnumeration(204);
    public static final MessageStoreMessageEnumeration
            NON_AUTHORITATIVE_INFORMATION
            = new MessageStoreMessageEnumeration(203);
    public static final MessageStoreMessageEnumeration RESET_CONTENT
            = new MessageStoreMessageEnumeration(205);
    public static final MessageStoreMessageEnumeration PARTIAL_CONTENT
            = new MessageStoreMessageEnumeration(206);

    /* ======================================================================
     * REDIRECTION
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration MULTIPLE_CHOICES
            = new MessageStoreMessageEnumeration(300);
    public static final MessageStoreMessageEnumeration MOVED_PERMANENTLY
            = new MessageStoreMessageEnumeration(301);
    public static final MessageStoreMessageEnumeration MOVED_TEMPORARILY
            = new MessageStoreMessageEnumeration(302);
    public static final MessageStoreMessageEnumeration SEE_OTHER
            = new MessageStoreMessageEnumeration(303);
    public static final MessageStoreMessageEnumeration NOT_MODIFIED
            = new MessageStoreMessageEnumeration(304);
    public static final MessageStoreMessageEnumeration USE_PROXY
            = new MessageStoreMessageEnumeration(305);

    /* ======================================================================
     * CLIENT ERROR
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration BAD_REQUEST
            = new MessageStoreMessageEnumeration(400);
    public static final MessageStoreMessageEnumeration UNAUTHORIZED
            = new MessageStoreMessageEnumeration(401);
    public static final MessageStoreMessageEnumeration PAYMENT_REQUIRED
            = new MessageStoreMessageEnumeration(402);
    public static final MessageStoreMessageEnumeration FORBIDDEN
            = new MessageStoreMessageEnumeration(403);
    public static final MessageStoreMessageEnumeration NOT_FOUND
            = new MessageStoreMessageEnumeration(404);
    public static final MessageStoreMessageEnumeration METHOD_NOT_ALLOWED
            = new MessageStoreMessageEnumeration(405);
    public static final MessageStoreMessageEnumeration NOT_ACCEPTABLE
            = new MessageStoreMessageEnumeration(406);
    public static final MessageStoreMessageEnumeration
            PROXY_AUTHENTICATION_REQUIRED
            = new MessageStoreMessageEnumeration(407);
    public static final MessageStoreMessageEnumeration REQUEST_TIMEOUT
            = new MessageStoreMessageEnumeration(408);
    public static final MessageStoreMessageEnumeration CONFLICT
            = new MessageStoreMessageEnumeration(409);
    public static final MessageStoreMessageEnumeration GONE
            = new MessageStoreMessageEnumeration(410);
    public static final MessageStoreMessageEnumeration LENGTH_REQUIRED
            = new MessageStoreMessageEnumeration(411);
    public static final MessageStoreMessageEnumeration PRECONDITION_FAILED
            = new MessageStoreMessageEnumeration(412);
    public static final MessageStoreMessageEnumeration REQUEST_ENTITY_TOO_LARGE
            = new MessageStoreMessageEnumeration(413);
    public static final MessageStoreMessageEnumeration REQUEST_URI_TOO_LONG
            = new MessageStoreMessageEnumeration(414);
    public static final MessageStoreMessageEnumeration UNSUPPORTED_MEDIA_TYPE
            = new MessageStoreMessageEnumeration(415);
    public static final MessageStoreMessageEnumeration
            REQUESTED_RANGE_NOT_SATISFIABLE
            = new MessageStoreMessageEnumeration(416);
    public static final MessageStoreMessageEnumeration EXPECTATION_FAILED
            = new MessageStoreMessageEnumeration(417);

    /* ======================================================================
     * SERVER ERROR
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration INTERNAL_SERVER_ERROR
            = new MessageStoreMessageEnumeration(500);
    public static final MessageStoreMessageEnumeration NOT_IMPLEMENTED
            = new MessageStoreMessageEnumeration(501);
    public static final MessageStoreMessageEnumeration BAD_GATEWAY
            = new MessageStoreMessageEnumeration(502);
    public static final MessageStoreMessageEnumeration SERVICE_UNAVAILABLE
            = new MessageStoreMessageEnumeration(503);
    public static final MessageStoreMessageEnumeration GATEWAY_TIMEOUT
            = new MessageStoreMessageEnumeration(504);
    public static final MessageStoreMessageEnumeration
            HTTP_VERSION_NOT_SUPPORTED
            = new MessageStoreMessageEnumeration(505);

    /* ======================================================================
     * GENERIC ERROR (NOT HTTP CODE)
     * ====================================================================== */
    public static final MessageStoreMessageEnumeration UNKNOWN_EXCEPTION
            = new MessageStoreMessageEnumeration(0);


    /**
     * The internal value for the enumeration literal.
     */
    private final int value;

    /**
     * A string representation of the enumeration literal used for the hashmap
     */
    private final String valueString;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param value the internal value for the new literal
     */
    private MessageStoreMessageEnumeration(int value) {
        this.value = value;
        this.valueString = Integer.toString(value);

        entries.put(valueString, this);
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return valueString;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String getName() {
        return valueString;
    }

    /**
     * Returns the literal value contained within the current instance of the
     * enumeration.
     *
     * @return value that the current instance is representing
     */
    public int getValue() {
        return value;
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * internal name, or null if the name is not recognized.
     *
     * @param name the internal name to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static MessageStoreMessageEnumeration literal(String name) {
        return (MessageStoreMessageEnumeration) entries.get(name);
    }

    /**
     * Retrieves the enumeration literal that is equivalent to the given
     * int literal value, or null if the value is not recognized.
     *
     * @param value the literal value to be looked up
     * @return the equivalent enumeration literal or null if the name is
     *         not recognized
     */
    public static MessageStoreMessageEnumeration literal(int value) {
        return (MessageStoreMessageEnumeration)
                entries.get(Integer.toString(value));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Aug-04	155/3	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
