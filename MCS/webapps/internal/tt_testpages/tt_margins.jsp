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

<vt:canvas theme="tt_margins" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_margins.jsp<vt:br/><vt:br/>
theme: tt_margins<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>This page tests the margin properties. This can be found in the box Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
canvas{margin-top:10.0px}<vt:br/>
.margin1{background-color:#ccf;margin:5.0px}<vt:br/>
.margin2{background-color:#cf9;margin:1.0px 2.0px 3.0px 4.0px}<vt:br/>
.margin3{background-color:#fcf;margin-top:4.0px;margin-bottom:7.0px}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above, except canvas => .VE-canvas<vt:br/>
Wap TV:<vt:br/>
canvas => card vspace="5" (average of margin-top and margin-bottom)<vt:br/>
margin1 on table tag => table vspace="5" hspace="5" bgcolor="#ccccff"<vt:br/>
margin2 on td tag => td bgcolor="#ccff99" hspace="3" vspace="2"<vt:br/>
margin3 on p tag => none<vt:br/>
margin1 on img tag => none<vt:br/>
</vt:p>

<vt:div styleClass="border">
<vt:table cols="1" styleClass="margin1">
<vt:tr>
<vt:td>
This table has styleClass=margin1.
</vt:td>
</vt:tr>
</vt:table>
</vt:div>

<vt:br/>

<vt:div styleClass="border">
<vt:table cols="1">
<vt:tr>
<vt:td styleClass="margin2">
This is within a table cell which has styleClass=margin2.
</vt:td>
</vt:tr>
</vt:table>
</vt:div>

<vt:br/>

<vt:div styleClass="border">
<vt:p styleClass="margin3">
This text is within p tags that has styleClass=margin3.
</vt:p>
</vt:div>

<vt:br/>

<vt:p>This image has styleClass="margin1"
<vt:div styleClass="border">
<vt:img styleClass="margin1" src="stars" />
</vt:div>
</vt:p>


<vt:br/>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_box.jsp">Box</vt:a> =>
tt_margins.jsp
</vt:p>

</vt:pane>

</vt:canvas>

