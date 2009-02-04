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
 % $Header: /src/voyager/webapp/internal/webtest/testIncludeDiv.jsp,v 1.3 2002/02/22 09:25:54 payal Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Feb-02    Payal            VBM:2002011410 - Created
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="inclayout" type="portal" theme="inctheme">
 
<vt:pane name="info">
    <vt:h2 styleClass="3A-FCB5">Outside with class 3A-FCB5</vt:h2>
    <vt:h2 id="3A-FCB5">Outside with id 3A-FCB5</vt:h2>
    <vt:p>Text in the div</vt:p>
</vt:pane>
 
<vt:region name="Region1">
    <jsp:include page="inc01.jsp"/>
</vt:region>
<vt:region name="Region2">
    <jsp:include page="inc02.jsp"/>
</vt:region>
<vt:region name="Region3">
    <jsp:include page="inc03.jsp"/>
    <jsp:include page="inc04.jsp"/>
</vt:region>
 
</vt:canvas>
