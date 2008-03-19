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
	MJ	15/11/2001	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>Horizontal Rule Test</vt:h1>
  <vt:p>Filename: tt_hr1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:hr element works.</vt:p>
  <vt:pre>
     Elements    Attributes
     hr
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with two horizontal rules. 
	One inserted between paragraphs, and another inserted mid-paragraph.
  </vt:p>
  <vt:p>We will need to update this page when noshade, color, align, size 
	and width are implemented.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test vt:hr outside of vt:p tags. About to test vt:hr outside of vt:p tags. About to test vt:hr outside of vt:p tags. About to test vt:hr outside of vt:p tags. Should be a line below this text. </vt:p>
  <vt:hr/>
  <vt:p>About to test vt:hr inside vt:p tags. About to test vt:hr inside vt:p tags. About to test vt:hr inside vt:p tags. About to test vt:hr inside vt:p tags. About to test vt:hr inside vt:p tags. Should be a line below this text.<vt:hr/>This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. </vt:p>
</vt:pane>
</vt:canvas>
