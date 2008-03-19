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
 * $Header: /src/voyager/com/volantis/mcs/repository/xml/XMLRepositoryConnection.java,v 1.8 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 28-Jan-02    Payal           VBM:2002012305 - Modified beingOperationSet(),
 *                              endOperationSet() and abortOperationSet() to
 *                              return a boolean value.
 * 28-Jan-02    Allan           VBM:2001121703 - Provided real implementations
 *                              of begin, end and abort operation sets. Added
 *                              updateElement(), deleteElement(), getElement()
 *                              and renameElement() methods.
 * 06-Feb-02    Paul            VBM:2001122103 - Added multiFile flag.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02	Mat		VBM:2002022009 - Added
 *				setCacheRetrievedObjects() to constructor
 * 14-Aug-02    Allan           VBM:2002072303 - Added lock() and unlock()
 *                              methods.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.impl.xml;

import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.impl.LocalRepositoryConnectionImpl;
import com.volantis.mcs.repository.xml.XMLRepositoryConnection;

/**
 * The XMLRepositoryConnection provides an XML based RepositoryConnection.
 */
public class XMLRepositoryConnectionImpl
        extends LocalRepositoryConnectionImpl
        implements XMLRepositoryConnection {

    /**
     * A flag which indicates whether the connection is still connected.
     */
    private boolean connected;

    /**
     * Create a new connection to the specified repository.
     *
     * @param repository The repository to which the connection should be
     *                   made.
     */
    public XMLRepositoryConnectionImpl(Repository repository) {
        super(repository);
        connected = true;
    }

    public void disconnect() throws RepositoryException {
        super.disconnect();

        connected = false;
    }

    // Javadoc inherited from super class.
    public boolean beginOperationSet()
            throws RepositoryException {
        return true;
    }

    // Javadoc inherited from super class.
    public boolean endOperationSet()
            throws RepositoryException {
        return true;
    }

    // Javadoc inherited from super class.
    public boolean abortOperationSet()
            throws RepositoryException {

        return true;
    }

    // Javadoc inherited from super class.
    public boolean supportsOperationSets() {
        return true;
    }

    // Javadoc inherited from super class.
    public boolean isConnected()
            throws RepositoryException {

        return connected;
    }

    public RepositoryConnection getUnderLyingConnection()
            throws RepositoryException {
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Oct-05	9789/1	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 15-Oct-04	5794/1	geoff	VBM:2004100801 MCS Import slow

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/9	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/7	tony	VBM:2004012601 localisation services update

 18-Feb-04	2789/4	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Feb-04	3090/1	ianw	VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

 11-Feb-04	2761/2	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2694/7	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/1	mat	VBM:2004011917 Improve the way repository connections are located

 03-Feb-04	2767/1	claire	VBM:2004012701 Adding project handling code

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 13-Jan-04	2573/1	andy	VBM:2003121907 removed remnants of single file support

 02-Jan-04	2302/1	andy	VBM:2003121706 gui now works with new repository structure

 23-Dec-03	2252/2	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/1	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 ===========================================================================
*/
