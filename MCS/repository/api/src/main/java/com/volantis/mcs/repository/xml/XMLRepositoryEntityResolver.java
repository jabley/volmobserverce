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
 * $Header: /src/voyager/com/volantis/mcs/repository/xml/XMLRepositoryEntityResolver.java,v 1.11 2002/12/13 08:41:16 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 05-Nov-01    Paul            VBM:2001092607 - Added mapping for europa dtd.
 * 07-Dec-01    Paul            VBM:2001120701 - Renamed europa to ganymede.
 * 09-Jan-02    Allan           VBM:2001121703 - Added mapping for phobos.dtd.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Adrian          VBM:2002040808 - Added DTD constants for Europa
 *                              and Repository-Theme DTD module.
 * 16-Aug-02    Paul            VBM:2002081514 - Added support for leda schema
 *                              and relative paths to support modules more
 *                              easily.
 * 27-Aug-02    Ian             VBM:2002081303 - Added Synopie dtd.
 * 29-Aug-02    Adrian          VBM:2002082901 - Added Mimas dts to dtds map.
 * 11-Sep-02    Ian             VBM:2002091101 - Corrected spelling of sinope.
 * 13-Dec-02    Adrian          VBM:2002100311 - Added Metis dtd to Map of dtds
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.xml;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.volantis.xml.sax.ExtendedSAXException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Resolve DTD entity references.
 */
public class XMLRepositoryEntityResolver
  implements EntityResolver,
             XMLRepositoryConstants {

    /**
     * localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XMLRepositoryEntityResolver.class);

  private static final String RESOURCE_PREFIX = "resource://";

  /**
   * The map from public IDs to DTD resource names.
   */
  private static Map dtds;

  static {
    dtds = new HashMap ();

    // Add callisto dtd.
    dtds.put (CALLISTO_DTD_PUBLIC_ID, CALLISTO_DTD_SYSTEM_ID);

    // Add ganymede dtd.
    dtds.put (GANYMEDE_DTD_PUBLIC_ID, GANYMEDE_DTD_SYSTEM_ID);

    // Add phobos dtd.
    dtds.put (PHOBOS_DTD_PUBLIC_ID, PHOBOS_DTD_SYSTEM_ID);

    // Add europa dtd.
    dtds.put (EUROPA_DTD_PUBLIC_ID, EUROPA_DTD_SYSTEM_ID);

    // Add leda dtd.
    dtds.put (LEDA_DTD_PUBLIC_ID, LEDA_DTD_SYSTEM_ID);

    // Add synopie dtd.
    dtds.put (SINOPE_DTD_PUBLIC_ID, SINOPE_DTD_SYSTEM_ID);
      
    // Add metis dtd.
    dtds.put(METIS_DTD_PUBLIC_ID, METIS_DTD_SYSTEM_ID);

    // Add mimas dtd.
    dtds.put (MIMAS_DTD_PUBLIC_ID, MIMAS_DTD_SYSTEM_ID);

      // Add triton dtd.
      dtds.put (TRITON_DTD_PUBLIC_ID, TRITON_DTD_SYSTEM_ID);
  }

  // Javadoc inherited from super class.
  public InputSource resolveEntity (String publicId, String systemId)
    throws SAXException {

    //System.out.println ("Public id " + publicId);
    //System.out.println ("System id " + systemId);

    // Modules do not have public ids.
    if (publicId != null) {
      // If the public id is not a mariner dtd then return immediately.
      if (!publicId.startsWith (MARINER_DTD_PUBLIC_ID_PREFIX)) {
        return null;
      }

      // The public id is a mariner dtd so see whether we recognize it, if we
      // don't and it is not relative then throw an exception.
      systemId = (String) dtds.get (publicId);
      if (systemId == null) {
        int start = MARINER_DTD_PUBLIC_ID_PREFIX.length ();
        int end = publicId.indexOf ("//", start);
        String version = publicId.substring (start, end);
        
        throw new ExtendedSAXException(new XMLRepositoryException (
                  EXCEPTION_LOCALIZER.format("repository-incompatible-version",
                                             version)));
      }
    }

    String resource;
    if (systemId.startsWith (RESOURCE_PREFIX)) {
      resource = systemId.substring (RESOURCE_PREFIX.length ());
    } else {
      resource = systemId;
      systemId = RESOURCE_PREFIX + systemId;
    }

    // Get the class loader.
    ClassLoader loader = getClass ().getClassLoader ();
    if (loader == null) {
      loader = ClassLoader.getSystemClassLoader ();
    }

    InputStream stream = loader.getResourceAsStream (resource);

    if (stream == null) {
      throw new ExtendedSAXException(new XMLRepositoryException (
                EXCEPTION_LOCALIZER.format("resource-not-found", resource)));
    }

    // Create an InputSource from the stream and make sure that it has the
    // correct system id.
    InputSource source = new InputSource (stream);
    source.setSystemId (systemId);

    return source;
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Sep-03	1429/1	byron	VBM:2003091705 New triton dtd is not referenced in code

 ===========================================================================
*/
