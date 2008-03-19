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
<vt:canvas layoutName="Complex3">

<vt:pane name="localauthorityimage">
<vt:p>Insdide the local autority , should be first.</vt:p>

<vt:p>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>


<vt:p pane="bodyleft">Should be a table below with 5 columns. In the bodyleft pane.</vt:p>
<vt:table pane="bodyleft" cols="5">
<vt:tr>
<vt:td>Data 1</vt:td>
<vt:td>Data 2</vt:td>
<vt:td>Data 3</vt:td>
<vt:td>Data 4</vt:td>
<vt:td>Data 5</vt:td>
</vt:tr>
<vt:tr><vt:td>Data 1</vt:td></vt:tr>
<vt:tr><vt:td>Data 1</vt:td></vt:tr>
</vt:table>

</vt:pane>

<vt:p pane="body">Inside the body</vt:p>
<vt:p pane="localauthorityimage">Inside the local authority, should be second.</vt:p>

<vt:p pane="globalheading">
This is the global heading.
Should be in the global heading.
</vt:p>

<vt:p pane="advert">This is the advert, below is a form that ocuppies the four step panes.</vt:p>

<vt:form pane="step1" action="CalculateSomething.jsp" method="POST"
submitText="Please submit me.." resetText="Having second thoughts ??"
preamblePane="step1" postamblePane="step4">
<vt:textinput pane="step2" name="UserInput" size="15" type="text">Input
some text here:</vt:textinput>
<vt:select pane="step3" name="UserSelection" multiple="true" default="something">
<vt:option value="1">First option</vt:option>
<vt:option value="2">Second option</vt:option>
</vt:select>
</vt:form>

<vt:p pane="comment">This is a comment.</vt:p>
<vt:p pane="buttons">This is the buttons.</vt:p>
<vt:p pane="nav_bottom">This is the nav_bottom pane.</vt:p>
<vt:p pane="question1">Question 1.</vt:p>
<vt:p pane="question2">Question 2.</vt:p>
<vt:p pane="question3">Question 3.</vt:p>



</vt:canvas>


