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
 * $Header: /src/voyager/webapp/internal/webtest/RuntimeLayoutTest.jsp,v 1.1 2002/09/03 10:36:10 sumit Exp $
 ******************************************************************************
 * (c) Volantis Systems Ltd 2001. 
 ******************************************************************************
 * Change History:
 *
 * Date         Who             Description
 * *********    ************    **********************************************
 * 02-Sep-02    Sumit           VBM:2002030703 Created. Test page for
 *                              PaneFormats
 *****************************************************************************
 -->

<%@ include file="/Volantis-mcs.jsp" %>
<%@page import="com.volantis.mcs.context.MarinerPageContext,
    com.volantis.mcs.layouts.DeviceLayout,
    com.volantis.mcs.layouts.Fragment,
    com.volantis.mcs.context.ContextInternals,
    com.volantis.mcs.papi.PaneFormat,
    java.util.Collection,
    java.util.Iterator" %>
<%!
PaneFormat pformat = null;
%>
<vt:canvas layoutName="layoutmod" pageTitle="Layout Modification test">
<vt:pane name="main">
<%
MarinerPageContext mpx = 
            ContextInternals.getMarinerPageContext(marinerRequestContext);
pformat = new PaneFormat(marinerRequestContext);
%>
This Pane had the following:
Colour:<%=pformat.getBackgroundColor()%><vt:br/>
Background component:<%=pformat.getBackgroundComponentName()%><vt:br/>
Background component type:<%=pformat.getBackgroundComponentType()%><vt:br/>
<vt:br/>
But now the background is yellow<vt:br/>
<%
pformat.setBackgroundColor("yellow");
%>
<vt:br/>
The next pane has the following PaneFormat:
<%
pformat = marinerRequestContext.getPaneFormat("main1");
%>
Colour:<%=pformat.getBackgroundColor()%><vt:br/>
Background component:<%=pformat.getBackgroundComponentName()%><vt:br/>
Background component type:<%=pformat.getBackgroundComponentType()%><vt:br/>
</vt:pane>
<%
pformat.setBackgroundColor("red");
%>
<vt:pane name="main1">
<vt:br/>
But it now has a background of red.
</vt:pane>
</vt:canvas>
