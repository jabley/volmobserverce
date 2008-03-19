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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/AnchorTest1.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 15-Oct-01    Paul            VBM:2001101207 - Created.
 % 19-Dec-01    Paul            VBM:2001120506 - Removed import of pagehelpers.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="AnchorTest" theme="AnchorTest" pageTitle="AnchorTest1">

<%-- Test emphasis tags outside an anchor --%>
<vt:p pane="Rows">
  <vt:h1>Heading 1</vt:h1>
</vt:p>

<vt:p pane="Rows">
  <vt:h2>Heading 2</vt:h2>
</vt:p>

<vt:p pane="Rows">
  <vt:h3>Heading 3</vt:h3>
</vt:p>

<vt:p pane="Rows">
  <vt:h4>Heading 4</vt:h4>
</vt:p>

<vt:p pane="Rows">
  <vt:h5>Heading 5</vt:h5>
</vt:p>

<vt:p pane="Rows">
  <vt:h6>Heading 6</vt:h6>
</vt:p>

<vt:p pane="Rows">
  <vt:blockquote>Block quote</vt:blockquote>
</vt:p>

<vt:p pane="Rows">
  <vt:cite>Cite</vt:cite>
</vt:p>

<vt:p pane="Rows">
  <vt:pre>Pre</vt:pre>
</vt:p>

<vt:p pane="Rows">
  <vt:address>Address</vt:address>
</vt:p>

<vt:p pane="Rows">
  <vt:small>Small</vt:small>
</vt:p>

<vt:p pane="Rows">
  <vt:big>Big</vt:big>
</vt:p>

<vt:p pane="Rows">
  <vt:u>U</vt:u>
</vt:p>

<vt:p pane="Rows">
  <vt:i>I</vt:i>
</vt:p>

<vt:p pane="Rows">
  <vt:b>B</vt:b>
</vt:p>

<vt:p pane="Rows">
  <vt:em>Em</vt:em>
</vt:p>

<vt:p pane="Rows">
  <vt:strong>Strong</vt:strong>
</vt:p>

<vt:p pane="Rows">
  <vt:sup>Sup</vt:sup>
</vt:p>

<vt:p pane="Rows">
  <vt:sub>Sub</vt:sub>
</vt:p>

<vt:p pane="Rows">
  <vt:code>Code</vt:code>
</vt:p>

<vt:p pane="Rows">
  <vt:cite>Cite</vt:cite>
</vt:p>

<vt:p pane="Rows">
  <vt:kbd>Kbd</vt:kbd>
</vt:p>

<vt:p pane="Rows">
  <vt:samp>Samp</vt:samp>
</vt:p>

<%-- Test emphasis tags inside an anchor --%>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h1>Heading 1</vt:h1></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h2>Heading 2</vt:h2></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h3>Heading 3</vt:h3></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h4>Heading 4</vt:h4></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h5>Heading 5</vt:h5></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:h6>Heading 6</vt:h6></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:blockquote>Block quote</vt:blockquote></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:cite>Cite</vt:cite></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:pre>Pre</vt:pre></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:address>Address</vt:address></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:small>Small</vt:small></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:big>Big</vt:big></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:u>U</vt:u></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:i>I</vt:i></vt:a>
</vt:p>

<vt:p pane="Rows">
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:em>Em</vt:em></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:strong>Strong</vt:strong></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:sup>Sup</vt:sup></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:sub>Sub</vt:sub></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:code>Code</vt:code></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:cite>Cite</vt:cite></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:kbd>Kbd</vt:kbd></vt:a>
</vt:p>

<vt:p pane="Rows">
  <vt:a href="nowhere"><vt:samp>Samp</vt:samp></vt:a>
</vt:p>

</vt:canvas>
