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
	Revision Information
	Name  	Date  		Comment
	MJ	15/11/2001	Created this file
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>Basic XFForm Test 2</vt:h1>
  <vt:p>Filename: tt_xfform04.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfform
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>An extended test of vt:siselect, vt:boolean and vt:muselect attributes.
  </vt:p>
<vt:pre>
     xfform       name, action, prompt
     xfsiselect   name, caption, captionPane, entryPane, help, prompt, active, errmsg, shortcut, initial (style menuformat)
     xfmuselect   name, caption, captionPane, entryPane, help, prompt, active, errmsg, shortcut(style menuformat)
     xfoption     caption, value, selected
     xfboolean    name, caption, captionPane, entryPane, help, prompt, trueValue, falseValue, active, errmsg, shortcut, initial
     xfaction     caption, captionPane, entryPane, type
  </vt:pre>


  <vt:h3>Devices</vt:h3>
  <vt:p>All.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A form should be created with a single select field, boolean field
	and a multiple select field. Captions should be spaced out from the
	fields.
        There should also be a submit button and a reset button.
  </vt:p>
  <vt:p>
	Drink should default to Hot Chocolate.
	Suagar should default to yes.
	Papers should default to The Sun and Daily Express</vt:p>
  <vt:p>Device Dependant: <vt:br/>
	PC: Prompts may be displayed as 'flying hints'. 
           Shortcuts are enabled. e.g. press ALT-2 to toggle sugar field. 
	<vt:br/>
	TV: Shortcuts are enabled e.g. press 2 to jump to sugar field.
	<vt:br/>
	VoiceXML: Help messages will be spoken when you say the word 'help'.
		Prompts will be spoken. Custom error message will be spoken
		if invalid input is found.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_forms.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit02.jsp"
		prompt="{tt_formprompt}">

<vt:xfsiselect	name="drink" 
		active="true"
		initial="Hot Chocolate"
		errmsg="{tt_fielderrmsg1}"
		shortcut="{one}"
		styleClass="menuformat"
		caption="{tt_fieldcaption1}"
		prompt="{tt_fieldprompt1}"
		help="{tt_fieldhelp1}"
    		captionPane="formCaption1"
		entryPane="formField1">
	<vt:xfoption caption="Tea" value="Tea"/>
        <vt:xfoption caption="Coffee" value="Coffee"/>
        <vt:xfoption caption="Hot Chocolate" value="Hot Chocolate"/>
        <vt:xfoption caption="Orange Juice" value="Orange Juice"/>
</vt:xfsiselect>


<vt:xfsiselect	name="my inactive siselect" 
		active="false"
		styleClass="menuformat"
		caption="{tt_fieldcaption1}"
		prompt="{tt_fieldprompt1}"
		help="{tt_fieldhelp1}"
    		captionPane="formCaption1"
		entryPane="formField1">
	<vt:xfoption caption="Tea" value="Tea"/>
        <vt:xfoption caption="Coffee" value="Coffee"/>
        <vt:xfoption caption="Hot Chocolate" value="Hot Chocolate"/>
        <vt:xfoption caption="Orange Juice" value="Orange Juice"/>
</vt:xfsiselect>

<!--**********************************************
    * Action tag in middle to prove the output   *
    * can deal with fields after the action tag  *
    **********************************************-->

<vt:xfaction	type="submit"
		caption="Submit"
		captionPane=""
		entryPane="formFooter"/>

<vt:xfaction	type="reset"
		caption="Reset"
		captionPane=""
		entryPane="formFooter"/>

<vt:xfboolean
        name="sugar"
	active="true"
	initial="1"
	errmsg="{tt_fielderrmsg1}"
	shortcut="{two}"
        caption="{tt_fieldcaption2}"
        captionPane="formCaption2"
        entryPane="formField2"
        help="{tt_fieldhelp2}"
        prompt="{tt_fieldprompt2}"
        falseValue="{tt_fieldfalse}"
        trueValue="{tt_fieldtrue}"/>

<vt:xfboolean
        name="my inactive boolean"
	active="false"
        caption="{tt_fieldcaption2}"
        captionPane="formCaption2"
        entryPane="formField2"
        help="{tt_fieldhelp2}"
        prompt="{tt_fieldprompt2}"
        falseValue="{tt_fieldfalse}"
        trueValue="{tt_fieldtrue}"/>

<vt:xfmuselect
        name="papers"
	active="true"
	errmsg="{tt_fielderrmsg1}"
	shortcut="{three}"
        caption="{tt_fieldcaption3}"
        captionPane="formCaption3"
        entryPane="formField3"
        help="{tt_fieldhelp3}"
        prompt="{tt_fieldprompt3}">
  <vt:xfoption caption="The Sun" value="The Sun" selected="true"/>
  <vt:xfoption caption="Telegraph" value="Telegraph"/>
  <vt:xfoption caption="Daily Express" value="Daily Express" selected="true"/>
</vt:xfmuselect>

<vt:xfmuselect
        name="my inactive muselect"
	active="false"
        caption="{tt_fieldcaption3}"
        captionPane="formCaption3"
        entryPane="formField3"
        help="{tt_fieldhelp3}"
        prompt="{tt_fieldprompt3}">
  <vt:xfoption caption="The Sun" value="The Sun"/>
  <vt:xfoption caption="Telegraph" value="Telegraph"/>
  <vt:xfoption caption="Daily Express" value="Daily Express"/>
</vt:xfmuselect>
		
</vt:xfform>

</vt:canvas>
