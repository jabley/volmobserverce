/**
 * (c) Volantis Systems Ltd 2006. 
 */

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

