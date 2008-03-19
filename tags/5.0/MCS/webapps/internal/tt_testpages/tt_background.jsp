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
<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_background" layoutName="standard" pageTitle="Themes: background" >

<vt:pane name="main2">
<vt:p>
filename: tt_background.jsp<vt:br/><vt:br/>
The body background should be blue, with the stars image as a bg image (right, repeat-y)</vt:p>
<vt:table cols="2">
  <vt:tr>
   <vt:td styleClass="color">Table Cell, BG=white</vt:td>
   <vt:td styleClass="image" height="100">Table Cell, BG=volantis image</vt:td>
  </vt:tr>
</vt:table>
<vt:a href="tt_themes.jsp">Themes Index</vt:a><vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:pane>

</vt:canvas>

