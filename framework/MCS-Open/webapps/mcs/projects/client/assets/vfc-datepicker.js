/**
 * (c) Volantis Systems Ltd 2006. 
 */
	
Widget.DatePicker = Widget.define(Widget.Appearable, Widget.Disappearable,
{ 
  inputField: null,
  dayInputField: null,
  monthInputField: null,
  yearInputField: null,    
  unfoldon: 'click',        
  load_src: null,
  load_when: null,
  
  initialize: function(id, blockId, options) {
    this.initializeWidget(null, options)
        
    this.id = blockId;
    this.block = $W(blockId);

    //Table is created on server side but tbody on client's and its appendet here then
    this.calendarTable=$(options.calendarDisplayId);

    this.calendar = new Calendar(options,this.id);
      
    this.prepareInputField();
		   
    // when tbdy is created append it into calendar table
    // all further updates of calendar conted will be performed on
    // created table body and won't need appendChild method
    // as a result appenchChild will be removed where necessary
    this.calendarTable.appendChild(this.calendar.createTableBody());
	
    this.addAction('next-month')
    this.addAction('previous-month')
    this.addAction('next-year')
    this.addAction('previous-year')
    this.addAction('set-today')
    
    this.addAction('dismiss')
    this.addAction('show')
	
    this.addProperty('month')
    this.addProperty('year')
    
    // Initialize a client for downloading configuration contents.
    this.client = new Widget.Internal.Client();

    // Listen to request success, failure, and interrupt.
    this.observe(this.client, "requestSucceeded", "clientRequestSucceeded");
    this.observe(this.client, "requestFailed", "clientRequestFailed");
    this.observe(this.client, "requestInterrupted", "clientRequestInterrupted");
  
    this.observe(this.block, "isHiddenChanged", "_blockHiddenChanged");
        
    if (this.load_src!=0 && this.load_when != 'defer') 
      this.loadConfiguration();
  },

  clientRequestSucceeded: function(pickerResponse) {
    Widget.log("DatePicker", "Request succeeded")    
    myDate = new MyDate();
    
    myDate.setDateFromISODate(pickerResponse.getCurrentDate())    
    this.calendar.setStartDate(myDate);

    this.calendar.startRange.setDateFromISODate(pickerResponse.getRangeStart());   
    this.calendar.endRange.setDateFromISODate(pickerResponse.getRangeEnd());
    Widget.log("DatePicker", "this.calendar.startRange"+this.calendar.startRange.dateToString())
    Widget.log("DatePicker", "this.calendar.endRange"+this.calendar.endRange.dateToString())
    this.calendar.refreshCalendar(); 
    
    this.notifyObservers("monthChanged");
    this.notifyObservers("yearChanged");
    this.notifyObservers("canNextMonthChanged");
    this.notifyObservers("canPreviousMonthChanged");
    this.notifyObservers("canNextYearChanged");
    this.notifyObservers("canPreviousYearChanged");
    
  },
  
  clientRequestFailed: function(message) {
    Widget.log("DatePicker", "Request failed")
  },
  
  clientRequestInterrupted: function() {
    Widget.log("DatePicker", "Request interrupted")
  },
  
  getMonth: function() {
    return this.calendar.getMonth().toString()
  },
  
  getYear: function() {
    return this.calendar.getYear().toString()
  },
  
  // There can be one or up to three input fields, This method binds action opening widget to them
  prepareInputField: function(){
    var u = (this.unfoldon == 'click' ) ? Widget.CLICK : Widget.FOCUS
    //because click works strange on opera set action to focus
    //problem is described on known issues on Opera on Wiki
    if (Prototype.operaMobile()) u =  Widget.FOCUS;

    if (this.inputField != null) {
      this.inputField = $(this.inputField);
      Widget.addElementObserver(this.inputField,u,this.show.bindAsEventListener(this));
    }else {
      if (this.dayInputField != null) {
        this.dayInputField = $(this.dayInputField);
        Widget.addElementObserver(this.dayInputField,u,this.show.bindAsEventListener(this));
      }
      if (this.monthInputField != null){
        this.monthInputField =$(this.monthInputField);
        Widget.addElementObserver(this.monthInputField,u,this.show.bindAsEventListener(this));
      }
      if (this.yearInputField != null){
        this.yearInputField = $(this.yearInputField);
        Widget.addElementObserver(this.yearInputField,u,this.show.bindAsEventListener(this));
      }
    }
  },

  dismiss: function() {
    if (this.canDismiss()) {
      this.block.hide();
      this.notifyObservers("dismissed");
    }
  },    

  /**
  * Action taken when user clicks navigation button
  */
  previousMonth : function(){ 
    this.calendar.prevMonth();
    this.notifyObservers("monthChanged")
    this.notifyObservers("yearChanged")
    this.notifyObservers("canNextMonthChanged")
    this.notifyObservers("canPreviousMonthChanged")
    this.notifyObservers("canNextYearChanged")
    this.notifyObservers("canPreviousYearChanged")
  },
  
  /**
  * Action taken when user clicks navigation button
  */
  nextMonth : function(){
    this.calendar.nextMonth();
    this.notifyObservers("monthChanged")
    this.notifyObservers("yearChanged")
    this.notifyObservers("canNextMonthChanged")
    this.notifyObservers("canPreviousMonthChanged")
    this.notifyObservers("canNextYearChanged")
    this.notifyObservers("canPreviousYearChanged")
  },

  /**
  * Action taken when usec clickr navigation button
  */
  previousYear : function(){
    this.calendar.prevYear();
    this.notifyObservers("monthChanged")
    this.notifyObservers("yearChanged")
    this.notifyObservers("canNextMonthChanged")
    this.notifyObservers("canPreviousMonthChanged")
    this.notifyObservers("canNextYearChanged")
    this.notifyObservers("canPreviousYearChanged")
  },
  
  /**
  * Action taken when user clicks navigation button
  */
  nextYear : function(){    
    this.calendar.nextYear();
    this.notifyObservers("monthChanged")
    this.notifyObservers("yearChanged")
    this.notifyObservers("canNextMonthChanged")
    this.notifyObservers("canPreviousMonthChanged")
    this.notifyObservers("canNextYearChanged")
    this.notifyObservers("canPreviousYearChanged")
  },

  canNextMonth: function() {
    var monthAfter = new MyDate(1, this.calendar.month+1,this.calendar.getYear());
    var end = this.calendar.endRange;
    return !monthAfter.isLater(end);
  },
  
  canPreviousMonth: function() {
    var monthBefore = new MyDate(31, this.calendar.month-1,this.calendar.getYear());
    var start = this.calendar.startRange;
    return !start.isLater(monthBefore);
  },
  
  canNextYear: function() {
    var yearAfter = new MyDate(1, this.calendar.month,this.calendar.getYear()+1);
    var end = this.calendar.endRange;
	return !yearAfter.isLater(end);
  },
  
  canPreviousYear: function() {
    var yearBefore = new MyDate(31, this.calendar.month,this.calendar.getYear()-1);
    var start = this.calendar.startRange;
    return !start.isLater(yearBefore);
  },

  /**
  * Action taken when usec clicks "set today" button
  * Closes date picker
  */
  setToday : function(){    
    this.calendar.returnToday();
  },


   // Loads configuraton object
   loadConfiguration: function () {
      this.client.setURL(this.load_src)
      var parameters = {};
      this.client.setParameters(parameters);
      this.client.sendRequest();
    },


  setDismisses:function(dismisses) {
    this.dismisses = dismisses || {};
    for (property in dismisses) {
      var dismiss = $(property);
      Widget.addElementObserver(dismiss, Widget.CLICK, this.hideAndRunCallback.bindAsEventListener(this));
      Widget.makeFocusable(dismiss);
    }
  },

  /**
   * Shows the widget using the effect specified in the appropriate style.
   * Has no effect if the widget is already visible or when an appear effect is already running.
   *
   * @volantis-api-include-in PublicAPI
   */
  show: function() {
    //to be shure that all styling is properly seted refresh calendar's content
    if (this.load_src!=0 && this.load_when == 'defer') this.loadConfiguration();
    
    this.calendar.refreshCalendar();
    // notify properties that values must be updated
    this.notifyObservers("monthChanged");
    this.notifyObservers("yearChanged");
    this.notifyObservers("canNextMonthChanged");
    this.notifyObservers("canPreviousMonthChanged");
    this.notifyObservers("canNextYearChanged");
    this.notifyObservers("canPreviousYearChanged");

    if (this.canShow()) {
      this.block.show();
    }
  },

  /**
   * Hides the widget using the effect specified in the appropriate style.
   * Has no effect if the widget is already hidden or when a disappear effect is already running.
   * Needed for backward compatibility
   * 
   * @volantis-api-include-in PublicAPI
   */ 
  hide: function() {
    this.dismiss();    
  },
  
  canShow: function() {
    return this.block.isHidden();
  },  

  canDismiss: function() {
    return (!this.block.isHidden());
  },

  isHidden: function() {
    return this.block.isHidden();
  },

  _blockHiddenChanged: function() {
    this.notifyObservers("canShowChanged");
    this.notifyObservers("canDismissChanged");
  },

  /*
  * Public API methods, for configurating calendar
  */

  /**
  * Public API method to set format of date which will be returned to single input field.
  * D - day, M - month, Y- year N - month literal name e.g. "DD/MM/YYYY", "day: DD month: N year : YY"
  * Format is case sensitiv.
  */
  setDateFormat : function(format){
    this.calendar.dateFormat = format;
  },

  /**
  * Set's style to specified date.
  * @day 1-31 when 0 effect applied for each day
  * @month 1-12 when 0 effect applied for each month
  * @year when 0 effect applied for each year
  * @todo what when year = 0 is within range?
  */
  setDateStyle : function (day,month,year,style){
    // calednar accepts month as number from [1;12]
    this.calendar.setDateStyle(day,month-1,year,style);
  },
  
  setDayOfWeekStyle : function(day,style){
    this.calendar.setDayOfWeekStyle(day,style);
  },
  
  setDisabledDate:function(d,m,y){
    // calednar accepts month as number from [1;12]
    this.calendar.setDisabledDate(d,m-1,y)
  },

  setDisabledStyle: function(s){
    this.calendar.disabledStyle = s;
  },

  setDateFormat : function(format){
    this.calendar.dateFormat = format;
  },
  
  /**
   * Set start date of calendar
   * @d day
   * @m month
   * @y year
   */
  setStartRange : function(d,m,y){
    // calednar accepts month as number from [1;12]
    this.calendar.startRange = new MyDate(d,m-1,y);
  },

  /**
   * Set end range of calendar
   * @d day
   * @m month
   * @y year
   */
  setEndRange : function(d,m,y){
    // calednar accepts month as number from [1;12]
    this.calendar.endRange = new MyDate(d,m-1,y);
  },

// Over-ride the built-in month names
  setMonthNames : function() {
      for (var i=0; i<arguments.length; i++) { this.calendar.monthNames[i] = arguments[i]; }
  },
// Over-ride the built-in day names
  setDayNames : function() {
      for (var i=0; i<arguments.length; i++) { this.calendar.dayNames[i] = arguments[i]; }
  },
  // Over-ride the built-in column headers for each day
  setDayHeaders : function(){
      this.calendar.setDayHeaders(arguments);
  },

 setWeekStartDay : function(day){ this.calendar.weekStartDay = day; },

 setDayOfWeekStyle:function(day,style){  
    this.calendar.setDayOfWeekStyle(day,style);
  },
  
  /**
   * Set date displayed as disabled
   * @day
   * @month 
   * @year   
   * @todo -move to calendar method
   */
  setDisabledDate : function(day,month,year){
    // calednar accepts month as number from [1;12]
    var d = new MyDate(day,month - 1,year);
    this.calendar.disabledDates[d.dateToString()] = true;
  },

  /**
   * set week days that will be disabled
   * @arguments array of days number
   */
  setDisabledWeekDays : function(){
    Widget.log("DatePicker","setDisabledWeekDays")
    this.calendar.setDisabledWeekDays(arguments); 
  },
  
  setDisabledStyle : function (style) {
    this.calendar.disabledStyle = style;
  },

  /**
   * Set start date
   * @day
   * @month 
   * @year
   */
  setStartDate : function(day, month, year){
    // calednar accepts month as number from [1;12]
    this.calendar.setStartDate(new MyDate(day,month-1,year));
  },
  
  getSelectedDate : function(){
    return this.calendar.returnededDate.dateToString();
  }  
});



  /**
  * Calendar object represents table with days of months.
  */
