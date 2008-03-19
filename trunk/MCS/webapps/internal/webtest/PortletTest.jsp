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
 % $Header: /src/voyager/webapp/internal/webtest/PortletTest.jsp,v 1.3 2002/03/12 13:01:26 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 02-Nov-01    Paul            VBM:2001102403 - Created.
 % 19-Nov-01    Paul            VBM:2001110202 - Changed text to differentiate
 %                              between this page and the included page.
 % 22-Nov-01    Paul            VBM:2001110202 - Added menus to check that
 %                              they work in a portal environment.
 % 08-Mar-02    Paul            VBM:2002030607 - Added theme.
 % 12-Mar-02    Paul            VBM:2002021201 - Added meta tag which is
 %                              written to the page head to make sure that
 %                              portlets in context can write to the portals
 %                              page head.
 % ======================================================================= --%>

<%@ include file="VolantisNoError-mcs.jsp" %>

<vt:canvas layoutName="PortletTest"
           theme="PortletTest" 
           pageTitle="Portlet Test"
           type="portlet">

<vt:meta name="barney" httpEquiv="betty" content="bambam"/>

<vt:p pane="Pane1" styleClass="red">
  Second level text.
</vt:p>

<vt:menu pane="Pane1" type="rolloverimage">
  <vt:menuitem href="Somewhere1.jsp" onImage="stars" offImage="volantis"
               text="Item 1"/>
</vt:menu>

<vt:region name="Region1">
  <jsp:include page="PortletTest2.jsp"/>
</vt:region>

</vt:canvas>
