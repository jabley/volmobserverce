/**
 * (c) Volantis Systems Ltd 2006.
 */
 
/*------------------ Widget.Swapable ----------------------------*/

//Swapable - mixin for carousel widget
//contains  Disappearable and Appearable mixins
Widget.Swapable = {
//end user parameters
    delay: 3,
    carWidth: 0,  //carousel width
    carHeight: 0, //carousel height
    maxNodeSize: 0,
    effectState: 'idle',   //allowed 'idle', 'finished', 'running'  
    storage: null, // !!!expacted object creation this.storage=document.createElement('ul') in widget constructor DOM structure of  carousel elements
    removeOldContent: false,

    /*
     * Moves item from storage to view.
     *
     * This method assumes, that storage is non empty.
     * It must be checked before invoking this method.
     *
     * @param storage all items
     * @param view ul element
     * @param next if true, gets node from beginning of the storage, elsewhere from the end
     */
    moveItemToView: function(storage,view, next) {
      if (next) {
        var newNode = storage.removeChild(storage.firstChild);
        this.setElementOppositeSize(newNode,this.maxNodeSize);
        view.appendChild(newNode);
        return newNode;
      } else {
        var newNode = storage.removeChild(storage.lastChild);
        this.setElementOppositeSize(newNode,this.maxNodeSize);
        view.insertBefore(newNode, view.firstChild);
        return newNode;
      }
    },

    clearContent: function(element) {
      while (element.firstChild) {
        element.removeChild(element.firstChild);
      }
    },

    addObservers: function(element) {
      Widget.addObserversToFocusableElements(element,Widget.BLUR,
         this.start.bindAsEventListener(this));
      //  now we return number of focusable elements
      // to check if fake focus should be added
      return Widget.addObserversToFocusableElements(element,Widget.FOCUS,
         this.stop.bindAsEventListener(this));
    },
    
    getOrigVisibleProps: function(element) {
      return {visibility: element.style.visibility, display: element.style.display, position: element.style.position};
    },

    getSize:function() {
      return this.maxCarouselHeight;
    },

    getElementOppositeSize: function(element){
      var dimension = Element.getDimensions(element);
      return dimension.width;
    },
    
    setElementOppositeSize: function(element,size){
      Element.setStyle(element,{'width' : size + 'px'});          
    },

    getElementMainSize: function(element){
      var dimension = Element.getDimensions(element);
      return dimension.height;
    },

    
    setElementMainSize: function(element,size){
      Element.setStyle(element,{'height' : size});
    },
    
    setCarouselStyle: function(element){
      Element.setStyle(element, {'height' : 'auto', 'width' : this.carWidth});
    },

    fillSwapBlockContent: function(view,reverse) {
      var ulSize = this.getElementMainSize(view);
      var overflow = false;

      var prevDis = Element.getStyle(this.effectElement,'display');
      // must be display to block otherwise element size are wrongly calculated 
      // after disappear effect and element <UL> is empty
      Element.setStyle(this.effectElement, {'visibility' : 'hidden', 'display' : 'block'});
      // DEBUG TEST
      //this.effectElement.style['visibility'] = 'hidden';
      //this.effectElement.style['display'] = 'block';

      while(!overflow)
      {
    
        
        if(this.storage.hasChildNodes()){  
          // Add next node to be displayed in the carousel.
          var addedNode = this.moveItemToView(this.storage,view, reverse);

          ulSize = this.getElementMainSize(view);
          // If the node overflows the carousel,
          // move it back to the storage. The exception
          // is if it's the first added node: it 
          // will be displayed even if it's larger than the 
          // carousel.
          if(ulSize > this.getSize()){
            overflow = true;
            if(reverse){
              this.putAsFirstToStorage(view,this.storage,addedNode)
            } else {
              this.putAsLastToStorage(view,this.storage,addedNode);  
            }
          }
        } else {
          overflow = true;  
        }
      }
      if(!view.hasChildNodes()){
        // View is still empty it means that first element was too big.
        // So move one item from storage (if it's not empty)
        if (this.storage.hasChildNodes()) {
          this.moveItemToView(this.storage,view,reverse);
        }
      }
      Element.setStyle(this.effectElement,{'visibility': 'inherit',  'display' : prevDis});

    },
    
    putAsFirstToStorage: function(view,storage,element){
      var removedElement = view.removeChild(element);
      storage.insertBefore(removedElement,storage.firstChild);  
    },
    
    putAsLastToStorage: function(view,storage,element){
      var removedElement = view.removeChild(element);
      storage.appendChild(removedElement);  
    },

    moveItemsToStorage: function(src, dest, show) {
      while(src.firstChild){
        if(show){
          // calculating size before removing element from container
          // this let us to store original size 
          // on Opera Mobile node size tends to collapse
          // so we have to force max height or width (depend on direction)  
          size = this.getElementOppositeSize(src.firstChild);
          if(size > this.maxNodeSize){
            this.maxNodeSize = size;  
          }
	  Element.setStyle(src.firstChild,{'display' : 'block'});

        }
        var newElement =  src.removeChild(src.firstChild);
        dest.appendChild(newElement);
      }
    }, 

    getRealItemElement: function(el) {
      return el.firstChild.firstChild;
    },

    clearWhitespaces: function(el) {
      Element.cleanWhitespace(el);
    }
    
};//Swapable

