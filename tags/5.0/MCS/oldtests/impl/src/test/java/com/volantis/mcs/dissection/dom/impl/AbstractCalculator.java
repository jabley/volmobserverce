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
import com.volantis.mcs.dissection.dom.impl.TString;
import com.volantis.mcs.dissection.dom.impl.TStringContext;
import com.volantis.mcs.dissection.dom.impl.TText;
import com.volantis.mcs.dissection.dom.CostCalculator;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.Accumulator;

public abstract class AbstractCalculator
    implements CostCalculator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    protected TStringContext context;

    public AbstractCalculator(TStringContext context) {
        this.context = context;
    }

    public void addTextCost(DissectableText text, Accumulator accumulator)
        throws DissectionException {

        TText tText = (TText) text;
        TString contents = tText.getContents();

        contents.addCost(context, accumulator);
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
