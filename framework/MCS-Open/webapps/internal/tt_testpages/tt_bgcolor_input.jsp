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
	AFP	31/10/2002	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="tt_xfform" theme="tt_input_bgcolor">

<vt:pane name="fileinfo">
  <vt:h3>Input BG Color Test</vt:h3>
  <vt:p>Filename: tt_bgcolor_input.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_ngcolor_input<vt:br/><vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="header">
<vt:p>
Test:<vt:br/>
This page shows a text input field with a bgcolor set to red. <vt:br/>
.bgcolor{background-color:#f03}<vt:br/>
<vt:br/>
Expected Results:<vt:br/>
CSS: as above. <vt:br/>
Wap TV: input bgcolor="#ff0033"<vt:br/>

</vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes Index</vt:a> =>
<vt:a href="tt_themes_background.jsp">Background Index</vt:a> =>
tt_input_bgcolor.jsp
</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit01.jsp">

<vt:xftextinput	name="drink"
		styleClass="bgcolor"
		caption="Drink: "
		captionPane="formCaption1"
		entryPane="formCaption1"/>

<vt:xfaction	type="submit"
		caption="Submit Order"
		captionPane="formCaption1"
		entryPane="formCaption1"/>
		
</vt:xfform>

</vt:canvas>
