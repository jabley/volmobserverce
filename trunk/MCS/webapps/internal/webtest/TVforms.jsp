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
<vt:canvas layoutName="Performance3" theme="iDTV" pageTitle="TV Forms Test">
<vt:p pane="preamble">
<vt:h2>TV Forms Test Page</vt:h2>
	filename:TVforms.jsp<vt:br/>
	purpose: Test Forms, incuding transparency on vt:textinput<vt:br/>
	devices: All<vt:br/>
	layout:Perforamce3<vt:br/>
	theme: iDTV<vt:br/>
</vt:p>

<vt:form preamblePane="preamble" postamblePane="postamble" action="formaction.jsp" submitText="Submit" resetText="Reset">

<vt:textarea name="general" rows="3" cols="4" entryPane="entry1" labelPane="label1" styleClass="transparent">
General Information</vt:textarea>

<vt:textinput name="snack" labelPane="label2" entryPane="entry2" styleClass="transparent">What is your favorite snack?</vt:textinput>


</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="TVindex.jsp">TV Test Pages index</vt:a>
</vt:p>
</vt:canvas>

