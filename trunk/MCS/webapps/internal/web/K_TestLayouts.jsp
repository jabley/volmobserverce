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
<vt:canvas layoutName="K_TestLayouts">
<vt:pane name="main">
   <vt:ul>
      <vt:p> These links related to layout related testing. Please select the appropriate like to see the property setting and the view of it. Pane Name and Fragment Name are usable with volantis tags  </vt:p> 
      <vt:li><vt:a href="/volantis/K_TestLayouts.jsp?vfrag=backgroundColour"> Test Background Colour</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestLayouts.jsp?vfrag=borderWidth"> Test Border Width</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestLayouts.jsp?vfrag=cellPading"> Test Cell Pading</vt:a></vt:li>
      <vt:li><vt:a href="/volantis/K_TestLayouts.jsp?vfrag=verticalHorizontal"> Test vertical and Horizontal alignment of panes</vt:a></vt:li>
      <vt:li><vt:i>Other Properties:Pane name, Parent Fragment, Fragment Name, Link Text, Default Fragment</vt:i></vt:li>
   </vt:ul>

   
</vt:pane>

<%// Test background Colours %>
<vt:pane name="backgroundColour11">
   <vt:p>...Background Colour-<vt:b>Red...</vt:b></vt:p>
</vt:pane>
<vt:pane name="backgroundColour12">
   <vt:p>...Background Colour-<vt:b>Blue...</vt:b></vt:p>
</vt:pane>
<vt:pane name="backgroundColour21">
   <vt:p>...Background Colour-<vt:b>Yellow...</vt:b></vt:p>
</vt:pane>
<vt:pane name="backgroundColour22">
   <vt:p>...Background Colour-<vt:b>Green...</vt:b></vt:p>
</vt:pane>

<%// Test Border Widths %>
<vt:pane name="borderWidth11">
   <vt:p>...Border Width- <vt:b>0</vt:b></vt:p>
</vt:pane>
<vt:pane name="borderWidth12">
   <vt:p>...Border Width- <vt:b>1</vt:b></vt:p>
</vt:pane>
<vt:pane name="borderWidth21">
   <vt:p>...Border Width- <vt:b>2</vt:b></vt:p>
</vt:pane>
<vt:pane name="borderWidth22">
   <vt:p>...Border Width- <vt:b>3</vt:b></vt:p>
</vt:pane>

<%// Test Cell pading %>
<vt:pane name="cellPading11">
   <vt:p>...Cell pading-<vt:b>0</vt:b></vt:p>
</vt:pane>
<vt:pane name="cellPading12">
   <vt:p>...Cell pading-<vt:b>5</vt:b></vt:p>
</vt:pane>
<vt:pane name="cellPading21">
   <vt:p>...Cell pading-<vt:b>10</vt:b></vt:p>
</vt:pane>
<vt:pane name="cellPading22">
   <vt:p>...Cell pading-<vt:b>20</vt:b></vt:p>
</vt:pane>

<%// Test vertical and horizontal %>
<vt:pane name="verticalHorizontal11">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride, </vt:p>
   <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,</vt:p>
 <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal12">
 <vt:p>...Vertical Top & Hozizontal Left...</vt:p> 
 <vt:p>...This should appear  top left to the left pane...</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal21">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride,</vt:p>
 <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,</vt:p>
 <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal22">
   <vt:p>...Vertical Bottom & Hozizontal Left...</vt:p>   
   <vt:p>...This should appear  bottom left to the left pane...</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal31">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride,</vt:p>
 <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,</vt:p>
 <vt:p>Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal32">
   <vt:p>...Vertical Centre & Hozizontal Left...</vt:p> 
   <vt:p>...This should appear  centre left to the left pane...</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal41">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal42">
   <vt:p>...Vertical Top & Hozizontal Left...</vt:p> 
   <vt:p>...This should appear left to the above pane...</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal51">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal52">
   <vt:p>...Vertical Top & Hozizontal Centre...</vt:p> 
   <vt:p>...This should appear centre to the above pane...</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal61">
   <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
 <vt:p>Volanis Systems,Kings Road Court, Kings Ride,Ascot, Berkshire, SL5 7JR, United Kingdom</vt:p>
</vt:pane>
<vt:pane name="verticalHorizontal62">
   <vt:p>...Vertical Top & Hozizontal Right...</vt:p> 
   <vt:p>...This should appear Right to the above pane...</vt:p>
</vt:pane>

</vt:canvas>
