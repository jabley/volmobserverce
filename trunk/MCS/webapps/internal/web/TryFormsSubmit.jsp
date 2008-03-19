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
<%@ page import="com.volantis.mcs.utilities.*,java.sql.*,java.util.*,java.net.*" %>
<html>
<head>
    <link rel=stylesheet href=volantis2.css>
    <title>Report Forms Fields</title>
</head>
<body>
<%
out.println("<h2>Here are the parameters to the form</h2>");
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements(); ) {
    String name = (String) e.nextElement();
    String value[] = request.getParameterValues(name); 
    out.println("<p><b>" + name + " </b>has value " + value[0] + "</p>");
}
%>
</body>
</html>
