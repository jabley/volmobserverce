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
 % $Header: /src/voyager/webapp/internal/webtest/TVhalfmodeset.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
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
<vt:canvas pageTitle="Wap TV modeset Test Page" layoutName="SkyHalfScreen" theme="iDTV">

<vt:menu type="plaintext" pane="menu">
	<vt:menuitem
		accessKey="{red}" 
		href="http://test2.volantis.com/delta/TVindex.jsp"
		text="TV Testing Index" />
</vt:menu>

<vt:pane name="browser">
<vt:h2>Sky Wap TV Modeset/Layout Test Page</vt:h2>
<vt:p>
	filename: TVhalfmodeset.jsp<vt:br/>
	purpose: Sky Wap TV modesets and layouts<vt:br/>
	devices: Sky<vt:br/>
	layout: SkyHalfScreen<vt:br/>
	theme: iDTV<vt:br/><vt:br/>

This is the BROWSER pane.<vt:br/>
</vt:p>
</vt:pane>

</vt:canvas>


