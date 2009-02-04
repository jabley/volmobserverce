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
<%-- --------------------------------------------------------------------------
 % (c) Volantis Systems Ltd 2005. 
 % ----------------------------------------------------------------------- --%>

<%@ page isErrorPage="true" %>

<%@ page contentType="x-application/vnd.xdime+xml" %>

<%-- Include the initialization JSP page. --%>
<%@ include file="MarinerMCSInitialize.jsp" %>

<%-- This is a sample error page and is customizable by customers from this
     comment down. --%>

<%
    // Log the exception. Note that this is intentionally not localized
    // since this is just a sample page.
    org.apache.log4j.Category logger =
            org.apache.log4j.Category.getInstance(page.getClass());
    logger.error("Exception in page", exception);
%>

<canvas layoutName="/error.mlyt" pageUsage="error">
    <h1 pane="error">Sorry!</h1>
    <p pane="error">Due to an internal processing problem, we're currently
unable to carry out your request.</p>
    <p pane="error">Our staff have been made aware of the problem and we hope to
correct the situation shortly.</p>
</canvas>
