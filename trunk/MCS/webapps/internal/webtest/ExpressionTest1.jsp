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
<!--
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 -->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="error" theme="expression">
    <vt:usePipeline>
        <template:apply>
        <template:bindings>            
            <template:binding name="paneName" value="error"/>
            <template:binding name="className" value="wibble"/>
            <template:binding name="idName" value="crisis"/>
            <template:binding name="plainText" value="And this is content from a value tag."/> 
            <template:binding name="plainTextRef" value="plainText"/> 
        </template:bindings>
        <template:definition>
            <template:declarations>                
                <template:parameter name="paneName" type="simple" use="required" expressionVariable="paneName"/>
                <template:parameter name="className" type="simple" use="required" expressionVariable="className"/>
                <template:parameter name="idName" type="simple" use="required" expressionVariable="idName"/>
                <template:parameter name="plainText" type="simple" use="required" expressionVariable="plainText"/>
                <template:parameter name="plainTextRef" type="simple" use="required" expressionVariable="plainTextRef"/>
            </template:declarations>
            <template:body>
                <vt:p pane="%{$paneName}" styleClass="%{$className}" id="%{$idName}">
                    This should be green text.
                    <template:value ref="%{$plainTextRef}"/>
                </vt:p>
            </template:body>
        </template:definition>
    </template:apply>
    </vt:usePipeline>
</vt:canvas>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jun-03	473/1	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 20-Jun-03	419/1	adrian	VBM:2003061606 added Expression support to jsp attributes

 ===========================================================================
--%>
