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
 * $Header: /src/voyager/com/volantis/mcs/repository/xml/XMLRepositoryConstants.java,v 1.12 2002/12/13 08:41:16 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 24-Oct-01    Paul            VBM:2001092608 - Added extra exception
 *                              constants.
 * 05-Nov-01    Paul            VBM:2001092607 - Added constants for europa
 *                              DTD.
 * 07-Dec-01    Paul            VBM:2001120701 - Renamed europa to ganymede.
 * 09-Jan-01    Allan           VBM:2001121703 - Added constants for phobos
 *                              dtd.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 26-Apr-02    Adrian          VBM:2002040808 - Added DTD constants for Europa
 *                              and Repository-Theme DTD module.
 * 16-Aug-02    Paul            VBM:2002081514 - Removed public id for theme
 *                              module and added ids for leda.
 * 23-Aug-02    Ian             VBM:2002081303 - Added synopie id's.
 * 29-Aug-02    Adrian          VBM:2002082901 - Added MIMAS id's and set them
 *                              to current.
 * 11-Sep-02    Ian             VBM:2002091101 - Corrected spelling of sinope.
 * 13-Dec-02    Adrian          VBM:2002100311 - Added DTD constants for Metis 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.xml;



/**
 * The constants used by the repository and the accessors.
 *
 * @deprecated 
 */
public interface XMLRepositoryConstants {

    // Callisto DTD constants
  public static final String CALLISTO_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER 2.3//EN";

  public static final String CALLISTO_DTD_SYSTEM_ID
    = "com/volantis/mcs/repository/xml/dtd/callisto.dtd";

  // Ganymede DTD constants
  public static final String GANYMEDE_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER 2.5//EN";

  public static final String GANYMEDE_DTD_SYSTEM_ID
    = "com/volantis/mcs/repository/xml/dtd/ganymede.dtd";

  // Phobos DTD constants
  public static final String PHOBOS_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER 2.5//EN";

  public static final String PHOBOS_DTD_SYSTEM_ID
    = "com/volantis/mcs/repository/xml/dtd/phobos.dtd";

  // Europa DTD constants
  public static final String EUROPA_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER 2.7//EN";

  public static final String EUROPA_DTD_SYSTEM_ID
    = "com/volantis/mcs/repository/xml/dtd/europa/europa.dtd";

  // Leda DTD constants
  public static final String LEDA_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER LEDA//EN";

  public static final String LEDA_DTD_SYSTEM_ID
    = "resource://com/volantis/mcs/repository/xml/dtd/leda/repository.dtd";

  // Synopie DTD constants
  public static final String SINOPE_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER SINOPE//EN";

  public static final String SINOPE_DTD_SYSTEM_ID
    = "resource://com/volantis/mcs/repository/xml/dtd/sinope/repository.dtd";

  // Metis DTD constants
  public static final String METIS_DTD_PUBLIC_ID
          = "-//VOLANTIS//DTD MARINER METIS//EN";

  public static final String METIS_DTD_SYSTEM_ID
          = "resource://com/volantis/mcs/repository/xml/dtd/metis/repository.dtd";
    
  // Mimas DTD constants
  public static final String MIMAS_DTD_PUBLIC_ID
    = "-//VOLANTIS//DTD MARINER MIMAS//EN";

  public static final String MIMAS_DTD_SYSTEM_ID
    = "resource://com/volantis/mcs/repository/xml/dtd/mimas/repository.dtd";

    // Triton DTD constants
    public static final String TRITON_DTD_PUBLIC_ID
      = "-//VOLANTIS//DTD MARINER TRITON//EN";

    public static final String TRITON_DTD_SYSTEM_ID
      = "resource://com/volantis/mcs/repository/xml/dtd/triton/repository.dtd";

  // Current release DTD constants
  // todo: these are not current any more, should be removed!
  public static final String CURRENT_DTD_PUBLIC_ID = TRITON_DTD_PUBLIC_ID;
  public static final String CURRENT_DTD_SYSTEM_ID = TRITON_DTD_SYSTEM_ID;

  public static final String MARINER_DTD_PUBLIC_ID_PREFIX
    = "-//VOLANTIS//DTD MARINER ";
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Feb-05	6986/1	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/1	emma	VBM:2005021411 Changing exception message to be more specific

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Feb-04	3136/3	philws	VBM:2004021908 Introduce new runtime device repository usage

 13-Jan-04	2573/1	andy	VBM:2003121907 renamed file variables to directory

 02-Jan-04	2302/2	andy	VBM:2003121706 gui now works with new repository structure

 23-Dec-03	2252/2	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 18-Sep-03	1429/1	byron	VBM:2003091705 New triton dtd is not referenced in code

 ===========================================================================
*/
