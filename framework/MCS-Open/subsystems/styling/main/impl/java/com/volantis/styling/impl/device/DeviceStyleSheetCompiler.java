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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.device;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.impl.compiler.CompilerConfigurationImpl;
import com.volantis.styling.impl.compiler.MatcherFactory;
import com.volantis.styling.impl.compiler.MatcherFactoryImpl;
import com.volantis.styling.impl.compiler.SpecificityCalculatorImpl;
import com.volantis.styling.impl.compiler.StyleSheetCompilerImpl;
import com.volantis.styling.impl.engine.listeners.ImmutableListeners;
import com.volantis.styling.impl.engine.listeners.Listeners;
import com.volantis.styling.impl.engine.matchers.NoNamespaceMatcher;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;
import com.volantis.styling.impl.engine.sheet.CompiledStyleSheetImpl;
import com.volantis.styling.impl.engine.sheet.ImmutableStylerList;
import com.volantis.styling.impl.sheet.StylesDelta;
import com.volantis.styling.impl.state.ImmutableStateRegistry;

/**
 * The compiler for device style sheets.
 */
public class DeviceStyleSheetCompiler
        extends StyleSheetCompilerImpl {

    /**
     * The factory to use for creating matchers, contains a default namespace
     * matcher that expects no namespace, i.e. "".
     */
    private static final MatcherFactory MATCHER_FACTORY =
            new MatcherFactoryImpl(new NoNamespaceMatcher());

    /**
     * The realistic defaults for devices where not all the information is
     * known.
     */
    private static final Defaults REALISTIC_DEFAULTS =
            new Defaults(DeviceValues.DEFAULT);

    /**
     * The optimistic defaults for devices where all the information is
     * known.
     */
    private static final Defaults OPTIMISTIC_DEFAULTS =
            new Defaults(DeviceValues.NOT_SET);

    /**
     * Create a configuration.
     *
     * @return The newly created configuration.
     */
    private static CompilerConfiguration createConfiguration() {
        CompilerConfiguration configuration = new CompilerConfigurationImpl();

        configuration.setSource(StyleSheetSource.DEVICE);
        configuration.setSupportedPseudoEntities(
                new DevicePseudoStyleEntities());
        configuration.setSpecificityCalculator(
                new SpecificityCalculatorImpl());
        return configuration;
    }

    /**
     * The defaults.
     */
    private final Defaults defaults;

    /**
     * Initialise.
     *
     * @param outlook The outlook on the device information.
     */
    public DeviceStyleSheetCompiler(DeviceOutlook outlook) {
        super(createConfiguration(), MATCHER_FACTORY);

        if (outlook == DeviceOutlook.OPTIMISTIC) {
            defaults = OPTIMISTIC_DEFAULTS;
        } else if (outlook == DeviceOutlook.REALISTIC) {
            defaults = REALISTIC_DEFAULTS;
        } else {
            throw new IllegalArgumentException("Unknown outlook " + outlook);
        }
    }

    // Javadoc inherited.
    protected CompiledStyleSheetImpl createCompiled(
            final ImmutableStylerList orderedStylerList,
            final ImmutableStateRegistry registry,
            final Listeners listeners) {
        return new DeviceCompiledStyleSheet(orderedStylerList,
                registry, listeners, defaults);
    }

    // Javadoc inherited.
    protected StylesDelta createStylesDelta(
            PseudoStyleEntity[] entities, PropertyValue[] values) {
        return new DeviceStylesDelta(entities, values);
    }
}
