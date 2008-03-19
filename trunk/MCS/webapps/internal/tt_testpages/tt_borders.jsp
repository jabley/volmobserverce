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
        AFP     04/11/02     	 Created this file

      *************************************************************************
-->


<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_borders" layoutName="standard" pageTitle="Themes: Borders" >

<vt:pane name="main2">
<vt:p>
filename: tt_borders.jsp<vt:br/><vt:br/>
theme: tt_borders<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>This page tests the border properties. These are under the Border tab in the Mariner GUI. <vt:br/><vt:br/>
Test:<vt:br/>
.border1{border:1.0px #000 solid}<vt:br/>
.border2{border:2.0px #f00 dashed}<vt:br/>
.borderspacing1{border-spacing:5.0px 25.0px}<vt:br/>
table.borderspacing2{border-spacing:10.0px}<vt:br/>
table.borderspacing3{border-spacing:15.0px}<vt:br/>
td.border1:active{border:1.0px #f0a solid}<vt:br/>
td.border1:focus{border:1.0px #0ff solid}<vt:br/>
tr.border1:active{border:1.0px #0aa solid}<vt:br/>
tr.border1:focus{border:1.0px #dda solid}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above.<vt:br/>
Wap TV:<vt:br/>

</vt:p>

<vt:div styleClass="border1">
This text is within a div tag that has a black solid border set (1px).
</vt:div>

<vt:br/>

<vt:div styleClass="border2">
This text is within a div tag that has a red dashed border.
</vt:div>

<vt:br/>

<vt:table cols="1" styleClass="border1">
<vt:tr>
<vt:td>
This is within a table with a styleClass="border1"
</vt:td>
</vt:tr>
</vt:table>

<vt:table cols="1">
<vt:tr styleClass="border1">
<vt:td>
This is within a table. The tr tag has a styleClass="border1"
</vt:td>
</vt:tr>
</vt:table>


<vt:table cols="1">
<vt:tr>
<vt:td styleClass="border1">
This is within a table. The td tag has a styleClass="border1"
</vt:td>
</vt:tr>
</vt:table>


<vt:table cols="1" styleClass="borderspacing1">
<vt:tr>
<vt:td>
This is within a table. The table tag has a styleClass="borderspacing1"
</vt:td>
</vt:tr>
</vt:table>


<vt:table cols="1" styleClass="borderspacing2">
<vt:tr>
<vt:td>
This is within a table. The table tag has a styleClass="borderspacing2"
</vt:td>
</vt:tr>
</vt:table>


<vt:table cols="1" styleClass="borderspacing3">
<vt:tr>
<vt:td>
This is within a table. The table tag has a styleClass="borderspacing3"
</vt:td>
</vt:tr>
</vt:table>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_border.jsp">Borders</vt:a> =>
tt_borders.jsp
</vt:p>

</vt:pane>

</vt:canvas>

