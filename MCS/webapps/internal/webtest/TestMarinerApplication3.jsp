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
 % $Header: /src/voyager/webapp/internal/webtest/TestMarinerApplication3.jsp,v 1.2 2002/11/22 23:48:17 pduffin Exp $
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
 % This page tests that if the Volantis bean has been created then the
 % MarinerServletApplication.getInstance method always creates the
 % MarinerServletApplication even if it has not been asked to create it. This
 % is necessary for backwards compatability.
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

<%
 // Try and retrieve the mariner application if it exists but ask not to
 // create it if it does not exist.
 MarinerServletApplication servletApplication
   = MarinerServletApplication.getInstance (application, false);

 if (servletApplication == null) {
   out.println ("<h1 style='background: red'>Error</h1>");
   out.println ("<p>MarinerServletApplication instance not created for"
                + " backwards compatability");
 } else {
   out.println ("<h1>Success</h1>");
   out.println ("<p>MarinerServletApplication instance created for backwards"
                + " compatability");

   // Make sure that the Volantis bean inside the MarinerServletApplication is
   // the same as the one referred to by the volantis variable.
   Volantis hidden = ApplicationInternals.getVolantisBean (servletApplication);
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
 }
 %>
