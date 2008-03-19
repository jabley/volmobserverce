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
<vt:canvas layoutName="Complex6">

<vt:logo pane="logo1" src="stars" styleClass="someclass" />
<vt:p pane="logo1">Should be the stars above, both in logo1 pane</vt:p> 

<vt:logo pane="logo2" src="volantis"/>
<vt:p pane="logo2">Should be volantis above, both in the logo2 pane</vt:p>

<vt:pane name="welcome">
<vt:p>The welcome pane. Should be a horizontal rule below.
<vt:hr title="sthg" id="someid" styleClass="someclass"/>
Should be a horizontal rule above.</vt:p>
</vt:pane>

<vt:pre pane="shop">Display without interpretation,<vt:br/> in the Shop
pane.</vt:pre>

<vt:address pane="adverts">Treat this as an address. This is inside the adverts pane.</vt:address>

<vt:p pane="cpynews">Text before the block-quote <vt:blockquote>This is some text inside a block quotation, in the
cpynews pane. </vt:blockquote> text after the block-quote</vt:p>


<vt:pane name="headlines">
<vt:p>These are the headlines, rollover text menu below, <vt:br/>
oncolor blue, offcolor varies.</vt:p>
</vt:pane>

<vt:menu pane="headlines" type="rollovertext">
<vt:menuitem text="Bush OR Gore ?" href="elections.jsp" onColor="blue"
offColor="red"/>
<vt:menuitem text="Bush OR Pearl Jam ?" href="concert.jsp" onColor="blue"
offColor="yellow"/> 
<vt:menuitem text="Bush OR Tree ?" href="gardening.jsp" onColor="blue"
offColor="magenta"/> 
</vt:menu>


<vt:p pane="adverts">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>


</vt:canvas>

