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
<vt:canvas layoutName="K_TestTextImages" >
<vt:pane name="main">
   <vt:ul>
      <vt:p> These links related to Text Images </vt:p> 
      <vt:ul>
         <vt:li><vt:a href="/volantis/K_TestTextImages.jsp?vfrag=textImage"> .....Test Text Image</vt:a></vt:li>
      </vt:ul>
   </vt:ul>
</vt:pane>

<vt:pane name="textImages">
   <vt:h1>...Testing Text Images.</vt:h1>
   <vt:p>
   <vt:hr/>
   <vt:b>Attributes</vt:b>
   <vt:br/>
   borderWidth-<vt:b>5</vt:b>
   <vt:br/>
   borderType-<vt:b>rectangle</vt:b>
   <vt:br/>
   fontName-<vt:b>Bookman DemiBold Italic</vt:b>
   <vt:br/>
   background-<vt:b>Green</vt:b>
   <vt:br/>
   name-<vt:b>volantisGUITest</vt:b>
   <vt:br/>
   textAlign-<vt:b>SouthEast</vt:b>
   <vt:br/>
   borderColor-<vt:b>Yellow</vt:b>
   <vt:br/>
   imageHeight-<vt:b>100</vt:b>
   <vt:br/>
   imageWith-<vt:b>400</vt:b>
   <vt:br/>
   textColor-<vt:b>Red</vt:b>
   <vt:br/>
   text-<vt:b>Volantis GUI Test</vt:b>
   <vt:br/>
   fontSize-<vt:b>40</vt:b>
   <vt:hr/>
   <vt:b> Volantis Tag</vt:b>
   <vt:br/>
   src-<vt:b>volantisGUITest</vt:b>
    <vt:br/>
   altText-<vt:b>Text Image Test</vt:b>
   </vt:p>
   <vt:hr/>
   <vt:logo src="volantisGUITest" altText="Text Image Test" />
</vt:pane>

</vt:canvas>
