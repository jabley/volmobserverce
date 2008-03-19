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
<vt:canvas layoutName="formstest2">
<vt:h2 pane="preamble">Here is a simple form to test</vt:h2> 
<vt:form  preamblePane="preamble"
               postamblePane="postamble"
               action="PrintFormsFields.jsp" 
               submitText="Do It" 
               resetText="Clear">
<vt:textinput labelPane="namelabel" entryPane="nameentry" 
                    name="Name" size="20" maxLength="20">Full Name: </vt:textinput>
<vt:textinput labelPane="addresslabel" entryPane="addressentry" 
                    name="Address" size="20" maxLength="100">Address: </vt:textinput>
<vt:select multiple="false" menuStyle="true" labelPane="occupationlabel" entryPane="occupationentry"
                    name="Occupation" default="tart" label="Occupation:">
<vt:option value="vicar">Vicar</vt:option>
<vt:option value="tart">Tart</vt:option>
<vt:option value="clown">Clown</vt:option>
<vt:option value="bdmgr">Business Development Manager</vt:option>
<vt:option value="rcgdrv">Racing Driver</vt:option>
</vt:select>
</vt:form>
</vt:canvas>
