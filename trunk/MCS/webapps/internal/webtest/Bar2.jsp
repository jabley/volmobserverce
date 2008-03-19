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
<vt:canvas layoutName="error">

<vt:menu pane="error" type="plaintext" >
<vt:menuitem text="Hellooo! " href="Content2.jsp?variable=hello" segment="content" />   
<vt:menuitem text="Whaz up? " href="Content2.jsp?variable=up" segment="content" />  
<vt:menuitem text="Goodbye! " href="Content2.jsp?variable=bye" segment="content" />   
</vt:menu>

<vt:p pane="error">
<vt:br/>
<vt:a href="index.jsp" segment="_top">Home</vt:a>
<vt:a href="Performance.jsp" segment="_top">Performance pages</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>
