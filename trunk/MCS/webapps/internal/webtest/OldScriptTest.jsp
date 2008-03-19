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
 % $Header: /src/voyager/webapp/internal/webtest/OldScriptTest.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Feb-02    Paul            VBM:2001122105 - Copied from ScriptTest.jsp
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="IndexLayout">
<vt:pane name="index">
<vt:p>Depending on the browser either an external script, or a message will appear...<vt:br/></vt:p>
<vt:script type="text/javascript" src="script.js" id="someid" title="sometitle" styleClass="someclass"></vt:script>
<vt:noscript id="someid" title="sometitle" styleClass="someclass">This message shouls appear instead for browsers with
no script support</vt:noscript>
</vt:pane>

<vt:p pane="index">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>


</vt:canvas>
