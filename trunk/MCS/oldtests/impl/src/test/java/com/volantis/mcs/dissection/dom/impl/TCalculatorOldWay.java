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

public class TCalculatorOldWay
    extends AbstractCalculator
    implements AccumulatorConstants {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private TDissectableDocument document;

    public TCalculatorOldWay(TDissectableDocument document) {
        super(document);
        this.document = document;
    }

    public void addDocumentOverhead(Accumulator accumulator)
        throws DissectionException {

        accumulator.add(20);
    }

    public void addElementOverhead(DissectableElement element,
                                   Accumulator accumulator)
        throws DissectionException {

        TElement tElement = (TElement) element;

        // Add the cost of the < and the >
        accumulator.add(2);

        // Add the cost of the element name.
        tElement.getName().addCost(context, accumulator);
        if (accumulator.isCalculationFinished()) {
            return;
        }

        List attributes = tElement.attributes();
        int count = attributes.size();
        for (int i = 0; i < count; i += 1) {
            // Add the cost of the space before the attribute, the equals sign
            // and the two double quote characters.
            accumulator.add(4);

            TAttribute attribute = (TAttribute) attributes.get(i);

            // Add the costs of the attribute name and value.
            attribute.getName().addCost(context, accumulator);
            if (accumulator.isCalculationFinished()) {
                return;
            }
            attribute.getValue().addCost(context, accumulator);
            if (accumulator.isCalculationFinished()) {
                return;
            }
        }

        // Add either the overhead of the / or the overhead of the
        // close element tag depending on whether the element is always empty.
        if (document.isAlwaysEmpty(tElement)) {
            accumulator.add(1);
        } else {
            accumulator.add(3);
            tElement.getName().addCost(context, accumulator);
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

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
