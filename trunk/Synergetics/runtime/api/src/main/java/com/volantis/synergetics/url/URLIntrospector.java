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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.url;

import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * A utility class which simplifies the task of manipulating URLs. <p> A URL
 * consists of the following parts <protocol>://authority>/<path>?<query>#<reference>
 * </p><p> The authority can be further broken down into the following
 * components <user>:<password>@<host>:<port> </p><p> The file part of a URL
 * consists of <path>?<query> </p><p> RFC 1808 was used to formulate the
 * parsing of the url string into the appropriate parts
 *
 * @deprecated Use {@link java.net.URI} instead.
 */
public class URLIntrospector implements Cloneable {

    /**
     * Used to indicate that a URL is fully qualified
     */
    public static final int FULLY_QUALIFIED_PATH = 0;

    /**
     * Used to indicate that a URL is host relative
     */
    public static final int HOST_RELATIVE_PATH = 1;

    /**
     * Used to indicate that a URL is document relative
     */
    public static final int DOCUMENT_RELATIVE_PATH = 2;

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2001.";

    /**
     * To store protocol part of this URL <p> This should never be the empty
     * string. </p>
     */
    private String protocol;

    /**
     * To store the userInfo part of the URL. This relates to a
     * <user>:<password> string to the left of any @ in the authority <p> This
     * should never be the empty string. </p>
     */
    private String userInfo;

    /**
     * To store the host part of the URL <p> This should never be the empty
     * string. </p>
     */
    private String host;

    /**
     * To store the port part of the URL
     */
    private int port = -1;

    /**
     * To store path of this URL
     */
    private String path;

    /**
     * Map to store the query string name value pairs in
     */
    private Map queryParameters;

    /**
     * To store the reference part of the URL
     */
    private String reference;

    /**
     * Flag which if true indicates that this object is read only and cannot be
     * modified.
     */
    private boolean readOnly;

    /**
     * A cached version of the external form.
     */
    private String externalForm;

    /**
     * Creates a new empty URLIntrospector instance.
     */
    public URLIntrospector() {
        queryParameters = new TreeMap();
    }

    /**
     * Creates a new URLIntrospector instance.
     *
     * @param url String reprsentation of the URL we wish to create
     */
    public URLIntrospector(String url) {
        queryParameters = new TreeMap();
        parseURL(url);
    }

    /**
     * Creates a new URLIntrospector instance.
     *
     * @param url         String representation of the URL we wish to create
     * @param queryString Map containing the query string name value pairs. The
     *                    key for the map entries is the parameter name and the
     *                    value is an array of strings representing the
     *                    values.
     */
    public URLIntrospector(String url, Map queryString) {
        queryParameters = new TreeMap();
        // copy the parameters into the query string map
        for (Iterator i = queryString.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            setParameterValues((String) entry.getKey(),
                               (String[]) entry.getValue());
        }
        parseURL(url);
    }

    /**
     * Creates a new URLIntrospector instance from a base URL and a relative
     * URL.
     *
     * @param base     A base URL string
     * @param relative A relative URL string
     */
    public URLIntrospector(String base, String relative) {
        this(new URLIntrospector(base), new URLIntrospector(relative));
    }

    /**
     * Creates a new URLIntrospector instance from a base URL and a relative
     * URL.
     *
     * @param base     A base URL
     * @param relative A relative URL string
     */
    public URLIntrospector(URLIntrospector base, String relative) {
        this(base, new URLIntrospector(relative));
    }

