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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme;

import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfigurationBuilder;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * StyledDOMThemeExtractorFactory might be not right name because 
 * this class allow to create Extractor, Optimizier and Renderer,
 * so name StyledDOMThemeFactory would be more apropriate but for 
 * avoiding class renamin name will stay untouched up to 
 * more extensive refactoring
 */
public abstract class StyledDOMThemeExtractorFactory {

    /**
     * Set up the meta default factory instance
     */
    private static final MetaDefaultFactory metaDefaultFactory =
            new MetaDefaultFactory(
            "com.volantis.mcs.dom2theme.impl.DefaultStyledDOMThemeExtractorFactory",
            StyledDOMThemeExtractorFactory.class.getClassLoader());
    
    
    /**
     * @return the default instance of the factory.
     */
    public static StyledDOMThemeExtractorFactory getDefaultInstance() {
        return (StyledDOMThemeExtractorFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }
    
    
    /**
     * Create a {@link StyledDOMThemeExtractor}.
     *
     * @param configuration The configuration for the extractor.
     * @param context       The contextual information for the extractor.
     * @return the created extractor.
     */
    public abstract StyledDOMThemeExtractor createExtractor(
            ExtractorConfiguration configuration,
            ExtractorContext context);

    /**
     * Create a {@link StyledDocumentOptimizer}.
     *
     * @param configuration The configuration for the optimizer.
     * @param context       The contextual information for the optimizer.
     * @return the created optimizer.
     */
    public abstract StyledDocumentOptimizer createOptimizer(
            ExtractorConfiguration configuration,
            ExtractorContext context);
    
    /**
     * Create a {@link StyledDOMStyleAttributeRenderer}.
     *
     * @param configuration
     * @param context
     * @return the created factory.
     */
    public abstract StyledDOMStyleAttributeRenderer createRenderer(
            final ExtractorConfiguration configuration,
            final ExtractorContext context);
    
    /**
     * Create a builder for the {@link ExtractorConfiguration}.
     *
     * @return The newly instantiated {@link ExtractorConfiguration}.
     */
    public abstract ExtractorConfigurationBuilder createConfigurationBuilder();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Jul-05	8668/3	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
