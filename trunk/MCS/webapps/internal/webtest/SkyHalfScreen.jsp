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
 % $Header: /src/voyager/webapp/internal/webtest/SkyHalfScreen.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas pageTitle="Sky Half Screen Layout" layoutName="SkyHalfScreen">


<vt:pane name="menu">
<vt:a accessKey="{red}" href="http://test2.volantis.com/delta">Main Test Page</vt:a>
</vt:pane>

<vt:pane name="browser">
<vt:p>This is the BROWSER pane and any content should go in here. 
</vt:p>
</vt:pane>

</vt:canvas>


