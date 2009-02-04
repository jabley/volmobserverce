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
<vt:canvas layoutName="testaudio">
<vt:h2 pane="mainpane">Here is a simple piece of video content</vt:h2> 
<vt:realvideo pane="mainpane" 
              name="test"
              height="125"
              width="275"
              controls="ImageWindow" 
              autoStart="true"/>
<vt:p pane="mainpane">
Here is a classic video clip.
</vt:p>
</vt:canvas>
