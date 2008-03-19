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
 % $Header: /src/voyager/webapp/internal/webtest/LinkAncTest.jsp,v 1.2 2002/05/23 09:49:21 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 21-Sep-01    Doug            VBM:2001090302 - Created
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 23-May-02    Paul            VBM:2002042202 - Added link with an anchor
 %                              which contains a query string with an &.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="error" theme="linktest">
<vt:pane name="error">
<vt:p>

<vt:h1>Welcome to Volantis Systems Ltd. (tm) </vt:h1>
<vt:img src="stars"/>
<vt:img src="volantis"/>
<vt:h2>Testing Anchor Tags with Link Components.</vt:h2>

<vt:h3>
<vt:a href="{news}">(BBC Master, ITN WML) from link component</vt:a>
</vt:h3>

<vt:h3>
<vt:a href="{mariner}">Back to this page</vt:a>
</vt:h3>

<vt:h3>
<vt:a href="http://www.bbc.co.uk">BBC from String Literal</vt:a>
</vt:h3>

<vt:h3>
<vt:a href="{zzzzzz}">bad link</vt:a>
</vt:h3>

<vt:h3>
<vt:a href="link-needs-escaping?a=1&b=2">link which needs escaping</vt:a>
</vt:h3>

<vt:dividehint/>

</vt:p>
</vt:pane>
</vt:canvas>
