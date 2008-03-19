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
  <vt:h1>Script Element Test</vt:h1>
  <vt:p>Filename: tt_script1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure the basic properties of the vt:script element work.</vt:p>
  <vt:pre>
     Elements   Attributes
     script	src
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with a script calling Hello World script component. Since the script doesn't have a pane attribute, it will be tested inside and outside block elements, and outside of pane elements. </vt:p>
  <vt:p><vt:b>You may need to view the source of this page to check the 
	script attributes have been generated correctly.</vt:b></vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_scripts.jsp">Scripts Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test element outside of vt:p tags. About to test element outside of vt:p tags. About to test element outside of vt:p tags. About to test element outside of vt:p tags. Should be element text below this text. </vt:p>
  <vt:script src="{HelloWorld}"/>
  <vt:p>About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. Should be element text after this text. Nested script in a span to prove it works in inside inline elements.
  <vt:span><vt:script src="{HelloWorld}"/></vt:span>
 This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. </vt:p>
  <vt:p>About to test element outside of pane block element. The script should appear at the top of the page, directly below the BODY element.</vt:p>
</vt:pane>

<vt:script src="{HelloWorld}"/>

</vt:canvas>
