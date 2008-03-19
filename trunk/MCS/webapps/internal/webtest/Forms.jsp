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
<vt:canvas layoutName="FormTestLayout">

<vt:form  pane="preamble" preamblePane="preamble"
               postamblePane="postamble"
               action="ProcessSetup.jsp"
               submitText="Submit"
               resetText="Reset">
 
<vt:select multiple="true" menuStyle="true" labelPane="databasetypelabel" entryPane="databasetypeentry"
                    name="DatabaseType" default="Unknown" label="Database Type:"><vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>
 
<vt:textinput labelPane="machinelabel" entryPane="machineentry"
                   name="Hostname" type="text" size="10"
              maxLength="10">Hostname: </vt:textinput>
 
<vt:textinput labelPane="sourcelabel" entryPane="sourceentry"
                   name="Source" type="text" size="10"
              maxLength="10">ODBC Source / TNS Alias: </vt:textinput>
 
<vt:textinput labelPane="userlabel" entryPane="userentry"
                   name="User" type="text" size="10"
              maxLength="10">Username: </vt:textinput>
 
<vt:textinput labelPane="passwordlabel" entryPane="passwordentry"
                   name="Password" type="password" size="10"
              maxLength="10">Password: </vt:textinput>
 
<vt:textinput labelPane="portlabel" entryPane="portentry"
                   name="Port" type="text" size="10"
              maxLength="10">Portnumber: </vt:textinput>
 
</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="AllForms.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>

