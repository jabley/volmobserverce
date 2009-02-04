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
 % $Header: /src/voyager/webapp/internal/webtest/FragLinkOrder.jsp,v 1.2 2003/03/14 16:37:28 doug Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 14-Mar-03    Doug            VBM:2003030409 - Created to test ordering of
 %                              of a fragments parent and peer links.
 % ======================================================================= --%>
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="FragLinkOrder" pageTitle="Fragments Test">

<vt:p pane="pane1">
filename: FragLinkOrdering.jsp<vt:br/><vt:br/>
<vt:a href="FragLinkOrder.jsp">Layouts Index</vt:a>
<vt:br/>
<vt:a href="index.jsp">Main Index</vt:a><vt:br/><vt:br/>

</vt:p>

<vt:p pane="11">
Fragment 1, pane 1
</vt:p>

<vt:p pane="12">
Fragment 1, pane 2
</vt:p>

<vt:p pane="10">
This fragment should have 2 children, 3 peers and one parent. Peer links are being generated and peer links will come BEFORE the parent link
</vt:p>

<vt:p pane="20">
This fragment should have 2 children, 3 peers and one parent. Peer links are being generated and peer links will come AFTER the parent link
</vt:p>

<vt:p pane="21">
Fragment 213, pane 1
</vt:p>

<vt:p pane="22">
Fragment 2, pane 2
</vt:p>

<vt:p pane="31">
Fragment 3, pane 1
</vt:p>

<vt:p pane="32">
Fragment 3, pane 2
</vt:p>

<vt:p pane="41">
Fragment 4, pane 1
</vt:p>

<vt:p pane="42">
Fragment 4, pane 2
</vt:p>




</vt:canvas>
