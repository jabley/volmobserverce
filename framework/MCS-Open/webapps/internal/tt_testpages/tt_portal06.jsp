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

<vt:pane name="fileinfo">
  <vt:h1>Portal Test 2</vt:h1>
  <vt:p>Filename: tt_portal02.jsp<vt:br/>
  	Layout: tt_portal<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Test that all valid canvas types can be included in a portal.</vt:p>
  <vt:pre>
     canvas       portal
     region       name
  </vt:pre>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All except VoiceXML.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>This portal contains three regions. 
	Each region contains a portlet page.
	You should see text from all three portlet pages.
  </vt:p>
  <vt:p>Device Dependant: <vt:br/>
	none
  </vt:p>
</vt:pane>


<vt:region name="regionthatdontexist">
 <jsp:include page="pagethatdontexist.jsp"/>
</vt:region>

<vt:region name="Region1">
 <jsp:include page="tt_portlet01.jsp"/>
</vt:region>

<vt:region name="Region2">
 <jsp:include page="tt_portlet10.jsp"/>
</vt:region>

<vt:region name="Region3">
 <jsp:include page="tt_portlet03.jsp"/>
</vt:region>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_portals.jsp">Portals Index</vt:a>
  </vt:p>
</vt:pane>

</vt:canvas>
