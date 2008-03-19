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
 * $Header: /src/voyager/com/volantis/mcs/protocols/SpatialFormatIteratorInstance.java,v 1.3 2002/12/10 18:14:28 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Nov-02    Sumit           VBM:2002111103 - Created
 * 05-Dec-02    Chris W         VBM:2002111103 - isEmptyImpl now inspects the
 *                              temporal format iterator's children to see if
 *                              they are empty.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * Contains all the state associated with a SpatialFormatIterator in a
 * particular MarinerPageContext.
 *
 * @mock.generate base="AbstractFormatIteratorInstance"
 */
public class SpatialFormatIteratorInstance
        extends AbstractFormatIteratorInstance {

    /**
     * Create a new <code>SegmentGridInstance</code>.
     */
    public SpatialFormatIteratorInstance(NDimensionalIndex index) {
        super(index);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/3	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/5	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
