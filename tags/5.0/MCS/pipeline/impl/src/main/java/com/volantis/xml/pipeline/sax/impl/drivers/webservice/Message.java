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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import java.util.ArrayList;

/**
 * A Message class contains a collection of parts and provides a means of
 * adding and accessing these parts. The order of parts are significant so they
 * should be added in the correct order since this is the order they will be
 * provided in.
 */
public class Message {
    /**
     * A List of parts keyed on the part name. There is no point in having a
     * message without any parts so initialize it now. This List is un-sorted
     * so that the order will be the order in which the parts are added.
     */
    private ArrayList parts = new ArrayList();

    /**
     * Add a part to this message.
     * @param part The part to add.
     */
    public void addPart(Part part) {
        parts.add(part);
    }

    /**
     * Retrieve a part from this message.
     * @param index The index of the part. The first part is at index 0.
     * @return The part at the specified index in the Message or null if there
     * is no part at the specified index. If the index is invalid this method
     * will return null rather than throw an exception.
     */
    public Part retrievePart(int index) {
        Part part = null;
        if (index < parts.size()) {
            part = (Part)parts.get(index);
        }

        return part;
    }

    /**
     * Return the number of parts in this Message.
     * @return The number of parts in this Message.
     */
    public int size() {
        return parts.size();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 27-Jun-03	98/2	allan	VBM:2003022822 Intermediate commit for jsp testing

 ===========================================================================
*/
