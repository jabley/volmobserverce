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

<vt:canvas theme="tt_textalign" layoutName="standard" pageTitle="Themes: Text">

<vt:pane name="main2">
<vt:p>
filename: tt_textalign.jsp<vt:br/>
theme: tt_textalign<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_text.jsp">Text</vt:a> =>
tt_textalign.jsp
</vt:p>


<vt:p>This page tests the text align property. This can be found in the Text Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.textalign1 {text-align:center}<vt:br/>
td.textalign2 {text-align:right}<vt:br/>
p.textalign3 {text-align:right}<vt:br/>
.textalign4 {text-align:justify}<vt:br/>
.textalign5 {text-align:left}
<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: no effect<vt:br/>
Wap TV:<vt:br/>
textalign1 on td tag => td align="C"<vt:br/>
textalign1 on p tag => td align="C"<vt:br/>
textalign2 on td tag => td textalign="R"<vt:br/>
textalign3 on p tag => td textalign="R"<vt:br/>
textalign4 on table tag => td textalign="L"<vt:br/>
textalign4 on td tag => td textalign="L"<vt:br/>
textalign5 on table tag => td textalign="L"<vt:br/>
textalign5 on td tag => td textalign="L"<vt:br/>
</vt:p>


<vt:table cols="1">
<vt:tr>
<vt:td styleClass="textalign1">
This td has styleClass=textalign1
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:p styleClass="textalign1">
This p tag has styleClass="textalign1"
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="textalign2">
This td has styleClass=textalign2.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:p styleClass="textalign3">
This p tag has styleClass=textalign3
</vt:p>

<vt:br/>

<vt:table cols="1" styleClass="textalign4">
<vt:tr>
<vt:td>
This table has styleClass=textalign4.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1" styleClass="textalign5">
<vt:tr>
<vt:td>
This table has styleClass=textalign5.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1" styleClass="textalign1">
<vt:tr>
<vt:td>
This table has styleClass=textalign1.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="textalign4">
This td has styleClass=textalign4.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="textalign5">
This td has styleClass=textalign5.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:a href="tt_themes.jsp">Themes Index</vt:a><vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:pane>

</vt:canvas>
