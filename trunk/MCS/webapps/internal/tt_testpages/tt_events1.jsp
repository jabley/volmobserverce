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

<vt:canvas layoutName="tt_default">

<vt:pane name="fileinfo">
  <!-- This script just opens a new window for the event handler messages -->
  <vt:script src="{openEventWindow}"/>

  <vt:h1>Script Event Handler Test</vt:h1>
  <vt:p>Filename: tt_event1.jsp<vt:br/>
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
     
	a	focus, mouse, click, key
	b	mouse, click, key
	strong	mouse, click, key
	i	mouse, click, key
	em	mouse, click, key
	h1-h6	mouse, click, key
	big	mouse, click, key
	small	mouse, click, key
	u	mouse, click, key
	p	mouse, click, key
	div	mouse, click, key
	span	mouse, click, key
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
  <vt:p>
  a<vt:br/>
  Move mouse and click over the link text to check the a event handlers.<vt:br/>
  <vt:a href="#"
	onblur="{statusBlur}"
	onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onfocus="{statusFocus}"
        onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the link text. 
  </vt:a>
  </vt:p>

  <!-- va element appears broken so commenting out.  -->

<vt:p>
  b<vt:br/>
  Move mouse and click over the bold text to check the b event handlers.<vt:br/>
  <vt:b onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the bold text.
  </vt:b>
</vt:p>

<vt:p>
  strong<vt:br/>
  Move mouse and click over the strong text to check the strong event handlers.<vt:br/>
  <vt:strong onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the strong text.
  </vt:strong>
</vt:p>

<vt:p>
  i<vt:br/>
  Move mouse and click over the italic text to check the i event handlers.<vt:br/>
  <vt:i onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the italic text.
  </vt:i>
</vt:p>

<vt:p>
  em<vt:br/>
  Move mouse and click over the emphasis text to check the em event handlers.<vt:br/>
  <vt:em onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the emphasis text.
  </vt:em>
</vt:p>

<vt:p>
  h1, h3, h5<vt:br/>
  Move mouse and click over the heading text to check the h1, h3, h5 event handlers.<vt:br/>
  <vt:h1 onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the h1 header.
  </vt:h1>
  <vt:h3 onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the h3 header.
  </vt:h3>
  <vt:h5 onmouseup="{statusMouseUp}"
        onmousedown="{statusMouseDown}"
        onmouseover="{statusMouseOver}"
        onmouseout="{statusMouseOut}"
        onmousemove="{statusMouseMove}"
        onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeyup="{statusKeyUp}"
        onkeypress="{statusKeyPress}">
  This is the h5 header.
  </vt:h5>
</vt:p>

<vt:p>
  big<vt:br/>
  Move mouse and click over the big text to check the big event handlers.<vt:br/>
  <vt:big
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  This is the big text.
  </vt:big>
</vt:p>

<vt:p>
  small<vt:br/>
  Move mouse and click over the small text to check the small event handlers.<vt:br/>
  <vt:small
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  This is the small text.
  </vt:small>
</vt:p>

  <vt:p>
  u<vt:br/>
  Move mouse and click over the underlined text to check the u event handlers.<vt:br/>
  <vt:u	onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the underlined text. 
  </vt:u>
  </vt:p>

  <vt:p>
  p<vt:br/>
  Move mouse and click over the paragraph text to check the p event handlers.<vt:br/>
  <vt:p	onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the paragraph text. 
  </vt:p>
  </vt:p>

<vt:p>
  div<vt:br/>
  Move mouse and click over the div text to check the div event handlers.<vt:br/>
  <vt:div
      
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  This is the div text.
  </vt:div>
</vt:p>
<vt:p>
  span<vt:br/>
  Move mouse and click over the span text to check the span event handlers.<vt:br/>
  <vt:span
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  This is the span text.
  </vt:span>
</vt:p>

</vt:pane>


</vt:canvas>
