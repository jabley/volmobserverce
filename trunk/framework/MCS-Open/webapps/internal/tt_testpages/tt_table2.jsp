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
	Name  	Date  		Comment
	MJ	08/10/2001	Added this header
				Ensured all text is enclosed in p or h tags

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>   
<%@ page import="com.volantis.mcs.styles.*" %>
<%@ page import="com.volantis.mcs.context.*, 
  com.volantis.mcs.repository.RepositoryConnection" %>

<vt:canvas layoutName="tt_table">

<vt:pane name="fileinfo">
  <vt:h1>Layout Border and Spacing Test 1</vt:h1>
  <vt:p>Filename: tt_table2.jsp<vt:br/>
  	Layout: tt_table<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to clarify table border, cell spacing and background color
	attributes using multipane layouts to create the table.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>The table is generated with a GUI Layout containing several panes.
	The grid has a purple background, a cell spacing of 25px, and
	a black border of 10px.
	</vt:p>
  <vt:p>The first row panes test the horizontal align pane property.</vt:p>
  <vt:p>The second row panes test the background color pane property.</vt:p>
  <vt:p>The third row panes test cell padding of 10px.</vt:p>
  <vt:h3>Try resizing the browser to check the table resizes OK.</vt:h3>
</vt:pane>

<vt:pane name="mobileheader">
<vt:p>	Mobile devices dont support table text alignment 
	very well. Text will always be left aligned except on the
	Ericsson R380 which will align columns, not individual cells.
</vt:p>
</vt:pane>


<vt:pane name="links">
  <vt:p>
  <vt:a href="tt_TableIndex.jsp">Table Index</vt:a>
  </vt:p>
</vt:pane>


<vt:pane name="test">
  <vt:p>The table above is created with the GUI Layout.</vt:p>
</vt:pane>

<vt:pane name="r1c1">
  <vt:p>center</vt:p>
</vt:pane>
 
<vt:pane name="r1c2">
  <vt:p>right</vt:p>
</vt:pane>

<vt:pane name="r2c1">
  <vt:p>the red pane</vt:p>
</vt:pane>

<vt:pane name="r2c2">
  <vt:p>The blue pane</vt:p>
</vt:pane>

<vt:pane name="r3c1">
  <vt:p>left</vt:p>
</vt:pane>

<vt:pane name="r3c2">
  <vt:p>left</vt:p>
</vt:pane>


</vt:canvas>
