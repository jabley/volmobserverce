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
 % $Header: /src/voyager/webapp/internal/webtest/XFFormLiberate.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 30-Jul-01    Paul            VBM:2001071609 - Added this header and added
 %                              some extra markup.
 % 01-Aug-01    Paul            VBM:2001072506 - Made some fields inactive.
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest" pageTitle="XFFormTest">

<vt:p pane="Welcome">

<vt:menu pane="Welcome" type="plaintext" styleClass="someClass">
 <vt:menuitem 
	  accessKey="{red}" 
	  href="AllForms.jsp" 
	  text="(Red) Forms Test Pages"/>
 <vt:menuitem href="Tables.jsp" text="Tables test."/>
 <vt:menuitem href="Layouts.jsp" text="Layouts Test Pages"/>
 <vt:menuitem href="Mixed.jsp" text="Mixed Test Pages"/>
 <vt:menuitem href="Performance.jsp" text="Performance Test Pages"/>
</vt:menu>
<vt:br/>

<vt:p>

<vt:a accessKey="{blue}" 
   href="http://www.bbc.co.uk">(Blue) Anchor to the BBC</vt:a>
</vt:p>
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
        prompt="{submitPrompt}"
        shortcut="{play}"/>

</vt:xfform>

</vt:canvas>
