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
package com.volantis.mcs.eclipse.ab.search.devices;

/**
 * Data object class for specifying search options.
 */
public class DeviceSearchQueryOptions {
    /**
     * Case sensitive option.
     */
    private boolean isCaseSensitive;

    /**
     * Regular expression option.
     */
    private boolean isRegularExpression;

    /**
     * Device name search option.
     */
    private boolean isDeviceNameSearch;

    /**
     * Device pattern search option.
     */
    private boolean isDevicePatternSearch;

    // javadoc unecessary
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    // javadoc unecessary
    public void setCaseSensitive(boolean caseSensitive) {
        isCaseSensitive = caseSensitive;
    }

    // javadoc unecessary
    public boolean isRegularExpression() {
        return isRegularExpression;
    }

    // javadoc unecessary
    public void setRegularExpression(boolean regularExpression) {
        isRegularExpression = regularExpression;
    }

    // javadoc unecessary
    public boolean isDeviceNameSearch() {
        return isDeviceNameSearch;
    }

    // javadoc unecessary
    public void setDeviceNameSearch(boolean deviceNameSearch) {
        isDeviceNameSearch = deviceNameSearch;
    }

    // javadoc unecessary
    public boolean isDevicePatternSearch() {
        return isDevicePatternSearch;
    }

    // javadoc unecessary
    public void setDevicePatternSearch(boolean devicePatternSearch) {
        isDevicePatternSearch = devicePatternSearch;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 ===========================================================================
*/
