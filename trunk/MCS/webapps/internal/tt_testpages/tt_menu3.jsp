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
<vt:canvas 	layoutName="tt_iterator"
		theme="tt_orientation">

<vt:pane name="fileinfo">
  <vt:h1>Rollover Image Menu Test 1</vt:h1>
  <vt:p>Filename: tt_menu3.jsp<vt:br/>
  	Layout: tt_iterator<vt:br/>
	Theme: tt_orientation
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To confirm that the vt:menu tag correctly processes the
	onimage and offimage attributes and the
        horizontal and vertical style properties 
	in normal, row and column iterated panes.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A series of three menu options should be displayed, first
        horizontally, and then vertically.
  </vt:p>
  <vt:p>The results are displayed in a normal pane, a row iterator pane, 
        and a column iterator pane. 
  </vt:p>
</vt:pane>

<vt:pane name="mobileheader">
<vt:p>Note that Nokia phones always display menus vertically.</vt:p>
</vt:pane>


<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_menus.jsp">Menu Index</vt:a><vt:br/>
    <vt:a href="index.jsp">Main Index</vt:a><vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="noniter">
  <vt:p>Should be two images below. If the images are displayed, 
        the menus should appear with rollover images.
  </vt:p>
</vt:pane>
 
<vt:img pane="noniter" src="tt_bluebox1" 
    altText="tt_bluebox1 should be displayed here."/>
<vt:img pane="noniter" src="tt_bluebox2" 
    altText="tt_bluebox2 should be displayed here."/>


<vt:pane name="noniter">
   <vt:p>This is the normal non-iterated pane.</vt:p>
</vt:pane>

<vt:menu pane="noniter" type="rolloverimage" styleClass="orienthoriz">
 <vt:menuitem href="horizthis.html" text="HorizThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="horizthat.html" text="HorizThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="horizother.html" text="HorizOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>
 
<vt:pane name="noniter">
	<vt:br/>
</vt:pane>

<vt:menu pane="noniter" type="rolloverimage" styleClass="orientvert">
 <vt:menuitem href="vertthis.html" text="VertThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertthat.html" text="VertThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertother.html" text="VertOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>


<vt:pane name="rowiter">
   <vt:p>This is the row iterated pane.</vt:p>
</vt:pane>

<vt:menu pane="rowiter" type="rolloverimage" styleClass="orienthoriz">
 <vt:menuitem href="horizthis.html" text="HorizThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="horizthat.html" text="HorizThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
<vt:menuitem href="horizother.html" text="HorizOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>

<vt:pane name="rowiter">
	<vt:br/>
</vt:pane>

<vt:menu pane="rowiter" type="rolloverimage" styleClass="orientvert">
 <vt:menuitem href="vertthis.html" text="VertThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertthat.html" text="VertThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertother.html" text="VertOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>

<vt:p pane="coliter">
This is the colum iterated pane.
</vt:p> 

<vt:menu pane="coliter" type="rolloverimage" styleClass="orienthoriz">
 <vt:menuitem href="horizthis.html" text="HorizThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="horizthat.html" text="HorizThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="horizother.html" text="HorizOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>

<vt:pane name="coliter">
	<vt:br/>
</vt:pane>

<vt:menu pane="coliter" type="rolloverimage" styleClass="orientvert">
 <vt:menuitem href="vertthis.html" text="VertThis" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertthat.html" text="VertThat" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
 <vt:menuitem href="vertother.html" text="VertOther" 
     offImage="tt_bluebox1" onImage="tt_bluebox2"/>
</vt:menu>

  
</vt:canvas>
