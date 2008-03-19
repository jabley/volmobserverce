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
 % $Header: /src/voyager/webapp/internal/webtest/FragForm.jsp,v 1.3 2002/03/19 13:45:15 sfound Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 04-Mar-02    Paul            VBM:2001101803 - Added this change history,
 %                              removed invalid import and added a submit
 %                              button to the first part of the fragment.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="FragForm" pageTitle="FragFormTest">

<vt:layout>
    <vt:fragment name="wrapfrag" backLinkText="Grandad" linkText="Grandad" />
    <vt:fragment name="Fragment" backLinkText="Not a form" linkText="Not a form" />
    <vt:fragment name="Fragment1" backLinkText="Daddy" linkText="Daddy" />
    <vt:formfragment name="Part1" backLinkText="List Section" linkText="List Section" />
    <vt:formfragment name="Part2" backLinkText="Text Section" linkText="Text Section" />
    <vt:formfragment name="Part3" backLinkText="Submit Section" linkText="Submit Section" />
</vt:layout>

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
 String shortcut = new String("vnd.wtv-y");
%>


 <% String coffee = new String("coffee");
     String tea = new String("tea");
     String milk = new String("milk");
     String nothing = new String("nothing");
  %>

<vt:xfform name="FragForm" method="get"
           action="FragFormSubmit.jsp"
           prompt="<%=prompt%>">

<vt:xfsiselect
        active="true" name="single1" caption="<%=caption%>"
        captionPane="Part1" entryPane="Part1" errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>">


  <vt:xfoption caption="<%=option1%>" value="coffee"/>
  <vt:xfoption caption="<%=option2%>" value="tea"/>
  <vt:xfoption caption="<%=option3%>" value="milk"/>
  <vt:xfoption caption="<%=option4%>" value="nothing"/>

</vt:xfsiselect>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="Action 2"
        captionPane="Part1"
        entryPane="Part1"
        help="<%=SubmitHelp%>"
        name="action2"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfboolean
        name="boolean"
        caption="<%=caption2%>"
        captionPane="Part1"
        entryPane="Part1"
        help="<%=help2%>"
        prompt="<%=prompt3%>"
        falseValue="<%=false_text%>"
        trueValue="<%=true_text%>"/>

<vt:xftextinput
        active="true"
        styleClass="singleRow"
        name="text1"
        caption="<%=caption%>"
        captionPane="Part2"
        entryPane="Part2"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="text2"
        caption="<%=caption%>"
        captionPane="Part3"
        entryPane="Part3"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<vt:xfaction type="submit"
        shortcut="<%=shortcut%>"
        caption="Action 1"
        captionPane="Part3"
        entryPane="Part3"
        help="<%=SubmitHelp%>"
        name="action1"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfimplicit name="name" value="Steve"/>

</vt:xfform>

<vt:p pane="Part4">This is another fragment outside the form.</vt:p>
</vt:canvas>


