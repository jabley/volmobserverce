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
 * $Header: /src/voyager/com/volantis/mcs/accessors/xml/XMLAccessorHelper.java,v 1.30 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 24-Oct-01    Paul            VBM:2001092608 - Replaced some for (;...;)
 *                              statements with while as they are simpler and
 *                              added retrieveAttributeElements method which
 *                              will optionally check to make sure that all the
 *                              elements have been read from the iterator.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 10-Jan-02    Allan           VBM:2001121703 - Overloaded getTopLevelElement
 *                              method to take an entity name parameter and
 *                              modified to try to find the top level element
 *                              in the entity if one is provided and the top
 *                              level element is not found in the root doc.
 *                              Added method getEntityRoot() to find the root
 *                              element of an external entity.
 * 22-Jan-02    Allan           VBM:2001121703 - Overloaded getSortedElement()
 *                              and findSortedElement() to work with multi-
 *                              file repositories. Renamed getEntityRoot() to
 *                              getExternalEntity() and updated for multi-
 *                              file repository use. Added getEntityFile(),
 *                              getMultiFileElementNames(), writeEntity(),
 *                              getMultiFileElements(), deleteEntityFile(),
 *                              renameEntityFile().
 * 28-Jan-02    Allan           VBM:2001121703 - Added rollbackEntity() and
 *                              removed built-in rollback from writeEntity().
 * 30-Jan-02    Mat             VBM:2002011410 - Added extra retrieve and add
 *                              ElementAttributes methods to allow them to
 *                              accept a SimpleAttributeContainer instead of
 *                              a map.
 * 30-Jan-02    Allan           VBM:2002013002 - Made the multi-file version
 *                              of getSortedElement() check that the element
 *                              does not already exist before it tries to
 *                              create it.
 * 06-Feb-02    Adrian          VBM:2002020507 - modified addAttributeElement
 *                              to stop outputing empty string values.
 * 06-Feb-02    Adrian          VBM:2002020507 - undid change above
 * 06-Feb-02    Paul            VBM:2001122103 - Added getElement method
 *                              similar to getSortedElement.
 * 22-Feb-02    Doug            VBM:2002011405 - Added revision processing
 *                              code to many of the methods.
 * 27-Feb-02    Doug            VBM:2002011405 - Modified the methods
 *                              retrieveAttributeElements() and
 *                              retrieveStructuredAttributeElements() as to
 *                              cope with a  Revision class interface change.
 * 04-Mar-02    Adrian          VBM:2002021908 - Modified method findElement
 *                              to throw RepositoryException.
 * 06-Mar-02    Allan           VBM:2002030617 - Updated getMultiFileElements()
 *                              to check that the element directory exists and
 *                              is a directory. If either case fails a warning
 *                              is logged. Also updated same method so that
 *                              anytime it finds no elements it returns an
 *                              empty collection instead of null.
 * 08-Mar-02    Paul            VBM:2002030804 - Added some logging which is
 *                              commented out for now.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 05-Apr-02    Mat             VBM:2002022009 - Added
 *                              (XMLRepositoryConnection)connection), also changed
 *                              any methods getting a XMLRepositoryConnection
 *                              to call the new method.
 * 30-Apr-02    Mat             VBM:2002040814 - Changed
 *                              (XMLRepositoryConnection)), to reflect
 *                              the new runtime repository.
 * 02-May-02    Doug            VBM:2002040803 - Ensured that the new Theme
 *                              accessors are used.
 * 02-May-02    Mat             VBM:2002040814 - Removed
 *                              getCorrectRepositoryConnection
 * 09-May-02    Adrian          VBM:2002040811 - update getMultiFileElementName
 *                              methods to only return those files that END in
 *                              .xml rather than have .xml in the name as
 *                              backup .xml-bak files were also being returned
 * 16-Aug-02    Paul            VBM:2002081514 - Removed unused kludge code.
 * 11-Dec-02    Allan           VBM:2002121124 - Use FormatMapper.getKeyString
 *                              in addAttributeElement(Element,String,
 *                              Object, String) to convert FormatTypes
 *                              to Strings correctly. This method changed
 *                              signature in that the 3rd param is was a
 *                              String and is now an Object. Calling methods
 *                              updated accordingly.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException
 *                              moved to Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors.xml.jdom;

