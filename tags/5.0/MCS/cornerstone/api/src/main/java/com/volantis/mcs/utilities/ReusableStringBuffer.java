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
 * $Header: /src/voyager/com/volantis/mcs/utilities/ReusableStringBuffer.java,v 1.5 2002/12/31 16:58:32 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Feb-02    Allan           VBM:2002013101 - Created. A class that has a
 *                              similar functionality to that of StringBuffer
 *                              except that it does not become shared when
 *                              converted into a String thus avoiding the need
 *                              to make a copy each time the length is set.
 * 13-Feb-02    Allan           VBM:2002013101 - Added append(char[],int,int),
 *                              fixed a few bugs and made append(Object) call
 *                              append(ReusableStringBuffer) if it can.
 * 21-Mar-02    Doug            VBM:2002032004 - Fixed a bug in the 
 *                              append(char[],int,int) method and removed the 
 *                              unecessary loop that filled the value char[] 
 *                              with null characters in the setLength() method.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols.
 * 25-Mar-02    Allan           VBM:2002022007 - Added trim().
 * 14-Oct-02    Sumit           VBM:2002070803 - Added logging
 * 30-Dec-02    Byron           VBM:2002071015 - This class no longer extends
 *                              from PooleableObject.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Like a StringBuffer but designed with re-use in mind. The methods in this
 * class are unsynchronized.
 */
public final class ReusableStringBuffer {

    /**
     * Volantis copyright.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ReusableStringBuffer.class);

    /**
     * The contents of this buffer.
     */
    private char value[];

    /**
     * The number of characters in the buffer.
     */
    private int length;

    /**
     * Expand the capacity of this ReusableStringBuffer.
     * @param minimumCapacity the minimum capacity to expand to
     */
    private void expandCapacity(int minimumCapacity) {
        int newCapacity = value.length * 2;
        
        if(newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        } else if(newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }

        char newValue[] = new char[newCapacity];
        System.arraycopy(value, 0, newValue, 0, length);
        value = newValue;
    }

    /**
     * Contructs a new ReusableStringBuffer with an initial capacity of
     * 16 characters.
     */
    public ReusableStringBuffer() {
      this(16);
    }

    /**
     * Contructs a new ReusableStringBuffer with the given initial capacity.
     * @param initialCapacity the initial capacity for this StringBuffer
     */
    public ReusableStringBuffer(int initialCapacity) {
        value = new char[initialCapacity];
        length = 0;
    }

    /**
     * Contructs a new ReusableStringBuffer with the contents of the given
     * String and an initial capacity of double the length of the String.
     * @param s the String with which to initialize this ReusableStringBuffer
     */
    public ReusableStringBuffer(String s) {
        value = new char[s.length()*2];
        length = 0;
        append(s);
    }

    /**
     * Returns the length of this ReusableStringBuffer.
     * @return the length of this ReusableStringBuffer.
     */
    public int length() {
        return length;
    }

    /**
     * Set the length of this ReusableStringBuffer.
     * @param newLength the new length for this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if newLength is negative
     */
    public void setLength(int newLength) {
    	if (newLength < 0) {
    	    throw new StringIndexOutOfBoundsException(newLength);
    	}
        if (newLength > value.length) {
            expandCapacity(newLength);
        } 
        length = newLength;
    }

    /**
     * Returns the capacity of this ReusableStringBuffer.
     * @return the capacity of this  ReusableStringBuffer.
     */
    public int capacity() {
        return value.length;
    }

