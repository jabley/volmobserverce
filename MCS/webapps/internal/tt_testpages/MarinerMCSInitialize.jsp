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
 % $Header: /src/voyager/webapp/redist/webprod/MarinerMCSInitialize.jsp,v 1.3 2002/11/22 23:48:17 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 13-Nov-02    Paul            VBM:2002091806 - Created to separate the
 %                              initialization of mariner mcs out into its own
 %                              file. Customers must not modify this file.
 % 22-Nov-02    Paul            VBM:2002091806 - Use application instead of
 %                              config to initialize the MarinerApplication.
 % ======================================================================= --%>

<%-- The JSP page which ensures that Mariner MCS is correctly initialized.
     This file must not be modified by customers. --%>

<jsp:useBean id="marinerApplication"
             class="com.volantis.mcs.servlet.MarinerServletApplication"
             scope="application">
</jsp:useBean>

<%-- The initialize code is outside of the jsp:useBean because the current 
     implementations of jsp:useBean do not ensure that the bean creation 
     and initialization is an atomic operation --%> 

<%marinerApplication.initialize (application);%>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Jul-04	4972/1	adrianj	VBM:2004063013 Fixes to VolantisErrorPage.jsp initialisation method

 ===========================================================================
--%>
