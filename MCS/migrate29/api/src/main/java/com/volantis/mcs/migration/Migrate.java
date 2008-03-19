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
 * $Header: /src/voyager/com/volantis/mcs/migration/Migrate.java,v 1.7 2003/02/19 11:49:29 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Jul-02    Ian             VBM:2002031803 Created.
 * 15-Jul-02    Ian             VBM:2002031803 - Fixed problem introduced
 *                              with URIResolvers.
 * 16-Aug-02    Paul            VBM:2002081514 - Configured logging to remove
 *                              error and forced the doctype to refer to the
 *                              current public and system ids.
 * 23-Aug-02    Adrian          VBM:2002082008 - Updated to read source xml
 *                              and extract the mariner version number from
 *                              publicId in the DocType.
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              org.apache.xalan references to 
 *                              com.volantis.xml.xalan
 * 18-Feb-03    Ian             VBM:2002031803 Updated Javadoc finally. Fixed 
 *                              typo's in ResourceURIResolver.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.migration;

// Volantis Classes
import com.volantis.mcs.bundles.BundleUtilities;
import com.volantis.mcs.bundles.EnhancedBundle;
import com.volantis.mcs.repository.xml.XMLRepositoryConstants;
import com.volantis.mcs.repository.xml.XMLRepositoryEntityResolver;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.log.DefaultConfigurator;
import com.volantis.xml.xalan.serialize.Serializer;
import com.volantis.xml.xalan.serialize.SerializerFactory;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This class implements a command line utility that will upgrade an XML
 * repository from one version to the next. The class is inteligent enough to
 * work out all possible routes and transformations from one version to the 
 * next. All transformations are held in XSL resource files.
 */
public class Migrate implements XMLRepositoryConstants {

  /**
   * The XSL resources used for the transformation.
   */
  private static Object [] xsltResources = {};
  
  /**
   * The default transformation.
   */
  private static String  defaultXsltResource =
      "com/volantis/mcs/migration/xsl/common/common.xsl";


  /**
   * The properties bundle that controls the behaviour of the Migration.
   */
  private static EnhancedBundle bundle = BundleUtilities.getBundle(
      "com.volantis.mcs.migration.MigrateBundle");

    /**
     * The meta factory to use for creating JAXP XSL transformer factories.
     * <p>
     * Since we are a command line tool we always use the repackaged MCS
     * version of Xalan which we ship rather than relying on the standard JAXP
     * dynamic lookup process.
     */
    private static TransformerMetaFactory transformerMetaFactory =
            new MCSTransformerMetaFactory();

