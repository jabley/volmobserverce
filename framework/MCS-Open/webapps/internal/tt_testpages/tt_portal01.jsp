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
	MJ	26/11/2001	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="tt_portal" type="portal">

<vt:pane name="header">
<vt:h3>This is a Portal page. This is the Portal Header pane.</vt:h3>
  <vt:p>This is to test whether a portal layout will leave whitespace
	by generating unnecessary table cells.</vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_portals.jsp">Portals Index</vt:a>
  </vt:p>
</vt:pane>


</vt:canvas>
