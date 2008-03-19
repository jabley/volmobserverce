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
 % $Header: /src/voyager/webapp/internal/webtest/MamlFragments.jsp,v 1.1 2002/03/19 13:54:48 sfound Exp $
 % ----------------------------------------------------------------------------
 % (c) Volantis Systems Ltd 2001. 
 % ----------------------------------------------------------------------------
 % Change History:
 %
 % Date         Who             Description
 % ---------    --------------- -----------------------------------------------
 % 19-mar-02    Steve            VBM:20021105 Invoke maml version of
 %                               fragments test.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.context.ContextInternals" %> 
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %> 
<%@ page import="com.volantis.mcs.maml.MamlSAXParser" %> 

<%
  MarinerJspRequestContext jspContext = MarinerJspRequestContext.getCurrent( pageContext );
  if( jspContext == null )
  {
	jspContext = new MarinerJspRequestContext( pageContext );
  }
  MamlSAXParser parser = new MamlSAXParser();
  parser.setRequestContext( jspContext );
  parser.parse( "http://localhost:8080/volantis/maml/fragments.xml" );
 jspContext.release();
%>


