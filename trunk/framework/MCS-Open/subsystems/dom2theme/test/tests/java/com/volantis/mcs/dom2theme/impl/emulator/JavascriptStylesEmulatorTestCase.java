/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.emulator;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.mcs.themes.PseudoClassTypeEnum;

/**
 * Verify that {@link JavascriptStylesEmulator} behaves as expected.
 */
public class JavascriptStylesEmulatorTestCase extends MockTestCaseAbstract {

    // Test objects.
    private CSSVersionMock cssVersion;
    private JavascriptStylesEmulator emulator;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        cssVersion = new CSSVersionMock("cssVersion", expectations);
    }

    /**
     * Verify that a {@link HoverEmulator} is not added if the hover pseudo
     * class is supported by the device.
     */
    public void testInitializationWhenHoverSupported() {
        cssVersion.expects.supportsPseudoSelectorId(
                PseudoClassTypeEnum.HOVER.getType()).returns(true);

        // Constructor calls initializeEmulators.
        emulator = new JavascriptStylesEmulator(cssVersion);
        assertTrue(emulator.emulators.isEmpty());
    }

    /**
     * Verify that a {@link HoverEmulator} is added if the hover pseudo
     * class is not supported by the device.
     */
    public void testInitializationWhenHoverNotSupported() {
        cssVersion.expects.supportsPseudoSelectorId(
                PseudoClassTypeEnum.HOVER.getType()).returns(false);

        // Constructor calls initializeEmulators.
        emulator = new JavascriptStylesEmulator(cssVersion);
        assertEquals(1, emulator.emulators.size());
        assertTrue(emulator.emulators.get(0) instanceof HoverEmulator);
    }
}
