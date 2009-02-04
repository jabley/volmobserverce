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
	AFP     04/11/2002      Updated Links for new theme navigation

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>XFTextInput Theme</vt:h1>
  <vt:p>Filename: tt_xfform10.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfform
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Test vt:xftextinput attributes with theme properties.
  </vt:p>
  <vt:pre>
     xfform       name, action, help, prompt
     xftextinput  name, caption, captionPane, entryPane, help, prompt, errmsg, initial, shortcut, type
     xfaction     name, caption, captionPane, entryPane, help, prompt, type
  </vt:pre>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All except VoiceXML.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A form should be created with a three text fields in altered fonts. There are no
	hidden fields on this page.
  </vt:p>
  <vt:p>Device Dependant: <vt:br/>
	PC: Prompts may be displayed as 'flying hints'.
	Ensure that the fonts can be specified and the 
	text boxes are shown to the correct sizes.
	<vt:br/>
	TV: Shortcuts are enabled. eg. press 2 to go to second field.
	<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_form.jsp">Form</vt:a> =>
tt_xfform10.jsp
</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit03.jsp"
		help="{tt_formhelp}"
		prompt="{tt_formprompt}">

<vt:xftextinput	name="drink"
		styleClass="alteredfont"
		help="{tt_fieldhelp1}"
		prompt="{tt_fieldprompt1}"
		errmsg="{tt_fielderrmsg1}"
		initial="alteredfont"
		shortcut="{one}"
		type="text"
		caption="{tt_fieldcaption1}"
		captionPane="formCaption1"
		entryPane="formField1"/>

<vt:xftextinput	name="sugar"
		styleClass="6x1"
		help="{tt_fieldhelp2}"
		prompt="{tt_fieldprompt2}"
		errmsg="{tt_fielderrmsg2}"
		initial="6by1"
		shortcut="{two}"
		caption="{tt_fieldcaption2}"
		captionPane="formCaption2"
		entryPane="formField2"/>

<vt:xftextinput	name="papers"
		styleClass="30x4"
		help="{tt_fieldhelp3}"
		prompt="{tt_fieldprompt3}"
		errmsg="{tt_fielderrmsg3}"
		initial="30x4"
		shortcut="{three}"
		caption="{tt_fieldcaption3}"
		captionPane="formCaption3"
		entryPane="formField3"/>


<vt:xfaction	type="submit"
		help="{tt_actionhelp}"
		prompt="{tt_actionprompt}"
		caption="{tt_actioncaption1}"
		captionPane="formFooter"
		entryPane="formFooter"/>

<vt:xfaction	type="reset"
		help="{tt_actionhelp}"
		prompt="{tt_actionprompt}"
		caption="{tt_actioncaption2}"
		captionPane="formFooter"
		entryPane="formFooter"/>


		
</vt:xfform>

</vt:canvas>
