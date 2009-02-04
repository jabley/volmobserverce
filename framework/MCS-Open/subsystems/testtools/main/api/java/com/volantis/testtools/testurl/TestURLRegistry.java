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
 * $Header: /src/voyager/com/volantis/testtools/marinerurl/MarinerURLRegistry.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815   A registry that associates 
 *                              a channel name with an input stream. This is
 *                              used by MarinerURLConnection to determine which
 *                              input stream to use for a given channel.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.testurl;

import java.io.InputStream;

import java.util.Hashtable;


/** 
 * A registry that associates various testURL keys with streams.
 */
public class TestURLRegistry extends Object {
    
    /** 
     * Magic URL key for a URL that cannot even be opened. If you
     * try to open <tt>testurl:errorOnConnect</tt>, an IOException will be 
     * thrown.
     */
    public static final String ERROR_ON_CONNECT = "errorOnConnect";

    /**
     * The singleton instance of this class.
     */ 
    private static TestURLRegistry instance = null;

    /** 
     * Mapping of String to InputStream. 
     */
    private Hashtable inputStreams = new Hashtable();

    /**
     * Register testurl: as a valid Java URL type.
     */ 
    public static void register() {
        getInstance();
    }
    
    /** 
     * Register an InputStream for the associated key. URLs of the form
     * testurl:key will return the registered input stream on open.
     * <p>
     * This implicitly registers testurl: as a valid Java URL type.
     */
    public static void register( String key, InputStream is ) {
        getInstance().registerKey( key, is );
    }

    /**
     * Private constructor for the singleton.
     * <p>
     * This implicitly registers testurl: as a valid Java URL type.
     */ 
    private TestURLRegistry() {
        // Register us with Java as a URL handler.
        System.setProperty( "java.protocol.handler.pkgs", 
                "com.volantis.testtools" );
    }
    
    /*
     * Register an InputStream for the associated key. URLs of the form
     * testurl:key will return the registered input stream on open.
     * Note that when this URL is read from the input stream is consumed;
     * these URLs are not typically reusable.
     */
    protected void registerKey( String key, InputStream is ) {
        inputStreams.put( key, is );
    }

    /** 
     * Return the input stream associated with the given key, or null
     * if none.
     */
    protected InputStream getInputStream( String key ) {
        return (InputStream) inputStreams.get( key );
    }

    /** 
     * Get access to the singleton instance of MarinerURLRegistry.
     * <p>
     * This implicitly registers testurl: as a valid Java URL type.
     */
    protected synchronized static TestURLRegistry getInstance() {
        if (instance == null) {
            instance = new TestURLRegistry();
        }
        return instance;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Aug-03	956/3	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
