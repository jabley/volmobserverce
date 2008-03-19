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
 % $Header: /src/voyager/webapp/internal/webtest/2002011101_XFForm.jsp,v 1.1 2002/01/15 18:40:32 aboyd Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 15-Jan-02    Allan           VBM:2002011101 - Created. (Copied from 
 %                              a jsp done by Doug.) 
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">


<!-- ****************************************
     * Declaring strings for the form.      *
     * This format is required by weblogic  *
     * which can't handle literal strings.  *
     ****************************************-->

<% String caption1=new String("{tt_fieldcaption1}");
   String prompt1=new String("{tt_fieldprompt1}");
   String help1=new String("{tt_fieldhelp1}");
   String errmsg1=new String("{tt_fielderrmsg1}");

   String caption2=new String("{tt_fieldcaption2}");
   String prompt2=new String("{tt_fieldprompt2}");   
   String help2=new String("{tt_fieldhelp2}");
   String errmsg2=new String("{tt_fielderrmsg1}");

   String caption3=new String("{tt_fieldcaption3}");
   String prompt3=new String("{tt_fieldprompt3}");   
   String help3=new String("{tt_fieldhelp3}");   
   String errmsg3=new String("{tt_fielderrmsg1}");

   String formprompt=new String("{tt_formprompt}");
%>

<!--   String captionpane1=new String("{tt_captionpane1}"); -->

<% String captionpane1=new String("formCaption1"); %>
<% String entrypane1=new String("formField1"); %>

<% String submitcaption=new String("Submit Order");
   String submitprompt=new String("Click here to send your order to Volantis");
   String resetcaption=new String("Reset Screen");
   String resetprompt=new String("Click here to reset to initial defaults");

   String option1=new String("Tea");
   String option2=new String("Coffee");
   String option3=new String("Hot Chocolate");
   String option4=new String("Orange Juice");

   String optiona=new String("The Sun");
   String optionb=new String("Telegraph ");
   String optionc=new String("Daily Express");
   String optiond=new String("{tt_nulloption}");

   String false_text=new String("no, nope");
   String true_text=new String("yes, yep, yeah");
%>

<vt:pane name="header">
  
  <vt:h1>Basic Xfform Test</vt:h1>
  <vt:h2>Purpose</vt:h2>
  <vt:p>This test is to ensure the &lt;vt:xfxxxxx&gt; tag works.
  </vt:p>
  <vt:h2>Devices</vt:h2>
  <vt:p>Test on all devices including VoiceXML.
  </vt:p>
  <vt:h2>Expected Results</vt:h2>
  <vt:p>A form should be created with a single select field, boolean field
	and a multiple select field. 
        There should also be an option to submit the form. This should
	generate a response confirming your selections.
  </vt:p>
  <vt:p>For devices that do not support nested tables on forms, a simple
	layout is used that does not align fields and contains the submit
	and reset buttons midway through the form.</vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p><vt:a href="tt_FormIndex.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit.jsp"
		prompt="<%=formprompt%>">

<vt:xfsiselect	name="drink" styleClass="menuformat"
		caption="<%=caption1%>"
		prompt="<%=prompt1%>"
		help="<%=help1%>"
    		initial="Coffee"
		captionPane="<%=captionpane1%>"
		entryPane="<%=entrypane1%>">
	<vt:xfoption caption="<%=option1%>" value="<%=option1%>"/>
        <vt:xfoption caption="<%=option2%>" value="<%=option2%>"/>
        <vt:xfoption caption="<%=option3%>" value="<%=option3%>"/>
        <vt:xfoption caption="<%=option4%>" value="<%=option4%>"/>
</vt:xfsiselect>

<!--**********************************************
    * Action tag in middle to prove the output   *
    * can deal with fields after the action tag  *
    **********************************************-->

<vt:xfaction	type="submit"
                styleClass="submitbutton"
		caption="<%=submitcaption%>"
		captionPane=""
		prompt="<%=submitprompt%>"
		entryPane="formFooter"/>

<vt:xfaction	type="reset"
		caption="<%=resetcaption%>"
		captionPane=""
		prompt="<%=resetprompt%>"
		entryPane="formFooter"/>

<vt:xfboolean
        name="sugar"
        caption="<%=caption2%>"
        captionPane="formCaption2"
        entryPane="formField2"
        help="<%=help2%>"
        prompt="<%=prompt2%>"
        falseValue="<%=false_text%>"
        trueValue="<%=true_text%>"/>

<vt:xfmuselect
        active="true"
        name="papers"
        caption="<%=caption3%>"
        captionPane="formCaption3"
        entryPane="formField3"
        errmsg="<%=errmsg3%>"
        help="<%=help3%>"
        prompt="<%=prompt3%>">

  <vt:xfoption caption="<%=optiona%>" value="<%=optiona%>"/>
  <vt:xfoption caption="<%=optionb%>" value="<%=optionb%>"/>
  <vt:xfoption caption="<%=optionc%>" value="<%=optionc%>"/>
  <vt:xfoption caption="<%=optiond%>" value="none"/>
</vt:xfmuselect>



		
</vt:xfform>




<!-- ****************************************
     * This section is repeated for the     *
     * WideWML layout that can't cope with  *
     * a table of panes on a form.          *
     ****************************************-->


<vt:pane name="simpleForm">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>
  
<vt:xfform 	name="simpleForm" 
		action="tt_xfformsubmit.jsp"
		prompt="<%=formprompt%>">

<vt:xfsiselect	name="drink" styleClass="menuformat"
		caption="<%=caption1%>"
		prompt="<%=prompt1%>"
		help="<%=help1%>"
    		initial="Coffee"
		captionPane="simpleForm"
		entryPane="simpleForm">
	<vt:xfoption caption="<%=option1%>" value="<%=option1%>"/>
        <vt:xfoption caption="<%=option2%>" value="<%=option2%>"/>
        <vt:xfoption caption="<%=option3%>" value="<%=option3%>"/>
        <vt:xfoption caption="<%=option4%>" value="<%=option4%>"/>
</vt:xfsiselect>

<!--**********************************************
    * Action tag in middle to prove the output   *
    * can deal with fields after the action tag  *
    **********************************************-->

<vt:xfaction	type="submit"
                styleClass="submitbutton"
		caption="<%=submitcaption%>"
		captionPane=""
		prompt="<%=submitprompt%>"
		entryPane="simpleForm"/>

<vt:xfaction	type="reset"
		caption="<%=resetcaption%>"
		captionPane=""
		prompt="<%=resetprompt%>"
		entryPane="simpleForm"/>

<vt:xfboolean
        name="sugar"
        caption="<%=caption2%>"
        captionPane="simpleForm"
        entryPane="simpleForm"
        help="<%=help2%>"
        prompt="<%=prompt2%>"
        falseValue="<%=false_text%>"
        trueValue="<%=true_text%>"/>

<vt:xfmuselect
        active="true"
        name="papers"
        caption="<%=caption3%>"
        captionPane="simpleForm"
        entryPane="simpleForm"
        errmsg="<%=errmsg3%>"
        help="<%=help3%>"
        prompt="<%=prompt3%>">

  <vt:xfoption caption="<%=optiona%>" value="<%=optiona%>"/>
  <vt:xfoption caption="<%=optionb%>" value="<%=optionb%>"/>
  <vt:xfoption caption="<%=optionc%>" value="<%=optionc%>"/>
  <vt:xfoption caption="<%=optiond%>" value="none"/>
</vt:xfmuselect>



		
</vt:xfform>


</vt:canvas>
