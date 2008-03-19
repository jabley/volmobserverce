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
package com.volantis.mcs.clientapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A sample implementation of Autocompleter that uses country
 * names to provide autocompletion 
 */
public class CountryNameAutocompleter implements Autocompleter {

    /**
     * The following list of countries was extracted from
     * the ISO 3166 Code lists
     */
    private final static String[] COUNTRIES = { "Afghanistan", "Aland Islands",
            "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
            "Anguilla", "Antarctica", "Antigua And Barbuda", "Argentina",
            "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
            "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus",
            "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
            "Bosnia And Herzegovina", "Botswana", "Bouvet Island", "Brazil",
            "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria",
            "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada",
            "Cape Verde", "Cayman Islands", "Central African Republic", "Chad",
            "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
            "Colombia", "Comoros", "Congo",
            "Congo, The Democratic Republic Of The", "Cook Islands",
            "Costa Rica", "C\u00f4te D'Ivoire", "Croatia", "Cuba", "Cyprus",
            "Czech Republic", "Denmark", "Djibouti", "Dominica",
            "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
            "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland",
            "France", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Gambia", "Georgia",
            "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada",
            "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea",
            "Guinea-Bissau", "Guyana", "Haiti",
            "Heard Island And Mcdonald Islands",
            "Holy See (Vatican City State)", "Honduras", "Hong Kong",
            "Hungary", "Iceland", "India", "Indonesia",
            "Iran, Islamic Republic Of", "Iraq", "Ireland", "Isle Of Man",
            "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan",
            "Kazakhstan", "Kenya", "Kiribati",
            "Korea, Democratic People'S Republic Of", "Korea, Republic Of",
            "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic",
            "Latvia", "Lebanon", "Lesotho", "Liberia",
            "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania",
            "Luxembourg", "Macao",
            "Macedonia, The Former Yugoslav Republic Of", "Madagascar",
            "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
            "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
            "Mayotte", "Mexico", "Micronesia, Federated States Of",
            "Moldova, Republic Of", "Monaco", "Mongolia", "Montserrat",
            "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
            "Netherlands", "Netherlands Antilles", "New Caledonia",
            "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
            "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman",
            "Pakistan", "Palau", "Palestinian Territory, Occupied", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn",
            "Poland", "Portugal", "Puerto Rico", "Qatar", "R\u00e9union", "Romania",
            "Russian Federation", "Rwanda", "Saint Helena",
            "Saint Kitts And Nevis", "Saint Lucia",
            "Saint Pierre And Miquelon", "Saint Vincent And The Grenadines",
            "Samoa", "San Marino", "Sao Tome And Principe", "Saudi Arabia",
            "Senegal", "Serbia And Montenegro", "Seychelles", "Sierra Leone",
            "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
            "South Africa", "South Georgia And The South Sandwich Islands",
            "Spain", "Sri Lanka", "Sudan", "Suriname",
            "Svalbard And Jan Mayen", "Swaziland", "Sweden", "Switzerland",
            "Syrian Arab Republic", "Taiwan, Province Of China", "Tajikistan",
            "Tanzania, United Republic Of", "Thailand", "Timor-Leste", "Togo",
            "Tokelau", "Tonga", "Trinidad And Tobago", "Tunisia", "Turkey",
            "Turkmenistan", "Turks And Caicos Islands", "Tuvalu", "Uganda",
            "Ukraine", "United Arab Emirates", "United Kingdom",
            "United States", "United States Minor Outlying Islands", "Uruguay",
            "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",
            "Virgin Islands, British", "Virgin Islands, U.S.",
            "Wallis And Futuna", "Western Sahara", "Yemen", "Zambia",
            "Zimbabwe" 
    };

    // javadoc override
    public List match(String input, int limit) {
        int inputLength = input.length();
        ArrayList retList = new ArrayList();
                
        try {
            // search for input in the country list
            int result = Arrays.binarySearch(COUNTRIES, input, String.CASE_INSENSITIVE_ORDER);
        
            // binarySearch returns a negative value if input wasn't found
            // It shows the place where the input WOULD be in the array
            if (result >= 0) {
                retList.add(COUNTRIES[result]);
            } else {
                // extract the array index from the (-(insertion point) - 1) 
                // value returned by binarySearch 
                result = -(result + 1);

                while ((limit >= 0 ? retList.size() < limit : true)
                        && COUNTRIES[result].substring(0, inputLength).equals(input)) {
                    
                    retList.add(COUNTRIES[result]);
                    result++;
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            // thrown by substring if next string is too short - it certainly
            // wouldn't match - ignore the exception
        } catch (ArrayIndexOutOfBoundsException e) {
            // thrown by COUNTRIES[result] if searching near start/end 
            // of array - ignore and return retList below
        }
        
        return retList;
    }
}
