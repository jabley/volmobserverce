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
 % $Header: /src/voyager/webapp/internal/webtest/PhoneNumber.jsp,v 1.1 2003/04/10 12:53:24 philws Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2003. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 09-Apr-03    Phil W-S        VBM:2002111502 - Created.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:p>Please call Volantis on
            <vt:phonenumber fullNumber="+44483739700"/>
        </vt:p>
        <vt:p>Alternatively you can call
            <vt:phonenumber fullNumber="+44483739739">
                +44 (1) 483 739 739
            </vt:phonenumber>
        </vt:p>
        <vt:p>Perhaps you would like to try contacting
            <vt:phonenumber fullNumber="{PhoneNumbers/DuffinPaul}">
                Paul Duffin
            </vt:phonenumber>, just don&apos;t hold your breath.
        </vt:p>
        <vt:p>Finally, you could call
            <vt:phonenumber fullNumber="{PhoneNumbers/GhostBusters}">
                Ghost Busters
            </vt:phonenumber>
        </vt:p>
    </vt:pane>
</vt:canvas>
