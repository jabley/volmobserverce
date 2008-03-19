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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.impl.TAttribute;
import com.volantis.mcs.dissection.dom.AccumulatorConstants;
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

import java.util.List;

public class TCalculatorNewWay
    extends AbstractCalculator
    implements AccumulatorConstants {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private TDissectableDocument document;

    public TCalculatorNewWay(TDissectableDocument document) {
        super(document);
        this.document = document;
    }

    public void addDocumentOverhead(Accumulator accumulator)
        throws DissectionException {

        accumulator.add(4);
    }

    public void addElementOverhead(DissectableElement element,
                                   Accumulator accumulator)
        throws DissectionException {

        TElement tElement = (TElement) element;

        // Add one for the token.
        accumulator.add(1);

        List attributes = tElement.attributes();
        int count = attributes.size();
        for (int i = 0; i < count; i += 1) {

            TAttribute attribute = (TAttribute) attributes.get(i);

            // Add 1 for the attribute name.
            accumulator.add(1);

            // If the attribute value is less than 6 characters long then add
            // one for its value.
            TString value = attribute.getValue();
            if (value.getString(context).length() < 6) {
                accumulator.add(1);
            } else {
                value.addCost(context, accumulator);
                if (accumulator.isCalculationFinished()) {
                    return;
                }
            }
        }

        // An empty element has no extra cost and an element with content
        // costs 1.
        if (!document.isAlwaysEmpty(tElement)) {
            accumulator.add(1);
        }
    }

    public void addShardLinkCost(DissectableElement element,
                                 Accumulator accumulator,
                                 ShardLinkDetails details)
        throws DissectionException {

        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
