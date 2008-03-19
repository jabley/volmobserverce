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
 * $Header: /src/voyager/com/volantis/mcs/repository/AbstractRepositoryConnection.java,v 1.3 2001/06/28 11:11:00 pduffin Exp 
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 26-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 27-Nov-01    Paul            VBM:2001112205 - Added more logging.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Apr-02    Mat             VBM:2002022009 - Added code to cacheObject() 
 *                                               and cacheRetrievedObjects()
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The AbstractRepositoryConnection class encapsulates differences between 
 * different repository connection implementations. Specific repositories 
 * inherit from this class and implement particular repository technology. 
 */
public abstract class AbstractRepositoryConnection 
  implements RepositoryConnection {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractRepositoryConnection.class);

  private Repository repository;

    /**
   * Creates a new <code>AbstractRepositoryConnection</code> instance.
   *
   * @param repository the repository to which this connection applies 
   */
  public AbstractRepositoryConnection (Repository repository) {
    this.repository = repository;
  }

  public Repository getRepository () {
    return repository;
  }
  
  public void disconnect ()
    throws RepositoryException {

    if(logger.isDebugEnabled()){
        logger.debug ("Disconnecting " + this);
    }
    repository.disconnect (this);
    if(logger.isDebugEnabled()){
        logger.debug ("Disconnected " + this);
    }
  }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Feb-04	2694/3	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/1	mat	VBM:2004011917 Improve the way repository connections are located

 ===========================================================================
*/
