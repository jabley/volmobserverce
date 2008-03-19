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
        <pipeline:content>
            <!-- some marlin markup that should be parsed by the content tag-->    
            <pane name="error">
                <p>
                    <h1>Welcome to Volantis Systems Ltd. (tm) </h1>
                    <img src="stars"/>
                    <img src="volantis"/>
                    <h2>This the the default Volantis test home page. </h2>
                    
                    <h3><a href="tt_index.jsp">The new test pages</a></h3>
                    <h3><a href="Certification.jsp">Certification Pages</a></h3>
                    <h3><a href="Performance.jsp">Performance Test Pages</a></h3>
                    <h3><a href="Multimedia.jsp">Multimedia  Test Pages</a></h3>
                    <h3><a href="Layouts.jsp">Layouts Test Pages</a></h3>
                    <h3><a href="Mixed.jsp">Mixed Test Pages</a></h3>
                    <h3><a href="AllForms.jsp">Forms Test Pages</a></h3>
                    <h3><a href="Menus.jsp">Menus Test Pages</a></h3>
                    <h3><a href="Tables.jsp">Tables Test Pages</a></h3>
                </p>
            </pane>    
        </pipeline:content>
    </vt:usePipeline>
</vt:canvas>
    
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jun-03	473/1	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 16-Jun-03	366/1	doug	VBM:2003041502 Integration with pipeline JSPs

 ===========================================================================
--%>
