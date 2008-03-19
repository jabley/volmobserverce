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
<vt:canvas layoutName="TLayout" >

<vt:pane name="11">
<vt:p>
Hello
</vt:p>

<vt:p>
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Layouts.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:pane>

<vt:pane name="12">
<vt:p>
Yazooooo
</vt:p>
</vt:pane>

<vt:pane name="13">
<vt:p>Whazzzzzuppp</vt:p>
</vt:pane>

<vt:pane name="21">
<vt:p>Eaaaakkkkk</vt:p>
</vt:pane>

<vt:pane name="22">
<vt:p>Kaaaaiiiii</vt:p>
</vt:pane>

<vt:pane name="23">
<vt:p>Tikaaaaaaaaaaa</vt:p>
</vt:pane>

<vt:pane name="31">
<vt:p>Boiz</vt:p>
</vt:pane>

<vt:pane name="32">
<vt:p>in da</vt:p>
</vt:pane>

<vt:pane name="33">
<vt:p>hood</vt:p>
</vt:pane>

<%-- using TLayout2 intead of TLayout, we have an extra pane called "message". However, it is illegal
 to have that layout for WMLWideScreen since in order to make that it would be needed to have
a grid inside a grid, therefore, a table inside a table, which is illegal for WML --%>

<vt:pane name="message">
<vt:p>
If protocol is set to WMLWideScreen, the underlying implementation of the panes above should be
with a 3x3 table. If however, the protocol is set to an ordinary WML, the underlying implementation
of the panes above will be just paragraphs.
</vt:p>
</vt:pane>


</vt:canvas>


