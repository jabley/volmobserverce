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
<vt:canvas layoutName="K_TestGUI" >
<vt:logo pane="main" src="GUILinks" altText="Volantis GUI Test Links" />
<vt:pane name="main">
<vt:p> This page is designed to test the Volantis GUI. Volantis GUI contain four main area, which are Layout, Components,Device and Theme</vt:p>
   <vt:ul>
      <vt:li><vt:a href="http://pike/shares/dept/devel/VolantisGUItest.doc"> GUI Test Documentation</vt:a></vt:li>
      <vt:hr />
      <vt:li><vt:a href="/volantis/K_TestComponents.jsp"> Test Components</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestDevices.jsp"> Test Devices(Not Available)</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestLayouts.jsp"> Test Layouts</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestThemes.jsp"> Test Themes</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestTextImages.jsp"> Test Text Images</vt:a></vt:li>
      
   </vt:ul>

</vt:pane>

</vt:canvas>
