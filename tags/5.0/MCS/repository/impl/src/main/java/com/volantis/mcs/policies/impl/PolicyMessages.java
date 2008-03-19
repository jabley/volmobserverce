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

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;

public class PolicyMessages {

    public static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(PolicyMessages.class);

    public static final MessageLocalizer MESSAGE_LOCALIZER =
            LocalizationFactory.createMessageLocalizer(PolicyMessages.class);

    /**
     * @i18n.message Variant type {0} not allowed inside policy {1}
     * @i18n.arg 0 Name of the variants type.
     * @i18n.arg 1 Name of the policy.
     */
    public static final String VARIANT_TYPE_NOT_ALLOWED =
            "policy.variant.type.not.allowed";

    /**
     * @i18n.message {0} must be greater than {1} but has a value of {2}
     * @i18n.arg 0 Name of the property.
     * @i18n.arg 1 Minimum exclusive value.
     * @i18n.arg 2 Actual value.
     */
    public static final String MINIMUM_EXCLUSIVE = "minimum.exclusive";

    /**
     * @i18n.message {0} must be >= {1} and <= {2} but has a value of {3}
     * @i18n.arg 0 Name of the property.
     * @i18n.arg 1 Minimum inclusive value.
     * @i18n.arg 2 Maximum inclusive value.
     * @i18n.arg 3 Actual value.
     */
    public static final String RANGE_INCLUSIVE_INCLUSIVE = "range.inclusive.inclusive";

    /**
     * @i18n.message {0} must be specified.
     * @i18n.arg 0 Name of item that must be specified.
     */
    public static final String UNSPECIFIED = "unspecified";

    /**
     * @i18n.message Policy {0} contains a variants of type {1} that has no selection specified.
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     */
    public static final String UNSPECIFIED_SELECTION = "policy.variant.unspecified.selection";

    /**
     * @i18n.message Policy {0} contains a variants of type {1} that has an unsupported selection type {2}
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     * @i18n.arg 2 Selection type.
     */
    public static final String UNSUPPORTED_SELECTION_TYPE = "policy.variant.unsupported.selection.type";

    /**
     * @i18n.message Policy {0} contains a variants of type {1} that has no meta data specified.
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     */
    public static final String UNSPECIFIED_META_DATA = "policy.variant.unspecified.meta.data";

    /**
     * @i18n.message Policy {0} contains a variants of type {1} that has an unsupported meta data type {2}
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     * @i18n.arg 2 Meta data type.
     */
    public static final String UNSUPPORTED_META_DATA_TYPE = "policy.variant.unsupported.meta.data.type";

    /**
     * @i18n.message Policy '{0}' contains a variants of type '{1}' that has no content specified.
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     */
    public static final String UNSPECIFIED_CONTENT = "policy.variant.unspecified.content";

    /**
     * @i18n.message Policy {0} contains a variant of type {1} that has an unsupported content type {0}
     * @i18n.arg 0 Policy name.
     * @i18n.arg 1 Variant type.
     * @i18n.arg 2 Content type.
     */
    public static final String UNSUPPORTED_CONTENT_TYPE = "policy.variant.unsupported.content.type";

    /**
     * @i18n.message At least one device, or category target should be specified.
     */
    public static final String NO_TARGETS = "policy.variant.targeted.no.targets";

    /**
     * @i18n.message At least one variant should be specified.
     */
    public static final String NO_VARIANTS = "policy.variable.no.variants";

    /**
     * @i18n.message URL value must be specified.
     */
    public static final String URL_UNSPECIFIED = "policy.variant.content.url.unspecified";

    /**
     * @i18n.message URL template must be specified.
     */
    public static final String URL_TEMPLATE_UNSPECIFIED = "policy.variant.content.url.template.unspecified";

    /**
     * @i18n.message Sequence size is '{0}' but must be greater than 0.
     * @i18n.arg 0 Sequence size.
     */
    public static final String SEQUENCE_SIZE_MINIMUM = "policy.variant.content.sequence.size.minimum";

    /**
     * @i18n.message URL template '{0]' in error, must be of the format .*{.*}.* where . is any character apart from { or }.
     * @i18n.arg 0 URL template.
     */
    public static final String URL_TEMPLATE_ERROR = "policy.variant.content.url.template.error";

    /**
     * @i18n.message Width hint '{0}' outside the expected range of 1 to 100 (inclusive).
     */
    public static final String WIDTH_HINT_RANGE = "policy.variant.width.hint.range";

    /**
     * @i18n.message Height hint '{0}' outside the expected range of 0 to 100 (inclusive).
     */
    public static final String HEIGHT_HINT_RANGE = "policy.variant.height.hint.range";

    /**
     * @i18n.message Encoding unspecified.
     */
    public static final String ENCODING_UNSPECIFIED = "policy.variant.encoding.unspecified";

    /**
     * @i18n.message Encoding of '{0}' is unsupported.
     */
    public static final String ENCODING_UNSUPPORTED = "policy.variant.encoding.unsupported";

    /**
     * @i18n.message X axis unspecified.
     */
    public static final String X_AXIS_UNSPECIFIED = "policy.variant.x.axis.unspecified";

    /**
     * @i18n.message Y axis unspecified.
     */
    public static final String Y_AXIS_UNSPECIFIED = "policy.variant.y.axis.unspecified";

    /**
     * @i18n.message Axis interval ''{0}'' must be greater than or equal to 0.
     */
    public static final String AXIS_INTERVAL_RANGE = "policy.variant.axis.interval.range";

    /**
     * @i18n.message Chart type unspecified.
     */
    public static final String CHART_TYPE_UNSPECIFIED = "policy.variant.chart.type.unspecified";

    /**
     * @i18n.message Axis title unspecified.
     */
    public static final String AXIS_TITLE_UNSPECIFIED = "policy.variant.axis.title.unspecified";

    /**
     * @i18n.message Texual content must be specified.
     */
    public static final String TEXT_UNSPECIFIED = "policy.variant.content.text.unspecified";

    /**
     * @i18n.message Unexpected policy reference type, expected '{0}', found '{1}'.
     * @i18n.arg 0 Expected policy type.
     * @i18n.arg 1 Actual policy type.
     */
    public static final String POLICY_REFERENCE_TYPE_MISMATCH = "policy.reference.unexpected.type";

    /**
     * @i18n.message Policy reference was not specified.
     */
    public static final String POLICY_REFERENCE_UNSPECIFIED = "policy.reference.unspecified";

    /**
     * @i18n.message Base URL must be specified.
     */
    public static final String BASE_URL_UNSPECIFIED = "policy.base.url.unspecified";

    /**
     * @i18n.message Illegal alternate name '{0}' for type '{1}'.
     */
    public static final String ALTERNATE_NAME_ILLEGAL = "policy.alt.name.illegal";

    /**
     * @i18n.message Illegal type '{0}' for alternate named '{1}'.
     */
    public static final String ALTERNATE_TYPE_ILLEGAL = "policy.alt.type.illegal";

    /**
     * @i18n.message Multiple alternates of type '{0}' are not allowed.
     */
    public static final String ALTERNATE_MULTIPLE_TYPES = "policy.alt.multiple.types";
    
    /**
     * @i18n.message Variant had errors, ignoring.
     */
    public static final String VARIANT_INVALID_IGNORED = "variant-invalid-ignored";
}
