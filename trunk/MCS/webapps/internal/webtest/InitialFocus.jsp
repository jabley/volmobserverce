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
 % $Header: /src/voyager/webapp/internal/webtest/2001100805.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 06-Apr-03    Byron           VBM:2003042208 - Created this jsp page that
 %                              should test the intialFocus.
 % ======================================================================= --%>
<%@ include file="VolantisNoError-mcs.jsp" %>
<%@page import="com.volantis.mcs.jsp.*,java.util.*,java.text.SimpleDateFormat"%>

<%
    int numberOfMonths = 5;
    int monthOffset = 0;
    if (request.getParameter("monthOffset")!=null) {
        monthOffset = Integer.parseInt(request.getParameter("monthOffset"));
    }
    GregorianCalendar depDateDay = new GregorianCalendar();
    depDateDay.setTime(new Date());
    int thisDay = depDateDay.get(Calendar.DAY_OF_MONTH);
    if (monthOffset>0) {
        depDateDay.add(Calendar.MONTH,monthOffset);
        thisDay=1;
    }
    depDateDay.set(Calendar.DAY_OF_MONTH,1);
    int thisdepDateMonth = depDateDay.get(Calendar.MONTH);
    StringBuffer cardTitleSb = new StringBuffer();
    cardTitleSb.append("from_date_day");

    SimpleDateFormat dd = new SimpleDateFormat("dd");
    SimpleDateFormat shSDF = new SimpleDateFormat("MM/yyyy");

    SimpleDateFormat flSDF = new SimpleDateFormat("MMMMMMMMM yyyy");
    String focused = String.valueOf(thisDay);

%>

<!-- type="inclusion" -->
<vt:canvas layoutName="singlePane" theme="lastminute"
    id="<%=cardTitleSb.toString()%>" title="Going Out search"  initialFocus="<%=focused%>" overlay="true" onenter="
</onevent>
<do type='prev'>

	<setattr name='bluepopup.visible' value='false' />
	<setattr name='searchinstruction.visible' value='false' />
	<go href='$main_searchbackup' target='main'/>
</do><onevent type='onenter'>">



  <vt:pane name="singlePane">

  <vt:table styleClass="calendarHeading" cols="3">
   <vt:tr styleClass="calendarChoose">
  <vt:td styleClass="calendarChoose"></vt:td>
  <vt:td styleClass="calendarChoose">Choose From Date</vt:td>
  <vt:td styleClass="calendarChoose"></vt:td>
  </vt:tr>
       <vt:tr styleClass="calendarHeading" >

      	<vt:td styleClass="monthArrows"> </vt:td>


	<vt:td styleClass="calendarMonth">
	<vt:span styleClass="white"><%=flSDF.format(depDateDay.getTime())%></vt:span>
	</vt:td>

	<vt:td styleClass="monthArrows"></vt:td>

	</vt:tr>
  </vt:table>

  <vt:table cols="9" styleClass="calendar">
  <vt:tr>

  <vt:td styleClass="calendarDays"><vt:span styleClass="white">s</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">m</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">t</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">w</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">t</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">f</vt:span></vt:td>
  <vt:td styleClass="calendarDays"><vt:span styleClass="white">s</vt:span></vt:td>
  </vt:tr>
 <%
    boolean focusDone = false;
    while (depDateDay.get(Calendar.MONTH)==thisdepDateMonth) {
 %>

  <vt:tr>
 <%
         for (int i=1;i<=7;i++) {
 %>

       <%String index = String.valueOf(depDateDay.get(Calendar.DAY_OF_MONTH));  %>
       <vt:td styleClass="calendar" id="<%=index%>">
        <vt:a href="" id="<%=index%>"
                      tabindex="<%=index%>"><%=index%></vt:a>
        <vt:span styleClass="darkbluedarkpinkfocused"><%=index%></vt:span>


      </vt:td>
<%
          depDateDay.add(Calendar.DAY_OF_MONTH,1);
       }
%>
    </vt:tr>
<%
    }
%>
  </vt:table>
  </vt:pane>
</vt:canvas>

