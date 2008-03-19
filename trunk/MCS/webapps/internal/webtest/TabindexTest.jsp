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
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 05-Jun-02    Byron           VBM:2002053002 - Created a jsp page to test
 %                              tabindex on form elements, dynamic visuals,
 %                              anchors and others
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="tabindex" pageTitle="Tab Index Test">

<vt:p pane="Anchors">
<vt:a href="blah.html" tabindex="1">Tab index 1</vt:a>
<vt:a href="blah.html" tabindex="3">Tab index 3</vt:a>
<vt:a href="blah.html" tabindex="2">Tab index 2</vt:a>
<vt:a href="blah.html">Tab index NONE 1</vt:a>
<vt:a href="blah.html">Tab index NONE 2</vt:a>
</vt:p>

<vt:mmflash pane="dynVis"
    styleClass="flash_movies"
    id="movie1"
    name="snake"
    play="true"
    loop="true"
    menu="true"
    altImg="volantis"
    altText="Dynamic visual should appear here"/>

<vt:mmflash pane="dynVis"
    styleClass="flash_movies"
    id="movie1"
    name="snake"
    play="true"
    loop="true"
    menu="true"
    altImg="volantis"
    altText="Dynamic visual should appear here"
    tabindex="10"/>
<br>
    <vt:mmflash pane="dynVis"
    styleClass="flash_movies"
    id="movie1"
    name="snake"
    play="true"
    loop="true"
    menu="true"
    altImg="volantis"
    altText="Dynamic visual should appear here"
    tabindex="10"/>

<vt:p pane="TopPane">
Testing the tabindex for form elements
<vt:p>
<vt:b>Test the following:</vt:b>
<ul>
<li>xfaction</li>
<li>xfboolean</li>
<li>xfmuselect</li>
<li>xfsiselect</li>
<li>xftextinput</li>
<li>a (anchor)</li>
<li>dynamic visuals</li>
</ul>
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
        action="TabindexTest.jsp"
        prompt="<%=prompt%>">

<vt:xfsiselect
        active="true"
        name="field1"
        caption="<%=caption%>"
        captionPane="TopPane"
        entryPane="TopPane"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        tabindex="101">


  <vt:xfoption caption="<%=option1%>" value="coffee"/>
  <vt:xfoption caption="<%=option2%>" value="tea"/>
  <vt:xfoption caption="<%=option3%>" value="milk"/>
  <vt:xfoption caption="<%=option4%>" value="nothing"/>

</vt:xfsiselect>
<br>
<vt:xfmuselect
        active="true"
        name="field1"
        caption="<%=caption%>"
        captionPane="TopPane"
        entryPane="TopPane"
        errmsg="<%=errmsg%>"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        tabindex="10">


  <vt:xfoption caption="<%=coffee%>" value="coffee"/>
  <vt:xfoption caption="<%=tea%>" value="tea"/>
  <vt:xfoption caption="<%=milk%>" value="milk"/>
  <vt:xfoption caption="<%=nothing%>" value="nothing"/>
</vt:xfmuselect>
<br>
<vt:xfboolean
        name="field2"
        caption="<%=caption2%>"
        captionPane="TopPane"
        entryPane="TopPane"
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
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<vt:xftextinput
        active="false"
        styleClass="multipleRows"
        name="field5"
        caption="<%=caption%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=help%>"
        prompt="<%=prompt2%>"
        validate="<%=validate%>"/>

<% String shortcut = new String("vnd.wtv-y"); %>
<br>
<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=SubmitHelp%>"
        name="action1"
        prompt="<%=SubmitPrompt%>"
        value="action1"
        tabindex="104"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 2\"%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=SubmitHelp%>"
        name="action2"
        prompt="<%=SubmitPrompt%>"
        tabindex="101"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 3\"%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"
        tabindex="200"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption4%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=SubmitHelp%>"
        name="action4"
        prompt="<%=SubmitPrompt%>"
        value="action4"
        tabindex="300"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption5%>"
        captionPane="TopPane"
        entryPane="TopPane"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        shortcut="<%=shortcut%>"
        value="action6"
        caption="Button 6"
        captionPane="TopPane"
        entryPane="TopPane" />

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        name="submitbutton"
        shortcut="<%=shortcut%>"
        value="action7"
        caption="Button 7"
        captionPane="TopPane"
        entryPane="TopPane" />

<vt:xfimplicit name="name" value="Paul"/>

</vt:xfform>

</vt:canvas>
