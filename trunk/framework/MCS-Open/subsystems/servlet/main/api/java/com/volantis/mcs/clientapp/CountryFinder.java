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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.clientapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Country finder.
 */
public class CountryFinder {
    
    private final static Country[] countries = {
        new Country("AD", "ANDORRA"),
        new Country("AE", "UNITED ARAB EMIRATES"),

        new Country("AF", "AFGHANISTAN"),
        new Country("AG", "ANTIGUA AND BARBUDA"),
        new Country("AI", "ANGUILLA"),
        new Country("AL", "ALBANIA"),
        new Country("AM", "ARMENIA"),

        new Country("AN", "NETHERLANDS ANTILLES"),
        new Country("AO", "ANGOLA"),
        new Country("AQ", "ANTARCTICA"),
        new Country("AR", "ARGENTINA"),
        new Country("AS", "AMERICAN SAMOA"),

        new Country("AT", "AUSTRIA"),
        new Country("AU", "AUSTRALIA"),
        new Country("AW", "ARUBA"),
        new Country("AZ", "AZERBAIJAN"),
        new Country("BA", "BOSNIA AND HERZEGOVINA"),

        new Country("BB", "BARBADOS"),
        new Country("BD", "BANGLADESH"),
        new Country("BE", "BELGIUM"),
        new Country("BF", "BURKINA FASO"),
        new Country("BG", "BULGARIA"),

        new Country("BH", "BAHRAIN"),
        new Country("BI", "BURUNDI"),
        new Country("BJ", "BENIN"),
        new Country("BM", "BERMUDA"),
        new Country("BN", "BRUNEI DARUSSALAM"),

        new Country("BO", "BOLIVIA"),
        new Country("BR", "BRAZIL"),
        new Country("BS", "BAHAMAS"),
        new Country("BT", "BHUTAN"),
        new Country("BV", "BOUVET ISLAND"),

        new Country("BW", "BOTSWANA"),
        new Country("BY", "BELARUS"),
        new Country("BZ", "BELIZE"),
        new Country("CA", "CANADA"),
        new Country("CC", "COCOS (KEELING) ISLANDS"),

        new Country("CD", "CONGO, THE DEMOCRATIC REPUBLIC OF THE"),
        new Country("CF", "CENTRAL AFRICAN REPUBLIC"),
        new Country("CG", "CONGO"),
        new Country("CH", "SWITZERLAND"),
        new Country("CI", "COTE D'IVOIRE"),

        new Country("CK", "COOK ISLANDS"),
        new Country("CL", "CHILE"),
        new Country("CM", "CAMEROON"),
        new Country("CN", "CHINA"),
        new Country("CO", "COLOMBIA"),

        new Country("CR", "COSTA RICA"),
        new Country("CU", "CUBA"),
        new Country("CV", "CAPE VERDE"),
        new Country("CX", "CHRISTMAS ISLAND"),
        new Country("CY", "CYPRUS"),

        new Country("CZ", "CZECH REPUBLIC"),
        new Country("DE", "GERMANY"),
        new Country("DJ", "DJIBOUTI"),
        new Country("DK", "DENMARK"),
        new Country("DM", "DOMINICA"),

        new Country("DO", "DOMINICAN REPUBLIC"),
        new Country("DZ", "ALGERIA"),
        new Country("EC", "ECUADOR"),
        new Country("EE", "ESTONIA"),
        new Country("EG", "EGYPT"),

        new Country("EH", "WESTERN SARARA"),
        new Country("ER", "ERITREA"),
        new Country("ES", "SPAIN"),
        new Country("ET", "ETHIOPIA"),
        new Country("FI", "FINLAND"),

        new Country("FJ", "FIJI"),
        new Country("FK", "FALKLAND ISLANDS (MALVINAS)"),
        new Country("FM", "MICRONESIA, FEDERATED STATES OF"),
        new Country("FO", "FAROE ISLANDS"),
        new Country("FR", "FRANCE"),

        new Country("GA", "GABON"),
        new Country("GB", "UNITED KINGDOM"),
        new Country("GD", "GRENADA"),
        new Country("GE", "GEORGIA"),
        new Country("GF", "FRENCH GUIANA"),

        new Country("GH", "GHANA"),
        new Country("GI", "GIBRALTAR"),
        new Country("GL", "GREENLAND"),
        new Country("GM", "GAMBIA"),
        new Country("GN", "GUINEA"),

        new Country("GP", "GUADELOUPE"),
        new Country("GQ", "EQUATORIAL GUINEA"),
        new Country("GR", "GREECE"),
        new Country("GS", "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"),
        new Country("GT", "GUATEMALA"),

        new Country("GU", "GUAM"),
        new Country("GW", "GUINEA-BISSAU"),
        new Country("GY", "GUYANA"),
        new Country("HK", "HONG KONG"),
        new Country("HM", "HEARD ISLAND AND MCDONALD ISLANDS"),

        new Country("HN", "HONDURAS"),
        new Country("HR", "CROATIA"),
        new Country("HT", "HAITI"),
        new Country("HU", "HUNGARY"),
        new Country("ID", "INDONESIA"),

        new Country("IE", "IRELAND"),
        new Country("IL", "ISRAEL"),
        new Country("IN", "INDIA"),
        new Country("IO", "BRITISH INDIAN OCEAN TERRITORY"),
        new Country("IQ", "IRAQ"),

        new Country("IR", "IRAN, ISLAMIC REPUBLIC OF"),
        new Country("IS", "ICELAND"),
        new Country("IT", "ITALY"),
        new Country("JM", "JAMAICA"),
        new Country("JO", "JORDAN"),

        new Country("JP", "JAPAN"),
        new Country("KE", "KENYA"),
        new Country("KG", "KYRGYZSTAN"),
        new Country("KH", "CAMBODIA"),
        new Country("KI", "KIRIBATI"),

        new Country("KM", "COMOROS"),
        new Country("KN", "SAINT KITTS AND NEVIS"),
        new Country("KP", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF"),
        new Country("KR", "KOREA, REPUBLIC OF"),
        new Country("KW", "KUWAIT"),

        new Country("KY", "CAYMAN ISLANDS"),
        new Country("KZ", "KAZAKHSTAN"),
        new Country("LA", "LAO PEOPLE'S DEMOCRATIC REPUBLIC"),
        new Country("LB", "LEBANON"),
        new Country("LC", "SAINT LUCIA"),

        new Country("LI", "LIECHTENSTEIN"),
        new Country("LK", "SRI LANKA"),
        new Country("LR", "LIBERIA"),
        new Country("LS", "LESOTHO"),
        new Country("LT", "LITHUANIA"),

        new Country("LU", "LUXEMBOURG"),
        new Country("LV", "LATVIA"),
        new Country("LY", "LIBYAN ARAB JAMABIRIYA"),
        new Country("MA", "MOROCCO"),
        new Country("MC", "MONACO"),

        new Country("MD", "MOLDOVA, REPUBLIC OF"),
        new Country("MG", "MADAGASCAR"),
        new Country("MH", "MARSHALL ISLANDS"),
        new Country("MK", "MACEDONIA, THE FORMER YUGOSLAV REPU8LIC OF"),
        new Country("ML", "MALI"),

        new Country("MM", "MYANMAR"),
        new Country("MN", "MONGOLIA"),
        new Country("MO", "MACAU"),
        new Country("MP", "NORTHERN MARIANA ISLANDS"),
        new Country("MQ", "MARTINIQUE"),

        new Country("MR", "MAURITANIA"),
        new Country("MS", "MONTSERRAT"),
        new Country("MT", "MALTA"),
        new Country("MU", "MAURITIUS"),
        new Country("MV", "MALDIVES"),

        new Country("MW", "MALAWI"),
        new Country("MX", "MEXICO"),
        new Country("MY", "MALAYSIA"),
        new Country("MZ", "MOZAMBIQUE"),
        new Country("NA", "NAMIBIA"),

        new Country("NC", "NEW CALEDONIA"),
        new Country("NE", "NIGER"),
        new Country("NF", "NORFOLK ISLAND"),
        new Country("NG", "NIGERIA"),
        new Country("NI", "NICARAGUA"),

        new Country("NL", "NETHERLANDS"),
        new Country("NO", "NORWAY"),
        new Country("NP", "NEPAL"),
        new Country("NU", "NIUE"),
        new Country("NZ", "NEW ZEALAND"),

        new Country("OM", "OMAN"),
        new Country("PA", "PANAMA"),
        new Country("PE", "PERU"),
        new Country("PF", "FRENCH POLYNESIA"),
        new Country("PG", "PAPUA NEW GUINEA"),

        new Country("PH", "PHILIPPINES"),
        new Country("PK", "PAKISTAN"),
        new Country("PL", "POLAND"),
        new Country("PM", "SAINT PIERRE AND MIQUELON"),
        new Country("PN", "PITCAIRN"),

        new Country("PR", "PUERTO RICO"),
        new Country("PT", "PORTUGAL"),
        new Country("PW", "PALAU"),
        new Country("PY", "PARAGUAY"),
        new Country("QA", "QATAR"),

        new Country("RE", "REUNION"),
        new Country("RO", "ROMANIA"),
        new Country("RU", "RUSSIAN FEDERATION"),
        new Country("RW", "RWANDA"),
        new Country("SA", "SAUDI ARABIA"),

        new Country("SB", "SOLOMON ISLANDS"),
        new Country("SC", "SEYCHELLES"),
        new Country("SD", "SUDAN"),
        new Country("SE", "SWEDEN"),
        new Country("SG", "SINGAPORE"),

        new Country("SH", "SAINT HELENA"),
        new Country("SI", "SLOVENIA"),
        new Country("SJ", "SVALBARD AND JAN MAYEN"),
        new Country("SK", "SLOVAKIA"),
        new Country("SL", "SIERRA LEONE"),

        new Country("SM", "SAN MARINO"),
        new Country("SN", "SENEGAL"),
        new Country("SO", "SOMALIA"),
        new Country("SR", "SURINAME"),
        new Country("ST", "SAO TOME AND PRINCIPE"),

        new Country("SV", "EL SALVADOR"),
        new Country("SY", "SYRIAN ARAB REPUBLIC"),
        new Country("SZ", "SWAZILAND"),
        new Country("TC", "TURKS AND CAICOS ISLANDS"),
        new Country("TD", "CHAD"),

        new Country("TF", "FRENCH SOUTHERN TERRITORIES"),
        new Country("TG", "TOGO"),
        new Country("TH", "THAILAND"),
        new Country("TJ", "TAJIKISTAN"),
        new Country("TK", "TOKELAU"),

        new Country("TM", "TURKMENISTAN"),
        new Country("TN", "TUNISIA"),
        new Country("TO", "TONGA"),
        new Country("TP", "EAST TIMOR"),
        new Country("TR", "TURKEY"),

        new Country("TT", "TRINIDAD AND TOBAGO"),
        new Country("TV", "TUVALU"),
        new Country("TW", "TAIWAN, PROVINCE OF CHINA"),
        new Country("TZ", "TANZANIA, UNITED REPUBLIC OF"),
        new Country("UA", "UKRAINE"),

        new Country("UG", "UGANDA"),
        new Country("UM", "UNITED STATES MINOR OUTLYING ISLANDS"),
        new Country("US", "UNITED STATES"),
        new Country("UY", "URUGUAY"),
        new Country("UZ", "UZBEKISTAN"),

        new Country("VE", "VENEZUELA"),
        new Country("VG", "VIRGIN ISLANDS, BRITISH"),
        new Country("VI", "VIRGIN ISLANDS, U.S."),
        new Country("VN", "VIET NAM"),
        new Country("VU", "VANUATU"),

        new Country("WF", "WALLIS AND FUTUNA"),
        new Country("WS", "SAMOA"),
        new Country("YE", "YEMEN"),
        new Country("YT", "MAYOTTE"),
        new Country("YU", "YUGOSLAVIA"),

        new Country("ZA", "SOUTH AFRICA"),
        new Country("ZM", "ZAMBIA"),
        new Country("ZW", "ZIMBABWE")
        };    

    public CountriesFeedResponse find(String query, int start, int count) {
        query = query.toUpperCase();
        List list;
        
        if (query.equals("")) {
            list = Arrays.asList(countries);
        } else {
            list = new ArrayList();
            for (int i = 0; i < countries.length; i++) {
                String countryName = countries[i].getName();
                if (countryName.indexOf(query) >= 0) {
                    list.add(countries[i]);
                }
            }
        }
        
        int listSize = list.size();
        int rows = count;   
        int startIndex = start;
        int endIndex = start + count;
        
        if (startIndex > listSize) {
            rows = 0;
        } else {
            if (endIndex > listSize) {
                endIndex = listSize;
            }
            rows = endIndex - startIndex;
        }
        
        if (rows < count) {
            endIndex = startIndex + rows;
        }
        
        Country[] countriesArray;
        if (rows > 0) {
            countriesArray = new Country[rows];
            for (int k=0, i = startIndex ; i < endIndex ; k++, i++) {
                countriesArray[k] = (Country)list.get(i);
            }
        } else {
            countriesArray = new Country[0];
        }
       
        return new CountriesFeedResponse(countriesArray, listSize);
    }
}
