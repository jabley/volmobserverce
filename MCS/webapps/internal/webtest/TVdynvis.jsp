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
 % $Header: /src/voyager/webapp/internal/webtest/TVdynvis.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Feb-02    Paul            VBM:2001122105 - Added this header and removed
 %                              reference to pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="iDTV1" theme="iDTV" pageTitle="DynVis Test Page">
<vt:pane name="main">
<vt:h2>TV Dynamic Visual Test Page</vt:h2>
<vt:p>
	filename: TVdynvis.jsp<vt:br/>
	purpose: dynvis tag<vt:br/>
	devices: <vt:br/>
	layout: iDTV1<vt:br/>
	theme: iDTV<vt:br/>

<vt:a  href="TVindex.jsp">
Return to TV Test Pages Index</vt:a><vt:br/>

</vt:p>
</vt:pane>

<vt:dynvis pane="TV"
	name="tv"
	altText="dynvis tv element" />

</vt:canvas>


