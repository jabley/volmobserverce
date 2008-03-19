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
<!--(c) Volantis Systems Ltd 2002. -->
<%@ page import="com.volantis.mcs.utilities.*"
                isErrorPage="true" %>
<%-- Include the initialization JSP page. --%>
<%@ include file="MarinerMCSInitialize.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs/volantis-mcs.tld" prefix="vt" %>

<%
  org.apache.log4j.Category logger
    = org.apache.log4j.Category.getInstance (page.getClass ());
  logger.error ("Exception in page", exception);
%>

<vt:canvas layoutName="/error.mlyt" pageUsage="error">
<vt:h1 pane="error">Sorry!</vt:h1>
<vt:p pane="error">Due to an internal processing problem, we're currently
unable to carry out your request.</vt:p>
<vt:p pane="error">Our staff have been made aware of the problem and we hope to
correct the situation shortly.</vt:p>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Jul-04	4972/3	adrianj	VBM:2004063013 Fixes to VolantisErrorPage.jsp initialisation method

 28-Jul-04	4972/1	adrianj	VBM:2004063013 Fixes to VolantisErrorPage.jsp initialisation method

 ===========================================================================
--%>
