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
<%@ include file="../Volantis-mcs.jsp" %>

<%-- Volantis-mcs currently includes volantis-mcs.tld with a vt prefix --%>
<%-- This may change in the future to include to have an overall file that inclu
des all others --%>

<vt:canvas uaContext="new"
        pageTitle="Test1"
        layoutName="tt_xfoption"
        theme="tt_sqlconnector">
    <vt:usePipeline>
        <vt:pane name="fileinfo">
            <vt:i>This page will now be redirected<vt:br/>
            Expect to see the parameters from the 3 param elements e.g.<vt:br/>
            param1=23; param2=47; param3=text string<vt:br/>
            ****************************************<vt:br/></vt:i>
        </vt:pane>

        <serv:includeJSP path="tt_includeSrvResTagRedirect.jsp">
            <serv:param name="param1" value="23"/>
            <serv:param name="param2" value="47"/>
            <serv:param name="param3" value="text%20string"/>
        </serv:includeJSP>
    </vt:usePipeline>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Aug-03	1115/1	philws	VBM:2003071704 Fix includeJSP to work from XML file

 ===========================================================================
--%>
