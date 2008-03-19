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
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.utilities.MarinerURL;

import java.io.IOException;
import java.io.Writer;

public class TShardLinkURLParameter
    extends AbstractTString
    implements TSubstitutionParameter {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public void addCost(TStringContext context,
                       Accumulator accumulator)
        throws DissectionException {

        accumulator.add(getString(context).length());
    }

    public String getString(TStringContext context) {

        ShardLinkDetails details = context.getShardLinkDetails();
        if (details == null) {
            return null;
        } else {
            MarinerURL url = details.getURL();
            return url.getExternalForm();
        }
    }

    public void append(TStringContext context,
                       StringBuffer buffer) {

        buffer.append(getString(context));
    }

    public void debugAppend(TStringContext context,
                            StringBuffer buffer) {

        String string = getString(context);
        if (string == null) {
            buffer.append("[url]");
        } else {
            buffer.append("[url=").append(string).append("]");
        }
    }

    public void debugWrite(TStringContext context, Writer writer)
        throws DissectionException {

        try {
            String string = getString(context);
            if (string == null) {
                writer.write("[url]");
            } else {
                writer.write("[url=");
                writer.write(string);
                writer.write("]");
            }
        } catch (IOException e) {
            throw new DissectionException(e);
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

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