    /**
     * Creates a new URLIntrospector instance from a base URL and a relative
     * URL.
     *
     * @param base     A base URLIntrospector object
     * @param relative A relative URLIntrospector object
     */
    public URLIntrospector(URLIntrospector base, URLIntrospector relative) {
        StringBuffer pathBuffer = new StringBuffer(80);

        // This algorithm for resolving relative URL's is based on
        // Section 4 of RFC1808
        if (base == null && relative == null) {
            return;
        }

        // 4.1  RFC1808
        if (base == null) {
            copy(relative);
            return;
        }
        // 4.2 (a) RFC1808
        if (relative == null) {
            copy(base);
            return;
        }
        copy(relative);
        // 4.2 (b) RFC1808
        if (getProtocol() != null) {
            return;
        }
        // 4.2 (c) RFC1808
        setProtocol(base.getProtocol());

        // 4.3 RFC1808
        if (getAuthority() == null) {
            setAuthority(base.getAuthority());

            // 4.4 RFC1808
            if ((this.path != null && !this.path.startsWith("/")) ||
                this.path == null) {
                // 4.5 RFC1808
                if (this.path == null) {
                    setPath(base.getPath());
                    // 4.5 (a) RFC1808 can be ignored for now as we do not extract
                    // <param> info from URL's
                    // 4.5 (b) RFC1808
                    if (this.queryParameters.size() == 0) {
                        setQuery(base.getQuery());
                    }
                } else {
                    // 4.6 RFC1808
                    String nPath = base.getPath();
                    int i, j;
                    if (nPath != null) {
                        i = nPath.lastIndexOf("/");
                        if (i != -1) {
                            for (j = 0; j <= i; j++) {
                                pathBuffer.append(nPath.charAt(j));
                            }
                        } else {
                            nPath = null;
                        }
                    }

                    pathBuffer.append(path);
                    setPath(pathBuffer.toString());
                }
            }
            // 4.7 RFC1808
        }
    }

    /**
     * Optimize the path of the url assocatiated with this URLIntrospector such
     * that "." characters are removed, ".."s are optimized and "//" are
     * changed to "/". (See RFC 1808 section 4.)
     */
    private void optimizePath() {
        if (path != null) {
            StringBuffer pathBuffer = new StringBuffer(path);
            int i;
            int len = pathBuffer.length();
            int pruneState = 0;
            int slashCount = 0;
            StringBuffer tempBuffer = new StringBuffer(80);
            boolean foundOtherChars = false;

            for (i = 0; i < len; i++) {
                char currentChar = pathBuffer.charAt(i);
                switch (currentChar) {
                    case '.':
                        pruneState++;
                        if (i == (len - 1)) {
                            if (pruneState == 1) {
                                // Found trailing "."
                                pruneState = -4;
                            } else {
                                // Found trailing ".."
                                pruneState = -3;
                            }
                        }
                        slashCount = 0;
                        break;
                    case '/':
                        slashCount++;
                        if (slashCount == 1) {
                            // Write out only the first slash.
                            tempBuffer.append('/');
                        }
                        if (pruneState == 1) {
                            // Found "./"
                            pruneState = -2;
                        } else if (pruneState > 1) {
                            // Found "../"
                            pruneState = -1;
                        }
                        break;
                    default:
                        foundOtherChars = true;
                        pruneState = 0;
                        slashCount = 0;
                }
                if (currentChar != '/') {
                    // Ensure that no slashes are written as the first slash
                    // of one or more consecutive slashes has already been
                    // written earlier.
                    tempBuffer.append(currentChar);
                }

                if (pruneState < 0) {
                    if (foundOtherChars) {
                        tempBuffer = prunePath(tempBuffer, pruneState);
                    }
                    pruneState = 0;
                }
            }
            path = tempBuffer.toString();
        }
    }

