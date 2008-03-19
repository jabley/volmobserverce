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
 % $Header: /src/voyager/webapp/internal/webtest/horizontal.jsp,v 1.1 2002/01/24 13:10:02 payal Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 24-Jan-02    Payal           VBM:2001102303 - Created.
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="error">
<vt:pane name="error">
<vt:p>Horizontal vt:hr tag test : Should be a hr below</vt:p>
<vt:hr/>
<vt:p>Horizontal vt:hr tag test : Should be a hr above</vt:p>
</vt:pane>
</vt:canvas>