Object.extend(Widget.Swapable, Widget.Appearable);
Object.extend(Widget.Swapable, Widget.Disappearable);


/*------------------ Widget.CarouselSwap ----------------------------*/
Widget.CarouselSwap = Class.create();

Object.extend(Widget.CarouselSwap.prototype,{
   swapable: {},

  initialize: function(swapable) {
    this.swapable = swapable;
    
    // set to UL element
    this.realElement = swapable.realElement;

    //internal vars
    this.swapStopped = false;
    this.swapable.effectState='idle';

    this.timeoutID=setTimeout(this.doSwap.bind(this, true), 1000* this.swapable.delay);
  },
  
  /*
   * @param nextPage next or previous page
   */
  doSwap: function(nextPage) {
    var sw = this.swapable;

    var visible = sw.element.isVisible()
    if(!visible){
      this.timeoutID=setTimeout(this.doSwap.bind(this, nextPage), 1000* sw.delay);
      sw.effectState='idle';        
      return;   
    }       
    
    if(this.swapStopped) return;


    if(sw.effectState=='idle')
    {
      this.doDisapearApear(nextPage);
    }

    
    if(sw.effectState=='finished')
    {
      this.timeoutID=setTimeout(this.doSwap.bind(this, nextPage), 1000* sw.delay);
      sw.effectState='idle';
    }
  },

  stop: function(event) {
    this.swapStopped = true;
    if(this.timeoutID!='undefined')
      clearTimeout(this.timeoutID); 
    this.timeoutID='undefined';
  },

  start: function(event) {
    if(!this.swapStopped)
      return;
    this.swapStopped = false;
    this.timeoutID=setTimeout(this.doSwap.bind(this, true),  1000); 
  },

/*
 * @param finalState: 'idle', ('running'), 'finished'
 * @param nextPage if true displays next subset of current set of items, otherwise displays previous subset
 */
  changePageInternal: function(finalState, nextPage) {
    //make change only if state is eq to 'idle'
    if (this.swapable.effectState != 'idle') {
      return;
    }

      this.swapable.effectState='running';
      Widget.TransitionFactory.createDisappearEffect(this.swapable.effectElement,this.swapable, 
        {  swapable: this.swapable, 
           effectElement : this.swapable.effectElement,
          afterFinish: function(effect) {
            // because user can switch into different tab during disappear effect
            // it is necessary to check once again if carousel is visible
            // if it is not visible content shoudlnd be reloaded  
            if(this.swapable.element.isVisible()){ 
              // only if carousel is visible content will be replaced 
              var liNodes = this.swapable.realElement.childNodes.length
              if(this.swapable.removeOldContent){  
                this.swapable.clearContent(this.swapable.realElement);
                this.swapable.removeOldContent = false;
              } else {
                this.swapable.moveItemsToStorage(this.swapable.realElement,
                  this.swapable.storage,false);
              }
              this.swapable.fillSwapBlockContent(this.swapable.realElement,nextPage); // put new storage into UL
            }
            Widget.TransitionFactory.createAppearEffect(this.effectElement,this.swapable,
              { swapable: this.swapable,
                effectElement : this.swapable.effectElement,
                afterFinish: function(effect) { 
		  // fake focus element will be add to main carousel div - not to the inner div
                  this.swapable.unsetFocus(this.swapable.element,this.swapable.stop.bindAsEventListener(this.swapable),
                    this.swapable.start.bindAsEventListener(this.swapable));  
                  this.swapable.setFocus(this.swapable.element,this.swapable.stop.bindAsEventListener(this.swapable),
                    this.swapable.start.bindAsEventListener(this.swapable));
                  this.swapable.effectState= finalState;
                }
              });              
          }
        });
  },

  /*
   * @param nextPage if true then displays next subset of current set of items, otherwise displays previous subset
   */
  doDisapearApear: function(nextPage) {
    switch(this.swapable.effectState)
    {
      case 'idle':
        this.changePageInternal('finished', nextPage);
        setTimeout(this.doDisapearApear.bind(this, nextPage),  20);
        break;
      case 'running':
        setTimeout(this.doDisapearApear.bind(this, nextPage),  20);
        break;
      case 'finished':
        setTimeout(this.doSwap.bind(this,nextPage),  20);
        break;
      }
  }  //doDisapearApear

} //End Widget.CarouselSwap
);


