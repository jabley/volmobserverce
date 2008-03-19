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
    this.isFocus = false;
    this.id = id;
    this.element = $(id);

    this.ftElement = $(this.ftElementId);
    this.fdElement = $(this.fdElementId);
    this.foldedSpan = $(this.foldedSpanId);
    this.unfoldedSpan = $(this.unfoldedSpanId);

    this.folded = (this.initial_state != 'active');

    if (this.folded) {
        this.fdElement.style.display='none';
        this.unfoldedSpan.style.display='none';
        this.foldedSpan.style.display='inline';
    } else{
        this.foldedSpan.style.display='none';
        this.unfoldedSpan.style.display='inline';
    }
  
    // a hack for firefox which does not notify focus event propery when
    // div element is focused with tab key
    if (Prototype.firefoxBrowser()  && this.unfoldon=='focus'){
      this.fdElement.parentNode.style.overflow = 'hidden';
    }
    this.changeStyle();

    this.focuser = Widget.makeFocusable(this.ftElement);
    this.gotResponse = false;
    this.setObservers();
    if (this.load_when == "onload") {
      this.loadAjax();
    }
  },


  setObservers:function(){
    if (this.unfoldon=='click'){
        Widget.addElementObserver(this.ftElement,Widget.CLICK,
          this.doClick.bindAsEventListener(this));
        if(this.focuser) {
          Widget.addElementObserver(this.focuser,Widget.CLICK,
            this.doClick.bindAsEventListener(this));
        }
        if (!this.showlabel){
          Widget.addElementObserver(this.closeMarker,Widget.CLICK,
            this.doClick.bindAsEventListener(this));
        }
    } else {
        if(this.focuser) {
          Widget.addElementObserver(this.focuser,Widget.FOCUS,
            this.doUnfold.bindAsEventListener(this));
          Widget.addElementObserver(this.focuser,Widget.BLUR,
            this.doFold.bindAsEventListener(this));
        }
    }
  },

//prepares a hidden input element which will catch focus
//   prepareFocuser: function(){
//     var d = document.createElement("div");
//     d.style.display = 'block';
//     d.style.overflow = 'hidden';
//     d.style.height = '1px';
//     d.style.width = '1px';
//     d.style.cursor = 'pointer';
//     d.style.border = '0px';
//
//     this.focuser = document.createElement("input");
//     this.focuser.type = "button";
//     this.focuser.style.height = '1px';
//     this.focuser.style.width = '1px';
//     this.focuser.style.backgroundColor='transparent';
//     this.focuser.style.border='0px';
//     this.focuser.style.position = 'absolute';
//
//     d.appendChild(this.focuser);
//     $(this.element).insertBefore(d, this.ftElement || null);
//   },

  changeStyle: function(){
    if (this.folded) {
      this.element.vfcRevertStyle();
      this.ftElement.vfcRevertStyle();
    } else {
      this.element.vfcReplaceStyle(this.unfoldedElementStyle);
      this.ftElement.vfcReplaceStyle(this.unfoldedFtElementStyle);
    }
  },

  loadAjax:function(){
    if (this.load_src && !this.gotResponse){
      // first child because we need to change content of internal div
      // there is always internal div so it will work
      new Widget.AjaxUpdater(this.fdElement.firstChild, this.load_src, {
        asynchronous:true,
        method: 'get'
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
        if (this.folded && this.isFocus) {
            this.doUnfold();
        } else if (!this.folded && !this.isFocus) {
            this.doFold();
        }
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

  doFold: function() {
    this.isFocus = false;
    if (!this.folded && !this.locked) {
        this.locked = true;
        this.folded = true;
        this.changeStyle();
        var it = this;
        Widget.TransitionFactory.createDisappearEffect(this.fdElement,this, {
          afterFinish: function(effect) {
            it.afterEffect();
          }
        });
        this.unfoldedSpan.style.display="none";
        this.foldedSpan.style.display="inline";
    }
  },

  doUnfold: function() {
    this.isFocus = true;
    if (this.folded && !this.locked){
        this.locked = true;
        this.folded = false;
        //if using ajax response
        this.loadAjax();
        this.changeStyle();
        this.foldedSpan.style.display="none";
        this.unfoldedSpan.style.display="inline";
        this.fdElement.parentNode.style.overflow = 'hidden';
        var it = this;
        Widget.TransitionFactory.createAppearEffect(this.fdElement,this, {
          afterFinish: function(effect) {
            it.afterEffect();
		  }
	    });
    }
  },

  /**
   * Folds the widget using the effect configured by the appropriate style.
   * Does nothing if folded or if folding effect is already running.
   *
   * @volantis-api-include-in PublicAPI
   */
  fold: function() {
    this.doFold();
  },

  /**
   * Unfolds the widget using the effect configured by the appropriate style.
   * Does nothing if unfolded or if unfolding effect is already running.
   *
   * @volantis-api-include-in PublicAPI
   */
  unfold: function() {
    this.doUnfold();
  }

}); //end of Widget.FoldingItem

/* instaed of + - signs we can use arrows with unicode values
    setSigns:function(){
        // s1 = '(\u2193');
        // s2= ('\u2191');
    },
*/