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

<vt:canvas layoutName="tt_portlet" type="portlet">

<vt:pane name="test1">
  <vt:p>This text is in tt_portlet04.jsp (pane test1)</vt:p>
</vt:pane>

<vt:pane name="test2">
  <vt:p>This text is in tt_portlet04.jsp (pane test2)</vt:p>
</vt:pane>

<vt:pane name="test3">
  <vt:p>This text is in tt_portlet04.jsp (pane test3)</vt:p>
</vt:pane>

<vt:region name="NestedRegion1">
 <jsp:include page="tt_portlet05.jsp"/>
</vt:region>

<vt:region name="NestedRegion2">
 <jsp:include page="tt_portlet06.jsp"/>
</vt:region>

<vt:region name="NestedRegion3">
 <jsp:include page="tt_portlet07.jsp"/>
</vt:region>



</vt:canvas>
