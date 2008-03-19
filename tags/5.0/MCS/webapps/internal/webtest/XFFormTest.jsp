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
 % $Header: /src/voyager/webapp/internal/webtest/XFFormTest.jsp,v 1.6 2002/08/16 15:23:32 pduffin Exp $
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
 % 14-Sep-01    Paul            VBM:2001091302 - Added text component names
 %                              for some of the select options.
 % 14-Sep-01    Paul            VBM:2001091405 - Added a name to the existing
 %                              xfaction tag and added four more, two with and
 %                              two without names.
 % 20-Sep-01    Paul            VBM:2001091202 - Added an implicit value.
 % 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 % 04-Oct-01    Doug            VBM:2001100201 - reactivated th input field 
 %                              field4 and provided shortcut attribute
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 10-Dec-01    Paul            VBM:2001110101 - Changed <b> into <vt:b>.
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % 19-Feb-02    Paul            VBM:2001100102 - Added a couple of value
 %                              attributes to the xfaction.
 % 04-Mar-02    Paul            VBM:2001101803 - Deactivated a couple of the
 %                              fields.
 % 05-Apr-02    Ian             VBM:2002030606 - Added tests to test submit 
 %                              buttons with the same names 
 % 15-Aug-02    Paul            VBM:2002081421 - Changed on of the action
 %                              buttons to a reset button.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest" pageTitle="XFFormTest">

<vt:p pane="Welcome">
Some text <vt:b>bold </vt:b>hello.
<vt:p>
Some nested text
</vt:p>
</vt:p>

<vt:p pane="Form Header">
Some text
<vt:p>
Some nested text
</vt:p>
</vt:p>

<%
 String prompt= new String ("{formPrompt}");
 String caption = new String ("{field1Caption}");
 String errmsg = new String ("{field1Error}");
 String help = new String ("{field1Help}");
 String prompt2= new String ("{field1Prompt}");
 String validate = new String ("{field1Validate}");
 String SubmitCaption= new String ("{submitCaption}");
 String SubmitCaption4= new String ("{submitCaption4}");
 String SubmitCaption5= new String ("{submitCaption5}");
 String SubmitHelp = new String ("{submitHelp}");
 String SubmitPrompt= new String ("{submitPrompt}");
 String caption2 = new String ("{field2Caption}");
 String help2 = new String ("{field2Help}");
 String prompt3= new String ("{field2Prompt}");
 String false_text= new String ("{field2FalseValues}");
 String true_text= new String ("{field2TrueValues}");

 String option1 = new String ("{field1Option1}");
 String option2 = new String ("{field1Option2}");
 String option3 = new String ("{field1Option3}");
 String option4 = new String ("{field1Option4}");
%>


 <% String coffee = new String("coffee");
     String tea = new String("tea");
     String milk = new String("milk");
     String nothing = new String("nothing");
  %>

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp"
        prompt="<%=prompt%>">

<vt:xfsiselect
        active="true"
        name="field1"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        initial="tea"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>">


  <vt:xfoption caption="<%=option1%>" value="coffee"/>
  <vt:xfoption caption="<%=option2%>" value="tea"/>
  <vt:xfoption caption="<%=option3%>" value="milk"/>
  <vt:xfoption caption="<%=option4%>" value="nothing"/>

</vt:xfsiselect>

<vt:xfmuselect
        active="true"
        name="field1"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>">


  <vt:xfoption caption="<%=coffee%>" value="coffee"/>
  <vt:xfoption caption="<%=tea%>" value="tea" selected="true"/>
  <vt:xfoption caption="<%=milk%>" value="milk"/>
  <vt:xfoption caption="<%=nothing%>" value="nothing" selected="true"/>
</vt:xfmuselect>

<vt:xfboolean
        name="field2"
        caption="<%=caption2%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=help2%>"
        prompt="<%=prompt3%>"
        falseValue="<%=false_text%>"
        trueValue="<%=true_text%>"/>

<vt:xftextinput
        active="true"
	shortcut="a"	  
        styleClass="singleRow"
        name="field4"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<vt:xftextinput
        active="false"
        styleClass="multipleRows"
        name="field5"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<% String shortcut = new String("vnd.wtv-y"); %>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action1"
        prompt="<%=SubmitPrompt%>"
        value="action1"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 2\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action2"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 3\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption4%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action4"
        prompt="<%=SubmitPrompt%>"
        value="action4"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption5%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        shortcut="<%=shortcut%>"
        value="action6"
        caption="Button 6"
        captionPane="Fields"
        entryPane="Fields" />

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        name="submitbutton"
        shortcut="<%=shortcut%>"
        value="action7"
        caption="Button 7"
        captionPane="Fields"
        entryPane="Fields" />

<vt:xfimplicit name="name" value="Paul"/>

</vt:xfform>

</vt:canvas>
