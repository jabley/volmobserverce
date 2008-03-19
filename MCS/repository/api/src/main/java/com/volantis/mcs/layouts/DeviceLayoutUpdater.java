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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Update the format count of a device layout.
 * @mock.generate
 */
public class DeviceLayoutUpdater extends  FormatVisitorAdapter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DeviceLayoutUpdater.class);

    /**
     * The format count.
     */
    private int count;

    /**
     * Increment the visit count by 1 and return false to continue the visiting.
     * @return false.
     */
    public boolean visitFormat(Format format,
                               Object object) {
        format.setInstance(count);
        ++count;
        return super.visitFormat(format,
                                 object);
    }

    /**
     * Updates the device layout format count by using its root format as the
     * entry point for counting the formats.  The format count is reinitialised
     * for each call to this method, allowing it to be reused.
     * @param layout the device layout to update.
     */
    public void update(Layout layout) {
        count = 0;
        try {
            if (layout.getRootFormat() != null) {
                layout.getRootFormat().visit(this,
                                               null);
            }
            layout.setFormatCount(count);
        } catch (FormatVisitorException e) {
            LOGGER.error(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Oct-05	9727/1	ianw	VBM:2005100506 Fixed remote repository issues

 03-Oct-05	9590/1	schaloner	VBM:2005092204 Updated formatCount in each format in DeviceLayout

 ===========================================================================
*/
