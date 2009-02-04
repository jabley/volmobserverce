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

<vt:usePipeline>
  <pipeline:content>
    <vt:pane name="error">
      p1=<%=request.getParameter("param1")%>
      <vt:br/>
      p2=<%=request.getParameter("param2")%>
      <vt:br/>
      p3=<%=request.getParameter("param3")%>
      <vt:br/>
      <vt:b>Redirected include via a content tag</vt:b>
    </vt:pane>
  </pipeline:content>
</vt:usePipeline>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-03	658/1	philws	VBM:2003062506 Provide includeJSP and includeServlet instead of includeServerResource

 ===========================================================================
--%>