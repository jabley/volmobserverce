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
<vt:canvas layoutName="standard" theme="standard" pageTitle="Test Index Page">

<vt:pane name="title">
<vt:img src="stars"/>
<vt:img src="volantis"/>
</vt:pane>

<vt:pane name="submenu">
<vt:table styleClass="menu1" cols="12">
<vt:tr>
	<vt:td styleClass="menu1"><vt:a href="tt_layouts.jsp" styleClass="menu1">LAYOUTS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_themes.jsp" styleClass="menu1">THEMES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_devices.jsp" styleClass="menu1">DEVICES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_forms.jsp" styleClass="menu1">FORMS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_tables.jsp" styleClass="menu1">TABLES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_images.jsp" styleClass="menu1">IMAGES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_multimedia.jsp" styleClass="menu1">MULTIMEDIA</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_menus.jsp" styleClass="menu1">MENUS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_tags.jsp" styleClass="menu1">ELEMENTS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_performance.jsp" styleClass="menu1">PERFORMANCE</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_portals.jsp" styleClass="menu1">PORTAL/PORTLET</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_scripts.jsp" styleClass="menu1">SCRIPTS</vt:a></vt:td>
</vt:tr>
</vt:table>
</vt:pane>


<vt:pane name="menuTV">
<vt:table styleClass="menu1" cols="5">
<vt:tr>
	<vt:td styleClass="menu1"><vt:a href="tt_layouts.jsp" styleClass="menu1">LAYOUTS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_themes.jsp" styleClass="menu1">THEMES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_devices.jsp" styleClass="menu1">DEVICES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_forms.jsp" styleClass="menu1">FORMS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_tables.jsp" styleClass="menu1">TABLES</vt:a></vt:td>
</vt:tr>
<vt:tr>	
	<vt:td styleClass="menu1"><vt:a href="tt_images.jsp" styleClass="menu1">IMAGES</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_charts.jsp" styleClass="menu1">CHARTS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_multimedia.jsp" styleClass="menu1">MULTIMEDIA</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_menus.jsp" styleClass="menu1">MENUS</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_tags.jsp" styleClass="menu1">ELEMENTS</vt:a></vt:td>
</vt:tr>
<vt:tr>
	<vt:td styleClass="menu1"><vt:a href="tt_performance.jsp" styleClass="menu1">PERFORMANCE</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="tt_portals.jsp" styleClass="menu1">PORTAL/PORTLET</vt:a></vt:td>
	<vt:td></vt:td>
	<vt:td></vt:td>
	<vt:td></vt:td>
</vt:tr>
</vt:table>
</vt:pane>

<vt:pane name="menuMobile">
<vt:p>
<vt:a href="tt_layouts.jsp">LAYOUTS</vt:a><vt:br/>
<vt:a href="tt_themes.jsp">THEMES</vt:a><vt:br/>
<vt:a href="tt_devices.jsp">DEVICES</vt:a><vt:br/>
<vt:a href="tt_forms.jsp">FORMS</vt:a><vt:br/>
<vt:a href="tt_tables.jsp">TABLES</vt:a><vt:br/>
<vt:a href="tt_images.jsp">IMAGES</vt:a><vt:br/>
<vt:a href="tt_multimedia.jsp">MULTIMEDIA</vt:a><vt:br/>
<vt:a href="tt_menus.jsp">MENUS</vt:a><vt:br/>
<vt:a href="tt_tags.jsp" styleClass="menu1">ELEMENTS</vt:a><vt:br/>
<vt:a href="tt_performance.jsp" styleClass="menu1">PERFORMANCE</vt:a><vt:br/>
<vt:a href="tt_portals.jsp" styleClass="menu1">PORTAL/PORTLET</vt:a><vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="menu2">
<vt:p styleClass="menu2">&nbsp;</vt:p>
</vt:pane>


<vt:pane name="info">
<vt:p>
	Filename: index.jsp <vt:br/>
	Layout: standard <vt:br/>
	Theme: standard <vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="main">
<vt:p>
These are the volantis mariner test pages. The pages are split into various areas to provide ease of use for testing. Each page should have its filename, layout and theme where relevant on the page. <vt:br/>
<vt:br/>
To complete full device certification all of these pages need to be used to fully test a device. For specific testing go to the relevant topic area and find the test page for the function you are looking for.<vt:br/>
<vt:br/>
If there are any problems with any of these pages, or you find an area that is not covered then please e mail the <vt:a href="mailto:test_team@volantis.com">Test Team</vt:a> and notify them of the issue.
</vt:p>
</vt:pane>

</vt:canvas>

