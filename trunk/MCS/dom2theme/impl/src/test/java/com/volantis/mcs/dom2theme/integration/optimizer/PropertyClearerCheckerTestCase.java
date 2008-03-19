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
package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerCheckerImpl;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.StatusUsage;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.InitialValueAccuracy;
import com.volantis.styling.properties.PropertyDetailsBuilder;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.PropertyDetailsSetBuilder;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.FixedInitialValue;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class PropertyClearerCheckerTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create styling related objects.
     */
    private static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    private static final StyleProperty STYLE_PROPERTY =
        StylePropertyDetails.BORDER_TOP_WIDTH;
    private static final StyleKeyword DEFAULT_VALUE = BorderWidthKeywords.MEDIUM;
    private static final StyleKeyword OTHER_VALUE = BorderWidthKeywords.THIN;
    private static final StyleValue ANY_VALUE = OptimizerHelper.ANY;

    private PropertyValues inputValues;
    private StyleValue deviceValue;

    protected void setUp() throws Exception {
        super.setUp();

        inputValues = StylingFactory.getDefaultInstance()
                .createPropertyValues();
    }

    /**
     * Backwards compatibility mode, no inheritance, not known device initial
     * value accuracy.
     */
    public void testCheckStatusDefaultStatus() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // value is same as the default value
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the default one
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value could be the default one
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, no inheritance, initial value known to be
     * the same as the default value.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyDefaultInitialValue() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(DEFAULT_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // value is same as the default value
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the default one
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value could be the default one
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, no inheritance, initial value is known to
     * be something different than the default value.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyOtherInitialValue() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(OTHER_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // the initial value is not the default one
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // the current value is the same as the device's known initial value
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // accept the device's initial value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode with inheritance but without parent value
     * set, unknown initial value accuracy.
     */
    public void testCheckStatusDefaultStatusInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // inheritable property, but no parent value set => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // inheritable property, but no parent value set => property is REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // inheritable property, no parent value set, but can have ANY value
        // => property is clearable.
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode with inheritance but without parent value
     * set, known initial value is the same as the default.
     *
     * If the property is inherited, device's initial value doesn't matter.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyDefaultInitialValueInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(DEFAULT_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // inherited property, but no parent value set => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // inherited property, but no parent value set => property is REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // inherited property, no parent value set, but can have ANY value
        // => property is clearable
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode with inheritance but without parent value
     * set, known initial value is the same as the default.
     *
     * If the property is inherited, device's initial value doesn't matter.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyOtherInitialValueInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(OTHER_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.DEFAULT;

        // inherited property, but no parent value set => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // inherited property, but no parent value set => property is REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // inherited property, no parent value set, but can have ANY value
        // => property is clearable
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, inheritable property, the parent has the
     * default value, the device has no known or unknown default value.
     */
    public void testCheckStatusDefaultStatusInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is the same as the parents value => CLEARABLE
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the parent's value => REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value can be the same as the parent's => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, inheritable property, the parent has the
     * default value, the device's initial value is known to be the default.
     *
     * If the property is inherited, device's initial value shouldn't matter.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyDefaultInitialValueInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(DEFAULT_VALUE));

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is the same as the parents value => CLEARABLE
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the parent's value => REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value can be the same as the parent's => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, inheritable property, the parent has the
     * default value, the device's initial value is known to be something else
     * than the default value.
     *
     * If the property is inherited, device's initial value shouldn't matter
     */
    public void testCheckStatusDefaultStatusKnownAccuracyOtherInitialValueInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(OTHER_VALUE));

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is the same as the parents value => CLEARABLE
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the parent's value => REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value can be the same as the parent's => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode, inheritable property, the parent has a
     * value other than the default, accuracy of device's initial value is
     * not known.
     */
    public void testCheckStatusDefaultStatusInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is different from the parent's value => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // value is the same as parent's value => CLEARABLE
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // value can be the same as parent's value => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode with inheritable property, the parent has a
     * value other than the default, device's initial value is known to be the
     * same as the default value.
     *
     * If the property is inherited, device's initial value shouldn't matter.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyDefaultInitialValueInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(DEFAULT_VALUE));

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is different from the parent's value => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // value is the same as parent's value => CLEARABLE
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // value can be the same as parent's value => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Backwards compatibility mode with inheritable property, the parent has a
     * value other than the default, device's initial value is known to be the
     * other than the default value.
     *
     * If the property is inherited, device's initial value shouldn't matter.
     */
    public void testCheckStatusDefaultStatusKnownAccuracyOtherInitialValueInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(OTHER_VALUE));

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.DEFAULT;

        // value is different from the parent's value => property is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // value is the same as parent's value => CLEARABLE
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // value can be the same as parent's value => CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Normal case, the property is known not to be set by the device's default
     * stylesheet, property is not inheritable, the accuracy of the device's
     * initial value is not known.
     */
    public void testCheckStatusNotSet() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.NOT_SET;

        // property will not be set by the device's default stylesheet but the
        // accuracy of the initial value of the device is not known.
        // The property must be required independently of the current value.

        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);
        checkValue(checker, ANY_VALUE, PropertyStatus.REQUIRED);
    }

    /**
     * Normal case, the property is known not to be set by the device's default
     * stylesheet and it is not inherited. The initial value of the device
     * for this element and property is known to be the default one.
     */
    public void testCheckStatusNotSetKnownAccuracyDefaultInitialValue() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(DEFAULT_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.NOT_SET;

        // value is same as the initial value of the device
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value is different from the device's initial value
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // accept the initial value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Normal case, the property is known not to be set by the device's default
     * stylesheet and it is not inherited. The initial value of the device
     * for this element and property is known to be other than the default one.
     */
    public void testCheckStatusNotSetKnownAccuracyOtherInitialValue() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInitialValueAccuracy(
            InitialValueAccuracy.KNOWN);
        propertyDetailsBuilder.setInitialValueSource(
            new FixedInitialValue(OTHER_VALUE));
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.NOT_SET;

        // value is different from the device's initial value
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // value is same as the initial value of the device
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // accept the initial value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The default stylesheet of the device will not set the property, the
     * property is inheritable, but the parent has no value.
     *
     * Initial value accuracy is not know, but it is irrelevant for inheritable
     * properties.
     */
    public void testCheckStatusNotSetInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, null);

        deviceValue = DeviceValues.NOT_SET;

        // since there is no parent value, the value is required, unless it is
        // ANY.

        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // inheritable property, no parent value set, but can have ANY value
        // => property is CLEARABLE
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has no value set in its default stylesheet, the property is
     * inheritable and the parent has the default value.
     * The accuracy of the device's initial value is not known, but it is not
     * important since it is an inheritable property.
     */
    public void testCheckStatusNotSetInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.NOT_SET;

        // parent's default value can be inherited
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // value different from parent's value
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // value can be inherited from parent
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The default stylesheet of the device will not set the property value, the
     * property is inheritable and the parent has different value than the
     * default one.
     */
    public void testCheckStatusNotSetInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker =
            createChecker(propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.NOT_SET;

        // value differs from what can be inherited
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // value is inherited
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // value can be inherited
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which is the same as the default value.
     * No inheritance is involved.
     */
    public void testCheckStatusKnownValueDefault() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = DEFAULT_VALUE;

        // the same value will be set by the device
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // device would try to set a different value
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // let the device set the value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which is the same as the default value.
     * The property is inheritable, but the parent has no value set for this
     * property.
     *
     * Inherited values are irrelevant if the device has a known/unknown value.
     */
    public void testCheckStatusKnownValueDefaultInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = DEFAULT_VALUE;

        // the same value will be set by the device
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // device would try to set a different value, so keep this
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // let the device to set a value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Everybody want's the default value. The device has the default value as a
     * known value and the parent has the default value set for the inheritable
     * property.
     */
    public void testCheckStatusKnownValueDefaultInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DEFAULT_VALUE;

        // the same value will be set by the device
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // device would try to set a different value, so keep this
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // let the device to set a value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has the default value as known value but the parent has other
     * value set for the inheritable property.
     * Since the device has a known value inherited values doesn't matter.
     */
    public void testCheckStatusKnownValueDefaultInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DEFAULT_VALUE;

        // this is the device's known value, so it is safe to clear
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.CLEARABLE);

        // it is different from the device's known value => property is REQUIRED
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // it can be the devices known value, so can be cleared
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which differs from the default value.
     */
    public void testCheckStatusKnownValueOther() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = OTHER_VALUE;

        // the device would want to set a different value, must keep the
        // property
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // the device would set the same value anyway
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // let the device choose the value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which differs from the default value.
     * The property is inheritable, but the parent element doesn't have a value
     * set for this property.
     */
    public void testCheckStatusKnownValueOtherInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = OTHER_VALUE;

        // the known value matters, so the default value is REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // OTHER_VALUE is the same as the device's known value
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // let the device decide the value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which differs from the default value.
     * The property is inheritable and the parent has the default set for this
     * property.
     */
    public void testCheckStatusKnownValueOtherInheritedWithDefaultParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = OTHER_VALUE;

        // default value is not the device's known value
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // it is the device's known value so it is safe to clear
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // can be the device's known value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device has a known value which differs from the default value.
     * The property is inheritable and the parent has the same value as the
     * device's known value.
     */
    public void testCheckStatusKnownValueOtherInheritedWithOtherParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createOtherParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = OTHER_VALUE;

        // the device's known value is something else so the default value is
        // REQUIRED
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // it is the device's known value, safe to clear
        checkValue(checker, OTHER_VALUE, PropertyStatus.CLEARABLE);

        // it can be the device's known value
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The default stylesheet of the device would set a value but this value is
     * unknown.
     */
    public void testCheckStatusUnknownValue() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = DeviceValues.UNKNOWN;

        // play it safe and keep the property
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // play it safe and keep the property
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // unknown value is good for ANY
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device wants to set a value for this inheritable property, but the
     * value is unknown.
     * The parent element doesn't have anything set for this property.
     *
     * If the device has a preferred value in the default stylesheet, inherited
     * values are irrelevant.
     */
    public void testCheckStatusUnknownValueInheritedNoParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, null);

        deviceValue = DeviceValues.UNKNOWN;

        // play it safe and keep the property
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // play it safe and keep the property
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // play it safe and keep the property
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * The device wants to set a value for this inheritable property, but the
     * preferred value is unknown.
     * The parent element has the default value set for this property, but in
     * this case it is the value the device wants to set that matters, inherited
     * values are irrelevant.
     */
    public void testCheckStatusUnknownValueInheritedWithParent() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);
        propertyDetailsBuilder.setInherited(true);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.UNKNOWN;

        // play it safe and keep the property
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);

        // play it safe and keep the property
        checkValue(checker, OTHER_VALUE, PropertyStatus.REQUIRED);

        // the value of the property is unimportant so clear it.
        checkValue(checker, ANY_VALUE, PropertyStatus.CLEARABLE);
    }

    /**
     * Ensure that when checking the status of a pseudo class property that
     * it requires a property that matches the initial value.
     */
    public void testCheckStatusPseudoClassPropertyRequiresInitial() {

        // set up property details set builder
        final PropertyDetailsBuilder propertyDetailsBuilder =
            new PropertyDetailsBuilder(STYLE_PROPERTY);

        final PropertyValues parentValues = createDefaultParentValues();
        final PropertyClearerCheckerImpl checker = createChecker(
            propertyDetailsBuilder, parentValues);

        deviceValue = DeviceValues.NOT_SET;

        // play it safe and keep the property
        checkValue(checker, DEFAULT_VALUE, PropertyStatus.REQUIRED);
    }

    private PropertyValues createDefaultParentValues() {

        final MutablePropertyValues propertyValues =
                STYLING_FACTORY.createPropertyValues();
        propertyValues.setComputedValue(STYLE_PROPERTY, DEFAULT_VALUE);
        return propertyValues;
    }

    private PropertyValues createOtherParentValues() {

        final MutablePropertyValues propertyValues =
                STYLING_FACTORY.createPropertyValues();
        propertyValues.setComputedValue(STYLE_PROPERTY, OTHER_VALUE);
        return propertyValues;
    }

    private PropertyValues createEmptyParentValues() {
        return STYLING_FACTORY.createPropertyValues();
    }

    private void checkValue(final PropertyClearerChecker checker,
                            final StyleValue value,
                            final PropertyStatus expectedStatus) {

        PropertyStatus propertyStatus = checker.checkStatus(STYLE_PROPERTY,
                value, StatusUsage.INDIVIDUAL,
                inputValues, deviceValue);
        assertEquals(expectedStatus, propertyStatus);
    }

    private PropertyClearerCheckerImpl createChecker(
            final PropertyDetailsBuilder propertyDetailsBuilder,
            PropertyValues parentValues) {

        if (parentValues == null) {
            parentValues = createEmptyParentValues();
        }

        final PropertyDetailsSetBuilder propertyDetailsSetBuilder =
            new PropertyDetailsSetBuilder();
        propertyDetailsSetBuilder.addBuilder(propertyDetailsBuilder);

        final PropertyDetailsSet detailsSet =
            propertyDetailsSetBuilder.getDetailsSet();

        final PropertyClearerCheckerImpl checker =
            new PropertyClearerCheckerImpl(detailsSet);

        // begin an element
        checker.prepare(parentValues, TargetEntity.ELEMENT);
        return checker;
    }
}
