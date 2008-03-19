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

<vt:canvas theme="tt_linegap" layoutName="standard" pageTitle="Themes: Format" >

<vt:pane name="main2">
<vt:p>
filename: tt_linegap.jsp<vt:br/>
theme: tt_linegap<vt:br/>
layout: standard<vt:br/>
Devices: Sky Wap TV
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_format.jsp">Format</vt:a> =>
tt_linegap.jsp
</vt:p>


<vt:p>This page tests the linegap property. This can be found in the Format Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.linegap1{:5.0px}
td.linegap2{:10.0px}
p.linegap3{:20.0px}
<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: no effect<vt:br/>
Wap TV:<vt:br/>
linegap1 on td tag => td linegap="5.0"<vt:br/>
linegap1 on p tag => p linegap="5.0"<vt:br/>
linegap2 on td tag => td linegap="10.0"<vt:br/>
linegap3 on p tag => tr linegap="20.0"<vt:br/>
</vt:p>


<vt:table cols="1">
<vt:tr>
<vt:td styleClass="linegap1">
This td has styleClass=linegap1
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:p styleClass="linegap1">
This p tag has styleClass="linegap1"
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="linegap2">
This td has styleClass=linegap2.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:p styleClass="linegap3">
This p tag has styleClass=linegap3
</vt:p>

</vt:pane>

</vt:canvas>

