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
 * $Header: /src/voyager/com/volantis/mcs/papi/MetaElement.java,v 1.6 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false 
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.MetaAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.TimedRefreshInfo;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.shared.net.http.HttpDateParser;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Date;
import java.net.URISyntaxException;

/**
 * The meta element.
 */
public class MetaElementImpl
        extends AbstractExprElementImpl {

    public static final String CACHE_AUTO = "mcs-cache-auto";

    public static final String NO_CACHE = "mcs-no-cache";

    public static final String CACHE_MAX_AGE = "mcs-cache-max-age";

    public static final String CACHE_EXPIRES = "mcs-cache-expires";

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(MetaElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.MetaAttributes pattributes;

    /**
     * Create a new <code>MetaElement</code>.
     */
    public MetaElementImpl() {
        pattributes = new com.volantis.mcs.protocols.MetaAttributes();
    }

    /**
     * Javadoc inherited from super class.
     * se
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        VolantisProtocol protocol = pageContext.getProtocol();

        MetaAttributes attributes = (MetaAttributes) papiAttributes;
        // Copy the attributes.
        pattributes.setName(attributes.getName());
        pattributes.setHttpEquiv(attributes.getHttpEquiv());
        pattributes.setLang(attributes.getLang());
        pattributes.setContent(attributes.getContent());

        String name = pattributes.getName();

        if (null != name) {
            if (name.equals("mcs-" + TimedRefreshInfo.NAME)) {
                try {
                    TimedRefreshInfo tri =
                            new TimedRefreshInfo(attributes.getContent());
                    protocol.writeTimedRefresh(tri);
                } catch (NumberFormatException nfe) {
                    throw new PAPIException(EXCEPTION_LOCALIZER.format(
                            "invalid-attribute", new Object[]{
                            "mcs-" + TimedRefreshInfo.NAME,
                            pattributes.getContent()}), nfe);
                } catch (ProtocolException e) {
                    throw new PAPIException(EXCEPTION_LOCALIZER.format(
                            "invalid-attribute", new Object[]{
                            "mcs-" + TimedRefreshInfo.NAME,
                            pattributes.getContent()}), e);
                }
            } else if (name.equals(CACHE_AUTO)) {
                applyCacheAuto(context);
            } else if (name.equals(NO_CACHE)) {
                applyNoCache(context);
            } else if (name.equals(CACHE_MAX_AGE)) {
                applyCacheMaxAge(context, attributes.getContent());
            } else if (name.equals(CACHE_EXPIRES)) {
                applyCacheExpires(context, attributes.getContent());
            } else {
                protocol.writeMeta(pattributes);
            }
        } else {
            protocol.writeMeta(pattributes);
        }

        return SKIP_ELEMENT_BODY;
    }

    /**
     * Apply Cache Expiry behaviour hints, allowing output headers to be set for cache-control.
     *
     * @param context The MarinerRequestContext within which this meta element is
     *                being processed.
     * @param content The String representation of the cache-expiry duration.
     * @throws PAPIException If there was a problem setting the cache expiry.
     */
    private void applyCacheExpires(
            MarinerRequestContext context, String content)
            throws PAPIException {
        final ResponseCachingDirectives cachingDirectives =
                getCachingDirectives(context);

        if (cachingDirectives != null) {
            // Determine cache expiry as Time
            try {
                Date httpDate = HttpDateParser.parse(content);
                if (httpDate == null) {
                    // If string didn't parse to long property, throw an exception
                    // reusing existing localised message, saying that Time was
                    // expected, but String was encountered.
                    throw new PAPIException(EXCEPTION_LOCALIZER.format(
                            "invalid-meta-content-type", new Object[]{
                            Time.class.getName(), String.class.getName()}));
                }

                Time expires = Time.inMilliSeconds(httpDate.getTime());

                cachingDirectives.setExpires(expires,
                        ResponseCachingDirectives.PRIORITY_HIGH);
                cachingDirectives.enable();
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * Apply Cache Max Age behaviour hints, allowing output headers to be set for cache-control.
     *
     * @param context The MarinerRequestContext within which this meta element is
     *                being processed.
     * @param content The String representation of the cache-max-age duration.
     * @throws PAPIException If there was a problem setting the cache-max-age.
     */
    private void applyCacheMaxAge(MarinerRequestContext context, String content)
            throws PAPIException {
        final ResponseCachingDirectives cachingDirectives =
                getCachingDirectives(context);

        if (cachingDirectives != null) {
            // Determine max-age as Period
            try {
                Period maxage = Period.inSeconds(Long.parseLong(content));

                cachingDirectives.setMaxAge(maxage,
                        ResponseCachingDirectives.PRIORITY_HIGH);
                cachingDirectives.enable();
            } catch (NumberFormatException e) {
                // If string didn't parse to long property, throw an exception
                // reusing existing localised message, saying that Period was
                // expected, but String was encountered.
                throw new PAPIException(EXCEPTION_LOCALIZER.format(
                        "invalid-meta-content-type", new Object[]{
                        Period.class.getName(), String.class.getName()}));
            }
        }
    }

    /**
     * Disables caching for this response, allowing output headers to be set for cache-control.
     *
     * @param context The MarinerRequestContext within which this meta element is
     *                being processed.
     */
    private void applyNoCache(MarinerRequestContext context) {
        final ResponseCachingDirectives cachingDirectives =
                getCachingDirectives(context);

        if (cachingDirectives != null) {
            cachingDirectives.disable();
        }
    }

    /**
     * Enables caching for this response, allowing output headers to be set for cache-control.
     *
     * @param context The MarinerRequestContext within which this meta element is
     *                being processed.
     */
    private void applyCacheAuto(MarinerRequestContext context) {
        final ResponseCachingDirectives cachingDirectives =
                getCachingDirectives(context);

        if (cachingDirectives != null) {
            cachingDirectives.enable();
        }
    }

    /**
     * Looks up the <code>ResponseCachingDirectives</code> for this request
     *
     * @param context The MarinerRequestContext within which this meta element is
     *                being processed.
     * @return The <code>ResponseCachingDirectives</code> or null if the
     *         directives cannot be located for this <code>MarinerRequestContext</code>.
     */
    private ResponseCachingDirectives getCachingDirectives(
            MarinerRequestContext context) {

        ResponseCachingDirectives directives = null;

        // Lookup caching directives
        final EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(context);
        directives = environmentContext.getCachingDirectives();

        return directives;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();

        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}