import com.volantis.mcs.accessors.AccessorHelper;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * @todo This class needs to have a large number of methods thrown away and re-written.
 * @todo Anything to do with obtaining suffixes should use FileExtension instead.
 * @todo Suffixes should not be hard coded anywhere in this class.
 * @todo Remove all "legacy" related methods since this class does not need to support legacy code anymore
 * @todo Write testcases for more than just one method!
 */
public class XMLAccessorHelper
  extends AccessorHelper
  implements XMLAccessorConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(XMLAccessorHelper.class);

    /**
     * Delete the file for the named policy.
     *
     * @param file the file of the policy to delete
     * @return true if the file is deleted; otherwise false
     */
    public static boolean deletePolicyFile(File file) {

        if (!file.exists()) {

            logger.error("entity-file-non-existant",
                    new Object[]{file.getAbsolutePath()});
            return false;
        }

        boolean result = file.delete();

        // NOTE: we ignore potentially empty directories as we do not know if
        // we created the directory in the first place or the user did so.

        return result;
    }


    /**
     * Scans a virtual top-level directory and its subdirectories for files.
     *
     * @param directory     the project directory.
     * @param policyType the type of policy.
     * @return the policy names as a Collection
     */
    public static Collection getRepositoryPolicyNames(
            File directory, PolicyType policyType) {

        // Name scanning is used for enumerating policies which only works for
        // the default project.
        if (directory == null) {
            throw new IllegalStateException("XML Project Directory not set");
        }
        if (!directory.exists()) {
            logger.warn("directory-non-existant",
                    new Object[]{directory.getAbsolutePath()});
            return Collections.EMPTY_SET;
        }

        FileFilter filter = new PolicyTypeFilter(policyType);
        Set elements = new TreeSet();
        addRepositoryPolicyNames(directory, filter, null, elements);

        return elements;
    }

    /**
     * Scans a virtual top-level directory and its subdirectories for files.
     *
     * @param directory The directory.
     * @param filter The filter
     * @param path The path from the root of the XML repository.
     * @param set The set of paths
     */
    private static void addRepositoryPolicyNames(File directory,
                                                FileFilter filter,
                                                String path,
                                                Set set) {

        File files [] = directory.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                String newPath = null;
                if (path == null) {
                    newPath = "/" + file.getName();
                } else {
                    newPath = path + "/" + file.getName();
                }

                addRepositoryPolicyNames(file, filter, newPath, set);
            } else {

                String add;
                if (path == null) {
                    add = "/" + file.getName();
                } else {
                    add = path + "/" + file.getName();
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding " + add);
                }
                set.add(add);
            }
        }
    }

    private static class PolicyTypeFilter
            implements FileFilter {

        private final PolicyType policyType;

        public PolicyTypeFilter(PolicyType policyType) {
            this.policyType = policyType;
        }

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }

            String name = file.getName();
            int index = name.lastIndexOf('.');
            if (index == -1) {
                return false;
            } else {
                FileExtension extension =
                        FileExtension.getFileExtensionForLocalPolicy(name);
                return (extension != null &&
                        extension.getPolicyType() == policyType);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 28-Apr-05	7908/1	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6670/2	adrianj	VBM:2005010506 Implementation of resource asset continued

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 15-Oct-04	5794/1	geoff	VBM:2004100801 MCS Import slow

 11-Jun-04	4678/2	geoff	VBM:2004061001 old gui cleanup: remove folder support code

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/8	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/6	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/4	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04	2962/4	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 29-Jan-04	2749/5	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project (supermerge of tonys cleanup changes)

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 28-Jan-04	2754/2	tony	VBM:2004011205 javadocs for NameTable and assoicated classes, move methods into JDOMXMLDeviceLayoutAccessor

 22-Jan-04	2696/3	geoff	VBM:2004012101 Import/Export: XML: Layouts: do attribute value translations per attribute

 22-Jan-04	2696/1	geoff	VBM:2004012101 Import/Export: XML: Layouts: do attribute value translations per attribute

 20-Jan-04	2677/1	geoff	VBM:2004011918 Migration: the option -renamebad for themes not working also not part of desc

 19-Jan-04	2662/5	geoff	VBM:2004011609 JDBC repositories are storing different slashes in front of names

 19-Jan-04	2662/3	geoff	VBM:2004011609 JDBC repositories are storing different slashes in front of names

 19-Jan-04	2657/1	geoff	VBM:2004011607 Import/Export: round trip from migrate30 files loses assets

 19-Jan-04	2642/3	geoff	VBM:2004011602 Import: Unable to import devices into the new JDBC repository (add much needed import parse error reporting and ignore .zip files in import directory)

 16-Jan-04	2642/1	geoff	VBM:2004011602 Import: Unable to import devices into the new JDBC repository

 15-Jan-04	2595/5	andy	VBM:2004011404 changed internal representation of policy names and partially implemented legacy mode to support old gui

 14-Jan-04	2573/6	andy	VBM:2003121907 changes to preserve public API

 13-Jan-04	2573/4	andy	VBM:2003121907 renamed file variables to directory

 13-Jan-04	2573/2	andy	VBM:2003121907 renamed methods to remove multi-file style name and removed single-file backupFile concept

 13-Jan-04	2570/1	andy	VBM:2004010202 removed redundant code and added documentation to test case

 12-Jan-04	2360/12	andy	VBM:2003121710 supermerge

 12-Jan-04	2360/9	andy	VBM:2003121710 fixed conflict

 05-Jan-04	2360/6	andy	VBM:2003121710 added PROJECT column to all tables

 04-Jan-04	2360/3	andy	VBM:2003121710 added PROJECT column to all tables

 12-Jan-04	2326/10	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge)

 07-Jan-04	2326/7	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge and fix marinerComponentURI)

 05-Jan-04	2326/4	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors

 12-Jan-04	2343/10	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (remove now unneccessary ignored xml attribute hack)

 12-Jan-04	2304/11	tony	VBM:2003121708 fixed several accessor bugs and integrated migrate30/accessor translations

 02-Jan-04	2343/2	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 09-Jan-04	2343/7	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (fix grid bug and namespace declaration)

 09-Jan-04	2343/5	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (import works now)

 02-Jan-04	2343/2	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 12-Jan-04	2532/5	andy	VBM:2004010903 removed redundant commented out code

 09-Jan-04	2532/1	andy	VBM:2004010903 hacks to get asset groups exporting with xsd validation

 06-Jan-04	2362/4	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 06-Jan-04	2362/2	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 05-Jan-04	2398/2	andy	VBM:2004010506 documents are now validated after they are created

 05-Jan-04	2382/1	andy	VBM:2004010501 fixed pathname bug in recursive directory scanning logic

 04-Jan-04	2369/1	geoff	VBM:2004010403 Import/Export: XML Accessors: Implement namespace handling for jdom

 02-Jan-04	2346/3	andy	VBM:2003121705 most component types are now using XSD validation

 02-Jan-04	2346/1	andy	VBM:2003121705 implemented xsd validation framework

 02-Jan-04	2302/8	andy	VBM:2003121706 correct handling of Components, Themes, Layouts in policy descriptor creation

 02-Jan-04	2302/6	andy	VBM:2003121706 gui now works with new repository structure

 23-Dec-03	2252/6	andy	VBM:2003121703 change to default name for non existant repository in test suite

 23-Dec-03	2252/4	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/2	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 ===========================================================================
*/
