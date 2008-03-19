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
 % $Header: /src/voyager/webapp/internal/webtest/Dissecting2.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 03-Dec-01    Paul            VBM:2001120301 - Created.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="dissecting">

<vt:pane name="welcome">
<vt:p>
<vt:b>Some text in the welcome pane. The Mariner release notes will follow in the next pane.</vt:b>
</vt:p>

<vt:p>
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Layouts.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:pane>


<vt:pane name="dissecting">
<vt:p>
<vt:b>Some text in the dissecting pane. Some comment about the Mariner Architecture.</vt:b><vt:br/>
</vt:p>
</vt:pane>


</vt:canvas>



