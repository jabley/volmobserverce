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
<vt:canvas layoutName="standard" theme="standard" pageTitle="Multimedia Index Page">

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


 <vt:pane name="menu2">
	<vt:a href="tt_flash1.jsp" styleClass="menu2">Flash Test</vt:a><vt:br/>
	<vt:a href="tt_quicktime.jsp" styleClass="menu2">Quick Time Test</vt:a><vt:br/>
	<vt:a href="tt_realvideo.jsp" styleClass="menu2">Real Time Video</vt:a><vt:br/>
	<vt:a href="tt_audio1.jsp" styleClass="menu2">Audio Elements 1</vt:a><vt:br/>
</vt:pane>

<vt:pane name="info">
<vt:p>
	Filename: tt_multimedia.jsp <vt:br/>
	Layout: standard <vt:br/>
	Theme: standard <vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="main">
<vt:p>
This section tests all formats of multimedia and multimedia fallback.
</vt:p>
</vt:pane>

</vt:canvas>



