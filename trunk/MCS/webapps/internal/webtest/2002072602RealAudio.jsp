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
<%-- ========================================================================== % $Header: /src/voyager/webapp/internal/webtest/2002072602RealAudio.jsp,v 1.2 2002/07/26 15:14:24 aboyd Exp $
 % ===========================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ===========================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===========================================
 % 26-Jul-02    Allan           VBM:2002072602 - Created
 % ====================================================================== --%> 
<%-- ==========================================================================
 % This page tests the real audio element without altText i.e. if there is
 % a fallback text component it should pick that up assuming an appropriate
 % asset cannot be found - that is the real test.
 % ====================================================================== --%>
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="IndexLayout" >  

<vt:realaudio pane="index" name="stonecutters" autoStart="true" id="someid"
styleClass="someclass" title="sometitle" height="200" width="300" loop="yes" numLoop="2" shuffle="false" startJVM="false"/>

<vt:p pane="index">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Multimedia.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>

