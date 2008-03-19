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
<vt:canvas layoutName="K_TestThemes" theme="testTheme">
<vt:pane name="main">
   <vt:ul>
      <vt:p> These links related to Theme Testing</vt:p> 
      <vt:li><vt:a href="/volantis/K_TestThemes.jsp?vfrag=backgroundColour"> .....Test background Colour</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestThemes.jsp?vfrag=edges"> .....Test Edges</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestThemes.jsp?vfrag=fonts"> .....Test Fonts</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestThemes.jsp?vfrag=text"> .....Test Text</vt:a></vt:li>
   </vt:ul>
   
</vt:pane>

<%// Test Real Audio %>
<vt:pane name="backgroundColour">
   <vt:h1>Backgroud & Colours...</vt:h1>
   <vt:p id="2">
   <vt:hr/>
   Padding-<vt:b>Scroll</vt:b>
   <vt:br/>
   background-colour-<vt:b>Black</vt:b>
   <vt:br/>
   background-position-<vt:b>none</vt:b>
   <vt:br/>
   background-repeat-no-<vt:b>repeat</vt:b>
   <vt:br/>
   colour-<vt:b>Purple</vt:b></vt:p>
   <vt:hr/>
</vt:pane
<vt:pane name="edges">
   <vt:h1>Edges...</vt:h1>
   <vt:p id="1">
   Padding-bottom-<vt:b>5</vt:b>
   <vt:br/>
   Padding-left-<vt:b>10</vt:b>
   <vt:br/>
   Padding-right-<vt:b>20</vt:b>
   <vt:br/>
   Padding-top-<vt:b>30</vt:b>
   <vt:br/>
   border-bottom-width-<vt:b>thin</vt:b>
   <vt:br/>
   border-left-width-<vt:b>medium</vt:b>
   <vt:br/>
   border-right-width-<vt:b>thick</vt:b>
   <vt:br/>
   border-style-<vt:b>solid</vt:b>
   <vt:br/>
   border-top-width-<vt:b>medium</vt:b></vt:p>
   </vt:pane>
<vt:pane name="fonts">
<vt:h1>Fonts...</vt:h1>
   <vt:p id="2">
   font-family-<vt:b>arial</vt:b>
   <vt:br/>
   font-size-<vt:b>xx-large</vt:b>
   <vt:br/>
   font-style-<vt:b>italic</vt:b>
   <vt:br/>
   font-variant-<vt:b>small-caps</vt:b>
   <vt:br/>
   font-weight-<vt:b>bold</vt:b></vt:p>
</vt:pane>
<vt:pane name="text">
<vt:h1>Text...</vt:h1>
   <vt:p id="4">
   letter-spacing-<vt:b>norman</vt:b>
   <vt:br/>
   line-height-<vt:b>nomal</vt:b>
   <vt:br/>
   text-align-<vt:b>centre</vt:b>
   <vt:br/>
   text-decoration-<vt:b>overline</vt:b>
   <vt:br/>
   text-indent-<vt:b>200</vt:b>
   <vt:br/>
   text-transform-<vt:b>capitalise</vt:b>
   <vt:br/>
   white-space-<vt:b>normal</vt:b>
   <vt:br/>
   word-spacing-<vt:b>normal</vt:b>
   </vt:p>
</vt:pane>

</vt:canvas>
