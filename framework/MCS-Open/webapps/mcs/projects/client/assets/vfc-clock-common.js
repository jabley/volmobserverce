/**
 * (c) Volantis Systems Ltd 2007.
 */
 
/** 
 * Responsible for displaying current time value
 */
Widget.TimeDisplay = Class.define(
{
  initialize: function(id, options, mode) {
    this.initializeTimeDisplay(id, options, mode);
  },

  /**
   * Constructor 
   */
  initializeTimeDisplay: function(id, options, mode) {
    this.id = id;
    this.mode = mode;
    this.element = $(id);
    
    //first preload images
    this.locked = true;

    //time to display
    this.time = new Date();


    //time in ms beetween local time and time from service
    this.timeDiff = 0;

    //used only to preload images
    this.preloadedImages = new Array();
    
    //format of the date displayed by the widget
    //some value is always supplied
    this.format = null;

    //special marks, used while parsing format string
    this.specialMarks = new Array('Y', 'y', 'F', 'm', 'D', 'd', 'h', 'H', 'i', 's', 'A','S')

    //the same as this.format but as an array and without '%' characters
    this.formatArray = null;

    /**
      * maps format characters to 3-elements array:
      * - the first element is method name to get data from date object, 
      * - the second is the method name to convert retrived date to desired format
      * - and the latter one is name of the array consists markup
      * for the element
      */
    this.mapping = null;

   // Element at the end of below arrays is a string with length equal to number
   // of elements in the array. 
   // '0' on the n-th position of this string indicates that n-th
   // element in the array is an HTML code, 
   // '1' indicates that it is id of the widget contains the HTML code.

    /**
     * Consists ids of HTML elements with code to display the digits
     * or HTML code itself.
     * Elements in order are as follows:
     * 0, 1, ..., 9
     */
    this.digits = null;
    
    /**
     * Consists of HTML code to display separators.
     * There can be as much separators as needed.
     * In format string they are used by index of an element
     * in the array preceded by the '%'.
     * So if we want to display the first separator in the array,
     * we need to put '%1' in the format string.
     */
    this.separators = null;
    
    /** 
     * Consists of HTML code to display months names.
     * Elements in order are: Jan, Feb, ..., Dec
     */
    this.months = null;

    /**
     * Consists of HTML code to display days names.
     * Elements in order are: Sun, Mon, ..., Sat
     */
    this.days = null;

    /**
     * Consists HTML code to display AM and PM indicator
     * Elements in order ar: am, pm
     */
    this.ampm = null;

    /**
     * Names of arrays with HTML markup for elements builds clock
     */
    this.arrayNames = new Array('digits', 'separators', 'months', 'days', 'ampm');

    Object.copyFields(this, options || {});

    var d = new Date();
    //on nokia oss offset has different sign
    this.sgn = Prototype.nokiaOSSBrowser() ? -1 : 1;
    // to handle timezones corractly
    this.offset = d.getTimezoneOffset()*60*1000*this.sgn;

    this.initializeMapping();

    this.formatArray = this.getFormatArray();

    if (this.mode=='timer' ) {
      this.setMode('timer');
    }

    this.preloadImages();
  },
  

  /**
    * Sets mode to clock/timer, deafault is clock
    * timer mode is used by timer and stopwatch widgets
    *@internal api
    */
   setMode : function(m){
    this.mode = m;
    if (m == 'timer'){
      this.prepareTimerDataAccesMethod();
      this.arrayNames = new Array('digits', 'separators');
    }
  },

  /**
   * @returns accuracy of the clock in ms.
   * returns 3600000 if the accuracy is less than hour
   */
  getAccuracy: function() {
      timeMarks = new Array('S','s', 'i');
      var min = null; //minimum
      for (var j = 0; j < timeMarks.length ; j++) {
        for (var i = 0 ; i < this.formatArray.length ; i++) {
          if (this.formatArray[i] == timeMarks[j]) {
            min = timeMarks[j];
            break;
          }
        }
        if (min != null) {
          break;
        }
      }
      switch(min) {
        case 'S': min = 123; break;
        case 's': min = 1000; break;
        case 'i': min = 60000; break;
        default: min = 3600000; break;
      }
      return min;
  },

  /**
   * Creates reformatted copy of this.format array.
   * 1/ '%' signs are removed
   * 2/ literal separators are added to this.separators array
   * and its id is put to returned array instead of the literal.
   */
  getFormatArray: function() {
    var ret = new Array();
    for (var i = 0 ; i < this.format.length ; i++) {
      if (this.format[i].charAt(0) == '%') {
        ret[i] = this.format[i].substr(1);
      } else {
        //literal separator
        ret[i] = this.addLiteralSeparator(this.format[i]);
      }
    }
    return ret;
  },
  
  /**
   * Adds new value to this.separators array and returns its index. 
   * @param separator text to be added as a separator
   * @returns id of separator added to this.separators array
   */
  addLiteralSeparator: function(separator) {
    var id = this.separators.length;
    this.separators[id] = this.separators[id-1] + '0';
    this.separators[id-1] = separator;
    return id;
  },

  /**
   * Sets new date to be displayed and refreshes.
   * @param date Date object of the new date
   */
  setTimeToDisplay: function(date) {
    if (!this.locked) {
    this.time = date;
    this.refresh();
    }
  },

  /** 
   * Refreshes displayed content using value of the this.time value
   */
  refresh: function() {
    /** 
     * Element of this.mapping array
     */
    var map; 

    //method name to get date from the Date object
    var dataAccesMethodName = '';

    //method name to format retrieved data
    var dataFormatMethodName = '';

    //array contains markup for element
    var markupArray = '';

    //HTML code to be substituted
    var time = '';

    //HTML code for current format element
    var tmp = null;

    //key to map array
    var key = ''; 
    for (var i = 0; i < this.formatArray.length ; i++) {
      tmp = null;

      key = this.formatArray[i];
      if (this.isNumber(key)) {
        key = 0;
      }

      map = this.mapping[key];
      if (map != null) {
        dataAccesMethodName = map[0];
        dataFormatMethodName = map[1];
        markupArray = map[2];

        if (dataAccesMethodName != null) {
          if (this.mode=='timer' && key == this.timerPrecision){
            //use a timer spcific method to get the time
            tmp = this.timerDataAccesMethod();
          }else{
            //use standard method
            tmp = this.time[dataAccesMethodName]();
          }
        }

        //if the dataAccessMethodName is null, then
        //use format element marker as an argument for the
        //dataFormatMethod (used for separators)
        if (tmp == null) {
          tmp = this.formatArray[i];
        }

        if (dataFormatMethodName != null) {
          if (this.mode=='timer' && key == this.timerPrecision){
            //use this method if in timer mode else use default
            tmp = this.num2Str(tmp);
          }else{
            tmp = this[dataFormatMethodName](tmp);
          }
        }

        if (markupArray != null) {
          time += this.stringValueToImages(tmp, markupArray);
        }
      }
    }
    this.element.innerHTML = time;
  },

  initializeMapping: function() {
    this.mapping = new Array();
  
    //A full numeric representation of a year, 4 digits
    this.mapping['Y'] = ['getFullYear', 'num2Str', 'digits'];

    //A two digit representation of a year
    this.mapping['y'] = ['getYear', 'getNumOn2Dig', 'digits'];

    //A full textual representation of a month, such as January or March
    this.mapping['F'] = ['getMonth', 'num2Str', 'months'];

    //Numeric representation of a month, with leading zeros
    this.mapping['m'] = ['getMonth', 'getIncrementedNumOn2Dig', 'digits'];

    //A textual representation of a day -> 'Friday'
    this.mapping['D'] = ['getDay', 'num2Str', 'days'];

   //	Day of the month, 2 digits with leading zeros -> 01 to 31
    this.mapping['d'] = ['getDate', 'getNumOn2Dig', 'digits'];

    //12-hour format of an hour with leading zeros
    this.mapping['h'] = ['getHours', 'getHour12', 'digits'];

    //24-hour format of an hour with leading zeros
    this.mapping['H'] = ['getHours', 'getNumOn2Dig', 'digits'];

    //Minutes with leading zeros
    this.mapping['i'] = ['getMinutes', 'getNumOn2Dig', 'digits'];

    //Seconds, with leading zeros
    this.mapping['s'] = ['getSeconds', 'getNumOn2Dig', 'digits'];

    //Milliseconds, with leading zeros
    this.mapping['S'] = ['getMilliseconds', 'getNumOn3Dig', 'digits'];

    //Ante meridiem and Post meridiem indicator
    this.mapping['A'] = ['getHours', 'getAMorPM', 'ampm']

    //separator
    this.mapping['0'] = [null, 'getSepNum', 'separators'];
  },

  
    /**
    * Prepares timer/stopwatch specific data acces method
    * @see timerDataAccesMethod
    */
    prepareTimerDataAccesMethod: function(){
        //divider is used in timer/stopwatch specific timerDataAccesMethod
        var p = this.resolveTimerPrecision();
        this.divider = 1;
        switch (p){
          case ('s') : this.divider = 1000; break;
          case ('i') : this.divider = 1000*60; break;
          case ('H') :  this.divider = 1000*60*60; break;
          case ('d') : this.divider = 1000*60*60*24; break;
        }
  },

 /**
  * Sets number of digits that will be used to present number;
  * @public method of TimDisplay;
  */
  calculateMinDigitsCount: function(startTime){
    this.time = startTime;
    var t = this.timerDataAccesMethod();
    if (t>99) {
      this.minDigitsCount = 3;
    } else if (t<99 && t>9) {
      this.minDigitsCount = 2;
    }else {
      this.minDigitsCount = 1;
    }
  },

  /**
  * This method is used to retrive value of 'largest' part of time format
  * When format is e.g. %H%i%s, hours need to change from 0 to  infinity
  * and not from 0-23; When %d%H, days need to change
  * from 0 to infinity instead of 1-31...
  */
  timerDataAccesMethod: function(){
    return parseInt((this.time.getTime()-this.offset)/ this.divider);
  },
  
  /**
  * Resolves timer precision from format 
  * @returns gratest format character in oreder d, H, i, s, S
  */
  resolveTimerPrecision:function(){
    if (this.formatArray.indexOf('d') != -1) return this.timerPrecision = 'd';
    if (this.formatArray.indexOf('H') != -1) return this.timerPrecision = 'H';
    if (this.formatArray.indexOf('i') != -1) return this.timerPrecision = 'i';
    if (this.formatArray.indexOf('s') != -1) return this.timerPrecision = 's';
    return this.timerPrecision = 'S';
  },

  getSepNum: function(tmp) {    
    var ret = null;
    if (this.isNumber) {
      ret = parseInt(tmp)-1;
    }
    return ret;
  },

  /**
   * @param hour digit from 0 to 23
   * @returns 0 - am, 1 - pm
   */
  getAMorPM: function(hour) {
    var ret;
    if (hour < 13) {
      ret = 0;
    } else {
      ret = 1;
    }
    return ret.toString();
  },

  getIncrementedNumOn2Dig: function(number) {
    return this.getNumOn2Dig(number + 1);
  },

  /**
   * @param hour a hour in 12 format
   */
  getHour12: function(hour) {
    if (hour > 12) { 
      hour -= 12;
    }
    return this.getNumOn2Dig(hour);
  },

  /** 
   * Returns number on two digits.
   * If number > 99 returns only the last 2 digits.
   */
  getNumOn2Dig: function(number) {
    if (number < 10) {
      return '0' + number;
    } else if (number > 99) {
      return number.toString().substr(number.toString().length - 2);
    } else {
      return number.toString()
    }
  },

  /** 
   * Returns number on three or more digits.
   */
  getNumOn3Dig: function(number) {
    if (number < 10) {
      return '00' + number;
    } else if (number > 9 && number <100) {
      return '0' + number;
    } else {
      return number.toString()
    }
  },

  num2Str: function(number) {
    switch (this.minDigitsCount){
      case 3: return this.getNumOn3Dig(number);
      case 2: return this.getNumOn2Dig(number);
    }
    return number.toString();
  },

  /**
   * @param text string to parse
   * @param markupArray name of the array consists ids of HTML element 
   * or HTML markup which should be used.
   * @returns HTML markup to display value.
   */
  stringValueToImages: function(text, markupArray) {
    var output = '';
    if (markupArray == 'digits') {
      //digits are substituted one by one
      for (var i = 0 ; i < text.length ; i++) {
        output += this.getHTMLcode(this.digits, parseInt(text.charAt(i)));
      }
    } else {
      //other elements are substituted as one
      output += this.getHTMLcode(this[markupArray], parseInt(text));
    }
    return output;
  },
  
  /**
   * @param array array to get code from
   * @param index index of the array
   * @returns element of the array, or content of the HTML element contains
   * code.
   */
  getHTMLcode: function(array, index) {
    if (array[array.length - 1].charAt(index) == '1') {
      return $W(array[index]);
    } else {
      return array[index];
    }
  },

  /**
   * @returns true if value is a number
   */
  isNumber: function(value) {
    if (value=="") return false;
    var d = parseInt(value);
    return (!isNaN(d));
  },

  /** 
   * Preloades images.
   */
  preloadImages: function(array) {
    var k = 0;
    
    var tmp = '';
    this.mElement = document.createElement("span")

    // Make an element invisible.
    this.mElement.style.visibility = "hidden"
    this.mElement.style.padding ="0px";
    this.mElement.style.border ="0px";
    this.mElement.style.margin ="0px";
    
    for (var i = 0; i < this.arrayNames.length ; i++) {
      var arr = this[this.arrayNames[i]];
      for (var j = 0; j < arr['length']; j++) {
        if (arr[arr.length-1].charAt(j) == '1') {
          tmp += $W(arr[j]);
          var files = this.getImagesFileNames($W(arr[j]));
          if (files.length > 0) {
            for (var l = 0 ; l < files.length ; l++) {
  	          this.preloadedImages[k] = new Image();
	          this.preloadedImages[k].src = files[l];
              k++;
            }
          }
        }
      }
    }
    
    this.mElement.innerHTML = tmp;
    document.body.appendChild(this.mElement)

    this.mElement.style.display = "none"
//            document.body.removeChild(this.mElement)

    this.locked = false;
  },
  
  /**
   * @param string HTML code
   * @returns images file names or null if 'string' does not contain any image file name
   */
  getImagesFileNames: function(string) {
    var ret = new Array();
    var tmp = string;
    var ind = 0;
    var stop = false;
    while (tmp.indexOf("<img") >= 0) {
        var i = tmp.indexOf("src");
        if (i > 0) {
          tmp = tmp.substr(i + 3);
          i = tmp.indexOf("\"");
          if (i > 0) {
            tmp = tmp.substr(i+1);
            i = tmp.indexOf("\"");
            ret[ind] = tmp.substr(0, i);
            ind++;
            tmp = tmp.substr(i+1);
          }
        } else {
          tmp = '';
        }
    }
    return ret;
  }
}
);

