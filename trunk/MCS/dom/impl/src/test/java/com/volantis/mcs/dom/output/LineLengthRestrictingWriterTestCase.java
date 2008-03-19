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
package com.volantis.mcs.dom.output;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Test case for a {@link LineLengthRestrictingWriter}.
 */
public class LineLengthRestrictingWriterTestCase extends TestCaseAbstract {

    /**
     * Constant for the new line character.
     */
    private static String NEW_LINE = "\n";

    /**
     * Constructor.
     * @param name the name of this test case
     */
    public LineLengthRestrictingWriterTestCase(String name) {
        super(name);
    }

    /**
     * Ensure that new line introduced when max line length is
     * exceeded and whitespace exists in the line.
     *
     * @throws Throwable
     */
    public void testWriteCharUntilMaxLineLengthExceeded() throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        llrw.write('a');
        llrw.write('b');
        llrw.write('c');
        llrw.write('d');
        llrw.write('e');
        llrw.write('f');
        llrw.write(' ');
        llrw.write('h');
        llrw.write('i');
        llrw.write('j');
        // line length exceeded.

        llrw.write('k');

        // flush the writer. Note that this will add a line seperator after
        // the remaining content in the buffer.
        llrw.flush();
        String contentWritten = llrw.toString();

        String expected = "abcdef" + NEW_LINE + "hijk";

