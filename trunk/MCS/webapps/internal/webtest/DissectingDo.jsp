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
 % $Header: /src/voyager/webapp/internal/webtest/DissectingDo.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 07-Dec-01    Paul            VBM:2001112910 - Created.
 % ======================================================================= --%>

<%-- ==========================================================================
 % This page tests that when dissecting a WML page we do not dissect a do tag.
 %
 % This is needed because although we only generate do tags when we are
 % processing forms and forms cannot really be dissected one of our customers
 % has a custom tag which generates anchor and do tags outside forms.
 %
 % In future we may provide stylistic control over whether our 'a' element
 % generates 'a' tags or 'anchor' tags at which time this test should probably
 % be changed to use them but for the moment this page manages the test.
 %
 % To be sure that the problem is not present you need to check that the log
 % file contains a line similar to
 %                   do cannot be broken down.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="DissectingDo">

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp">

<%
  for (int i = 0; i < 8; i += 1) {
    String caption = "Action " + i;
    String actionName = "action" + i;
    String implicitName = "implicit" + i;
%>

<vt:xfimplicit name="<%= implicitName %>" value="Paul"/>

<vt:xfaction
        caption="<%= caption %>"
        type="submit"
        name="<%= actionName %>"/>

<%
  }
%>

</vt:xfform>

</vt:canvas>
