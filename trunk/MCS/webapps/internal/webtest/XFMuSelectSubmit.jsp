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
 % $Header: /src/voyager/webapp/internal/webtest/XFMuSelectSubmit.jsp,v 1.1 2002/03/05 12:36:38 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 04-Mar-02    Paul            VBM:2001101803 - Created to test multiple
 %                              select works with values with embedded ;.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormSubmit">

<vt:p pane="Result">
<%
  String [] drinks = marinerRequestContext.getParameterValues ("field1");
  int length = drinks == null ? 0 : drinks.length;
  if (length == 0) {
    out.println ("You did not select any drinks");
  } else {
    out.println ("You selected the following drinks");
    for (int i = 0; i < length; i += 1) {
%><vt:br/><%
      String value = drinks [i];
      if (value.equals ("cof;fee")) {
        out.println ("Coffee");
      } else if (value.equals ("t,e,a")) {
        out.println ("Tea");
      } else if (value.equals ("m;il;k")) {
        out.println ("Milk");
      } else if (value.equals (";v,,,od;,;,ka")) {
        out.println ("Vodka");
      } else {
        out.println ("Unrecognized drink " + value);
      }
    }
  }
%>
</vt:p>

</vt:canvas>
