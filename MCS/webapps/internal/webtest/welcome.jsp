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

<vt:canvas layoutName="welcome" theme="welcome" pageTitle="Welcome to Mariner">

<vt:pane name="logo" >
<vt:img src="vol_logo" altText="Volantis Systems Ltd" />
</vt:pane>


<vt:pane name="background" >
<vt:p styleClass="background">Volantis takes its name from a circumpolar star, visible anytime, anywhere. That's what we do: enable access to your e-business applications anytime, anywhere, from any device.</vt:p>
</vt:pane>

<vt:pane name="congratulations" >
<vt:h2>Congratulations</vt:h2>
<vt:p>You have successfully installed and configured Mariner.</vt:p>
</vt:pane>


<vt:pane name="competition">
<vt:h2>Win a Compaq iPAQ !</vt:h2>
<vt:p>Submit a screenshot of your best Mariner-based site, and we'll ship you a Volantis polo shirt.</vt:p>
<vt:p>If we use it on the Volantis web-site we'll send you a new Compaq iPAQ!</vt:p>
<vt:p>E Mail: <vt:a href="mailto:poloshirt@volantis.com">poloshirt@volantis.com</vt:a></vt:p>

<vt:p>Please include:</vt:p>
<vt:p>
<vt:ol title="Please include">
	<vt:li>Your name, address, E Mail and Phone Number</vt:li>
	<vt:li>Shirt Size (S,M,L and XL)</vt:li>
	<vt:li>Application server</vt:li>
	<vt:li>Devices you support with Volantis</vt:li>
	<vt:li>Brief description of your site</vt:li>
</vt:ol>
</vt:p>
</vt:pane>

<vt:pane name="smallprint">
<vt:p styleClass="supersmall">
Submitted entries may be used in marketing or promotions for Volantis Systems. By offering images and text, you are consenting to such use of names, likenesses, and general locations, for advertising and promotional purposes without additional compensation.
</vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
	<vt:a href="http://www.volantis.com">www.volantis.com</vt:a></vt:p>
<vt:p>
	<vt:a href="mailto:support@volantis.com">support@volantis.com</vt:a></vt:p>
<vt:p>
	<vt:a href="mailto:moreinfo:volantis.com">moreinfo@volantis.com</vt:a>
</vt:p>
</vt:pane>

<vt:pane name="blank">
<vt:p></vt:p>
</vt:pane>


<vt:pane name="copyright">
<vt:p styleClass="supersmall">Copyright (c) 2002 Volantis Systems Ltd.  </vt:p>
<vt:p styleClass="supersmall">Mariner <vt:sup styleClass="supersmall">tm</vt:sup>  and Volantis <vt:sup styleClass="supersmall">tm</vt:sup> are trademarks of Volantis Systems Ltd.</vt:p>
</vt:pane>

<vt:pane name ="agility">
<vt:img src="agility-by-volantis" altText="Agility by Volantis" />
</vt:pane>

</vt:canvas>
