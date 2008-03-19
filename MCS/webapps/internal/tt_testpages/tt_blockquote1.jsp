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
  <vt:h1>Blockquote Test</vt:h1>
  <vt:p>Filename: tt_blockquote1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure all non-common attributes of the vt:blockquote element work.
	The element is rendered differently dependent on browser, but normally
	puts the text in a new indented paragrapgh. <vt:br/>
	The cite attribute references a url for the source of the quote.
	The cite is NOT displayed by the browser, but may be used by 
	search engines / users to identify the source documents.</vt:p>
  <vt:pre>
     Elements    Attributes
     blockquote  cite
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with some blockquote text.<vt:br/> 
	1 - Inserted between paragraphs<vt:br/>
	2 - Inserted mid-paragraph<vt:br/>
  </vt:p>
  <vt:h4>You must view the source of this page to ensure that the cite
	attribute has been output with the blockquote element.
  </vt:h4>

</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_tags.jsp">Elements Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>About to test element outside of vt:p tags. About to test element outside of vt:p tags. About to test element outside of vt:p tags. About to test element outside of vt:p tags. Should be blockquote from Radio Times review of Scorpion King below this text. </vt:p>
<vt:blockquote cite="radiotimesreview_scorpionking.htm">
 Calling to mind Italian sword-and-sandal flicks starring sturdy Steve Reeves (not surprising, since the co-producers are the World Wrestling Federation), director Chuck (The Mask) Russell's spin-off from The Mummy Returns is entertaining, escapist hokum. The plot is hardly original: an evil warlord (Steven Brand), who has conquered and enslaved half the tribes in the desert, is opposed by a lone warrior (wrestler Dwayne "The Rock" Johnson, in an imposing, star-making performance), who unites the remaining warring factions and leads them against the common oppressor. There might not be much sense or depth amid the noisy, but not terribly gory, violence, yet the spirited non-stop action, vigorously directed by Russell, makes for an unpretentious, enjoyable adventure. This effective macho epic is a lot more fun to watch than a mere cash-in title, made solely for the wrestling brigade.
</vt:blockquote>

<vt:p>About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. About to test element inside vt:p tags. Should be Radio Times review of Scorpion King text after this text. <vt:blockquote cite="radiotimesreview_scorpionking.htm">Calling to mind Italian sword-and-sandal flicks starring sturdy Steve Reeves (not surprising, since the co-producers are the World Wrestling Federation), director Chuck (The Mask) Russell's spin-off from The Mummy Returns is entertaining, escapist hokum. The plot is hardly original: an evil warlord (Steven Brand), who has conquered and enslaved half the tribes in the desert, is opposed by a lone warrior (wrestler Dwayne "The Rock" Johnson, in an imposing, star-making performance), who unites the remaining warring factions and leads them against the common oppressor. There might not be much sense or depth amid the noisy, but not terribly gory, violence, yet the spirited non-stop action, vigorously directed by Russell, makes for an unpretentious, enjoyable adventure. This effective macho epic is a lot more fun to watch than a mere cash-in title, made solely for the wrestling brigade. </vt:blockquote>This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. This is the rest of the paragragh. </vt:p>

</vt:pane>



</vt:canvas>
