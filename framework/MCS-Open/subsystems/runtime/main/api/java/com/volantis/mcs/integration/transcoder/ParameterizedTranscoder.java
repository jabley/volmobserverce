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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.integration.transcoder;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.AssetTranscoderContext;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.TranscoderURLParameterProvider;
import com.volantis.mcs.integration.TranscodingException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.utilities.PreservedArea;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * This base class can be extended to provide transcoder implementations that
 * generate a parameterized URL.
 */
public abstract class ParameterizedTranscoder
    implements PluggableAssetTranscoder {
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
                ParameterizedTranscoder.class);
    
    /**
     * The TranscoderURLParameterProvider for providing the name of the
     * url paramaters for this ParameterizedTranscoder.
     */
    private final TranscoderURLParameterProvider tURLPP;
    
    /**
     * Construct a new ParameterizedTranscoder.
     * @param transcoderURLParameterProvider the TranscodeURLParameterProvider
     * that provides the names of the url parameters.
     */
    public ParameterizedTranscoder(TranscoderURLParameterProvider transcoderURLParameterProvider) {
        this.tURLPP = transcoderURLParameterProvider;
    }
    
    // javadoc inherited
    public String constructImageURL(AssetTranscoderContext ctx)
        throws TranscodingException {
        
        String ruleValue = ctx.getRuleValue();
        String url = ctx.getUrl();
        
        String rule = ruleToBeApplied(ruleValue, ctx.getContext());
        // Allocate a string buffer with sensibly estimated size
        StringBuffer value = new StringBuffer(estimatedLength(url, rule));
        
        // Get first occurrence, if any, of the question mark. This delimits
        // the search for the last slash.
        int firstQuestionMarkPos = url.indexOf('?');
        int delimitPos = firstQuestionMarkPos;
        if (firstQuestionMarkPos == -1) {
            delimitPos = url.length();
        }
        
        // Find the last slash (before the first question mark if there is one.)
        int pos = url.substring(0, delimitPos).lastIndexOf('/');
        
        // The constructed URL starts with the input URL
        value.append(url);
        
        // Place the rule in the URL immediately after the last "/" (the URL
        // is assumed to always contain at least one "/", otherwise the
        // rule will be inserted at the start of the URL)
        value.insert(pos + 1, rule + "/");
        
        int lengthBeforeParameters = value.length();
        
        // Add in the width parameter
        if (tURLPP.getWidthParameterName() != null) {
            addWidth(rule, value, ctx.getWidth(), ctx.getContext());
        } else {
            // MCSMI0005X="The width parameter is mandatory but is unnamed in {1}"
            throw new TranscodingException(
                    exceptionLocalizer.format("width-mandatory",
                            getClass().getName()));
        }
        
        // Add in the height parameter if it exists
        if (tURLPP.getHeightParameterName() != null) {
            addHeight(value);
        }
        
        // Add the optional maxSize parameter if name and value have been
        // specified
        if ((tURLPP.getMaxImageSizeParameterName() != null) && (ctx.getMaxSize() > 0)) {
            addMaxSize(rule, value, ctx.getMaxSize(), ctx.getContext());
        }
        
        // Add the optional preserve area parameter if name and value have been
        // specified
        if ((tURLPP.getPreserveAreaParameterName() != null) && (ctx.getPreservedArea() != null )) {
            this.addPreserveArea(value,ctx.getPreservedArea());
        }
        
        // Now add the transcoder-specific device policy value to the URL,
        // if defined
        if (tURLPP.getExtrasPolicyParameterName() != null) {
            addDeviceParameters(rule,
                    value,
                    ctx.getContext());
        }
        
        if (firstQuestionMarkPos == -1 &&
                value.length() > lengthBeforeParameters) {
            value.setCharAt(lengthBeforeParameters, '?');
        }
        
        return value.toString();
    }
    
    /**
     * Adds the width parameter name/value pair to the URL. This should always
     * be called (URLs are expected to always have width parameters) and all
     * implementations of this method must always add a parameter to the URL
     * unless one has already been added.
     *
     * @param rule    the (possibly modified) rule. See ruleValue in {@link
     *                #constructImageURL}
     * @param url     the (partially generated) URL value onto which the width
     *                parameter should be appended
     * @param width   the actual width value
     * @param context see the same parameter in {@link #constructImageURL}
     * @throws TranscodingException if there is a problem adding the parameter
     */
    protected void addWidth(String rule,
            StringBuffer url,
            int width,
            MarinerRequestContext context)
        throws TranscodingException {
        // The width parameter is always added as long as there is not
        // already width parameter.
        if(!parameterExists(url, tURLPP.getWidthParameterName())) {
            url.append('&').append(tURLPP.getWidthParameterName()).
            append('=').append(width);
        }
    }
    
    /**
     * Adds the maxSize parameter name/value pair to the URL. Note that this
     * method should only called if a parameter name has been defined and the
     * maxSize value is greater than zero. If the maxSize is added, the
     * parameter punctuation '&' must also be added (as a prefix).
     *
     * @param rule    the (possibly modified) rule. See ruleValue in {@link
     *                #constructImageURL}
     * @param url     the (partially generated) URL value onto which the width
     *                parameter should be appended
     * @param maxSize the requested maxSize
     * @param context see the same parameter in {@link #constructImageURL}
     * @throws TranscodingException if there is a problem adding the parameter
     */
    protected void addMaxSize(String rule,
            StringBuffer url,
            int maxSize,
            MarinerRequestContext context)
        throws TranscodingException {
        // The maxSize is always added, irrespective on the target encoding
        // (which is different from the original MCS Transforce implementation)
        url.append('&').append(tURLPP.getMaxImageSizeParameterName()).
        append('=').append(maxSize);
    }
    
    /**
     * Adds the height parameter name/value pair to the URL. Currently this
     * parameter has a fixed value of 9999 and is only used when in
     * Transforce mode. The height parameter is deliberately invalid (i.e. it
     * is too big) so that Transforce ignores the value. For some reason the
     * height value must be present in order for Transforce to process the
     * width parameter.
     */
    private void addHeight(StringBuffer url) {
        // The height parameter is only added if there is not already a
        // height parameter.
        if(!parameterExists(url, tURLPP.getHeightParameterName())) {
            url.append('&').append(tURLPP.getHeightParameterName()).
            append('=').append("9999");
        }
    }
    
    /**
     * Adds the extra, device-specific transcoder parameters to the URL, if
     * defined. This should only be called if the policy name has been defined.
     * The parameter punctuation '&' must be added as a prefix explicitly (if
     * there are extra parameters and they don't start with '&').
     *
     * @param rule    the (possibly modified) rule. See ruleValue in {@link
     *                #constructImageURL}
     * @param url     the (partially generated) URL value onto which the width
     *                parameter should be appended
     * @param context see the same parameter in {@link #constructImageURL}
     * @throws TranscodingException if there is a problem adding the parameter
     */
    protected void addDeviceParameters(String rule,
            StringBuffer url,
            MarinerRequestContext context)
        throws TranscodingException {
        String parameters;
        parameters = context.
        getDevicePolicyValue(tURLPP.getExtrasPolicyParameterName());
        
        if ((parameters != null) &&
                (!"".equals(parameters))) {
            if (!parameters.startsWith("&")) {
                url.append('&');
            }
            
            url.append(parameters);
        }
    }
    
    /**
     * Adds the preserve area name/value pair to the URL.
     */
    private void addPreserveArea(StringBuffer url,PreservedArea preservedArea) {
        // The height parameter is only added if there is not already a
        // height parameter.
        if(!parameterExists(url, tURLPP.getPreserveAreaParameterName())) {
            url.append('&').append(tURLPP.getPreserveAreaParameterName()).
            append('=').append(preservedArea.toUrlString());
        }
    }
    
    /**
     * Checks to see whether a URL contains a specified parameter.
     *
     * @param url The URL to check, as a StringBuffer
     * @param parameterName The name of the parameter to check for
     * @return True if the parameter exists in the URL, false otherwise
     */
    private boolean parameterExists(StringBuffer url, String parameterName) {
        boolean parameterExists = false;
        int parameterIndex = url.indexOf(parameterName);
        if(parameterIndex!=-1) {
            boolean hasEquals = url.charAt(parameterIndex +
                    parameterName.length())=='=';
            boolean hasQOrA = url.charAt(parameterIndex - 1) == '?' ||
            url.charAt(parameterIndex -1 ) == '&';
            parameterExists = hasEquals && hasQOrA;
        }
        return parameterExists;
    }
    
    /**
     * This supporting method permits specializations to modify the actual rule
     * used in constructing the URL based on the requested rule, the device's
     * support for various formats and the validity of a given rule for the
     * transcoding server being supported.
     *
     * @param ruleValue see the same parameter in {@link #constructImageURL}
     * @param context   see the same parameter in {@link #constructImageURL}
     * @return the rule to be applied given any constraints on the device or
     *         supported transcoding server
     * @throws TranscodingException if a problem is encountered while trying to
     *                              determine an alternative rule
     */
    protected String ruleToBeApplied(String ruleValue,
            MarinerRequestContext context)
        throws TranscodingException {
        // By default the transcoder supports all rules
        return ruleValue;
    }
    
    /**
     * This supporting method attempts to reduce potential garbage by deriving
     * a completed URL length. Note, however, that computation is kept to a
     * minimum, so the length derived is only an estimate. The calculation is
     * based on the default implementations of the various supporting methods.
     *
     * @param url  the main URL value onto which additional parameters are to
     *             be added
     * @param rule the rule to be applied
     * @return an estimate for the length of the completed URL
     */
    protected int estimatedLength(String url,
            String rule) {
        // URL rule '/' '?'
        int main = url.length() + rule.length() + 2;
        
        // Calculated later if needed
        int width = 0;
        
        // Calculated later if needed
        int height = 0;
        
        // Calculated later if needed
        int maxSize = 0;
        
        // Calculated lated if needed
        int preserveArea = 0;
        
        // Placeholder to avoid having to actually do expensive look up twice
        int extras = 60;
        
        if (tURLPP.getWidthParameterName() != null) {
            // name '=' value
            width = tURLPP.getWidthParameterName().length() + 1 + 6;
        }
        
        if (tURLPP.getHeightParameterName() != null) {
            // name '=' value
            height = tURLPP.getHeightParameterName().length() + 1 + 5;
        }
        
        if (tURLPP.getMaxImageSizeParameterName() != null) {
            // '&' name '=' value
            maxSize = 1 + tURLPP.getMaxImageSizeParameterName().length()
            + 1 + 6;
        }
        
        if (tURLPP.getPreserveAreaParameterName() != null) {
            // '&' name '=' value
            preserveArea = 1 + tURLPP.getPreserveAreaParameterName().length()
            + 1 + 6 + 1 + 6; // number ',' number
        }
        
        return main + width + height + maxSize + extras + preserveArea;
    }
    
    // javadoc inherited
    public String getHostParameter() {
        return tURLPP.getHostParameterName();
    }
    
    // javadoc inherited
    public String getPortParameter() {
        return tURLPP.getPortParameterName();
    }
    
    // javadoc inherited
    public String getWidthParameter() {
        return tURLPP.getWidthParameterName();
    }
    
    // javadoc inherited
    public String getMaxImageSizeParameter() {
        return tURLPP.getMaxImageSizeParameterName();
    }
    
//  javadoc inherited
    public String getPreserveAreaParameter() {
        return tURLPP.getPreserveAreaParameterName();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset
 
 17-Feb-05	7015/1	adrianj	VBM:2005021508 Fix for parameters when creating image URLs
 
 17-Feb-05	7008/1	adrianj	VBM:2005021508 Fixes to creation of image URL
 
 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter
 
 05-Jan-05	6595/1	allan	VBM:2005010509 Only add a width if there is no width parameter
 
 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build
 
 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build
 
 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework
 
 04-Nov-04	6109/2	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names
 
 10-Sep-04	5493/2	pcameron	VBM:2004091002 Fixed URL generation when parameter values have slashes
 
 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)
 
 29-Sep-03	1454/3	philws	VBM:2003092401 Minor updates requested in the code review
 
 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations
 
 ===========================================================================
 */
