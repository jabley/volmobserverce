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
 % $Header: /src/voyager/webapp/internal/webtest/EmulateTagTest.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 14-Sep-01    Paul            VBM:2001083114 - Created.
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="error" pageTitle="Emulate Tag Test">

<vt:p pane="error">
<vt:big>Big Text</vt:big>
<vt:br/>
<vt:b>Bold Text</vt:b>
<vt:br/>
<vt:em>Emphasized Text</vt:em>
<vt:br/>
<vt:i>Italic Text</vt:i>
<vt:br/>
<vt:a href="nowhere">Link Text</vt:a>
<vt:br/>
<vt:small>Small Text</vt:small>
<vt:br/>
<vt:strong>Strong Text</vt:strong>
<vt:br/>
<vt:u>Underlined Text</vt:u>
</vt:p>

</vt:canvas>
