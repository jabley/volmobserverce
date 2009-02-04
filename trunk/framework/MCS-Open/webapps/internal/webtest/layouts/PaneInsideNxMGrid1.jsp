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
 % $Header: /src/voyager/webapp/internal/webtest/layouts/PaneInsideNxMGrid1.jsp,v 1.1 2002/04/26 15:20:15 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 26-Apr-02    Paul            VBM:2002042205 - Created to verify correctness
 %                              of changes made for this task.
 % ======================================================================= --%>

<%@ include file="/Volantis-mcs.jsp" %>

<vt:canvas layoutName="PaneInsideNxMGrid1"
           pageTitle="Pane Inside NxM Grid Test 1 (Background Color)">

<vt:p pane="Pane">
This page was created to test the mark up which is generated for a pane
which is inside an NxM grid and which has a background colour
set.
</vt:p>

<%
  for (int pane = 1; pane < 5; pane += 1) {
%>
<vt:p pane="<%=\"Pane\" + pane%>">
Some text inside pane <%=pane%>
</vt:p>
<%
  }
%>

</vt:canvas>
