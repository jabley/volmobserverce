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
<vt:canvas layoutName="MenuTestLayout" theme="menus1">

<vt:pane name="M1">
 <vt:p> This example is a horizontal image rollover menu. </vt:p> 
</vt:pane> 

<vt:menu pane="M1" type="rolloverimage" styleClass="someClass">
 <vt:menuitem href="AllForms.jsp" onImage="stars" offImage="volantis" text="Forms Test Pages"/>
 <vt:menuitem href="Tables.jsp" onImage="stars" offImage="volantis" text="Tables test."/>
 <vt:menuitem href="Layouts.jsp" onImage="stars" offImage="volantis" text="Layouts Test Pages"/>
 <vt:menuitem href="Mixed.jsp" onImage="stars" offImage="volantis" text="Mixed Test Pages"/>
 <vt:menuitem href="Performance.jsp" onImage="stars" offImage="volantis" text="Performance Test Pages"/>
</vt:menu>

<vt:p pane="M1">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Menus.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>
