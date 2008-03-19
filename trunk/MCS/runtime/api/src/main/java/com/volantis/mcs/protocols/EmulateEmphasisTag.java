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
 * $Header: /src/voyager/com/volantis/mcs/protocols/EmulateEmphasisTag.java,v 1.3 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Mar-02    Allan           VBM:2002022007 - Created. Moved out of
 *                              inner class in VolantisProtocol.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class encapsulates all of the information needed to emulate
 * an emphasis tag.
 */
public final class EmulateEmphasisTag {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(EmulateEmphasisTag.class);


    /**
     * The prefix string to add before the content of the tag.
     */
    private String prefix;

    /**
     * The suffix string to add after the content of the tag.
     */
    private String suffix;

    /**
     * The alternate tag to use. If it is not null then it is wrapped
     * around the prefix, content and suffix, otherwise it has no effect.
     */
    private final String altTag;

    /**
     * Initialise the object from the context using the key to find
     * the device policy values.
     *
     * @param device The device to use to find the device policy values.
     * @param key     The key to use to find the device policy values.
     */
    public EmulateEmphasisTag(InternalDevice device, String key) {
        prefix = device.getPolicyValue(key + ".prefix");
        if (logger.isDebugEnabled()) {
            logger.debug("Prefix for " + key + " is " + prefix);
        }
        if (prefix == null) {
            prefix = "";
        }

        suffix = device.getPolicyValue(key + ".suffix");
        if (logger.isDebugEnabled()) {
            logger.debug("Suffix for " + key + " is " + suffix);
        }
        if (suffix == null) {
            suffix = "";
        }

        altTag = device.getPolicyValue(key + ".altTag");
        if (logger.isDebugEnabled()) {
            logger.debug("Alternate tag for " + key
                    + " is " + altTag);
        }
    }

    /**
     * Get the prefix string.
     *
     * @return The prefix string.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the suffix string.
     *
     * @return The suffix string.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Get the alternate tag.
     *
     * @return The alternate tag, or null if none is required.
     */
    public String getAltTag() {
        return altTag;
    }

    /**
     * Create an instance of this class if emulation is required, or
     * return null if it is not.
     *
     * @param device The device to use to find the device policy values.
     * @param key     The key to use to find the device policy values.
     * @return An instance of this class if emulation is required and
     *         null otherwise.
     */
    public static EmulateEmphasisTag create(
            InternalDevice device,
            String key) {
        if (device.getBooleanPolicyValue(key + ".enable")) {
            return new EmulateEmphasisTag(device, key);
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
