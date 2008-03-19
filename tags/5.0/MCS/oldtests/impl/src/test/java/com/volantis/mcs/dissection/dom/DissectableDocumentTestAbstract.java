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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dissection.annotation.DissectedDocumentImpl;
import com.volantis.mcs.dissection.annotation.DissectableArea;
import com.volantis.mcs.dissection.DissectionTestCaseHelper;
import com.volantis.mcs.dissection.DissectionContext;
import com.volantis.mcs.dissection.ShardIterator;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.Dissector;
import com.volantis.mcs.dissection.DocumentInformationImpl;
import com.volantis.mcs.dissection.MyDissectionURLManager;
import com.volantis.mcs.dissection.DissectionURLManager;
import com.volantis.mcs.dissection.MyDissectionContext;
import com.volantis.mcs.dissection.DissectionCharacteristicsImpl;
import com.volantis.mcs.dissection.RequestedShards;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

/**
 * This class tests that an implementation of the DissectableDocument interface
 * adheres to the rules for the interface.
 * <p>
 * It does not rely on any knowledge of the underlying implementation, instead
 * it uses an abstract builder to create the document from a known xml file and
 * then simply checks to make sure that the dissectable document provides the
 * correct view of that file.
 */
public abstract class DissectableDocumentTestAbstract
    extends TestCaseAbstract {

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance(
            "com.volantis.mcs.dissection.dom.DissectableDocumentTestAbstract");

    public DissectableDocumentTestAbstract(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    protected void tearDown() throws Exception {
        Category.shutdown();
    }


    protected TestDocumentDetails createDissectableDocument(String path)
        throws Exception {

        // NOTE: This is currently hard coded to find test files only at the
        // abstract level. If you want to define test files at the subclass 
        // level, then you will need to extend the helper to search up the 
        // inheritance heirarchy of the class provided below and pass 
        // "this.getClass()" instead of this class.

        DissectableDocumentBuilder builder = createBuilder();
        return DissectionTestCaseHelper.createDissectableDocument(builder, 
                DissectableDocumentTestAbstract.class, path);
    }

    /**
     * Create a builder which will be used by the test case to create a 
     * {@link DissectableDocument} for the DOM implementation we are testing.
     * 
     * @return the dissectable documentbuilder 
     */
    protected abstract DissectableDocumentBuilder createBuilder() throws Exception;

    /**
     * Create a testing representation of the output document for 
     * the DOM implementation we are testing, which will be passed by the test
     * case into {@link #createDissectedContentHandler}.
     * <p>
     * Currently this will be either a {@link TextOutputDocument} or a 
     * {@link BinaryOutputDocument}. 
     * 
     * @return the output document
     * @throws Exception
     */ 
    protected abstract OutputDocument createOutputDocument()
        throws Exception;

    /**
     * Create a {@link DissectedContentHandler} which will be used by the test
     * case for serialising the DOM implementation we are testing.
     * 
     * @param output
     * @return the dissectable content handler
     * @throws Exception
     */ 
    protected abstract DissectedContentHandler createDissectedContentHandler(
            OutputDocument output)
        throws Exception;

    public void assertEquals(StringBuffer failures,
                             String message,
                             int expected,
                             int actual) {
        if (expected != actual) {
            failures
                .append(message)
                .append(" expected:<")
                .append(expected)
                .append("> but was:<")
                .append(actual)
                .append(">\n");
        }
    }

    public void checkNodeCount(StringBuffer failures,
                               NodeCounts expectedCounts,
                               NodeCounts actualCounts) {

        assertEquals(failures,
                     "Element count did not match,",
                     expectedCounts.elementCount,
                     actualCounts.elementCount);
        assertEquals(failures,
                     "Text count did not match,",
                     expectedCounts.textCount,
                     actualCounts.textCount);
        assertEquals(failures,
                     "Dissectable area count did not match,",
                     expectedCounts.dissectableAreaCount,
                     actualCounts.dissectableAreaCount);
        assertEquals(failures,
                     "Keep together count did not match,",
                     expectedCounts.keepTogetherCount,
                     actualCounts.keepTogetherCount);
        assertEquals(failures,
                     "Shard link count did not match,",
                     expectedCounts.shardLinkCount,
                     actualCounts.shardLinkCount);
        assertEquals(failures,
                     "Shard link group count did not match,",
                     expectedCounts.shardLinkGroupCount,
                     actualCounts.shardLinkGroupCount);
        assertEquals(failures,
                     "Shard link conditional count did not match,",
                     expectedCounts.shardLinkConditionalCount,
                     actualCounts.shardLinkConditionalCount);
        assertEquals(failures,
                     "Shared content count did not match,",
                     expectedCounts.sharedContentCount,
                     actualCounts.sharedContentCount);

        if (failures.length() > 0) {
            appendNodeCountInitCode(failures, actualCounts);
        }
    }

    public
        void checkDissectableDocument(String path,
                                      DocumentStats expectedStats)
        throws Exception {

        TestDocumentDetails details = createDissectableDocument(path);

        checkDissectableDocument(details, expectedStats);
    }

    public void checkDissectableArea(StringBuffer failures,
                                     TestDocumentDetails details,
                                     DissectionContext context,
                                     DissectableArea area,
                                     DissectableAreaStats expectedAreaStats)
        throws Exception {

        // todo: Check that the node counts are correct.

        String daDescription = "Dissectable Area " + area.getIndex();
        checkSharedContentUsages(failures,
                                 daDescription,
                                 details,
                                 expectedAreaStats.getOverheadContentUsages(),
                                 area.getOverheadUsages());

        // Populate all the shards so that we can check that they are correct.
        int actualShardCount = 0;
        for (ShardIterator iterator = area.getShardIterator(context);
             iterator.hasMoreShards();) {
            iterator.populateNextShard();
            actualShardCount += 1;
        }

        assertEquals(failures,
                     "Shard count incorrect",
                     expectedAreaStats.shardCount,
                     actualShardCount);

        for (int i = 0; i < actualShardCount; i += 1) {
            Shard shard = area.retrieveShard(context, i, null);
            ShardStats shardStats = expectedAreaStats.getShardStats(i);
            checkSharedContentUsages(failures,
                                     daDescription + " Shard " + i,
                                     details,
                                     shardStats.getSharedContentUsages(),
                                     shard.getSharedContentUsages());
        }
    }

    public void checkSharedContentUsages(StringBuffer failures,
                                         String description,
                                         TestDocumentDetails details,
                                         ExpectedSharedContentUsages expectedUsages,
                                         SharedContentUsages actualUsages)
        throws Exception {

        SharedContentUsages usages = expectedUsages.getExpectedUsages(details);
        if (usages == null) {
            if (actualUsages != null) {
                fail("Expected usages but found none");
            }
            return;
        } else if (actualUsages == null) {
            fail("Did not expect usages but found some");
        }
        int count = usages.getCount();

        // Collate all the failures so that they can be reported in one go.
        boolean failed = false;
        for (int i = 0; i < count; i += 1) {
            boolean expected = usages.isSharedContentUsed(i);
            boolean actual = actualUsages.isSharedContentUsed(i, false);
            String name = details.getCommonStringName(i);
            if (expected && !actual) {
                failed = true;
                failures
                    .append("Expected entity '")
                    .append(name)
                    .append("' to be used but it wasn't\n");
            } else if (!expected && actual) {
                failed = true;
                failures
                    .append("Did not expect entity '")
                    .append(name)
                    .append("' to be used but it was\n");
            }
        }

        if (failed) {
            failures
                .append("Above entity problems are from ")
                .append(description)
                .append("\n");
        }
    }

    public
        void checkDissectableDocument(TestDocumentDetails details,
                                      DocumentStats expectedDocumentStats)
        throws Exception {

        StringBuffer failures = new StringBuffer();

        // Get the dissectable document.
        DissectableDocument document = details.getDocument();

        // Create a dissected document.
        DissectionContext context = new MyDissectionContext();
        DissectionCharacteristicsImpl characteristics
            = new DissectionCharacteristicsImpl();
        // HACK to force dissection without relying on hints in the input
        if (expectedDocumentStats.pageSize > 0) {
            characteristics.setMaxPageSize(expectedDocumentStats.pageSize);
        } else {
            characteristics.setMaxPageSize(100000);
        }
        DissectionURLManager urlManager = new MyDissectionURLManager();
        DocumentInformationImpl docInfo = new DocumentInformationImpl();
        docInfo.setDocumentURL(new MarinerURL("common1.xml"));

        Dissector dissector = new Dissector();
        DissectedDocumentImpl dissected = (DissectedDocumentImpl)
            dissector.createDissectedDocument(context, characteristics,
                                              document, urlManager, docInfo);

        // Check the characteristics of the dissected document.
        NodeCounts expectedDocumentNodeCounts
            = expectedDocumentStats.nodeCounts;
        int dissectableAreaCount
            = expectedDocumentNodeCounts.dissectableAreaCount;
        assertEquals(failures,
                     "Incorrect number of dissectable areas,",
                     dissectableAreaCount,
                     dissected.getDissectableAreaCount());

        // Check the node counts.
        NodeCounts actualNodeCounts = null;

        // Gather statistics by visiting all the nodes.
        VisitingStatisticsGatherer visitor = new VisitingStatisticsGatherer();
        document.visitDocument(visitor);
        NodeCounts visitorStatistics = visitor.getStatistics().nodeCounts;

        // Gather statistics by iterating over all the nodes.
        IteratingStatisticsGatherer iterator = new IteratingStatisticsGatherer();
        document.visitDocument(iterator);
        NodeCounts iteratorStatistics = iterator.getStatistics().nodeCounts;

        // Make sure that all the different methods of gathering statistics
        // agree.
        assertEquals("Discrepancy between statistics gatherers",
                     visitorStatistics, iteratorStatistics);

        // Set the actualStatistics value so they can be output later.
        actualNodeCounts = visitorStatistics;

        // Check that the statistics match the expectation.
        checkNodeCount(failures,
                       expectedDocumentStats.nodeCounts,
                       actualNodeCounts);

        // Check the shared content usages.
        checkSharedContentUsages(failures,
                                 "Total Cost",
                                 details,
                                 expectedDocumentStats.getTotalContentUsages(),
                                 dissected.getTotalContentUsages());

        checkSharedContentUsages(failures,
                                 "Fixed Cost",
                                 details,
                                 expectedDocumentStats.getFixedContentUsages(),
                                 dissected.getFixedContentUsages());

        // Check that the dissectable areas are correct.
        for (int i = 0; i < dissectableAreaCount; i += 1) {
            DissectableArea area = dissected.getDissectableArea(i);
            DissectableAreaStats expectedDAStats
                = expectedDocumentStats.getDissectableAreaStats(i);
            checkDissectableArea(failures, details, context, area,
                                 expectedDAStats);
        }

        OutputDocument output = createOutputDocument();
        DissectedContentHandler outputHandler
            = createDissectedContentHandler(output);
        RequestedShards requestedShards = dissected.createRequestedShards();
        dissector.serialize(context, dissected,
                            requestedShards, outputHandler);
        int totalCost = dissected.getTotalCost();
        int outputSize = output.getSize();
        // Check output size is reasonable; we allow explicit output size to
        // be greater than the total cost to handle WBDOM generating WML.
        if (expectedDocumentStats.outputSize > 0) {
            int expectedOutputSize = expectedDocumentStats.outputSize + 
                    expectedDocumentStats.totalCostMimasAdjust;
            // check that
            if (outputSize != expectedOutputSize) {
                failures.append("Output size (" + outputSize
                                + ") is not equal to expected output size ("
                                + expectedOutputSize + ")\n");
            }
        } else {
            if (outputSize > totalCost) {
                failures.append("Output size (" + outputSize
                                + ") is greater than total cost ("
                                + totalCost + ")\n");
            }
        }

        // Make sure that the total cost matches what we have set.
        assertEquals(failures,
                     "Total cost did not match,",
                     expectedDocumentStats.totalCost + 
                        expectedDocumentStats.totalCostMimasAdjust,
                     totalCost);

        // Make sure that the fixed cost matches what we have set.
        int fixedCost = dissected.getFixedCost();
        assertEquals(failures,
                     "Fixed cost did not match,",
                     expectedDocumentStats.fixedCost +
                         expectedDocumentStats.fixedCostMimasAdjust,
                     fixedCost);

//        // Enable this if you want to see the output for all the files :-).
//        assertEquals(failures, "Fail everything: totalCost ", 
//                expectedDocumentStats.totalCost, 0);
        
        if (failures.length() != 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(baos);
            output.write(out);
            failures
                .append("\n====Output Start====\n")
                .append(baos.toString())
                .append("\n====Output End====\n");
            fail("Document failed:\n" + failures);
        }
    }

    protected void appendNodeCountInitCode(StringBuffer buffer,
                                           NodeCounts actualStatistics) {
        buffer.append("NodeCounts counts"
                      + " = new NodeCounts();\n");
        buffer.append("statistics.elementCount = "
                      + actualStatistics.elementCount
                      + ";\n");
        buffer.append("statistics.textCount = "
                      + actualStatistics.textCount
                      + ";\n");
        buffer.append("statistics.dissectableAreaCount = "
                      + actualStatistics.dissectableAreaCount
                      + ";\n");
        buffer.append("statistics.keepTogetherCount = "
                      + actualStatistics.keepTogetherCount
                      + ";\n");
        buffer.append("statistics.shardLinkCount = "
                      + actualStatistics.shardLinkCount
                      + ";\n");
        buffer.append("statistics.shardLinkGroupCount = "
                      + actualStatistics.shardLinkGroupCount
                      + ";\n");
        buffer.append("statistics.shardLinkConditionalCount = "
                      + actualStatistics.shardLinkConditionalCount
                      + ";\n");
        buffer.append("statistics.sharedContentCount = "
                      + actualStatistics.sharedContentCount
                      + ";\n");
    }

    protected void checkSimple1(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 1;

        checkDissectableDocument("simple1.xml", statistics);
    }

    protected void checkSimple2(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 1;
        nodeCounts.textCount = 1;

        checkDissectableDocument("simple2.xml", statistics);
    }

    protected void checkSimple3(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 1;
        nodeCounts.textCount = 1;

        checkDissectableDocument("simple3.xml", statistics);
    }

    protected void checkSimple4(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 3;

        checkDissectableDocument("simple4.xml", statistics);
    }

    protected void checkSimple5(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 2;

        checkDissectableDocument("simple5.xml", statistics);
    }

    /**
     * Test that no empty text nodes are inserted between start element tags.
     */
    protected void checkSimple6(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 1;

        checkDissectableDocument("simple6.xml", statistics);
    }

    /**
     * Test that no empty text nodes are inserted between end element tags.
     */
    protected void checkSimple7(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 1;

        checkDissectableDocument("simple7.xml", statistics);
    }

    /**
     * Test that no empty text nodes are inserted between end element tags.
     */
    protected void checkSimple8(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 3;
        nodeCounts.textCount = 2;

        checkDissectableDocument("simple8.xml", statistics);
    }

    protected void checkComplex1(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 16;
        nodeCounts.textCount = 11;
        nodeCounts.dissectableAreaCount = 1;
        nodeCounts.keepTogetherCount = 1;
        nodeCounts.shardLinkCount = 2;
        nodeCounts.shardLinkGroupCount = 1;
        nodeCounts.shardLinkConditionalCount = 1;
        nodeCounts.sharedContentCount = 3;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addEntity("term");
        usages.addEntity("definition");
        usages.addEntity("some");

        usages = statistics.getFixedContentUsages();
        usages.addEntity("term");
        usages.addEntity("definition");

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 1;

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("some");

        checkDissectableDocument("complex1.xml", statistics);
    }

    protected void checkCommon1(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 1;
        nodeCounts.dissectableAreaCount = 0;
        nodeCounts.keepTogetherCount = 0;
        nodeCounts.shardLinkCount = 0;
        nodeCounts.shardLinkGroupCount = 0;
        nodeCounts.shardLinkConditionalCount = 0;
        nodeCounts.sharedContentCount = 1;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addEntity("sharedContent");

        usages = statistics.getFixedContentUsages();
        usages.addEntity("sharedContent");

        checkDissectableDocument("common1.xml", statistics);
    }

    protected void checkCommon2(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 12;
        nodeCounts.textCount = 9;
        nodeCounts.dissectableAreaCount = 1;
        nodeCounts.keepTogetherCount = 0;
        nodeCounts.shardLinkCount = 2;
        nodeCounts.shardLinkGroupCount = 1;
        nodeCounts.shardLinkConditionalCount = 1;
        nodeCounts.sharedContentCount = 5;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addEntity("dissectableOnly");
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedOnly");
        usages.addEntity("fixedThenDissectable");

        usages = statistics.getFixedContentUsages();
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedOnly");
        usages.addEntity("fixedThenDissectable");

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 1;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("shard");

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableOnly");
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedThenDissectable");

        checkDissectableDocument("common2.xml", statistics);
    }

    protected void checkCommon3(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 20;
        nodeCounts.textCount = 15;
        nodeCounts.dissectableAreaCount = 2;
        nodeCounts.keepTogetherCount = 0;
        nodeCounts.shardLinkCount = 4;
        nodeCounts.shardLinkGroupCount = 2;
        nodeCounts.shardLinkConditionalCount = 2;
        nodeCounts.sharedContentCount = 6;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addEntity("dissectableOnly");
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedOnly");
        usages.addEntity("fixedThenDissectable");

        usages = statistics.getFixedContentUsages();
        usages.addEntity("fixedOnly");
        usages.addEntity("fixedThenDissectable");
        usages.addEntity("dissectableThenFixed");

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 1;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("shard");

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedThenDissectable");

        daStats = statistics.getDissectableAreaStats(1);
        daStats.shardCount = 1;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("page");

        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableOnly");
        usages.addEntity("dissectableThenFixed");
        usages.addEntity("fixedThenDissectable");

        checkDissectableDocument("common3.xml", statistics);
    }

    protected void checkCommon4(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 21;
        nodeCounts.textCount = 16;
        nodeCounts.dissectableAreaCount = 2;
        nodeCounts.keepTogetherCount = 6;
        nodeCounts.shardLinkCount = 4;
        nodeCounts.shardLinkGroupCount = 2;
        nodeCounts.shardLinkConditionalCount = 2;
        nodeCounts.sharedContentCount = 6;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addEntity("fixedOnly");
        usages.addEntity("dissectableOnly");
        usages.addEntity("fixedThenDissectable");
        usages.addEntity("dissectableThenFixed");

        usages = statistics.getFixedContentUsages();
        usages.addEntity("fixedOnly");
        usages.addEntity("fixedThenDissectable");
        usages.addEntity("dissectableThenFixed");

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 3;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("shard");

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("fixedThenDissectable");

        shardStats = daStats.getShardStats(1);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableOnly");

        shardStats = daStats.getShardStats(2);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableThenFixed");

        daStats = statistics.getDissectableAreaStats(1);
        daStats.shardCount = 3;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("page");

        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("fixedThenDissectable");

        shardStats = daStats.getShardStats(1);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableOnly");

        shardStats = daStats.getShardStats(2);
        usages = shardStats.getSharedContentUsages();
        usages.addEntity("dissectableThenFixed");

        checkDissectableDocument("common4.xml", statistics);
    }

    protected void checkCommon5(DocumentStats statistics)
        throws Exception {
        
        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 2;
        nodeCounts.textCount = 1;
        nodeCounts.sharedContentCount = 1;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addElementEntity("e");

        usages = statistics.getFixedContentUsages();
        usages.addElementEntity("e");

        checkDissectableDocument("common5.xml", statistics);
    }
    
    protected void checkCommon6(DocumentStats statistics)
        throws Exception {
        
        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 29;
        nodeCounts.textCount = 15;
        nodeCounts.dissectableAreaCount = 2;
        nodeCounts.keepTogetherCount = 0;
        nodeCounts.shardLinkCount = 4;
        nodeCounts.shardLinkGroupCount = 2;
        nodeCounts.shardLinkConditionalCount = 2;
        nodeCounts.sharedContentCount = 6;

        ExpectedSharedContentUsages usages;

        usages = statistics.getTotalContentUsages();
        usages.addElementEntity("dissectableOnly");
        usages.addElementEntity("dissectableThenFixed");
        usages.addElementEntity("fixedOnly");
        usages.addElementEntity("fixedThenDissectable");

        usages = statistics.getFixedContentUsages();
        usages.addElementEntity("fixedOnly");
        usages.addElementEntity("fixedThenDissectable");
        usages.addElementEntity("dissectableThenFixed");

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 1;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("shard");

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addElementEntity("dissectableThenFixed");
        usages.addElementEntity("fixedThenDissectable");

        daStats = statistics.getDissectableAreaStats(1);
        daStats.shardCount = 1;

        usages = daStats.getOverheadContentUsages();
        usages.addEntity("page");

        shardStats = daStats.getShardStats(0);
        usages = shardStats.getSharedContentUsages();
        usages.addElementEntity("dissectableOnly");
        usages.addElementEntity("dissectableThenFixed");
        usages.addElementEntity("fixedThenDissectable");

        checkDissectableDocument("common6.xml", statistics);
    }
    
    public void testSimple1()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 52;
        statistics.fixedCost = 52;

        checkSimple1(statistics);
    }

    public void testSimple2()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 66;
        statistics.fixedCost = 66;

        statistics.totalCostMimasAdjust -= 1;
        statistics.fixedCostMimasAdjust -= 1;
        checkSimple2(statistics);
    }

    public void testSimple3()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 144;
        statistics.fixedCost = 144;

        statistics.totalCostMimasAdjust -= 1;
        statistics.fixedCostMimasAdjust -= 1;
        checkSimple3(statistics);
    }

    public void testSimple4()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 113;
        statistics.fixedCost = 113;

        statistics.totalCostMimasAdjust -= 1;
        statistics.fixedCostMimasAdjust -= 1;
        checkSimple4(statistics);
    }

    public void testSimple5()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 110;
        statistics.fixedCost = 110;

        statistics.totalCostMimasAdjust -= 1;
        statistics.fixedCostMimasAdjust -= 1;
        checkSimple5(statistics);
    }

    /**
     * Test that no empty text nodes are inserted between start element tags.
     */
    public void testSimple6()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 69;
        statistics.fixedCost = 69;

        checkSimple6(statistics);
    }

    /**
     * Test that no empty text nodes are inserted between end element tags.
     */
    public void testSimple7()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 69;
        statistics.fixedCost = 69;

        checkSimple7(statistics);
    }

    /**
     * Test that no empty text nodes are inserted between end element tags.
     */
    public void testSimple8()
        throws Exception {
        
        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 86;
        statistics.fixedCost = 86;

        checkSimple8(statistics);
    }

    public void testComplex1()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 507;
        statistics.fixedCost = 320;

        checkComplex1(statistics);
    }

    public void testCommon1()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 230;
        statistics.fixedCost = 230;

        statistics.totalCostMimasAdjust -= 13;
        statistics.fixedCostMimasAdjust -= 13;
        checkCommon1(statistics);
    }

    public void testCommon2()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 1088;
        statistics.fixedCost = 650;

        statistics.totalCostMimasAdjust -= 66;
        statistics.fixedCostMimasAdjust -= 30;
        checkCommon2(statistics);
    }

    public void testCommon3()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 1591;
        statistics.fixedCost = 864;

        statistics.totalCostMimasAdjust -= 100;
        statistics.fixedCostMimasAdjust -= 40;
        checkCommon3(statistics);
    }

    public void testCommon4()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 1756;
        statistics.fixedCost = 864;

        statistics.totalCostMimasAdjust -= 136;
        statistics.fixedCostMimasAdjust -= 40;
        checkCommon4(statistics);
    }

    public void testCommon5()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 138;
        statistics.fixedCost = 138;

        // This test is triton onwards only.
        checkCommon5(statistics);
    }

    public void testCommon6()
        throws Exception {

        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 1572;
        statistics.fixedCost = 860;

        // This test is triton onwards only.
        checkCommon6(statistics);
    }

    // todo : check that duplicate dissectable areas are detected.
    //
    // todo : check that missing group, next, and / or previous links are
    // todo : detected properly.
    public void testErrors()
        throws Exception {
    }

    /**
     * Test dissection of strings. 
     * <p>
     * This was hacked in, quickly, by someone other than the original author
     * of this class, well after the above tests were created, and uses a 
     * different methodology to trigger the dissection.
     * <p>
     * AFAIK, the only "common4" actually does dissection, and it achieves 
     * that by using <KEEP-TOGETHER forceBreak...>.
     * <p>
     * This test, however, uses the DocumentStats.pageSize attribute (that
     * was added for it) to trigger the dissection "normally" based on the 
     * output size. This means that, unlike dissection tests that use 
     * KEEP-TOGETHER, subclasses that use different sizing methods *may* need 
     * to override the page size in order to trigger dissection. However,
     * I haven't seemed to need to for the subclasses we have currently.
     * <p>
     * Note: it may be more appropriate to reduce the shard link group down
     * to it's minimum size for this test to avoid having to have such a large
     * input document. This would make it easier to do the sizings. 
     * 
     * @throws Exception
     */ 
    public void testText1() throws Exception {
        DocumentStats statistics = new DocumentStats();
        statistics.totalCost = 593;
        statistics.fixedCost = 52;
        
        int fixedCost = 35;         
        int dissectableCost = 483; // no longer output?
        // With and without shared content, this does not vary
        int dissectableOverhead = 190; // shard link stuff
        int pageSize = fixedCost + dissectableOverhead +
                (dissectableCost / 3);
        
        statistics.pageSize = (560 / 3) + 190 + 50 + 17; // add the overhead

        statistics.totalCostMimasAdjust -= 9;
        statistics.pageSize -= 34 + 3; // (daoverhead 163->129, ~= 9 x 3);
        checkText1(statistics);
    }

    protected void checkText1(DocumentStats statistics)
        throws Exception {

        NodeCounts nodeCounts = statistics.getNodeCounts();
        nodeCounts.elementCount = 6;
        nodeCounts.textCount = 3;
        nodeCounts.dissectableAreaCount = 1;
        nodeCounts.shardLinkCount = 2;
        nodeCounts.shardLinkGroupCount = 1;
        nodeCounts.shardLinkConditionalCount = 1;

        DissectableAreaStats daStats;
        daStats = statistics.getDissectableAreaStats(0);
        daStats.shardCount = 3;

        ShardStats shardStats;
        shardStats = daStats.getShardStats(0);

        checkDissectableDocument("text1.xml", statistics);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/2	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/3	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 10-Jul-03	774/2	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/6	geoff	VBM:2003070703 merge from metis, and rename files manually, and fix up sizes for whitespace differences

 10-Jul-03	770/4	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/4	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/3	geoff	VBM:2003061005 fix a test case failure I forgot

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	309/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
