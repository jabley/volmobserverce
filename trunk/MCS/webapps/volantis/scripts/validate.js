/*
(c) Volantis Systems Ltd 2002. 
*/

var whitespace = " \t\n\r";
var numerics = "0123456789";
var lcalpha = "abcdefghijklmnopqrstuvwxyz";
var ucalpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
var punct = ".,';:";
var symbols = "$%^&";

var seperators = "-.+ ";

var dateprefixes = "DMY";
var timeprefixes = "Hms";
var datechars =  dateprefixes + "/-"
var timechars = timeprefixes + " :a"

var daysInMonth = new makeArray(12);

daysInMonth[0] = 31;
daysInMonth[1] = 29;   // must programmatically check this
daysInMonth[2] = 31;
daysInMonth[3] = 30;
daysInMonth[4] = 31;
daysInMonth[5] = 30;
daysInMonth[6] = 31;
daysInMonth[7] = 31;
daysInMonth[8] = 30;
daysInMonth[9] = 31;
daysInMonth[10] = 30;
daysInMonth[11] = 31;


// This trick is needed for JavaScript 1.0
function makeArray(n) {
	this.length = n;
	return this;
}

// trim leading and trailing whitespace from a string
function trimStr(str) {
	var len = str.length
	var i, j;
	// find leading whitespace
	for(i=0; i<len; i++) {
		c = str
		if(whitespace.indexOf(str.charAt(i)) == -1) {
			break;
		}
	}
	// all whitespace
	if( i == len)	{
		return "";
	}
	// find trailing whitespace
	for(j=len-1; j>=0; j--) {
		if(whitespace.indexOf(str.charAt(j)) == -1) {
			break;
		}
	}
	if(i == 0 && j == len - 1) {
		return str;
	}
	return str.substring(i, j+1);
}

// Check whether string s is empty.
function isEmpty(s) {   
	return ((s == null) || (s.length == 0))
}

function containsOnly(str, validChars) {
	var c;
    	for (var i = 0; i < str.length; i++) {
        	c = str.charAt(i);
        	if (validChars.indexOf(c) == -1) {
			// found invalid char
			return false;
		}
    	}
	// All is valid
	return true;
}

function validIntRange (str, a, b) {
	if (isEmpty(str)) {
		return false
	} 
      
    	// Catch non-integer strings to avoid creating a NaN
    	// which isn't available on JavaScript 1.0 for Windows.
    	if (!containsOnly(str, numerics)) {
		return false;
	}

	// Now, explicitly change the type to integer via parseInt
    	// so that the comparison code below will work both on 
    	// JavaScript 1.2 (which typechecks in equality comparisons)
    	// and JavaScript 1.1 and before (which doesn't).
    	var num = parseInt (str);
    	return ((num >= a) && (num <= b));
}

function daysInFebruary (year) {
	// February has 29 days in any year evenly divisible by four,
    	// EXCEPT for centurial years which are not also divisible by 400.
    	return (  ((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0) ) ) ? 29 : 28 );
}

function checkDate(day, month, year) {
	var intYear = -1;
	var intMonth = -1;
	var inDay = -1;	
	
	if(year != "") {
		if(!containsOnly(year, numerics)) {
			return false;
		}
		intYear = parseInt(year);
		if(intYear < 0) {
			return false;
		}
	}
	if(month != "") {
		if(!containsOnly(month, numerics)) {
			return false;
		}
		intMonth = parseInt(month);
		if(intMonth < 1 || intMonth > 12) {
			return false;
		}
	}
	if(day != "") {
		if(!containsOnly(day, numerics)) {
			return false;
		}
		intDay = parseInt(day);
		// catch invalid days, except for February
    		if(intMonth != -1 && intDay > daysInMonth[intMonth - 1]) {
			return false; 
		}

    		if(intMonth == 2 && intYear != -1 && intDay > daysInFebruary(intYear)) {
			 return false;
		}
	}
	return true;
}

