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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/IMDAPITarget.java,v 1.1 2002/05/16 08:42:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Mar-02    Mat             VBM:2002022009 - Created.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeDefinition;
import com.volantis.mcs.build.parser.ProcessingInstructionTarget;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.SchemaParser;
import com.volantis.mcs.build.parser.Scope;
import org.jdom.ProcessingInstruction;

import java.util.List;

/**
 * Instances of this class handle processing instructions with a target of
 * "imdapi".
 */
public class IMDAPITarget
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
            if ((value = pi.getValue("name")) != null) {
                info.setAPIName(value);
                parser.println("Attribute " + info.getName() +
                        " is known as "
                        + info.getAPIName() + " in java code");
            }

            if ((value = pi.getValue("protocolName")) != null) {
                info.setProtocolName(value);
                parser.println("Attribute " + info.getName() +
                        " is known as "
                        + info.getProtocolName() + " in protocol");
            }

        } else if (object instanceof ElementInfo) {
            ElementInfo info = (ElementInfo) object;
            AttributesStructureInfo attributesStructureInfo
                    = info.getAttributesStructureInfo();

            info.setAPIElementClass(pi.getValue("elementClass"));

            value = pi.getValue("attributesClass");
            info.setAPIAttributesClass(value);

            value = pi.getValue("naturalName");
            info.setNaturalName(value);

            value = pi.getValue("assetClass");
            attributesStructureInfo.setApiAssetClassName(value);

            value = pi.getValue("componentClassBase");
            attributesStructureInfo.setApiComponentClassBase(value);

        } else if (object instanceof AttributeGroupInfo) {
            AttributeGroupInfo info = (AttributeGroupInfo) object;
            AttributesStructureInfo attributesStructureInfo
                    = info.getAttributesStructureInfo();

            info.setAPIElementClass(pi.getValue("elementClass"));

            value = pi.getValue("attributeGroupClass");
            //System.out.println ("attributeGroupClass=" + value);
            attributesStructureInfo.setAPIAttributesClass(value);

            value = pi.getValue("attributeGroupInterface");
            attributesStructureInfo.setAPIAttributesInterface(value);

            value = pi.getValue("baseAttributeGroup");
//      System.out.println("baseAttributeGroup = " + value);
            attributesStructureInfo.setAPIBaseAttributeGroup(value);

            value = pi.getValue("naturalName");
            attributesStructureInfo.setNaturalName(value);



            // Add any extra attributes.
            if ((value = pi.getValue("attribute")) != null) {
                String name = value;

                Scope scope = parser.getScope();
                AttributeDefinition definition = scope.addAttributeDefinition(
                        name);
                definition.setType(pi.getValue("type"));
                definition.setUse(pi.getValue("use"));

                List attributes = attributesStructureInfo.getAttributes();
                attributes.add(definition);
            }

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

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
