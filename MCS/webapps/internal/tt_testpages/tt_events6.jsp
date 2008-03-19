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

<vt:canvas layoutName="tt_default" onload="{statusOnLoad}">

<vt:pane name="fileinfo">
  <!-- This script just opens a new window for the event handler messages -->
  <vt:script src="{openEventWindow}"/>

  <vt:h1>Script Event Handler Test</vt:h1>
  <vt:p>Filename: tt_event6.jsp<vt:br/>
        Layout: tt_default<vt:br/>
        Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This is to test event handlers such as onMouseOver, onKeyPress, etc.</vt:p>
  <vt:pre>
 	focus - onblur, onfocus
	mouse - onmousedown, onmouseup, onmouseover, onmouseout, onmousemove
	click - onclick, ondblclick
	key   - onkeypress, onkeydown, onkeyup
     
	img		mouse, click, key
	logo		mouse, click, key
	chart		mouse, click, key
	mmflash 	mouse, click, key
	quicktime	mouse, click, key
	realvideo	mouse, click, key
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A separate window should pop up and log messages whenever an event is
	triggered. Move mouse around screen, click and press keyboard buttons
	and check the appropriate events are triggered.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Tags Index</vt:a>
  </vt:p>
</vt:pane>


<vt:pane name="test">
 
  <vt:p>Move mouse and click over the image below to check the img event handlers.</vt:p>
  <vt:img
        src="tt_bluebox1"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>

  <vt:p>Move mouse and click over the image below to check the logo event handlers.</vt:p>
  <vt:logo
        src="tt_bluebox1"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>


  <vt:p>Move mouse and click over the chart image below to check the chart event handlers.</vt:p>

<vt:chart 
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"
        styleClass="mychart" 
	name="ChartTest" 
	data="50 60 70,80 60 40"
	altText="the altText from the JSP" 
	labels="Jan Feb Mar" 
	title="The chart title" />

  <vt:p>Move mouse and click over the dynvis below to check the mmflash event handlers.</vt:p>
  <vt:mmflash
        name="bart"
        play="true"
        loop="true"
        menu="true"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>

  <vt:p>Move mouse and click over the dynvis below to check the quicktime event handlers.</vt:p>
  <vt:quicktime
        name="missiles"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>

 <vt:p>Move mouse and click over the dynvis below to check the realvideo event handlers.</vt:p>
  <vt:realvideo
        name="missiles"
 	autoStart="true"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>


</vt:pane>
</vt:canvas>
