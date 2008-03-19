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
        AFP     31/10/02     	 Created this file

      *************************************************************************
-->


<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_display" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_display.jsp<vt:br/><vt:br/>
theme: tt_display<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>This page tests the display properties. This can be found in the box Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.display1{color:#a0a;display:block}<vt:br/>
.display2{color:#180;display:inline}<vt:br/>
.display3{background-color:#aff;display:block;height:50.0px;width:200.0px}<vt:br/>
.display4{background-color:#fab;display:none;height:50.0px;width:200.0px}<vt:br/>
.display5{border:1.0px #0f0 solid;display:list-item;margin:5.0%}<vt:br/>
.display6{border:1.0px #f00 solid;display:block;margin:5.0%}<vt:br/>
.display7{display:list-item}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above<vt:br/>
Wap TV:<vt:br/>
</vt:p>

<vt:div>
This is a div with no styleClass that encloses the following text.
<vt:div styleClass="display1">
This div has styleClass=display1 and should start on a new line
<vt:div styleClass="display2">
And this nexted div has styleClass="display2" and should be inline.
</vt:div>
</vt:div>
End of outer div
</vt:div>

<vt:br/>

<vt:div styleClass="display3">
This div has styleClass=display3.
</vt:div>

<vt:p>Below there is a div with display set to "none" so should not be visible, or affect layout.</vt:p>
<vt:div styleClass="display4">
This div has styleClass=display4.
</vt:div>

<vt:div styleClass="display6">
Below are 2 divs with display:list-item
<vt:div styleClass="display5">
This div has styleClass=display5
</vt:div>
<vt:div styleClass="display5">
This div has styleClass=display5
</vt:div>
</vt:div>

<vt:div>
Below are 2 divs with display:list-item
<vt:div styleClass="display7">
This div has styleClass=display7
</vt:div>
<vt:div styleClass="display7">
This div has styleClass=display7
</vt:div>
</vt:div>

<vt:br/>
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_display.jsp
</vt:p>


</vt:pane>

</vt:canvas>

