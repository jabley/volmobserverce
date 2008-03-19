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
 % $Header: /src/voyager/webapp/internal/webtest/IncludeTest3.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Oct-01    Paul            VBM:2001102608 - Created.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<%-- ==========================================================================
 % This page tests what happens when we include a partial page with some block
 % tags.
 % ======================================================================= --%>

<vt:canvas layoutName="IncludeTest" pageTitle="Include Test 3">

<vt:p pane="Pane1">
<jsp:include page="PartialPage3.jsp"/>
</vt:p>

</vt:canvas>
