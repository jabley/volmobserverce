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
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dissection.annotation.DissectedDocumentImpl;
import com.volantis.mcs.dissection.dom.*;
import com.volantis.mcs.dissection.dom.impl.*;
import com.volantis.mcs.dissection.dom.impl.TCalculatorFactory;
import com.volantis.mcs.dissection.dom.impl.TCalculatorNewWayFactory;
import com.volantis.mcs.dissection.dom.impl.TCalculatorOldWayFactory;
import com.volantis.mcs.dissection.dom.impl.TDissectableDocument;
import com.volantis.mcs.dissection.dom.impl.TPlainOutputter;
import com.volantis.mcs.dissection.dom.impl.TDissectableDocumentBuilder;
import com.volantis.mcs.utilities.MarinerURL;

import java.io.PrintWriter;

/**
 * The test cases in this class exercise the dissection process with a number
 * of different pages.
 */
public class DissectorTestCase
    extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public DissectorTestCase(String name) {
        super(name);
    }

    protected TDissectableDocument createDocument(TCalculatorFactory factory,
                                                  String path)
        throws Exception {

        TDissectableDocumentBuilder builder
            = new TDissectableDocumentBuilder(factory);
        TestDocumentDetails details
            = DissectionTestCaseHelper.createDissectableDocument(builder,
                    this.getClass(), path);
        return (TDissectableDocument) details.getDocument();
    }

    protected void dissectDocument(TCalculatorFactory factory,
                                   String path,
                                   int expectedSharedEntryCount,
                                   int maxPageSize,
                                   DissectionURLManager urlManager)
        throws Exception {

        DissectableDocument inputDocument = createDocument(factory, path);

        assertEquals("String table not populated properly",
                     expectedSharedEntryCount,
                     inputDocument.getSharedContentCount());

        Dissector dissector = new Dissector();

        DissectionCharacteristicsImpl characteristics
            = new DissectionCharacteristicsImpl();
        characteristics.setMaxPageSize(maxPageSize);

        DissectionContext dissectionContext = new MyDissectionContext();

        DocumentInformationImpl docInfo = new DocumentInformationImpl();
        docInfo.setDocumentURL(new MarinerURL("hello.xml"));

        DissectedDocument intermediateDocument = (DissectedDocumentImpl)
            dissector.createDissectedDocument(dissectionContext, characteristics, inputDocument,
                                              urlManager, docInfo);

        RequestedShards requestedShards
            = intermediateDocument.createRequestedShards();

        /*
        // Request that all shards be selected.
        AvailableShards availableShards
            = intermediateDocument.createAvailableShards();

        dissector.selectRequestedShards(intermediateDocument, requestedShards,
                                        availableShards);
                                        */

        // Populate all the shards in each dissectable area.
        int count = intermediateDocument.getDissectableAreaCount();
        int max = 0;
        for (int d = 0; d < count; d += 1) {
            ShardIterator iterator
                = intermediateDocument.getShardIterator(dissectionContext, d);
            while (iterator.hasMoreShards()) {
                if (d == 0) {
                    max += 1;
                }
                iterator.populateNextShard();
            }
        }

        //DissectedContentHandler contentHandler
        //            = new DebugContentHandler(new PrintWriter(System.out));
        DissectedContentHandler contentHandler
            = new TPlainOutputter(new PrintWriter(System.out));

        try {
            // Output the first shard
            System.out.println("Begin First Shard 0");
            dissector.serialize(dissectionContext,
                                intermediateDocument,
                                requestedShards,
                                contentHandler);
            System.out.println("End First Shard 0");

            // Output the second shard if it is the middle.
            if (max > 1) {
                requestedShards.setShard(0, 1);

                System.out.println("Begin Middle Shard 1");
                dissector.serialize(dissectionContext,
                                    intermediateDocument,
                                    requestedShards,
                                    contentHandler);
                System.out.println("End Middle Shard 1");
            }

            // Output the last shard if there is more then one.
            if (max > 0) {
                requestedShards.setShard(0, max - 1);

                System.out.println("Begin Last Shard " + (max - 1));
                dissector.serialize(dissectionContext,
                                    intermediateDocument,
                                    requestedShards,
                                    contentHandler);
                System.out.println("End Last Shard " + (max - 1));
            }

        } finally {
            System.out.println("BEFORE");
            ((DissectedDocumentImpl) intermediateDocument).debugOutput(new PrintWriter(System.out));
            System.out.println("AFTER");
        }
    }

    public void testDissector()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorOldWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        dissectDocument(factory, "dissector-input.xml", 3, 1160,
                        urlManager);
    }

    public void testWilliamHillHorseOld()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorOldWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        try {
            dissectDocument(factory, "wh_horse.xml", 0, 1300,
                            urlManager);
            fail("Fixed content size should be too large");
        } catch (DissectionException e) {
        }
    }

    public void testWilliamHillHorseNew()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorNewWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        dissectDocument(factory, "wh_horse.xml", 0, 1300,
                        urlManager);
    }

    public void testWilliamHillFootballOld()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorOldWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        dissectDocument(factory, "wh_football.xml", 0, 1300,
                        urlManager);
    }

    public void testWilliamHillFootballNew()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorNewWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        dissectDocument(factory, "wh_football.xml", 0, 1300,
                        urlManager);
    }

    public void testDissectorVisitor()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorOldWayFactory();

        DissectableDocument inputDocument
            = createDocument(factory, "dissector-input.xml");
        VisitingStatisticsGatherer visitor = new VisitingStatisticsGatherer();
        inputDocument.visitDocument(visitor);

        visitor.print(System.out);
    }

    public void testLargeInputVisitor()
        throws Exception {

        TCalculatorFactory factory = new TCalculatorOldWayFactory();

        DissectionURLManager urlManager = new MyDissectionURLManager();

        dissectDocument(factory, "large-input.xml", 0, 2000,
                        urlManager);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
