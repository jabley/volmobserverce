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
	MJ	08/10/2001	Added this header
				Ensured all text is enclosed in p or h tags
	AFP     04/11/2002      Updated Links for new theme navigation

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>XFForm Submit Button Test</vt:h1>
  <vt:p>Filename: tt_xfform06.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: tt_xfform
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Extended test of vt:xfaction attributes. 
	This test is to ensure the multiple images and submit buttons 
	can be used on a form. Check the correct button is identified 
	on the submit page.
  </vt:p>
  <vt:pre>
     xfform       name, action, prompt
     xfsiselect   name, caption, captionPane, entryPane, help, prompt, (style menuformat)
     xfmuselect   name, caption, captionPane, entryPane, help, prompt, (style menuformat)
     xfoption     caption, value	
     xfboolean    name, caption, captionPane, entryPane, help, prompt, trueValue, falseValue	
     xfaction     caption, captionPane, entryPane, type, name, shortcut
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>Test on all devices including VoiceXML.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>The form should contain an image. There should also be submit
	buttons. The first and second have the same 'name' property, but
        the submit page should still be able to distinguish them.
        The third and fourth  buttons have image components.
  </vt:p>
  <vt:p>Submit the page to check it can identify which submit button was used.</vt:p>
  <vt:p>Device Dependant<vt:br/>
	Shortcuts have been used on the submit buttons. eg on PC ALT-3 will
	trigger submit button 3. Use of shortcuts varies depending upon device.
  </vt:p>

</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_form.jsp">Form</vt:a> =>
tt_xfform06.jsp
</vt:p>
</vt:pane>

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit04.jsp"
		prompt="{tt_formprompt}">

<!--vbm 2002022510 says only xf tags are allowed within xfform tags.
    Although the line below works on tomcat 4.0.3 with phobos 2.5.2
    this is unsupported and has been commented out.
    You can get an image in a form pane as long as the vt:img tag is 
    outside the vt:xfform tag. See tag at end of file.-->
<!--vt:img pane="formCaption1" src="stars"/-->


<vt:xfsiselect	name="drink" styleClass="menuformat"
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


<!-- Image submit buttons have to be uniquely named if you want to 
     validate which one was pressed. Text submit buttons can have the
     same name, as long as they submit different values. -->

<vt:xfaction	type="submit"
		shortcut="1"
                name="submitbutton"
	        value="the_button_1a_value"
		caption="Submit Button 1a"
		captionPane=""
		prompt="This is submitbutton1a"
		entryPane="formFooter"/>

<vt:xfaction	type="submit"
		shortcut="1"
                name="submitbutton"
	        value="the_button1b_value"
		caption="Submit Button 1b"
		captionPane=""
		prompt="This is submitbutton1b"
		entryPane="formFooter"/>

<vt:xfaction	type="submit"
		shortcut="2"
                name="submitimagebutton2"
                styleClass="submitbutton"
		caption="Submit Button 2"
		captionPane=""
		prompt="This is submitbutton2"
		entryPane="formFooter"/>

<vt:xfaction	type="submit"
		shortcut="3"
                name="submitimagebutton3"
                styleClass="submitbutton"
		caption="Submit Button 3"
		captionPane=""
		prompt="This is submitbutton3"
		entryPane="formFooter"/>

<vt:xfaction	type="submit"
		shortcut="4"
                name="submitbutton4"
		caption="Submit Button 4"
		captionPane=""
                value="the_button4_value"
		prompt="This is submitbutton4"
		entryPane="formFooter"/>

<vt:xfaction	type="submit"
		shortcut="5"
                name="submitbutton5"
		caption="Submit Button 5"
		captionPane=""
		prompt="This is submitbutton5"
		entryPane="formFooter"/>



<vt:xfaction	type="reset"
		name="resetbutton1"
		caption="Reset Screen"
		captionPane=""
		prompt="Click here to reset screen to defaults."
		entryPane="formFooter"/>

<vt:xfboolean
        name="sugar"
        caption="{tt_fieldcaption2}"
        captionPane="formCaption2"
        entryPane="formField2"
        help="{tt_fieldhelp2}"
        prompt="{tt_fieldprompt2}"
        falseValue="no, nope"
        trueValue="yes, yep, yeah"/>


<vt:xfmuselect
        name="papers"
        caption="{tt_fieldcaption3}"
        captionPane="formCaption3"
        entryPane="formField3"
        errmsg="{tt_fielderrmsg3}"
        help="{tt_fieldhelp3}"
        prompt="{tt_fieldprompt3}">

  <vt:xfoption caption="The Sun" value="The Sun"/>
  <vt:xfoption caption="Telegraph" value="Telegraph"/>
  <vt:xfoption caption="Daily Express" value="Daily Express"/>
  <vt:xfoption caption="{tt_nulloption}" value="none"/>
</vt:xfmuselect>
		
</vt:xfform>

<!--vbm 2002022510 says only xf tags are allowed within xfform tags.
    Although it works in Tomcat 4.0.3 with vt:img inside the vt:xfform tags
    this is unsupported. The following img tag puts an image inside the form,
    and is correctly placed outside the vt:xfform tags-->
<vt:img pane="formCaption1" src="stars"/>

</vt:canvas>
