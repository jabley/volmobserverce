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
 % $Header: /src/voyager/webapp/internal/webtest/Wtai.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 24-Sep-01    Doug            VBM:2001090304 - Created
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="error">
<vt:pane name="error">
<vt:p>

<vt:h1>Welcome to Volantis Systems Ltd. (tm) </vt:h1>
<vt:img src="stars"/>
<vt:img src="volantis"/>
<vt:h2>Testing wtai protocol. </vt:h2>

<vt:h3><vt:a href="{phone}">Call Dougs Phone</vt:a></vt:h3>
<vt:h3><vt:a href="{index.jsp}">volantis</vt:a></vt:h3>

</vt:p>
</vt:pane>
</vt:canvas>



