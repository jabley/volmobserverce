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
 % $Header: /src/voyager/webapp/internal/webtest/TableThemes.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Oct-01    Allan           VBM:2001090401 - Created to test table
 %                              tag attributes with theme settings
 % ======================================================================= --%>
 
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="TableTags" theme="TableTags">
<vt:pane name="Page">
<vt:table cols="2">
<vt:tr>
<vt:th colSpan="2" rowSpan="2">
Header:
</vt:th>
</vt:tr>
<vt:tr>
<vt:td>
</vt:td>
</vt:tr>
<vt:tr>
<vt:td colSpan="2" rowSpan="2">
TD 
</vt:td>
</vt:tr>
</vt:table>
</vt:pane>
</vt:canvas>
