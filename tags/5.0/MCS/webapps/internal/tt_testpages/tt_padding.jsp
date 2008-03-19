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

<vt:canvas theme="tt_padding" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_padding.jsp<vt:br/><vt:br/>
theme: tt_padding<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>This page tests the padding properties. This can be found in the box Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.padding1{background-color:#ccf;padding:5.0px}<vt:br/>
.padding2{background-color:#cf9;padding:1.0px 2.0px 3.0px 4.0px}<vt:br/>
.padding3{background-color:#fcf;padding-top:4.0px;padding-bottom:7.0px}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above<vt:br/>
Wap TV:<vt:br/>
canvas => card vspace="5" (average of padding-top and padding-bottom)<vt:br/>
padding1 on table tag => table vpad="5" hpad="5" bgcolor="#ccccff"<vt:br/>
padding2 on td tag => td bgcolor="#ccff99" hpad="3" vpad="2"<vt:br/>
padding3 on p tag => none<vt:br/>
padding1 on img tag => none<vt:br/>
</vt:p>

<vt:div styleClass="border">
<vt:table cols="1" styleClass="padding1">
<vt:tr>
<vt:td>
This table has styleClass=padding1.
</vt:td>
</vt:tr>
</vt:table>
</vt:div>

<vt:br/>

<vt:div styleClass="border">
<vt:table cols="1">
<vt:tr>
<vt:td styleClass="padding2">
This is within a table cell which has styleClass=padding2.
</vt:td>
</vt:tr>
</vt:table>
</vt:div>

<vt:br/>

<vt:div styleClass="border">
<vt:p styleClass="padding3">
This text is within p tags that has styleClass=padding3.
</vt:p>
</vt:div>

<vt:br/>

<vt:p>This image has styleClass="padding1"
<vt:div styleClass="border">
<vt:img styleClass="padding1" src="stars" />
</vt:div>
</vt:p>


<vt:br/>
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_padding.jsp
</vt:p>


</vt:pane>

</vt:canvas>

