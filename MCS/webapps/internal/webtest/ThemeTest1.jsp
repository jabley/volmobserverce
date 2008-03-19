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
 % $Header: /src/voyager/webapp/internal/webtest/ThemeTest1.jsp,v 1.1 2002/03/08 12:42:43 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 08-Mar-02    Paul            VBM:2002030607 - Created to test external
 %                              style sheets are generated correctly.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="ThemeTest1" theme="ThemeTest1" pageTitle="Theme Test 1">

<vt:p pane="Pane" id="id1">
Text
</vt:p>

</vt:canvas>
