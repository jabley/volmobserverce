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
<vt:canvas layoutName="Forms2" theme="Forms2">

<vt:form  pane="preamble" preamblePane="preamble"
               postamblePane="postamble"
               action="ProcessSetup.jsp"
               submitText="Submit"
               resetText="Reset">


<vt:select 	multiple="true" 
		styleClass="selectstyle" 
		menuStyle="true" 
		labelPane="selectlabel1" 
		entryPane="selectentry1"
                name="select1" 
		default="Unknown" 
		label="Database Type:">
<vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>


<vt:select 	multiple="true" 
		menuStyle="true" 
		labelPane="selectlabel1" 
		entryPane="selectentry1"
                name="select1" 
		default="Unknown" 
		label="Database Type:">
<vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>




 
<vt:select multiple="true" menuStyle="false" labelPane="selectlabel2" entryPane="selectentry2"
                    name="select2" default="Unknown" label="Database Type:"><vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>
  
<vt:select multiple="false" menuStyle="true" labelPane="selectlabel3" entryPane="selectentry3"
                    name="select3" default="Unknown" label="Database Type:"><vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>

<vt:select multiple="false" menuStyle="false" labelPane="selectlabel4" entryPane="selectentry4"
                    name="select4" default="Unknown" label="Database Type:"><vt:option value="oracle">Oracle</vt:option>
<vt:option value="odbc">SQL Server</vt:option>
<vt:option value="odbc">Sybase</vt:option>
<vt:option value="informix">Informix</vt:option>
<vt:option value="db2">DB2</vt:option>
<vt:option value="unknown">Unknown</vt:option>
</vt:select>
  

</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="AllForms.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>


