/*
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
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.cookies.Cookie;

/**
 * This is a HTTPMessageEntityMerger for WebRequestCookie objects.
 * Like SimpleHTTPMessageEntityMerger it merges name and value but it
 * also merges the other cookie properties as appropriate.
 */
public class SimpleCookieMerger implements HTTPMessageEntityMerger {

    public HTTPMessageEntity
            mergeHTTPMessageEntities(HTTPMessageEntity master,
                                     HTTPMessageEntity alternative)
            throws HTTPException {

        WebRequestCookie masterCookie = (WebRequestCookie) master;

        WebRequestCookie cookie = new WebRequestCookie();
        cookie.setName(masterCookie.getName());
        cookie.setDomain(masterCookie.getDomain());
        cookie.setPath(masterCookie.getPath());

        Cookie altCookie = (Cookie) alternative;

        if (masterCookie.getValue() != null) {
            cookie.setValue(masterCookie.getValue());
        } else {
            cookie.setValue(altCookie.getValue());
        }

        if (masterCookie.getComment() != null) {
            cookie.setComment(masterCookie.getComment());
        } else {
            cookie.setComment(altCookie.getComment());
        }

        if (masterCookie.secureHasBeenSet()) {
            cookie.setSecure(masterCookie.isSecure());
        } else {
            cookie.setSecure(altCookie.isSecure());
        }

        if (masterCookie.maxAgeHasBeenSet()) {
            cookie.setMaxAge(masterCookie.getMaxAge());
        } else {
            cookie.setMaxAge(altCookie.getMaxAge());
        }

        // Set the version to that of the master if the master has been set
        // otherwise set to the value of the alternate.
        if (masterCookie.versionHasBeenSet()) {
            cookie.setVersion(masterCookie.getVersion());
        } else {
            cookie.setVersion(altCookie.getVersion());
        }

        return cookie;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Mar-05	7443/1	matthew	VBM:2005031708 refactor AbstractPluggableHTTPManager

 ===========================================================================
*/
