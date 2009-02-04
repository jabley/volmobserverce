/**
 * (c) Volantis Systems Ltd 2006.
 */

Widget.FoldingItem = Class.create();

  // Copy fields from Appearable to widget's prototype
Object.extend(Widget.FoldingItem.prototype, Widget.Appearable);
Object.extend(Widget.FoldingItem.prototype, Widget.Disappearable);

// Widget implementation.
Object.extend(Widget.FoldingItem.prototype, {
  initialize: function(id, options) {
    this.unfoldon= "click";
    this.folded = true;
    this.showlabel= true;
    this.foldedSpan = 0;
    this.unfoldedSpan = 0;
    this.ftElementId = null;
    this.fdElementId = null;
    this.foldedSpanId = null;
    this.unfoldedSpanId = null;
    this.load_src = 0;
    this.load_when = 0;
    this.unfoldedElementStyle = {};
    this.unfoldedFtElementStyle = {};
    this.initial_state = 'inactive';
    Object.copyFields(this, options || {});

    this.locked = false;
    this.gotFocus = false;
    this.id = id;
    this.element = $(id);

    this.t = $(this.ftElementId); //title, summary
    this.d = $(this.fdElementId); //details
    this.foldedSpan = $(this.foldedSpanId);
    this.unfoldedSpan = $(this.unfoldedSpanId);

    this.folded = (this.initial_state != 'active');

    if (this.folded) {
        this.d.style.display='none';
        this.unfoldedSpan.style.display='none';
        this.foldedSpan.style.display='inline';
    } else{
        this.foldedSpan.style.display='none';
        this.unfoldedSpan.style.display='inline';
        this.loadAjax()
    }
  
    // a hack for firefox which does not notify focus event propery when
    // div element is focused with tab key
    if (Prototype.firefoxBrowser()  && this.unfoldon=='focus'){
      this.d.parentNode.style.overflow = 'hidden';
    }
    this.changeStyle();

    this.focuser = Widget.makeFocusable(this.t  );

    this.gotResponse = false;
    this.setObservers();
    if (this.load_when == "onload" && !this.folded) {
      this.loadAjax();
    }
  },

  setObservers:function(){
    if (this.unfoldon=='click'){
        this.obsrv(this.t,Widget.CLICK,this.doClick)
        if(this.focuser) {
          this.obsrv(this.focuser,Widget.CLICK,this.doClick)
        }
        if (!this.showlabel){
          this.obsrv(this.closeMarker,Widget.CLICK,this.doClick)
        }
    } else {
        this.obsrv(this.t,Widget.FOCUS,this.unfold)
        this.obsrv(this.t,Widget.BLUR,this.blur0)
        if(this.focuser) {
          this.obsrv(this.focuser,Widget.FOCUS,this.unfold)
          this.obsrv(this.focuser,Widget.BLUR,this.blur0)
        }
    }
  },

  //add observers to content part in case when opens on focus to stop folding when focus goes 
  //down to content
  addObserversToContentPart:function(){
    if (this.unfoldon=='focus'){
      Widget.addObserversToFocusableElements(this.d, Widget.FOCUS, this.checkGotFocus.bindAsEventListener(this));
      Widget.addObserversToFocusableElements(this.d, Widget.BLUR, this.blur0.bindAsEventListener(this));
    }
  },

  obsrv:function(el,event,func){
    Widget.addElementObserver(el,event,func.bindAsEventListener(this))
  },

  changeStyle: function(){
    if (this.folded) {
      this.element.vfcRevertStyle();
      this.t.vfcRevertStyle();
    } else {
      this.element.vfcReplaceStyle(this.unfoldedElementStyle);
      this.t.vfcReplaceStyle(this.unfoldedFtElementStyle);
    }
  },

  loadAjax:function(){
    if (this.load_src && !this.gotResponse){
      // first child because we need to change content of internal div
      // there is always internal div so it will work
      new Ajax.Updater(this.d.firstChild, this.load_src, {
        asynchronous:true,
        method: 'get',
        onComplete: this.addObserversToContentPart.bind(this)
      });
      this.gotResponse = true;
    }
  },

  afterEffect: function() {
    this.locked = false;
    this.checkFocus();
  },

  checkFocus: function() {
    if (this.unfoldon == "focus" ) {
      this.addObserversToContentPart();
      if (this.folded && this.gotFocus) {
          this.unfold();
      } else if (!this.folded && !this.gotFocus) {
          this.fold();
      }
    }
  },

  checkGotFocus:function(){
    this.gotFocus = true;
  },

  //if an click event occurs
  doClick: function(evt){
    Widget.stopEventPropagation(evt);
    if (this.folded) {
      this.unfold();
    } else {
      this.fold();
    }
  },

  //    the order of coling function is Blur0, Blur1, fold
  blur0:function(evt){
      Widget.stopEventPropagation(evt);
      this.gotFocus = false;
      if(this.timeout){
        window.clearTimeout(this.timeout);
      }
      this.timeout = window.setTimeout("Widget.getInstance('"+this.id+"').blur1()", 100);
  },

  blur1:function(){
    if (!this.gotFocus) {
      this.fold();
    }
  },

 /**
   * Folds the widget using the effect configured by the appropriate style.
   * Does nothing if folded or if folding effect is already running.
   *
   * @volantis-api-include-in PublicAPI
   */
  fold: function() {
    this.gotFocus = false;
    if (!this.folded && !this.locked) {
        this.locked = true;
        this.folded = true;
        this.changeStyle();
        var it = this;
        Widget.TransitionFactory.createDisappearEffect(this.d,this, {
          afterFinish: function(effect) {
            it.afterEffect();
          }
        });
        this.unfoldedSpan.style.display="none";
        this.foldedSpan.style.display="inline";
    }
  },

  /**
   * Unfolds the widget using the effect configured by the appropriate style.
   * Does nothing if unfolded or if unfolding effect is already running.
   *
   * @volantis-api-include-in PublicAPI
   */
  unfold: function() {  
    this.gotFocus = true;
    if (this.folded && !this.locked){
        this.locked = true;
        this.folded = false;
        //if using ajax response
        this.loadAjax();
        this.changeStyle();
        this.foldedSpan.style.display="none";
        this.unfoldedSpan.style.display="inline";
        this.d.parentNode.style.overflow = 'hidden';
        var it = this;
        Widget.TransitionFactory.createAppearEffect(this.d,this, {
          afterFinish: function(effect) {
            it.afterEffect();
    	  }
	    });
    }
  }

}); //end of Widget.FoldingItem
