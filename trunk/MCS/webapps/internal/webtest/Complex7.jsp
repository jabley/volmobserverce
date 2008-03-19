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
   
<vt:canvas layoutName="Complex7" >

<vt:form pane="preamble" preamblePane="preamble" postamblePane="postamble" action="anotherjsppage.jsp" submitText="SBMT"
resetText="RST"> 
<vt:p>Should be in the preamble pane of the form</vt:p>  
<vt:select pane="occupationentry" multiple="false" labelPane="occupationlabel" label="The label in the occupationlabel"
menuStyle="true" name="occupation" default="I steal other people's JSP pages">
<vt:option value="I steal other people's JSP pages">I steal other people's JSP
pages</vt:option>     
<vt:option value="tart">Tart</vt:option>    
<vt:option
value="clown">Clown</vt:option>   
<vt:option value="bdmgr">Business
Development Manager</vt:option>     
<vt:option value="rcgdrv">Racing
Driver</vt:option>  
</vt:select>

<vt:textinput pane="nameentry" name="name" labelPane="namelabel">Please input your
name:</vt:textinput>

<vt:textinput pane="addressentry" name="address" labelPane="addresslabel">Please enter your
address:</vt:textinput>

</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="AllForms.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>




