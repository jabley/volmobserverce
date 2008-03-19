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

<vt:canvas theme="tt_width" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_width.jsp<vt:br/>
theme: tt_width<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_width.jsp
</vt:p>

<vt:p>This page tests the width property. This can be found in the Box Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.width1{background-color:#0a0;width:300.0px}<vt:br/>
.width2{background-color:#ddd;width:400.0px}<vt:br/>
.width3{background-color:#aff;width:50.0%}<vt:br/>
.width4{background-color:#a0a;width:80.0%}<vt:br/>
.width5{background-color:#fa0;width:100.0%}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above<vt:br/>
Wap TV:<vt:br/>
</vt:p>

<vt:p styleClass="width5">
This p tag has styleClass="width5"
</vt:p>

<vt:p styleClass="width3">
This p tag has styleClass=width3
</vt:p>

<vt:p styleClass="width2">
This p tag has styleClass="width2"
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="width1">
This td has styleClass=width1
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="2" styleClass="width2">
<vt:tr>
<vt:td>
This table has styleClass=width2
</vt:td>
<vt:td></vt:td>
</vt:tr>
<vt:tr>
<vt:td styleClass="width1">
This td has styleClass=width1.
</vt:td>
<vt:td>
This td has no width set
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="2" styleClass="width5">
<vt:tr>
<vt:td>
This table has styleClass=width5
</vt:td>
<vt:td></vt:td>
</vt:tr>
<vt:tr>
<vt:td styleClass="width3">
This td has styleClass=width3.
</vt:td>
<vt:td>
This td has no width set
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:div styleClass="width4">
This div tag has styleClass=width4
</vt:div>

<vt:br/>

<vt:div styleClass="width2">
This div tag has styleClass=width2
<vt:div styleClass="width4">
And this nested div tag has styleClass=width4
</vt:div>
</vt:div>

<vt:br/>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_width.jsp
</vt:p>

</vt:pane>

</vt:canvas>

