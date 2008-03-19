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
package com.volantis.xml.pipeline.sax.tryop;

import com.volantis.xml.pipeline.sax.CompositeXMLPipelineException;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dependency.DependencyTestRuleConfigurator;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;

/**
 * This class tests error recovery in the pipeline.
 */
public class TryOperationTestCase extends PipelineTestAbstract {

    protected void configureDynamicProcess(
            DynamicProcessConfiguration configuration) {
        super.configureDynamicProcess(configuration);

        DependencyTestRuleConfigurator.getDefaultInstance()
                .configure(configuration);
    }

    /**
     * The configuration used by this test case.
     */
    private CacheProcessConfiguration cpc;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        cpc = new CacheProcessConfiguration();
    }

    // javadoc inherited
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        final XMLPipelineConfiguration config =
                super.createPipelineConfiguration();
        config.storeConfiguration(CacheProcessConfiguration.class, cpc);
        return config;
    }


    /**
     * <try>
     * <preferred>
     * <fetch .../> FAIL
     * <failOnExecute/>
     * </preferred>
     * <alternative>
     * <content .../> SUCCESS
     * </alternative>
     * </try>
     */
    public void testFetchFailFirst() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "FetchFailFirst.input.xml", "FetchFailFirst.expected.xml");
    }

    /**
     * <try>
     * <preferred>
     * <content .../> SUCCESS
     * </preferred>
     * <alternative>
     * <fetch .../> WOULD FAIL BUT NEVER REACHED.
     * </alternative>
     * </try>
     */
    public void testFetchFailSecond() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "FetchFailSecond.input.xml", "FetchFail.expected.xml");
    }

    /**
     * <try>
     * <preferred>
     * <fetch .../> FAIL
     * </preferred>
     * <alternative>
     * <fetch .../> FAIL
     * </alternative>
     * <alternative>
     * <fetch .../> FAIL
     * </alternative>
     * <alternative>
     * <fetch .../> FAIL
     * </alternative>
     * <alternative>
     * <fetch .../> FAIL
     * </alternative>
     * <alternative>
     * <content .../> SUCCESS
     * </alternative>
     * </try>
     */
    public void testMultipleFetchFail() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "MultipleFetchFail.input.xml", "FetchFail.expected.xml");
    }

    /**
     * <try>
     * <preferred>
     * <fetch .../> FAIL
     * </preferred>
     * <alternative>
     * <fetch .../> FAIL
     * </alternative>
     * </try>
     */
    public void testAllFail() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                    "AllFetchFail.input.xml", "FetchFail.expected.xml");
            fail("Exception should have been thrown.");
        } catch (CompositeXMLPipelineException composite) {
        }
    }

    /**
     * <try>
     * <preferred>
     * <try>
     * <preferred>
     * <error/>
     * </preferred>
     * <alternative>
     * <succeed/>
     * </alternative>
     * </try>
     * </preferred>
     * <alternative>
     * <try>
     * <preferred>
     * <assert-fail/>
     * </preferred>
     * <alternative>
     * <assert-fail/>
     * </alternative>
     * </try>
     * </alternative>
     * </try>
     */
    public void testNestedTryFirstSucceeds() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "NestedTryFirstSucceeds.input.xml", "NestedTry.expected.xml");
    }

    /**
     * <try>
     * <preferred>
     * <try>
     * <preferred>
     * <error/>
     * </preferred>
     * <alternative>
     * <error/>
     * </alternative>
     * </try>
     * </preferred>
     * <alternative>
     * <try>
     * <preferred>
     * <succeed/>
     * </preferred>
     * <alternative>
     * <assert-error/>
     * </alternative>
     * </try>
     * </alternative>
     * </try>
     */
    public void testNestedTrySecondSucceeds() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "NestedTrySecondSucceeds.input.xml", "NestedTry.expected.xml");
    }

    /**
     * Tests that the template apply process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testTemplateApplyCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "template/ApplyFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the template binding process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testTemplateBindingCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "template/BindingFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the template parameter process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testTemplateParameterCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "template/ParameterFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the web driver process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testWebDriverPostCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "drivers/web/WebDriverPostFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the fetch uri process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testFetchCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "urid/FetchFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the sql query process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testSQLQueryCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "sqlconnector/SQLQueryFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the transform process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testTransformCanBeRecovered() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "transform/SimpleTransformFailure.input.xml",
                "StandardAlternative.expected.xml");
    }

    /**
     * Tests that the cache process behaves correctly inside a
     * try operation
     *
     * @throws Exception if an error occurs
     */
    public void testCacheCanBeRecovered() throws Exception {
        cpc.createCache("myCache", "1000", "0");

        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "cache/CacheFailure.input.xml",
                "cache/CacheFailure.expected.xml");
    }

    public void testFetchXMLParseFail() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "FetchXMLParseFail.input.xml", "FetchFail.expected.xml");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 19-Dec-03	489/2	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/
