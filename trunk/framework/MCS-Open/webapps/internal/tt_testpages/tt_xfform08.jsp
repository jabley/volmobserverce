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
	AFP     04/11/2002      Updated Links for new theme navigation
	
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>Radio / Checkbox Theme Test</vt:h1>
  <vt:p>Filename: tt_xfform08.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfform
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure the list-ui style can create radio buttons and
	checkbox fields in altered theme fonts.
  </vt:p>
<vt:pre>
     xfform       name, action, prompt
     xfsiselect   name, caption, captionPane, entryPane, help, prompt, (style alteredradiofont)
     xfmuselect   name, caption, captionPane, entryPane, help, prompt, (style alteredcheckboxfont)
     xfoption     caption, value	
     xfboolean    name, caption, captionPane, entryPane, help, prompt, trueValue, falseValue	
     xfaction     caption, captionPane, entryPane, type
  </vt:pre>


  <vt:h3>Devices</vt:h3>
  <vt:p>All.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>The Drinks field should be presented as radio buttons. 
	The Papers field should be presented as checkboxes.
        The fields should have font styles applied to them. 
  </vt:p>
  <vt:p>Device Dependant: <vt:br/>
	PC: Prompts may be displayed as 'flying hints'.<vt:br/>
	VoiceXML: Help messages will be spoken when you say the word 'help'.
		Prompts will be spoken.
  </vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_form.jsp">Form</vt:a> =>
tt_xfform08.jsp
</vt:p>

</vt:pane>

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit02.jsp"
		prompt="{tt_formprompt}">

<vt:xfsiselect	name="drink" 
		styleClass="alteredradiofont"
		caption="{tt_fieldcaption1}"
		prompt="{tt_fieldprompt1}"
		help="{tt_fieldhelp1}"
    		captionPane="formCaption1"
		entryPane="formField1">
	<vt:xfoption caption="Milkshake" value="Milk"/>
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
        captionPane="formCaption2"
        entryPane="formField2"
        help="{tt_fieldhelp2}"
        prompt="{tt_fieldprompt2}"
        falseValue="{tt_fieldfalse}"
        trueValue="{tt_fieldtrue}"/>

<vt:xfmuselect
	styleClass="alteredcheckboxfont"
        name="papers"
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