function checkTime(hour, minutes, seconds, twentyFourHours) {
	var maxHour = (twentyFourHours)?23:12;
	var minHour = (twentyFourHours)?0:1;
	if(hour != "" && !validIntRange(hour, minHour, maxHour)) {
		return false;
	}
	if(minutes != "" && !validIntRange(minutes, 0, 59)) {
		return false;
	}
	if(seconds != "" && !validIntRange(seconds, 0, 59 )) {
		return false;
	}
	return true;	
}

function ParseState(pIndex, sIndex, prevState) {
	this.pIndex = pIndex;
	this.sIndex = sIndex;
	this.previousState = prevState;
}


function RegEx(pattern, str) {

	this.pattern = trimStr(pattern);
	this.str = trimStr(str);
	// index to mark parse location in strings
	// as we try  to find a match
	this.pIndex = 0;
	this.sIndex = 0;

	// chain of ParseState objects so that
	// we can backtrack to saved states if
	// we can't find a match
	this.savedStates = null;

	// method
	this.match = match;

	// helper methods
	this.checkMatched = checkMatched;
	this.doCharsMatch = doCharsMatch;

	// helper methods to manipulate caharcters
	// in the pattern string and string we are 
	// trying to match	
	this.popNextPChar = popNextPChar;
	this.peekNextPChar = peekNextPChar;
	this.popNextSChar = popNextSChar;
	this.peekNextSChar = peekNextSChar;
	this.putBackLastSChar = putBackLastSChar;
	this.hasMorePChars = hasMorePChars;
	this.hasMoreSChars = hasMoreSChars;

	this.recordState = recordState;
	this.restoreState = restoreState;
	this.doMatch = doMatch;

	this.dateOK = dateOK;
	this.timeOK = timeOK;
}

function match() {
	while(!this.doMatch()) {
		if(!this.restoreState()) {
			return false;
		}
	}
	return true;		
	
}


function popNextPChar() {
	var c = null;
	if(this.pIndex < this.pattern.length) {
		c = this.pattern.charAt(this.pIndex++);
	}
	return c;
}

function peekNextPChar() {
	var c = null;
	if(this.pIndex < this.pattern.length) {
		c = this.pattern.charAt(this.pIndex);
	}
	return c;
}

function peekNextSChar() {
	var c = null;
	if(this.sIndex < this.str.length) {
		c = this.str.charAt(this.sIndex);
	}
	return c;
}

function popNextSChar() {
	var c = null;
	if(this.sIndex < this.str.length) {
		c = this.str.charAt(this.sIndex++);
	}
	return c;
}

function putBackLastSChar() {
	if(this.sIndex != 0) {
		this.sIndex--;
	}
}

function hasMorePChars() {
	return (this.pIndex < this.pattern.length);
}

function hasMoreSChars() {
	return (this.sIndex < this.str.length);
}

function doMatch() {
	var p, s;
	while(this.hasMorePChars()) {

		p = this.popNextPChar();
		s = this.popNextSChar();
		if(p == "*" && this.hasMorePChars()) {
			p = this.popNextPChar();
			if(s == null) {
				continue;
			}
			var matches = 0;
			while(this.doCharsMatch(p, s)) {
				this.recordState(this.pIndex-1, this.sIndex-1);
				if(!this.doCharsMatch(p,this.peekNextSChar())){
					break;
				}
				s = this.popNextSChar();
				matches++;	
			}
			if(matches == 0) {
				this.putBackLastSChar();
			}
		}
		else if(dateprefixes.indexOf(p) != -1) {
			if(!this.dateOK(p, s)) {
				return false;
			}
		}
		else if(timeprefixes.indexOf(p) != -1) {
			if(!this.timeOK(p, s)) {
				return false;
			}
		}
		else if(!this.doCharsMatch(p, s)) {
			return false;
		}
	}	
	// we have got to the end of the pattern string.
	// check to see if we matched all characters in the
	// target string
	return this.checkMatched();
}

