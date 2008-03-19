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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/TableTags.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Oct-01    Allan           VBM:2001090401 - Created to test table
 %                              tag attributes on the tag overidding theme.
 % ======================================================================= --%>
 
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="TableTags" theme="TableTags">
<vt:pane name="Page">
<vt:table cols="2">
<vt:tr>
<vt:th align="right" valign="top" bgColor="red" height="42" width="42" colSpan="2" rowSpan="2">
Header: align=right, valign=top, bgColor=red, height=42, widht=52, colSpan=2, rowSpan=2 noWrap
</vt:th>
</vt:tr>
<vt:tr>
<vt:td>
</vt:td>
</vt:tr>
<vt:tr align="left" valign="top" bgColor="white">
<vt:td align="right" bgColor="blue" valign="middle" height="42" width="42" colSpan="2" rowSpan="2">
TD align="right" valign="middle" bgColor="blue" height="42" width="42" colSpan="2" rowSpan="2"
</vt:td>
</vt:tr>
</vt:table>
</vt:pane>
</vt:canvas>
