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
 % $Header: /src/voyager/webapp/internal/webtest/TVxfforms.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Feb-02    Paul            VBM:2001122105 - Added this header and removed
 %                              reference to pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="iDTV" pageTitle="TV XFForm Test">

<vt:p pane="Welcome">

<vt:h2>TV XF Forms Test Page</vt:h2>

<vt:menu pane="Welcome" type="plaintext" styleClass="someClass">
 <vt:menuitem 
	  accessKey="{red}" 
	  href="TVindex.jsp" 
	  text="TV Test Pages Index"/>
</vt:menu>
<vt:br/>

<vt:p>
	filename: TVxfforms.jsp<vt:br/>
	purpose: test XF forms<vt:br/>
	devices: All TV devices<vt:br/>
	layout: XFFormTest<vt:br/>
	theme: iDTV<vt:br/>

</vt:p>

<vt:p pane="Form Header">
<vt:p>
Some nested text
</vt:p>
</vt:p>

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp"
        prompt="{formPrompt}">

<vt:xfsiselect
        active="active"
        name="field1"
        caption="(yellow) Single Select"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}"
        shortcut="{yellow}">

  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>

</vt:xfsiselect>

<vt:xfmuselect
        active="true"
        name="field1"
        caption="(Green) Multiple Select"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}"
	shortcut="{green}">	
  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>

</vt:xfmuselect>

<vt:xfboolean
        name="field2"
        caption="(fast rewind) Checkbox"
        captionPane="Fields"
        entryPane="Fields"
        help="{field2Help}"
        prompt="{field2Prompt}"
        falseValue="{field2FalseValues}"
        trueValue="{field2TrueValues}"
        shortcut="{fastRewind}"/>

<vt:xftextinput
        active="true"
        styleClass="singleRow"
        name="field4"
        caption="(pause) text box"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        shortcut="{pause}"/>

<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="field5"
        caption="(mute) textarea"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        shortcut="{mute}"/>

<vt:xfaction type="submit"
        caption="(play) submit"
        captionPane="Fields"
        entryPane="Fields"
        help="{submitHelp}"
        prompt="{submitPrompt}"/>

</vt:xfform>

</vt:canvas>
