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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.search.devices;

import java.text.MessageFormat;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.search.SearchMessages;
import com.volantis.mcs.eclipse.ab.search.SearchScope;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.mcs.utilities.StringUtils;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

/**
 * A search query that searches for references to a device in set of resources
 * that can include device references (including device repositories).
 */
public class DeviceSearchQuery implements ISearchQuery {

    /**
     * The Logger for this class.
     */
    private static final Logger logger =
            Logger.getLogger(DeviceSearchQuery.class);

    /**
     * The prefix for resources messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "DeviceSearchQuery.";

    /**
     * The singular label for this query.
     */
    private static final MessageFormat SINGULAR_LABEL_FORMAT =
            new MessageFormat(SearchMessages.getString(RESOURCE_PREFIX +
            "label.singular"));

    /**
     * The multiple label for this query.
     */
    private static final MessageFormat MULTIPLE_LABEL_FORMAT =
            new MessageFormat(SearchMessages.getString(RESOURCE_PREFIX +
            "label.multiple"));

    /**
     * The progress monitor task name.
     */
    private static final String TASK_NAME =
            SearchMessages.getString(RESOURCE_PREFIX +
            "progressMonitor.taskName");

    /**
     * The scope of the query i.e. the Collection of resources that are
     * to be the subject of the search.
     */
    private final SearchScope scope;

    /**
     * The search string.
     */
    private final String searchString;

    /**
     * The String representation of the regular expression to search for.
     */
    private String regExpString;

    /**
     * The DeviceSearchQueryOptions associated with this DeviceSearchQuery.
     */
    private final DeviceSearchQueryOptions options;

    /**
     * The ISearchResult associated with this ISearchQuery.
     */
    private DeviceSearchResult searchResult = new DeviceSearchResult() {
        public ISearchQuery getQuery() {
            return DeviceSearchQuery.this;
        }
    };

    /**
     * Construct a new DeviceSearchQuery.
     * @param scope the SearchScope designating the resources that are to be
     * searched
     * @param searchString the String to search for
     * @param options the DeviceSearchQueryOptions that configure the query.
     */
    public DeviceSearchQuery(SearchScope scope, String searchString,
                             DeviceSearchQueryOptions options) {

        assert(scope != null);
        assert(searchString != null);
        assert(options != null);

        this.scope = scope;
        this.searchString = searchString;
        this.options = options;
    }


    // javadoc inherited
    public IStatus run(IProgressMonitor progressMonitor) {
        IFile files [] = scope.getFiles();


        if (!options.isRegularExpression()) {
            // To use the same process for the search we convert the
            // searchString into a regular expression.
            regExpString = createRegExpString();
        } else {
            this.regExpString = searchString;
        }

        if (logger.isDebugEnabled()) {
            String message = "Search Started. searchString=\"" + regExpString +
                    "\", scope size=" + files.length;
            logger.debug(message);
        }

        IStatus status = Status.OK_STATUS;

        progressMonitor.beginTask(TASK_NAME, files.length);
        boolean isCancelled = false;
        for (int i = 0; i < files.length && !isCancelled; i++) {
            FileExtension extension =
                    FileExtension.getFileExtensionForExtension(files[i].
                    getFileExtension());
            if (extension == FileExtension.DEVICE_REPOSITORY) {
                searchDeviceRepository(files[i]);
            } else {
                throw new IllegalArgumentException("Searching of " +
                        extension + "files not supported");
            }
            progressMonitor.worked(1);
            isCancelled = progressMonitor.isCanceled();
            if (isCancelled) {
                status = Status.CANCEL_STATUS;
            }
        }

        return status;
    }

    /**
     * Create a regular expression string from the searchString and
     * return the result. * and ? chars are preceded with a . provided they
     * are not preceded by a \ and case sensitivity is also handled.
     * @return the regular expression String of searchString
     */
    private String createRegExpString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('^');
        for (int i = 0; i < searchString.length(); i++) {
            char c = searchString.charAt(i);
            switch (c) {
                case '\\':
                    buffer.append(searchString.charAt(i + 1));
                    i++;
                    break;
                case '*':
                case '?':
                    buffer.append(".").append(c);
                    break;
                default:
                    if (Character.isLetter(c) && !options.isCaseSensitive()) {
                        buffer.append('[').append(Character.toUpperCase(c))
                              .append(StringUtils.toLowerIgnoreLocale(
                                      String.valueOf(c)))
                               .append(']');

                    } else {
                        buffer.append(c);
                    }
                    break;
            }
        }
        buffer.append('$');
        return buffer.toString();
    }

    // javadoc inherited
    public String getLabel() {
        Object args [] = {
            searchString,
            new Integer(searchResult.getMatchCount()),
            scope.getLabel()
        };

        return searchResult.getMatchCount() == 1 ?
                SINGULAR_LABEL_FORMAT.format(args) :
                MULTIPLE_LABEL_FORMAT.format(args);
    }

    // javadoc inherited
    public boolean canRerun() {
        return true;
    }

    // javadoc inherited
    public boolean canRunInBackground() {
        return true;
    }

    // javadoc inherited
    public ISearchResult getSearchResult() {
        return searchResult;
    }

    /**
     * Perform a search on the specified device repository. Devices that
     * match the regExpString will be used to create new DeviceSearchMatches
     * that are in turn added to the searchResult.
     * @param repositoryFile the device repository file to search
     */
    private void searchDeviceRepository(IFile repositoryFile) {
        String fileName = repositoryFile.getLocation().toOSString();
        TransformerMetaFactory transformerFactory =
                new JAXPTransformerMetaFactory();
        try {
            RE re = new RE(regExpString);
            String selectedDevices [];

            if (options.isDeviceNameSearch()) {
                selectedDevices = DeviceRepositoryAccessorManager.
                        selectHierarchyDevices(fileName,
                        transformerFactory, new ODOMFactory(), re);
                for (int i = 0; i < selectedDevices.length; i++) {
                    DeviceSearchMatch match =
                            new DeviceSearchMatch(repositoryFile,
                            selectedDevices[i]);
                    searchResult.addMatch(match);
                }
            }
            if (options.isDevicePatternSearch()) {
                selectedDevices =
                        DeviceRepositoryAccessorManager.
                        selectIdentityDevices(fileName,
                                transformerFactory, new ODOMFactory(), re);
                for (int i = 0; i < selectedDevices.length; i++) {
                    DeviceSearchMatch match =
                            new DeviceSearchMatch(repositoryFile,
                            selectedDevices[i]);
                    searchResult.addMatch(match);
                }
            }
        } catch (RepositoryException e) {
            EclipseCommonPlugin.logError(ABPlugin.getDefault(), getClass(), e);
        } catch (RESyntaxException e) {
            EclipseCommonPlugin.logError(ABPlugin.getDefault(), getClass(), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/4	allan	VBM:2004070608 Device search

 ===========================================================================
*/
