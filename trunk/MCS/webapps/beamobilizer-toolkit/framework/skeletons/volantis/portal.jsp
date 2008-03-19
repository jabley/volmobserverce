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
<%@ page import="com.bea.netuix.servlets.controls.application.BodyPresentationContext,
                com.volantis.mcs.bea.weblogic.platform.runtime.Canvas" %>
<%@ page session="false"%>
<%@ taglib uri="render.tld" prefix="render" %>
<%
    BodyPresentationContext body = BodyPresentationContext.getBodyPresentationContext(request);
    Canvas canvas = Canvas.get(body);

%>
<render:beginRender>
    <body
        <render:writeAttribute name="id" value="<%= body.getPresentationId() %>" />
        <render:writeAttribute name="class" value="<%= body.getPresentationClass() %>" defaultValue="bea-portal-body"/>
        <render:writeAttribute name="style" value="<%= body.getPresentationStyle() %>"/>
        <render:writeAttribute name="onload" value="<%= body.getOnloadScript() %>"/>
        <render:writeAttribute name="onunload" value="<%= body.getOnunloadScript() %>"/>
    >
    
    <%-- Begin Canvas --%>
    <%  if(canvas.isMobile()) { %>
        <div
            <render:writeAttribute name="class" value="<%= canvas.toDivClass() %>"/>
        >
    <% } %>
    
            <%-- Begin Body Content --%>
            <div class="bea-portal-body-content">
    
</render:beginRender>
<render:endRender>

            </div>
            <%-- End Body Content --%>
        
        <% if(canvas.isMobile()) { %>
            </div>
        <% } %>
        <%-- End Canvas --%>
        
        
    </body>
</render:endRender>
