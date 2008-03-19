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
<%-- ==========================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ======================================================================= --%>

<%@ page import="com.volantis.mcs.servlet.MarinerServletApplication,
                 com.volantis.mcs.utilities.Volantis,
                 com.volantis.mcs.application.ApplicationInternals,
                 com.volantis.mcs.runtime.configuration.XMLProcessConfigurations,
                 com.volantis.xml.pipeline.sax.wsdriver.WSDriverConfiguration,
                 com.volantis.xml.pipeline.sax.wsdriver.WSDLEntry"%>
<%@ include file="Volantis-mcs.jsp" %>

 <%
     // Get the volantis bean.
     Volantis volantis = ApplicationInternals.getVolantisBean (marinerApplication);
     XMLProcessConfigurations xmlCfgs = volantis.getPipelineProcessConfigurations();

     final WSDriverConfiguration wsDriverConfiguration = xmlCfgs.getWsDriverConfiguration();
     if (wsDriverConfiguration == null) {
         out.println("<h1>No web service configurations found</h1>");
     } else {
         out.println("<h1>Web service configurations found!</h1>");
         String url = "/";
         final WSDLEntry wsdlEntry = wsDriverConfiguration.getWSDLCatalog().retrieveWSDLEntry(url);
         if (wsdlEntry != null) {
             out.println("WSDL entry has uri: " + wsdlEntry.getURI());
             out.println("Alternate input source: " + wsdlEntry.provideAlternativeInputSource());
         } else {
             out.println("Could not find wsdl entry with url: " + url);
         }
     }
 %>



<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 ===========================================================================
--%>
