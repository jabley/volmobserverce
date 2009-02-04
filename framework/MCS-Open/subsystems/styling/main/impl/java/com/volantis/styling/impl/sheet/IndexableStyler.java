package com.volantis.styling.impl.sheet;

/**
 * A styler that can be indexed.
 *
 * @mock.generate base="Styler"
 */
public interface IndexableStyler extends Styler {
    /**
     * If there is a specific element that the styler will only match on,
     * return that element. Otherwise return null.
     *
     * @return The element that the styler will match on, or null if it will
     *         match any element
     */
    public String getMatchableElement();

    /**
     * If there are any specific classes that the styler will only match on,
     * return those classes in an array. Otherwise return null.
     *
     * @return The class(es) that the styler will match on, or null if it will
     *         match any class
     */
    public String[] getMatchableClasses();

    /**
     * Returns true if the styler can match any class/element, false if it will
     * only match specific values in either of those categories.
     *
     * @return true if the styler can match any class/element
     */
    public boolean isMatchAny();
}
