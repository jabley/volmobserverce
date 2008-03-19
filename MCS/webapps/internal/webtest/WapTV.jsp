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
 % $Header: /src/voyager/webapp/internal/webtest/WapTV.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="FormTV" theme="waptv">
<vt:p pane="form">

Here is some text within the p tags<vt:br/>

<vt:a accessKey="{blue}" href="SkyHalfScreen.jsp">
Press the blue button to go to SkyHalfScreen.jsp</vt:a><vt:br/>

<vt:a accessKey="{green}" href="WapTitles1.jsp">
Press the green button to go to WapTitles1.jsp</vt:a><vt:br/>

<vt:a accessKey="{yellow}" href="index.jsp">
Press the Yellow Button to go to index.jsp</vt:a>

</vt:p>
<vt:dynvis pane="TV"
          name="tv"
          altText="AltText"/>
</vt:canvas>


