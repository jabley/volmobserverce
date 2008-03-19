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
 % $Header: /src/voyager/webapp/internal/webtest/LinkXFForm.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 21-Sep-01    Doug            VBM:2001090302 - Created
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest" pageTitle="XFFormTest">

<vt:p pane="Welcome">
Some text <b>bold </b>hello.
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
        action="{submit}"
        prompt="<%=prompt%>">

<vt:xfsiselect
        active="true"
        name="field1"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>">


  <vt:xfoption caption="<%=option1%>" value="coffee"/>
  <vt:xfoption caption="<%=option2%>" value="tea"/>
  <vt:xfoption caption="<%=option3%>" value="milk"/>
  <vt:xfoption caption="<%=option4%>" value="nothing"/>

</vt:xfsiselect>

<vt:xfmuselect
        active="false"
        name="field1"
        caption="<%=caption%>"
        captionPane="Fields"
        entryPane="Fields"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>">


 <vt:xfoption caption="<%=coffee%>" value="coffee"/>
  <vt:xfoption caption="<%=tea%>" value="tea"/>
  <vt:xfoption caption="<%=milk%>" value="milk"/>
  <vt:xfoption caption="<%=nothing%>" value="nothing"/>
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
        active="false"
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
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action1"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 2\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action2"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 3\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption4%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action4"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption5%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfimplicit name="name" value="Paul"/>

</vt:xfform>

</vt:canvas>
