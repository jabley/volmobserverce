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
  <vt:p>Filename: tt_event3.jsp<vt:br/>
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
     
	noscript	mouse, click, key
	pre		mouse, click, key
	tt		mouse, click, key
	code		mouse, click, key
	samp		mouse, click, key
	kbd		mouse, click, key
	hr		mouse, click, key
	blockquote	mouse, click, key
	cite		mouse, click, key
	address		mouse, click, key
	sup		mouse, click, key
	sub		mouse, click, key
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
  noscript<vt:br/>
  View the source and check the noscript element contains appropriate event handlers. (We know event handler support for browsers that don't support scripts is a bit contradictory, but this is part of the XHTML basic and HTML 4.0 spec.)<vt:br/>
  <vt:noscript onclick="{statusClick}"
        ondblclick="{statusDblClick}"
        onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the noscript text.
  </vt:noscript>
  </vt:p>


  <vt:p>
  pre<vt:br/>
  Move mouse and click over the code preformatted text to check the pre event handlers.<vt:br/>
  <vt:pre onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the preformatted text. 
  </vt:pre>
  </vt:p>

  <vt:p>
  tt<vt:br/>
  Move mouse and click over the teletype text to check the tt event handlers.<vt:br/>
  <vt:tt onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the teletype text. 
  </vt:tt>
  </vt:p>

  <vt:p>
  code<vt:br/>
  Move mouse and click over the code text to check the code event handlers.<vt:br/>
  <vt:code onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the code text. 
  </vt:code>
  </vt:p>

  <vt:p>
  samp<vt:br/>
  Move mouse and click over the sample text to check the samp event handlers.<vt:br/>
  <vt:samp onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the sample text. 
  </vt:samp>
  </vt:p>

  <vt:p>
  kbd<vt:br/>
  Move mouse and click over the keyboard text to check the kbd event handlers.<vt:br/>
  <vt:kbd onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the keyboard text. 
  </vt:kbd>
  </vt:p>

  <vt:p>
  hr<vt:br/>
  Move mouse and click over the horizontal rule to check the hr event handlers.<vt:br/>
  <vt:hr onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}"/>
  </vt:p>

  <vt:p>
  blockquote<vt:br/>
  Move mouse and click over the quote text to check the blockquote event handlers.<vt:br/>
  <vt:blockquote onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the quote text. 
  </vt:blockquote>
  </vt:p>

  <vt:p>
  cite<vt:br/>
  Move mouse and click over the citation text to check the cite event handlers.<vt:br/>
  <vt:cite onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the citation text. 
  </vt:cite>
  </vt:p>

  <vt:p>
  address<vt:br/>
  Move mouse and click over the address text to check the address event handlers.<vt:br/>
  <vt:address onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the address text. 
  </vt:address>
  </vt:p>

  <vt:p>
  sup<vt:br/>
  Move mouse and click over the superscript text to check the sup event handlers.<vt:br/>
  Normal Text <vt:sup onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the superscript text
  </vt:sup>
  </vt:p>

  <vt:p>
  sub<vt:br/>
  Move mouse and click over the subscript text to check the sub event handlers.<vt:br/>
  Normal Text <vt:sub onclick="{statusClick}"
        ondblclick="{statusDblClick}"
	onkeydown="{statusKeyDown}"
        onkeypress="{statusKeyPress}"
        onkeyup="{statusKeyUp}"
        onmousedown="{statusMouseDown}"
        onmousemove="{statusMouseMove}"
        onmouseout="{statusMouseOut}"
        onmouseover="{statusMouseOver}"
        onmouseup="{statusMouseUp}">
  This is the subscript text
  </vt:sub>
  </vt:p>

</vt:pane>

</vt:canvas>
