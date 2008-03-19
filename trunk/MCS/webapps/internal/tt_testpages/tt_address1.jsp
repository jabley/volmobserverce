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
	MJ	22/04/2002	Created this file

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default" theme="tt_address">

<vt:pane name="fileinfo">
  <vt:h1>Address Test</vt:h1>
  <vt:p>Filename: tt_address1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:address element work.
	The element is rendered differently dependent on browser but normally
	adds a blank line of whitespace and puts text in italics.
	The tag may be used in some search engines for author and 
	location information.</vt:p>
  <vt:pre>
     Elements    Attributes
     address
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with four addresses.<vt:br/> 
	1 - Inserted between paragraphs<vt:br/>
        2 - Inserted mid-paragraph<vt:br/>
        3 - Inserted between paragraphs (with style formatting)<vt:br/>
        4 - Inserted mid-paragragh (with style formatting)
  </vt:p>
  <vt:p>The altered format style should display non-italic, bold, blue, centered, smaller font text.</vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. Should be address below this text. </vt:p>
  <vt:address>Volantis Systems<vt:br/>
	      Occam Road<vt:br/>
	      Guildford GU2 7YT
  </vt:address>
  <vt:p>About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. Should be address below this text.<vt:address>Volantis Systems<vt:br/>Occam Road<vt:br/>Guildford GU2 7YT</vt:address>This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. 
  </vt:p>


  <vt:p>About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. About to test vt:address outside of vt:p element. Should be address below this text. </vt:p>
  <vt:address styleClass="alteredformat">Volantis Systems<vt:br/>
	      Occam Road<vt:br/>
	      Guildford GU2 7YT
  </vt:address>
  <vt:p>About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. About to test vt:address inside vt:p element. Should be address below this text.<vt:address styleClass="alteredformat">Volantis Systems<vt:br/>Occam Road<vt:br/>Guildford GU2 7YT</vt:address>This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. 
  </vt:p>

</vt:pane>

</vt:canvas>
