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
<%@ include file="VolantisNoError-mcs.jsp" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %>


<%
  String portlet2brand = "";
  if (request.getParameter ("portlet2brand") != null) {
  portlet2brand = request.getParameter ("portlet2brand");
  } else {
  portlet2brand = "";
  }

%>

<vt:canvas layoutName="^tt_default" type="portlet">

<vt:pane name="fileinfo">
  <vt:h1>Portlet 2 with no brand attribute</vt:h1>
</vt:pane>

<vt:pane name="test">
<vt:p>
<%
  String pattern = new String("The portlet 2 canvas has no brand attribute");
  Object [] args = new Object [] {portlet2brand};
  out.print (java.text.MessageFormat.format (pattern, args));
%>
  </vt:p>
  <vt:p>
  Portlet 2 Result 1: <vt:img src="tt_mtsbrandlogo" altText="The MTS brand logo should be here."/>
  <vt:br/>
  Portlet 2 Result 2: <vt:script src="{tt_brandscript}"/>
  <vt:br/>
  Portlet 2 Result 3: <vt:a href="{tt_brandlink}">Hover over this link to check the  url in the browser status bar.</vt:a>
  </vt:p>
  
</vt:pane>

</vt:canvas>