/*------------------ Widget.Carousel ----------------------------*/

// Widget implementation
Widget.Carousel = Class.define(Widget.Swapable,Widget.Refreshable,Widget.Focusable ,{
  height_properties: ['padding-top','padding-bottom','border-bottom-width','border-top-width'],
  width_properties: ['padding-right','padding-left','border-right-width','border-left-width'], 
  initialize: function(id, options) {
    this.id = id;
    this.element = $(id);

    this.clearWhitespaces(this.element); /* @todo -remove ?? */

    Object.copyFields(this, options || {});
    this.excludeEffects();     

    this.startRefresh();
    this.setup();
    this.doSwap();
  },
  
  /**
   * change unsupported effect into none (instant)
   */
  excludeEffects : function(){
    // only for Opera Mobile so far
    if(Prototype.operaMobile()){  
      // change unsupported effects into instant    
      var excludedEffectsList = new Array();
      excludedEffectsList['puff'] = 'none';
      if(excludedEffectsList[this.disappearEffect] != undefined){
        this.disappearEffect =  excludedEffectsList[this.disappearEffect]; 
      }
      if(excludedEffectsList[this.appearEffect] != undefined){
        this.appearEffect =  excludedEffectsList[this.appearEffect]; 
      }
    }  
  },
  
  /**
   * TODO - There are problems with height here
   */

  getInitialCarouselDimensions : function(){
    var dims = Element.getDimensions(this.element);     
    this.maxCarouselWidth = dims.width - 
              this.getPaddingAndBorder(this.element,
                  this.width_properties)
    this.maxCarouselHeight = dims.height -
        this.getPaddingAndBorder(this.element,
            this.height_properties)      

    if(this.maxCarouselHeight == 0){
      // height was not specified for carousel
      // can be modified
      this.initialSize = false  
    } else {
      this.initialSize = true; 
    }
  },
  
  setup: function() {
    this.effectElement = this.element.firstChild; 

    this.realElement = this.getRealItemElement(this.element);
    this.clearWhitespaces(this.realElement);
    this.storage = this.realElement.cloneNode(false); // shallow clone of UL element

    this.moveItemsToStorage(this.realElement, this.storage,true);

    this.getInitialCarouselDimensions();

    if(this.initialSize){
      this.prepareBlockSwapLayout(true);
    } else {
      while(this.storage.hasChildNodes()){
        this.moveItemToView(this.storage,this.realElement,true);  
      }  
      this.fixCarouselDimensions();
    }
  },
  
  doSwap: function() { 
    this.carouselEffect = new Widget.CarouselSwap(this);
  },

  /**
   * Makes visible next subset of current set of items
   *
   * @volantis-api-include-in PublicAPI
   */   
  nextPage: function() {
    if (this.carouselEffect.swapable.effectState == 'running') {
      return;
    }
    
    if(this.carouselEffect.timeoutID!='undefined') {
      clearTimeout(this.carouselEffect.timeoutID);
    }
    
    this.carouselEffect.changePageInternal('finished', true)

    //there is no need to call it earlier...
    setTimeout(this.startTimeout.bind(this), 1000* this.carouselEffect.swapable.delay);  
  },

  /**
   * Makes visible previous subset of current set of items
   *
   * @volantis-api-include-in PublicAPI
   */   
  previousPage: function() {
    if (this.carouselEffect.swapable.effectState == 'running') {
      return;
    }
    
    var swapable = this.carouselEffect.swapable
    if(this.carouselEffect.timeoutID!='undefined') {
      clearTimeout(this.carouselEffect.timeoutID);
    }
    
    this.carouselEffect.changePageInternal('finished', false)

    //there is no need to call it earlier...
    setTimeout(this.startTimeout.bind(this), 1000* this.carouselEffect.swapable.delay);
  },

  /* Starts automatically refreshing content after calling next or previous page methods */
  startTimeout: function() {
    switch(this.carouselEffect.swapable.effectState)
    {
      case 'running':
        setTimeout(this.startTimeout.bind(this),  20);
        break;
      case 'finished':
        this.carouselEffect.swapable.effectState = 'idle'
        this.carouselEffect.timeoutID = setTimeout(this.carouselEffect.doSwap.bind(this.carouselEffect, true),  1000* this.carouselEffect.swapable.delay);
        break;
    }
  },

  /*
   * @param pageNext true: nextPage, false:previousPage
   */
  prepareBlockSwapLayout: function(pageNext) {
    this.fillSwapBlockContent(this.realElement,pageNext);
    var counter = this.addObservers(this.element)
    // fakek focus will be applied to main carousel div, not to inner div
    this.setFocus(this.element,this.stop.bindAsEventListener(this),
           this.start.bindAsEventListener(this));

  },

  getPaddingAndBorder: function(element,propertiesArray){
    var result = 0;
    var i = 0;
    for(i = 0;i<propertiesArray.length;i++){
     if (!Prototype.hiddenAttr(propertiesArray[i])) {
       var v = parseInt(Element.getStyle(this.element, propertiesArray[i]));
       if(v > 0){
         result+=v;
       }
     }
    }  
    return result;
  },

  /**
   * To hold carousel size during transitions we need to fix carouselSize
   * as a container for <LI> markup, and also fix external div (parentNode)
   * to hold dimensions when internal div disappears as result of transition
   */
  fixCarouselDimensions: function(){
    if(!this.initialSize){
      var carouselDimension = Element.getDimensions(this.element);
      carouselDimension.height += 
           this.getPaddingAndBorder(this.element,this.height_properties);
      this.maxCarouselHeight = carouselDimension.height;
      this.setElementMainSize(this.element,carouselDimension.height + 'px');        
    }
  },

  processAJAXResponse: function (originalRequest) {
    this.setContentInternal(originalRequest.responseText);
    //update the content if carousel is not visible
    if (!this.element.isVisible()){
      this.realElement.innerHTML=originalRequest.responseText;
      // fit the content in carousel with the same method which is used for initial
      // content provided in xdime
      this.setup();
    }
  },
   
  /**
   * Replaces whole carousel content with new from htmlText parameter
   */   
  setContentInternal: function(htmlText) {
    var tmpStorage = document.createElement('div');
    tmpStorage.innerHTML = htmlText;
    this.clearWhitespaces(tmpStorage);

    this.setContentElementsInternal(tmpStorage.childNodes)
  },
  
  /**
   * Replaces whole carousel content with new from htmlText parameter
   */   
  setContentElementsInternal: function(elements) {
    var wasStopped = this.carouselEffect.swapStopped;
    this.stop();
    var tmpStorage = document.createElement('div');
    tmpStorage.style.visibility='hidden';
    for (var i = 0; i < elements.length; i++) {
      tmpStorage.appendChild(elements[i])
    }

    this.clearContent(this.storage);
    this.moveItemsToStorage(tmpStorage, this.storage,true);
    this.removeOldContent = true;

    // remove temporary created DIV
    document.body.appendChild(tmpStorage);
    document.body.removeChild(tmpStorage);

    if(!wasStopped)
    {
      this.start();
    }
  },
  
  /**
   * Replaces whole carousel content with new set of items.
   * 
   * @htmlItems The array containing HTML strings of each item
   *
   * @volantis-api-include-in PublicAPI
   */   
  setContent: function(htmlItems) {
    contentInternal = ""
    
    for (var i = 0; i < htmlItems.length; i++) {
      contentInternal += "<li>" + htmlItems[i] + "</li>"
    }
    
    this.setContentInternal(contentInternal)
  },
  
  /**
   * Replaces whole carousel content with new set of items immediately.
   * 
   * @htmlItems The array containing HTML strings of each item
   */   
  setContentNow: function(htmlItems) {
    // TODO: Don't know how to do it yet, so by now use setContent()
    this.setContent(htmlItems)
  },
  
  /**
   * Replaces whole carousel content with new set of items.
   * 
   * @elements The array of DOM inline elements
   *
   * @volantis-api-include-in PublicAPI
   */   
  setContentElements: function(elements) {
    var liElements = []
    
    for (var i = 0; i < elements.length; i++) {
     if (!Prototype.hiddenAttr(elements[i])) {
      var liElement = document.createElement("li")
      liElement.appendChild(elements[i])
      liElements.push(liElement)
     } 
    }
    
    this.setContentElementsInternal(liElements)
  },
  
  /**
   * Replaces whole carousel content with new set of elements.
   * 
   * @elements The array of inline DOM elements
   */   
  setContentElementsNow: function(elements) {
    var contentHTML = "";
    for (var i = 0; i < elements.length; i++) {
      var liElement = document.createElement("li")
      liElement.appendChild(elements[i])
      contentHTML+=liElement.innerHTML
    }
    this.realElement.innerHTML=contentHTML

    this.setContentElementsInternal(elements);
    this.effectElement = this.element.firstChild; 
    this.moveItemsToStorage(this.realElement, this.storage,true);

    this.getInitialCarouselDimensions();

    if(this.initialSize){
      this.prepareBlockSwapLayout(true);
    } else {
      while(this.storage.hasChildNodes()){
        this.moveItemToView(this.storage,this.realElement,true);  
      }  
      this.fixCarouselDimensions();
    }
  },
  
  /**
   * Stop widget transitions so any new slice of information will be dispalyed.
   *
   * @volantis-api-include-in PublicAPI
   */ 
  stop: function() {
    this.carouselEffect.stop();
  },

  /**
   * Resume widget transitions.
   *
   * @volantis-api-include-in PublicAPI
   */ 
  start: function() { 
    this.carouselEffect.start();
  }
}
);