        assertEquals("The strings should match", expected, contentWritten);
    }

    /**
     * Ensure that new line introduced when max line length is
     * exceeded and new line exists in the line.
     */
    public void
            testWriteCharUtilMaxLineLengthExceededWhenLineHasLineSeparator()
        throws Throwable {

        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        llrw.write('a');
        llrw.write('b');
        llrw.write('c');
        llrw.write('d');
        llrw.write('e');
        llrw.write('f');

        char newline = '\n';
        llrw.write(newline);
        llrw.write('h');
        llrw.write('i');
        llrw.write('j');
        // line length exceeded.

        llrw.write('k');

        // flush the writer. Note that this will add a line seperator after
        // the remaining content in the buffer.
        llrw.flush();
        String contentWritten = llrw.toString();

        String expected = "abcdef" + NEW_LINE + "hijk";

        assertEquals("The strings should match", expected, contentWritten);
    }

    /**
     * Ensure that whitespace is replaced by newline character.
     */
    public void testWriteCharUntilLineLimitReachedAndNextCharIsWhitespace()
        throws Exception {
        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        llrw.write('a');
        llrw.write('b');
        llrw.write('c');
        llrw.write('d');
        llrw.write('e');
        llrw.write('f');
        llrw.write('g');
        llrw.write('h');
        llrw.write('i');
        llrw.write('j');
        // line length exceeded.

        llrw.write(' ');

        String contentWritten = llrw.toString();

        String expected = "abcdefghij" + NEW_LINE;

        assertEquals("The strings should match", expected, contentWritten);
    }

    /**
     * Ensure that we break the line on immediately before '<' when
     * there is no whitespace in the line.
     */
    public void testWriteCharUntilLineLimitExceededWhenContainsAngledBracket()
        throws Exception {

        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        llrw.write('a');
        llrw.write('b');
        llrw.write('c');
        llrw.write('d');
        llrw.write('e');
        llrw.write('<');
        llrw.write('f');
        llrw.write('>');
        llrw.write('g');
        llrw.write('h');
        // line length exceeded.

        llrw.write('i');
        llrw.flush();

        String contentWritten = llrw.toString();

        String expected = "abcde" + NEW_LINE + "<f>ghi";

        assertEquals("The strings should match", expected, contentWritten);
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * This particular test ensures that we can add some char arrays to the
     * buffer and that they are succesfully added, when there are no line
     * breaks in the arrays, and there is no need to add line breaks manually.
     */
    public void testwriteCharArrayPlainWriteOut() throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "there";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1,0,cbuf1.length);

        // now lets write out the second char array
        llrw.write(cbuf2, 0, cbuf2.length);
        // dont forget to flush as line limit has not been exceeded yet
        llrw.flush();

        // ensure that it looks as expected
        String expected = testString1 + testString2;
        assertEquals("The strings should match", expected, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>This particular test ensures that the results are as expected when the
     * new buffer is too big to fit on the current line, and there are no
     * natural breaks in the line, and we can not insert any in any whitespace
     * because there is no whitespace.
     */
    public void
            testwriteCharArrayWhenMaxLineSizeAndExceededAndBreakNotAvailable()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "there1";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array, which will exceed the line
        // length.
        llrw.write(cbuf2, 0, cbuf2.length);

        llrw.flush();
        // ensure that it looks as expected
        String expected = "1";
        assertEquals("The strings should match", expected, (llrw.toString()));

    }

    /**
     * Ensure that a line is not broken immediately after the &lt; character.
     * <p>
     * We dont want a document that looks like: <br>
     * &lt; <br>
     * form&gt;
     * <p>
     * The above example breaks most browsers (probably all).
     */
    public void testWriteCharArrayDoesNotSplitLineImmediatelyAfterOpenElement()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello1234<";
        String testString2 = "div>";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array which is too big to fit on
        // the line... The < character needs to be placed on the next line
        // in addition to the new content being added.
        llrw.write(cbuf2, 0, cbuf2.length);

        llrw.flush();

        // ensure that it looks as expected
        String expected = "hello1234" + NEW_LINE + "<div>";
        assertEquals("The strings should match", expected, (llrw.toString()));
    }


    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>This particular test ensures that when a natural link break occurs
     * the buffer is written out correctly on the same line as the previous
     * entry, and the characters after the line break are written out after
     * a new line character.</p>
     *
     * <p>The character buffer would not have been deemed to fit on the
     * current line - due to the number of characters exceeding the number of
     * characters remaining for that line - it is only due to the line break
     * that some characters can be placed on the current line.</p>
     */
    public void testwriteCharArrayNaturalLineBreakWithinLimit()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "there" + NEW_LINE + 1;

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array which is too big to fit on
        // the line... but has a naturally occuring line break, and thus
        // should actually work out ok.
        llrw.write(cbuf2, 0, cbuf2.length);

        llrw.flush();

        // ensure that it looks as expected
        String expected = testString1 + testString2;
        assertEquals("The strings should match", expected, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>This particular test ensures that when a natural link break occurs
     * the buffer is written out correctly on the same line as the previous
     * entry, and the characters after the line break are written out after
     * a new line character.</p>
     *
     * <p>The character buffer should have been deemed to fit on the
     * current line - since the number of characters in the cbuf1 are less
     * than the remaining characters.</p>
     */
    public void testwriteCharArrayNaturalLineBreakWithinLimitAndLength()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        // ensure that we have all characters remaining
        //testCharactersRemaining(llrw, lineLimit);

        String testString1 = "hello" + NEW_LINE + "there";
        String testString2 = "hi";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array which should be appended
        // to the new line
        llrw.write(cbuf2, 0, cbuf2.length);
        llrw.flush();

        // ensure that it looks as expected
        String expected = testString1 + testString2;
        assertEquals("The strings should match", expected, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>Here we are specifically trying to test that the creation of line
     * breaks in place of whitespace is working as expected.</p>
     */
    public void testwriteCharArrayArtificialLineBreak()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "hi this is too big";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array - part of which should be
        // appended to the current line, (up to 'hi'), then a new line character
        // should replace the space and the rest should be on the new line up
        // to the word 'too' since this does not fit onto one line,
        // another artificial separator should have been inserted after the
        // word too and then the word 'big' should just be written out.
        llrw.write(cbuf2, 0, cbuf2.length);

        llrw.flush();

        // ensure that it looks as expected
        String expected = "hellohi" + NEW_LINE
                + "this is" + NEW_LINE + "too big";

        assertEquals("The strings should match", expected, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>Here we are specifically trying to test that the creation of line
     * breaks in place of whitespace is working as expected when the
     * character array has been given a start index and number of
     * characters to output.</p>
     */
    public void testwriteCharArrayArtificialLineBreakNonStandard()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "hi this is too big";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array - the 'h' from i should be
        // ommited but i should be appended to the current line, Also the 'g'
        // from big should be ommited.
        llrw.write(cbuf2, 1, cbuf2.length-2);
        llrw.flush();

        // ensure that it looks as expected
        String expected = "helloi" + NEW_LINE
                + "this is" + NEW_LINE + "too bi";

        assertEquals("The strings should match", expected, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes the char array, start index and length. This method is called by
     * all the other methods apart from the method which takes an int.
     *
     * <p>Here we are specifically trying to test that a line which is too
     * big to fit on a line is not written out.</p>
     */
    public void testwriteCharArrayArtificialLineBreakNonS()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 10;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hello";
        String testString2 = "hithisistoobig";

        char cbuf1[] = testString1.toCharArray();
        char cbuf2[] = testString2.toCharArray();

        // add the first string
        llrw.write(cbuf1, 0, cbuf1.length);

        // now lets write out the second char array - the 'h' from i should be
        // ommited but i should be appended to the current line, Also the 'g'
        // from big should be ommited.
        llrw.write(cbuf2, 0, cbuf2.length);

        llrw.flush();

        String expectedResult = "sistoobig";

        // ensure that it looks as expected
        assertEquals("The strings should match", expectedResult,
                     (llrw.toString()));
    }

    /**
     * Tests {@link LineLengthRestrictingWriter#write}.
     *
     * <p>This method ensures that if the last character written out was not
     * a whitespace - then neither a new line character nor the character
     * represented by the int should be written out.</p>
     */
    public void testWriteCharacterWhenMaxLineSizeExceededAndBreakNotAvailable()
            throws Throwable {

        String initial = "hello";
        char c = 'a';
        int linelimit = 5;

        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, linelimit);

        String expected = "a";
        String result = null;

        try {
            llrw.write(initial);
            llrw.write(c);
            llrw.flush();
            result = llrw.toString();
            // test that the new char has been written on a new line
            assertEquals("String is not as expected ", expected, result);
        } catch (IOException e) {
            fail("write should not throw an IOException: " + e);
        }
    }

    /**
     * Tests {@link LineLengthRestrictingWriter#write}.
     *
     * <p>This method ensures that if the last character written out was a
     * whitespace - then both a new line character as well as the character
     * represented by the int should be written out.</p>
     */
    public void testWriteCharacterCreatesNewLine() throws Throwable {
        String initial = "hell ";
        char c = 'a';
        int linelimit = 5;

        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, linelimit);

        String expected = "hell" + NEW_LINE + c;
        String result = null;

        try {
            llrw.write(initial);
            llrw.write(c);
            llrw.flush();
            result = llrw.toString();
            // test that the new char has been written on a new line
            assertEquals("String is not as expected ", expected, result);
            // test that the counter has been set correctly
        } catch (IOException e) {
            fail("write should not throw an IOException: " + e);
        }
    }


    /**
     * Tests {@link LineLengthRestrictingWriter#write} does not create a new
     * line when one is not required as the line is taken to its limit.
     */
    public void testWriteCharacterNoNewLine() throws Throwable {
        String initial = "hello";
        char c = 'a';
        int linelimit = 6;

        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, linelimit);

        String expected = initial + c;
        String result = null;

        try {
            llrw.write(initial);
            llrw.write(c);
            llrw.flush();
            result = llrw.toString();
            // test that the new char has been written on a new line
            assertEquals("String is not as expected ", expected, result);
        } catch (IOException e) {
            fail("write should not throw an IOException: " + e);
        }
    }

    /**
     * Tests {@link LineLengthRestrictingWriter#write} when called with a
     * single character.
     */
    public void testWriteCharacterInit() throws Throwable {
        char c = 'a';
        int linelimit = 5;

        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, linelimit);

        // Note that we expected a line seperator as the character 'a' is
        // buffered and we need to flush to write, which adds a line seperator
        // character.
        String expected = "a";
        String result = null;

        try {
            llrw.write(c);

            llrw.flush();

            result = llrw.toString();
            // test that the new char has been written on a new line
            assertEquals("String is not as expected ", expected, result);

        } catch (IOException e) {
            fail("write should not throw an IOException: " + e);
        }
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     * @throws Throwable
     */
    public void testwriteString()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 12;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);


        String testString1 = "hellothere";
        String testString2 = "hi";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 0, testString2.length());

        // now try and write another String - this should cause the character
        // array method to be called since a newline needs to be inserted
        String testString3 = "yo";
        llrw.write(testString3,0, testString3.length());
        llrw.flush();

        String expected = "yo";

        // ensure that it looks as expected
        assertEquals("The strings should match", expected, llrw.toString());
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     * @throws Throwable
     */
    public void testwriteStringWithWhitespaceOwing()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 13;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hellothere ";
        String testString2 = "hi";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 0, testString2.length());

        llrw.flush();

        // ensure that it looks as expected
        String expected2 = testString1 + testString2;
        assertEquals("The strings should match", expected2, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     *
     * This particular test ensures that when there is whitespace owing, it is
     * replaced by a new line character.
     *
     * @throws Throwable
     */
    public void testwriteStringWithWhitespaceOwingIsReplaced()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 13;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hellothere ";
        String testString2 = "hits";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 0, testString2.length());

        llrw.flush();
        // ensure that it looks as expected
        String expected2 = "hellothere" + NEW_LINE + testString2;
        assertEquals("The strings should match", expected2, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     *
     * <p>This particular test ensures that when there is whitespace owing, it is
     * replaced by a new line character.</p>
     *
     * <p>We are ensuring that this test works as expected when the entire String
     * is not being printed i.e. the offset and length parameters have been
     * specified.</p>
     *
     * @throws Throwable
     */
    public void testwriteStringWithWhitespaceOwingIsReplacedOffset()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 13;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hellothere ";
        String testString2 = "xhitsx";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 1, testString2.length()-2);
        llrw.flush();

        // ensure that it looks as expected
        String expected2 = "hellothere" + NEW_LINE + "hits";
        assertEquals("The strings should match", expected2, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     *
     * <p>This particular test ensures that when there is whitespace owing, it is
     * replaced by a new line character and that because the new string to
     * be added contains whitespace - the counters and flags are set as
     * is expected.</p>
     *
     * @throws Throwable
     */
    public void testwriteStringWithWhitespaceOwingWithWhitespaceInNewString()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 13;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);



        String testString1 = "hellothere ";
        String testString2 = "hits ";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 0, testString2.length());
        llrw.flush();

        // ensure that it looks as expected
        String expected2 = "hellothere" + NEW_LINE + "hits ";
        assertEquals("The strings should match", expected2, (llrw.toString()));
    }

    /**
     * This tests the {@link LineLengthRestrictingWriter#write} method which
     * takes a String, an offset and the length.
     *
     * This particular test ensures that when there is whitespace owing, it is
     * replaced by a new line character and that because the new string to
     * be added contains whitespace - the counters and flags are set as
     * is expected.
     *
     * <p>This variant of a test ensures that the expected results are obtained
     * when we are not writing out a complete String, i.e. the offset and
     * length have been specified.</p>
     *
     * @throws Throwable
     */
    public void
            testwriteStringWithWhitespaceOwingWithWhitespaceInNewStringOffset()
            throws Throwable {

        // create a LineLengthRestrictingWriter
        int lineLimit = 13;
        StringWriter sw = new StringWriter();
        LineLengthRestrictingWriter llrw =
                new LineLengthRestrictingWriter(sw, lineLimit);

        String testString1 = "hellothere ";
        String testString2 = "xhits x";

        // add the first string
        llrw.write(testString1, 0, testString1.length());

        // now lets write out the second String which should be put on the
        // same line
        llrw.write(testString2, 1, testString2.length()-2);

        llrw.flush();
        // ensure that it looks as expected
        String expected2 = "hellothere" + NEW_LINE + "hits ";
        assertEquals("The strings should match", expected2, (llrw.toString()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 03-Oct-05	9683/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 30-Sep-05	9583/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 04-Jul-05	8894/7	tom	VBM:2005062107 Added Extra Tests for witheld whitespace

 01-Jul-05	8894/5	tom	VBM:2005062107 Added line break to lines which are too big for device

 01-Jul-05	8894/3	tom	VBM:2005062107 Added line break to lines which are too big for device

 01-Jul-05	8894/1	tom	VBM:2005062107 Added line break to lines which are too big for device

 ===========================================================================
*/
