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
<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="iDTV1" theme="iDTV">
<vt:pane name="main">
 <vt:h2>Localsrc Image Test Page</vt:h2>
 <vt:p>
	filename: tt_tvlocalsrc.jsp<vt:br/>
	purpose: test localsrc image on Sky<vt:br/>
	devices: Sky<vt:br/>
	layout: iDTV1<vt:br/>
	theme: iDTV<vt:br/>
 </vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a><vt:br/>
<vt:a href="tt_devices.jsp">Devices Index</vt:a><vt:br/>
</vt:p>

<vt:img src="localsrc" altText="localsrc image" />

</vt:pane>
</vt:canvas>


