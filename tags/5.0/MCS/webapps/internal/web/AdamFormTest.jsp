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
<%@ include file="Volantis-mcs.jsp" %> 
<vt:canvas layoutName="adamFormTest" theme="adam">
<%// This part tests the column iterator part /%>
<vt:form method="get" action="AdamFormTest.jsp" 
         preamblePane="f1" 
         postamblePane="f3" 
         submitName="hello">
<vt:p pane="f2"> Defaulty display with no images or panes</vt:p>
</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f4" 
         postamblePane="f6" 
         submitPaneName="f4" 
         submitImage="volantis">
<vt:p pane="f5"> SubmitImage to the left </vt:p>
</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f7" 
         postamblePane="f9" 
         submitPaneName="f9" 
         submitImage="volantis">
<vt:p pane="f8"> SubmitImage to the right </vt:p>
</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f10" 
         postamblePane="f12" 
         submitPaneName="f12" 
         submitImage="volantis"
         resetPaneName="f10">
<vt:p pane="f11">Rest button and Submit Images </vt:p>
</vt:form>

<vt:form method="get" action="AdamFormTest.jsp"
         preamblePane="f13" 
         postamblePane="f15" 
         submitPaneName="f13" 
         submitImage="submit"
         resetPaneName="f15"
         submitName="submitNameonURL"> 
        
<vt:p pane="f14">Rest button and Submit Images Again</vt:p>
</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f16" 
         postamblePane="f18" 
         submitPaneName="f16" 
         submitImage="volantis"
         submitText="submitText"
         resetPaneName="f18">
<vt:p pane="f17">Submit Text (image not found) and reset button </vt:p>

</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f19" 
         postamblePane="f21" 
         submitPaneName="f19" 
         submitImage="stars"
         submitText="submitText"
         resetText="resetText"
         resetPaneName="f21">
<vt:p pane="f20">Submit image and reset button and resetText </vt:p>

</vt:form>

<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f22" 
         postamblePane="f24" 
         resetPaneName="f22">

<vt:p pane="f23">ResetpaneName only (default submit button added )</vt:p>

</vt:form>
<vt:form method="post" action="AdamFormTest.jsp"
         preamblePane="f25" 
         postamblePane="f27" 
         resetText="resetText">

<vt:p pane="f26">ResetpaneName only (default submit button added )</vt:p>

</vt:form>
</vt:canvas>
