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

package com.volantis.xml.pipeline.sax.operations.transform;

import com.volantis.xml.pipeline.sax.config.Configuration;

/**
 * Configuration for the Transform operation
 *
 * Contains configuration for the transform operations.
 *  <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface TransformConfiguration
        extends Configuration {

    /**
     * Specify whether template (XSLT) compilation is required.
     * <p>
     * Just because template compilation is required does not mean that it will
     * actually be attempted. At the moment template compilation does not work
     * on all templates so only those transformations that explicitly specify
     * that the template is compilable will perform compilation.
     * </p>
     * @param required True if it is required and false if it is not.
     */
    public void setTemplateCompilationRequired(boolean required);

    /**
     * Check whether template compilation is required.
     * @return boolean True if it is required and false if it is not.
     */
    public boolean isTemplateCompilationRequired();

    /**
     * Specifies whether the templates should be cached.
     * <p>
     * If this is false then templates will not be cached. Otherwise, the
     * first time a transform operation is run an unlimited size cache will be
     * created and associated with this configuration. Transform operation
     * will use the cache associated with this configuration to store those
     * templates that can be cached.
     * </p>
     * <p>
     * As multiple transform operations can be executing simultaneously the
     * creation of and access to the cache associated with this configuration
     * will be synchronized in order to prevent race conditions between the
     * operations.
     * </p>
     * @param required If true then template caching is required, otherwise it
     * is not.
     */
    public void setTemplateCacheRequired(boolean required);

    /**
     * Check whether template caching is required.
     * @return boolean True if it is required and false if it is not.
     */
    public boolean isTemplateCacheRequired();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Jan-04	545/5	claire	VBM:2004012202 Updated TransformConfiguration and related implementations and testcases

 22-Jan-04	545/3	claire	VBM:2004012202 Correct JavaDoc in TransformConfiguration

 22-Jan-04	545/1	claire	VBM:2004012202 transform configuration to support new template cache property

 07-Aug-03	268/2	chrisw	VBM:2003072905 Public API changed for transform configuration

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
