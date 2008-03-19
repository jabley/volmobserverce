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
<vt:canvas layoutName="TableTestLayout" theme="andrew">

<vt:table cols="3" title="Test of tables" pane="T1">

<vt:tr bgColor="#00FF00">
<vt:td>A green row.</vt:td>
<vt:td bgColor="#FFFF00">This cell should be yellow, overriding the row color.</vt:td> 
<vt:td>Back to the row color.</vt:td>
</vt:tr>

<vt:tr bgColor="#0000FF">
<vt:td>A blue row.</vt:td>
<vt:td> </vt:td>
<vt:td rowSpan="2">This cell takes the color of the topmost row that it spans</vt:td>
</vt:tr>

<vt:tr bgColor="#FF0000">
<vt:td>A red row.</vt:td>
</vt:tr>

<vt:tr bgColor="#00FF00">
<vt:td colSpan="3" >A red row. spanning all columns</vt:td>
</vt:tr>

<vt:tr styleClass="blueRow">
<vt:td colSpan="2" >A blue row. spanning 2 columns</vt:td>
<vt:td colSpan="1" >A blue row. spanning 1 columns</vt:td>
</vt:tr>

</vt:table> 

<vt:p pane="T1">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Tables.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>
