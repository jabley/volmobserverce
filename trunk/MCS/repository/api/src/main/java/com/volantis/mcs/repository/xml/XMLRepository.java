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
 * $Header: /src/voyager/com/volantis/mcs/repository/xml/XMLRepository.java,v 1.19 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 24-Oct-01    Paul            VBM:2001092608 - Made the backup file optional
 *                              and added an option to replace an existing
 *                              repository.
 * 14-Nov-01    Pether          VBM:2001111303 - In the createJDBCRepository
 *                              method, changed the SAXBuilder to explicit
 *                              specify which parser to use.
 * 19-Nov-01    Pether          VBM:2001111303 - Make sure to close the
 *                              outputstream before renaming the file in the
 *                              write() method.
 * 03-Nov-01    Doug            VBM:2001112003 - Modified the
 *                              createXMLRepository method so that the
 *                              Document could be created from a generic
 *                              InputStream as well as a File. Also modified
 *                              the write method so that the XML could be
 *                              written out to a generic OutputStream as well
 *                              as a File.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 10-Jan-02    Allan           VBM:2001121703 - Added getFile(). Renamed
 *                              FILE_NAME_PROPERTY to file-name to match the
 *                              config file xml attribute. Removed
 *                              BACKUP_FILE_NAME_PROPERTY. Added
 *                              BACKUP_FILE_SUFFIX.
 * 16-Jan-02    Allan           VBM:2001121703 - Added multiFile property.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 29-Apr-02    Adrian          VBM:2002040811 - Theme rewrite modifications.
 * 30-Apr-02    Ian             VBM:2002040811 - Changed PHOBOS_DTD* to
 *                              EUROPA_DTD* in createDocumentStream.
 * 14-Aug-02    Allan           VBM:2002072303 - Added lock() and unlock()
 *                              methods.
 * 16-Aug-02    Paul            VBM:2002081514 - Removed commented out code and
 *                              kludge code and throw an exception if the
 *                              versions do not match.
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.repository.xml;

import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.repository.DeprecatedRepository;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;

import java.util.Map;

/**
 * This class implements an XML <code>Repository</code>. The implementation
 * uses, and exposes in the extended API, JDOM objects representing the XML
 * data.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link LocalRepository} and {@link XMLRepositoryFactory}.
 *             This was deprecated in version 3.5.1.
 */
