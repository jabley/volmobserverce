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
 % $Header: /src/voyager/webapp/internal/webtest/RowIterMenus.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 23-Oct-01    Pether          VBM:2001101603 - Created
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_iterator"
		theme="tt_orientation">

<vt:menu pane="rowiter" type="plaintext" styleClass="orienthoriz">
	<vt:menuitem href="this.html" text="HorizThis"/>
	<vt:menuitem href="that.html" text="HorizThat"/>
	<vt:menuitem href="other.html" text="HorizOther"/>
</vt:menu>

<vt:pane name="rowiter">
	<vt:br/>
</vt:pane>

<vt:menu pane="rowiter" type="plaintext" styleClass="orientvert">
	<vt:menuitem href="this.html" text="VertThis"/>
	<vt:menuitem href="that.html" text="VertThat"/>
	<vt:menuitem href="other.html" text="VertOther"/>
</vt:menu>

  
</vt:canvas>
