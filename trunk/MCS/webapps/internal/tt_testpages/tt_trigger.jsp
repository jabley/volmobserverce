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
	AFP	06/11/2002	Created this file
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_trigger">

<vt:pane name="fileinfo">
  <vt:h1>Selection List Trigger</vt:h1>
  <vt:p>Filename: tt_trigger.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_trigger<vt:br/>
	Devices: Wap TV<vt:br/>
  </vt:p>
</vt:pane>


<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_form.jsp">Form</vt:a> =>
tt_trigger.jsp
</vt:p>

</vt:pane>

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit02.jsp"
		prompt="{tt_formprompt}">

<vt:xfsiselect	name="drink" 
		styleClass="trigger1"
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


<vt:xfsiselect	name="sugar" 
		styleClass="trigger2"
		caption="{tt_fieldcaption2}"
		prompt="{tt_fieldprompt2}"
		help="{tt_fieldhelp2}"
    		captionPane="formCaption2"
		entryPane="formField2">
	<vt:xfoption caption="Sugar" value="Sugar"/>
        <vt:xfoption caption="Sweetener" value="Sweetener"/>
        <vt:xfoption caption="None" value="None"/>
</vt:xfsiselect>


<vt:xfsiselect	name="Cup" 
		styleClass="trigger3"
		caption="{tt_fieldcaption3}"
		prompt="{tt_fieldprompt3}"
		help="{tt_fieldhelp3}"
    		captionPane="formCaption3"
		entryPane="formField3">
	<vt:xfoption caption="Mug" value="Mug"/>
        <vt:xfoption caption="Cup" value="Cup"/>
        <vt:xfoption caption="Glass" value="Glass"/>
</vt:xfsiselect>

<vt:xfmuselect
        name="papers"
	styleClass="trigger4"
        caption="{tt_fieldcaption4}"
        captionPane="formCaption4"
        entryPane="formField4"
        help="{tt_fieldhelp4}"
        prompt="{tt_fieldprompt4}">
  <vt:xfoption caption="The Sun" value="The Sun"/>
  <vt:xfoption caption="Telegraph" value="Telegraph"/>
  <vt:xfoption caption="Daily Express" value="Daily Express"/>
</vt:xfmuselect>
	

<vt:xfmuselect
        name="chocolate"
	styleClass="trigger5"
        caption="{tt_fieldcaption5}"
        captionPane="formCaption5"
        entryPane="formField5"
        help="{tt_fieldhelp5}"
        prompt="{tt_fieldprompt5}">
  <vt:xfoption caption="Mars" value="Mars"/>
  <vt:xfoption caption="Snickers" value="Snickers"/>
  <vt:xfoption caption="Kit Kat" value="Kit Kat"/>
</vt:xfmuselect>

<vt:xfmuselect
        name="books"
	styleClass="trigger6"
        caption="{tt_fieldcaption6}"
        captionPane="formCaption6"
        entryPane="formField6"
        help="{tt_fieldhelp6}"
        prompt="{tt_fieldprompt6}">
  <vt:xfoption caption="Jungle Book" value="Jungle Book"/>
  <vt:xfoption caption="Rebecca" value="Rebecca"/>
  <vt:xfoption caption="Fever Pitch" value="Fever Pitch"/>
</vt:xfmuselect>


<vt:xfaction	type="submit"
		caption="Submit"
		captionPane=""
		entryPane="formFooter"/>

</vt:xfform>

</vt:canvas>
