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

<vt:canvas theme="tt_bgradius" layoutName="standard" pageTitle="Themes: Format" >

<vt:pane name="main2">
<vt:p>
filename: tt_bgradius.jsp<vt:br/>
theme: tt_bgradius<vt:br/>
layout: standard<vt:br/>
Devices: Sky Wap TV
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_format.jsp">Format</vt:a> =>
tt_bgradius.jsp
</vt:p>

<vt:p>This page tests the corner radius property. This can be found in the Format Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.bgradius1{mariner-corner-radius:5.0px}
.bgradius2{mariner-corner-radius:3.0px}
.bgradius3{mariner-corner-radius:4.0px}
<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: no effect<vt:br/>
Wap TV:<vt:br/>
bgradius1 on table tag => table bgradius="5.0"<vt:br/>
bgradius2 on td tag => td bgradius="3.0"<vt:br/>
bgradius3 on tr tag => tr bgradius="4.0"<vt:br/>
</vt:p>


<vt:table cols="1" styleClass="bgradius1">
<vt:tr>
<vt:td>
This table has styleClass=bgradius1
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="bgradius2">
This is within a table cell which has styleClass=bgradius2.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>


<vt:table cols="1">
<vt:tr styleClass="bgradius3">
<vt:td>
This is within a table row which has styleClass=bgradius3.
</vt:td>
</vt:tr>
</vt:table>

</vt:pane>

</vt:canvas>

