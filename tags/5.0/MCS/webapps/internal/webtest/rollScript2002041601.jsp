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
 % $Header: /src/voyager/webapp/internal/webtest/rollScript2002041601.jsp,v 1.2 2002/04/16 15:10:39 aboyd Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 16-Apr-02    Allan           VBM:2002041601 - Created. From myk1.jsp.
 % ======================================================================= --%><%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="all" theme="XFFormTest2"
      onload="{statusOnLoad}"
      onunload="{OnAny}">

<vt:script src="{openEventWindow}"/>

<vt:menu pane="para" type="rolloverimage" styleClass="navigation_site">
<vt:menuitem href="#" onmouseout="debug('onMouseOut')"
onmouseover="debug('mouseOvertext')" onImage="stars"
offImage="volantis" text="Fun Love und Spiele"/>

</vt:menu>
</vt:canvas>

