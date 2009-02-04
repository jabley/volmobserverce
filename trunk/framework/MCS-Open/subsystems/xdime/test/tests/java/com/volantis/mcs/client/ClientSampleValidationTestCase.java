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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.client;

import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;
import com.volantis.mcs.xdime.schema.XDIME2Elements;
import com.volantis.mcs.xdime.validation.XDIME2CompiledSchema;
import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.drivers.uri.URIDriverFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.template.TemplateFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.xml.sax.XMLFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

/**
 * Test cases for validating the sample client files.
 */
public class ClientSampleValidationTestCase
        extends TestCase {

    public static Test suite() throws IOException {

        URL url = ClientSampleValidationTestCase.class.getResource(".");
        String externalForm = url.toExternalForm();
        if (!externalForm.startsWith("file:")) {
            return null;
        }

        int index = externalForm.lastIndexOf("built/subsystems");
        if (index == -1) {
            return null;
        }

        File file = new File(externalForm.substring(5, index));
        File appDir = new File(file, "webapps/mcs/projects/client-app");
        File[] xdimeFiles = appDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xdime");
            }
        });

        TestSuite suite = new TestSuite();
        for (int i = 0; i < xdimeFiles.length; i++) {
            File xdimeFile = xdimeFiles[i];
            suite.addTest(new ClientSuccessfulTest(
                    xdimeFile.getName(), xdimeFile.toURL()));
        }

        return suite;
    }

    private static class ClientSuccessfulTest
            extends XDIMEValidationTestAbstract {

        private final URL url;

        public ClientSuccessfulTest(String name, URL url) {
            this.url = url;
            setName(name);
        }

        protected SchemaNamespaces getSchemaNamespaces() {
            return XDIME2Elements.getDefaultInstance();
        }

        protected CompiledSchema getCompiledSchema() {
            return XDIME2CompiledSchema.getCompiledSchema();
        }


        protected XMLFilter createFilter() throws Exception {

            XMLPipelineFactory factory =
                    XMLPipelineFactory.getDefaultInstance();
            XMLPipelineConfiguration configuration =
                    factory.createPipelineConfiguration();
            DynamicProcessConfiguration dynamic =
                    factory.createDynamicProcessConfiguration();
            configuration.storeConfiguration(DynamicProcessConfiguration.class,
                    dynamic);
            TemplateFactory templateFactory =
                    TemplateFactory.getDefaultInstance();
            templateFactory.getRuleConfigurator().configure(dynamic);
            URIDriverFactory uriDriverFactory =
                    URIDriverFactory.getDefaultInstance();
            uriDriverFactory.getRuleConfigurator().configure(dynamic);

            XMLPipelineContext context = factory.createPipelineContext(
                    configuration, (EnvironmentInteraction) null);
            XMLPipeline pipeline = factory.createDynamicPipeline(context);
            return factory.createPipelineFilter(pipeline);
        }

        public void runBare()
                throws Exception {
                checkValidationFromFile(url);
        }
    }

}
