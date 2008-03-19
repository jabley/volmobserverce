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

package com.volantis.mcs.policies.variants.content;

import com.volantis.mcs.policies.BaseURLPolicy;

/**
 * Content represents a sequence of URLs that are automatically generated from
 * a template.
 *
 * <p>The template is of the form <code>....{...}...</code> where
 * <code>{...}</code> is the portion to replace. The characters <code>{}</code>
 * must not appear anywhere else within the URL and must be balanced.</p>
 *
 * <p>For each item in the sequence from the first which has an index of 0 to
 * the last which has an index of 1 less than the sequence size the
 * <code>{...}</code> portion of the template is replaced with the index.</p>
 *
 * <p>e.g. if the template is <code>image{index}.gif</code> and the sequence
 * size is 4 then the following URLs will be created.</p>
 * <ul>
 * <li>image0.gif</li>
 * <li>image1.gif</li>
 * <li>image2.gif</li>
 * <li>image3.gif</li>
 * </ul>
 *
 * <p>For each item in the sequence a {@link URLContent} instance is constructed
 * with the generated URL and the same {@link BaseURLPolicy} as this.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="urlTemplate">
 * <td align="right" valign="top" width="1%"><b>URL&nbsp;template</b></td>
 * <td>the template that is used to create the URLs in the sequence.</td>
 * </tr>
 *
 * <tr id="sequenceSize">
 * <td align="right" valign="top" width="1%"><b>sequence size</b></td>
 * <td>the size of the sequence.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see AutoURLSequenceBuilder
 * @since 3.5.1
 */
public interface AutoURLSequence
        extends Content, BaseURLRelative {

    /**
     * Get a new builder instance for {@link AutoURLSequence}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link AutoURLSequenceBuilder#getAutomaticURLContentSequence()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    AutoURLSequenceBuilder getAutomaticURLContentSequenceBuilder();

    /**
     * Getter for the <a href="#urlTemplate">URL template</a> property.
     *
     * @return Value of the <a href="#urlTemplate">URL template</a>
     *         property.
     */
    String getURLTemplate();

    /**
     * Getter for the <a href="#sequenceSize">sequence size</a> property.
     * @return Value of the <a href="#sequenceSize">sequence size</a>
     * property.
     */
    int getSequenceSize();
}
