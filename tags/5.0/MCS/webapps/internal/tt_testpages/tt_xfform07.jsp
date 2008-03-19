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
  <vt:h1>XFForm Iterator Test</vt:h1>
  <vt:p>Filename: tt_xfform07.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfform
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure fields can be sent to row iterator panes.
  </vt:p>
<vt:pre>
     xfform       name, action, prompt
     xfsiselect   name, caption, captionPane, entryPane, help, prompt, (style menuformat)
     xfmuselect   name, caption, captionPane, entryPane, help, prompt, (style menuformat)
     xfoption     caption, value	
     xfboolean    name, caption, captionPane, entryPane, help, prompt, trueValue, falseValue	
     xfaction     caption, captionPane, entryPane, type
  </vt:pre>


  <vt:h3>Devices</vt:h3>
  <vt:p>All.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>This layout requires the Row Iterator Panes have a height property
	set to the greatest height of the component fields. The muselect is 65
	pixels high so the panes are set with height of 65 pixels.</vt:p>
  <vt:p>The fields and captions should therefore be vertically centered 
	in cells that are 65 pixels high.
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

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit02.jsp"
		prompt="{tt_formprompt}">

<vt:xfsiselect	name="drink" 
		styleClass="menuformat"
		caption="{tt_fieldcaption1}"
		prompt="{tt_fieldprompt1}"
		help="{tt_fieldhelp1}"
    		captionPane="formRowIterCaption"
		entryPane="formRowIterField">
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
        caption="{tt_fieldcaption2}"
        captionPane="formRowIterCaption"
        entryPane="formRowIterField"
        help="{tt_fieldhelp2}"
        prompt="{tt_fieldprompt2}"
        falseValue="{tt_fieldfalse}"
        trueValue="{tt_fieldtrue}"/>

<vt:xfmuselect
        name="papers"
        caption="{tt_fieldcaption3}"
        captionPane="formRowIterCaption"
        entryPane="formRowIterField"
        help="{tt_fieldhelp3}"
        prompt="{tt_fieldprompt3}">
  <vt:xfoption caption="The Sun" value="The Sun"/>
  <vt:xfoption caption="Telegraph" value="Telegraph"/>
  <vt:xfoption caption="Daily Express" value="Daily Express"/>
</vt:xfmuselect>
		
</vt:xfform>

</vt:canvas>
