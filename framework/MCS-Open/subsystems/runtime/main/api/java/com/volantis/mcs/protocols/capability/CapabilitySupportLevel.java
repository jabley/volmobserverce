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
/**
 * TODO javadoc this class
 */
package com.volantis.mcs.protocols.capability;

/**
 * Enumeration class for capability support level.
 */
public class CapabilitySupportLevel {
    /**
     * Default capability support - calling code satisfy the contract 
     * "behave as you did before on that device" 
     */
    public static final CapabilitySupportLevel DEFAULT = new CapabilitySupportLevel(
            "default");

    /**
     * Full capability support is avaliable
     */
    public static final CapabilitySupportLevel FULL = new CapabilitySupportLevel(
            "full");

    /**
     * Partial capability support is avaliable
     */
    public static final CapabilitySupportLevel PARTIAL = new CapabilitySupportLevel(
            "partial");

    /**
     * No capability support is avaliable
     */
    public static final CapabilitySupportLevel NONE = new CapabilitySupportLevel(
            "none");

    /**
     * the Level of support
     */
    private final String supportLevel;

    /**
     * Create a new enumeration with the given support type
     * @param supportLevel
     */
    private CapabilitySupportLevel(String supportLevel) {
        this.supportLevel = supportLevel;
    }

    //javadoc inheritied
    public String toString() {
        return supportLevel;
    }

    /**
     * Convert a string value into a CapabilitySupportLevel
     * @param supportString [full|partial|none]|[true|false]
     * @return
     */
    public static CapabilitySupportLevel getSupportLevel(String supportString) {

        CapabilitySupportLevel supportLevel = null;

        if ("full".equals(supportString)
                || Boolean.valueOf(supportString).booleanValue()) {
            supportLevel = FULL;
        } else if ("partial".equals(supportString)) {
            supportLevel = PARTIAL;
        } else if ("default".equals(supportString)) {
            supportLevel = DEFAULT;
        } else if (supportString != null) {
            supportLevel = NONE;
        } 

        // return null in case supportString is null
        return supportLevel;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
