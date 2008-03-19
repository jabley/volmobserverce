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
<%-- The Volantis root file for use by JSP pages that provide their own
           error handling. Use this include for normal JSP's, for example in the
           policy management pages --%> 
<%@ page import="com.volantis.mcs.utilities.*, java.util.*"%>
<jsp:useBean id="volantis" class="com.volantis.mcs.utilities.Volantis" 
             scope="application"/>
<%volantis.initialize(application);%>
<%@ taglib uri="WEB-INF/taglibs/volantis-mcs.tld" prefix="vt" %>
