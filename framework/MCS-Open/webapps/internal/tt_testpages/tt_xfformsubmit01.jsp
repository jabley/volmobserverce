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
<!--  *************************************************************************
	(c) Volantis Systems Ltd 2001. 
      *************************************************************************
	Revision Info 
	Name  	Date  		Comment
	MJ	15/11/2001	Created this file.

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 

<vt:canvas layoutName="tt_default">

<vt:pane name="header">
 <vt:p>	This page is not intended to be accessed directly.
	It is called from tt_xfform pages and returns confirmation
	of the drink selection.
 </vt:p>
</vt:pane>


<vt:p pane="test">
<%
  String drink = marinerRequestContext.getParameter ("drink");
  String pattern = new String("You selected {0}.   ");
  Object [] args = new Object [] {drink};
  out.print (java.text.MessageFormat.format (pattern, args));
%>
  <vt:br/>

  Goodbye.
</vt:p>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_forms.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>


</vt:canvas>
