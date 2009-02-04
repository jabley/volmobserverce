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
<vt:canvas layoutName="error">
<vt:h2 pane="error">The form reported the following fields</vt:h2> 
<%
String outline;
String headerValues;
String queryString;

queryString = "<b>Query:</b> " + request.getQueryString();
%>
    <vt:p pane="error"><%=queryString%></vt:p>
<%
for (Enumeration e = request.getHeaderNames() ; e.hasMoreElements(); ) {
    String headerName = (String) e.nextElement();
    outline = "<b>" + headerName + " </b>has values ";
%>
    <vt:p pane="error"><%=outline%></vt:p>
<%
    for (Enumeration f = request.getHeaders(headerName) ; f.hasMoreElements(); ) {
      headerValues = (String) f.nextElement();
%>
      <vt:p pane="error"><%=headerValues%></vt:p>
<%
    }
}
%>
</vt:canvas>

