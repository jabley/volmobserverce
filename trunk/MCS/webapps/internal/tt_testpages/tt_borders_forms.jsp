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
	Name  	Date  		Comment
	AFP	04/11/2002	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas theme="tt_borders" layoutName="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>Borders on Form Elements</vt:h1>
  <vt:p>Filename: tt_borders_forms.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_border.jsp">Borders</vt:a> =>
tt_borders_forms.jsp
</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit01.jsp">

<vt:xftextinput	name="drink"
		caption="Drink: "
		captionPane="formCaption1"
		entryPane="formCaption1"
		styleClass="border1"/>

<vt:xftextinput	name="drink"
		caption="Drink: "
		captionPane="formCaption1"
		entryPane="formCaption1"
		styleClass="border2"/>

<vt:xfaction	type="submit"
		caption="Submit Order"
		captionPane="formCaption1"
		entryPane="formCaption1"/>
		
</vt:xfform>



</vt:canvas>
