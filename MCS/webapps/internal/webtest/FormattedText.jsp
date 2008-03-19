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
<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="FormattedText" layoutName="FormattedText" title="The title" pageTitle="The Page Title" styleClass="someclass" id="someid" >

<vt:p pane="pane">
Below there is a list of all tags that could be used for formatting text:<vt:br/>
<vt:br title="sometitle" id="someid" styleClass="someclass"/>
</vt:p>

<vt:address pane="pane" styleClass="address" id="f1" title="Address title">
This should be rendered as an address
</vt:address>


<vt:p pane="pane">

<vt:b  styleClass="bold" id="f2" title="Bold title">
This should be rendered as bold
</vt:b>

<vt:big styleClass="big" id="f3" title="Big title">
This should be big
</vt:big>

<vt:em styleClass="emphasize" id="f4" title="Emphasized title">
This should be emphasized
</vt:em>

<vt:i styleClass="italic" id="f5" title="Italic title">
This should be italic
</vt:i>

<vt:kbd styleClass="keyboard" id="f6" title="Keyboard title">
This should be rendered as keyboard text
</vt:kbd>

<vt:samp styleClass="sample" id="f7" title="Sample title">
This should be rendered as sample text
</vt:samp>

<vt:small styleClass="small" id="f8" title="Small title">
This should be rendered small
</vt:small>

<vt:strong styleClass="strong" id="f9" title="Strong title">
This should be rendered as strong text
</vt:strong>

<vt:sup styleClass="superscript" id="f10" title="Superscript title">
This should be rendered as superscript
</vt:sup>

<vt:sub styleClass="subscript" id="f11" title="Subscript title">
This should be rendered as subscript
</vt:sub>

<vt:u styleClass="underlined" id="f11" title="Underlined title">
This should be underlined
</vt:u>

This is some simple text but <vt:cite styleClass="cite" id="f12" title="Cite title"> this is inside a cite tag,</vt:cite> so, there
should be a difference betwwen them.

<vt:code styleClass="code" id="f13" title="Code title">
This appears inside a "code" tag
</vt:code>

Below there is a "blockquote" tag with all attributes set (except the pane attribute).
<vt:blockquote styleClass="blockquote" id="f14" title="Blockquote title" cite="http://www.yahoo.com">This is the text inside
the block quotation, and should have a special rendering by the browser.</vt:blockquote>
This is ordinary text just after the block quotation.
<vt:tt styleClass="teletype" title="TT tile" id="f15">This should appear as teletype text</vt:tt>


</vt:p>

<vt:pre styleClass="preformatted" id="f16" title="Preformatted title" pane="pane">This text should appear as preformatted, so the 
broeswer should not eg suppress empty new lines



like the ones above.</vt:pre>

<vt:h1 pane="pane" id="f17" styleClass="heading1" title="Heading1 title">This is a heading 1 text</vt:h1>
<vt:h2 pane="pane" id="f18" styleClass="heading2" title="Heading2 title">This is a heading 2 text</vt:h2>
<vt:h3 pane="pane" id="f19" styleClass="heading3" title="Heading3 title">This is a heading 3 text</vt:h3>
<vt:h4 pane="pane" id="f20" styleClass="heading4" title="Heading4 title">This is a heading 4 text</vt:h4>
<vt:h5 pane="pane" id="f21" styleClass="heading5" title="Heading5 title">This is a heading 5 text</vt:h5>
<vt:h6 pane="pane" id="f22" styleClass="heading6" title="Heading6 title">This is a heading 6 text</vt:h6>

<vt:p pane="pane">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>


</vt:canvas>

