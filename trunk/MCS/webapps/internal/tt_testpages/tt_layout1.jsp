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
<vt:canvas layoutName="tt_layoutwidth1">

<vt:pane name="fileinfo">
  <vt:h1>Layout Width Test 1</vt:h1>
  <vt:p>Filename: tt_layout1.jsp<vt:br/>
  	Layout: tt_layoutwidth1<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p> This test is to confirm that the pane and grid width settings 
	 function correctly.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices except mobiles.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>
     A series of panes should be displayed. 
     The yellow background shows the limits of the pane.
  </vt:p>
  <vt:p>
     There are four width attributes that affect how a pane is displayed.
  </vt:p>
  <vt:p>Pane -&gt; Properties -&gt; Width </vt:p>
  <vt:p>Pane -&gt; Grid -&gt; Column Width</vt:p>
  <vt:p>Grid -&gt; Properties -&gt; Width 
        (of the grid that contains the pane) 
  </vt:p>
  <vt:p>Grid -&gt; Grid -&gt; Column Width 
        (of the Grid that contains the Pane) 
  </vt:p>
  <vt:p>Pane 1 - Yellow should extend 100% of browser window.
        <vt:br/>
	Pane 3 - Yellow should extend 75% of the browser window
  </vt:p>
  <vt:h3>Try resizing the browser to check the panes resize OK.</vt:h3>
  <vt:p>No formatting or styles are applied</vt:p>
</vt:pane>

<vt:p pane="mobileheader">Mobiles are not wide enough to verify this test.
</vt:p>

<vt:pane name="links">
  <vt:p>
  <vt:a href="tt_LayoutIndex.jsp">Layout Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
<vt:p>Pane 1 - GridCol=100 GridProp=100 PaneCol=100 PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test2">
<vt:p>Pane 2 - GridCol=100 GridProp=100 PaneCol=100 PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test3">
<vt:p>Pane 3 - GridCol=100 GridProp=100 PaneCol=100 PaneProp=75</vt:p>
</vt:pane>


<vt:pane name="test4">
<vt:p>Pane 4 - GridCol=100 GridProp=100 PaneCol=null PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test5">
<vt:p>Pane 5 - GridCol=100 GridProp=100 PaneCol=null PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test6">
<vt:p>Pane 6 - GridCol=100 GridProp=100 PaneCol=null PaneProp=75</vt:p>
</vt:pane>


<vt:pane name="test7">
<vt:p>Pane 7 - GridCol=100 GridProp=100 PaneCol=75 PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test8">
<vt:p>Pane 8 - GridCol=100 GridProp=100 PaneCol=75 PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test9">
<vt:p>Pane 9 - GridCol=100 GridProp=100 PaneCol=75 PaneProp=75</vt:p>
</vt:pane>


<vt:pane name="test10">
<vt:p>Pane 10 - GridCol=null GridProp=null PaneCol=100 PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test11">
<vt:p>Pane 11 - GridCol=null GridProp=null PaneCol=100 PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test12">
<vt:p>Pane 12 - GridCol=null GridProp=null PaneCol=100 PaneProp=75</vt:p>
</vt:pane>

<vt:pane name="test13">
<vt:p>Pane 13 - GridCol=null GridProp=null PaneCol=null PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test14">
<vt:p>Pane 14 - GridCol=null GridProp=null PaneCol=null PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test15">
<vt:p>Pane 15 - GridCol=null GridProp=null PaneCol=null PaneProp=75</vt:p>
</vt:pane>

<vt:pane name="test16">
<vt:p>Pane 16 - GridCol=null GridProp=null PaneCol=75 PaneProp=100</vt:p>
</vt:pane>

<vt:pane name="test17">
<vt:p>Pane 17 - GridCol=null GridProp=null PaneCol=75 PaneProp=null</vt:p>
</vt:pane>

<vt:pane name="test18">
<vt:p>Pane 18 - GridCol=null GridProp=null PaneCol=75 PaneProp=75</vt:p>
</vt:pane>

</vt:canvas>
