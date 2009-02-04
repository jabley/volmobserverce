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
<%@ page import="volantis.mcs.example.utilities.ExampleMarkupPlugin"%>
<%@ include file="Volantis-mcs.jsp" %>

<%
    ExampleMarkupPlugin.setWriter(response.getWriter());
%>
<vt:canvas layoutName="IndexLayout">  
<vt:pane name="index">
    <vtim:invoke pluginName="examplePlugin" methodName="initialize">
        <vtim:arguments>
          <vtim:argument name="Ford" value="Focus"/>
          <vtim:argument name="Nissan" value="Micra"/>
          <vtim:argument name="Renault" value="Clio"/>
          <vtim:argument name="Vauxhall" value="Corsa"/>
        </vtim:arguments>
    </vtim:invoke>    
    <vtim:invoke pluginName="examplePlugin" methodName="process">
        <vtim:arguments>
          <vtim:argument name="number" value="one"/>
          <vtim:argument name="letter" value="a"/>          
        </vtim:arguments>
    </vtim:invoke>    
    <vtim:invoke pluginName="examplePlugin" methodName="release"/>
    
    <vtim:invoke pluginName="notExamplePlugin" methodName="initialize">
        <vtim:arguments>
          <vtim:argument name="Ford" value="Focus"/>
          <vtim:argument name="Nissan" value="Micra"/>
          <vtim:argument name="Renault" value="Clio"/>
          <vtim:argument name="Vauxhall" value="Corsa"/>
        </vtim:arguments>
    </vtim:invoke>    
    <vtim:invoke pluginName="notExamplePlugin" methodName="process">
        <vtim:arguments>
          <vtim:argument name="number" value="one"/>
          <vtim:argument name="letter" value="a"/>          
        </vtim:arguments>
    </vtim:invoke>    
    <vtim:invoke pluginName="notExamplePlugin" methodName="release"/>
</vt:pane>

</vt:canvas>


<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jul-03	820/1	adrian	VBM:2003071701 Added jsp invocation integration tags

 ===========================================================================
--%>
