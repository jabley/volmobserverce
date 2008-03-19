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
<%@ page import="com.volantis.mcs.context.ContextInternals" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %>
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %>
<%@ page import="com.volantis.mcs.marlin.sax.MarlinSAXHelper" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="java.net.URL" %>
<%      MarinerJspRequestContext jspContext =
        MarinerJspRequestContext.getCurrent( pageContext );
        if( jspContext == null )
        {
            jspContext = new MarinerJspRequestContext( pageContext );
        }

        String xml = request.getParameter("xml");
        URL url = new URL("http://localhost:8080/volantis/" + xml);

        InputSource source = new InputSource(url.openStream());
        MarlinSAXHelper.parse(jspContext, null, source);
        jspContext.release();
%>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 ===========================================================================
--%>