    /**
     * Cut the path back based on a path state.
     *
     * State -1 represents a path such as /a/b/c/../ which would be pruned to
     * /a/b/d/e.jsp
     *
     * State -2 represents a path such as /a/b/c/./ which would become /a/b/c/
     *
     * State -3 represents a path such as /a/b/c/.. which would become /a/b/
     *
     * State -4 represents a path such as /a/b/c/. which would become /a/b/c/
     *
     * @param path  A <code>StringBuffer</code> containing the path
     * @param state the state of the path (see descriptions above)
     * @return The <code>StringBuffer</code> with the modified path.
     *
     * @throws IllegalStateException if the state is illegal for the specified
     *                               path.
     */
    private StringBuffer prunePath(StringBuffer path, int state) {
        int slashCount = 0;
        switch (state) {
            case -1:
                slashCount = 3;
                break;
            case -2:
                slashCount = 2;
                break;
            case -3:
                slashCount = 2;
                break;
            case -4:
                slashCount = 1;
        }

        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '/') {
                slashCount--;
            }
            if (slashCount == 0) {
                path.delete(i + 1, path.length());
                // todo put this return outside of the loop
                return path;
            }
        }

        // todo: this exception does not always get thrown when it should - see testcase
        throw new IllegalStateException("Cannot resolve relative address because"
                                        + " the path does not contain enough"
                                        +
                                        " directory structures to be able to"
                                        + " traverse back up the tree.");
    }

    /**
     * Create a clone of another URLIntrospector.
     */
    public URLIntrospector(URLIntrospector url) {
        copy(url);
    }

    /**
     * Prevent any one from modifying this again by making it read only.
     */
    public void makeReadOnly() {
        readOnly = true;
    }

    /**
     * Return whether this url is read only or not.
     *
     * @return true if this url cannot be modified and false otherwise.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Make sure that this url is modifiable.
     *
     * @throws UnsupportedOperationException If this url is read only.
     */
    private void checkModifiableState() {
        if (readOnly) {
            throw new UnsupportedOperationException(
                "Cannot modify read only url");
        }

        // Make sure that the cached external form is invalidated.
        externalForm = null;
    }

    /**
     * Make this URLIntrospector a copy of the URLIntrospector that is passed
     * in
     *
     * @param url The URLIntrospector to copy
     */
    private void copy(URLIntrospector url) {
        queryParameters = new TreeMap();
        // copy internal state of this to copy
        this.protocol = url.protocol;
        this.reference = url.reference;
        this.host = url.host;
        this.userInfo = url.userInfo;
        this.port = url.port;

        setPath(url.path);

        // copy the query string
        setQuery(url.getQuery());
    }

    /**
     * Create a deep copy of this URLIntrospector object.
     */
    // rest of javadoc inherited
    public Object clone() {
        URLIntrospector copy = null;
        try {
            copy = (URLIntrospector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UndeclaredThrowableException(e);
        }
        // copy internal state of this to copy
        copy.protocol = this.protocol;
        copy.reference = this.reference;
        copy.host = this.host;
        copy.userInfo = this.userInfo;
        copy.port = this.port;
        copy.path = this.path;

        // copy the query string
        copy.setQuery(this.getQuery());

        return copy;
    }

    // Javadoc inherited from super class.
    public boolean equals(Object other) {
        if (!(other instanceof URLIntrospector)) {
            return false;
        }

        URLIntrospector o = (URLIntrospector) other;
        return ObjectHelper.equals(protocol, o.protocol)
            && ObjectHelper.equals(userInfo, o.userInfo)
            && ObjectHelper.equals(host, o.host)
            && ObjectHelper.equals(port, o.port)
            && ObjectHelper.equals(path, o.path)
            && ObjectHelper.equals(queryParameters, o.queryParameters)
            && ObjectHelper.equals(reference, o.reference);
    }

    // Javadoc inherited from super class.
    public int hashCode() {
        return ObjectHelper.hashCode(protocol)
            + ObjectHelper.hashCode(userInfo)
            + ObjectHelper.hashCode(host)
            + ObjectHelper.hashCode(port)
            + ObjectHelper.hashCode(path)
            + ObjectHelper.hashCode(queryParameters)
            + ObjectHelper.hashCode(reference);
    }

    /**
     * Return the protocol part of this URL
     *
     * @return the protocol part
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Set the protocol part of this URL
     *
     * @param protocol the protocol part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setProtocol(String protocol) {
        checkModifiableState();
        this.protocol = protocol;
    }

    /**
     * Return the authority part of this URL
     *
     * @return the authority part
     */
    public String getAuthority() {
        if (hasAuthority()) {
            StringBuffer sb = new StringBuffer();
            reconstructAuthority(sb);
            return sb.toString();
        }
        return null;
    }

    /**
     * Set the authority part of this URL
     *
     * @param authority the authority part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setAuthority(String authority) {
        checkModifiableState();
        parseAuthority(authority);
    }

    /**
     * Return the host part of this URL
     *
     * @return the host part
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the host part of this URL
     *
     * @param host the host part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setHost(String host) {
        checkModifiableState();
        this.host = host;
    }

    /**
     * Get the port part of this URL
     *
     * @return the port number or -1 if none is specified
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port number part of this URL
     *
     * @param port the port number
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setPort(int port) {
        checkModifiableState();
        this.port = port;
    }

    /**
     * Return the userInfo part of this URL
     *
     * @return the userInfo part
     */
    public String getUserInfo() {
        return this.userInfo;
    }

    /**
     * Set the userInfo part of this URL the user info consists of
     * <user>:<password>
     *
     * @param userInfo the new user info part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setUserInfo(String userInfo) {
        checkModifiableState();
        this.userInfo = userInfo;
    }

    /**
     * Return the path part of this URL
     *
     * @return the path part
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path part of this URL
     *
     * @param path the path part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setPath(String path) {
        checkModifiableState();
        this.path = path;
        optimizePath();
    }


    /**
     * Return the file part of this URL. The file is the path plus the query
     * string
     *
     * @return the file part
     */
    public String getFile() {
        StringBuffer sb = new StringBuffer(80);
        if (this.path != null) {
            sb.append(this.path);
        }
        String query = getQuery();
        if (query != null && query.length() > 0) {
            sb.append('?').append(query);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Return the reference part of the URL
     *
     * @return the reference part
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Set the reference part of the URL
     *
     * @param reference new reference part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setReference(String reference) {
        checkModifiableState();
        this.reference = reference;
    }

    /**
     * Return the query part of this URL
     *
     * @return the query part
     */
    public String getQuery() {
        if (hasQuery()) {
            StringBuffer sb = new StringBuffer();
            reconstructQueryString(sb);
            return sb.toString();
        }

        return null;
    }

    /**
     * Set the entire query part of this URL.
     *
     * @param query the query part
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setQuery(String query) {
        checkModifiableState();
        queryParameters.clear();
        if (query != null) {
            parseQueryString(query);
        }
    }

    /**
     * Adds a name value pair to the query string
     *
     * @param name  the name
     * @param value the value
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void addParameterValue(String name, String value) {
        checkModifiableState();
        List values = (List) queryParameters.get(name);
        if (null == values) {
            // currently no parameter so need to create entry in map
            // and vector to hold values
            values = new ArrayList(1);
            queryParameters.put(name, values);
        }
        if (value != null) {
            values.add(value);
        }
    }

    /**
     * Remove a parameter and all of its values from the query string
     *
     * @param name the name of the parameter to remove
     * @return true if the parameter was found and removed successfully
     *
     * @throws UnsupportedOperationException If this url is read only.
     */
    public boolean removeParameter(String name) {
        checkModifiableState();
        return (queryParameters.remove(name) != null);
    }

    /**
     * Replace all values of a parameter with a single new value
     *
     * @param name  the name of the parameter
     * @param value the
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setParameterValue(String name, String value) {
        checkModifiableState();
        removeParameter(name);
        addParameterValue(name, value);
    }

    /**
     * Replace all values of a parameter with a an array of new values
     *
     * @param name   the name of the parameter
     * @param values the array of new values
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void setParameterValues(String name, String[] values) {
        checkModifiableState();
        removeParameter(name);
        if (values == null || values.length == 0) {
            return;
        }
        List list = new ArrayList(values.length);
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        queryParameters.put(name, list);
    }

    /**
     * Remove all parameter-value pairs from the query string
     *
     * @throws UnsupportedOperationException If this url is read only.
     */
    public void removeAllParameters() {
        checkModifiableState();
        queryParameters.clear();
    }

    /**
     * Return a Map containing all the parameters and their values. <p> It is
     * in the same format as the Map which is passed into the constructor.
     * </p>
     *
     * @return The Map of parameters and values.
     */
    public Map getParameterMap() {
        Map map = new HashMap();
        for (Iterator i = queryParameters.entrySet().iterator();
             i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            Collection values = (Collection) entry.getValue();
            String[] array = new String[values.size()];
            values.toArray(array);

            map.put(entry.getKey(), array);
        }

        return map;
    }

    /**
     * Return an enumeration over the parameters names.
     *
     * @return An Enumeration over the parameters names.
     */
    public Enumeration getParameterNames() {
        // Wrap the Iterator in an Enumeration.
        return new Enumeration() {
            Iterator iterator = (URLIntrospector.this.queryParameters.keySet().
                iterator());

            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            public Object nextElement() {
                return iterator.next();
            }
        };
    }

    /**
     * Return the value of a given parameter. Note: if the parameter has
     * multiple values then only the first one is returned.
     *
     * @param name The name of the parameter whose value is requested.
     * @return The value of the given parameter.
     */
    public String getParameterValue(String name) {
        List values = (List) queryParameters.get(name);
        if (null == values || values.isEmpty()) {
            return null;
        }
        return (String) values.get(0);
    }

    /**
     * Retrieve the values for a given parameter
     *
     * @param name the name of the parameter
     * @return a String array containing the values or null if the parameter
     *         does not exist
     */
    public String[] getParameterValues(String name) {
        List values = (List) queryParameters.get(name);
        if (null == values) {
            return null;
        }
        return (String[]) (values.toArray(new String[values.size()]));
    }

    /**
     * Return the external form of this object. <p> The returned string is
     * suitable for use in an anchor. </p>
     *
     * @return The external form of this object.
     */
    public String getExternalForm() {
        if (externalForm == null) {
            StringBuffer url = new StringBuffer(80);
            if (null != protocol) {
                url.append(protocol).append(':');
            }
            // Add the authority prefixed by // if it is not empty.
            if (hasAuthority()) {
                url.append("//");
                reconstructAuthority(url);
            }

            // Add the path.
            if (path != null) {
                if (!path.startsWith("/") && path.length() > 0 &&
                    url.length() > 0) {
                    url.append('/');
                }
                url.append(path);
            }

            // Add the query string, prefixed with ? if it is not empty.
            if (hasQuery()) {
                url.append("?");
                reconstructQueryString(url);
            }

            // Add the reference.
            if (reference != null) {
                url.append('#').append(reference);
            }
            externalForm = url.toString();
        }

        return externalForm;
    }

    /**
     * Is this an absolute URL. An absolute URL is defined as a URL that has a
     * valid protocol part.
     *
     * @return true if the URL is absolute, false otherwise.
     */
    public boolean isAbsolute() {
        return (getProtocol() != null);
    }

    /**
     * Is this a relative URL. A relative URL is defined as a URL that does not
     * have a protocol part.
     *
     * @return true if the URL is absolute, false otherwise.
     */
    public boolean isRelative() {
        return (getProtocol() == null);
    }

    /**
     * Return the type of the path contained by this relative URL.
     *
     * @return an int that indicates the type
     */
    public int getPathType() {
        if (isAbsolute()) {
            throw new IllegalStateException
                ("Cannot call this method on an absolute url '"
                 + getExternalForm() + "'");
        }

        if (getAuthority() != null) {
            // URL is relative with a net_path (authority)
            // therefore it is fully qualified
            return FULLY_QUALIFIED_PATH;
        } else {
            // must be dealing with a relative URL
            String path = getPath();

            if (path != null && path.startsWith("/")) {
                return HOST_RELATIVE_PATH;
            }
            // relative URL is neither fully qualified or host relative
            // so it must be document relative.
            return DOCUMENT_RELATIVE_PATH;
        }
    }

    /**
     * Is this a fully qualified URL
     *
     * @return true if fully qualifed, false otherwise
     */
    public boolean containsFullyQualifiedPath() {
        return (isRelative() && getPathType() == FULLY_QUALIFIED_PATH);
    }

    /**
     * Is this a host relative URL
     *
     * @return true if host relative, false otherwise
     */
    public boolean containsHostRelativePath() {
        return (isRelative() && getPathType() == HOST_RELATIVE_PATH);
    }

    /**
     * Is this a document relative URL
     *
     * @return true if document relative, false otherwise
     */
    public boolean containsDocumentRelativePath() {
        return (isRelative() && getPathType() == DOCUMENT_RELATIVE_PATH);
    }

    /**
     * Return the string representation of this object. <p> The returned string
     * identifies this object and encodes its state. It is only suitable for
     * debugging. </p>
     */
    // rest of javadoc inherited
    public String toString() {
        return ObjectHelper.identityString(this, paramString());
    }

    /**
     * Return a String representation of the state of the object.
     *
     * @return The String representation of the state of the object.
     */
    protected String paramString() {
        return protocol
            + "," + userInfo
            + "," + host
            + "," + port
            + "," + path
            + "," + queryParameters
            + "," + reference;
    }

    /**
     * Tests whether a string is a valid as a url protocol part see RFC 1808
     * for what is defined as valid
     *
     * @param pcol the string to test
     * @return true if pcol id a valid protocol
     */
    private boolean isValidProtocol(String pcol) {
        int len = pcol.length();
        if (len < 1) {
            return false;
        }
        char c = pcol.charAt(0);
        if (!Character.isLetter(c)) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            c = pcol.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '+' &&
                c != '-') {
                return false;
            }
        }
        return true;
    }


    /**
     * Pull apart a query string into name value pairs and store them away in
     * the query string map. Note the query string passed in should not start
     * with a ?
     *
     * @param query the query string to dissect
     */
    private void parseQueryString(String query) {
        StringTokenizer apmTokenizer = new StringTokenizer(query, "&");
        StringTokenizer eqTokenizer;
        String pvPairStr;

        while (apmTokenizer.hasMoreTokens()) {
            pvPairStr = apmTokenizer.nextToken();
            if (null == pvPairStr) {
                continue;
            }
            eqTokenizer = new StringTokenizer(pvPairStr, "=");
            if (eqTokenizer.countTokens() != 2) {
                // Parameters with blank value should be accepted therefore we must add it to
                // parameters map.
                // i.e ?a= should be translated to ?a=
                // previously parameter with empty value was removed
                // consequence of this fix is also parsing ?p1 to ?p1= as result
                // so separate VBM: 2006022817 is raised wit description of this problem
                if (!pvPairStr.startsWith("=")) {
                    addParameterValue(eqTokenizer.nextToken(), "");
                    // otherwise we have: ?=p1 as input - so input should be also ?=p1 or dropped
                    // because it is possible that other parameter p1 already exists.
                }
            } else {
                addParameterValue(eqTokenizer.nextToken(),
                                  decode(eqTokenizer.nextToken()));
            }
        }
    }

    /**
     * Decode a String which is encoded in the x-www-form-urlencoded format.
     * <p> This is needed because in JDK 1.2 URLDecode.decode declares that it
     * throws Exception. </p>
     *
     * @param string The String to decode.
     * @return The decoded String.
     */
    private String decode(String string) {
        try {
            return URLDecoder.decode(string);
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Check whether this url actually has a query string.
     *
     * @return true if the url has a query part; false otherwise.
     */
    private boolean hasQuery() {
        return !queryParameters.isEmpty();
    }

    /**
     * Build a query string from the parameter value pairs that are stored in
     * the query string map
     */
    private void reconstructQueryString(StringBuffer sb) {
        String seperator = "";
        String value;
        for (Iterator i = queryParameters.entrySet().iterator();
             i.hasNext();) {

            Map.Entry entry = (Map.Entry) i.next();

            List values = (List) entry.getValue();
            String name = (String) entry.getKey();

            for (Iterator j = values.iterator(); j.hasNext();) {
                value = (String) j.next();

                sb.append(seperator)
                    .append(name)
                    .append('=')
                    .append(URLEncoder.encode(value));

                seperator = "&";

            }
        }
    }

    /**
     * Seperate a URL out into the following parts protocol, authority, host,
     * port, userinfo, path, reference and query
     *
     * @param URL the url to dissect
     */
    private void parseURL(String URL) {

        if (null == URL) {
            return;
        }

        String url = URL.trim();

        int start = 0;
        // End is 1 index past the end of the string
        int end = url.length();

        // extract the reference if any
        if (end > 0) {
            int refIndex = url.indexOf('#');
            if (refIndex != -1) {
                this.reference = url.substring(refIndex + 1);
                url = url.substring(0, refIndex);
                end = refIndex;
            }
        }


        // extract the protocol if any
        if (end >= 1) {
            int protocolEnd = url.indexOf(':');
            if (protocolEnd != -1) {
                this.protocol = url.substring(0, protocolEnd);
                url = url.substring(protocolEnd + 1);
                end = url.length();
                if (!isValidProtocol(this.protocol)) {
                    this.protocol = null;
                }
            }
        }

        // extract the query if any part
        if (end >= 1) {
            int queryStart = url.indexOf('?');
            //queryOnly = queryStart == start;
            if (queryStart != -1) {
                parseQueryString(url.substring(queryStart + 1));
                url = url.substring(0, queryStart);
                end = queryStart;
            }
        }

        // Parse the authority part if any
        if (end > 1 && url.startsWith("//")) {
            int authEnd = url.indexOf('/', 2);
            if (authEnd == -1) {
                authEnd = end;
            }

            parseAuthority(url.substring(2, authEnd));
            url = url.substring(authEnd);
            end = url.length();
        }

        // Parse the file path if any
        if (end > 0) {
            setPath(url);
        }

        // Check to make sure that this object is not invalid.
        checkInvariant();
    }

    /**
     * break down an authority string into userInfo, host and port strings
     *
     * @param authority the authority
     */
    private void parseAuthority(String authority) {
        this.host = authority;
        this.userInfo = null;
        this.port = -1;

        // If the authority is null then there is nothing else we can do.
        if (authority == null) {
            return;
        }

        // If the authority is empty then clear the host and return.
        if (authority.length() == 0) {
            this.host = null;
            return;
        }

        int end = authority.length();

        int index = authority.indexOf('@');
        if (index != -1) {
            this.userInfo = authority.substring(0, index);
            this.host = authority.substring(index + 1);
        }
        index = this.host.indexOf(':');
        if (index != -1) {
            // port can be null according to RFC2396
            if (this.host.length() > (index + 1)) {
                this.port = Integer.parseInt(this.host.substring(index + 1));
            }
            this.host = host.substring(0, index);
        }
    }

    /**
     * Check whether this url actually has an authority.
     *
     * @return true if the url has an authority part; false otherwise.
     */
    private boolean hasAuthority() {
        return (userInfo != null || host != null || port != -1);
    }

    /**
     * Reconstruct the authority part from the userInfo, host and port parts
     *
     * @todo find out what this method is doing and javadoc
     */
    private void reconstructAuthority(StringBuffer sb) {

        // Check to make sure that this object is not invalid.
        checkInvariant();

        if (userInfo != null) {
            sb.append(userInfo).append('@');
        }

        if (host != null) {
            sb.append(host);
        }

        if (port >= 0) {
            sb.append(':').append(port);
        }
    }

    /**
     * Validate the host and userInfo parts of the url if they are non-null.
     *
     * @throws IllegalStateException if either host or user info parts of the
     *                               url are invalid.
     */
    private void checkInvariant() {
        if (host != null && host.length() == 0) {
            throw new IllegalStateException("Host is empty");
        }

        if (userInfo != null && userInfo.length() == 0) {
            throw new IllegalStateException("User info is empty");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-May-05	456/4	pcameron	VBM:2005050416 Fixed multiple slash path optimization

 05-Jul-04	266/1	allan	VBM:2004070113 Fix bug in clone() method and make equals & hashcode final

 26-May-04	239/1	pcameron	VBM:2004052601 Fixed null path and base/relative construction

 26-May-04	236/1	adrian	VBM:2004052403 Ensure URLIntrospector handles null paths

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 24-May-04	219/1	allan	VBM:2004052101 Add URLIntrospector and SortedURLTreeMap

 ===========================================================================
*/
