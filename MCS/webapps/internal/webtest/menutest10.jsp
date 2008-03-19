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
<vt:canvas 	layoutName="menutest10"
		theme="menutest10"   
		pageTitle="Menu Test 10">

<vt:pane name="main">
  <vt:h1>Purpose</vt:h1>
  <vt:p>This test is to confirm that the &lt;vt:menu&gt; 
	and &lt;vt:menuitem&gt; tags
        can be used to create a horizontal and vertical menu.
  </vt:p>
  <vt:h1>Devices</vt:h1>
  <vt:p>This page should be tested on all devices that display pages visually.
  </vt:p>
  <vt:h1>Expected Results</vt:h1>
  <vt:p>A series of three menu options should be displayed, first
        horizontally, and then vertically.
  </vt:p>
</vt:pane>

<vt:pane name="noniter">
	<vt:p>This is the noniter pane.
	</vt:p>
</vt:pane>

<vt:menu pane="noniter" type="plaintext" styleClass="orienthoriz">
	<vt:menuitem href="this.html" text="HorizThis"/>
	<vt:menuitem href="that.html" text="HorizThat"/>
	<vt:menuitem href="other.html" text="HorizOther"/>
</vt:menu>
 
<vt:pane name="noniter">
	<vt:br/>
</vt:pane>

<vt:menu pane="noniter" type="plaintext" styleClass="orientvert">
	<vt:menuitem href="this.html" text="VertThis"/>
	<vt:menuitem href="that.html" text="VertThat"/>
	<vt:menuitem href="other.html" text="VertOther"/>
</vt:menu>


<vt:pane name="rowiter">
	<vt:p>This is the rowiter pane.
	</vt:p>
</vt:pane>

<vt:menu pane="rowiter" type="plaintext" styleClass="orienthoriz">
	<vt:menuitem href="this.html" text="HorizThis"/>
	<vt:menuitem href="that.html" text="HorizThat"/>
	<vt:menuitem href="other.html" text="HorizOther"/>
</vt:menu>

<vt:pane name="rowiter">
	<vt:br/>
</vt:pane>

<vt:menu pane="rowiter" type="plaintext" styleClass="orientvert">
	<vt:menuitem href="this.html" text="VertThis"/>
	<vt:menuitem href="that.html" text="VertThat"/>
	<vt:menuitem href="other.html" text="VertOther"/>
</vt:menu>



<vt:pane name="coliter">
	<vt:p>This is the coliter pane.
	</vt:p>
</vt:pane>

<vt:menu pane="coliter" type="plaintext" styleClass="orienthoriz">
	<vt:menuitem href="this.html" text="HorizThis"/>
	<vt:menuitem href="that.html" text="HorizThat"/>
	<vt:menuitem href="other.html" text="HorizOther"/>
</vt:menu>

<vt:pane name="coliter">
	<vt:br/>
</vt:pane>

<vt:menu pane="coliter" type="plaintext" styleClass="orientvert">
	<vt:menuitem href="this.html" text="VertThis"/>
	<vt:menuitem href="that.html" text="VertThat"/>
	<vt:menuitem href="other.html" text="VertOther"/>
</vt:menu>
  
</vt:canvas>
