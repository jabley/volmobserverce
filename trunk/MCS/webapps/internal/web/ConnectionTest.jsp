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
<%--
 * ----------------------------------------------------------------------------
 * $Header: /src/voyager/webapp/internal/web/ConnectionTest.jsp,v 1.1 2001/12/27 15:54:59 jason Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Jul-01    Paul            VBM:2001050209 - Created.
 * ----------------------------------------------------------------------------
--%>
<%@page import="com.volantis.mcs.utilities.*, com.volantis.mcs.repository.*, com.volantis.mcs.styles.*, java.sql.*" %> 
<jsp:useBean id="volantis" class="com.volantis.mcs.utilities.Volantis"
             scope="application"/>
<%volantis.initialize(application);%>

<%
RepositoryConnection connection = volantis.getConnection();
connection.disconnect ();
if (connection.isConnected ()) {
  out.println ("Error, connection is still connected");
} else {
  out.println ("Success, connection has been disconnected");
}
%>
