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
<vt:canvas layoutName="standard" theme="standard" pageTitle="Themes: Format">

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
	<vt:a href="tt_colors.jsp" styleClass="menu2">Colors</vt:a><vt:br/>
	<vt:a href="tt_xfaction_color.jsp" styleClass="menu2">XFaction color</vt:a><vt:br/>
	<vt:a href="tt_caretcolor.jsp" styleClass="menu2">Caret Color</vt:a><vt:br/>
	<vt:a href="tt_bgradius.jsp" styleClass="menu2">Corner Radius</vt:a><vt:br/>
	<vt:a href="tt_linegap.jsp" styleClass="menu2">Line Gap</vt:a><vt:br/>
	<vt:a href="tt_overflow_1.jsp" styleClass="menu2">Overflow: scroll</vt:a><vt:br/>
	<vt:a href="tt_overflow_2.jsp" styleClass="menu2">Overflow: visible</vt:a><vt:br/>
	<vt:a href="tt_overflow_3.jsp" styleClass="menu2">Overflow: hidden</vt:a><vt:br/>
	<vt:a href="tt_overflow_4.jsp" styleClass="menu2">Overflow: auto</vt:a><vt:br/>
	<vt:a href="tt_themes.jsp" styleClass="menu2">Themes Index</vt:a><vt:br/>
	<vt:a href="index.jsp" styleClass="menu2">Main Index</vt:a><vt:br/>
	</vt:p>
</vt:pane>

<vt:pane name="info">
<vt:p>
	Filename: tt_themes_format.jsp <vt:br/>
	Layout: standard <vt:br/>
	Theme: standard <vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="main">
<vt:p>
This is the 'Format' Index page.
It contains links to test pages that cover the theme properties that are in the 'Format' tab in style properties window in the GUI. Properties are:<vt:br/>
- Color<vt:br/>
- Caret Color<vt:br/>
- Corner Radius<vt:br/>
- Line Gap<vt:br/>
- Overflow<vt:br/>
- Paragraph Gap<vt:br/>
</vt:p>


</vt:pane>

</vt:canvas>



