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
<%@ page import="com.bea.netuix.servlets.controls.window.WindowPresentationContext,
                 com.bea.netuix.servlets.controls.window.TitlebarPresentationContext,
                 java.util.List,
                 java.util.Iterator,
                 com.bea.netuix.servlets.controls.page.BookPresentationContext,
                 com.bea.netuix.servlets.controls.window.WindowCapabilities,
                 com.volantis.mcs.bea.weblogic.platform.runtime.Canvas"
%>
<%@ page session="false"%>
<%@ taglib uri="render.tld" prefix="render" %>

<%
    WindowPresentationContext window = WindowPresentationContext.getWindowPresentationContext(request);
    boolean isMinimized =  window.getWindowState().equals(WindowCapabilities.MINIMIZED);
    Canvas canvas = Canvas.get(window);
%>
<render:beginRender>
<%
    TitlebarPresentationContext titlebar = (TitlebarPresentationContext) window.getTitlebarPresentationContext();
    final String expandWidth = "100%";    
%>
    <%-- Begin Canvas --%>
    <%  if(canvas.isMobile()) { %>
        <div
            <render:writeAttribute name="class" value="<%= canvas.toDivClass() %>"/>
        >
    <% } %>

    <%-- Begin Window --%>
    <div
        <render:writeAttribute name="id" value="<%= window.getPresentationId() %>"/>
        <render:writeAttribute name="class" value="<%= window.getPresentationClass() %>" defaultValue="bea-portal-window"/>
        <render:writeAttribute name="style" value="<%= window.getPresentationStyle() %>"/>
        <render:writeAttribute name="width" value="<%= window.isPacked() ? null : expandWidth %>"/>
    >
        <render:renderChild presentationContext="<%= titlebar %>"/>
        <%-- Begin Window Content --%>
<%  if (! isMinimized )
    {
        %><div
            <render:writeAttribute name="class" value="<%= window.getContentPresentationClass() %>" defaultValue="bea-portal-window-content"/>
            <render:writeAttribute name="style" value="<%= window.getContentPresentationStyle() %>" />
        ><%
    }
%>
</render:beginRender>
<render:endRender>
<%  if (! isMinimized )
    {
        %></div><%
    }
%>
    </div>
    <%-- End Window --%>
    
    <%  if(canvas.isMobile()) { %>
        </div>
    <% } %>
    <%-- End Canvas --%>
    
</render:endRender>
