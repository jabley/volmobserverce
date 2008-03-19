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

<vt:canvas layoutName="tt_portlet" type="portlet" theme="tt_portlet08">

<vt:pane name="test2">
  <vt:p>This is normal text in tt_portlet08.jsp. This portlet has a theme which should be ignored.</vt:p>
  <vt:p styleClass="special1">This is portlet08's special1 text, but it should look like the portal's special1 text.</vt:p>
 <vt:p styleClass="portletspecial">This is portlet08's portletspecial text, but it should look like normal text.</vt:p>

<vt:pre>     theme="tt_portlet08"</vt:pre>
</vt:pane>




</vt:canvas>
