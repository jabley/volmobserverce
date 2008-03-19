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

<vt:canvas theme="tt_height" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_height.jsp<vt:br/>
theme: tt_height<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_margins.jsp
</vt:p>


<vt:p>This page tests the height property. This can be found in the Box Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.height1{background-color:#0a0;height:60.0px}<vt:br/>
.height2{background-color:#ddd;height:130.0px}<vt:br/>
.height3{background-color:#aff;height:50.0%}<vt:br/>
.height4{background-color:#a0a;height:80.0%}<vt:br/>
.height5{background-color:#ff0;height:auto}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above<vt:br/>
Wap TV:<vt:br/>
</vt:p>

<vt:p styleClass="height1">
This p tag has styleClass="height1"
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="height2">
This td has styleClass=height2
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr styleClass="height1">
<vt:td>
This tr has styleClass=height1
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:div styleClass="height1">
This div has styleClass=height1
<vt:p styleClass="height3">
This p tag has styleClass=height3
</vt:p>
</vt:div>

<vt:br/>

<vt:div styleClass="height2">
This div tag has styleClass=height2
<vt:div styleClass="height4">
And this nested div tag has styleClass=height4
</vt:div>
</vt:div>

<vt:p styleClass="height5">
This p tag has styleClass=height5.  This property specifies the content height of boxes generated by block-level and replaced elements. This property does not apply to non-replaced inline-level elements. The height of a non-replaced inline element's boxes is given by the element's (possibly inherited) 'line-height' value.Values have the following meanings:
<vt:br/>
length:
Specifies a fixed height. percentage:
Specifies a percentage height. The percentage is calculated with respect to the height of the generated box's containing block. If the height of the containing block is not specified explicitly (i.e., it depends on content height), the value is interpreted like 'auto'. auto
The height depends on the values of other properties. See the prose below.
</vt:p>

<vt:br/>
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_height.jsp
</vt:p>

</vt:pane>

</vt:canvas>
