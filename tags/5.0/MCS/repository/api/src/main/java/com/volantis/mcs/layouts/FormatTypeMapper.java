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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatTypeMapper.java,v 1.3 2003/03/11 12:42:56 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Dec-02    Allan           VBM:2002121017- A class that provides a
 *                              mapping (or possibly mappings in the future)
 *                              between an object and a FormatType.
 * 11-Dec-02    Allan           VBM:2002121124 - Added getKeyString().
 * 11-Mar-03    Allan           VBM:2003010303 - Removed DeviceLayoutFormat 
 *                              from mappings. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import java.util.HashMap;

public class FormatTypeMapper {

    /**
     * A mapping of format type names to FormatType objects.
     */
    static final HashMap typeStringToFormatType = new HashMap();

    static {
        //populate typeStringToFormatType
        typeStringToFormatType.put(FormatType.COLUMN_ITERATOR_PANE.getTypeName(),
                                   FormatType.COLUMN_ITERATOR_PANE);
        typeStringToFormatType.put(FormatType.DISSECTING_PANE.getTypeName(),
                                   FormatType.DISSECTING_PANE);
        typeStringToFormatType.put(FormatType.FORM.getTypeName(),
                                   FormatType.FORM);
        typeStringToFormatType.put(FormatType.FORM_FRAGMENT.getTypeName(),
                                   FormatType.FORM_FRAGMENT);
        typeStringToFormatType.put(FormatType.FRAGMENT.getTypeName(),
                                   FormatType.FRAGMENT);
        typeStringToFormatType.put(FormatType.GRID.getTypeName(),
                                   FormatType.GRID);
        typeStringToFormatType.put(FormatType.PANE.getTypeName(),
                                   FormatType.PANE);
        typeStringToFormatType.put(FormatType.REGION.getTypeName(),
                                   FormatType.REGION);
        typeStringToFormatType.put(FormatType.REPLICA.getTypeName(),
                                   FormatType.REPLICA);
        typeStringToFormatType.put(FormatType.ROW_ITERATOR_PANE.getTypeName(),
                                   FormatType.ROW_ITERATOR_PANE);
        typeStringToFormatType.put(FormatType.SEGMENT.getTypeName(),
                                   FormatType.SEGMENT);
        typeStringToFormatType.put(FormatType.SEGMENT_GRID.getTypeName(),
                                   FormatType.SEGMENT_GRID);
        typeStringToFormatType.put(FormatType.SPATIAL_FORMAT_ITERATOR.getTypeName(),
                                   FormatType.SPATIAL_FORMAT_ITERATOR);
        typeStringToFormatType.put(FormatType.TEMPORAL_FORMAT_ITERATOR.getTypeName(),
                                   FormatType.TEMPORAL_FORMAT_ITERATOR);
    }

    /**
     * Get the FormatType associated with a specified type name.
     * @param typeName The type name.
     * @return The FormatType associated with the type name.
     */
    public static FormatType getFormatType(String typeName) {
        return (FormatType) typeStringToFormatType.get(typeName);
    }

    /**
     * Get the String that maps to a given FormatType.
     * @param type The FormatType
     * @return The String that maps to the specified FormatType.
     */
    public static String getKeyString(FormatType type) {
        return type.getTypeName();
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
