/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

public class SourceType {
    public static final SourceType MOCKED = new SourceType("MOCKED", false, -1);
    public static final SourceType BASE_INTERFACE = new SourceType("BASE_INTERFACE", true, 100);
    public static final SourceType BASE_CLASS = new SourceType("BASE_CLASS", true, 100);
    public static final SourceType MOCKED_INTERFACE = new SourceType("MOCKED_INTERFACE", true,
            10);

    private final String name;
    private final boolean mockAvailable;
    private final int priority;

    private SourceType(String name, boolean mockAvailable, int priority) {
        this.name = name;
        this.mockAvailable = mockAvailable;
        this.priority = priority;
    }

    public boolean isMockAvailable() {
        return mockAvailable;
    }

    public String toString() {
        return name;
    }

    public boolean isBetterThan(SourceType other) {
        return priority > other.priority;
    }
}
