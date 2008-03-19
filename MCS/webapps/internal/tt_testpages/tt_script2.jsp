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
	MJ	29/04/2002	Created this page
      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>Script Element Test 2</vt:h1>
  <vt:p>Filename: tt_script2.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:script element work.</vt:p>
  <vt:pre>
     Elements   Attributes
     script	src, language, type, charSet, defer
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with a script calling tt_helloworld.js 
	script file. </vt:p>
  <vt:p><vt:b>CHECK THE SOURCE of this page to check the 
	script attributes have been generated correctly.</vt:b></vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_scripts.jsp">Scripts Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test script element. Check the attributes in the source.</vt:p>
  <vt:script src="/volantis/scripts/tt_helloworldscript.js" language="javascript" type="text/javascript" charSet="ISO-8859-1" defer="true"/>
</vt:pane>

</vt:canvas>
