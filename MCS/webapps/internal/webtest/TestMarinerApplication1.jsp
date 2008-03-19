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
 % $Header: /src/voyager/webapp/internal/webtest/TestMarinerApplication1.jsp,v 1.4 2002/11/22 23:48:17 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 13-Nov-02    Paul            VBM:2002091806 - Created to test new
 %                              MarinerServletApplication classes.
 % 22-Nov-02    Paul            VBM:2002091806 - Use application instead of
 %                              config to initialize the MarinerApplication.
 % ======================================================================= --%>

<%-- ==========================================================================
 % This page tests that the Volantis bean can be created before the
 % MarinerServletApplication. It must be run in a newly started instance of the
 % application server before any other mariner pages are run.
 %
 % It checks that the following conditions hold:
 %
 % 1) The MarinerServletApplication defined by the jsp:useBean is
 %    the same as the one accessed through the API.
 % 2) The Volantis instance defined by the jsp:useBean is the same as the
 %    one referenced by the MarinerServletApplication.
 % ======================================================================= --%>

<%@ page import="com.volantis.mcs.application.ApplicationInternals" %>
<%@ page import="com.volantis.mcs.servlet.MarinerServletApplication" %>
<%@ page import="com.volantis.mcs.utilities.Volantis" %>

<%-- Create the volantis bean first. --%>

<jsp:useBean id="volantis" class="com.volantis.mcs.utilities.Volantis" 
             scope="application">
</jsp:useBean>

<%-- The initialize code is outside of the jsp:useBean because the current 
     implementations of jsp:useBean do not ensure that the bean creation 
     and initialization is an atomic operation --%> 

<%volantis.initialize(application);%>

<%-- Create the mariner servlet application second. --%>

<jsp:useBean id="marinerApplication"
             class="com.volantis.mcs.servlet.MarinerServletApplication"
             scope="application">
</jsp:useBean>

<%-- The initialize code is outside of the jsp:useBean because the current 
     implementations of jsp:useBean do not ensure that the bean creation 
     and initialization is an atomic operation --%> 

<%marinerApplication.initialize (application);%>

<%
 MarinerServletApplication servletApplication
   = MarinerServletApplication.getInstance (application);

 if (marinerApplication == servletApplication) {
   out.println ("<h1>Success</h1>");
   out.println ("<p>MarinerServletApplication instances retrieved from API"
                + " and jsp:useBean are the same: " + marinerApplication);
 } else {
   out.println ("<h1 style='background: red'>Error</h1>");
   out.println ("<p>MarinerServletApplication instance retrieved from API: "
                + servletApplication
                + " is not the same as the one retrieved from jsp:useBean: "
                + marinerApplication);
 }

 // Make sure that the Volantis bean inside the MarinerServletApplication is
 // the same as the one referred to by the volantis variable.
 Volantis hidden = ApplicationInternals.getVolantisBean (marinerApplication);
 if (volantis == hidden) {
   out.println ("<h1>Success</h1>");
   out.println ("<p>Volantis instances retrieved from API"
                + " and jsp:useBean are the same: " + volantis);
 } else {
   out.println ("<h1 style='background: red'>Error</h1>");
   out.println ("<p>Volantis instance retrieved from API: " + hidden
                + " is not the same as the one retrieved from jsp:useBean: "
                + volantis);
 }
 %>
