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
package com.volantis.synergetics.testtools.servletunit;
/********************************************************************************************************************
* $Id: ServletUnitHttpSession.java,v 1.1 2002/11/08 12:29:47 sfound Exp $
*
* Copyright (c) 2000-2001, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions 
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.net.URL;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.ServletContext;


class ServletUnitHttpSession implements HttpSession {


    final static public String SESSION_COOKIE_NAME = "JSESSION";


    /**
     * Returns the maximum time interval, in seconds, that the servlet engine will keep this session open 
     * between client requests. You can set the maximum time interval with the setMaxInactiveInterval method.
     **/
    public int getMaxInactiveInterval() {
        if (_isInvalid) throw new IllegalStateException();
        return _maxInactiveInterval;
    }


    /**
     * Specifies the maximum length of time, in seconds, that the servlet engine keeps this session 
     * if no user requests have been made of the session.
     **/
    public void setMaxInactiveInterval( int interval ) {
        if (_isInvalid) throw new IllegalStateException();
        _maxInactiveInterval = interval;
    }


    /**
     * Returns a string containing the unique identifier assigned to this session. 
     * The identifier is assigned by the servlet engine and is implementation dependent.
     **/
    public String getId() {
        if (_isInvalid) throw new IllegalStateException();
        return _id;
    }


    /**
     * Returns the time when this session was created, measured
     * in milliseconds since midnight January 1, 1970 GMT.
     *
     * @exception IllegalStateException		if you attempt to get the session's
     *						creation time after the session has
     *						been invalidated
     **/
    public long getCreationTime() {
        if (_isInvalid) throw new IllegalStateException();
        return _creationTime;
    }


    /**
     * Returns the last time the client sent a request associated with this session, 
     * as the number of milliseconds since midnight January 1, 1970 GMT.
     **/ 
    public long getLastAccessedTime() {
        if (_isInvalid) throw new IllegalStateException();
        return _lastAccessedTime;
    }


    /**
     * Returns true if the Web server has created a session but the client 
     * has not yet joined. For example, if the server used only
     * cookie-based sessions, and the client had disabled the use of cookies, 
     * then a session would be new.
     **/
    public boolean isNew() {
        return _isNew;
    }


    /**
     * Invalidates this session and unbinds any objects bound to it.
     **/
    public void invalidate() {
        _isInvalid = true;
        _values.clear();
    }


    /**
     * @deprecated no replacement.
     **/
    public HttpSessionContext getSessionContext() {
        return null;
    }


    /**
     * @deprecated as of JSDK 2.2, use getAttribute
     **/
    public Object getValue( String name ) {
        return getAttribute( name );
    }


    /**
     * @deprecated as of JSDK 2.2, use setAttribute
     **/
    public void putValue( String name, Object value ) {
        setAttribute( name, value );
    }


    /**
     * @deprecated as of JSDK 2.2, use removeAttribute
     **/ 
    public void removeValue( String name ) {
        removeAttribute( name );
    }


    /**
     * @deprecated as of JSDK 2.2, use getAttributeNames.
     **/
    public String[] getValueNames() {
        if (_isInvalid) throw new IllegalStateException();
        throw new RuntimeException( "getValueNames not implemented" );
    }


    /**
     * Returns the object bound with the specified name in this session or null if no object of that name exists.
     **/
    public Object getAttribute( String name ) {
        if (_isInvalid) throw new IllegalStateException();
        return _values.get( name );
    }


    /**
     * Binds an object to this session, using the name specified. If an object of the same name 
     * is already bound to the session, the object is replaced.
     **/
    public void setAttribute( String name, Object value ) {
        if (_isInvalid) throw new IllegalStateException();
        _values.put( name, value );
    }


    /**
     * Removes the object bound with the specified name from this session. If the session does not 
     * have an object bound with the specified name, this method does nothing.
     **/ 
    public void removeAttribute( String name ) {
        if (_isInvalid) throw new IllegalStateException();
        _values.remove( name );
    }


    /**
     * Returns an array containing the names of all the objects bound to this session. 
     * This method is useful, for example, when you want to delete all the objects bound to this session.
     **/
    public Enumeration getAttributeNames() {
        if (_isInvalid) throw new IllegalStateException();
        return _values.keys();
    }


//---------------------------- methods added to HttpSession in JSDK 2.3 ----------------------------------------


    /**
     * Returns the ServletContext to which this session belongs.
     *
     * @since 1.3
     **/
    public ServletContext getServletContext() {
        return null;    // XXX implement me
    }

//-------------------------------------------- package members -------------------------------------------------


    /**
     * This method should be invoked when a servlet joins an existing session. It will update the last access time
     * and mark the session as no longer new.
     **/
    void access() {
        _lastAccessedTime = new Date().getTime();
        _isNew = false;
    }


    URL getOriginalURL() {
        return _originalURL;
    }


    void setOriginalURL( URL originalURL ) {
        _originalURL = originalURL;
    }


    /**
     * Sets the authenticated user information for a session.
     *
     * @param userName the name the user supplied when logging in
     * @param roles an array of role names assigned to the user
     **/
    void setUserInformation( String userName, String[] roles ) {
        _userName = userName;
        _roles    = roles;
    }


    String getUserName() {
        return _userName;
    }


    String[] getRoles() {
        return _roles;
    }


//------------------------------------- private members ---------------------------------------

    private static int _NextID = 1;

    private final long _creationTime = new Date().getTime();

    private final String _id = Integer.toString( _NextID++ );



    private int       _maxInactiveInterval;

    private long      _lastAccessedTime = new Date().getTime();

    private boolean   _isInvalid;

    private Hashtable _values = new Hashtable();

    private boolean   _isNew = true;

    private String    _userName;

    private String[]  _roles;

    private URL       _originalURL;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
