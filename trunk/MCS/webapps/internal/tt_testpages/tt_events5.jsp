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
  <vt:p>Filename: tt_event5.jsp<vt:br/>
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
     
	ol		mouse, click, key
	ul		mouse, click, key
	li		mouse, click, key
	menuitem	mouse, click, key
	dl		mouse, click, key
	dt		mouse, click, key
	dd		mouse, click, key
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

  <vt:ol
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">

  <vt:li>Move mouse and click over this item text to check the ol event handlers.</vt:li>
  </vt:ol>

  <vt:ul
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">

  <vt:li>Move mouse and click over this item text to check the ul event handlers.</vt:li>
  </vt:ul>


  <vt:ol>
  <vt:li
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">

       Move mouse and click over this item text to check the li event handlers.</vt:li>
  </vt:ol>



  <vt:menu type="plaintext">
  <vt:menuitem
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
	href="#" 
	text="Move mouse and click over this menu item text to check the menuitem event handlers."/>
  </vt:menu>
  
  <vt:br/>

  <vt:menu type="rolloverimage">
  <vt:menuitem
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
	href="#" 
	offImage="tt_bluebox1"
	onImage="tt_bluebox2"
	text="Move mouse and click over this menu item text to check the menuitem event handlers."/>
  </vt:menu>


<vt:dl
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  <vt:dt>Move mouse and click over this definition text to check the dl event handlers.</vt:dt>
</vt:dl>


<vt:dl>
<vt:dt  onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  Move mouse and click over this definition text to check the dt event handlers.</vt:dt>
</vt:dl>

<vt:dl>
<vt:dd  onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  Move mouse and click over this definition text to check the dd event handlers.</vt:dd>
</vt:dl>





</vt:pane>

</vt:canvas>
