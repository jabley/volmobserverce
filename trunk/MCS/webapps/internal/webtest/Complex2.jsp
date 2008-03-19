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
<vt:canvas layoutName="Complex2">

<vt:pane name="welcome">

<vt:p>This is the welcome pane.</vt:p>

<vt:ol pane="shop" styleClass="someclass" id="someid" title="Shopping choices" start="7">
<vt:li title="sometitle" id="someid" styleClass="someclass">First item in the list, should be numbered 7</vt:li>
<vt:li title="title2" id="id2" styleClass="class2">Second item</vt:li>
<vt:li>etc</vt:li>
<vt:li>All should be in the shop pane, and are inside an ordered list.</vt:li>
</vt:ol>


<vt:logo pane="logo1" src="stars"/>
<vt:p pane="logo1">Should be the stars above, both inside logo1 pane.</vt:p>

<vt:logo pane="logo2" src="volantis"/>
<vt:p pane="logo2">Should be volantis above, both inside logo2 pane.</vt:p>

<vt:dl pane="headlines" styleClass="someclass" id="someid" title="A definition list in the Headlines pane">
<vt:dt title="sometitle" id="someid" styleClass="someclass">Term 1</vt:dt>
<vt:dd>The definition of the term 1</vt:dd>
<vt:dt title="title2" id="id2" styleClass="style2">Term 2</vt:dt>
<vt:dd>The definition of term 2</vt:dd>
</vt:dl>

<vt:p pane="cpynews">The company news pane</vt:p>

</vt:pane>

<vt:p pane="adverts">This should appear inside the adverts pane.</vt:p>

<vt:p pane="adverts">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>

</vt:canvas>

