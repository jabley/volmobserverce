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
 % $Header: /src/voyager/webapp/internal/web/TVtest.jsp,v 1.1 2001/12/27 15:54:59 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 18-Oct-01    Payal            VBM:2001100904 - Created.
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>
<%@ page import="com.volantis.mcs.pagehelpers.*" %>
<vt:canvas layoutName="tv" theme="tv" pageTitle="DynVis Test Page">
<vt:pane name="main">
<vt:h2>Test Page for Modes</vt:h2>
<vt:p>
       This test Page is to test<vt:br/>
       TV mode<vt:br/>
     
 <vt:a  href="TVdynvis.jsp">
TV-Mode  View-TV</vt:a><vt:br/>
 
 
</vt:p>
</vt:pane>
 
<vt:dynvis pane="TV"
        name="tv"
        altText="dynvis tv element" />
 
</vt:canvas>
