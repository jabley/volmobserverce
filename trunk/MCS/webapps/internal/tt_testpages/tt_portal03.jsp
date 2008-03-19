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
  <vt:h1>Portal Test 3</vt:h1>
  <vt:p>Filename: tt_portal03.jsp<vt:br/>
  	Layout: tt_portal<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Test that nested portlets work.</vt:p>
  <vt:pre>
     canvas       portal
     region       name
  </vt:pre>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All except VoiceXML.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>This portal contains three regions. 
	You should see tt_portlet2.jsp. 
	Below that tt_portlet04.jsp, and tt_portlet03.jsp.<vt:br/>
	Note that tt_portlet04.jsp contains three normal panes and
	three nested regions.</vt:p>
  <vt:p>Ensure you can see text in the nested regions 
	tt_portlet05.jsp, tt_portlet06.jsp and tt_portlet07.jsp.</vt:p>
  <vt:p>Device Dependant: <vt:br/>
	none
  </vt:p>
</vt:pane>


<vt:region name="regionthatdontexist">
 <jsp:include page="pagethatdontexist.jsp"/>
</vt:region>

<vt:region name="Region1">
 <jsp:include page="tt_portlet02.jsp"/>
</vt:region>

<vt:region name="Region2">
 <jsp:include page="tt_portlet04.jsp"/>
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
