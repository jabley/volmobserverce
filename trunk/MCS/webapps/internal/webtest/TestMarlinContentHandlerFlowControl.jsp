<!--
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
-->
<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext,
                 com.volantis.mcs.utilities.Volantis,
                 com.volantis.mcs.context.ContextInternals,
                 com.volantis.mcs.context.MarinerPageContext,
                 com.volantis.mcs.marlin.sax.MarlinSAXHelper,
                 com.volantis.mcs.runtime.pipeline.PipelineInitialization,
                 com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule,
                 com.volantis.xml.pipeline.sax.dynamic.DynamicProcess,
                 com.volantis.xml.namespace.ExpandedName,
                 org.xml.sax.Attributes,
                 org.xml.sax.SAXException,
                 com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration,
                 com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration,
                 com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet,
                 com.volantis.xml.pipeline.Namespace,
                 java.io.Writer" %>


<%
    MarinerJspRequestContext marinerJspRequestContext =
        new MarinerJspRequestContext(pageContext);

    MarinerPageContext marinerPageContext =
            ContextInternals.getMarinerPageContext(marinerJspRequestContext);

    Volantis volantis = marinerPageContext.getVolantisBean();
    
    PipelineInitialization pipelineInitialization =
            volantis.getPipelineInitialization();
    
    // register this rule 
    XMLPipelineConfiguration configuration 
            = pipelineInitialization.getPipelineConfiguration();
    
    DynamicProcessConfiguration dynamicConfiguration = 
            (DynamicProcessConfiguration)configuration.retrieveConfiguration(
                    DynamicProcessConfiguration.class);
    
    NamespaceRuleSet ruleSet = dynamicConfiguration.getNamespaceRules(
            Namespace.PIPELINE.getURI());
    
    // add a rule that will throw an exception if invoked. This can be used to
    // test if the pipeline markup is indeed being suppressed
    ruleSet.addRule("failIfInvoked",
                    new DynamicElementRule() {
                        // javadoc inherited
                        public Object startElement(
                                DynamicProcess dynamicProcess, 
                                ExpandedName expandedName, 
                                Attributes attributes) throws SAXException {                        
                            if (true) {
                                throw new IllegalStateException(
                                        "Pipeline Rule Was executed");
                            }
                            return null;
                        }

                        public void endElement(DynamicProcess dynamicProcess, 
                                               ExpandedName expandedName, 
                                               Object o) throws SAXException {
                            if (true) {
                                throw new IllegalStateException(
                                        "Pipeline Rule Was executed");
                            }
                        }
                    });
    
    String systemId =
            "http://localhost:8080/volantis/marlin/PipelineFlowControl.xml";
    try {
        MarlinSAXHelper.parse(marinerJspRequestContext, null, systemId);
    } finally {
      marinerJspRequestContext.release();
    }
  
%>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Aug-03	1048/1	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 ===========================================================================
--%>
