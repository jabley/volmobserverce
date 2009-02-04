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
<vt:canvas layoutName="tt_xfform" theme="tt_xfaction_color">

<vt:pane name="fileinfo">
  <vt:h1>Test Color Property on xfaction tag</vt:h1>
  <vt:p>Filename: tt_xfaction_color.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfaction_color<vt:br/>
	Devices: All
  </vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_format.jsp">Format</vt:a> =>
tt_xfaction_color.jsp
</vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Test</vt:h3>
  xfaction.color{color:#33f}<vt:br/>
  xfaction.color:focus{color:#f03}<vt:br/>

  <vt:h3>Expected Results</vt:h3>
   css:<vt:br/>
   .VE-xfaction.color{color:#33f}<vt:br/>
   .VE-xfaction.color:focus{color:#f03}<vt:br/>
   WapTV:<vt:br/>
   font fgcolor="#3333ff" fgfocused="#ff0033" fgactivated="#ff0033"<vt:br/>
   
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit01.jsp">

<vt:xftextinput	name="drink"
		caption="Drink: "
		captionPane="formCaption1"
		entryPane="formCaption1"/>

<vt:xfaction	type="submit"
		styleClass="color"
		caption="Submit Order"
		captionPane="formCaption1"
		entryPane="formCaption1"/>
		
</vt:xfform>

</vt:canvas>
