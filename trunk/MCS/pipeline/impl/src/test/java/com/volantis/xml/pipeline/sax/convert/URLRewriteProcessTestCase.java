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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.synergetics.url.URLPrefixRewriteOperation;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.NamespaceContentWriter;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Writer;

/**
 * Test case for URLRewriteProcess
 */
public class URLRewriteProcessTestCase extends PipelineTestAbstract {

    /**
     * Maintain an XMLPipelineConfiguration that we can change on a per-test
     * basis if we need to and still have callers who do not need a
     * specific configuration work as normal.
     */
    private XMLPipelineConfiguration privateConfiguration;

    /**
     * Override this method in order to preserve the namespace markup in the
     * generated output.
     *
     * @param  writer the writer.
     * @return        a new DefaultHandler that includes the namespace values
     *                in the output.
     */
    protected ContentHandler createContentHandler(Writer writer) {

        return new NamespaceContentWriter(writer) {
            // javadoc inherited from superclass
            public void characters(char buf[], int offset, int len) throws SAXException {
                String s = new String(buf, offset, len);
                write(s.trim());
            }

            protected void writeAttributes(Attributes attrs) throws SAXException {
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aName = attrs.getLocalName(i);
                        if ("".equals(aName) || (attrs.getQName(i) != null)) {
                            aName = attrs.getQName(i);
                        }
                        write(" " + aName + "=\"" + attrs.getValue(i) + "\"");
                    }
                }
            }
        };
    }

    /**
     * Construct a new instance of this test case.
     * @param name The name of this test.
     */
    public URLRewriteProcessTestCase(String name) {
        super(name);
    }

    /**
     * Test that sets of url prefixes are added as necessary. This
     * demonstrates the relative-to-absolute capability of
     * URLRewriteProcess.
     */
    public void testURLPrefixAdd() throws Exception {

        URLRewriteProcessConfiguration configuration =
                new URLRewriteProcessConfiguration();

        ConverterConfiguration convertConfig =
                configuration.getConverterConfiguration();

        ConverterTuple[] tuples = {
                   new ConverterTuple(null, "a", "href"),
                   new ConverterTuple(null, "form", "action"),
                   new ConverterTuple(
                       "http://www.volantis.com/tuple", "img", "src")
               };
        convertConfig.setTuples(tuples);

        URLPrefixRewriteManager rewriteManager =
                configuration.getURLPrefixRewriteManager();
        rewriteManager.addRewritableURLPrefix(null,
                "http://volantis.com/dsb/BBCNews",
                URLPrefixRewriteOperation.ADD_PREFIX);

        privateConfiguration = createURLRewriterConfiguration();

        privateConfiguration.
                storeConfiguration(URLRewriteProcessConfiguration.class,
                        configuration);

        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "URLRewriteProcessRelativeToAbsoluteTestCase.input.xml",
                "URLRewriteProcessRelativeToAbsoluteTestCase.expected.xml");
    }

    /**
     * Test that sets of url prefixes are removed as necessary. This
     * demonstrates the absolute-to-relative capability of
     * URLRewriteProcess.
     */
    public void testURLPrefixRemove() throws Exception {

        URLRewriteProcessConfiguration configuration =
                new URLRewriteProcessConfiguration();

        ConverterConfiguration convertConfig =
                configuration.getConverterConfiguration();

        ConverterTuple[] tuples = {
                   new ConverterTuple(null, "a", "href"),
                   new ConverterTuple(null, "form", "action"),
                   new ConverterTuple(
                       "http://www.volantis.com/tuple", "img", "src")
               };
        convertConfig.setTuples(tuples);

        URLPrefixRewriteManager rewriteManager =
                configuration.getURLPrefixRewriteManager();
        rewriteManager.addRewritableURLPrefix("http://www.bbc.co.uk/news/",
                null,
                URLPrefixRewriteOperation.REMOVE_PREFIX);
        rewriteManager.addRewritableURLPrefix("http://www.cnn.com/news/",
                null,
                URLPrefixRewriteOperation.REMOVE_PREFIX);

        privateConfiguration = createURLRewriterConfiguration();

        privateConfiguration.
                storeConfiguration(URLRewriteProcessConfiguration.class,
                        configuration);

        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "URLRewriteProcessAbsoluteToRelativeTestCase.input.xml",
                "URLRewriteProcessAbsoluteToRelativeTestCase.expected.xml");
    }
    /**
     * Test that sets of url prefixes are replaced by another set as
     * necessary.
     */
    public void testURLPrefixReplacement() throws Exception {

        URLRewriteProcessConfiguration configuration =
                new URLRewriteProcessConfiguration();

        ConverterConfiguration convertConfig =
                configuration.getConverterConfiguration();

        ConverterTuple[] tuples = {
                   new ConverterTuple(null, "a", "href"),
                   new ConverterTuple(null, "form", "action"),
                   new ConverterTuple(
                       "http://www.volantis.com/tuple", "img", "src")
               };
        convertConfig.setTuples(tuples);

        URLPrefixRewriteManager rewriteManager =
                configuration.getURLPrefixRewriteManager();
        rewriteManager.addRewritableURLPrefix("http://www.bbc.co.uk/news",
                "http://volantis.com/dsb/BBCNews",
                URLPrefixRewriteOperation.REPLACE_PREFIX);
        rewriteManager.addRewritableURLPrefix("http://www.cnn.com/news",
                "http://volantis.com/dsb/CNNNews",
                URLPrefixRewriteOperation.REPLACE_PREFIX);

        privateConfiguration = createURLRewriterConfiguration();

        privateConfiguration.
                storeConfiguration(URLRewriteProcessConfiguration.class,
                        configuration);

        doTest(new IntegrationTestHelper().getPipelineFactory(),
                "URLRewriteProcessReplacementTestCase.input.xml",
                "URLRewriteProcessReplacementTestCase.expected.xml");
    }

    // javadoc inherited
    protected synchronized
            XMLPipelineConfiguration createPipelineConfiguration() {

        XMLPipelineConfiguration configuration = privateConfiguration;
        if (configuration == null) {
            configuration = super.createPipelineConfiguration();
        }
        return configuration;
    }

    private XMLPipelineConfiguration createURLRewriterConfiguration() {

        // Get hold of default configuration
        XMLPipelineConfiguration configuration =
                super.createPipelineConfiguration();

        DynamicProcessConfiguration dynamicConfiguration =
                (DynamicProcessConfiguration)
                configuration.retrieveConfiguration(DynamicProcessConfiguration.class);

        NamespaceRuleSet ruleSet = dynamicConfiguration.getNamespaceRules(
                Namespace.PIPELINE.getURI(), true);

        ruleSet.addRule("urlRewriter",
                new AbstractAddProcessRule() {
                    protected XMLProcess createProcess(DynamicProcess dynamicProcess,
                                                       ExpandedName elementName,
                                                       Attributes attributes)
                            throws SAXException {
                        return new URLRewriteProcess();
                    }
                });

        return configuration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 26-May-04	708/1	allan	VBM:2004052102 Provide a URL rewriting process.

 ===========================================================================
*/
