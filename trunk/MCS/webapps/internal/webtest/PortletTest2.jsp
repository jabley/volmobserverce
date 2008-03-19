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
 % $Header: /src/voyager/webapp/internal/webtest/PortletTest2.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 02-Nov-01    Paul            VBM:2001102403 - Created.
 % 19-Nov-01    Paul            VBM:2001110202 - Changed layout to PortletTest2
 %                              which doesn't have a region.
 % 22-Nov-01    Paul            VBM:2001110202 - Added menus to check that
 %                              they work in a portal environment.
 % ======================================================================= --%>

<%@ include file="VolantisNoError-mcs.jsp" %>

<vt:canvas layoutName="PortletTest2" 
           pageTitle="Portlet Test"
           type="portlet">

<vt:p pane="Pane1">
  Third level text.
</vt:p>

<vt:menu pane="Pane1" type="rolloverimage">
  <vt:menuitem href="Somewhere1.jsp" onImage="stars" offImage="volantis"
               text="Item 1"/>
</vt:menu>

</vt:canvas>
