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
package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.repository.impl.devices.DevicePattern;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;
import java.io.InputStream;
import java.io.IOException;

/**
 * Encapsulate a device pattern cache which is used to decrease the time
 * it takes to match an user agent pattern to a device. The cache uses a
 * simple divide and conquer algorithm to drill down quickly to the nearest
 * pattern which is uses to match the user agent to its device.
 */
public class DevicePatternCache {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DevicePatternCache.class);

    /**
     * Constant for the double backslash character.
     */
    private static final char DOUBLE_BACKSLASH = '\\';

    /**
     * Set of characters that need to be escaped when used within
     * regular expressions if they are not intended as wildcards.
     */
    private static final Set ESCAPED_CHARACTERS = new HashSet();

    static {
        // initialise the set of escaped characters.
        Character dot = new Character('.');
        Character asterix = new Character('*');

        ESCAPED_CHARACTERS.add(dot);
        ESCAPED_CHARACTERS.add(asterix);
    }

    /**
     * The DevicePattern cache is implemented using a hashmap. The map is
     * keyed using Integer objects. The objects in the map are TreeSets.
     */
    private HashMap cache;

    /**
     * Store a list of KeyList items used for looking up keys
     */
    private ArrayList keyList = null;

    /**
     *  Default Constructor
     */
    public DevicePatternCache() {
        cache = new HashMap();
        initializeKeyInfo();
    }

    /**
     * Reset the items in the cache
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Return the size of the cache which includes all the elements within
     * the treeSet as well.
     * @return the size of the cache
     */
    public int size() {
        int cacheSize = 0;
        Iterator keys = cache.keySet().iterator();
        while (keys.hasNext()) {
            cacheSize += ((TreeSet) cache.get(keys.next())).size();
        }
        return cacheSize;
    }

    /**
     * Returns the device name that has been matched against the supplied
     * userAgentString.
     *
     * @param userAgentString a user agent string that needs to be matched
     * to a device in the cache.
     *
     * @return the name of the device that has been matched the supplied
     * userAgentString or null if no such match could be made.
     */
    public String match(String userAgentString) {

        // Lets try and match the supplied user agent string
        String matchedDeviceName = null;
        Integer key = new Integer(-1);
        // iterate through the possible device pattern buckets
        while (key != null) {
            key = getPatternMapKey(userAgentString, false, getBaseKey(key) + 1);
            if (key != null) {

                TreeSet set = (TreeSet) cache.get(key);
                if (set != null) {
                    matchedDeviceName = matchUserAgentToDevice(
                        set.iterator(), userAgentString);
                    if (matchedDeviceName != null) {
                        // stop searching if we find the name
                        break;
                    }
                }
            }
        }

        if (matchedDeviceName == null) {
            // The specified user agent string has not matched to any device
            // contained in the bucket keyed using this user agent's sub key.

            // Lets try and match against the general bucket for this
            // type of user agent. e.g for DoCoMo devices, say, we try
            // to match the user agent string against DoCoMo/.*.
            Iterator generalDevicePatternIter =
                    getGeneralDevicePatternIterator(userAgentString);
            matchedDeviceName = matchUserAgentToDevice(
                    generalDevicePatternIter, userAgentString);
        }
        if (matchedDeviceName == null) {
            // The general user agent string has not matched to any device
            // Lets try and match against the ".*" general bucket for this
            // type of user agent. e.g we try to match the user agent string
            // against .*Danger hiptop 2.0.*.
            Iterator generalDevicePatternIter =
                    getGeneralDevicePatternIterator(".*");
            matchedDeviceName = matchUserAgentToDevice(
                    generalDevicePatternIter, userAgentString);
        }

        if (matchedDeviceName != null) {
            if (logger.isDebugEnabled()) {
                    logger.debug("Matched " + matchedDeviceName +
                                 " for userAgent: " + userAgentString +
                                 " from cache");
            }
        } else if (logger.isDebugEnabled()) {

            logger.debug("Device name for userAgent " +
                                 userAgentString +
                                 " not in devicePatternCache");
        }
        return matchedDeviceName;
    }

    /**
     * Iterates over the supplied devicePatternIterator and attempts to
     * match the supplied userAgentString to a device using
     * {@link DevicePattern#match(String userAgent)}.  If a match is made,
     * the the name of the device matched is returned; if no match is made
     * the null will be returned.
     *
     * @param devicePatternIterator an iteration of DevicePattern instances.
     * @param userAgentString the user agent string to be matched to a
     * device name.
     *
     * @return the name of the device (contained in devicePatternIterator)
     * that can be matched to the supplied userAgentString or null if
     * no match is found.
     */
    private String matchUserAgentToDevice(Iterator devicePatternIterator,
                                          String userAgentString) {

        boolean foundMatch = false;
        String deviceName = null;

        if (devicePatternIterator != null) {
            while(devicePatternIterator.hasNext() && !foundMatch) {
                DevicePattern currentDevicePattern =
                        (DevicePattern)devicePatternIterator.next();

                if (currentDevicePattern.match(userAgentString)) {
                    foundMatch = true;
                    deviceName = currentDevicePattern.getDeviceName();
                }
            }
        }
        return deviceName;
    }

    /**
     * Returns an iteration of DevicePattern instances stored in the
     * TreeSet that is obtained using a key that is equal to the first
     * two characters in the supplied userAgentString.
     *
     * @param userAgentString the user agent string for which an iteration of
     * DevicePattern instances is required.
     *
     * @return iteration of DevicePattern instances for the supplied
     * userAgentString or null if none exist.
     */
    private Iterator getGeneralDevicePatternIterator(String userAgentString) {
        char mainKey = userAgentString.length() > 0 ?
                        userAgentString.charAt(0) : (char)0;
        char subKey = userAgentString.length() > 1 ?
                        userAgentString.charAt(1) : (char)0;
        Integer key = new Integer(getBinaryKey(keyList.size(),
                                           mainKey, subKey));
        TreeSet set = (TreeSet) cache.get(key);
        if (set != null) {
            return set.iterator();
        }
        return null;
    }

    /**
     * Map the pattern to a device name in the cache.
     * @param pattern the pattern which may contain wildcard characters
     * @param deviceName the device name to match the pattern to.
     */
    public void mapPatternAndDevice(String pattern, String deviceName) {
        Integer key = getPatternMapKey(pattern, true, 0);
        TreeSet set = (TreeSet) cache.get(key);
        if (set == null) {
            set = new TreeSet();
        }
        set.add(new DevicePattern(pattern, deviceName));
        cache.put(key, set);
    }

    /**
     * Intialize the key info array list.
     */
    private void initializeKeyInfo() {
        if (keyList == null) {
            keyList = new ArrayList();
        }
        keyList.clear();

        // Read the properties from 'DevicePatternBucket.properties' and
        // populate the list containing KeyInfo instances accordingly.
        Properties properties = new Properties();
        try {
            // Get the class loader.
            ClassLoader loader = getClass().getClassLoader();
            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
            }

            final InputStream in = loader.getResourceAsStream(
                "com/volantis/devrep/repository/impl/accessors/DevicePatternBucket.properties");
            properties.load(in);
            in.close();

            // We now have the properties so populate the keys-list with
            // the values obtained from the properties file.
            int counter = 1; // 1-based key-counter
            boolean done = false;
            while (!done) {
                String key = "Key." + counter;
                String value = properties.getProperty(key + ".value");
                if (value != null) {
                    int index = Integer.valueOf(properties.getProperty(
                            key + ".index")).intValue();
                    String isDigitStr = properties.getProperty(
                            key + ".isdigit");
                    final KeyInfo keyInfo;
                    boolean isDigit = false;
                    if (isDigitStr != null) {
                        isDigit = Boolean.valueOf(isDigitStr).booleanValue();
                    }
                    keyInfo = new KeyInfo(value, index, isDigit);
                    keyList.add(keyInfo);
                } else {
                    // If the value is null we have reached the end of the
                    // property files expected entries.
                    done = true;
                }
                ++counter;
            }
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }
    }

    /**
     *  Debug method may dump the entire contents of the cache to the log
     *  in a CSV format [key],[subkey],[size],[deviceName],[pattern]
     */
    private void outputPatternCache() {
        Iterator iterator = cache.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();

            int main = key.intValue() >> 16;
            int subKey1 = (key.intValue() - (main << 16)) >> 8;
            int subKey2 = (key.intValue() - (main << 16) - (subKey1 << 8));

            String keyValue = "Other";
            if (main < keyList.size()) {
                keyValue = ((KeyInfo) keyList.get(main)).getValue(false);
            }
            String subKey = "" + (char) subKey1;
            if (subKey2 != 0) {
                subKey += ("" + (char) subKey2);
            }

            TreeSet set = (TreeSet) cache.get(key);
            Iterator it = set.iterator();
            while (it.hasNext()) {
                DevicePattern value = (DevicePattern) it.next();
                if (logger.isDebugEnabled()) {
                    logger.debug(" , " + keyValue +
                                 " , " + subKey +
                                 " , " + set.size() +
                                 " , " + value.getDeviceName() +
                                 " , " + value.getPattern()
                    );
                }
            }
        }
    }

    /**
     * Given 3 base keys chars, create binary integer combination of them.
     * The base char is shifted 16 bits to the left and then the sub char
     * is added to it.
     *
     * <p>For example:</p>
     *
     * <dl>
     *
     * <dt>Basekey</dt>
     *
     * <dd>4 '00000100'</dd>
     *
     * <dt>mainKey</dt>
     *
     * <dd>32 '00100000'</dd>
     *
     * <dt>subkey</dt>
     *
     * <dd>54 '00110110'</dd>
     *
     * </dl>
     *
     * results in 00000100 00100000 00110110 (270390)
     *
     * @param baseKey the main bucket
     * @param mainKey the 1st half of the key
     * @param subKey the 2nd half of the key
     */
    private int getBinaryKey(int baseKey, char mainKey, char subKey) {
        return ((baseKey << 16) + (((int) mainKey) << 8) + subKey);
    }

    /**
     * Returns the base key used for keys generated by
     * {@link #getBinaryKey(int, char, char)}.
     *
     * @param key the key whose base key to return, must not be null
     * @return the base key computed from the specified key.
     */
    private int getBaseKey(final Integer key) {
        return key.intValue() >> 16;
    }

    /**
     * Ensure key characters do not contain true wildcard characters,
     * i.e. 3 chars like 'a.9', 'aa*' and 'a3*' have wildcards
     * whilst '\.9', 'a\*', 'aaa', '*32', '.32' don't
     * Note we are only interested in the latter 2 characters because these
     * characters form the key.
     * @param pos1 the first character
     * @param pos2 the second character
     */
    private boolean isWildcard(char pos1, char pos2) {
        if (pos1 != '\\' && pos2 == '.') {
            return true;
        } else if (pos1 != '\\' && pos2 == '*') {
            return true;
        }
        return false;
    }

    /**
     * Return a KeyPair object containing a main and sub integer derived from
     * the userAgent string.
     *
     * @param userAgentOrPattern the user agent string or pattern for
     * which a key is required.
     *
     * @param useDevicePattern true key should use pattern containing "\\."
     * and not just "." characters
     *
     * @param startIndex the index of the first bucket to examine
     * @return a keyPair object resulting from the examination of the userAgent
     * string or null if startIndex is larger than or equals to the number of
     * buckets
     *
     * @todo This method is very confusing due to the fact that it attempts to calculate a key for both specific user agents and user agent patterns. This functionality really needs to be performed by two separate methods.
     */
    private Integer getPatternMapKey(String userAgentOrPattern,
                                     boolean useDevicePattern,
                                     int startIndex) {

        if (startIndex >= keyList.size()) {
            return null;
        }

        // When useDevicePattern is true we are trying to obtain a key for
        // a user agent pattern; when false we are trying to obtain a key for
        // a user agent string.

        int key = 0;
        for (int i = startIndex; keyList != null && i < keyList.size(); i++) {
            KeyInfo info = (KeyInfo) keyList.get(i);
            int startingIndexOfUniqueDeviceInfo =
                    info.getIndex(useDevicePattern);

            // This may be either a user agent string or a user agent pattern.
            String deviceValueInCurrentKeyInfo =
                    info.getValue(useDevicePattern);

            // Check to see if the agent matches one of the main 'buckets'
            if (isValidMatch(userAgentOrPattern, deviceValueInCurrentKeyInfo,
                             startingIndexOfUniqueDeviceInfo)) {

                if (useDevicePattern) {
                    // we know that this method is being used to obtain the key
                    // for a user agent pattern, rather than a user agent string.
                    if (startingIndexOfUniqueDeviceInfo >
                        (deviceValueInCurrentKeyInfo.length() + 1)) {

                        // We have a situation whereby we could have escaped
                        // wildcard characters between the manufacturer name
                        // and the start of the device specific information.
                        // e.g DoCoMo/1\\.0/ N901ci.*.  This changes the index
                        // at which device specific information begins,
                        // therefore it needs to be recalculated.
                        startingIndexOfUniqueDeviceInfo =
                                reCalculateStartingIndexOfDeviceSpecificInfo(
                                        info, userAgentOrPattern);

                    }
                }

                char subKeyChar1 =
                        userAgentOrPattern.charAt(startingIndexOfUniqueDeviceInfo);

                if (info.checkDigit()) {
                    if (Character.isDigit(subKeyChar1)) {
                        key = getBinaryKey(i, subKeyChar1, (char) 0);
                    } else {
                        // digit expected, try another KeyInfo
                        continue;
                    }
                } else {

                    char subKeyChar2;
                    if (userAgentOrPattern.length() ==
                            startingIndexOfUniqueDeviceInfo + 1) {

                        // The user agent string is only 1 character greater
                        // than the well known device name.  Therefore there
                        // is only one character available for use in
                        // getBinaryKey(...).  Just use zero.
                        subKeyChar2 = (char) 0;
                    } else {
                        subKeyChar2 =
                                userAgentOrPattern.charAt(
                                        startingIndexOfUniqueDeviceInfo + 1);
                    }

                    if (useDevicePattern) {
                        // Ensure key characters are not true wildcard characters
                        if (!isWildcard(userAgentOrPattern.charAt(
                                startingIndexOfUniqueDeviceInfo - 1), subKeyChar1) &&
                                !isWildcard(subKeyChar1, subKeyChar2)) {
                            key = getBinaryKey(i, subKeyChar1, subKeyChar2);
                        }
                    } else {
                        // check if subKeyChar1 or subKeyChar2 was escaped in
                        // the pattern
                        if (needsRexExpEscaping(subKeyChar1)) {
                            key = getBinaryKey(i, '\\', subKeyChar1);
                        } else if (needsRexExpEscaping(subKeyChar2)) {
                            key = getBinaryKey(i, subKeyChar1, '\\');
                        } else {
                            key = getBinaryKey(i, subKeyChar1, subKeyChar2);
                        }
                    }
                }
                break;
            }
        }
        if (key == 0) {
            char mainKey = userAgentOrPattern.length() > 0 ?
                            userAgentOrPattern.charAt(0) : (char)0;
            char subKey = userAgentOrPattern.length() > 1 ?
                            userAgentOrPattern.charAt(1) : (char)0;
            key = getBinaryKey(keyList.size(), mainKey, subKey);
        }
        return new Integer(key);
    }

    /**
     * Returns true if the specified character needs to be escaped in regular
     * expressions.
     *
     * @param ch the character to check
     * @return true iff the character needs escaping in regular expressions
     */
    private boolean needsRexExpEscaping(final char ch) {
        return ch == '.' || ch == '[' || ch == ']' || ch == '+' || ch == '\"' ||
            ch == '*' || ch == '?' || ch == '\\';
    }

    /**
     * Recalculates the index at which device specific information begins
     * taking into account any escaped wildcard characters, ie '\\.' or '\\*',
     * that may exist in the supplied <code>userAgentPattern</code>.
     * <p>
     * Note that the first two characters of device specific information are
     * used to generate a key that is used to store and retrieve the user
     * agent pattern from the cache.
     *
     * <p>
     * Consider the set of user agent strings that start with DoCoMo/
     * and contain device specific information starting at index 11 (0 based).
     * <p>
     * e.g
     * <p>
     * DoCoMo/2.0 N901iCxxx - device specific information starts with 'N'
     * <p>
     * The user agent pattern that needs to be stored to match the
     * above user agent string is: DoCoMo/2\\.0 N901iC.*
     * <p> As this is a regular expression the . needs to be escaped so that
     * it is not considered a wildcard.  This has the affect increasing
     * the length of the pattern by one.  Therefore,
     * UserAgentPattern[11] = [space] rather than 'N', hence the need to
     * recalculate the index at which device specific information begins.
     *
     *
     * @param keyInfo key information associated with the
     * supplied userAgentPattern.
     *
     * @param userAgentPattern the pattern to be inspected for escaped
     * wildcard characters.
     *
     * @return <p>the newly calculated index at which device specific
     * information beings in the user agent pattern when
     * considering escaped wildcard characters. If the supplied
     * <code>userAgentPattern</code> does not contain any escaped wildcard
     * characters, then this method will return the value obtained from
     * keyInfo.getIndex(false);
     */
    private int reCalculateStartingIndexOfDeviceSpecificInfo(KeyInfo keyInfo,
                                                             String userAgentPattern) {
        // Obtain the index at which device specific information
        // is said to begin when there are no escaped wildcard
        // characters.
        int startingIndexOfUniqueDeviceInfo = keyInfo.getIndex(false);

        // Does the user agent pattern contain escaped wildcard
        // characters, e.g '\\.' or '\\*'? If so then we need to
        // increase the index of the first character of device specific
        // information accordingly.

        // Consider the following example. Suppose we are mapping the
        // user agent pattern "DoCoMo/2\\.0 N901iC.*" to the device
        // "DoCoMo-N901iC".  The index at which the device specific
        // information begins is 11
        // (obtained from DevicePatternBucket.properties).  This means
        // that [space]N will be used to generate the key that will be
        // used to identify this device in the cache.  However, when
        // we use the user agent string "DoCoMo/2.0 N901iCxxx", which
        // should match the DoCoMo-N901iC device, N9 is used to
        // generate the key.  This is not the same as the key used to
        // store the device pattern so the device match will fail.

        // When mapping device names to a regular expression we need
        // to increase the index at which device information starts by
        // 1 each time a '\\.' or '\\*' is encountered between
        // the start of the device pattern and the index at which
        // device specific information is said to begin.
        int numberOfEscapedWildcardCharacters =
                 getNumberOfEscapedWildcardCharacters(
                         userAgentPattern,
                         startingIndexOfUniqueDeviceInfo);

        startingIndexOfUniqueDeviceInfo =
                 startingIndexOfUniqueDeviceInfo +
                    numberOfEscapedWildcardCharacters;

        return startingIndexOfUniqueDeviceInfo;
    }


    /**
     * Returns the number of escaped wildcard characters, ie "\\." or "\\*"
     * contained in the supplied userAgentPattern between the start of this
     * userAgent (as defined in DevicePatternBucket.properties,
     * e.g Nokia, DoCoMo/...) and the start of device specific information.
     * <p>
     * e.g
     * For the set of DoCoMo devices we know that the user agent string
     * will always be in the following format: <br>
     * DoCoMo/XXX/CC, hence we would be searching for escaped characters in
     * the substring XXX/
     * <p>
     * Examples: <br>
     *
     * User Agent Pattern: DoCoMo/2\\.0/P700i.* would return 1.
     * <br>
     * User Agent Pattern: DoCoMo/\\.\\.0/P700i.* would return 2.
     * <br>
     * User Agent Pattern: DoCoMo/\\.\\*\\./P700i.* would return 3.
     *
     * @param userAgentPattern the pattern to search for escaped wildcard
     * characters.
     *
     * @param indexOfDeviceSpecificInformationInUAString the index at which
     * device specific information starts in a user agent string that is
     * to be matched by the supplied userAgentPattern.
     *
     * @return the number of escaped wildcard characters found in <code>
     * userAgentPattern </code> between the start of this userAgentPattern and
     * the start of device specific information.
     */
    private int getNumberOfEscapedWildcardCharacters(
            String userAgentPattern,
            int indexOfDeviceSpecificInformationInUAString) {

        // As user agent patterns can contain escaped characters,
        // this results in the length of the user agent pattern
        // being increased, e.g representing 1.0 in a user agent
        // pattern needs to be written as 1\.0.  Therefore
        // for each escaped character found, the length of
        // the useragent pattern is increased by 1.  This increase
        // in length means that the index in the user agent string
        // at which device specific information begins is no
        // longer correct in the user agent pattern.

        // e.g Consider the user agent string DoCoMo/2.0 P700ixxx, device
        // specific information starts at index 11.
        // Now consider a user agent pattern that can match this user agent
        // string: DoCoMo/2\.0 P700i.*.  In this case device specific
        // information starts at index 12

        // Or DoCoMo/\.\.\. P700i.*.  In this case device specific information
        // starts at index 14.

        // The above situations mean that we cannot use
        // indexOfDeviceSpecificInfoRelativeToUAString to determine where
        // to stop searching the supplied userAgentPattern.  We can use
        // indexOfDeviceSpecificInfoRelativeToUAString as a suitable
        // starting point at which to end the search, but we must increment
        // this value each time an escaped character is found.

        int numberOfEscapedCharactersFound = 0;
        // The index if the current character to inspect.
        int currentCharIndex = 0;
        int indexToStopSearching = indexOfDeviceSpecificInformationInUAString;

        // Inspect the characters in the supplied search string looking
        // for occurrences of characters that need to be escaped in reg
        // expressions if they are not intended as wilcards, eg * and . .
        while (currentCharIndex <= indexToStopSearching) {
            // Inspect the current character
            char currentChar = userAgentPattern.charAt(currentCharIndex);
            // Have we found a character that needs to be escaped if it is
            // not being used as a wildcard?
            if (ESCAPED_CHARACTERS.contains(new Character(currentChar))) {
                // is this character escaped, ie, preceded by '\\'?
                if(isCharacterEscaped(userAgentPattern, currentCharIndex)) {
                    // Increment number of escaped characters found.
                    numberOfEscapedCharactersFound++;

                    // We have found an escaped character so we must increment
                    // the index at which to stop searching to compensate
                    // for the increase in the length of the user agent
                    // pattern due to escaped characters.
                    indexToStopSearching++;
                }
            }
            currentCharIndex++;
        }

        return numberOfEscapedCharactersFound;
    }

    /**
     * Returns true if the character at the supplied <code>indexOfChar</code>
     * in the supplied <code>userAgentPattern</code> is preceded by
     * {@link #DOUBLE_BACKSLASH}, ie, is an escaped character.
     *
     * @param userAgentPattern the string to search.
     * @param indexOfChar the index of the character to check for being
     * escaped.
     *
     * @return true if the characters at the supplied index in the the supplied
     * userAgentPattern is escaped.
     */
    private boolean isCharacterEscaped(String userAgentPattern,
                                       int indexOfChar) {

        // Ensure that the bounds of the string are not exceeded.
        if (indexOfChar >= userAgentPattern.length()) {
            throw new IllegalArgumentException(
                    "indexOfChar:" + indexOfChar +
                    " >= string length: " + userAgentPattern.length());
        }

        boolean isCharEscaped = false;
        // Handle special case when index is 0
        if (indexOfChar == 0) {
            isCharEscaped = false;
        } else {
            char previousCharacter = userAgentPattern.charAt(indexOfChar - 1);
            if (previousCharacter == DOUBLE_BACKSLASH) {
                isCharEscaped = true;
            }
        }

        return isCharEscaped;
    }


    /**
     * Return true if a valid match, false otherwise. A match is valid if the
     * userAgent starts with the pattern AND the userAgent's length is longer
     * than the (index + 1) AND the userAgent string does not contain wildcards
     * before the index position
     * @return true if a valid match, false otherwise
     */
    private boolean isValidMatch(String userAgent, String pattern, int index) {
        if ((userAgent.startsWith(pattern) == false) ||
                (userAgent.length() < (index + 1)) ||
                (userAgent.lastIndexOf(".*", index) != -1)) {
            return false;
        }
        return true;
    }

    /**
     * This inner class is used to store strings and indices used to extract
     * the key from the user agent string.
     */
    class KeyInfo {

        private int deviceSubKeyIndex;
        private int userAgentSubKeyIndex;
        private String devicePatternValue;
        private String userAgentValue;
        private boolean checkDigit = false;

        /**
         * Construct a key info object with string value and index
         * @param value the string used as a pattern to match part of the user
         * agent string
         * @param index the index that will be used to start extracting a sub-key
         * from the user agent string. For 'NokiaCC' the index will be 5
         * (the start of the unique key in the user agent string denoted by CC)
         */
        public KeyInfo(String value, int index) {
            this(value, index, false);
        }

        /**
         * Construct a key info object with string value and index
         * @param value the string used as a pattern to match part of the user
         * agent string
         * @param index the index that will be used to start extracting a sub-key
         * from the user agent string. For 'NokiaCC' the index will be 5
         * (the start of the unique key in the user agent string denoted by CC)
         * @param checkDigit for some cases we need to verify that the char at
         * the index position is a digit and then react accordingly
         */
        public KeyInfo(String value, int index, boolean checkDigit) {
            this.userAgentValue = value;
            this.devicePatternValue = value;

            this.userAgentSubKeyIndex = index;
            this.deviceSubKeyIndex = index;
            this.checkDigit = checkDigit;

            // If the userAgent string contains any '.' characters create another
            // string that has the '.' chars expanded to "\\." strings.
            // For example, "Mozilla/4.0" changes to "Mozilla/4\\.0"
            // This is necessary because the pattern cache is built up from device
            // patterns which already have the '.' expanded. When a user agent
            // string (which does not have any expanded '.'s) requires a key, it
            // must map to the correct bucket and
            if (userAgentValue.indexOf('.') != -1) {
                devicePatternValue = expandDotCharsInString(value);
            }
        }

        /**
         * Take the value and expand any '.' chars to a "\\." string.
         * If there are no '.' chars, return the unchanged string. Also increment
         * the internal variable deviceSubKeyIndex for each '.' found
         * @return a modified string containing "\\." strings instead of '.'
         */
        private String expandDotCharsInString(String value) {
            StringBuffer buffer = new StringBuffer(value);
            int pos = value.lastIndexOf('.');
            while (pos > 0) {
                buffer.insert(pos, '\\');
                pos = value.lastIndexOf('.', pos - 1);
                ++deviceSubKeyIndex;
            }
            return buffer.toString();
        }

        /**
         * Return the index
         * @param useDevicePattern true if we want to use the expanded string,
         * false otherwise
         * @return the index of the start of the two-character sub-key
         */
        public int getIndex(boolean useDevicePattern) {
            if (useDevicePattern) {
                return deviceSubKeyIndex;
            }
            return userAgentSubKeyIndex;
        }

        /**
         * Return the pattern value
         * @param useDevicePattern true if we want to use the expanded string,
         * false otherwise
         * @return the pattern to compare to the start of the user agent string
         */
        public String getValue(boolean useDevicePattern) {
            if (useDevicePattern) {
                return devicePatternValue;
            }
            return userAgentValue;
        }

        /**
         * See if the digit needs to be checked
         * @return true if we need to check for a digit, false otherwise
         */
        public boolean checkDigit() {
            return checkDigit;
        }

        /**
         * Returns a string representation of a KeyInfo instance.
         * @return a string representation of this KeyInfo instance
         */
        public String toString() {
            return "Device pattern value    : " + devicePatternValue + "\n" +
                   "Device agent value      : " + userAgentValue + "\n" +
                   "Device subkey index     : " + deviceSubKeyIndex + "\n" +
                   "User agent subkey index : " + userAgentSubKeyIndex;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	10055/1	rgreenall	VBM:2005092902 Fixed fallback identification of DoCoMo devices.

 18-Oct-05	9860/1	rgreenall	VBM:2005092211 Fixed identification of DoCoMo devices.

 02-Mar-05	7130/3	rgreenall	VBM:2005011201 Post review corrections

 02-Mar-05	7130/1	rgreenall	VBM:2005011201 Fixed bug where the mapping of a user agent pattern to a device name would fail if the pattern was one character greater than the device name.

 ===========================================================================
*/
