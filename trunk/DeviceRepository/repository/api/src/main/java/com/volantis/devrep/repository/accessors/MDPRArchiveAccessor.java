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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.accessors;

import com.volantis.mcs.accessors.xml.ZipArchive;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Accessor class for reading information from an MDPR archive. Capable of
 * checking whether the MDPR file is compatible with the current release, and
 * if necessary upgrading it so that it is.
 */
public class MDPRArchiveAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MDPRArchiveAccessor.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MDPRArchiveAccessor.class);

    /**
     * The version identifier for version 3.0 of the MDPR file, which has two
     * distinct forms with the same version number, and thus needs to be
     * treated as a special case.
     */
    private static final String VERSION_3_0 = "3.0";

    /**
     * The name of the file used to store the TAC identification details in
     * version 3.0 - duplicated here so that it's preserved if the structure
     * changes in the future.
     */
    private static final String TAC_IDENTIFICATION_FILE_3_0 =
            "tac-identification.xml";

    /**
     * The name of the file used to store the hierarchy details in
     * version 3.0 - duplicated here so that it's preserved if the structure
     * changes in the future.
     */
    private static final String HIERARCHY_FILE_3_0 =
            "hierarchy.xml";

    /**
     * The factory for creating XSL transformers. This is used to create
     * the transformations for upgrading the TAC file. Will be null if should
     * not do transformations; this is only valid when used in a runtime
     * context.
     */
    private TransformerFactory transformerFactory;

    /**
     * The filename of the MDPR file being accessed through this instance.
     */
    private String archiveFileName;

    /**
     * <P>Creates a new accessor for the specified MDPR filename. At this point
     * a check is carried out to ensure that the file exists, but no
     * validation of the content is carried out until we need to know about
     * the compatibility of the file or access its contents.</P>
     * <P>If the file name is null or references a file that does not exist,
     * an IllegalArgumentException is generated.</P>
     *
     * @param fileName The name of an MDPR file to access
     * @param transformerMetaFactory The meta factory we use for creating
     *      XSL transformers.
     */
    public MDPRArchiveAccessor(String fileName,
            TransformerMetaFactory transformerMetaFactory) {

        if (fileName == null) {
            throw new IllegalArgumentException("File name should not be null");
        } else {
            File f = new File(fileName);
            if (!f.exists()) {
                throw new IllegalArgumentException("File '" + fileName +
                        "'does not exist.");
            }
        }
        archiveFileName = fileName;

        // Create a transformer factory. If the result is null, we assume that
        // the client did not want to do any transformations. This can validly
        // be the case when being used in a runtime context. In this case,
        // we don't need to do upgrades, as only the GUI requires the upgrade.
        this.transformerFactory =
                transformerMetaFactory.createTransformerFactory();

        if (logger.isDebugEnabled() && transformerFactory == null) {
            logger.debug("No transformer factory available; auto-upgrading " +
                    "disabled");
        }
    }

    /**
     * Get the name of the file associated with this MDPRArchiveAccessor.
     * @return the name of the archive file
     */
    public String getArchiveFileName() {
        return archiveFileName;
    }

    /**
     * <P>Checks whether the format of the MDPR file represented by this
     * accessor is compatible with the current version of the codebase,
     * either directly or through potential transformations that can be
     * carried out.</P>
     * <P>Note that this method does not carry out any validation on the
     * structure or content of the device repository - it relies on an
     * accurate version number provided in the version.txt file.</P>
     *
     * @return True if the MDPR file associated with this accessor can be
     *         read by the current version of the code
     * @throws RepositoryException if an error occurs reading the repository
     *                             file
     */
    public boolean isCompatible() throws RepositoryException {
        // Currently we can rely on the version number, since the only
        // available upgrade is between the two different version 3.0 file
        // formats. Future implementations will have to know about upgrade
        // paths between different version numbers, probably through a
        // migration API.
        String requiredVersion = EclipseDeviceRepository.
                getRequiredMDPRVersion();
        String mdprVersion =
                EclipseDeviceRepository.getRepositoryVersion(
                        new File(archiveFileName));
        boolean compatible = false;
        if (requiredVersion.equals(mdprVersion)) {
            compatible = true;
        }
        return compatible;
    }

    /**
     * <P>Checks whether modification to the device repository is required
     * at load-time in order to provide compatibility with the current
     * version of the code.</P>
     * <P>If this method returns true, then saving the ZipArchive generated
     * by {@link #getArchive} will result in the device repository version
     * being updated, which may lead to the file becoming incompatible with
     * earlier releases.</P>
     *
     * @return True if the ZipArchive returned by {@link #getArchive} will
     *         be modified
     * @throws RepositoryException if the device repository format is not
     *                             compatible, or if an error occurs reading
     *                             the device repository.
     */
    public boolean willBeModifiedOnLoad() throws RepositoryException {
        if (!isCompatible()) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "device-repository-incompatible"));
        }
        boolean willBeModified = false;
        if (VERSION_3_0.equals(
                EclipseDeviceRepository.getRequiredMDPRVersion())) {
            if (logger.isDebugEnabled()) {
                logger.debug("modified: version 3.0");
            }
            ZipFile zf = null;
            try {
                zf = new ZipFile(archiveFileName);
                ZipEntry ze = zf.getEntry(TAC_IDENTIFICATION_FILE_3_0);
                if (ze == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("modified: no tac id file");
                    }
                    if (transformerFactory != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("modified: has transformer factory");
                        }
                        willBeModified = true;
                    }
                }
            } catch (IOException ioe) {
                throw new RepositoryException(ioe);
            } finally {
                if (zf != null) {
                    try {
                        zf.close();
                    } catch (IOException ioe) {
                        throw new RepositoryException(ioe);
                    }
                }
            }
        }
        return willBeModified;
    }

    /**
     * <P>Retrieves a {@link com.volantis.mcs.accessors.xml.ZipArchive} representing the MDPR associated with
     * this accessor in a form that is compatible with the current version of
     * the code (ie. with any necessary upgrades applied). The device
     * repository is not saved at this stage, so the version on disk will be
     * left unmodified until the {@link com.volantis.mcs.accessors.xml.ZipArchive#save} method is called.</P>
     * <P>This method could potentially be extremely slow, depending on the
     * number and type of operations required by the upgrade, so where possible
     * the number of calls made to it should be minimised.</P>
     * <P>Note that each time this method is called, a fresh ZipArchive
     * instance is created, and that any changes made to one ZipArchive will
     * <EM>not</EM> be reflected in the state of another.</P>
     *
     * @return A ZipArchive representing the upgraded version of the MDPR
     *         file associated with this accessor.
     * @throws RepositoryException if an error occurs reading or upgrading
     *                             the repository.
     */
    public ZipArchive getArchive() throws RepositoryException {
        try {
            ZipArchive archive = new ZipArchive(archiveFileName);
            if (willBeModifiedOnLoad()) {
                try {
                    InputStream styleSheetInput = MDPRArchiveAccessor.class.
                            getResourceAsStream("MDPRTACIdentification.xsl");
                    InputStream hierarchyInput =
                            archive.getInputFrom(HIERARCHY_FILE_3_0);
                    Source stylesheet = new StreamSource(styleSheetInput);
                    Source hierarchy = new StreamSource(hierarchyInput);
                    // NOTE: transformer factory will be guaranteed to be
                    // non-null here since we have already called
                    // willBeModifiedOnLoad...
                    Transformer transformer = transformerFactory.
                            newTransformer(stylesheet);
                    OutputStream tacOutput = archive.getOutputTo(
                            TAC_IDENTIFICATION_FILE_3_0);
                    transformer.transform(hierarchy,
                            new StreamResult(tacOutput));
                    tacOutput.flush();
                    tacOutput.close();
                } catch (TransformerException te) {
                    throw new RepositoryException(te);
                }
            }
            return archive;
        } catch (IOException ioe) {
            throw new RepositoryException(ioe);
        }
    }

    /**
     * <P>Retrieves an {@link InputStream} which reads from a specified file
     * within the device repository file, with any necessary compatibility
     * upgrades applied.</P>
     * <P>The implementation is expected to only apply those upgrades that
     * are required for the specified file to be retrieved, which may
     * require a careful check of how the upgrade alters individual files,
     * and what the chain leading to a particular file across multiple version
     * updates is. This functionality should be included in any future
     * migration API, and be accessed by the MDPRArchiveAccessor.</P>
     * <P>This method could potentially be extremely slow, depending on whether
     * the particular file being accessed requires upgrades to the archive and
     * the number and type of operations required by the upgrade, so where
     * possible the number of calls made to it should be minimised.</P>
     *
     * @param fileName The name of the file within the device repository
     *                 to retrieve.
     * @return An InputStream reading from that file, with any necessary
     *         upgrades applied or null if that file does not exist.
     * @throws RepositoryException if an error occurs reading from or upgrading
     *                             the device repository.
     */
    public InputStream getArchiveEntryInputStream(String fileName)
            throws RepositoryException {
        ZipArchive za = null;
        try {
            if (TAC_IDENTIFICATION_FILE_3_0.equals(fileName) &&
                    willBeModifiedOnLoad()) {
                za = getArchive();
            } else {
                za = new ZipArchive(archiveFileName);
            }

            return za.getInputFrom(fileName);
        } catch (IOException ioe) {
            throw new RepositoryException(ioe);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 25-Aug-04	5298/1	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 16-Aug-04	5206/2	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 12-Aug-04	5167/2	adrianj	VBM:2004081107 Created MDPRArchiveAccessor for reading device repository

 ===========================================================================
*/
