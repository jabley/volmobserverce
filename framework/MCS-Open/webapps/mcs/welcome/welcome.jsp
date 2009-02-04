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
<%@ include file="/Volantis-mcs.jsp" %>

<mcs:page>

<canvas layoutName="/welcome.mlyt"
    theme="/welcome.mthm"
    pageTitle="Welcome to the Volantis Mobile Content Framework">

    <pane name="logo">
        <img src="/welcome/vol_logo.mimg" alt="Volantis Systems Ltd. Logo"/>
    </pane>

    <pane name="background" >
      <h3>Volantis Mobile Content Framework</h3>
      <p>
        The Volantis Mobile Content Framework provides a comprehensive, standards-based,
        development and execution environment for delivering multi-channel content, applications and
        services that are automatically optimized for thousands of different consumer devices.
      </p>
      <p>
        Built around the principle of device-independent delivery, Volantis Mobility Server 
        uniquely separates content, design, and device-issues into abstract device-independent 
        policies. This enables a "create once, run anywhere" environment which reduces complexity, 
        cost and time to market for both development and maintenance.
      </p>
    </pane>

    <pane name="congratulations" >
        <h3>Congratulations!</h3>
        <p>
          You have successfully installed and configured The Volantis Mobile Content Framework<sup>TM</sup>.
        </p>
    </pane>

   <pane name="links">
        <h3>Useful Links:</h3>
        <dl>
            <dt>The Volantis home page</dt>
            <dd>
                <a href="http://www.volantis.com">www.volantis.com</a>
            </dd>
            <dt>Our support desk</dt>
            <dd>
                <a href="mailto:support@volantis.com">support@volantis.com</a>
            </dd>
            <dt>Our information desk</dt>
            <dd>
                <a href="mailto:moreinfo@volantis.com">moreinfo@volantis.com</a>
            </dd>
        </dl>
    </pane>

    <pane name="copyright">
        <p class="copyr">Copyright (c) 2000-2008 Volantis Systems Ltd.  </p>
        <p class="copyr">
          Volantis <sup>TM</sup> is a trademark of Volantis Systems Ltd.
        </p>
    </pane>
</canvas>

</mcs:page>