Calendar = Class.define(Widget.Observer,
{
  initialize: function(options,id) {
    this.monthNames = new Array("January","February","March","April","May","June","July","August","September","October","November","December");
    this.dayNames = new Array("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
    this.dayHeaders = new Array("S","M","T","W","T","F","S");
    this.daysinmonth = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
    //default set to monday
    this.weekStartDay = 1;
    this.currentDate = null;
    this.disabledStyle = {'color':'gray'}
    this.focusedStyle ={'color':'white','backgroundColor':'black'};

    this.inputField = null;
    this.dayInputField = null;
    this.monthInputField = null;
    this.yearInputField = null;
    this.id = id;
    Object.copyFields(this, options || {});
    this.startRange;
    this.endRange;
    this.prepareInputField();
    this.dateFormat = "DD/MM/YYYY";
    this.buttons =  new Array();
    this.divs = new Array();
    this.tds = new Array();
    this.startRange = new MyDate(1,0,1);
    this.endRange = new MyDate(1,0,3000);
    this.setStartDate(new MyDate());
    this.dateStyles = {};
    this.monthStyles = {};
    this.yearStyles = {};
    this.dayOfWeekStyles = {};
    this.dateOfYearStyles = {};

    // selected month of selected year
    this.monthOfYearStyles = {};
    // selected day of each months with selected year
    this.dayOfYearStyles = {};
    this.dayOfMonthStyles = {};
    this.disabledDates = {},
    this.disabledWeekDays = {},
    this.disabledDivs = {};
  },
   
  /**
   * Resolve references to input fields where date will be passed
   */
  prepareInputField: function(){
    if (this.inputField != null) {
      this.inputField = $(this.inputField);
    }else {
      if (this.dayInputField != null) this.dayInputField = $(this.dayInputField);
      if (this.monthInputField != null) this.monthInputField =$(this.monthInputField);
      if (this.yearInputField != null) this.yearInputField = $(this.yearInputField);
    }
  },

  //this function appends information about styling specific dates.
  setDateStyle : function (day,month,year,style){
    if (day<1 && month<0){ 
    //0,0,2000
      this.yearStyles[year] = style;
    } else if (day<1 && year == 0){ 
    //0,2,0
      this.monthStyles[month] = style;
    } else if (month<0 && year == 0){  
    //2,0,0
      this.dayOfMonthStyles[day] = style;
    } else if (year == 0){  
    //2,2,0
      this.dateOfYearStyles[day+"."+month] = style;
    } else if (day < 1){  
    //0,2,2
      this.monthOfYearStyles[month+"."+year] = style;
    } else if (month < 0){  
    //2,0,2
      this.dayOfYearStyles[day+"."+year] = style;
    } else {
      var d = new MyDate(day,month,year);
      this.dateStyles[d.dateToString()] = style;
    }
  },

  setDayOfWeekStyle:function(day,style){  
    this.dayOfWeekStyles[day]=style;
  },
    
  setDisabledDate : function(day,month,year){
    var d = new MyDate(day,month,year);
    this.disabledDates[d.dateToString()] = true;
  },

  // Set which weekdays should not be clickable
  setDisabledWeekDays : function (array) {
Widget.log("DatePicker","calendar setDisabledWeekDays");
    for (var i=0; i<array.length; i++){
      Widget.log("DatePicker", array[i]);
      this.disabledWeekDays[array[i]] = true; 
    }
  },

  /**
   * Set day headers.
   * @daysList list containing descriptions of each day. 
   */ 
  setDayHeaders : function(daysList){
    var newHeader = document.createElement('TR');
    var parent = this.trHeader.parentNode;
    for (var j=0; j<7; j++) {
      var td =document.createElement('TH')
      td = Element.extend(td)
      newHeader.appendChild(td);
      var span = document.createElement('SPAN');
      td.appendChild(span);
      var node = document.createTextNode(daysList[(this.weekStartDay+j)%7])
      span.appendChild(node);
    }
    parent.insertBefore(newHeader,this.trHeader);
    parent.removeChild(this.trHeader);
    this.trHeader = newHeader;
  },
  
  setDisabledStyle : function (style) {
    this.disabledStyle = style;
  },

  //focus field gets this style
  applyFocusStyle : function(i){
    if (!this.disabledDivs[i]) this.divs[i].vfcReplaceStyle(this.focusedStyle);
  },

  //when un focusing field
  applyBlurStyle : function(i,col){
    if (this.disabledDivs[i]) return;
    var d = this.retrieveFocusedDate(i);
    this.divs[i].vfcRevertStyle();
    this.applyStyle(d,i,col);
  },

  /**
  * Creates table body, on finish calls fillCalendar to fill it with proper day numbers.
  * returns created tbody element.
  */
  createTableBody : function(){
    this.tbody = document.createElement('TBODY');        
    Element.setStyle(this.tbody,{'width':'100%','height':'100%'});
    // trHeader for first row that is header in fact
    this.trHeader = document.createElement('TR');
    this.tbody.appendChild(this.trHeader);

    //headers
    for (var j=0; j<7; j++) {
      var td =document.createElement('TH')
      td = Element.extend(td)
      this.trHeader.appendChild(td);
      var span = document.createElement('SPAN');
      td.appendChild(span);
      var node = document.createTextNode(this.dayHeaders[(this.weekStartDay+j)%7])
      span.appendChild(node);
    }

    // tr for handling remains rows
    var tr;
      //days
    var i = 1;
    for (var row=1; row<=6; row++) {
        this.tbody.appendChild(tr = document.createElement('TR'));
        for (var col=1; col<=7; col++) {
          tr.appendChild(td = document.createElement('TD'));
          this.tds[i] = td;
          this.divs[i] = document.createElement('DIV');
          Element.setStyle(this.divs[i],{'text-align':'center', 'cursor':'pointer'});

          //functions for setting style to currently focused cells
          var f = this.applyFocusStyle.bind(this,i).bindAsEventListener(this)
          var b = this.applyBlurStyle.bind(this,i,col).bindAsEventListener(this)
          Widget.addElementObserver(this.divs[i],Widget.FOCUS,f);
          Widget.addElementObserver(this.divs[i],Widget.BLUR,b);

          //create Widget.Button and add function returning date to inputfield
          this.buttons[i] = new Widget.Internal.Button(this.divs[i]);
          this.observe(this.buttons[i], "pressed", "buttonPressed", i)
          td.appendChild(this.divs[i]);

          //increment tables index
          i++;
        }
    }
    
    //fill calendar with content
    this.fillCalendar();
    return this.tbody;
  },

  buttonPressed: function(i) {
    this.returnDate(i)
  },

  //focusable element on netfront have added small 1px size button, v indicates day number
  netfrontButtonSetInnerHTML : function(el,v){
      el.innerHTML = "<input type='button' style='width:3px; font-size:1px; height:3px; backgroundColor:transparent; border:0px'>"+v;
  },

  //retrns innerHTML without focusable element
  netfrontButtonGetInnerHTML : function(el){
  //takes number after the >sign which ends input
      var s = el.innerHTML;
      s = s.split(">");
      return s[1];
  },

  /**
    * Enable or disable specified date
    */
  setEnabled : function(i, enabled){
    this.buttons[i].setEnabled(enabled); 
    if (Prototype.firefoxBrowser() ||  Prototype.msieBrowser()) {
      // On Mozilla and IE, the tabIndex property of the element has special meaning.
      // For more info see: http://developer.mozilla.org/en/docs/Key-navigable_custom_DHTML_widgets
      if (enabled) {
        this.divs[i].tabIndex = "0"
      }else{
        this.divs[i].removeAttribute('tabIndex');
      }
    }
    this.disabledDivs[i] = !enabled;
  },

  /**
  * Fills calendar table with day numbers, applies styles to specified dates
  */
  fillCalendar : function (){
	  var i = 1;
          var disabled = false;
          var d;                    
          //apply styles to specific months if they are defined
          if (this.monthStyles[this.month]) {
            //on nokia oss and firefox it is necesarry to set style to table also
            this.tbody.vfcReplaceStyle(this.monthStyles[this.month]);    
          }else  if (this.yearStyles[this.year]) {
            this.tbody.vfcReplaceStyle(this.yearStyles[this.year]);
          }else  if (this.monthOfYearStyles[this.month+"."+this.year]) {
            this.tbody.vfcReplaceStyle(this.monthOfYearStyles[this.month+"."+this.year]);
          }else {
            this.tbody.vfcRevertStyle();  
          }

          //iterates threw calendar table
          for (var row=1; row<=6; row++) {
              for (var col=1; col<=7; col++) {    
                  var selected_date = this.display_date;
                  var selected_month = this.display_month;
                  var selected_year = this.display_year;
                  //sets day number to table cell
                  if(Prototype.netFront() || Prototype.netFrontMobile()) this.netfrontButtonSetInnerHTML(this.divs[i],this.display_date)
                    else  this.divs[i].innerHTML = this.display_date;
                  
                  //create MyDate for currently processed date
                  d = new MyDate(this.display_date,this.display_month,this.display_year);
  
                 //days from former or next month should be disabled
                //days out off start-end range should be also disabled
                  if (this.display_month == this.month 
                        && d.isLaterOrEqual(this.startRange) 
                        && this.endRange.isLaterOrEqual(d)){
                    
                    this.setEnabled(i, true);

                  }else{
                    this.setEnabled(i, false);
                  }
                  
                  //disable disabled dates and week days
                  if (this.disabledDates[d.dateToString()] 
                                || this.disabledWeekDays[col]){
                    this.setEnabled(i, false);
                  }
                  
                  this.applyStyle(d,i,col);
                  
                  //increment day, and if jump thre month end change month and year if necessary
                  this.display_date++;
                  if (this.display_date > this.daysinmonth[this.display_month]) {
                      this.display_date=1;
                      this.display_month++; 
                      }
                  if (this.display_month > 11) {
                      this.display_month=0;
                      this.display_year++;
                      }
                    //increment cell number
                    i++;
                  }
              }
      return this.tbody;

 },

//applies style to calendar cell if it's defined
//d indicates which style, i indicates which div
//if no specific style is defined for date, does nothing
  applyStyle:function(d,i,col){
    if (this.dateStyles[d.dateToString()]) {
      //explicite date
      this.divs[i].vfcReplaceStyle(this.dateStyles[d.dateToString()]);
    } else if (this.disabledDates[d.dateToString()]) {
      //explicite disabled date
      this.divs[i].vfcReplaceStyle(this.disabledStyle);
    } else if (this.dateOfYearStyles[d.getDate()+"."+d.getMonth()]) {
      //e.g.christmas are each year
      this.divs[i].vfcReplaceStyle(this.dateOfYearStyles[d.getDate()+"."+d.getMonth()]);
    } else if (this.monthOfYearStyles[d.getMonth()+"."+d.getYear()]) {
      //e.g.two months of shool-holidays
      this.divs[i].vfcReplaceStyle(this.monthOfYearStyles[d.getMonth()+"."+d.getYear()]);
    } else if (this.dayOfMonthStyles[d.getDate()]) {
      //e.g. eg all 1'st days of month
      this.divs[i].vfcReplaceStyle(this.dayOfMonthStyles[d.getDate()]);
    } else if (this.dayOfWeekStyles[col]) {
    //day of week
      this.divs[i].vfcReplaceStyle(this.dayOfWeekStyles[col]);
    } else if (this.dayOfYearStyles[d.getDate()+"."+d.getYear()]) {
    //day of week
      this.divs[i].vfcReplaceStyle(this.dayOfYearStyles[d.getDate()+"."+d.getYear()]);
    } else if (this.disabledWeekDays[col]) {
    //day of week
      this.divs[i].vfcReplaceStyle(this.disabledStyle);
    } 
  },

//recalculates dates and fill's calendar
  refreshCalendar:function(){    
    this.setStartDate(this.currentDate);
    return this.fillCalendar();
  },

//returns focused date
  retrieveFocusedDate : function(day){
    var d = new MyDate();
    d.date = this.divs[day].innerHTML;
    d.month = this.month;
    d.year = this.getYear();
    return d;
  },

//types selected date to input field
  returnDate : function(day){  
    var i;
    if  (day>-1){
      //on netfront there is focusable button added
      if (Prototype.netFront() || Prototype.netFrontMobile()) i = this.netfrontButtonGetInnerHTML(this.divs[day]);
        else i = this.divs[day].innerHTML;
      this.returnedDate =  new MyDate(i,this.month,this.year);
    }else{      
      this.returnedDate =  new MyDate();
      i=this.returnedDate.getDate();
      this.month = this.returnedDate.getMonth();
      this.year = this.returnedDate.getYear();
    }
    if (this.inputField!=null) {            
      this.inputField.value = this.formatDate(this.returnedDate,this.dateFormat);
    } else {
      if (this.dayInputField != null) this.dayInputField.value = i;
      if (this.monthInputField != null) this.monthInputField.value = this.month+1;
      if (this.yearInputField != null) this.yearInputField.value = this.year;
    }
    //hides hole widget because date is selected.
    Widget.getInstance(this.id).hide();
    this.setStartDate(this.returnedDate);
    return this.returnededDate;
  },
  
  //types to input field current date.
  returnToday : function(){  
    this.returnDate(-1);
  },

  LZ : function(x) {
    return(x<0||x>9?"":"0")+x
  },


//sets format of date which will be returned to input field
  formatDate : function(date,format){
    format=format+"";
    var result="";
    var i_format=0;
    var c="";
    var token="";
    var Y=date.getYear()+"";
    var M=date.getMonth()+1;
    var D=date.getDate();
    var N=this.monthNames[date.getMonth()];
    var YYYY,YY,MMM,MM,DD;
    // Convert real date parts into formatted versions
    var value=new Object();
    if (Y.length < 4) {Y=""+(Y-0+1900);}
    value["Y"]=""+Y;
    value["YYYY"]=Y;
    value["YY"]=Y.substring(2,4);
    value["M"]=M;
    value["MM"]=this.LZ(M);
    value["D"]=D;
    value["DD"]=this.LZ(D);
    value["N"]=N;

    while (i_format < format.length) {
        c=format.charAt(i_format);
        token="";
        while ((format.charAt(i_format)==c) && (i_format < format.length)) {
            token += format.charAt(i_format++);
            }
        if (value[token] != null) { result=result + value[token]; }
        else { result=result + token; }
        }    
    return result;
    },

  nextMonth : function(){
    this.month++;
    if (this.month > 11) {
      this.month=0;
      this.year++;      
    }
    this.calculateDates();    
    this.fillCalendar();
    return this.tbody;
  },
  
  prevMonth : function(){
    this.month--;
    if (this.month < 0) {
      this.month=11;
      this.year--;      
    }
    this.calculateDates();
    this.fillCalendar();
    return this.tbody;
  },

  // return literal name of current month
  getMonth : function(){    
  	return this.monthNames[this.month];
  },

  nextYear : function(){
    this.year++;
    this.calculateDates();    
    this.fillCalendar();
    return this.tbody;
  },
  
  prevYear : function(){
    this.year--;
    this.calculateDates();
    this.fillCalendar();
    return this.tbody;
  },
  
 //returns current year
  getYear : function(){
  	return this.year;
  },

  getCurrentDate : function(){  
    return this.currentDate;
  },

/**
* Set's start page of calendar to be the one containing specified start date 
*/
  setStartDate : function(date){
Widget.log("DatePicker", "seting start date"+this.startRange.dateToString())
    this.currentDate = date;
    //move to start or end of range if now is outside
    if (this.currentDate.isLater(this.endRange)){
      this.currentDate = this.endRange;
    } else if (this.startRange.isLater(this.currentDate)){
      this.currentDate = this.startRange;
    }
    //set's current month and year
    this.month = this.currentDate.getMonth();
    this.year = this.currentDate.getFullYear(); 
    this.calculateDates();
  },

  //order's day of month to day of week
  calculateDates : function(){
    if ( ( (this.year%4 == 0)&&(this.year%100 != 0) ) || (this.year%400 == 0) ) {
        this.daysinmonth[1] = 29;
    } else {this.daysinmonth[1] = 28}

    this.current_month = new Date(this.year,this.month,1);
    this.display_year = this.year;
    this.display_month = this.month;
    this.display_date = 1;
    this.weekday= this.current_month.getDay();
    //number of days to display from former month
    this.offset = 0;
    this.offset = (this.weekday >= this.weekStartDay) ? this.weekday-this.weekStartDay : 7-this.weekStartDay+this.weekday ;
    if (this.offset > 0) {
        this.display_month--;
        if (this.display_month < 0) { this.display_month = 11; this.display_year--; }
        this.display_date = this.daysinmonth[this.display_month]-this.offset+1;
        }
    this.next_month = this.month+1;
    this.next_month_year = this.year;
    if (this.next_month > 11) { this.next_month=0; this.next_month_year++; }
    this.last_month = this.month-1;
    this.last_month_year = this.year;
    if (this.last_month < 0) { this.last_month=11; this.last_month_year--; }
  }

});

//Simply implemantation of date.
//holds date, month, year as integers.
MyDate = Class.create();
Object.extend(MyDate.prototype, {
  initialize: function(date,month,year) {
    if (arguments.length==0) {
      this.myDateFromDate(new Date());
    } else {
      this.date = date;
      this.month = month;
      this.year = year;
    }
  },

  getDate : function(){
    return this.date;
  },
  
  getMonth : function(){
    return this.month;
  },

  getYear : function(){
    return this.year;
  },
  
  isLater : function(d){
    if (this.year>d.year) return true;
    if (this.year<d.year) return false;
    if (this.month>d.month) return true;
    if (this.month<d.month) return false;
    return (this.date>d.date);
  },
  
  isEqual : function(d){
   return (this.year==d.year && this.month==d.month && this.date==d.date);
  },

  isLaterOrEqual : function(d){
    return this.isLater(d) || this.isEqual(d);
  },
  
  getFullYear : function(){
    return this.year;
  },

  //Builds my date object from javascript object.
  myDateFromDate : function (date){
    this.date = date.getDate();
    this.month = date.getMonth();
    if (Prototype.msieBrowser()) {
    // Javascript Date object under msie differs from objects in other browsers. It returns actual year instead of year - 1900
      this.year = date.getYear();
    }
    else this.year = date.getYear()+1900;
  },

    //Builds my date object from string in ISO format (yyyy-mm-dd).
  setDateFromISODate : function (date){
    if (date !== null) {
      var aDate = date.split('-');        
      this.year = parseInt(aDate[0]);
      this.month = parseInt(aDate[1],10) -1;
      this.date = parseInt(aDate[2],10);
    }
  },

  
  dateToString : function(){
    return ""+this.date+"."+(this.month+1)+"."+this.year;
  },

  addMonth : function(){
    this.month++;
  },
  addDay : function(){
    this.date++;
  }
});


Widget.Response.DatePicker = Class.define(
{
	initialize: function(currentDate, rangeStart, rangeEnd) {
	  this.currentDate = currentDate;
	  this.rangeStart = rangeStart;
	  this.rangeEnd = rangeEnd;	
  },
  
  getCurrentDate: function() {
    return this.currentDate;
  },

  getRangeStart: function() {
    return this.rangeStart;
  },
  
  getRangeEnd: function() {
    return this.rangeEnd;
  }   
})

