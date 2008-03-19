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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.search.ui.text.Match;

/**
 * Data object for storing the details of a device search match.
 *
 * This class implements IAdaptable making it usable with Eclipse components
 * such as WorkbenchLabelProvider and others that make use of IAdaptable.
 */
public class DeviceSearchMatch extends Match implements IAdaptable {
    /**
     * The IFile associated with this DeviceSearchMatch.
     */
    private final IFile file;

    /**
     * The name of the device that was matched.
     */
    private final String deviceName;

    /**
     * Construct a new DeviceSearchMatch.
     * @param file the IFile in which the match was found
     * @param deviceName the name of the device that was matched
     */
    public DeviceSearchMatch(IFile file, String deviceName) {
        super(file, -1, -1);
        assert(file!=null);
        assert(deviceName!=null);

        this.file = file;
        this.deviceName = deviceName;
    }

    public Object getElement() {
        return this;
    }

    /**
     * Get the IFile associated with this DeviceSearchMatch.
     * @return file
     */
    public IFile getFile() {
        return file;
    }

    /**
     * Get the name of the device that was matched.
     * @return deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    // javadoc inherited
    public Object getAdapter(Class aClass) {
        return file.getAdapter(aClass);
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = o != null && o.getClass().equals(getClass());
        if(equals) {
            DeviceSearchMatch match = (DeviceSearchMatch) o;
            equals = match.getFile().equals(getFile()) &&
                    match.getDeviceName().equals(getDeviceName());
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return getFile().hashCode() * getDeviceName().hashCode();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/3	allan	VBM:2004070608 Device search

 ===========================================================================
*/
