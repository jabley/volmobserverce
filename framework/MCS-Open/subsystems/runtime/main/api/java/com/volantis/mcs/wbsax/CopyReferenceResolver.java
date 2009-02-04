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
package com.volantis.mcs.wbsax;

/**
 * A {@link ReferenceResolver} for use with a filter which makes changes to 
 * the string table of the WBSAX event stream that it is filtering.
 * <p>
 * This will use a new output string table and resolve references from the
 * input string table into strings and then add them into the output string
 * table. 
 */ 
public class CopyReferenceResolver implements ReferenceResolver {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private StringReferenceFactory references;

    public CopyReferenceResolver(StringReferenceFactory references) {
        this.references = references;
    }

    public StringTable resolve(StringTable table) {
        // Return the output string table.
        return references.getStringTable();
    }

    public StringReference resolve(StringReference reference) {
        // Extract the string from the input string table.
        WBSAXString string = reference.resolveString();
        // Add the string to the output string table.
        return references.createReference(string);
    }

    public void markComplete() {
        // Mark the output string table complete.
        references.getStringTable().markComplete();        
    }

    public StringReferenceFactory getReferenceFactory() {
        return references;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
