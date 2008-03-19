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

<vt:canvas layoutName="tt_xfform" theme="tt_xfform" onload="{statusOnLoad}">

<vt:pane name="fileinfo">
  <!-- This script just opens a new window for the event handler messages -->
  <vt:script src="{openEventWindow}"/>

  <vt:h1>Script Event Handler Test</vt:h1>
  <vt:p>Filename: tt_event2.jsp<vt:br/>
        Layout: tt_xfform<vt:br/>
        Theme: tt_xfform
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

     
	a		focus, mouse, click, key
	xfsiselect	focus, onselect, onchange
	xfmuselect 	focus, onselect, onchange
	xfboolean	focus, onselect, onchange
	xftextinput	focus, onselect, onchange
	xfaction	focus, onselect, onchange
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


<vt:pane name="formHeader">
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

  <vt:p>
  xf elements<vt:br/>
  Tab in and out of fields to check focus, and change values.
  </vt:p>
</vt:pane>


<vt:xfform     
      name="orderForm"
      action="#">

<vt:xfsiselect
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        active="true"
        name="field1"
        caption="Drink"
        captionPane="formCaption1"
        entryPane="formField1">
  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfsiselect>

<vt:xfsiselect
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        active="true"
        name="field1"
        caption=""
	styleClass="radioformat"
        captionPane="formCaption1"
        entryPane="formField1">
  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfsiselect>

<vt:xfmuselect
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        active="true"
        name="field3"
        captionPane="formCaption3"
        entryPane="formField3"
        caption="Paper">
  <vt:xfoption caption="The Sun" value="sun"/>
  <vt:xfoption caption="Telegraph" value="telegraph"/>
  <vt:xfoption caption="Daily Express" value="express"/>
  <vt:xfoption caption="Financial Times" value="ft"/>
</vt:xfmuselect>

<vt:xfboolean
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        name="field2"
        caption="Sugar"
        captionPane="formCaption2"
        entryPane="formField2"
        falseValue="{field2FalseValues}"
        trueValue="{field2TrueValues}"/>

<vt:xftextinput name="field4"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                maxLength="6"
                type="text"
                caption="Room #"
                captionPane="formCaption4"
                entryPane="formField4"/>

<vt:xftextinput name="field4"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                maxLength="3"
                type="text"
		styleClass="30x4"
                caption="Notes"
                captionPane="formCaption5"
                entryPane="formField5"/>

<vt:xfaction    type="submit"
		name="Submit Now"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_actionhelp}"
                prompt="{tt_actionprompt}"
                caption="Submit The Form"
                entryPane="formFooter"/>

</vt:xfform>


</vt:canvas>