function dateOK(p, s) {	
	var datepattern = "";
	var datepart = "";

	var day = "";
	var month = "";
	var year = "";

	while(datechars.indexOf(p) != -1 && 
	      this.hasMorePChars() && this.hasMoreSChars()) {
		datepattern = p;
		datepart = s;
		if(p == "D" || p == "M" || p == "Y") {
			while(this.peekNextPChar() == p) { 
				datepattern += this.popNextPChar(); 
			}
			while(numerics.indexOf(this.peekNextSChar())!=-1) {
				datepart += this.popNextSChar();
			}
			if(p == "D") { day = datepart; }
			else if(p == "M") { month = datepart; }
			else if(p == "Y") { year = datepart; }
		}
		else if(p != s) {
			return false;
		}
		if(datepart.length < datepattern.length) {
			return false;
		}

		p = this.popNextPChar();
		s = this.popNextSChar();
	}
	
	return checkDate(day, month, year);
}

function timeOK(p, s) {	

	var twfh = true;
	var timepattern = "";
	var timepart = "";

	var hour = "";
	var minutes = "";
	var seconds = "";

 	while(timechars.indexOf(p) != -1 && this.hasMorePChars()) {
		timepattern = p;
		timepart = s;
		if(p == "H" || p == "m" || p == "s") {
			while(this.peekNextPChar() == p) { 
				timepattern += this.popNextPChar(); 
			}
			while(numerics.indexOf(this.peekNextSChar())!=-1) {
				timepart += this.popNextSChar();
			}
			if(p == "H") { hour = timepart; }
			else if(p == "m") { minutes = timepart; }
			else if(p == "s") { seconds = timepart; }
		}
		else if(p == " ") {
			if(this.peekNextPChar() == "a") {
				var ind = s + this.peekNextSChar() 
					+ this.str.charAt(this.sIndex + 1);
				if(ind = " PM") {
					twfh = false;
					this.popNextSChar();
					this.popNextSChar();
					this.popNextPChar();
					return checkTime(hour, minutes,
							seconds, twfh);
				}
			}
		}
		else if(p != s) {
			return false;
		}

		p = this.popNextPChar();
		s = this.popNextSChar();
	}
	return checkTime(hour, minutes, seconds, twfh);
}


function doCharsMatch(p, s) {
	if(p == "#" && numerics.indexOf(s) != -1) {
		return true;
 	}
	else if(seperators.indexOf(p) != -1 && s == p) {
		return true;
	}
	else if(p == "A" && (ucalpha + punct).indexOf(s) != -1) {
		return true;
	}
	else if(p == "a" && (lcalpha + punct).indexOf(s) != -1) {
		return true;
	}
	else if(p == "X" && ucalpha.indexOf(s) != -1) {
		return true;
	}
	else if(p == "x" && lcalpha.indexOf(s) != -1) {
		return true;
	}
	else if( (p == "Z" || p == "z") && s != null) {
		return true;
	}
	else if(p == "S" && (numerics + punct + symbols).indexOf(s) != -1) {
	    return true;
	}
	return false;
}

function checkMatched() {
	return ((this.pIndex == this.pattern.length) &&
	    	(this.sIndex == this.str.length) );
		
}

// Chain together the parse state at a given instance
// this is required because we are being greedy when encountering
// a *. If no match we can visted previous states and follow
// a different route.
function recordState(pIndex, sIndex) {
	var newParseState = new ParseState(pIndex, sIndex, this.savedStates);
	this.savedStates = newParseState;
}
	
// restore a previous parse state
function restoreState() {
	if(this.savedStates == null) {
		return false;
	}
	// restore saved state
	this.pIndex = this.savedStates.pIndex;
	this.sIndex = this.savedStates.sIndex;

	this.savedStates = this.savedStates.previousState;
	return true;
}
