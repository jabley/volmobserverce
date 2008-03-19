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
/**
 * (c) Copyright Volantis Systems Ltd. 2005. 
 */
package com.volantis.synergetics.path;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent and allow manipulations on unix style paths.
 */
public final class Path {

    /**
     * Localize the exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Path.class);

    /**
     * The separator character to use.
     */
    public static final char SEPARATOR_CHAR = '/';

    /**
     * The separator character to use.
     */
    public static final String SEPARATOR_STRING = "" + SEPARATOR_CHAR;

    /**
     * The empty path
     */
    public static final Path EMPTY = Path.parse("");

    /**
     * The normalized String represenation of the path. This path is always
     * normalized and never begins with a leading "/". If a leading slash is
     * needed it is added in the asString method
     */
    private String path;

    /**
     * True if this path is abolute
     */
    private boolean isAbsolute = false;

    /**
     * The path stack
     */
    private PathStack ps = new PathStack();

    /**
     * Create a new String with the given stack
     *
     * @param stack the stack to use
     */
    private Path(PathStack stack, boolean isAbsolute) {
        this.isAbsolute = isAbsolute;
        this.ps = stack;
        this.path = this.ps.asString();
    }

    /**
     * Create a new Path from the specified string
     *
     * @param path the string representation of the path
     * @throws IllegalArgumentException if the path is null
     */
    private Path(String path) {
        if (path == null) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("path-is-null"));
        }
        String trimmed = path.trim();
        this.isAbsolute = isAbsolute(trimmed);
        this.path = normalize(ps, trimmed);
    }

    /**
     * Create a new path from the provided path starting at path fragment
     * <code>start</code> (inclusive) and ending at path fragment
     * <code>end</code> (exclusive).
     * 
     * If <code>start</code> is 0 then the new path will return the same
     * value for {@link Path#isAbsolute()} as the original path. If start is
     * not 0 then the new path will be relative.
     * 
     * @param start
     *        low endpoint (inclusive) of the path
     * @param end
     *        high endpoint (exclusive) of the path
     * @throws IndexOutOfBoundsException
     *         for an illegal endpoint index value (start &lt; 0 || end &gt;
     *         size || start &gt; end).
     */
    public Path(Path path, int start, int end) {

        // if the new path starts at index 0 then it inherites the relative
        // or absolute property from its source
        if (start == 0) {
            this.isAbsolute = path.isAbsolute();
        }
        this.ps = new PathStack(path.ps, start, end);
        this.path = this.ps.asString();
    }

    /**
     * Parse the <code>path</code> parameter into a Path class.
     *
     * @param path the String path representation
     * @return a Path object representing the string path provided
     *
     * @throws IllegalArgumentException if the path is null or the path is
     *                                  empty
     */
    public static Path parse(String path) {
        return new Path(path);
    }


    /**
     * Resolve the String path against this path
     *
     * @param path the path to resolve agains the base
     * @return the Path object representing the resolved path.
     */
    public Path resolve(String path) {
        if (path == null) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("path-is-null"));
        }
        if (isAbsolute(path)) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("path-is-absolute", path));
        }

        StringBuffer sb = new StringBuffer(
            1 + asString().length() + path.length());
        if (isAbsolute()) {
            sb.append(SEPARATOR_STRING);
        }
        sb.append(asString());
        sb.append(Path.SEPARATOR_STRING);
        sb.append(path);
        return new Path(sb.toString());
    }

    /**
     * Resolve the path relative to this path
     *
     * @param path the path to resolve against this path
     * @return the resolved path.
     */
    public Path resolve(Path path) {
        return Path.resolve(this, path);
    }

    /**
     * Resolve the String path agains the current base path
     *
     * @param base the base path to resolve against
     * @param path the path to resolve agains the base
     * @return the Path object representing the resolved path.
     */
    public static Path resolve(String base, String path) {
        return Path.parse(base).resolve(path);
    }

    /**
     * Resolve the <code>path</code> against the base path.
     *
     * @param base the path to resolve against
     * @param path the path to resolve
     * @return the resolved path
     */
    public static Path resolve(Path base, Path path) {
        return base.resolve(path.asString());
    }


    /**
     * Return the parent path of the current path
     *
     * <p>e.g. <code>Path.parse("a/b/c").getParent().asString().equals("a/b");
     * </code></p>
     *
     * @return the parent path of the current path.
     *
     * @throws IllegalStateException if there is no parent.
     */
    public Path getParent() {
        // check that a parent exists.
        if (isAbsolute()) {
            if (isEmpty()) {
                throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("path-has-no-parent", path));
            }
        } else {
            if (getNumberOfFragments() <= 1) {
                throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("path-has-no-parent", path));
            }
        }
        return new Path(this, 0, getNumberOfFragments() - 1);
    }

    /**
     * Return the Path as a normalized string
     *
     * @return the path as a normalized string
     */
    public String asString() {
        return (isAbsolute()) ? SEPARATOR_STRING + path : path;
    }

    /**
     * Return the path as an absolute string (has a leading "/")
     *
     * @return the path as an absolute string.
     */
    public String asAbsoluteString() {
        return SEPARATOR_STRING + path;
    }

    /**
     * Return the path as a relative string (has not leading "/")
     *
     * @return the path as a relative string.
     */
    public String asRelativeString() {
        return path;
    }

    /**
     * Return true if this Path represents and absolute path.
     *
     * @return true if this Path represents and absolute path.
     */
    public boolean isAbsolute() {
        return this.isAbsolute;
    }

    /**
     * Return a Path that is the the same as this Path but not absolute.
     *
     * @return a Path that is the the same as this Path but not absolute.
     */
    public Path asRelativePath() {
        Path result = this;
        if (isAbsolute()) {
            result = new Path(this.ps, false);
        }
        return result;
    }

    /**
     * Return the path as a platform specific string. Note that this will not
     * indroduce drive letters on windows platforms. It simply replaces path
     * seperator characters.
     *
     * @return The path as a platform specific string.
     */
    public String asPlatformSpecificString() {
        return (isAbsolute()) ?
            File.separator + ps.asString(File.separator) :
            ps.asString(File.separator);
    }

    /**
     * Return a string represenation of the Path object.
     * <p><strong>WARNING:</strong> The string obtained from this method should
     * NOT be used as an externalized version of the path.
     *
     * @return a string representation of the Path object
     */
    public String toString() {
        return asString();
    }

    /**
     * Return true if the current path has a parent path.
     *
     * @return true if the current path has a parent path.
     */
    public boolean hasParent() {
        return ps.size() > 1;
    }

    /**
     * Return true if the Path contains no path fragments.
     *
     * @return true if the path contains no path fragments.
     */
    public boolean isEmpty() {
        return ps.isEmpty();
    }

    /**
     * Return the number of path fragments
     *
     * @return the number of path fragments
     */
    public int getNumberOfFragments() {
        return ps.size();
    }

    /**
     * Return the path fragment at the specified index
     *
     * @param index the index of the path fragment to return
     * @return the path fragment at the specified index.
     */
    public String getFragment(int index) {
        return ps.getFragment(index);
    }

    /**
     * Return true if the path is absolute, false if it is negative.
     *
     * @param path test this path to see if it is absolute
     * @return true if the provided path is abolute, false otherwise
     */
    private static boolean isAbsolute(String path) {
        boolean result = false;
        if (path.length() > 0 && path.charAt(0) == SEPARATOR_CHAR) {
            result = true;
        }
        return result;
    }

    /**
     * Return the hashcode for this path
     *
     * @return the hashcode for this path
     */
    public int hashCode() {
        return path.hashCode();
    }

    /**
     * Return true if obj is equal to this Path
     *
     * @param obj the object to test for equality
     * @return true if obj is equal to this
     */
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj.getClass() == getClass()) {
            Path p = (Path) obj;
            result = path.equals(p.path) && isAbsolute() == p.isAbsolute();
        }
        return result;
    }

    /**
     * Return the number of leading path fragments that this Path and
     * <code>path</code> share. The method does not check that the paths have
     * the same value for {@link Path#isAbsolute()}.
     *
     * @param path the path to compare to this path.
     * @return the number of path fragments that this path and the provided
     *         path share in common. Will return 0 if none.
     */
    public int commonFragments(Path path) {
        boolean commonPrefix = false;
        boolean commonFragment = true;
        int limit =
            Math.min(getNumberOfFragments(), path.getNumberOfFragments());
        int startIndex = 0;

        for (int i = 0; i < limit && commonFragment; i++) {

            commonFragment =
                getFragment(i).equals(path.getFragment(i));
            if (commonFragment) {
                if (!commonPrefix) {
                    // indicate that we have found at least on fragment in
                    // common
                    commonPrefix = true;
                }
                startIndex = i + 1;

            }
        }
        return startIndex;
    }

    /**
     * Return true if this path starts with the path fragments from the
     * specified path.
     *
     * @param path the path to check
     * @return true if this path starts with the path fragments in the supplied
     *         path. This method does not check {@link Path#isAbsolute()}
     */
    public boolean startsWith(Path path) {
        boolean result = false;
        int i = commonFragments(path);
        if (i > 0 && i == path.getNumberOfFragments()) {
            result = true;
        }
        return result;
    }

    /**
     * Return a list of all directories upto and including the name of this
     * Path in lenght order (shortest to longest)
     *
     * <p>e.g. "/a/b/c.txt" would result a list containing:</p> <or>
     * <li>/a</li> <li>/a/b</li> <li>/a/b/c.txt</li> </or>
     *
     * @return a list of all directories upto and including the name of this
     *         Path
     */
    public List getPaths() {
        int limit = getNumberOfFragments();
        List result = new ArrayList(limit);
        if (limit > 0) {

            for (int i = 1; i < limit; i++) {
                result.add(new Path(this, 0, i));
            }
        }
        result.add(this);
        return result;
    }


    /**
     * Return the name of this file or directory this path corresponds to. This
     * is the last path fragment or null if the path is empty
     *
     * @return the name of this file or directory this path corresponds to
     */
    public String getName() {
        String result = null;

        if (isAbsolute()) {
            result = "";
        }
        if (!isEmpty()) {
            result = getFragment(getNumberOfFragments() - 1);
        }

        return result;
    }


    /**
     * Return the normalized representation of the provided path. The
     * normalized path will never have a leading or trailing "/", will never
     * contain mutliple separators (e.g. "///"), will never contain current
     * directory markers (e.g. "/./") and will never contain parent directory
     * fragments (e.g. "/../").
     *
     * <p><strong>WARNING:</strong> care should be taken when calling this
     * method</p>
     *
     * @param path the path to normalize
     * @return the normalized path
     */
    private static String normalize(PathStack pStack, String path) {

        String[] splits = path.split("/");

        for (int i = 0; i < splits.length; i++) {
            String s = splits[i];

            if (!"".equals(s) && !".".equals(s) && !"..".equals(s)) {
                pStack.push(s);
            } else if (s.equals("..")) {
                pStack.pop(); // just loose the previous fragment
            }

        }
        return pStack.asString();
    }

    /**
     * Creates new relative path containing fragment elements from index 1 to
     * the end of the path.
     * 
     * @return path containing fragment elements form index 1 to the end of
     *         path if number of fragments is greater than 1.
     * @throws IllegalStateException
     *         if number of fragments &lt; 1
     */
    public Path getChild() {

        if (getNumberOfFragments() < 1){
                throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("path-has-no-child", path));
        } else {
            return new Path(this, 1, getNumberOfFragments());
        }
    }

    /**
     * Return the last path fragment from the stack
     * 
     * @return the last path fragment on the stack
     */
    public String lastFragment() {
        return ps.peek();
    }

    /**
     * A simple stack for manipulation of path fragments.
     */
    private static class PathStack {

        /**
         * The array holding the stack
         */
        private List list = null;

        /**
         * Default constructor
         */
        private PathStack() {
            list = new ArrayList();
        }

        /**
         * Copy constructor. Note that this method will create a copy of the
         * path stack that will reflect changes to the pathstack it is copied
         * from.
         * 
         * @param pathStack
         *        the pathstack to copy
         * @param start
         *        low endpoint (inclusive) of the stack to copy
         * @param end
         *        high endpoint (exclusive) of the stack to copy
         * @throws IndexOutOfBoundsException
         *         for an illegal endpoint index value (start &lt; 0 || end
         *         &gt; size || start &gt; end).
         */
        private PathStack(PathStack pathStack, int start, int end) {
            list = pathStack.list.subList(start, end);
        }

        /**
         * Copy constructor
         *
         * @param pathStack the pathstack to copy
         */
        private PathStack(PathStack pathStack) {
            this(pathStack, 0, pathStack.size());
        }

        /**
         * Peek at the last path fragment on the stack
         *
         * @return the value of the last path fragment on the stack
         */
        public String peek() {
            int size = list.size();
            if (size <= 0) {
                throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "path-has-no-parent", asString()));
            }
            return (String) list.get(list.size() - 1);
        }

        /**
         * Push a new path fragment onto the stack
         *
         * @param value the path fragment to push onto the path
         */
        public void push(String value) {
            list.add(value);
        }

        /**
         * Pop the last path fragment from the stack
         *
         * @return the last path fragment on the stack.
         *
         * @throws IllegalStateException if no fragments are left on the stack
         */
        public String pop() {
            int size = list.size();
            if (size <= 0) {
                throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "path-has-no-parent", asString()));
            }
            return (String) list.remove(list.size() - 1);
        }

        /**
         * Return the path fragment at the specified index.
         *
         * @param index the index of the fragment to get
         * @return the path of the fragment at the specified index
         */
        private String getFragment(int index) {
            return (String) list.get(index);
        }

        /**
         * Return the string representation of the path in this fragment stack
         *
         * @param separator the separator to use
         * @return the string representation of the path in this fragment
         *         stack
         */
        public String asString(String separator) {
            if (separator == null) {
                throw new IllegalArgumentException(
                    "Cannot be null: separator");
            }
            StringBuffer sb = new StringBuffer();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    sb.append(separator);
                }
                sb.append(list.get(i));
            }
            return sb.toString();
        }

        /**
         * @return
         */
        public String asString() {
            return asString(Path.SEPARATOR_STRING);
        }

        /**
         * Return the number of path fragments in the stack
         *
         * @return the number of path fragments in the stack
         */
        public int size() {
            return list.size();
        }

        /**
         * Return true if the stack is empty
         *
         * @return true if the stack is empty
         */
        public boolean isEmpty() {
            return list.isEmpty();
        }

    }
}
