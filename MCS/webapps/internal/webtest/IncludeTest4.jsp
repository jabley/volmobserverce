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
 % $Header: /src/voyager/webapp/internal/webtest/IncludeTest4.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Oct-01    Paul            VBM:2001102608 - Created.
 % ======================================================================= --%>

<%@ include file="VolantisNoError-mcs.jsp" %>

<%-- ==========================================================================
 % This page tests what happens when we include a partial page which generates
 % an error. There seems to be a bug in Tomcat where an enclosing page
 % continues processing after an included page has thrown and then handled an
 % exception. If the page is included in the body of a custom tag then another
 % exception will occur if the tag's body content is used as it was closed by
 % the forward method.
 %
 % The error has nothing to do with our tags, replacing the canvas tag with
 % another custom tag will still cause the problem to occur.
 %
 % If this problem is fixed then you will see the standard volantis error
 % page, otherwise you will see the standard Tomcat error page with a stack
 % trace which looks something like this.
 % 
 % java.lang.NullPointerException
 %   at java.lang.System.arraycopy(Native Method)
 %   at java.lang.String.getChars(String.java:553)
 %   at org.apache.jasper.runtime.BodyContentImpl.write(Unknown Source)
 %   at org.apache.jasper.runtime.BodyContentImpl.write(Unknown Source)
 %   at org.apache.jsp.IncludeTest4$jsp._jspService(IncludeTest4$jsp.java:144)
 %
 % ======================================================================= --%>

<vt:canvas layoutName="IncludeTest" pageTitle="Include Test 4">

<jsp:include page="PartialPage4.jsp"/>

</vt:canvas>
