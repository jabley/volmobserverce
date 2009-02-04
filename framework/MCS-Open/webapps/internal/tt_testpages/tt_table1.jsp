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
<vt:canvas layoutName="tt_table">

<vt:pane name="fileinfo">
  <vt:h1>Simple Table Test</vt:h1>
  <vt:p>Filename: tt_table1.jsp<vt:br/>
  	Layout: tt_table<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:a href="index.jsp">Main Index</vt:a><vt:br/>
  <vt:a href="tt_tables.jsp">Tables Index</vt:a><vt:br/>
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to confirm that the vt:table 
	 and associated tags can be used to create a simple table.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices that display pages visually.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A table should be displayed.</vt:p>
  <vt:p>The vt:table tag has a title attribute of 
	'Community Courses'.
  </vt:p>
  <vt:p>The first row contains a vt:th tag with the text
	'Bath Technical College'.
  </vt:p>
  <vt:p>The second row contains 5 separate &lt;vt:th&gt; tags.</vt:p>
  <vt:h3>Try resizing the browser to check the table resizes OK.</vt:h3>
  <vt:p>No formatting or styles are applied.</vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_TableIndex.jsp">Table Index</vt:a>
  </vt:p>
</vt:pane>


<vt:table pane="test" cols="5" title="Community Courses">
	 
  <vt:tr>
    <vt:th>Bath Technical College</vt:th>
  </vt:tr>
	
  <vt:tr>
    <vt:th>Course Name</vt:th>
    <vt:th>Course Tutor</vt:th>
    <vt:th>Summary</vt:th>
    <vt:th>Code</vt:th>
    <vt:th>Fee</vt:th>
  </vt:tr>
	
  <vt:tr>
    <vt:td>After the Civil War</vt:td>
    <vt:td>Dr. John Wroughton</vt:td>
    <vt:td>The course will examine the turbulent years in England
	   after 1646. 6 weekly meetings starting Monday 13th
	   October.</vt:td>
    <vt:td>H27</vt:td>
    <vt:td>82.00</vt:td>
  </vt:tr>
	
  <vt:tr>
     <vt:td>An Introduction to Anglo-Saxon England</vt:td>
     <vt:td>Mark Cottle</vt:td>
     <vt:td>One day course introducing the early medieval
	    period reconstruction the Anglo-Saxons and
	    their society. Saturday 18th October.</vt:td>
      <vt:td>H28</vt:td>
      <vt:td>38.00</vt:td>
   </vt:tr>

   <vt:tr>
     <vt:td>The Glory that was Greece</vt:td>
     <vt:td>Valerie Lorenz</vt:td>
     <vt:td>Birthplace of democracy, philosophy, heartland of theater, home of
	    argument. The Romans may have done it but the Greeks did it
	    first. Saturday day school 25th October 1997</vt:td>
     <vt:td>H30</vt:td>
     <vt:td>30.00</vt:td>
   </vt:tr>

</vt:table>

</vt:canvas>
