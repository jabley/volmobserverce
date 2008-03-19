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
        pageTitle="Test1"
        layoutName="error">
  <vt:usePipeline>
    <vt:pane name="error">
      This page will now be redirected
    </vt:pane>

    <serv:includeJSP path="tt_includeRedirect.jsp?param1=23&param2=47&param3=text%20string"/>
  </vt:usePipeline>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Aug-03	1129/5	buildusr	VBM:2003070101 failure

 01-Jul-03	676/4	allan	VBM:2003062506 Changed servlet prefix to serv as servlet is reserved in Tomcat 4.0.1

 01-Jul-03	658/1	philws	VBM:2003062506 Provide includeJSP and includeServlet instead of includeServerResource

 ===========================================================================
--%>
