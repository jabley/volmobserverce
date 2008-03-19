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

<%! com.volantis.xml.expression.Value developer; %>

<vt:canvas layoutName="error">
    <vt:usePipeline>
        <template:apply>
            <template:bindings>
                <template:binding name="name" value="Phil"/>
                <template:binding name="company">
                    <template:complexValue evaluationMode="deferred">
                        <pipeline:content>
                            <b>Volantis</b>
                        </pipeline:content>
                    </template:complexValue>
                </template:binding>
            </template:bindings>
            <template:definition>
                <template:declarations>
                    <template:parameter name="name"
                                        use="required"
                                        type="simple"
                                        runtimeVariable="developer"/>
                    <template:parameter name="company"
                                        use="required"
                                        type="complex"/>
                </template:declarations>
                <template:body>
                    <vt:pane name="error">
                        <vt:h1>Template Pipeline Example 2</vt:h1>
                        <vt:p>Welcome, <%= developer.stringValue().asJavaString() %>!</vt:p>
                        <vt:p/>
                        <vt:p>Your name, <template:value ref="name"/>, was inserted
                           using a runtimeVariable binding in the welcome
                           message, but was inserted using the template value
                           tag in this paragraph.</vt:p>
                        <vt:p>Don't you just love <template:value ref="company"/>?</vt:p>
                    </vt:pane>
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

 31-Jul-03	828/2	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 25-Jun-03	473/1	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 19-Jun-03	452/1	philws	VBM:2003061905 MCS Template Model implementation

 ===========================================================================
--%>
