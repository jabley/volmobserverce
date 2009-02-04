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
<%@ include file="VolantisNoError-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" type="portlet">

<vt:pane name="fileinfo">
  <vt:h1>XFForm Validation Test</vt:h1>
  <vt:p>Filename: tt_xfform05.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Test the vt:xfimplicit tag 
	and extended vt:textinput attributes.
  </vt:p>
  <vt:pre>
     xfform       name, action, help, prompt
     xftextinput  name, caption, captionPane, entryPane, help, prompt, errmsg, initial, shortcut, maxLength, validate, type
     xfaction     name, caption, captionPane, entryPane, help, prompt, type
     xfimplicit   name, value
  </vt:pre>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A form should be created with a single text field. 
	There should also be a Submit Order button to submit the form,
	and a Reset Form button to clear the field value.
  </vt:p>
  <vt:p>Check the fields correctly validate input.</vt:p>
  <vt:p>Device Dependant: <vt:br/>
	PC: Prompts may be displayed as 'flying hints'.
	    Shortcuts are enabled. eg. press ALT-2 to jump to second field.
	    MaxLength on all fields is 6 chars. Password should display ***'s.
	<vt:br/>
	TV: Shortcuts are enabled. eg. press 2 to go to second field.
	<vt:br/>
	VoiceXML: Help messages will be spoken when you say the word 'help'.
		Prompts will be spoken. <vt:br/>
		*** NB VoiceXML validates drinks and papers! ***
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_FormIndex.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit03.jsp"
		help="{tt_formhelp}"
		prompt="{tt_formprompt}">

<vt:xftextinput	name="drink"
		help="{tt_fieldhelp4}"
		prompt="{tt_fieldprompt4}"
		validate="{tt_fieldvalidate4}"
		errmsg="{tt_fielderrmsg4}"
		initial="17:30"
		shortcut="{one}"
		maxLength="6"
		type="text"
		caption="{tt_fieldcaption4}"
		captionPane="formCaption1"
		entryPane="formField1"/>

<vt:xfimplicit  name="myhiddenfield"
		value="The hidden value. Hurrah!"/>


<vt:xftextinput	name="sugar"
		help="{tt_fieldhelp5}"
		prompt="{tt_fieldprompt5}"
		validate="{tt_fieldvalidate5}"
		errmsg="{tt_fielderrmsg5}"
		initial="ABC"
		shortcut="{two}"
		maxLength="6"
		type="password"
		caption="{tt_fieldcaption5}"
		captionPane="formCaption2"
		entryPane="formField2"/>

<vt:xftextinput	name="papers"
		help="{tt_fieldhelp6}"
		prompt="{tt_fieldprompt6}"
		validate="{tt_fieldvalidate6}"
		errmsg="{tt_fielderrmsg6}"
		initial="0.00"
		shortcut="{three}"
		maxLength="6"
		type="text"
		caption="{tt_fieldcaption6}"
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

<vt:xfimplicit  name="mysecondhiddenfield"
		value="The second hidden field. Hurrah!"/>
		
</vt:xfform>

</vt:canvas>
