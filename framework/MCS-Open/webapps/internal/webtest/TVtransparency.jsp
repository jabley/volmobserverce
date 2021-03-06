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
<vt:canvas layoutName="iDTV2" theme="iDTV" pageTitle="TV Transparency Test Page">
<vt:pane name="main">
 <vt:h2>TV Test Pages</vt:h2>
 <vt:p>
	filename: TVindex.jsp<vt:br/>
	purpose: index of TV test pages<vt:br/>
	devices: All TV devices<vt:br/>
	layout: iDTV1<vt:br/>
	theme: iDTV<vt:br/>
 </vt:p>

 <vt:p>These test pages are designed to demonstrate and test the specific functionality for various TV devices. Some functions will apply to all the TV devices, and some will apply to specific TV devices, as specified. <vt:br/>
 The following devices will use these test pages:
<vt:ol title="devices">
	<vt:li>Liberate (Lib)</vt:li>
	<vt:li>Netgem Netbox (Net)</vt:li>
	<vt:li>Sky Interactive Wap TV (Sky)</vt:li>
	<vt:li>MS Web TV (MS)</vt:li>
	<vt:li>Bush Internet TV (Bush)</vt:li>
</vt:ol>
<vt:a href="TVsoftKeys.jsp">Soft Keys Test Page - All Devices</vt:a><vt:br/>
<vt:a href="TVtables.jsp">Tables Test Page  - Sky Wap TV</vt:a><vt:br/>
<vt:a href="TVhalfmodeset.jsp">Sky Wap TV Modeset  - Sky Wap TV</vt:a><vt:br/>
<vt:a href="TVdynvis.jsp">Dynamic Visual Element Test Page - All</vt:a>
</vt:p>
</vt:pane>
</vt:canvas>


