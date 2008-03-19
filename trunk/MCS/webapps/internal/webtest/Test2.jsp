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
 % $Header: /src/voyager/webapp/internal/webtest/Test2.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Oct-01    Pether          VBM:2001040901 - Corrected, some tags had 
 %                              changed.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="Test2" title="Test2 testing page" theme="Theme2">

<vt:p pane="pane1">Below should be the volantis image:<vt:br/></vt:p>

<vt:img pane="pane1" styleClass="image" id="id1" title="Image Title" src="volantis" altText="Volantis Picture"/>

<vt:logo pane="pane1" styleClass="logo" id="id2" title="Logo title" src="stars" altText="Stars Picture"/>

<vt:form action="dosomething.jsp" pane="pane1">
<vt:textarea name="thename">Some text inside the text area</vt:textarea>
</vt:form>

<vt:p pane="pane1">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>

</vt:canvas>


