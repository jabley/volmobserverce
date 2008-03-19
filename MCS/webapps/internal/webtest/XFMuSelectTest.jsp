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
 % $Header: /src/voyager/webapp/internal/webtest/XFMuSelectTest.jsp,v 1.1 2002/03/05 12:36:38 pduffin Exp $
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

<vt:canvas layoutName="XFFormTest" theme="XFFormTest"
           pageTitle="XFMuSelectTest">

<vt:xfform
        name="Form"
        method="get"
        action="XFMuSelectSubmit.jsp">

<vt:xfmuselect
        active="true"
        name="field1"
        caption="Multiple select">

  <vt:xfoption caption="Coffee" value="cof;fee"/>
  <vt:xfoption caption="Tea" value="t,e,a"/>
  <vt:xfoption caption="Milk" value="m;il;k"/>
  <vt:xfoption caption="Vodka" value=";v,,,od;,;,ka"/>

</vt:xfmuselect>

<vt:xfaction type="submit"
        caption="Submit"
        name="action1"
        value="action1"/>

</vt:xfform>

</vt:canvas>
