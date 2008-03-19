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
<jsp:useBean id="urlRewriter"
             class="com.volantis.mcs.samples.urlrewriter.SimpleSamplePageURLRewriter"
             scope="application">
</jsp:useBean>

<%-- The initialize code is outside of the jsp:useBean because the current
     implementations of jsp:useBean do not ensure that the bean creation
     and initialization is an atomic operation --%>

<%urlRewriter.initialize(application);%>

<%
        String redirectURL = urlRewriter.translateURL(request.getQueryString());
        response.sendRedirect(redirectURL);
%>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Jul-04	4801/1	allan	VBM:2004070113 Sample PageURLRewriter java and jsp.

 ===========================================================================
--%>