  /**
   * The default Constructor for the Migrate utility.
   * @param srcXml The source XML repository to transform.
   * @param destXml The stream to write the transformed XML to.
   */
  public Migrate (InputSource srcXml,
                  StreamResult destXml) {

    // Disable logging during migration.
    DefaultConfigurator.configure (false);

    TransformerFactory transformerFactory;
    SAXTransformerFactory saxTransformerFactory;
    XMLFilter [] filters = null;
    XMLFilter lastFilter = null;
    Templates lastTemplates = null;

    // Make sure that we know how to find the xml repository schema which is
    // used when outputting the result.
    EntityResolver entityResolver = new XMLRepositoryEntityResolver ();

    try {
      // Instantiate a TransformerFactory.
      transformerFactory = transformerMetaFactory.createTransformerFactory();

      // Determine whether the TransformerFactory supports the use of
      // SAXSource and SAXResult, if it does not then there is nothing that
      // we can do.
      if (!transformerFactory.getFeature(SAXSource.FEATURE)
          || !transformerFactory.getFeature(SAXResult.FEATURE)) {

        System.err.println ("TranformerFactory does not support"
                            + " necessary features");
        System.exit (1);
      }

      // Cast the TransformerFactory to SAXTransformerFactory.
      saxTransformerFactory = (SAXTransformerFactory) transformerFactory;


      // Create the XMLFilters for each of the transformations.
      int count = xsltResources.length;
      filters = new XMLFilter [count];
      for (int i = 0; i < count; i += 1) {
        String xsltResource = (String)xsltResources [i];
          URL xsltURL = getClass().getResource(xsltResource);
        Source source = new StreamSource(xsltURL.toExternalForm());

        Templates templates = saxTransformerFactory.newTemplates (source);
        XMLFilter filter = saxTransformerFactory.newXMLFilter (templates);
        filter.setEntityResolver (entityResolver);
        filters [i] = filter;

        // Chain the filters together.
        if (lastFilter != null) {
          filter.setParent (lastFilter);
        }
        lastFilter = filter;
        lastTemplates = templates;
      }
    }
    catch (Exception e) {
      System.err.print("\nAn unknown error occured whilst loading "+
                       "the stylesheet\n\n");
      e.printStackTrace();
      System.exit(1);
    }

    try {

      SAXParserFactory parserFactory = SAXParserFactory.newInstance ();
      parserFactory.setNamespaceAware (true);

      // Do not validate the input document
      parserFactory.setValidating (false);

      XMLReader reader = parserFactory.newSAXParser ().getXMLReader ();

      // Make sure that we can resolve any entities which refer to resources
      // in the class path.
      //entityResolver = new ResourceEntityResolver (entityResolver);
      reader.setEntityResolver (entityResolver);

      ProtectedFilter protectedFilter=new ProtectedFilter();

      protectedFilter.setParent(reader);
      filters [0].setParent (protectedFilter);

      Properties properties = lastTemplates.getOutputProperties ();

      // Set the system and public id for the doc type to the current values
      // used by the xml repository.
      properties.setProperty ("doctype-system",
                              XMLRepositoryConstants.CURRENT_DTD_SYSTEM_ID);
      properties.setProperty ("doctype-public",
                              XMLRepositoryConstants.CURRENT_DTD_PUBLIC_ID);

      Serializer serializer = SerializerFactory.getSerializer (properties);
      OutputStream outputStream = destXml.getOutputStream ();
      if (outputStream == null) {
        Writer writer = destXml.getWriter ();
        if (writer == null) {
          throw new IllegalArgumentException
            ("Destination does not have output stream, or writer");
        }
        serializer.setWriter (writer);
      } else {
        serializer.setOutputStream (outputStream);
      }

      lastFilter.setContentHandler (serializer.asContentHandler ());

      lastFilter.setEntityResolver (entityResolver);
      lastFilter.parse (srcXml);

      // Exit.
      System.exit (0);

    } catch ( Exception e) {
      System.err.print("\nAn unknown error occured whilst processing "+
                       "the transformation\n\n");
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * This method determines if a transformation is possible from the source XML
   * repository to the current revision and if so builds the list of XSLT 
   * resources in the correct order to pass to the transformer.
   *
   * @param fromRevision The revison of the repository to transform.
   * @return true if we can transform the repository to the new version.
   */
  public static boolean  buildTransformations(String fromRevision) {

    String toRevision=bundle.getString("revision");

    boolean canUpgrade=false;

    ArrayList path=new ArrayList();

    Enumeration e=bundle.getKeys();

    if ( !fromRevision.equals(toRevision) ) {
      while (e.hasMoreElements() && !canUpgrade) {
        String key=(String)e.nextElement();
        if ( key.startsWith("path.") ) {
          path.clear();
          String pathString=bundle.getString(key);
          StringTokenizer st=new StringTokenizer(pathString,"->");
          while (st.hasMoreTokens()) {
            String nextStep=(String)st.nextToken();
            if ( nextStep.equals(fromRevision) ) {
              System.out.print("\nUpgrading: "+fromRevision);
              canUpgrade=true;
            }
            if (canUpgrade) {
              //
              // Add the steps we nee to get from the old revision to
              // the new revision.
              //
              if ( !nextStep.equals(fromRevision) ) {
                System.out.print(" --> "+nextStep);
              }
              path.add(nextStep);
            }
          }
        }
      }
    }
    if (canUpgrade) {
      System.out.print("\n\n");
      ArrayList transformations=new ArrayList();
      //
      // We have a valid upgrade path, so now
      // all we have to do is build a list of transformations.
      //
      for (Iterator i=path.iterator(); i.hasNext(); ) {
        String revision=(String)i.next();
        if (!revision.equals(toRevision)) {
          String key="transformations."+revision;
          String transformationsString=bundle.getString(key);
          StringTokenizer st=new StringTokenizer(transformationsString,",");
          while (st.hasMoreTokens()) {
            String resourceKey="resource."+(String)st.nextToken();
            //System.out.println ("key: " + resourceKey
            //+ " value: " + bundle.getString(resourceKey));
            transformations.add(bundle.getString(resourceKey));
          }
        }
      }
      if (transformations.isEmpty()) {
        //
        // There are no transformations required for this upgrade
        // so we will just add a passthru transformation to copy
        // the input XML to the output and notify the user.
        transformations.add(defaultXsltResource);
        System.out.print("No Transformation required.\n\n");
        System.out.println("Copying source to destination.\n\n");
      }
      xsltResources=transformations.toArray();
    }
    return canUpgrade;
  }

  /**
   * The main entry point for running the transformation as a command line.
   */
  public static void main(String[] args) {
    if (args.length==2|| args.length==3) {
      try {
        if ( args.length==3 ) {
          //
          // Allow the user to overide the migrate30 properties supplied
          // by default.
          //
          bundle = BundleUtilities.getBundle(args[2]);
        }
        //
        // Attempt to find an upgrade path from the specified revision to the
        // current revision.
        //
        
        File input = new File(args[0]);
        if (!(input.exists())) {
          System.err.println("Source XML file not found.");
          System.exit(1);
        }
        
        FileInputStream fis = new FileInputStream(input);
        String version = null;
        
        try {
          SAXBuilder builder = new SAXBuilder(
              "com.volantis.xml.xerces.parsers.SAXParser", true);

          builder.setEntityResolver (new XMLRepositoryEntityResolver());

          Document document = builder.build (fis);
          DocType docType = document.getDocType ();

          // Check that a DTD was specified and is the correct one.
          String publicID = docType.getPublicID ();
          if (publicID == null
              || !publicID.startsWith (MARINER_DTD_PUBLIC_ID_PREFIX)) {
            System.err.print("\nThe specified source file does not appear to "
                           + "be a valid Mariner XML repository.\n\n" );
            System.exit(1);
          }

          // If the DTD is not the current one then we do not need to migrate30.
          if (publicID.equals (CURRENT_DTD_PUBLIC_ID)) {
            System.err.print("\nThe specified XML repository is up to date "
                             + "- migration is not required.\n\n");
            System.exit(1);
          }

          int start = MARINER_DTD_PUBLIC_ID_PREFIX.length ();
          int end = publicID.indexOf ("//", start);
          version = publicID.substring (start, end);
          
          System.out.println("\nSource repository revision: " + version);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("\nAn IO error occured whilst trying to determine "
                             + "the revision of your XML repository: "
                             + e.getLocalizedMessage() + "\n\n" );
            System.exit(1);

        } catch (JDOMException jdome) {
            jdome.printStackTrace();
            System.err.print("\nAn error occured whilst trying to determine "
                           + "the revision of your XML repository: "
                           + jdome.getLocalizedMessage() + "\n\n" );
            System.exit(1);
        }
        
        
        
        if ( buildTransformations(version) ) {
          Migrate instance=new Migrate(new InputSource(args[0]),
            new StreamResult(new FileOutputStream(args[1])));
        } else {
          System.err.print("\nNo upgrade path found for revision "+ version +
              "\n\n");
          System.exit(1);
        }
      } catch ( FileNotFoundException e) {
          e.printStackTrace();
        System.err.print("\nAn error occured whilst trying to "+
                       "open the XML repository " +e.getLocalizedMessage()+"\n\n");
        System.exit(1);
      }
    } else {
      System.err.print("\nUsage: migrate sourceXML destXML [propertiesFile]\n\n");
      System.exit(2);
    }
  }

  /**
   * This Extended XMLFilter protects the Entity Resolver that is setup
   * on the reader.
   */
  private class ProtectedFilter
     extends XMLFilterImpl {

    //javadoc inherited
    public void parse(InputSource source)
        throws SAXException, IOException {

      setupModifiedParse();
      this.getParent().parse(source);
    }
    
    /**
     * This method copies the DTDhandler, ContentHandler and ErrorHandler from
     * the parent filter.
     */
    private void setupModifiedParse() {
      if (this.getParent() == null) {
        throw new NullPointerException("No parent for filter");
      }
      this.getParent().setDTDHandler(this);
      this.getParent().setContentHandler(this);
      this.getParent().setErrorHandler(this);
    }

  }

}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Apr-05	7759/4	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	7665/3	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2114/1	geoff	VBM:2003112012 Migration: Themes: Translate old XML format to new XML format.

 06-Nov-03	1818/1	geoff	VBM:2003110604 Prevent third party code picking up our repackaged XML tools via JAXP.

 23-Jul-03	693/3	byron	VBM:2003070207 Versioning now handled via librarian v2

 22-Jul-03	693/1	byron	VBM:2003070207 Versioning now handled via librarian

 ===========================================================================
*/
