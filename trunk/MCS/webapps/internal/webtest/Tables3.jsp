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
<vt:canvas layoutName="TableTestLayout">

<vt:table cols="2" title="This table lists SI list" pane="T1" >
	     <vt:thead id="thead id" title="thead title" align="right" >
	       <vt:tr>
		 <vt:th>Name</vt:th>
		 <vt:th>Symbol</vt:th>
		 <vt:th>Quantity</vt:th>
	       </vt:tr>
	     </vt:thead>
	     <vt:tbody id="12" title="tbody">
	       <vt:tr>
		 <vt:td>meter</vt:td>
		 <vt:td>m</vt:td>
		 <vt:td>length</vt:td>
	       </vt:tr>
	       <vt:tr>
		 <vt:td>kilogram</vt:td>
		 <vt:td>kg</vt:td>
		 <vt:td>mass</vt:td>
	       </vt:tr>
	     </vt:tbody>
	     <vt:tbody>
	       <vt:tr>
		 <vt:td>hertz</vt:td>
		 <vt:td>Hz</vt:td>
		 <vt:td>frequency</vt:td>
	       </vt:tr>
	       <vt:tr>
		 <vt:td>pascal</vt:td>
		 <vt:td>Pa</vt:td>
		 <vt:td>pressure</vt:td>
	       </vt:tr>
	     </vt:tbody>
	     <vt:tbody>
	       <vt:tr>
		 <vt:td>radian</vt:td>
		 <vt:td>rad</vt:td>
		 <vt:td>plane angle</vt:td>
	       </vt:tr>
	     
	     </vt:tbody>
	   </vt:table>

<vt:p pane="T1">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Tables.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>
