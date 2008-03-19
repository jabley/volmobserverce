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
 % $Header: /src/voyager/webapp/internal/webtest/MontageTestBar.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 15-Oct-01    Paul            VBM:2001101207 - Created.
 % 19-Dec-01    Paul            VBM:2001120506 - Added this header and removed
 %                              setting of orientation on menu.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="error">

  <vt:menu pane="error" type="rolloverimage">
    <vt:menuitem text="tables1" href="Tables1.jsp" segment="content"
                 onImage="row" offImage="column"/>
    <vt:menuitem text="tables2" href="Tables2.jsp" segment="content"
                 onImage="row" offImage="column"/>
    <vt:menuitem text="tables3" href="Tables3.jsp" segment="content"
                 onImage="row" offImage="column"/>
  </vt:menu>

  <vt:p pane="error">
    <vt:br/>
    <vt:a href="index.jsp">Home</vt:a>
    <vt:a href="Layouts.jsp">Back</vt:a>
    <vt:br/>
  </vt:p>

</vt:canvas>
