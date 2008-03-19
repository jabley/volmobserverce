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
<vt:canvas 	layoutName="tt_default" theme="tt_list">

<vt:pane name="fileinfo">
  <vt:h1>List Image Test</vt:h1>
  <vt:p>Filename: tt_list1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: tt_list
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure list images work.</vt:p>
  <vt:pre>
     ol
     li
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A list should be displayed with image bulletmarks.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_FormIndex.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">

<vt:ol title='Please include'>
  <vt:li styleClass='dot'>Your name, address, E Mail and Phone Number</vt:li>
  <vt:li styleClass='dot'>Shirt Size (S,M,L and XL)</vt:li>
  <vt:li styleClass='dot'>Application server</vt:li>
  <vt:li styleClass='dot'>Devices you support with Volantis</vt:li>
  <vt:li styleClass='dot'>Brief description of your site</vt:li>
</vt:ol> 

</vt:pane>
</vt:canvas>
