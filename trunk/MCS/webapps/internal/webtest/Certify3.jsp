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
<%@ page import="com.volantis.mcs.styles.*" %>
<%@ page import="com.volantis.mcs.context.*, com.volantis.mcs.repository.RepositoryConnection" %>

<vt:canvas layoutName="error" theme="Certify3">
<vt:pane name="error">

<vt:p>Some text without edge decoration, for reference</vt:p>

<vt:p styleClass="border">All the border-related attributes have been set in this paragraph</vt:p>

<vt:p styleClass="margin">All the margin-related attributes have been set in this paragraph</vt:p>

<vt:p styleClass="padding">All the padding-related attributes have been set in this paragraph</vt:p>

<vt:chart data="10 20 30 40 50 60 70 80 90" name="Certify3"/>

<vt:mmflash name="snake" menu="true"/>

<vt:p>

<%
MarinerPageContext mc = (MarinerPageContext)pageContext.getAttribute("volantis.context");
Theme th = mc.getTheme();
RepositoryConnection conn = volantis.getConnection();
out.println("<b>The Stylesheet used fo the stylistic information was: </b><br/>");
out.println(th.outputStyleSheet(conn,volantis,request));
%>

<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Certification.jsp">Back</vt:a>

</vt:p>

</vt:pane>
</vt:canvas>

