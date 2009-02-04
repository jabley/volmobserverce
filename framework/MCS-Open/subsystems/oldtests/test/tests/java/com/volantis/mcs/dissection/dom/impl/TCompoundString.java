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
 * (c) Volantis Systems Ltd 2002. 
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
import com.volantis.mcs.dissection.dom.Accumulator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TCompoundString
    extends AbstractTString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private List strings;

    public TCompoundString() {
        strings = new ArrayList();
    }

    public void addString(TString string) {
        strings.add(string);
    }

    public void addCost(TStringContext context,
                       Accumulator accumulator)
        throws DissectionException {

        int count = strings.size();
        for (int i = 0; i < count; i += 1) {
            TString string = (TString) strings.get(i);
            string.addCost(context, accumulator);
            if (accumulator.isCalculationFinished()) {
                return;
            }
        }

        return;
    }

    public void append(TStringContext context,
                       StringBuffer buffer) {

        int count = strings.size();
        for (int i = 0; i < count; i += 1) {
            TString string = (TString) strings.get(i);
            string.append(context, buffer);
        }
    }

    public void debugAppend(TStringContext context,
                            StringBuffer buffer) {

        int count = strings.size();
        buffer.append("{");
        for (int i = 0; i < count; i += 1) {
            if (i != 0) {
                buffer.append("+");
            }
            TString string = (TString) strings.get(i);
            string.debugAppend(context, buffer);
        }
        buffer.append("}");
    }

    public void debugWrite(TStringContext context, Writer writer)
        throws DissectionException {

        try {
            int count = strings.size();
            writer.write("{");
            for (int i = 0; i < count; i += 1) {
                if (i != 0) {
                    writer.write("+");
                }
                TString string = (TString) strings.get(i);
                string.debugWrite(context, writer);
            }
            writer.write("}");
        } catch (IOException ioe) {
            throw new DissectionException(ioe);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

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
