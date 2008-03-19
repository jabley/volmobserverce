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
<!--  *************************************************************************
	(c) Volantis Systems Ltd 2001. 
      *************************************************************************
	Revision Info 
	Name  	Date  		Comment
	MJ	15/11/2001	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>Simple XFForm Test 2</vt:h1>
  <vt:p>Filename: tt_xfform02.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>An extended test of vt:xfform and vt:xftextinput attributes.
	This page can be tested on all devices.</vt:p>
  <vt:pre>
     xfform       name, action, help, prompt
     xftextinput  name, caption, captionPane, entryPane, active, help, prompt
     xfaction     name, caption, captionPane, entryPane, active, help, prompt, type
  </vt:pre>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A form should be created with a single text field. 
	There should also be a Submit Order button to submit the form,
	and a Reset Form button to clear the field value.
  </vt:p>
  <vt:p>Device Dependant: <vt:br/>
	PC: Prompts may be displayed as 'flying hints'.<vt:br/>
	VoiceXML: Help messages will be spoken when you say the word 'help'.
		Prompts will be spoken.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_forms.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit01.jsp"
		help="{tt_formhelp}"
		prompt="{tt_formprompt}">

<vt:xftextinput	name="my inactive field"
		active="false"
		caption="Inactive Field Caption: "
		captionPane="formCaption1"
		entryPane="formCaption1"/>

<vt:xftextinput	name="drink"
		active="true"
		help="{tt_fieldhelp1}"
		prompt="{tt_fieldprompt1}"
		caption="{tt_fieldcaption1}"
		captionPane="formCaption1"
		entryPane="formCaption1"/>


<vt:xfaction	name="my inactive button"
		active="false"
		type="submit"
		caption="Inactive Button"
		captionPane="formCaption1"
		entryPane="formCaption1"/>

<vt:xfaction	name="submitbutton1"
		active="true"
		type="submit"
		help="{tt_actionhelp}"
		prompt="{tt_actionprompt}"
		caption="{tt_actioncaption1}"
		captionPane="formCaption1"
		entryPane="formCaption1"/>

<vt:xfaction	name="resetbutton1"
		active="true"
		type="reset"
		help="{tt_actionhelp}"
		prompt="{tt_actionprompt}"
		caption="{tt_actioncaption2}"
		captionPane="formCaption1"
		entryPane="formCaption1"/>
		
</vt:xfform>

</vt:canvas>
