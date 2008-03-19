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
 * $Header: /src/voyager/com/volantis/mcs/repository/xml/XMLRepositoryException.java,v 1.6 2002/05/23 11:19:07 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 24-Oct-01    Paul            VBM:2001092608 - Added a check to make sure
 *                              that the specific key is not null before trying
 *                              to use it to find a resource.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 11-Feb-02    Paul            VBM:2001122105 - Fixed problem with retrieving
 *                              the bundle.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 23-May-02    Payal           VBM:2002041903 - Removed 
 *                              getLocalizedSpecificMessage () to use 
 *                              getLocalizedSpecificMessage () of parent class.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.xml;

import com.volantis.mcs.repository.RepositoryException;

/**
 * The XMLRepositoryException class encapsulates exceptions thrown when
 * there is an error in a XML repository operation.
 */
public class XMLRepositoryException
  extends RepositoryException {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2000.";

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message The error message associated with the exception. If
     *                localization is required this message must be
     *                pre-localized.
     */
    public XMLRepositoryException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message   The error message associated with the exception. If
     *                  localization is required this message must be
     *                  pre-localized.
     * @param exception The associated exception
     */
    public XMLRepositoryException(String message,
                 Exception exception) {
        super(message, exception);
    }

    /**
     * Create a new <code>XMLRepositoryException</code> from an Exception.
     * @param e An IOException.
     */
    public XMLRepositoryException(Exception e) {
        super(e);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
