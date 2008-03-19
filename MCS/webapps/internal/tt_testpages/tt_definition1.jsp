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
  <vt:h1>Definition Test</vt:h1>
  <vt:p>Filename: tt_definition1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:dd, vt:dt and vt:dd 
	elements work. 
	The elements are rendered differently dependent on browser but normally
	dt adds a line of whitespace and puts dt text on a new line,
	dd text is indented, and dl is just used as an internal wrapper 
	around all the dt and dl elements. <vt:br/>
	Styles should override it.
  </vt:p>
  <vt:pre>
     Elements    Attributes
     dl
     dt
     dd
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with some definition text.<vt:br/> 
	1 - Inserted between paragraphs<vt:br/>
	2 - Inserted mid-paragraph<vt:br/>
  </vt:p>
  <vt:p>Each Term is normally on a new line, and each Definition
	is in an intented paragraph. Styles may override this, but
	on NN4, only styles applied to DT work.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test element outside of vt:p element. About to test element outside of vt:p element. About to test element outside of vt:p element. About to test element outside of vt:p element. Definition under this text.</vt:p>
<vt:dl>
<!--It is invalid to put text directly inside the dl element. Must be nested in
    side dt or dd element. Closing dt and dd element are required by Mariner. -->
<vt:dt>Term</vt:dt>
<vt:dd>Definition</vt:dd> 
<vt:dt>Cluster</vt:dt>
<vt:dd>WebLogic Cluster consists of two Servers deployed on the network, coordinated with a combination of Domain Name Service (DNS), JNDI naming tree replication, session data replication, and WebLogic RMI. Web servers (plug-in configured in web servers) connect with a WebLogic Cluster using a virtual cluster address defined on the network and listener port (7001 as default).</vt:dd>
</vt:dl> 

<vt:p>About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. About to test element inside vt:p element. Definition after this text. <vt:dl><vt:dt>Term</vt:dt><vt:dd>Definition</vt:dd><vt:dt>Cluster</vt:dt><vt:dd>WebLogic Cluster consists of two Servers deployed on the network, coordinated with a combination of Domain Name Service (DNS), JNDI naming tree replication, session data replication, and WebLogic RMI. Web servers (plug-in configured in web servers) connect with a WebLogic Cluster using a virtual cluster address defined on the network and listener port (7001 as default).</vt:dd></vt:dl>This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. </vt:p>

</vt:pane>



</vt:canvas>