    /**
     * Return a specified char in this ReusableStringBuffer.
     * @param index the index of the char to return
     * @throws StringIndexOutOfBoundsException if index is negative or more 
     * than the length of this ReusableStringBuffer.
     */
    public char charAt(int index) {
        if(index<0 || index>length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    /**
     * Set the char at the specified index to the specified char.
     * @param index the index of the char to set
     * @param c the new value for the specified char
     * @throws StringIndexOutOfBoundsException if index is negative or more 
     * than the length of this ReusableStringBuffer.
     */
    public void setCharAt(int index, char c) {
        if(index<0 || index>length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        value[index] = c;
    }

    /**
     * Get the char array used by this ReusableStringBuffer. Note 
     * that you should NOT use the array length attribute of the returned 
     * character array to determine the number of characters in the array as
     * this value is the capacity value. Instead, the length() method should 
     * be used to return the number of significant characters.
     * @return the char array used by this ReusableStringBuffer
     */
    public char[] getChars() {
        return value;
    }

    /**
     * Append the toString() result of the given object to the end of this
     * ReusableStringBuffer.
     * @param o the object to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(Object o) {
        if(o instanceof ReusableStringBuffer) {
            return append((ReusableStringBuffer)o);
        }
                                                 
        return append(o.toString());    
    }

    /**
     * Append the given String to the end of this ReusableStringBuffer.
     * @param s the String to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(String s) {
	if (s == null) {
	    s = String.valueOf(s);
	}

        int newLength = s.length() + length;
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        s.getChars(0, s.length(), value, length);
        setLength(newLength);

        return this;
    }   

    /**
     * Append the given StringBuffer to the end of this ReusableStringBuffer
     * without converting the StringBuffer to a String.
     * @param sb the String to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(StringBuffer sb) {
        if (sb == null) {
            return append("null");
        }

        int newLength = sb.length() + length;
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        sb.getChars(0, sb.length(), value, length);
        setLength(newLength);

        return this;
    }

    /**
     * Append the given ReusableStringBuffer to the end of this 
     * ReusableStringBuffer.
     * @param rsb the String to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(ReusableStringBuffer rsb) {
        if (rsb == null) {
            return append("null");
        }

        int newLength = rsb.length + length;
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        System.arraycopy(rsb.value, 0, value, length, rsb.length);

        setLength(newLength);

        return this;
    }
  
    /**
     * Append the given char array to the end of this ReusableStringBuffer.
     * This method assumes that the characters to append will be up to and
     * not including the first '\0' character should there be any such 
     * characters.
     * @param chars the char array to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(char chars[]) {

        int newLength = chars.length + length;
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        System.arraycopy(chars, 0, value, length, chars.length);

        setLength(newLength);

        return this;
    }  

    /**
     * Append the given char array to the end of this ReusableStringBuffer.
     * @param chars the char array to append
     * @param offset the offset from the beginning of chars
     * @param length the number of chars to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(char chars[], int offset, int length) {
        if(offset>=chars.length) {
            return this;
        }
        
        int charCount = (length + offset) > chars.length ? 
            chars.length - offset :
            length;

        int newLength = charCount + this.length;
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        System.arraycopy(chars, offset, value, this.length, charCount);

        setLength(newLength);

        return this;
    }  

    /**
     * Append the given char to the end of this ReusableStringBuffer.
     * @param c the character to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(char c) {
        int newLength = length + 1;
        setLength(newLength);

        value[newLength-1] = c;
        return this;
    }

    /**
     * Append the String representation of the given boolean to the end 
     * of this ReusableStringBuffer.
     * @param b the boolean whose value to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(boolean b) {
        if(b) { 
            return append("true");
        } else {
            return append("false");
        }
    }

    /**
     * Append the Integer.toString() representation of the given int to the 
     * end of this ReusableStringBuffer. (Assumes radix 10.)
     * @param i the int whose value to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(int i) {
        return append(Integer.toString(i));
    }

    /**
     * Append the Long.toString() representation of the given long to the 
     * end of this ReusableStringBuffer. (Assumes radix 10.)
     * @param l the long whose value to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(long l) {
        return append(Long.toString(l));
    }

    /**
     * Append the Float.toString() representation of the given float to the 
     * end of this ReusableStringBuffer. (Assumes radix 10.)
     * @param f the float whose value to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(float f) {
        return append(Float.toString(f));
    }

    /**
     * Append the Double.toString() representation of the given double to the 
     * end of this ReusableStringBuffer. (Assumes radix 10.)
     * @param d the double whose value to append
     * @return this ReusableStringBuffer
     */
    public ReusableStringBuffer append(double d) {
        return append(Double.toString(d));
    }

    /**
     * Delete a section of this ReusableStringBuffer keeping the capacity
     * the same.
     * @param start the start of the section to delete
     * @param end the end of the section to delete
     * @return this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if start is negative or more 
     * than the length of this ReusableStringBuffer or end is less than start.
     */
    public ReusableStringBuffer delete(int start, int end) {
        if(start<0 || start>length || end<start) {
            throw new StringIndexOutOfBoundsException("length is " +
                                                      length + ", start is " +
                                                      start + ", end is " +
                                                      end);
        }

        int moveLen = end - start;
        if(moveLen>0) {
            System.arraycopy(value, start+moveLen, value, start, length-end);
            setLength(length - moveLen);
        }
        return this;
    }

    /**
     * Delete the char at the specified index in this ReusableStringBuffer.
     * @param index the index of the char to delete
     * @return this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if index is negative or more 
     * than or equal to the length of this ReusableStringBuffer.
     */
    public ReusableStringBuffer deleteCharAt(int index) {
        return delete(index, index+1);
    }

    /**
     * Replace a subsection of the characters in this ReusableStringBuffer with
     * characters from an array. The substring begins at the index start and 
     * extends to the character at index end - 1 or to the end of the 
     * ReusableStringBuffer if no such character exists. First the characters 
     * in the substring are removed and then the characters from the array are
     * inserted at index start. (The ReusableStringBuffer will be lengthened to
     * accommodate the new characters if necessary.)
     * @param start the start of the subsection to replace (inclusive)
     * @param end the end of the subsection to replace (exclusive)
     * @param chars the character array to use for the replacement
     * @return this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if start is less than zero, if
     * start is more than end or start is more than the length of this
     * ReusableStringBuffer.
     */
    public ReusableStringBuffer replace(int start, int end, char[] chars) {
        if(start<0 || start>length || end<start) {
            throw new StringIndexOutOfBoundsException("length is " +
                    length + ", start is " +
                    start + ", end is " +
                    end);
        }

        // Make sure we dont fall off the end.
        if(end>length) {
            end=length;
        }
        
        int strlen = chars.length;
        int newLength = length + strlen - (end - start);
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        // Make room for the characters then insert them.
        System.arraycopy(value, end, value, start+strlen, length-end);
        int idx = start;
        for(int i = 0; i<strlen; i++ ) {
            value[idx++] = chars[i];
        }
        setLength(newLength);

        return this;
    }

    /**
     * Replace a subsection of the characters in this ReusableStringBuffer with
     * characters from the specified ReusableStringBuffer. The substring begins
     * at the index start and extends to the character at index end - 1 or to 
     * the end of the ReusableStringBuffer if no such character exists. First 
     * the characters in the substring are removed and then the specified 
     * ReusableStringBuffer is inserted at index start. (The 
     * ReusableStringBuffer will be lengthened to accommodate the new characters
     * if necessary.)
     * @param start the start of the subsection to replace (inclusive)
     * @param end the end of the subsection to replace (exclusive)
     * @param rsb the ReusableStringBuffer to use for the replacement
     * @return this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if start is less than zero, if
     * start is more than end or start is more than the length of this
     * ReusableStringBuffer.
     */
    public ReusableStringBuffer replace(int start, int end, 
                                        ReusableStringBuffer rsb) {

        if(start<0 || start>length || end<start) {
            throw new StringIndexOutOfBoundsException("length is " +
                    length + ", start is " +
                    start + ", end is " +
                    end);
        }

        // Make sure we dont fall off the end.
        if(end>length) {
            end=length;
        }
        
        int strlen = rsb.length();
        int newLength = length + strlen - (end - start);
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        // Make space for the string then insert it
        System.arraycopy(value, end, value, start+strlen, length-end);
        int idx = start;
        for(int i = 0; i<strlen; i++ ) {
            value[idx++] = rsb.charAt(i);
        }
        setLength(newLength);

        return this;
    }

    /**
     * Replace a subsection of the characters in this ReusableStringBuffer with
     * characters from the specified String. The substring begins
     * at the index start and extends to the character at index end - 1 or to 
     * the end of the ReusableStringBuffer if no such character exists. First 
     * the characters in the substring are removed and then the specified 
     * String is inserted at index start. (The ReusableStringBuffer will be 
     * lengthened to accommodate the new characters if necessary.)
     * @param start the start of the subsection to replace
     * @param end the end of the subsection to replace
     * @param s the String to use for the replacement
     * @return this ReusableStringBuffer
     * @throws StringIndexOutOfBoundsException if start is less than zero, if
     * start is more than end or start is more than the length of this
     * ReusableStringBuffer.
     */
    public ReusableStringBuffer replace(int start, int end, String s) {
        if(start<0 || start>length || end<start) {
            throw new StringIndexOutOfBoundsException("length is " +
                                                      length + ", start is " +
                                                      start + ", end is " +
                                                      end);
        }

        // Make sure we dont fall off the end.
        if(end>length) {
            end=length;
        }
        
        int strlen = s.length();
        int newLength = length + strlen - (end - start);
        if(newLength > value.length) {
            expandCapacity(newLength);
        }

        // Since we do not have access to the String's char array, we
        // need to delete and then insert...
        System.arraycopy(value, end, value, start+strlen, length-end);
        s.getChars(0, strlen, value, start);
        setLength(newLength);

        return this;
    }

    /**
     * Return a new String that consists of a subsection of this 
     * ReusuableStringBuffer. The subsection starts at the specified index
     * and ends at the end of this ReusableStringBuffer.
     * @param start the start index of the sub-string.
     * @throws StringIndexOutOfBoundsException if start is less than 
     * zero or start is greater than the length of the ReusableStringBuffer.
     */
    public String substring(int start) {
        return substring(start, length);
    }

    /**
     * Return a new String that consists of a subsection of this 
     * ReusuableStringBuffer. The subsection starts at the specified index
     * and ends at index end - 1. 
     * An exception is thrown if
     * @param start the start index of the sub-string.
     * @param end the end index of the sub-string.
     * @throws StringIndexOutOfBoundsException if start or end are less than 
     * zero, if start or end are greater than the length of the 
     * ReusableStringBuffer or start is greater than end.
     */
    public String substring(int start, int end) {
        if( start < 0 || end < 0 || start >= length || end > length || 
            end < start) {
            throw new StringIndexOutOfBoundsException("length is " +
                                                      length + ", start is " +
                                                      start + ", end is " +
                                                      end);
        }
        return new String(value, start, end - start);
    }

    /**
     * Return a new String consisting of the characters that make up this
     * ReusableStringBuffer.
     * @return the String representation if this ReusbleStringBuffer.
     */
    public String toString() {
        return new String(value,0,length);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 07-Feb-05	6833/2	ianw	VBM:2005020205 IBM fixes interim checkin

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 25-Feb-04	2974/4	steve	VBM:2004020608 supermerged

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
