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
 % $Header: /src/voyager/webapp/internal/webtest/XFFormTest3.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
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
Some text in the welcome pane.
<vt:p>
Some nested text in the welcome pane
</vt:p>
</vt:p>

<vt:pane name="FormHeader">
<vt:p>Some text in the form header pane. Text field active.
</vt:p>
</vt:pane>

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp"
        prompt="{formPrompt}">

<!--******************************************************-->  
<!--Mark either the single or multi select field as active-->
<!--******************************************************-->  
      
<vt:xfsiselect
        active="false"
        name="field1"
        caption="{field1Caption}"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}">

  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfsiselect>

<vt:xfmuselect
        active="false"
        name="field1"
        caption="{field1Caption}"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}">

  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfmuselect>

<vt:xftextinput
        active="false"
        styleClass="singleRow"
        name="field1"
        caption="{field1Caption}"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="{field1Validate}"/>

<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="field5"
        caption="<%=new String (\"Validate This\")%>"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="{field1Validate}"/>

<!--Put xfaction here to ensure output compiles without
	exception, even if the jsp lists fields
        after the xfaction. (VBM:2001071102)      -->

<vt:xfaction type="submit"
        shortcut="vnd.wtv-y"
        caption="{submitCaption}"
        captionPane="Fields"
        entryPane="Fields"
        help="{submitHelp}"
        prompt="{submitPrompt}"/>

<vt:xfboolean
        name="field2"
        caption="{field2Caption}"
        captionPane="Fields"
        entryPane="Fields"
        help="{field2Help}"
        prompt="{field2Prompt}"
        falseValue="{field2FalseValues}"
        trueValue="{field2TrueValues}"/>




</vt:xfform>

</vt:canvas>
