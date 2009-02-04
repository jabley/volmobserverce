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
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 -->
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext,
                 com.volantis.mcs.marlin.sax.MarlinSAXHelper" %>

<%
  MarinerJspRequestContext marinerJspRequestContext =
      new MarinerJspRequestContext(pageContext);

  try {
      MarlinSAXHelper.parse(marinerJspRequestContext, null, systemId);
  } finally {
    marinerJspRequestContext.release();
  }
%>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 17-Jun-03	416/1	doug	VBM:2003061601 Intergrated XML pipeline with marlin

 ===========================================================================
--%>
