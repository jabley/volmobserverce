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
	MJ	22/04/2002	Created this page
      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>Big Element Test</vt:h1>
  <vt:p>Filename: tt_big1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:b element work.
	Nested big elements can increase font sizes 
	(as defined on each browser) up to 3 levels. 
	(Similar to h1 - h3.)</vt:p>
  <vt:pre>
     Elements    Attributes
     big
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with some big text.<vt:br/> 
	1 - Inserted between paragraphs<vt:br/>
	2 - Inserted mid-paragraph<vt:br/>
  </vt:p>
  <vt:p>Since only 3 levels are supported, the 4th level text should appear 
	the same as level 3 text.</vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test element outside of vt:p element. About to test element outside of vt:p element. About to test element outside of vt:p element. About to test element outside of vt:p element. Should be big text below this text. </vt:p>
  <vt:big>This is big text1.
  <vt:big>This is nested big text2.
  <vt:big>This is nested big text3.
  <vt:big>This is nested big text4.
  </vt:big>Back to text3.
  </vt:big>Back to text2.
  </vt:big>Back to text1.
  </vt:big>
  <vt:p>About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. Should be big text after this text. <vt:big>This is big text1. <vt:big>This is nested big text2. <vt:big>This is nested big text3. <vt:big>This is nested big text4. </vt:big>Back to text3. </vt:big>Back to text2. </vt:big>Back to text1. </vt:big> This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. </vt:p>

</vt:pane>



</vt:canvas>
