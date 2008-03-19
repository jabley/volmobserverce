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

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PathTestCase extends TestCase {

    /**
     * Standard test case contructor
     *
     * @param testCaseName the name of the test case
     */
    public PathTestCase(String testCaseName) {
        super(testCaseName);
    }

    public void testMe() throws Exception {
        final String pstring = "/localhost/model/languages/Messages.properties";
        Path path = Path.parse(pstring);
        String pss = path.asPlatformSpecificString();
        System.out.println("pss is: " + pss);
    }

    /**
     * @throws Exception
     */
    public void testParseNull() throws Exception {
        try {
            Path p = Path.parse(null);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException iae) {
            //success
        }
    }

    /**
     * @throws Exception
     */
    public void testParseEmpty() throws Exception {

        Path p = Path.parse("");
        assertEquals("", p.asString());
    }

    /**
     * @throws Exception
     */
    public void testParseBrokenRelative() throws Exception {
        try {
            Path p = Path.parse("..");
            fail("Exception should have been thrown");
        } catch (IllegalStateException ise) {
            //success
        }
        try {
            Path p = Path.parse("..//");
            fail("Exception should have been thrown");
        } catch (IllegalStateException ise) {
            //success
        }
    }

    /**
     * Three dots is a file or directory name
     *
     * @throws Exception
     */
    public void testMoreThan3DotsPathFragment() throws Exception {

        doTest("...", "...", false);
        doTest(".....", ".....", false);
        doTest(".a.", ".a.", false);
        doTest("...b", "...b", false);
    }

    /**
     * @throws Exception
     */
    public void testSimpleRelativePaths() throws Exception {
        doTest("", ".", false);
        doTest("", "./", false);
        doTest("", ".///", false);
        doTest("a", "a", false);
        doTest(".a", ".a", false); // yep this is a file name
        doTest("a", "./a", false);
        doTest("a", "./////a", false);
    }

    /**
     * @throws Exception
     */
    public void testSimpleAbsolutePaths() throws Exception {
        doTest("/", "/", true);
        doTest("/", "////", true);
        doTest("/a", "/a", true);
        doTest("/a", "//a", true);
        doTest("/b/b", "/////b/b", true);
    }


    /**
     * Test some stupidly complex paths.
     *
     * @throws Exception
     */
    public void testComplexPaths() throws Exception {
        doTest("/b/c", "///b/../b///.//c//./", true);
        doTest("b/c/d",
               "d/../e///f..//././..//.//../b///././././///c//../././c/././//d",
               false);
    }

    /**
     * Test that an exception is thrown if there is no parent.
     *
     * @throws Exception
     */
    public void testGetParentNoParent() throws Exception {

        //  null paths cause failure in constructor so we cannot test
        // getparent on empty paths.

        try {
            Path.parse("").getParent();
            fail("Exception should have been thrown");
        } catch (IllegalStateException ise) {
            // success
        }

        try {
            Path.parse("/").getParent();
            fail("Exception should have been thrown");
        } catch (IllegalStateException ise) {
            // success
        }

        {
            Path p = Path.parse("a");
            try {
                p.getParent();
                fail("Exception should have been thrown");
            } catch (IllegalStateException ise) {
                // success
            }
        }
    }

    /**
     * @throws Exception
     */
    public void testGetParent() throws Exception {
        assertEquals("/", Path.parse("/a").getParent().asString());
        assertEquals("a", Path.parse("a/b").getParent().asString());
        assertEquals("a/b/c", Path.parse("a/b/c/d").getParent().asString());
        assertEquals("b/c",
                     Path.parse(
                         "d/../e///f..//././..//.//../b///././././///c//../././c/././//d")
                     .getParent()
                     .asString());
        assertEquals("/b/c", Path.parse(
            "/d/../e///f..//././..//.//../b///././././///c//../././c/././//d")
                             .getParent().asString());
    }

    /**
     * @throws Exception
     */
    public void testCannotResolve() throws Exception {

        try {
            assertEquals("/", Path.parse("/").resolve("/"));
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException ise) {
            // success
        }

        try {
            assertEquals("/a/b/../c", Path.parse("/a/b/c").resolve("/a/b/c"));
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException ise) {
            // success
        }
    }

    /**
     * @throws Exception
     */
    public void testAsRelative() throws Exception {
        assertEquals("", Path.parse("/").asRelativeString());
        assertEquals("a/b", Path.parse("a/b//").asRelativeString());
        assertEquals("a/b", Path.parse("///a/b///").asRelativeString());

    }

    /**
     * @throws Exception
     */
    public void testAsAbsolute() throws Exception {
        assertEquals("/", Path.parse("").asAbsoluteString());
        assertEquals("/a/b", Path.parse("///a/b//").asAbsoluteString());
    }

    /**
     * Test the reolve(String) method
     *
     * @throws Exception
     */
    public void testResolveString() throws Exception {
        assertEquals("a/b/c", Path.parse("a///").resolve("b/c").asString());
        assertEquals("/a/b/c",
                     Path.parse("///a/b//").resolve("c///").asString());
    }

    /**
     * Test the resolve(String, String) static method
     *
     * @throws Exception
     */
    public void testResolvePath() throws Exception {
        assertEquals("a/b/c", Path.resolve("a///", "b/c").asString());
        assertEquals("/a/b/c", Path.resolve("///a/b//", "c///").asString());
    }


    /**
     * See that getName works
     *
     * @throws Exception
     */
    public void testGetName() throws Exception {
        assertEquals("a", Path.parse("a").getName());
        assertEquals("a", Path.parse("a").getName());
        assertEquals(null, Path.parse("").getName());
        assertEquals("", Path.parse("/").getName());
        assertEquals("c.txt",
                     Path.parse("/a/b/c/../../../a/b/c.txt").getName());

    }

    /**
     * List the paths in the Path
     */
    public void testListPaths() {

        List list = Path.parse("").getPaths();
        assertEquals("list should have one item", 1, list.size());

        list = Path.parse("/").getPaths();
        assertEquals("list should have one item", 1, list.size());

        list = Path.parse("/a/b/c.txt").getPaths();
        assertEquals("list shoudl contain 3 items", 3, list.size());
        assertEquals(list.get(0), Path.parse("/a"));
        assertEquals(list.get(1), Path.parse("/a/b"));
        assertEquals(list.get(2), Path.parse("/a/b/c.txt"));

    }

    /**
     * Test the common fragments method
     *
     * @throws Exception
     */
    public void testCommonFragments() throws Exception {
        Path a = Path.parse("/a/b/c/d/e.txt");
        Path b = Path.parse("/a/b/c/f/g");
        Path c = Path.parse("/a/b/c");
        Path d = Path.parse("a/b/c");

        assertEquals("three fragments", 3, a.commonFragments(b));
        assertEquals("three fragments", 3, c.commonFragments(d));

    }

    /**
     * Test the statsWith method
     *
     * @throws Exception
     */
    public void testStartsWith() throws Exception {
        Path a = Path.parse("/a/b/c/d/e.txt");
        Path b = Path.parse("/a/b/c/f/g");
        Path c = Path.parse("/a/b/c");
        Path d = Path.parse("a/b/c");

        assertFalse(a.startsWith(b));
        assertTrue(a.startsWith(c));
        assertTrue(a.startsWith(d));
        assertFalse(d.startsWith(b));
    }

    /**
     * Generic test method.
     *
     * @param expected   the expected string result
     * @param input      the input string
     * @param isAbsolute true if the result shuld be absolute
     */
    private void doTest(String expected, String input, boolean isAbsolute) {
        Path p = Path.parse(input);
        assertEquals(expected, p.asString());
        assertEquals(isAbsolute, p.isAbsolute());
    }


    /**
     * Test the behaviour of the Path as a key in hashmaps
     *
     * @throws Exception
     */
    public void testHashMap() throws Exception {
        HashMap map = new HashMap();

        map.put(Path.parse("/"), "/");
        map.put(Path.parse("/a"), "/a");
        assertEquals("Should be two entries", 2, map.size());
        map.remove(Path.parse("/a"));
        assertEquals("Should be one entry", 1, map.size());

        map.put(Path.parse("/a/b"), "/a/b");
        map.put(Path.parse("/b"), "/b");
        map.put(Path.parse("/b/c/"), "/b/c/");
        assertEquals("should be four entries", 4, map.size());

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Path p = (Path) it.next();
            it.remove();
        }

        assertEquals("should be nothing left in the map", 0, map.size());
    }
    
    /**
     * 
     */
    public void testGetChild() {

        assertEquals("", Path.parse("a").getChild().asString());
        assertEquals("b", Path.parse("a/b").getChild().asString());
        assertEquals("b/c/d", Path.parse("a/b/c/d").getChild().asString());
        assertEquals(
                "c/d",
                Path.parse("/d/../e///f//././..//.//../b///././././///c//" +
                        "../././c/././//d")
                        .getChild().asString());
    }
}
