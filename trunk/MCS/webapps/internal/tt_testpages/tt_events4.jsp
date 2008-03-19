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
  <vt:p>Filename: tt_event4.jsp<vt:br/>
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
     
	table	mouse, click, key
	tr	mouse, click, key
	td	mouse, click, key
	tbody	mouse, click, key
	thead	mouse, click, key
	tfoot	mouse, click, key
	th	mouse, click, key
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

  <vt:table cols="1"
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
  <vt:tr>
  <vt:td>
  <vt:p>  
  Move mouse and click over this table to check the vt:table event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:tr
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
  <vt:td>
  <vt:p>  
  Move mouse and click over this table to check the vt:tr event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:tr>
  <vt:td
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
  <vt:p>  
  Move mouse and click over this table to check the vt:td event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:tbody
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
  <vt:tr>
  <vt:td>
  <vt:p>  
  Move mouse and click over this table to check the vt:tbody event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:tbody>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:thead
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
  <vt:tr>
  <vt:td>
  <vt:p>  
  Move mouse and click over this table to check the vt:thead event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:thead>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:tfoot
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
  <vt:tr>
  <vt:td>
  <vt:p>  
  Move mouse and click over this table to check the vt:tfoot event handlers.</vt:p>
  </vt:td>
  </vt:tr>
  </vt:tfoot>
  </vt:table>

  <vt:br/>

  <vt:table cols="1">
  <vt:tr>
  <vt:th
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
  <vt:p>  
  Move mouse and click over this table to check the vt:th event handlers.</vt:p>
  </vt:th>
  </vt:tr>
  </vt:tfoot>
  </vt:table>

  <vt:br/>

</vt:pane>

</vt:canvas>
