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
<!--  *************************************************************************
        (c) Volantis Systems Ltd 2001. 
      *************************************************************************
        Revision Info
        Name    Date            Comment
        AFP     31/10/02         Created this file

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="standard" theme="standard" pageTitle="Themes: Form">

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
	<vt:a href="tt_xfform08.jsp" styleClass="menu2">Radio/Checkbox 1</vt:a><vt:br/>
	<vt:a href="tt_xfform09.jsp" styleClass="menu2">Radio/Checkbox 2</vt:a><vt:br/>
	<vt:a href="tt_xfform10.jsp" styleClass="menu2">Text Input</vt:a><vt:br/>
	<vt:a href="tt_xfform11.jsp" styleClass="menu2">Menu Themes</vt:a><vt:br/>
	<vt:a href="tt_xfform06.jsp" styleClass="menu2">Submit Button</vt:a><vt:br/>
	<vt:a href="tt_ind_image.jsp" styleClass="menu2">Indicating Image</vt:a><vt:br/>
	<vt:a href="tt_trigger.jsp" styleClass="menu2">Selection List Trigger</vt:a><vt:br/>
	
	<vt:a href="tt_themes.jsp" styleClass="menu2">Thems Index</vt:a><vt:br/>
	<vt:a href="index.jsp" styleClass="menu2">Main Index</vt:a><vt:br/>
	</vt:p>
</vt:pane>

<vt:pane name="info">
<vt:p>
	Filename: tt_themes_form.jsp <vt:br/>
	Layout: standard <vt:br/>
	Theme: standard <vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="main">
<vt:p>
This is the 'Form' Index page.
It contains links to test pages that cover the theme properties that are in the 'Form' tab in style properties window in the GUI. Properties are:<vt:br/>
- Form Action Style<vt:br/>
- Form Action Image<vt:br/>
- Indicating Image<vt:br/>
- Columns<vt:br/>
- Rows<vt:br/>
- Selection List Layout<vt:br/>
- Selection List Style<vt:br/>
- Selection List Trigger<vt:br/>
- Focus<vt:br/>
</vt:p>

</vt:pane>

</vt:canvas>