public final class XMLRepository
        extends DeprecatedRepository
        implements XMLRepositoryConstants {

    private static final XMLRepositoryFactory factory =
            XMLRepositoryFactory.getDefaultInstance();

    /**
     * Specifies the default root policy directory for the repository.
     */
    public static final String DEFAULT_PROJECT_DIRECTORY_PROPERTY = "directory";

    /**
     * Specifies the device repository file's name as an absolute filename or
     * as a file name relative to the default project directory.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-include-in ProfessionalServicesAPI
     * @volantis-api-include-in InternalAPI
     */
    public static final String DEVICE_REPOSITORY_PROPERTY =
            "deviceRepository";

    /**
     * The name of the replace file name property.
     */
    public static final String REPLACE_DIR_PROPERTY = "replaceDir";

    private XMLRepository(LocalRepository repository, Project defaultProject) {
        super(repository, defaultProject);
    }

    /**
     * A factory method that is used to create a new XMLRepository instance.
     * The specified <code>properties</code> <b>must</b> contain values for the
     * following properties (other properties are either optional or
     * deprecated):
     *
     * <dl><dt>None</dt></dl>
     *
     * @param properties configuration for the new repository
     * @return a new repository instance
     * @throws RepositoryException If there was a problem creating or accessing
     *                             the repository.
     */
    public static XMLRepository createRepository(Map properties)
            throws RepositoryException {

        // DEFAULT_PROJECT_DIRECTORY_PROPERTY should already have been
        // validated and be absolute. Only set by CL tools and tests.
        String defaultProjectDirName = (String) properties.get(
                DEFAULT_PROJECT_DIRECTORY_PROPERTY);

        LocalRepository repository = factory.createXMLRepository(null);

        // If necessary delete the default project directory.
        Boolean replaceFile = (Boolean) properties.get(REPLACE_DIR_PROPERTY);

        // Create the project.
        ProjectFactory projectFactory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration projectConfiguration = projectFactory.createProjectConfiguration();
        projectConfiguration.setRepository(repository);
        projectConfiguration.setPolicyLocation(defaultProjectDirName);
        projectConfiguration.setDeleteProjectContents(replaceFile != null
                && replaceFile.booleanValue());
        Project project = projectFactory.createProject(projectConfiguration);

        return new XMLRepository(repository, project);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/7	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/8	emma	VBM:2005021411 Modifications after review

 18-Feb-05	6974/6	emma	VBM:2005021411 Changing exception message to be more specific

 15-Feb-05	6974/4	emma	VBM:2005021411 Modifications after review

 15-Feb-05	6974/2	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/2	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 25-Feb-04	3136/6	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 24-Feb-04	3136/4	philws	VBM:2004021908 Introduce new runtime device repository usage

 19-Feb-04	2789/10	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/8	tony	VBM:2004012601 update localisation services

 16-Feb-04	2789/6	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 16-Feb-04	3023/4	philws	VBM:2004010901 Fix JDK 1.4/Eclipse XML API issue with JDOM SAXBuilder and the Volantisized Xerces parser

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 19-Jan-04	2642/1	geoff	VBM:2004011602 Import: Unable to import devices into the new JDBC repository (add much needed import parse error reporting and ignore .zip files in import directory)

 15-Jan-04	2595/5	andy	VBM:2004011404 commented out debug statement

 14-Jan-04	2573/12	andy	VBM:2003121907 changes to preserve public API

 13-Jan-04	2573/9	andy	VBM:2003121907 renamed file variables to directory

 13-Jan-04	2573/5	andy	VBM:2003121907 removed support for import/export with stdin/stdout

 13-Jan-04	2573/3	andy	VBM:2003121907 renamed methods to remove multi-file style name and removed single-file backupFile concept

 13-Jan-04	2573/1	andy	VBM:2003121907 removed remnants of single file support

 12-Jan-04	2360/11	andy	VBM:2003121710 supermerge

 12-Jan-04	2360/8	andy	VBM:2003121710 fixed conflict

 05-Jan-04	2360/5	andy	VBM:2003121710 added PROJECT column to all tables

 04-Jan-04	2360/2	andy	VBM:2003121710 added PROJECT column to all tables

 12-Jan-04	2326/9	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge)

 07-Jan-04	2326/6	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge and fix marinerComponentURI)

 05-Jan-04	2326/3	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors

 12-Jan-04	2304/8	tony	VBM:2003121708 fixed several accessor bugs and integrated migrate30/accessor translations

 02-Jan-04	2343/2	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 09-Jan-04	2343/5	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (import works now)

 02-Jan-04	2343/2	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 06-Jan-04	2409/1	andy	VBM:2004010509 completed -renamebad logic, fixed bugs in policy descriptors for layouts

 05-Jan-04	2398/1	andy	VBM:2004010506 documents are now validated after they are created

 04-Jan-04	2369/1	geoff	VBM:2004010403 Import/Export: XML Accessors: Implement namespace handling for jdom

 02-Jan-04	2346/1	andy	VBM:2003121705 implemented xsd validation framework

 02-Jan-04	2302/5	andy	VBM:2003121706 correct handling of Components, Themes, Layouts in policy descriptor creation

 02-Jan-04	2302/3	andy	VBM:2003121706 gui now works with new repository structure

 23-Dec-03	2252/4	andy	VBM:2003121703 change to default name for non existant repository in test suite

 23-Dec-03	2252/2	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 22-Jul-03	693/1	byron	VBM:2003070207 Versioning now handled via librarian

 ===========================================================================
*/
