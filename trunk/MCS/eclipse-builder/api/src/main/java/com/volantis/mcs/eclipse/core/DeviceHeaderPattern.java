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
package com.volantis.mcs.eclipse.core;

/**
 * Encapsulates a devices header pattern (secondary pattern)
 */
public class DeviceHeaderPattern {

    /**
     * The value of the header patterns name. Cannot be null.
     */
    private String name;

    /**
     * The value of the header patterns regular expression. Cannot be null
     */
    private String regularExpression;

    /**
     * The value of the header patterns base device. Could be null.
     */
    private String baseDevice;

    /**
     * Creates a <code>DeviceHeaderPattern</code> with the given arguments
     * @param name the name associated with the pattern. Cannot be null.
     * @param regularExpression the regularExpression associated with the
     * pattern. Cannot be null.
     * @param baseDevice the baseDevice associated with the pattern. Can be
     * null
     * @throws IllegalArgumentException if either of the name or
     * regularExpression arguments are null.
     */
    public DeviceHeaderPattern(String name,
                               String regularExpression,
                               String baseDevice) {
        setName(name);
        setRegularExpression(regularExpression);
        setBaseDevice(baseDevice);
    }

    /**
     * Gets the name value. Never null.
     * @return the name value.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the base device. Could be null.
     * @return the base device or null if it has not been set.
     */
    public String getBaseDevice() {
        return baseDevice;
    }

    /**
     * Gets the regular expression. Never null
     * @return the regular expression.
     */
    public String getRegularExpression() {
        return regularExpression;
    }

    /**
     * Sets the name value.
     * @param name the name. Cannot be null.
     * @throws IllegalArgumentException if name is null
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot be null: name.");
        }
        this.name = name;
    }

    /**
     * Sets the regular expression.
     * @param regexp the regular expression. Cannot be null.
     * @throws IllegalArgumentException if regexp is null
     */
    public void setRegularExpression(String regexp) {
        if (regexp == null) {
            throw new IllegalArgumentException("Cannot be null: regexp.");
        }
        this.regularExpression = regexp;
    }

    /**
     * Sets the base device.
     * @param baseDevice the base device. Can be null.
     */
    public void setBaseDevice(String baseDevice) {
        this.baseDevice = baseDevice;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10539/3	adrianj	VBM:2005111712 fixed up merge conflicts

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4172/3	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 07-May-04	4172/1	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 ===========================================================================
*/
