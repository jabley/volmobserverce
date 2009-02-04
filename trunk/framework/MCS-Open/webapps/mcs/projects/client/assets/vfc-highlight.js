/**
 * (c) Volantis Systems Ltd 2006.
 */

/*------------------ Widget.Highlightable ----------------------------*/
Widget.Highlightable = {
  time: 1,        //time in seconds
  startColor : '#ffff99',    //lightyellow
  endColor : '#ffffff'    //white
};

Widget.TransitionFactory.createHighlightEffect = function(element, highlightable, extension) {
    var el = $(element);
    var hl = highlightable || {};
    var ext = extension || {};
    //default disappear options
    var opt = {
      duration: hl.time,
      startcolor: hl.startColor,
      endcolor: hl.endColor,
      restorecolor: hl.endColor
    };
    return new Effect.Highlight(el, Object.extend(opt, ext));
};

// ObjectRange and $R moved from prototype as it is used only in this widget
ObjectRange = Class.create();
Object.extend(ObjectRange.prototype, Enumerable);
Object.extend(ObjectRange.prototype, {
  initialize: function(start, end, exclusive) {
    this.start = start;
    this.end = end;
    this.exclusive = exclusive;
  },

  _each: function(iterator) {
    var value = this.start;
    do {
      iterator(value);
      value = value.succ();
    } while (this.include(value));
  },

  include: function(value) {
    if (value < this.start)
      return false;
    if (this.exclusive)
      return value < this.end;
    return value <= this.end;
  }
});

var $R = function(start, end, exclusive) {
  return new ObjectRange(start, end, exclusive);
}

//Efect taken moved scriptaculous.js as they are used only in this widget:

Effect.Highlight = Class.create();
Object.extend(Object.extend(Effect.Highlight.prototype, Effect.Base.prototype), {
  initialize: function(element) {
    this.element = $(element);
    var options = Object.extend({ startcolor: '#ffff99' }, arguments[1] || {});
    this.start(options);
  },
  setup: function() {
    // Prevent executing on elements not in the layout flow
    if(this.element.getStyle('display')=='none') { this.cancel(); return; }
    // Disable background image during the effect
    this.oldStyle = {
      backgroundImage: this.element.getStyle('background-image') };
    this.element.setStyle({backgroundImage: 'none'});
    if(!this.options.endcolor)
      this.options.endcolor = this.element.getStyle('background-color').parseColor('#ffffff');
    if(!this.options.restorecolor)
      this.options.restorecolor = this.element.getStyle('background-color');
    // init color calculations
    this._base  = $R(0,2).map(function(i){ return parseInt(this.options.startcolor.slice(i*2+1,i*2+3),16) }.bind(this));
    this._delta = $R(0,2).map(function(i){ return parseInt(this.options.endcolor.slice(i*2+1,i*2+3),16)-this._base[i] }.bind(this));
  },
  update: function(position) {
    this.element.setStyle({backgroundColor: $R(0,2).inject('#',function(m,v,i){
      return m+(Math.round(this._base[i]+(this._delta[i]*position)).toColorPart()); }.bind(this)) });
  },
  finish: function() {
    this.element.setStyle(Object.extend(this.oldStyle, {
      backgroundColor: this.options.restorecolor
    }));
  }
});

//Widget implementation

Widget.HighlightingNavigation = Class.create();

Object.extend(Widget.HighlightingNavigation.prototype, Widget.Highlightable);

Object.extend(Widget.HighlightingNavigation.prototype, {

  initialize: function(id, options) {
    this.id = id;
    Object.copyFields(this, options || {});

    this.checkColor();

    Widget.addElementObserver($(this.id), Widget.FOCUS,
      this.highlightStart.bindAsEventListener(this));
    Widget.addElementObserver($(this.id),Widget.BLUR,
      this.highlightEnd.bindAsEventListener(this));
  },

  highlightStart: function(evt) {
    Widget.TransitionFactory.createHighlightEffect(this.id, this);
  },

  highlightEnd: function(evt) {
    var color = this.endColor;
    this.endColor = this.startColor;
    this.startColor = color;

    Widget.TransitionFactory.createHighlightEffect(this.id, this);

    color = this.endColor;
    this.endColor = this.startColor;
    this.startColor = color;
  },

  checkColor: function() {
    if(this.startColor.charAt(0) == '#') {
      this.startColor = this.startColor.parseColor();
    } else {
      this.startColor = namedColors[this.startColor];
    }

    if(this.endColor.charAt(0) == '#') {
      this.endColor = this.endColor.parseColor();
    } else {
      this.endColor = namedColors[this.endColor];
    }
  }
});

