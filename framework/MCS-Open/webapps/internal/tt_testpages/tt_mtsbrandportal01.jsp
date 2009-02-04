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
<!--  *************************************************************************
	(c) Volantis Systems Ltd 2001. 
      *************************************************************************
	Revision Info 
	Name  	Date  		Comment
	MJ	26/11/2001	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.context.MarinerPageContext" %>

<%
  String portalbrand = "";
  if (request.getParameter ("portalbrand") != null) {
  portalbrand = request.getParameter ("portalbrand");
  } else {
  portalbrand = "";
  }

%>

<vt:canvas layoutName="^tt_portalxfform" type="portal" brand="<%=portalbrand%>" >

<vt:pane name="fileinfo">
  <vt:h1>MTS Brand Portal Test</vt:h1>
  <vt:p>Filename: tt_brandportal01.jsp<vt:br/>
  	Layout: tt_portalxfform<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>Test that all portlets get branding, INCL MTS IMAGES.</vt:p>
  <vt:h3>Test Devices</vt:h3>
  <vt:p>All except VoiceXML.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>This portal contains two portlets.
        The portal and portlet 1 can be assigned brands.
        Portlet 2 is fixed and has no brand attribute. This is to test that
        portlet 2 will inherit the portal brand.
  </vt:p>
  <vt:p>When first displayed portal and portlets must use generic elements.
  <vt:br/>When the portal has a brand but portlet 1 has empty string,
        the portal and portlet 2 must use brand elements
        but portlet 1 must use generic elements.
  <vt:br/>When portlet 1 has a brand, portlet 1 must brand elements.
  </vt:p>
  <vt:p>Ensure the correct elements are shown when brands are applied.</vt:p>
  <vt:p>Test 1. image component - brand logo must be displayed.
  <vt:br/>Test 2. script component - brand text must be displayed.
  <vt:br/>Test 3. link component - hover over link text to ensure links to brand url.
  </vt:p>
  <vt:p>
  <%
  String pattern = new String("The portal canvas brand=\"{0}\"   ");
  Object [] args = new Object [] {portalbrand};
  out.print (java.text.MessageFormat.format (pattern, args));
  %>
  <vt:br/>
  Portal Result 1:
  <vt:img src="tt_mtsbrandlogo" altText="The brand logo should be here."/>
  <vt:br/>
  Portal Result 2: <vt:script src="{tt_brandscript}"/>
  <vt:br/>
  Portal Result 3: <vt:a href="{tt_brandlink}">Hover over this link to check the  url in the browser status bar.</vt:a>
  </vt:p>
</vt:pane>

<vt:xfform      name="orderForm"
                action="tt_mtsbrandportal01.jsp">

<vt:xfsiselect  name="portalbrand"
                styleClass="menuformat"
                caption="Portal Brand"
                prompt="Choose a brand"
                captionPane="formCaption1"
                entryPane="formField1">
        <vt:xfoption caption="No Brand" value=""/>
        <vt:xfoption caption="Blue" value="tt_bluebrand"/>
        <vt:xfoption caption="Green" value="tt_greenbrand"/>
        <vt:xfoption caption="Red" value="tt_redbrand"/>
</vt:xfsiselect>

<vt:xfsiselect  name="portlet1brand"
                styleClass="menuformat"
                caption="Portlet 1 Brand"
                prompt="Choose a brand"
                captionPane="formCaption2"
                entryPane="formField2">
        <vt:xfoption caption="No Brand" value=""/>
        <vt:xfoption caption="Blue" value="tt_bluebrand"/>
        <vt:xfoption caption="Green" value="tt_greenbrand"/>
        <vt:xfoption caption="Red" value="tt_redbrand"/>
</vt:xfsiselect>

<vt:xfaction    type="submit"
                caption="Submit"
                captionPane=""
                entryPane="formFooter"/>

<vt:xfaction    type="reset"
                caption="Reset"
                captionPane=""
                entryPane="formFooter"/>
            
</vt:xfform>

<vt:region name="Region2">
 <jsp:include page="tt_mtsbrandportlet01.jsp"/>
</vt:region>

<vt:region name="Region3">
 <jsp:include page="tt_mtsbrandportlet02.jsp"/>
</vt:region>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_portals.jsp">Portals Index</vt:a>
  </vt:p>
</vt:pane>

</vt:canvas>
