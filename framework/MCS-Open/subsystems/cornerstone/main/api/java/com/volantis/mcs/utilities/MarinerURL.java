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
 * $Header: /src/voyager/com/volantis/mcs/utilities/MarinerURL.java,v 1.16 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Nov-01    Doug            VBM:2001112004 - Created. MarinerURL looks very
 *                              much like a java.set.URL. However. MarinerURL's
 *                              are mutable and also allow you to represent
 *                              relative URL's.
 * 28-Nov-01    Paul            VBM:2001112202 - Moved from papi package and
 *                              renamed from PAPIURL. Added equals and
 *                              hashCode methods. The functionality of the
 *                              toString method has been moved into the
 *                              getExternalForm method and the toString method
 *                              returns a string which identifies the object.
 * 29-Nov-01    Paul            VBM:2001112906 - Removed removeParameterValue
 *                              method as we need to try and keep this class
 *                              as simple and maintainable as possible as it
 *                              is part of the public integration API.
 * 30-Nov-01    Paul            VBM:2001112909 - Added getParameterMap method.
 * 06-Dec-01    Paul            VBM:2001113004 - Worked around limitation / bug
 *                              of JDK 1.2.2 by catching any exceptions thrown
 *                              by URLDecoder.decode and wrapping them in an
 *                              UndeclaredThrowableException.
 * 21-Dec-01    Paul            VBM:2001121702 - Added getParameterNames
 *                              method.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 24-Jan-02    Doug            VBM:2002011406 - Added the methods copy()
 *                              getType(), containsHostRelativePath(),
 *                              containsFullyQualifiedPath(),
 *                              containsDocumentRelativePath(),
 *                              isAbsolute(), isRelative() and two new
 *                              constructors.
 * 11-Feb-02    Paul            VBM:2001122105 - Renamed getType to getPathType
 *                              and made it throw an exception if it was called
 *                              on an absolute path. Also fixed a problem in
 *                              the resolution process when the base path did
 *                              not contain a /.
 * 13-Feb-02    Paul            VBM:2002021203 - Added support for read only
 *                              instances in order to protect urls which are
 *                              stored away from being modified.
 * 19-Feb-02    Paul            VBM:2001100102 - Made getExternalForm more
 *                              efficient by caching the result and modifying
 *                              the reconstruct... methods to take a
 *                              StringBuffer rather than use their own. Also
 *                              modified getParameterNames to return an
 *                              Enumeration not an Iterator.
 * 21-Feb-02    Allan           VBM:2002022007 - Set StringBuffer initial
 *                              capacity to 80 in getExternalForm() and
 *                              getRepositoryDir() to reduce the need to expand.
 * 04-Mar-02    Paul            VBM:2001101803 - Added constructor which
 *                              takes a base MarinerURL and a relative string
 *                              url.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 09-Apr-02    Adrian          VBM:2002022001 - modified constructor to use
 *                              fewer strings while resolving paths.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper and
 *                              UndeclaredThrowableException moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import com.volantis.synergetics.url.URLIntrospector;

import java.util.Map;
import java.util.Enumeration;

