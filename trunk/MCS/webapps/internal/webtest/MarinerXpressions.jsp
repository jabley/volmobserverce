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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/MarinerXpressions.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 08-Oct-01    Doug            VBM:2001100401 - Created 
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest" pageTitle="XFFormTest">

<vt:p pane="Welcome">

<!--
The Following anchor tag attributes can be Mariner Expresions
href, shortcut, accessKey
-->
<vt:h1>Anchor Tag Test</vt:h1>
<vt:a
	accessKey="{red}" 
	href="{news}">(BBC Master, ITN WML) from link expression
</vt:a>
<vt:br/>
<vt:a 
	  accessKey="a" 
	  href="http://www.bbc.co.uk">BBC from String literal
</vt:a>
<vt:br/>
<vt:a 
	    shortcut="{yellow}" 
	    href="\\{test.html\\}">(escaped literal expression - link should be '{test.html}')
</vt:a>
<vt:br/>
<vt:a 
	    shortcut="b"
	    href="http://www.itn.co.uk">ITN from String literal
</vt:a>
<vt:br/>
<vt:h1>Menu Item Tag Test</vt:h1>
</vt:p>

<!--
The Following menuitem tag attributes can be Mariner Expresions
href, shortcut, accessKey
-->

<vt:menu type="plaintext" pane="Welcome">
 <vt:menuitem href="{AllForms}" shortcut="a" text="Forms Test Pages"/>
 <vt:menuitem href="Tables.jsp" shortcut="{red}" text="Tables test."/>
 <vt:menuitem href="\\{duff\\}" accessKey="b" text="Layouts Test Pages"/>
 <vt:menuitem href="{Mixed}" accessKey="{yellow}" text="Mixed Test Pages"/>
</vt:menu>





<!--
the following Form tag attributes can be Mariner Expresions
action, help and prompt
-->

<vt:p pane="Fields">
<vt:br/>
<vt:h1>Form Test</vt:h1>
</vt:p>

<vt:p pane="Fields">

<vt:xfform
        name="Form"
        method="get"
        action="{submit}"
        help="{field2Help}"
        prompt="{formPrompt}">



<!--
the following Single Select  attributes can be Mariner Expresions
errmsg, caption, help, propmt and shortcut
-->

<vt:br/>

<vt:xfsiselect
        active="true"
        name="field1"
        caption="{field1Caption}"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="\\{Test escaped error message\\}"
        help="do you really need help"
        prompt="{field1Prompt}"
        shortcut="a">
<!--
the following Option  attributes can be Mariner Expresions
caption
-->

  <vt:xfoption caption="{field1Option1}" value="coffee"/>
  <vt:xfoption caption="Tea" value="tea"/>
  <vt:xfoption caption="\\{Milk\\}" value="milk"/>
  <vt:xfoption caption="{field1Option4}" value="nothing"/>

</vt:xfsiselect>

<!--
the following Multi Select  attributes can be Mariner Expresions
errmsg, caption, help, propmt and shortcut
-->
<vt:br/>

<vt:xfmuselect
        active="true"
        name="field111"
        caption="Multi Drinks"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}">
        shortcut="{red}"

 <vt:xfoption caption="milky coffee" value="coffee"/>
  <vt:xfoption caption="Tea" value="tea"/>
  <vt:xfoption caption="\\Milk\\}" value="milk"/>
  <vt:xfoption caption="Orange" value="orange"/>
</vt:xfmuselect>


<!--
the following Boolean attributes can be Mariner Expresions
caption, help, prompt, shortcut, trueValue and falseValue
-->

<vt:xfboolean
        name="field2"
        caption="{field2Caption}"
        captionPane="Fields"
        entryPane="Fields"
        help="Come on, it is obvious what this field means"
        prompt="{field2Prompt}"
        falseValue="No way"
	trueValue="Okay doe kay"
	shortcut="{yellow}"/>
				
<!--
the following Input attributes can be Mariner Expresions
caption, help, prompt, shortcut, validate and errmsg
-->
						  
<vt:xftextinput
        active="true"
	shortcut="a"	  
        styleClass="singleRow"
        name="singleInput"
        caption="Text Input"
        captionPane="Fields"
        entryPane="Fields"
        help="Text Input: Enter a number with 3 digits"
	errmsg="Text Input: Enter a number with 3 digits"
        prompt="Enter a number"
        validate="N:###"/>

<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="field5"
        caption="Text area"
        captionPane="Fields"
        entryPane="Fields"
	errmsg="{field1Error}"
        help="{field2Help}"
        prompt="{field2Prompt}"
        shortcut="{yellow}"
        validate="{num_validate}"/>
						   

<% String shortcut = new String("vnd.wtv-y"); %>

<!--
the following action attributes can be Mariner Expresions
caption, help, prompt, shortcut
-->

<vt:xfaction type="submit"
        styleClass="button"
        shortcut="{red}"
        caption="Submit Form"
        captionPane="Fields"
        entryPane="Fields"
        help="{field2Help}"
        name="action1"
        prompt="Go on"/>

<vt:xfaction type="submit"
        shortcut="f"
        caption="<%=\"Action 2\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="Help is not avilable"
        name="action2"
        prompt="{submitPrompt}"/>

<vt:xfaction type="reset"
        shortcut="{yellow}"
        caption="Reset"
        captionPane="Fields"
        entryPane="Fields"
        help="Reset the form"
        prompt="come on"/>

<vt:xfaction type="submit"
        shortcut="g"
        caption="Another submit"
        captionPane="Fields"
        entryPane="Fields"
        help="{submitHelp}"
        name="action4"
        prompt="{submitPrompt}"/>

<vt:xfaction type="submit"
        shortcut="{red}"
        caption="Last submit"
        captionPane="Fields"
        entryPane="Fields"
        help="can't help"
        prompt="prompt"/>

<vt:xfimplicit name="name" value="Paul"/>

</vt:xfform>

</vt:p>
</vt:canvas>
