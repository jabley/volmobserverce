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
package com.volantis.mcs.servlet;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

/**
 * <p>
 * This class is responsible for providing a cookie jar that can be used to
 * store cookies received from a remote server hosting a remote MCS project.
 * <p>
 * This cookie jar conforms to the proposed cookie management scheme defined
 * in RFC 2109, Section 4.3.3, include here:
 * <p>
 * <b>Cookie Management</b>
 * <p>
 * If a user agent receives a Set-Cookie response header whose NAME is
 * the same as a pre-existing cookie, and whose Domain and Path
 * attribute values exactly (string) match those of a pre-existing
 * cookie, the new cookie supersedes the old.  However, if the Set-
 * Cookie has a value for Max-Age of zero, the (old and new) cookie is
 * discarded.  Otherwise cookies accumulate until they expire (resources
 * permitting), at which time they are discarded.
 */
public class CookieJar {

    /**
     * The collection of cookies maintained in this cookie jar.
     */
    private HTTPMessageEntities cookieJar = new SimpleHTTPMessageEntities();

    /**
     * Adds the given <code>cookie<code> to the cookie jar in accordance
     * with RFC 2109, Section 4.3.3
     *
     * @param cookie the cookie to be added.
     */
    public void addCookie(Cookie cookie) {

        // Do we already have a cookie stored with the same
        // name, path and domain values as the cookie we are trying
        // to add?
        Cookie storedCookieWithMatchingNamePathAndDomain =
                getCookieWithMatchingNamePathAndDomain(cookie);

        if (storedCookieWithMatchingNamePathAndDomain != null) {

            // Handle the requirement of RFC 2109, Section 4.3.3, i.e
            // If a user agent receives a Set-Cookie response header
            // whose NAME is the same as a pre-existing cookie, and whose
            // Domain and Path attribute values exactly (string) match those
            // of a pre-existing cookie, the new cookie supersedes the old.
            // However, if the Set-Cookie has a value for Max-Age of zero,
            // the (old and new) cookie is discarded.
            cookieJar.remove(
                     storedCookieWithMatchingNamePathAndDomain.getIdentity());
            addCookieImpl(cookie);
        } else {
            // we dont have any existing cookies with the same name so
            // simply add the supplied cookie to the jar if the max age
            // is greater than zero.
            addCookieImpl(cookie);
        }
    }

    /**
     * Provides an {@link Iterator} containing all of the cookies in the
     * cookie jar.
     *
     * @return an iteration of the cookies stored in this cookie jar.
     */
    public Iterator iterator() {
        return cookieJar.iterator();
    }

    /**
     * The number of cookie stored in this cookie jar.
     *
     * @return the number of cookies stored in this cookie jar.
     */
    public int size() {
        return cookieJar.size();
    }

    /**
     * Adds the given <code>cookie</code> to the cookie jar if the max age
     * is not equal to zero.
     *
     * @param cookie the cookie to be added.
     */
    private void addCookieImpl(Cookie cookie) {
         if (cookie.getMaxAge() != 0) {
                cookieJar.add(cookie);
         }
    }

    /**
     * Returns the cookie in this cookie jar that has the same values for:
     * name, path and domain as the given cookie, or null if no such
     * cookie exists.
     *
     * @param cookie the cookie whose name, path and domain values will be used
     * to match an existing cookie in this jar.
     *
     * @return the cookie in this cookie jar that has the same values for:
     * name, path and domain as the given cookie, or null if no such
     * cookie exists.
     */
    private Cookie getCookieWithMatchingNamePathAndDomain(Cookie cookie) {

        Cookie storedCookieWithMatchingNamePathAndDomain = null;

        // Obtain all of the cookies in the jar with the same name as the
        // given cookie.
        Collection cookiesWithSameName = getCookiesWithName(cookie.getName());
        Iterator cookiesWithSameNameIter = cookiesWithSameName.iterator();

        boolean foundCookieWithIdenticalNamePathAndDomain = false;
        // Search for a cookie that has the same name, path and domain values
        // as the given cookie.
        while (cookiesWithSameNameIter.hasNext() &&
                !foundCookieWithIdenticalNamePathAndDomain) {


            Cookie currentCookie = (Cookie)cookiesWithSameNameIter.next();

            //todo we have already tested for the name - only need to
            //check the domains.
            if (cookiesNamePathAndDomainsMatch(currentCookie, cookie)) {

                storedCookieWithMatchingNamePathAndDomain = currentCookie;

                foundCookieWithIdenticalNamePathAndDomain = true;
            }
        }
        return storedCookieWithMatchingNamePathAndDomain;
    }

    /**
     * Returns a {@link Collection} of cookies that have the given
     * <code>cookieName</code>
     *
     * @param name the name of the cookies we want to retrieve.
     *
     * @return a collection of cookies with the given name.
     */
    private Collection getCookiesWithName(String name) {

        Collection cookiesMatchingName = new ArrayList();
        Iterator cookiesInJar = iterator();
        while (cookiesInJar.hasNext()) {
            Cookie currentCookie = (Cookie)cookiesInJar.next();
            if (currentCookie.getName().equals(name)) {
                cookiesMatchingName.add(currentCookie);
            }
        }
        return cookiesMatchingName;
    }

    /**
     * Returns true if the given cookies have identical values for:
     * name, path and domain.
     *
     * @param cookie the first cookie.
     * @param other  the second cookie.
     *
     * @return true if the supplied cookies have identical values for:
     * name, path and domain; otherwise false.
     */
    private boolean cookiesNamePathAndDomainsMatch(Cookie cookie,
                                                   Cookie other) {

        boolean namePathAndDomainMatch = false;
        if (cookie.getName().equals(other.getName()) &&
            cookie.getPath().equals(other.getPath()) &&
            cookie.getDomain().equals(other.getDomain())) {

            namePathAndDomainMatch = true;
        }
        return namePathAndDomainMatch;
    }
}
