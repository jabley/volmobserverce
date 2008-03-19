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
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.WBDOMDocument;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbsax.CopyReferenceResolver;
import com.volantis.mcs.wbsax.NullReferenceResolver;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * A simple, general, concrete implementation of 
 * {@link WBSAXSerialisationVisitor} which is suitable for serialising a
 * normal (read non-dissectable) WBDOM to WBSAX events. 
 * <p> 
 * NOTE: currently this is only used for testing, but I have left it in the
 * main package as I suspect that it will be useful for detailed logging in
 * cases where there are problems with WBXML support in the field.
 */ 
public class WBSAXSerialiser extends WBSAXSerialisationVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            WBSAXSerialiser.class);

    public WBSAXSerialiser(WBSAXContentHandler handler,
            SerialisationConfiguration configuration,
            SerialisationURLListener urlListener) {
        super(handler, configuration, urlListener);
    }

    /**
     * Serialise an entire WBDOM document. 
     * 
     * @param document the WBDOM document to serialise.
     * @throws WBDOMException if there was a problem during serialisation.
     */ 
    public void serialise(WBDOMDocument document) 
            throws WBDOMException {
        
        // Set up for the serialisation.
        // This involves setting up for the creation of an output string table
        // if there was an input string table. 
        ReferenceResolver resolver;
        StringTable outputStringTable = null;
        StringTable inputStringTable = document.getStringTable();
        if (inputStringTable != null && inputStringTable.size() > 0) {
            outputStringTable = new StringTable();
            StringReferenceFactory references  = new StringReferenceFactory(
                    outputStringTable, document.getStringFactory());
            resolver = new CopyReferenceResolver(references);            
        } else {
            resolver = new NullReferenceResolver();
        }
        initialiseSerialisers(resolver);
        
        // Serialise the document.
        try {
            // Kick off the document.
            handler.startDocument(document.getVersion(), 
                    document.getPublicId(), document.getCodec(), 
                    outputStringTable, document.getStringFactory());
            // Visit down in to the document to serialise it.
            visitElement(document.getRootElement());
            // Complete the output string table, if neccessary.
            resolver.markComplete();
            // Finish off the document.
            handler.endDocument();
        } catch (WBSAXException e) {            
            throw new WBDOMException(
                        exceptionLocalizer.format("wbdom-document-write-error"),
                        e);
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 16-Jun-03	421/1	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
