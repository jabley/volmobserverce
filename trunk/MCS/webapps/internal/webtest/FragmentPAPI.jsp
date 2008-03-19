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
 % $Header: /src/voyager/webapp/internal/webtest/FragmentPAPI.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ----------------------------------------------------------------------------
 % (c) Volantis Systems Ltd 2001. 
 % ----------------------------------------------------------------------------
 % Change History:
 %
 % Date         Who             Description
 % ---------    --------------- -----------------------------------------------
 % 19-Dec-01    Doug            VBM:2001121701 - Created
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %> 

  
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %> 
<%@ page import="com.volantis.mcs.papi.Fragment" %> 
<%@ page import="java.util.*" %> 

<vt:canvas layoutName="FragmentPAPI" cacheScope="all">

<%
    MarinerJspRequestContext jspRequestContext
    = MarinerJspRequestContext.getCurrent (pageContext);
     List l = jspRequestContext.getFragments();

    Fragment parent = jspRequestContext.getFragment("parent");
    if(null != parent) {
     parent.overrideLinkToText("JSP Customized to Parent");
     parent.overrideLinkFromText("JSP Customized from Parent");
    }

    Fragment frag1 = jspRequestContext.getFragment("Fragment1");
    if(null != frag1) {
     frag1.overrideLinkToText("JSP Customized to Fragment1");
     frag1.overrideLinkFromText("JSP Customized from Fragment1");
    }

%>    

<vt:p pane="Welcome">
<vt:br/>
<vt:h1>
Welcome to the PAPI Fragment test. 
</vt:h1> 
<vt:br/>
We are going to customize the fragment links
<vt:br/>
This layout contains the following 
<%
out.write(" " + l.size() + " ");
%>
fragments
<vt:br/>
<%
   
    Iterator i = l.iterator();
    while(i.hasNext()) {
    Fragment f = (Fragment)i.next();
%>
<vt:br/>
<vt:b>
<%
      out.write("" + f.getName());
%>
</vt:b>  
&nbsp;link to =
<vt:b>
<%
      out.write(" " + f.getLinkToText());
%>
</vt:b>  

&nbsp;link from =
<vt:b>
<%
      out.write(" " + f.getLinkFromText());
%>
</vt:b>  
<%
}
%>

<vt:br/>
</vt:p>

<vt:p pane="Pane">



<vt:br/>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
</vt:p>



<vt:p pane="Pane1">

<vt:br/>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
</vt:p>

<vt:p pane="Pane2">
<vt:br/>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b> plain <vt:b>bold</vt:b>
</vt:p>


</vt:canvas>
