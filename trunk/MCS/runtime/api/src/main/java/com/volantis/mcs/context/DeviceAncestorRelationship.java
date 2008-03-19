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
package com.volantis.mcs.context;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Typesafe Enumeration for the set of int values that the
 * {@link MarinerRequestContext#getAncestorRelationship} returns.
 * A {@link #get} method is provided that returns
 * the <code>DeviceAncestorRelationship</code> for a given int.
 */
public final class DeviceAncestorRelationship {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DeviceAncestorRelationship.class);

    /**
     * DeviceAncestorRelationship for the "unknown" relationship
     */
    public static final DeviceAncestorRelationship UNKNOWN
            = new DeviceAncestorRelationship(MarinerRequestContext.UNKNOWN,
                                             "unknown");

    /**
     * DeviceAncestorRelationship for the "unrelated" relationship
     */
    public static final DeviceAncestorRelationship UNRELATED
            = new DeviceAncestorRelationship(MarinerRequestContext.IS_UNRELATED,
                                             "unrelated");

    /**
     * DeviceAncestorRelationship for the "ancestor" relationship
     */
    public static final DeviceAncestorRelationship ANCESTOR
            = new DeviceAncestorRelationship(MarinerRequestContext.IS_ANCESTOR,
                                             "ancestor");

    /**
     * DeviceAncestorRelationship for the "device" relationship
     */
    public static final DeviceAncestorRelationship DEVICE
            = new DeviceAncestorRelationship(MarinerRequestContext.IS_DEVICE,
                                             "device");

    /**
     * The int value of the relationship.
     * {@see MarinerRequestContext#getAncestorRelationship} for the set
     * of valid int values.
     */
    private int relationshipValue;

    /**
     * String representation of the relationship
     */
    private String relationshipName;

    /**
     * Creates a new <code>DeviceAncestorRelationship<code> instance
     * @param relationshipCode the int representation of the relationship.
     *        {@see MarinerRequestContext#getAncestorRelationship} for the set
     *         of valid int values.
     * @param relationshipName the name of the relationship.
     */
    private DeviceAncestorRelationship(int relationshipCode,
                                       String relationshipName) {
        this.relationshipValue = relationshipCode;
        this.relationshipName = relationshipName;
    }

    /**
     * Returns the DeviceAncestorRelationship literal for the given int
     * value. {@see MarinerRequestContext#getAncestorRelationship} for the set
     * of valid int values.
     * @param relationshipValue the value
     * @return The <code>DeviceAncestorRelationship</code> instance that
     * corresponds to the value parameter. If the value parameter is
     * not a valid value the {@link #UNKNOWN} literal is returned.
     */
    public static DeviceAncestorRelationship get(
            int relationshipValue) {

        DeviceAncestorRelationship relationship;
        // map int value to literal
        switch (relationshipValue) {
            case MarinerRequestContext.IS_UNRELATED:
                relationship = DeviceAncestorRelationship.UNRELATED;
                break;
            case MarinerRequestContext.IS_ANCESTOR:
                relationship = DeviceAncestorRelationship.ANCESTOR;
                break;
            case MarinerRequestContext.IS_DEVICE:
                relationship = DeviceAncestorRelationship.DEVICE;
                break;
            case MarinerRequestContext.UNKNOWN:
                relationship = DeviceAncestorRelationship.UNKNOWN;
                break;
            default:
                // unknown relationship value. log a warning and return the
                // unknown value
                logger.warn("unkown-device-relationship", new Object[]{new Integer(relationshipValue)});
                relationship = DeviceAncestorRelationship.UNKNOWN;
        }
        return relationship;
    }

    /**
     * Get the relationship value.
     * {@see MarinerRequestContext#getAncestorRelationship} for the set
     * of valid int values.
     * @return the relationship code
     */
    public int getRelationshipValue() {
        return relationshipValue;
    }

    /**
     * Get the name of the relationship
     * @return the name of the relationship
     */
    public String getRelationshipName() {
        return relationshipName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
