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
<vt:canvas layoutName="K_TestComponents" >
<vt:pane name="main">
   <vt:ul>
      <vt:p> These links related to Components related testing. Please select the appropriate like to see the property setting and the view of it. Pane Name and Fragment Name are usable with volantis tags  </vt:p> 
      <vt:ul>
         <vt:li>Audio</vt:li>
         <vt:li><vt:a href="/volantis/K_TestComponents.jsp?vfrag=audioRealAudio"> .....Test Real Audio</vt:a></vt:li>
         <vt:li><vt:a href="/volantis/K_TestComponents.jsp?vfrag=audioWindowMediaPlayer"> .....Test Window Media Player</vt:a></vt:li>
         <vt:li>Images</vt:li>
         <vt:li><vt:a href="/volantis/K_TestComponents.jsp?vfrag=images"> .....Test GIF</vt:a></vt:li>
         <vt:li>Dynamic Visuals</vt:li>
         <vt:li><vt:a href="/volantis/K_TestComponents.jsp?vfrag=dynamicVisuals"> .....Test Micromedia Flash</vt:a></vt:li>
      </vt:ul>
   </vt:ul>

   
</vt:pane>

<%// Test Real Audio %>
<vt:pane name="audioRealAudio">
   <vt:h1>...Testing Real Audio BBC Radio News...</vt:h1>
   <vt:p>
   <vt:hr/>
   <vt:b>Attributes</vt:b>
   <vt:br/>
   Name-<vt:b>BBCradioNews</vt:b>
   <vt:br/>
   Encoding-<vt:b>Real Audio</vt:b>
   <vt:br/>
   File Name-<vt:b>http://www.bbc.co.uk/fivelive/audio/latest_news.ram</vt:b>
   <vt:hr />
   <vt:b>Volantis Tags</vt:b>
   <vt:br/>
   name-<vt:b>audioRealAudio</vt:b>
   <vt:br/>
   autoStart-<vt:b>true</vt:b>
   <vt:br/>
   controls-<vt:b>true</vt:b>
   <vt:br/>
   width-<vt:b>300</vt:b>
   <vt:br/>
   height-<vt:b>100</vt:b>
   <vt:br/>
   loop-<vt:b>true</vt:b>
   <vt:br/>
   numLoop-<vt:b>2</vt:b>
   <vt:br/>
   altText-<vt:b>This is myReal Audio</vt:b>
   </vt:p>
   <vt:hr/>
   <vt:realaudio name="BBCRadioNews" autoStart="true" width="300" height="100" numLoop="2" shuffle="false" controls="true" loop="false" altText="This is myReal Audio" />    
</vt:pane>


<vt:pane name="audioWindowMediaPlayer">
   <vt:h1>...Testing Window Madia Player.</vt:h1>
   <vt:p>
   <vt:hr/>
   <vt:b>Attributes</vt:b>
   <vt:br/>
   Name-<vt:b>guitar</vt:b>
   <vt:br/>
   Encoding-<vt:b>Windows Media Player (Audio)</vt:b>
   <vt:br/>
   File Name-<vt:b>http://raven.uk.volantis.com/plugin/audio/guitar.wma</vt:b>
   <vt:hr />
   <vt:b>Volantis Tags</vt:b>
   <vt:br/>
   name-<vt:b>guitar</vt:b>
   <vt:br/>
   autoStart-<vt:b>true</vt:b>
   <vt:br/>
   controls-<vt:b>true</vt:b>
   <vt:br/>
   width-<vt:b>400</vt:b>
   <vt:br/>
   height-<vt:b>400</vt:b>
   <vt:br/>
   loop-<vt:b>true</vt:b>
   <vt:br/>
   numLoop-<vt:b>2</vt:b>
   <vt:br/>
   altText-<vt:b>This is my Window Media Player</vt:b>
   </vt:p>
   <vt:hr/>
   <vt:realaudio name="TamilRadio" autoStart="true" width="400" height="400" numLoop="2" shuffle="false" controls="true" loop="false" altText="This is Window Madia Player" />       
</vt:pane>

<vt:pane name="dynamicVisuals">
   <vt:h1>...Testing Dynamic Visuals.</vt:h1>
   <vt:p>
   <vt:hr/>
   <vt:b>Attributes</vt:b>
   <vt:br/>
   Name-<vt:b>pages</vt:b>
   <vt:br/>
   Encoding-<vt:b>Micromedia Flash</vt:b>
   <vt:br/>
   Pixels X-<vt:b>400</vt:b>
   <vt:br/>
   Pixels Y-<vt:b>400</vt:b>
   <vt:br/>
   File Name-<vt:b>pages.sef</vt:b>
   <vt:hr />
   <vt:b>Volantis Tags</vt:b>
   <vt:br/>
   name-<vt:b>testPages</vt:b>
   <vt:br/>
   play-<vt:b>true</vt:b>
   <vt:br/>
   loop-<vt:b>true</vt:b>
   <vt:br/>
   menu-<vt:b>true</vt:b>
   <vt:br/>
   altImg-<vt:b>volantis</vt:b>
   <vt:br/>
   altText-<vt:b>This is my Media Player</vt:b>
   </vt:p>
   <vt:hr/>
   <vt:mmflash name="testPages" play="true" loop="true" menu="true" altImg="volantis" altText="This in my Micromedia Flash"/>
</vt:pane>

<vt:pane name="images">
   <vt:h1>...Testing Images.</vt:h1>
   <vt:p>
   <vt:hr/>
   <vt:b>Attributes</vt:b>
   <vt:br/>
   Name-<vt:b>Bugmanwalking</vt:b>
   <vt:br/>
   Encoding-<vt:b>GIF</vt:b>
   <vt:br/>
   Pixels X-<vt:b>100</vt:b>
   <vt:br/>
   Pixels Y-<vt:b>100</vt:b>
   <vt:br/>
   Pixels depth-<vt:b>0</vt:b>
   <vt:br/>
   Rendering-<vt:b>Colour</vt:b>
   <vt:br/>
   Width hint-<vt:b>0</vt:b>
   <vt:br/>
   File Name-<vt:b>Bugmanwalking.gif</vt:b>
   <vt:hr/>
   <vt:b>Volantis Tag</vt:b>
   <vt:br/>
   src-<vt:b>Bugmanwalking</vt:b>
   <vt:br/>
   altText-<vt:b>BUGMAN Walking</vt:b>
    <vt:hr/>
   </vt:p>
   <vt:logo src="Bugmanwalking" altText="BUGMAN Walking" />
</vt:pane>

</vt:canvas>
