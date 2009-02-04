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
	MJ	08/09/2002	Created this page
      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>Meta Element Test</vt:h1>
  <vt:p>Filename: tt_meta1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all attribute value pairs of the vt:meta tag work.
</vt:p>
  <vt:pre>
     Tags        Attributes
     meta
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 


  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with some meta tags.<vt:br/> 
	VIEW PAGE SOURCE TO CHECK THEY WERE GENERATED CORRECTLY<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>Meta tags here. View source!
</vt:p>

<vt:meta name="keywords"  content="Volantis, Mariner, Pervasive Internet, Wireless Internet, Multi-channel" />
<vt:meta httpEquiv="expires"  content="01 Jan 05" /><vt:meta name="Authors"  content="Mike Ashfield" />
</vt:pane>



</vt:canvas>
