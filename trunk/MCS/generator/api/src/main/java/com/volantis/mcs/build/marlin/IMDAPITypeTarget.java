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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/IMDAPITypeTarget.java,v 1.1 2002/05/16 08:42:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-02    Mat             VBM:2002022009 - Created.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.ProcessingInstructionTarget;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.SchemaParser;
import org.jdom.ProcessingInstruction;

/**
 * Instances of this class handle processing instructions with a target of
 * "imdapi".
 */
public class IMDAPITypeTarget
        implements ProcessingInstructionTarget {

    // Javadoc inherited from super class.
    public void handleProcessingInstruction(
            SchemaParser parser,
            String target, String data) {

        SchemaObject object = parser.getCurrentObject();

        ProcessingInstruction pi = new ProcessingInstruction(target, data);
        String value;

        if (object instanceof AttributeInfo) {
            AttributeInfo info = (AttributeInfo) object;

            // Process the options.
            if ((value = pi.getValue("attributeType")) != null) {
                info.setAttributeType(value);
                parser.println("Attribute " + info.getName() + " is of type "
                        + info.getAttributeType() + " in java code");
            }


        } else if (object instanceof ElementInfo) {

        } else if (object instanceof AttributeGroupInfo) {

        } else {
            throw new IllegalStateException("Unhandled object " + object);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
