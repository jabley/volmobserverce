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

<vt:canvas layoutName="error">
    <vt:usePipeline>
        <template:apply>
            <template:bindings>
                <template:binding name="name">
                    <template:complexValue>
                        <pipeline:content>
                            Cool <a href="http://www.volantis.com">Dude</a> Phil
                        </pipeline:content>
                    </template:complexValue>
                </template:binding>
                <template:binding name="company">
                    <template:complexValue>
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
                                        type="complex"/>
                    <template:parameter name="company"
                                        use="required"
                                        type="complex"/>
                </template:declarations>
                <template:body>
                    <vt:pane name="error">
                        <vt:h1>Template Pipeline Example 5</vt:h1>
                        <vt:p>Welcome, <template:value ref="name"/>!</vt:p>
                        <vt:p/>
                        <vt:p>Your name, <template:value ref="name"/>, has
                           been generated using mixed markup so can't be
                           inserted using a runtimeVariable binding, but can
                           be using the template value tag.</vt:p>
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

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 ===========================================================================
--%>
