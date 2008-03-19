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
 % $Header: /src/voyager/webapp/internal/webtest/asseturl.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 24-Jan-02    Doug            VBM:2002011406 - created to test the
 %                              Asset URL Rewriting functionality
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="standard" theme="standard" pageTitle="Asset URL test">

<vt:pane name="title">
<vt:img src="stars0"/>
<vt:img src="stars9"/>
<vt:img src="stars8"/>
<vt:h3>
<vt:a href="{news}">(BBC) from link component</vt:a>
</vt:h3>

</vt:pane>

</vt:canvas>
