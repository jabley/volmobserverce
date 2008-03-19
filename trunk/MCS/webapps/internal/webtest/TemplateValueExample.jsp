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

<vt:canvas uaContext="new"
           pageTitle="Template Value"
           layoutName="tt_xfoption">
  <template:apply>
    <template:bindings>
      <template:binding name="URL1" value="http://www.volantis.com"/>
      <template:binding name="URL2" value="http://www.ibm.com"/>
    </template:bindings>

    <template:definition>

      <template:declarations>

        <template:parameter name="URL1" 
                            type="simple" 
                            expressionVariable="V_URL1"/>
        <template:parameter name="URL2" 
                            type="simple" 
                            expressionVariable="V_URL2"/>
      </template:declarations>
      <template:body>
        <vt:pane name="fileinfo">
          <vt:p>Here are some URLs:<vt:br/>
            <vt:a href='%{$V_URL1}'>Volantis (<template:value ref="URL1"/>)</vt:a><vt:br/>
            <vt:a href='%{$V_URL2}'>IBM (<template:value ref="URL2"/>)</vt:a><vt:br/>
          </vt:p>
        </vt:pane>
      </template:body>
    </template:definition>
  </template:apply>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-03	1148/1	philws	VBM:2003081805 Fix template:value so that its content is written correctly

 ===========================================================================
--%>
