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
        AFP     31/10/02         Created this file

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_bgcolor" layoutName="standard" pageTitle="Themes: background" >

<vt:pane name="main2">
<vt:p>
filename: tt_bgcolor.jsp<vt:br/>
theme: tt_background<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>This page tests the background properties. These are under the Background tab in the Mariner GUI. <vt:br/><vt:br/>
Test:<vt:br/>
canvas{background-color:#69f}<vt:br/>
.background1{background-color:#f6f}<vt:br/>
.background2{background-color:#ff9}<vt:br/>
td.background1:focus{background-color:#0a0}<vt:br/>
td.background1:active{background-color:#a0a}<vt:br/>
tr.background1:active{background-color:#ff0}<vt:br/>
tr.background1:focus{background-color:#faa}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above.<vt:br/>
Wap TV:<vt:br/>
</vt:p>



Table 1:<vt:br/>

<vt:table cols="2">
  <vt:tr>
   <vt:td styleClass="background1">This td has styleClass=background1</vt:td>
   <vt:td styleClass="background2">This td has styleClass=background2</vt:td>
  </vt:tr>
</vt:table>


Table 2:<vt:br/>
<vt:table cols="2" styleClass="background2">
  <vt:tr>
   <vt:td>This table has styleClass=background2</vt:td>
   <vt:td>Table Cell</vt:td>
  </vt:tr>
</vt:table>


Table 3:<vt:br/>
<vt:table cols="2">
  <vt:tr styleClass="background1">
   <vt:td>This tr has styleClass=background1</vt:td>
   <vt:td>Table Cell</vt:td>
  </vt:tr>
 
  <vt:tr>
   <vt:td>Table Cell. tr tag has no bgcolor</vt:td>
   <vt:td>Table Cell</vt:td>
  </vt:tr>
</vt:table>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_background.jsp">Background</vt:a> =>
tt_bgcolor.jsp
</vt:p>

</vt:pane>

</vt:canvas>

