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
<vt:canvas layoutName="iDTVtables" theme="iDTV" pageTitle="TV Tables Test">

<vt:pane name="pane1">
<vt:table cols="1" title="Page Info Table">
<vt:tr title="TR Title">
	<vt:td title="TD title">
		<vt:h2>TV Tables Test Page</vt:h2>
		filename: TVtables.jsp<vt:br/>
		purpose: nested tables, rowgap, colgap and Style Info.<vt:br/>
		devices: Sky<vt:br/>
		layout: iDTVtables<vt:br/>
		theme: iDTV<vt:br/>
		<vt:br/>
		This is within a vt:table. all above info in cell 1.
	</vt:td>
</vt:tr>
<vt:tr>
	<vt:td>This is table cell 2.<vt:br/>
		the following style attributes apply to table tag:<vt:br/>
		rowgap=2, hpad=5, vpad=2, hspace=3, vspace=4<vt:br/>
		and the following to the td tag:<vt:br/>
		linegap=4, hpad=5, vpad=2, hspace=2, vspace=1<vt:br/>	
	</vt:td>
</vt:tr>
</vt:table>
<vt:p>
This is a paragraph in pane 1 (within vt:p tags).<vt:br/>
Paragraph tag style attributes: linegap=1.<vt:br/>
Body tag style attributes: paragap=10 (emulated using vspace=5 in td tag surrounding paragraph text)<vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="pane2">
  <vt:h3>This is Pane 2 <vt:br/>
	(h3, cellpadding=4 and bg=red defined in layout)</vt:h3>
</vt:pane>

<vt:pane name="pane3">
  <vt:h3>This is Pane 3 <vt:br/>
	(h3, cellspacing=6 defined in layout)</vt:h3>
</vt:pane>

<vt:pane name="pane4">
<vt:a href="TVindex.jsp">Return to TV Test Pages Index</vt:a>
</vt:pane>

</vt:canvas>