/**
 * A utility class which simplifies the task of manipulating URLs.
 * <p>
 * A URL consists of the following parts
 * <protocol>://authority>/<path>?<query>#<reference>
 * </p><p>
 * The authority can be further broken down into the following components
 * <user>:<password>@<host>:<port>
 * </p><p>
 * The file part of a URL consists of <path>?<query>
 * </p><p>
 * RFC 1808 was used to formulate the parsing of the url string into
 * the appropriate parts
 * </p>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class MarinerURL extends URLIntrospector
  implements Cloneable {


    /**
     * Creates a new empty MarinerURL instance.
     */
    public MarinerURL() {
        super();
    }

    /**
     * Creates a new MarinerURL instance.
     *
     * @param url String reprsentation of the URL we wish to create
     */
    public MarinerURL(String url) {
        super(url);
    }

   /**
     * Create a clone of another MarinerURL.
     */
    public MarinerURL(MarinerURL url) {
        super(url);
    }

    /**
     * Creates a new MarinerURL instance.
     *
     * @param url String representation of the URL we wish to create
     * @param queryString Map containing the query string name value pairs.
     * The key for the map entries is the parameter name and the value is
     * an array of strings representing the values.
     */
    public MarinerURL(String url, Map queryString) {
        super(url, queryString);
    }

    /**
     * Creates a new MarinerURL instance from a base URL
     * and a relative URL.
     *
     * @param base A base URL string
     * @param relative A relative URL string
     */
    public MarinerURL(String base, String relative) {
        super(base, relative);
    }

    /**
     * Creates a new MarinerURL instance from a base URL
     * and a relative URL.
     *
     * @param base A base URL
     * @param relative A relative URL string
     */
    public MarinerURL(MarinerURL base, String relative) {
        super(base, relative);
    }

    /**
     * Creates a new MarinerURL instance from a base URL
     * and a relative URL.
     *
     * @param base A base MarinerURL object
     * @param relative A relative MarinerURL object
     */
    public MarinerURL(MarinerURL base, MarinerURL relative) {
        super(base, relative);
    }


    // The following methods are overridden purely to ensure that the
    // javadoc is complete for this class. This is because the base class
    // is not part of the public api.

    /**
     * Prevent any one from modifying this again by making it read only.
     */
    public void makeReadOnly() {
        // Make sure that the external form is created before marking it as
        // read only because immutable URLs may be shared across threads and
        // the getExternalForm() method is not synchronized. This should really
        // be in the base class but there is not enough time to do that at the
        // moment as it is in another depot.
        // todo move this into the base class.
        super.getExternalForm();
        super.makeReadOnly();
    }

    /**
     * Return whether this url is read only or not.
     * @return True if this url cannot be modified and false otherwise.
     */
    public boolean isReadOnly() {
        return super.isReadOnly();
    }

    /**
     * Create a deep copy of this URLIntrospector object.
     * @return the copy
     */
    public Object clone() {
        return super.clone();
    }

    /**
     * Return the protocol part of this URL
     *
     * @return the protocol part
     */
    public String getProtocol() {
        return super.getProtocol();
    }

    /**
     * Set the protocol part of this URL
     *
     * @param protocol the protocol part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setProtocol(String protocol) {
        super.setProtocol(protocol);
    }

    /**
     * Return the authority part of this URL
     *
     * @return the authority part
     */
    public String getAuthority() {
        return super.getAuthority();
    }

    /**
     * Set the authority part of this URL
     *
     * @param authority the authority part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setAuthority(String authority) {
        super.setAuthority(authority);
    }

    /**
     * Return the host part of this URL
     *
     * @return the host part
     */
    public String getHost() {
        return super.getHost();
    }

    /**
     * Set the host part of this URL
     *
     * @param host the host part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setHost(String host) {
        super.setHost(host);
    }

    /**
     * Get the port part of this URL
     *
     * @return the port number or -1 if none is specified
     */
    public int getPort() {
        return super.getPort();
    }

    /**
     * Set the port number part of this URL
     *
     * @param port the port number
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setPort(int port) {
        super.setPort(port);
    }

    /**
     * Return the userInfo part of this URL
     *
     * @return the userInfo part
     */
    public String getUserInfo() {
        return super.getUserInfo();
    }

    /**
     * Set the userInfo part of this URL
     * the user info consists of <user>:<password>
     *
     * @param userInfo the new user info part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setUserInfo(String userInfo) {
        super.setUserInfo(userInfo);
    }

    /**
     * Return the path part of this URL
     *
     * @return the path part
     */
    public String getPath() {
        return super.getPath();
    }

    /**
     * Set the path part of this URL
     *
     * @param path the path part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setPath(String path) {
        super.setPath(path);
    }

    /**
     * Return the file part of this URL. The file is the path plus
     * the query string
     * @return the file part
     */
    public String getFile() {
        return super.getFile();
    }

    /**
     * Return the reference part of the URL
     *
     * @return the reference part
     */
    public String getReference() {
        return super.getReference();
    }

    /**
     * Set the reference part of the URL
     *
     * @param reference new reference part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setReference(String reference) {
        super.setReference(reference);
    }

    /**
     * Return the query part of this URL
     *
     * @return the query part
     */
    public String getQuery() {
        return super.getQuery();
    }

    /**
     * Set the entire query part of this URL.
     *
     * @param query the query part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setQuery(String query) {
        super.setQuery(query);
    }

    /**
     * Adds a name value pair to the query string
     *
     * @param name the name
     * @param value the value
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void addParameterValue(String name, String value) {
        super.addParameterValue(name, value);
    }

    /**
     * Remove a parameter and all of its values from the query string
     *
     * @param name the name of the parameter to remove
     * @return true if the parameter was found and removed successfully
     * @throws UnsupportedOperationException If this url is read only.
     */
    public boolean removeParameter(String name) {
        return super.removeParameter(name);
    }

    /**
     * Replace all values of a parameter with a single new value
     * @param name the name of the parameter
     * @param value the
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setParameterValue(String name, String value) {
        super.setParameterValue(name, value);
    }

    /**
     * Replace all values of a parameter with a an array of new values
     * @param name the name of the parameter
     * @param values the array of new values
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setParameterValues(String name, String[] values) {
        super.setParameterValues(name, values);
    }

    /**
     * Remove all parameter-value pairs from the query string
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void removeAllParameters() {
        super.removeAllParameters();
    }

    /**
     * Return a Map containing all the parameters and their values.
     * <p>
     * It is in the same format as the Map which is passed into the constructor.
     * </p>
     * @return The Map of parameters and values.
     */
    public Map getParameterMap() {
        return super.getParameterMap();
    }

    /**
     * Return an enumeration over the parameters names.
     * @return An Enumeration over the parameters names.
     */
    public Enumeration getParameterNames() {
        return super.getParameterNames();
    }

    /**
     * Return the value of a given parameter. Note: if the parameter has
     * multiple values then only the first one is returned.
     * @param name The name of the parameter whose value is requested.
     * @return The value of the given parameter.
     */
    public String getParameterValue(String name) {
        return super.getParameterValue(name);
    }

    /**
     * Retrieve the values for a given parameter
     * @param name the name of the parameter
     * @return a String array containing the values or null if the
     * parameter does not exist
     */
    public String[] getParameterValues(String name) {
        return super.getParameterValues(name);
    }

    /**
     * Return the external form of this object.
     * <p>
     * The returned string is suitable for use in an anchor.
     * </p>
     * @return The external form of this object.
     */
    public String getExternalForm() {
        return super.getExternalForm();
    }

    /**
     * Is this an absolute URL. An absolute URL is defined as a URL
     * that has a valid protocol part.
     * @return true if the URL is absolute, false otherwise.
     */
    public boolean isAbsolute() {
        return super.isAbsolute();
    }

    /**
     * Is this a relative URL. A relative URL is defined as a URL
     * that does not have a protocol part.
     * @return true if the URL is absolute, false otherwise.
     */
    public boolean isRelative() {
        return super.isRelative();
    }

    /**
     * Return the type of the path contained by this relative URL.
     * @return an int that indicates the type
     */
    public int getPathType() {
        return super.getPathType();
    }

    /**
     * Is this a fully qualified URL
     * @return true if fully qualifed, false otherwise
     */
    public boolean containsFullyQualifiedPath() {
        return super.containsFullyQualifiedPath();
    }

    /**
     * Is this a host relative URL
     * @return true if host relative, false otherwise
     */
    public boolean containsHostRelativePath() {
        return super.containsHostRelativePath();
    }

    /**
     * Is this a document relative URL
     * @return true if document relative, false otherwise
     */
    public boolean containsDocumentRelativePath() {
        return super.containsDocumentRelativePath();
    }

    /**
     * Return the string representation of this object.
     * <p>
     * The returned string identifies this object and encodes its state. It is
     * only suitable for debugging.
     * </p>
     */
    public String toString() {
        return super.toString();
    }

    /**
     * Return a String representation of the state of the object.
     * @return The String representation of the state of the object.
     */
    protected String paramString() {
        return super.paramString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Jul-04	4801/1	allan	VBM:2004070113 Sample PageURLRewriter java and jsp.

 25-May-04	4545/5	allan	VBM:2004052101 Override base class methods for javadoc.

 25-May-04	4545/3	allan	VBM:2004052101 Override base class methods for javadoc.

 24-May-04	4545/1	allan	VBM:2004052101 Move common url handling code to Synergetics.

 13-Jan-04	2573/1	andy	VBM:2003121907 renamed file variables to directory

 ===========================================================================
*/
