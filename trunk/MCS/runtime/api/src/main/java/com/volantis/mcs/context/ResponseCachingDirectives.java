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
package com.volantis.mcs.context;

import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Comparator;
import com.volantis.shared.system.SystemClock;

/**
 */
public class ResponseCachingDirectives {

    /**
     * Normal priority.
     */
    public static final Priority PRIORITY_NORMAL = new Priority("Normal", 0);

    /**
     * High priority.
     */
    public static final Priority PRIORITY_HIGH = new Priority("High", 1);

    /**
     * Time of expiry as set by {@link #setExpires} or computed after calling
     * {@link #setMaxAge}. Null, if no max-age/expires is set.
     */
    private Time expires;

    /**
     * Priority used to set the expires/max-age value.
     */
    private Priority expiresPriority;

    /**
     * True iff {@link #enable} was called.
     */
    private boolean enabled;

    /**
     * True iff {@link #disable} was called.
     */
    private boolean disabled;

    /**
     * True iff {@link #close} was called.
     */
    private boolean closed;

    /**
     * The clock to be used to get the current time.
     */
    private final SystemClock clock;

    /**
     * Creates a new instance that'll use the specified clock to get the current
     * time.
     *
     * @param clock the clock to be used to get the current time, must not be
     * null
     */
    public ResponseCachingDirectives(final SystemClock clock) {
        if (clock == null) {
            throw new IllegalArgumentException("Clock cannot be null");
        }
        this.clock = clock;
        expires = null;
        expiresPriority = PRIORITY_NORMAL;
        enabled = false;
        disabled = false;
        closed = false;
    }

    /**
     * Sets the maximum age with the specified priority.
     *
     * <p>Set is only effective if the level of priority is higher than the
     * level used at the last effective set (starting from
     * {@link #PRIORITY_NORMAL}) or the levels are equal and the new value is
     * more restrictive than the previous effective set.</p>
     *
     * <p>E.g. setting the max-age value with higher priority always changes the
     * stored expiry value and setting a lower max-age value updates the stored
     * expiry value if same level of priority is used.</p>
     *
     * @param period the value to set
     * @param priority the priority
     * @return true if the call changed the time of expiry
     */
    public boolean setMaxAge(final Period period, final Priority priority) {
        checkClosed();
        final Time time = clock.getCurrentTime().addPeriod(period);
        return setExpires(time, priority);
    }

    /**
     * Sets the time of expiry with the specified priority.
     *
     * <p>Set is only effective if the level of priority is higher than the
     * level used at the last effective set (starting from
     * {@link #PRIORITY_NORMAL}) or the levels are equal and the new value is
     * more restrictive than the previous effective set.</p>
     *
     * <p>E.g. setting the expires value with higher priority always changes the
     * stored expiry value and setting an earlier expires value updates the
     * stored expiry value if same level of priority is used.</p>
     *
     * @param time the value to set
     * @param priority the priority
     * @return true if the call changed the time of expiry
     */
    public boolean setExpires(final Time time, final Priority priority) {
        checkClosed();
        boolean changed = false;
        if ((priority.getLevel() > expiresPriority.getLevel()) ||
            ((priority.getLevel() == expiresPriority.getLevel()) &&
                (expires == null || Comparator.GT.compare(expires, time)))) {

            expires = time;
            expiresPriority = priority;
            changed = true;
        }

        return changed;
    }

    /**
     * Returns the time of expiry.
     * @return the time of expiry or null if it wasn't set by either
     * {@link #setMaxAge} or {@link #setExpires}.
     */
    public Time getExpires() {
        return expires;
    }

    /**
     * Returns the current expires priority.
     *
     * @return the current expires priority
     */
    public Priority getExpiresPriority() {
        return expiresPriority;
    }

    /**
     * If caching is not disabled, enables caching.
     *
     * @see #isEnabled()
     */
    public void enable() {
        checkClosed();
        this.enabled = true;
    }

    /**
     * Returns true iff caching is enabled and not disabled.
     *
     * <p>Note: enabling/disabling caching is asymmetric. Initially caching is
     * neither enabled nor disabled and isEnabled() returns false. If
     * {@link #enable()} is called then caching is enabled (and isEnabled()
     * returns true) until {@link #disable()} is called. After calling
     * {@link #disable()} caching is disabled forever (isEnabled() will always
     * return false even if {@link #enable()} is called again).</p>
     *
     * @return true iff caching is enabled and not disabled
     */
    public boolean isEnabled() {
        return enabled && !disabled;
    }

    /**
     * Disables caching. Disabling caching has priority over enabling it, thus
     * if caching is disabled once, it cannot be re-enabled again.
     *
     * @see #isEnabled()
     */
    public void disable() {
        checkClosed();
        this.disabled = true;
    }

    /**
     * Returns the current time to live value or null if no expiry time is set.
     *
     * @return the TTL value using the current time of the clock of the object
     * or null
     */
    public Period getTimeToLive() {
        return expires == null ? null :
            expires.getPeriodSince(clock.getCurrentTime());
    }

    /**
     * Closes the caching directives object from further set operations.
     */
    public void close() {
        checkClosed();
        closed = true;
    }

    /**
     * Returns the {@link SystemClock} object that is used to retrieve current
     * time.
     *
     * @return the clock
     */
    public SystemClock getClock() {
        return clock;
    }

    /**
     * Throws {@link IllegalStateException} if this caching directives object is
     * closed.
     */
    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("Caching directives are closed.");
        }
    }

    /**
     * Type-safe enumeration for priority levels.
     */
    public static class Priority {
        /**
         * Name of the priority.
         */
        private final String name;

        /**
         * Level of the priority.
         */
        private final int level;

        private Priority(final String name, final int level) {
            this.name = name;
            this.level = level;
        }

        /**
         * Returns the priority level.
         *
         * @return the priority level
         */
        public int getLevel() {
            return level;
        }

        // javadoc inherited
        public String toString() {
            return name;
        }

        /**
         * Returns true if the level of this priority is lower than the level of
         * the specified priority.
         *
         * @param other the other priority to compare to
         * @return true iff the level of this priority is lower
         */
        public boolean isLower(final Priority other) {
            return level < other.getLevel();
        }
    }
}
