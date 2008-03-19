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
<vt:canvas layoutName="example">
<vt:logo pane="logo1" src="stars" />
<vt:logo pane="logo2" src="volantis" altText="volantis"/>
<vt:pane name="cpynews">
<vt:h2>Floods in Britain set to worsen.</vt:h2>
<vt:p>
Weather forecasters are predicting further strong winds and heavy rain this weekend, adding to the misery for the thousands of people whose homes have been flooded during the last two weeks. With rivers at record levels, the worst floods for over 50 years have forced large numbers of people to seek shelter elsewhere.
</vt:p>
<vt:p>
Travel has also been badly hit. Coming on top of the rail chaos caused by remedial engineering work in the wake of the Hatfield tragedy and the Bristol derailment, the flooding has caused additional line closures, badly affecting commuters and long-distance travellers. On the roads, the M25 and M3 have been badly affected in the south, as has the M1 in the further north.
</vt:p>
</vt:pane>
<vt:pane name="headlines">
<vt:ul>
<vt:li><vt:a href="elvis.jsp">Nun sees Elvis on moon</vt:a>/<vt:li> 
<vt:li><vt:a href="man.jsp"><vt:p pane="headlines">Man bites dog</vt:a></vt:li>
<vt:li><vt:a href="vol.jsp"><vt:p pane="headlines">Volantis shares soar on IPO</vt:a></vt:li>
<vt:li><vt:a href="elvis.jsp"><vt:h2 pane="cpynews">Hamster abducted by aliens</vt:a></vt:li>
</vt:pane>
</vt:canvas>
