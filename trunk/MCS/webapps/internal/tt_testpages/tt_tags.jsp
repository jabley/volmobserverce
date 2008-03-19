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
<vt:canvas layoutName="standard" theme="standard" pageTitle="Element Index Page">

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
	<vt:a href="tt_address1.jsp" styleClass="menu2">address</vt:a><vt:br/>
	<vt:a href="tt_b1.jsp" styleClass="menu2">b</vt:a><vt:br/>
	<vt:a href="tt_big1.jsp" styleClass="menu2">big</vt:a><vt:br/>
	<vt:a href="tt_blockquote1.jsp" styleClass="menu2">blockquote</vt:a><vt:br/>
	<vt:a href="tt_cite1.jsp" styleClass="menu2">cite</vt:a><vt:br/>
	<vt:a href="tt_code1.jsp" styleClass="menu2">code</vt:a><vt:br/>
	<vt:a href="tt_definition1.jsp" styleClass="menu2">dl, dt and dd</vt:a><vt:br/>
	<vt:a href="tt_em1.jsp" styleClass="menu2">em</vt:a><vt:br/>
	<vt:a href="tt_hr1.jsp" styleClass="menu2">hr</vt:a><vt:br/>
	<vt:a href="tt_i1.jsp" styleClass="menu2">i</vt:a><vt:br/>
	<vt:a href="tt_list1.jsp" styleClass="menu2">ol and li lists</vt:a><vt:br/>
	<vt:a href="tt_kbd1.jsp" styleClass="menu2">kbd</vt:a><vt:br/>
	<vt:a href="tt_meta1.jsp" styleClass="menu2">meta 1</vt:a><vt:br/>
	<vt:a href="tt_meta2.jsp" styleClass="menu2">meta 2</vt:a><vt:br/>
	<vt:a href="tt_samp1.jsp" styleClass="menu2">samp</vt:a><vt:br/>
	<vt:a href="tt_small1.jsp" styleClass="menu2">small</vt:a><vt:br/>
	<vt:a href="tt_strong1.jsp" styleClass="menu2">strong</vt:a><vt:br/>
	<vt:a href="tt_subsup1.jsp" styleClass="menu2">sub and sup</vt:a><vt:br/>
	<vt:a href="index.jsp" styleClass="menu2">Main Index</vt:a>
</vt:pane>

<vt:pane name="info">
	<vt:p>
	Filename: tt_tags.jsp <vt:br/>
	Layout: standard <vt:br/>
	Theme: standard <vt:br/>
	</vt:p>
</vt:pane>

<vt:pane name="main">
	<vt:p>
	This section will test vt: elements that are not covered by any of the other headings.
	</vt:p>
</vt:pane>

</vt:canvas>



