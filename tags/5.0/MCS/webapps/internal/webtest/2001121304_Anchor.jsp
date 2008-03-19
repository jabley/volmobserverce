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
 % $Header: /src/voyager/webapp/internal/webtest/2001121304_Anchor.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 20-Dec-01    Adrian          VBM:2001101207 - Created.
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="AnchorTest" theme="AnchorTest" pageTitle="AnchorTest2">

<%-- Test emphasis tags outside an anchor --%>
<vt:p pane="Rows">
  <vt:a href="location" name="first"><vt:h1>With href=&quot;location&quot;</vt:h1></vt:a>
  <vt:a href="" name="second"><vt:h1>With href=&quot;&quot;</vt:h1></vt:a>
  <vt:a name="third"><vt:h1>Without href</vt:h1></vt:a>
</vt:p>
</vt:canvas>
