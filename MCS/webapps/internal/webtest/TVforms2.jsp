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
<vt:canvas layoutName="Performance3" theme="iDTV">
<vt:p pane="preamble"><vt:h1>Exercise Vs Food intake</vt:h1></vt:p>
<vt:form preamblePane="preamble" postamblePane="postamble" action="formaction.jsp" submitText="Submit" resetText="Reset">

<vt:textarea name="general" rows="3" cols="4" entryPane="entry1" labelPane="label1" styleClass="transparent">
General Information</vt:textarea>

<vt:textinput name="snack" labelPane="label2" entryPane="entry2" styleClass="transparent">What is your favorite snack?</vt:textinput>


</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Performance.jsp">Back</vt:a>
</vt:p>
</vt:canvas>

