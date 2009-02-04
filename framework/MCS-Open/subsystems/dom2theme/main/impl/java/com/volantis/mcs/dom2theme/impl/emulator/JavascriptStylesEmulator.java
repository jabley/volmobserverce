/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates any styles which cannot be supported by CSS using javascript.
 */
public class JavascriptStylesEmulator {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(JavascriptStylesEmulator.class);

    /**
     * Used to determine which emulators are required for this device.
     */
    private CSSVersion cssVersion;

    /**
     * List of emulators which are required by this device.
     */
    protected final List emulators = new ArrayList();

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param cssVersion which describes the device's level support for css
     */
    public JavascriptStylesEmulator(CSSVersion cssVersion) {
        this.cssVersion = cssVersion;

        initializeEmulators();
    }

    /**
     * For use only by subclasses.
     */
    protected JavascriptStylesEmulator() {
    }

    /**
     * Create and add the relevant emulators as appropriate.
     */
    private void initializeEmulators() {
        if (cssVersion == null) {
            // The CSSVersion should never be null; it should default to CSS2
            // if no device can be found from which to configure it.
            LOGGER.warn("css-version-null");
        } else {
            if (!cssVersion.supportsPseudoSelectorId(
                    PseudoClassTypeEnum.HOVER.getType())) {
                emulators.add(new HoverEmulator());
            }
        }
    }

    /**
     * Emulate the unsupported styles using javascript. The styles which are
     * emulated are determined by the device capabilities.
     *
     * @param element       for which the styles apply
     * @param outputStyles  to be emulated using javascript
     */
    public void emulate(Element element,
                        OutputStyles outputStyles) {
        for (int i=0; i<emulators.size(); i++) {
            final JavascriptStylesEmulator emulator =
                    ((JavascriptStylesEmulator)emulators.get(i));
            emulator.emulate(element, outputStyles);
        }
    }
}

