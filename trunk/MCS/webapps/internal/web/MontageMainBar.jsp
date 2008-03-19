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
<vt:canvas layoutName="MontageMainBar">

  <vt:menu pane="mainbar" type="rolloverimage" orientation="horizontal">
  <vt:menuitem text="menu1" href="MontageMenu1.jsp" segment="menu" onImage="row" offImage="column"/>
  <vt:menuitem text="link2" href="link2.jsp" segment="menu" onImage="row" offImage="column"/>
  <vt:menuitem text="link3" href="link3.jsp" onImage="row" offImage="column"/>
  <vt:menuitem text="link4" href="link4.jsp" onImage="row" offImage="column"/>
  </vt:menu>

</vt:canvas>
