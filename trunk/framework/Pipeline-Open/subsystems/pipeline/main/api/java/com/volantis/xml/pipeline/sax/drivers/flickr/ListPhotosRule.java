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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.flickr;

import java.text.MessageFormat;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.drivers.uri.Fetcher;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipeline;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
 * A FetchAdapterProcess is an AdapterProcess that includes another
 * Document that is specified via a URL.
 *
 * <p>This is used by DSB.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class ListPhotosRule
        extends DynamicElementRuleImpl {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new ListPhotosRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(ListPhotosRule.class);

    /**
     * Identifier for the flickr rest api url
     */
    private static final String FLICKR_REST_API_URL = "http://api.flickr.com/services/rest/";

    /**
     * Identifier for the flickr api photos method
     */
    private static final String FLICKR_API_PHOTOS_METHOD = "method=flickr.photos.search";

    /**
     * Identifier for the flickr api photosets method
     */
    private static final String FLICKR_API_PHOTOSETS_METHOD = "method=flickr.photosets.getPhotos";
    
    /**
     * Identifier for the flickr api kay parameter
     */
    private static final String FLICKR_API_KEY_PARAM = "api_key={0}";

    /**
     * Identifier for the flickr user id parameter
     */
    private static final String FLICKR_USER_ID_PARAM = "user_id={0}";

    /**
     * Identifier for the flickr photoset id parameter
     */
    private static final String FLICKR_PHOTOSET_ID_PARAM = "photoset_id={0}";

    /**
     * Identifier for the flickr tags parameter
     */
    private static final String FLICKR_TAGS_PARAM = "tags={0}";

    /**
     * Identifier for the flickr tag mode parameter with fixed value
     * to enforce 'AND' tags combination) 
     */
    private static final String FLICKR_TAG_MODE_PARAM = "tag_mode=all";

    /**
     * Identifier for the flickr text parameter
     */
    private static final String FLICKR_TEXT_PARAM = "text={0}";

    /**
     * Identifier for the flickr per page parameter
     */
    private static final String FLICKR_PER_PAGE_PARAM = "per_page={0}";

    /**
     * Identifier for the flickr page parameter
     */
    private static final String FLICKR_PAGE_PARAM = "page={0}";

    /**
     * Identifier for the flickr extras parameter with fixed value
     * for currently supported fields to fetch extra information
     */
    private static final String FLICKR_EXTRAS_PARAM =
            "extras=license,date_upload,date_taken,owner_name,icon_server," +
            "original_format,last_update,geo,tags,machine_tags,o_dims,views,media";

    /**
     * Identifier for the api key attribute
     */
    private static final String API_KEY_ATTRIBUTE = "api-key";

    /**
     * Identifier for the user id attribute
     */
    private static final String USER_ID_ATTRIBUTE = "user-id";

    /**
     * Identifier for the photoset id attribute
     */
    private static final String PHOTOSET_ID_ATTRIBUTE = "photoset-id";

    /**
     * Identifier for the tags attribute
     */
    private static final String TAGS_ATTRIBUTE = "tags";

    /**
     * Identifier for the query attribute
     */
    private static final String QUERY_ATTRIBUTE = "query";

    /**
     * Identifier for the page-size attribute
     */
    private static final String PAGE_SIZE_ATTRIBUTE = "page-size";

    /**
     * Identifier for the page-index attribute
     */
    private static final String PAGE_INDEX_ATTRIBUTE = "page-index";

    /**
     * Identifier for the page-size attribute default value
     */
    private static final String DEFAULT_PAGE_SIZE = "10";

    /**
     * Identifier for the page-index attribute default value
     */
    private static final String DEFAULT_PAGE_INDEX = "1";
    
    /**
     * Identifier for the parse attribute default value
     */
    private static final String PARSE_ATTRIBUTE_VALUE = "xml";

    /**
     * Identifier for the encoding attribute default value
     */
    private static final String ENCODING_ATTRIBUTE_VALUE = "UTF-8";

    /**
     * Identifier for the timeout attribute default value
     */
    private static final String TIMEOUT_ATTRIBUTE_VALUE = "-1";

    /**
     * Creates a new ListPhotosRule
     */
    public ListPhotosRule() {
    }

    // Javadoc inherited
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLPipeline pipeline = dynamicProcess.getPipeline();

        StringBuffer queryString = new StringBuffer();         

        String photosetId = attributes.getValue(PHOTOSET_ID_ATTRIBUTE);
        String userId = attributes.getValue(USER_ID_ATTRIBUTE);
        String tags = attributes.getValue(TAGS_ATTRIBUTE);
        String query = attributes.getValue(QUERY_ATTRIBUTE);

        if (photosetId != null) {
            if (userId != null || tags != null || query != null) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "not allowed to use any of the attributes: 'user-id', 'tags', 'query' " +
                                "when photoset-id is specified",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            queryString.append("?").append(FLICKR_API_PHOTOSETS_METHOD);
            queryString.append("&")
                           .append(MessageFormat.format(
                                FLICKR_PHOTOSET_ID_PARAM, photosetId));
        } else {
            queryString.append("?").append(FLICKR_API_PHOTOS_METHOD);

            if (userId != null) {
                queryString.append("&")
                           .append(MessageFormat.format(
                                FLICKR_USER_ID_PARAM, userId));
            } else if (tags == null && query == null) {
                // parameterless queries are not supported
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "at least one of the attributes: 'user-id', 'tags', 'query' should be specified",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }

            if (tags != null && query != null) {
                // in order not to produce misleading results (as flickr ignores tags param
                // when query/text param is set) it is considered invalid attributes
                // combination
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "not allowed to specify both 'tags' and 'query' attributes",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }

            if (tags != null) {
                // convert space separated list to comma separated list
                String commaSeparatedTags = tags.trim().replaceAll("\\s+", ",");
                queryString.append("&")
                           .append(MessageFormat.format(
                               FLICKR_TAGS_PARAM, commaSeparatedTags))
                           .append("&")
                           .append(FLICKR_TAG_MODE_PARAM);
            }

            if (query != null) {
                queryString.append("&")
                           .append(MessageFormat.format(
                               FLICKR_TEXT_PARAM, query));
            }

        }

        if (attributes.getValue(API_KEY_ATTRIBUTE) != null) {
            queryString.append("&")
                       .append(MessageFormat.format(
                            FLICKR_API_KEY_PARAM,
                            attributes.getValue(API_KEY_ATTRIBUTE)));
        } else {
            Locator locator = pipeline.getPipelineContext().getCurrentLocator();
            XMLPipelineException e =
                    new XMLPipelineException(
                            "api-key attribute should be specified",
                            locator);
            getTargetProcess(pipeline).fatalError(e);
            return null;
        }

        queryString.append("&").append(FLICKR_EXTRAS_PARAM);

        // pagination parameters
        String pageSize = attributes.getValue(PAGE_SIZE_ATTRIBUTE);
        String pageIndex = attributes.getValue(PAGE_INDEX_ATTRIBUTE);

        if (pageSize != null) {
            Integer pageSizeInt = null;
            try {
                pageSizeInt = Integer.parseInt(pageSize);
            } catch(NumberFormatException nfe) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "page-size attribute should be of integer value",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }

            if (pageSizeInt < 1 || pageSizeInt > 500) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "page-size attribute value should be in the range of [1,500]",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            pageSize = pageSizeInt.toString();
        } else {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        queryString.append("&")
                .append(MessageFormat.format(
                    FLICKR_PER_PAGE_PARAM, pageSize));

        if (pageIndex != null) {
            Integer pageIndexInt;
            try {
                pageIndexInt = Integer.parseInt(pageIndex);
            } catch(NumberFormatException nfe) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "page-index attribute should be of integer value",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }

            if (pageIndexInt < 1) {
                Locator locator = pipeline.getPipelineContext().getCurrentLocator();
                XMLPipelineException e =
                        new XMLPipelineException(
                                "page-index attribute value should be in the range of [1,...]",
                                locator);
                getTargetProcess(pipeline).fatalError(e);
                return null;
            }
            pageIndex = pageIndexInt.toString();

        } else {
            pageIndex = DEFAULT_PAGE_INDEX;
        }

        queryString.append("&").append(MessageFormat.format(
                FLICKR_PAGE_PARAM, pageIndex));

        Fetcher operation = new Fetcher(pipeline);

        // OK to pass null strings to operations attribute setters
        // Operation process can validate the attributes whenever
        // it performs its operation
        operation.setHref(FLICKR_REST_API_URL + queryString.toString());
        operation.setParse(PARSE_ATTRIBUTE_VALUE);
        operation.setEncoding(ENCODING_ATTRIBUTE_VALUE);

        // Handle the timeout setting. Firstly, we need to see if there is a
        // timeout attribute. We default to -1 to indicate no timeout
        String timeoutAttr = TIMEOUT_ATTRIBUTE_VALUE;
        Period timeout = null;

        if (timeoutAttr != null) {
            // The attribute should contain an integer number of seconds
            int timeoutInSecs;
            try {
                timeoutInSecs = Integer.parseInt(timeoutAttr);
            } catch (NumberFormatException e) {
                LOGGER.warn("invalid-timeout", timeoutAttr);
                throw e;
            }

            timeout = Period.treatNonPositiveAsIndefinitely(
                    timeoutInSecs * 1000);
        }

        operation.setTimeout(timeout);

        operation.doInclude();

        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}