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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/MarlinFragments.jsp,v 1.1 2002/11/23 01:04:28 pduffin Exp $
 % ----------------------------------------------------------------------------
 % (c) Volantis Systems Ltd 2002. 
 % ----------------------------------------------------------------------------
 % Change History:
 %
 % Date         Who             Description
 % ---------    --------------- -----------------------------------------------
 % 22-Nov-02    Paul            VBM:2002112214 - Invoke marlin version of
 %                              fragments test.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<%
  String systemId = "http://localhost:8080/volantis/marlin/fragments.xml";
%>

<%@ include file="MarlinParse.jsp" %>
