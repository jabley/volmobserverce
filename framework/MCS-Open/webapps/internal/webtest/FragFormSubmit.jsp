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
 % $Header: /src/voyager/webapp/internal/webtest/FragFormSubmit.jsp,v 1.2 2002/03/05 12:36:38 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Feb-02    Steve           Display parameters from a fragmented form.
 % 04-Mar-02    Paul            VBM:2001101803 - Modified to use the public
 %                              API on marinerRequestContext to retrieve the
 %                              values from the request.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormSubmit">

<vt:p pane="Result">
<%
  String drink = marinerRequestContext.getParameter ("single1");
  String sugar = marinerRequestContext.getParameter ("boolean");
  String text = marinerRequestContext.getParameter ("text1");
  String mtext = marinerRequestContext.getParameter ("text2");
  String name = marinerRequestContext.getParameter ("name");
%>

Name : <%=name%><vt:br />
Selection : <%=drink%><vt:br />
Boolean : <%=sugar%><vt:br />
Text : <%=text%><vt:br />
Multiline Text : <%=mtext%><vt:br />
</vt:p>

Goodbye.
</vt:p>

</vt:canvas>

