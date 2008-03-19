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
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 -->
<%@ include file="../Volantis-mcs.jsp" %>
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext, 
	com.volantis.xml.expression.ExpressionContext, 
	com.volantis.mcs.jsp.ExpressionSupport,
	org.xml.sax.XMLReader,
    com.volantis.mcs.marlin.sax.MarlinSAXHelper" %> 
    
<%
  String systemId = "http://localhost:8080/volantis/TemplateWithFunctions.xml";
%>

<%@ include file="MarlinPipelineParse.jsp" %>


<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Jul-03	828/2	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 ===========================================================================
--%>
