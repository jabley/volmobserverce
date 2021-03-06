/**
 * (c) Volantis Systems Ltd 2006.
 */

Widget.FieldExpander = Class.create();

   // Copy fields from Appearable to widget's prototype
Object.extend(Widget.FieldExpander.prototype, Widget.Appearable);
Object.extend(Widget.FieldExpander.prototype, Widget.Disappearable);

   // Widget implementation.
Object.extend(Widget.FieldExpander.prototype, {
  initialize: function(id, options) {
    this.id = id;
    this.element = $(id);
    this.unfoldon= "click";
    this.objName = 0;
    this.folded = true;
    this.foldedMarker = "+ ";
    this.unfoldedMarker = "- ";
    this.foldedSpan = null;
    this.unfoldedSpan = null;
    this.foldedImg= 0;
    this.unfoldedImg= 0;
    this.foldedStyle = 0;
    this.unfoldedStyle = 0;
    this.initial_state = 'inactive';
    this.ftElementId = null;
    this.fdElementId = null;
    this.primFieldId = null;
    this.foldedSpanId = null;
    this.unfoldedSpanId = null;
    this.locked = false;
    Object.copyFields(this, options || {});

    if (this.initial_state == 'active') {
      this.folded = false;
    }
    this.findElements();
    this.prepareStyles();
    this.preparePrimField();
    this.prepareFocuser();
    this.setObservers();
    this.gotFocus = false;
  },

  //searches for DOM elements used in further implementation
  findElements:function(){
    //the two spans
    this.foldedSpan = $(this.foldedSpanId);
    this.unfoldedSpan = $(this.unfoldedSpanId);

    // and summary, details and field elements
    this.ftElement = $(this.ftElementId);
    this.fdElement = $(this.fdElementId);
    this.primField = $(this.primFieldId);
  },

  //only chcecks the checkbox if necesary
  preparePrimField:function(){
    if (this.primField.type === "checkbox") {
        this.primField.checked = !this.folded;
    }
  },

  setObservers:function(){
      if (this.unfoldon=='click'){
          Widget.addElementObserver(this.primField,Widget.CLICK,
            this.doClick.bindAsEventListener(this));
      } else {
          Widget.addElementObserver(this.focuser,Widget.FOCUS,
            this.doFocus.bindAsEventListener(this));
          Widget.addElementObserver(this.focuser,Widget.BLUR,
            this.blur0.bindAsEventListener(this));
          this.addObserversToContentPart();
      }
  },

  addObserversToContentPart:function(){
      var collection = this.fdElement.getElementsByTagName('a');
      var i=0;
      for (i=0;i<collection.length;i++){
          Widget.addElementObserver(collection[i],Widget.FOCUS,
            this.checkGotFocus.bindAsEventListener(this));
          Widget.addElementObserver(collection[i],Widget.BLUR,
            this.blur0.bindAsEventListener(this));
      }

      collection = this.fdElement.getElementsByTagName('input');
      i=0;
      for (i=0;i<collection.length;i++){
          Widget.addElementObserver(collection[i],Widget.FOCUS,
            this.checkGotFocus.bindAsEventListener(this));
          Widget.addElementObserver(collection[i],Widget.BLUR,
            this.blur0.bindAsEventListener(this));
      }
  },


    //makes the primary field a focusable object
  prepareFocuser: function(){
      this.focuser = this.primField;
      this.focuser.style.display = 'inline';
   },

  prepareStyles:function(){
      this.foldedStyle = {};
      for (var s in this.unfoldedStyle){
          this.foldedStyle[s] = Element.getStyle(this.ftElement,s);
      }
      if (this.folded) {
        if (this.foldedStyle) {
          this.changeStyle();
        }
        this.fdElement.style.display='none';
        this.foldedSpan.style.display = 'inline';
        this.unfoldedSpan.style.display = 'none';
      } else {
        this.foldedSpan.style.display = 'none';
        this.unfoldedSpan.style.display = 'inline';
        if (this.unfoldedStyle) {
          this.changeStyle();
        }
      }
  },

  changeStyle: function(){
      if (this.folded) {
          Element.setStyle(this.ftElement,this.foldedStyle);
          Element.setStyle(this.foldedSpan,this.foldedStyle);
          Element.setStyle(this.unfoldedSpan,this.foldedStyle);
      } else {
          Element.setStyle(this.ftElement,this.unfoldedStyle);
          Element.setStyle(this.foldedSpan,this.unfoldedStyle);
          Element.setStyle(this.unfoldedSpan,this.unfoldedStyle);
      }
  },


//if an click event occurs
  doClick: function(evt){
      Widget.stopEventPropagation(evt);
      if (this.folded) {
        this.doUnfold();
      } else {
        this.doFold();
      }
  },

  doFocus: function(evt){
      Widget.stopEventPropagation(evt);
      this.gotFocus = true;
      this.doUnfold();
  },

  //    the order of coling function is Blur0, Blur1, doBlur
  blur0:function(evt){
      Widget.stopEventPropagation(evt);
      this.gotFocus = false;
      if(this.timeout){
        window.clearTimeout(this.timeout);
      }
      this.timeout = window.setTimeout("Widget.getInstance('"+this.id+"').blur1()", 2000);
  },

  blur1:function(){
    if (!this.gotFocus) {
      this.doBlur();
    }
  },

  checkGotFocus:function(){
    this.gotFocus = true;
  },

  doBlur: function(){
    this.doFold();
  },

  afterEffect: function() {
    this.locked = false;
    this.checkFocus();
  },

  checkFocus: function() {
    if (this.unfoldon == "focus") {
      if (this.folded && this.gotFocus) {
        this.doUnfold();
      } else if (!this.folded && !this.gotFocus) {
        this.doFold();
      }
    }
  },

//makes the transition
  doFold: function() {
    if (!this.locked) {
      this.locked = true;
      this.folded = true;
      if (this.foldedStyle) {
        this.changeStyle();
      }
      Widget.TransitionFactory.createDisappearEffect(this.fdElement,this, {
          id: this.id,
          afterFinish: function(effect) {
            Widget.getInstance(this.id).afterEffect();
          }
        });
      this.foldedSpan.style.display = 'inline';
      this.unfoldedSpan.style.display = 'none';
    }
  },

//makes the transition
  doUnfold: function() {
    if (this.folded && !this.locked) {
      this.locked = true;
      this.folded = false;
      if (this.unfoldedStyle) {
        this.changeStyle();
      }
      this.foldedSpan.style.display = 'none';
      this.unfoldedSpan.style.display = 'inline';
      Widget.TransitionFactory.createAppearEffect(this.fdElement,this, {
          id: this.id,
          afterFinish: function(effect) {
            Widget.getInstance(this.id).afterEffect();
          }
        });
    }
  }

}); //end of Widget.FieldExpander

/* instaed of + - signs we can use arrows with unicode values
    setSigns:function(){
        // s1 = '(\u2193');
        // s2= ('\u2191');
    },
*/