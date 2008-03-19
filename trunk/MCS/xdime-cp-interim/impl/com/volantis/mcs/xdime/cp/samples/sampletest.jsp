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
<!--
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2004. 
 ! ============================================================================
 -->
<%@ include file="../../VolantisNoError-mcs.jsp" %>

<%@ page import="java.io.PrintWriter, org.xml.sax.*,
                 com.volantis.mcs.jsp.MarinerJspRequestContext,
                 com.volantis.mcs.marlin.sax.MarlinSAXHelper,
                 org.apache.xml.serialize.XMLSerializer,
                 java.io.StringWriter,
                 org.apache.xml.serialize.OutputFormat,
                 java.io.StringReader" %>

<%
    String systemId = application.getResource("/xdime-cp/samples/sampleloader.xml").toString();

    MarinerJspRequestContext jspContext
        = new MarinerJspRequestContext (pageContext);
    try {

        // Process the XDIME CP and serialize the result into a StringBuffer.
        XMLReader xmlReader = MarlinSAXHelper.getXMLReader(jspContext);

        StringWriter stringWriter = new StringWriter();
        XMLSerializer serializer
                = new XMLSerializer(stringWriter, new OutputFormat());
        xmlReader.setContentHandler(serializer.asContentHandler());

        xmlReader.parse(systemId);

        // Print the XDIME contents as a debugging aid.
        System.out.println("XDIME is " + stringWriter.toString());

        // Parse the resulting string as XDIME.
        StringReader stringReader = new StringReader(stringWriter.toString());
        InputSource source = new InputSource(stringReader);
        xmlReader = MarlinSAXHelper.getXMLReader(jspContext);
        xmlReader.setContentHandler(MarlinSAXHelper.getContentHandler(jspContext));
        xmlReader.setErrorHandler(MarlinSAXHelper.getDefaultErrorHandler());
        xmlReader.parse(source);
    }
    finally {
        jspContext.release ();
    }
%>


<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/1	pduffin	VBM:2004060306 Integrated and produced distribution

 ===========================================================================
--%>
