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
	
	Revision Info 
	Name  	Date  		Comment
	MJ	08/10/2001	Added this header
				Ensured all text is enclosed in p or h tags

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="tt_layoutwidth2">

<vt:pane name="fileinfo">
  <vt:h1>Layout Width Test 2</vt:h1>
  <vt:p>Filename: tt_layout2.jsp<vt:br/>
  	Layout: tt_layoutwidth2<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to confirm that the pane and grid width settings 
	function correctly. It is the same as Test 1 but has additional
	text to see where the word wrap break is.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices except mobiles.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A series of panes should be displayed. 
        The yellow background shows the limits of the pane.
  </vt:p>
  <vt:p>There are four width attributes that affect how a pane is displayed.
  </vt:p>
  <vt:p>Pane -&gt; Properties -&gt; Width  </vt:p>
  <vt:p>Pane -&gt; Grid -&gt; Column Width  </vt:p>
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

<vt:p pane="test">
Pane 1 - GridCol=100% GridProp=100% PaneCol=100% This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=300p
</vt:p>

<vt:p pane="test2">
Pane 2 - GridCol=100 GridProp=100 PaneCol=100 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test3">
Pane 3 - GridCol=100 GridProp=100 PaneCol=100 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>


<vt:p pane="test4">
Pane 4 - GridCol=100 GridProp=100 PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=100
</vt:p>

<vt:p pane="test5">
Pane 5 - GridCol=100 GridProp=100 PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test6">
Pane 6 - GridCol=100 GridProp=100 PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>


<vt:p pane="test7">
Pane 7 - GridCol=100 GridProp=100 PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=100
</vt:p>

<vt:p pane="test8">
Pane 8 - GridCol=100 GridProp=100 PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test9">
Pane 9 - GridCol=100 GridProp=100 PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>


<vt:p pane="test10">
Pane 10 - GridCol=null GridProp=null PaneCol=100 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=100
</vt:p>

<vt:p pane="test11">
Pane 11 - GridCol=null GridProp=null PaneCol=100 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test12">
Pane 12 - GridCol=null GridProp=null PaneCol=100 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>

<vt:p pane="test13">
Pane 13 - GridCol=null GridProp=null PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=100
</vt:p>

<vt:p pane="test14">
Pane 14 - GridCol=null GridProp=null PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test15">
Pane 15 - GridCol=null GridProp=null PaneCol=null This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>

<vt:p pane="test16">
Pane 16 - GridCol=null GridProp=null PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=100
</vt:p>

<vt:p pane="test17">
Pane 17 - GridCol=null GridProp=null PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=null
</vt:p>

<vt:p pane="test18">
Pane 18 - GridCol=null GridProp=null PaneCol=75 This is some text to see where the word wrap break is. This is some text to see where the word wrap break is. This is sometext to see where the word wrap break is. PaneProp=75
</vt:p>

</vt:canvas>
