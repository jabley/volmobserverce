/**
 * (c) Volantis Systems Ltd 2006. 
 */ 
/**
 * Some refactoring done as part of VBM concerning image quality improvement
 * VBM : 2007030119
 * 
 *  New design of Google Maps widget implementation is described in design description
 *  for mentioned VBM. 
 */
/**
 * Contans some static data. 
 */
Widget.GConst = {
  MAP_MODE : "map",
  SAT_MODE: "photo",
  
  MAX_ZOOM: 17,
  MIN_ZOOM: 0,
  
  MAP_SIZE : 5,
  
  MAP_MOVE_DURATION : 5000 /** 5s for map move */
}

/**
 * contains operations codes and hardcoded indexes for images on map.
 */
Widget.GReqCode = {
    // codes of move operation - where only one AJAX request is performed
    // for additional images that are initially invisible
  GO_LEFT : 1,
  GO_RIGHT: 2,
  GO_UP: 3,
  GO_DOWN: 4,

    // codes of refresh operations - where two AJAX requests are performed
    // first for visible images and second for hidden images
  ZOOM_OUT: 5,
  ZOOM_IN: 6,
  TO_SATELLITE:7,
  TO_MAPS: 8,
    
    // type of request when two ajax request are needed - initial for visible part
    // bg for hidden part 
  INITIAL_REQUEST: 9,
  BG_REQUEST: 10,
    
    // code of search operation 
  SEARCH : 11,
    
    // request for whole map when caching is disabled
  ALL_MAP : 12,
  
    // lists of images for default size = 5
    // when it is calculated dynamically any size of map could be supported
  FULL_MAP_AREA: [1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,21,22,23],
  CETRAL_MAP_AREA: [6,7,8,11,12,13,16,17,18],
  BG_MAP_AREA : [1,2,3,5,9,10,14,15,19,21,22,23]
}

/**
 * Responsible for sent/receive AJAX request 
 */
Widget.GAJAXHandler = Class.define({
    
  _onSuccess : null,
  _onFailure : null,
    
  initialize : function(serviceLocation){
    this.serviceLocation = serviceLocation;
  },
   
    /**
     * Send AJAX Request
     * @param {Object} opCode
     * @param {Object} search
     * @param {Object} x
     * @param {Object} y
     * @param {Object} t
     * @param {Object} zoom
     * @param {Object} d
     * @param {Object} m
     */
    sendRequest : function(opCode,search,x,y,t,zoom,d,m,offx,offy) {
      var requestURL = this._prepareURL(opCode,search,x,y,t,zoom,d,m,offx,offy);
      Widget.log("GAJAX", "ajax request = "+requestURL);
      new Widget.AjaxRequest(
        requestURL,
        { method: 'get',
          parameters: '',
          onSuccess: this._onSuccess.bind(this),
          onFailure: this._onFailure.bind(this) 
        });
    },
    
    /**
     * prepare URL from parameters
     * @param {Object} opCode
     * @param {Object} search
     * @param {Object} x
     * @param {Object} y
     * @param {Object} t
     * @param {Object} zoom
     * @param {Object} d
     * @param {Object} m
     */
    _prepareURL : function(opCode,search,x,y,t,zoom,d,m,offx,offy){
      var requestURL = this.serviceLocation;
      requestURL += "/"+opCode+"?";
      if(null != search)requestURL += 'q='+search+'&';
    if(null != d)requestURL += 'd='+d+'&';
    if(null != x)requestURL += 'x='+x+'&';
    if(null != y)requestURL += 'y='+y+'&';
    if(null != zoom)requestURL += 'z='+zoom+'&';
    if(null != t)requestURL += 't='+t+'&';
    if(null != m)requestURL += 'm='+m+'&';
    if(null != offx)requestURL += 'offx='+offx+'&';
    if(null != offy)requestURL += 'offy='+offy+'&';
      // remove last & character      
    return requestURL.substring(0,requestURL.length-1);
    },
    
    setCallbackOnSuccess : function(func){
      this._onSuccess = func;
    },
    
    setCallbackOnFailure : function(func){
      this._onFailure = func;
    }
});

/**
 * Stores images in array in identical order as images are displayed therefore
 * GImageArray is responsible for move (up/down/left/right) operation and passing right image placement
 * to presentation layer.
 * 
 * Map is divided into two areas - central that contains images that are always visible
 * and border that contains images used for map scrolling, invisible except scrolling effect.
 * 
 * Map division is propagated through all Google Maps Widget it means different areas
 * are treated differently both in logic (GoogleMap) and presentation (GMapView) components. 
 */
Widget.GImageArray = Class.define({
    
  /* internal values and not connected with global operations codes */
  _LEFT : 1,
  _RIGHT: 2,
  _UP: 3,
  _DOWN: 4,
  
  initialize : function(size){
    /** temporary hardcoded into size of 5 - but could be calculated */
    this._CENTRAL_AREA = Widget.GReqCode.CETRAL_MAP_AREA;
    this._BORDER_AREA = Widget.GReqCode.BG_MAP_AREA;
    this._AFTER_LEFT = [1,5,10,15,21];
    this._AFTER_RIGHT = [3,9,14,19,23];
    this._AFTER_UP = [1,2,3,5,9];
    this._AFTER_DOWN = [15,19,21,22,23];
    this._CENTRAL_IMAGE = 12;

      
    this._mapSize = size;  
    this._map = new Array(size*size);
    for(var i = 0;i< this._map.length;i++){
      this._map[i] = { src : null, struct : null };    
    }        
  },

  /**
   * clone item 
   * @param {Object} imageItem
   */
  _cloneItem: function(imageItem){
    if(imageItem != null && imageItem.src != null && imageItem.struct != null){
      var newItem = { src : null, struct : {}};
    newItem.src = imageItem.src;
    newItem.struct.x = imageItem.struct.x;  
    newItem.struct.y = imageItem.struct.y;
    newItem.struct.t = imageItem.struct.t;
    newItem.struct.offx = imageItem.struct.offx;
    newItem.struct.offy = imageItem.struct.offy;
    return newItem;
  }
  return undefined;
  },  

  /**
   * move map left/right
   * @param {Object} dir
   */
  _shiftHorizontal : function(dir){
    var start = 0;
    var stop = this._mapSize-1;
    var inc = 1;
    if(dir == this._LEFT){
      start = this._mapSize-1;
      stop = 0;
      inc = -1;
    }
    var current = start;
    while(current != stop){
    for(i = 0;i<this._mapSize;i++){
        this._map[this._mapSize*i+current]= 
        this._cloneItem(this._map[this._mapSize*i+current+inc])
      }
      current +=inc;
    }
  },
    
  /**
   * move map up/down
   * @param {Object} dir
   */
  _shiftVertical : function(dir){
    var start = 0;
    var stop = this._mapSize-1;
    var inc = 1;
    if(dir == this._UP){
      start =this._mapSize-1;
      stop = 0;
      inc = -1;
    }
    var current = start;
    while(current != stop){
      for(i = 0;i<this._mapSize;i++){
        this._map[this._mapSize*current+i] = 
            this._cloneItem(this._map[this._mapSize*(current+inc)+i])
      }
      current +=inc;
    }        
  },
    
    moveUp : function(){
      this._shiftVertical(this._UP);  
    },
    
    moveDown : function(){
      this._shiftVertical(this._DOWN);
    },
    
    moveLeft : function(){
      this._shiftHorizontal(this._LEFT);          
    },
    
    moveRight : function(){
      this._shiftHorizontal(this._RIGHT);                  
    },
    
    /** 
     * Central image must be treated exceptionally because its coordinations are 
     * needed for map operation like move/zoom.
     */        
    getCentralImage: function(){
      return this._map[this._CENTRAL_IMAGE];
    },

    /** 
     * Central image must be treated exceptionally because its coordinations are 
     * needed for map operation like move/zoom.
     */        
    setCentralImage: function(imageId){
      this._map[this._CENTRAL_IMAGE] = this._map[imageId];
    },

    
    /**
     * get items list that occupies central area (without borders)
     */
    getCentralArea : function(){
      var centralItemsList = [];
      for(var i = 0;i<this._CENTRAL_AREA.length;i++){
        centralItemsList[i] = this._map[this._CENTRAL_AREA[i]];     
      }  
      return centralItemsList;
    },

    /**
     * set central area of map (without borders)
     * @param {Object} imagesList
     */    
    setCentralArea : function(imagesList){
      Widget.log("GImageArray", "central area set");
      if(imagesList.length !== this._CENTRAL_AREA.length){
        alert('central lists has different sizes');
      }  
      for(var i = 0;i<this._CENTRAL_AREA.length;i++){
        this._map[this._CENTRAL_AREA[i]] = imagesList[i];
      }  
    },
    
    /**
     * get list of items that occupies border area
     */
    getBorderArea : function(){
      var borderItemsList = [];
      for(var i = 0;i<this._BORDER_AREA.length;i++){
        borderItemsList[i] = this._map[this._BORDER_AREA[i]];     
      }  
      return borderItemsList;        
    },
    
    /**
     * set items that occupies border area
     * @param {Object} imagesList
     */
    setBorderArea : function(imagesList){
      Widget.log("GImageArray", "border area set");
      if(imagesList.length !== this._BORDER_AREA.length){
        alert('border lists has different sizes');
      }  
      for(var i = 0;i<this._BORDER_AREA.length;i++){
        this._map[this._BORDER_AREA[i]] = imagesList[i];     
      }
    },

    /**
     * update items after left move
     * @param {Object} imagesList
     */
    updateAfterLeft : function(imagesList){
      if(imagesList.length !== this._AFTER_LEFT.length){
        alert('update after left lists has different sizes');
      }  
      for(var i = 0;i<this._AFTER_LEFT.length;i++){
        this._map[this._AFTER_LEFT[i]] = imagesList[i];     
      }                  
    },

    /**
     * update items after rigth move
     * @param {Object} imagesList
     */
    updateAfterRight : function(imagesList){
      if(imagesList.length !== this._AFTER_RIGHT.length){
        alert('update after right lists has different sizes');
      }  
      for(var i = 0;i<this._AFTER_RIGHT.length;i++){
        this._map[this._AFTER_RIGHT[i]] = imagesList[i];     
      }                          
    },
    
    /**
     * update items after up move
     * @param {Object} imagesList
     */
    updateAfterUp : function(imagesList){
      if(imagesList.length !== this._AFTER_UP.length){
        alert('update after up lists has different sizes');
      }  
      for(var i = 0;i<this._AFTER_UP.length;i++){
        this._map[this._AFTER_UP[i]] = imagesList[i];     
      }                          
    },
    
    /**
     * update items after down move
     * @param {Object} imagesList
     */
    updateAfterDown : function(imagesList){
      if(imagesList.length !== this._AFTER_DOWN.length){
        alert('update after down lists has different sizes');
      }  
      for(var i = 0;i<this._AFTER_DOWN.length;i++){
        ;;;Widget.log("GImageArray", "image: "+imagesList[i]);
        this._map[this._AFTER_DOWN[i]] = imagesList[i];     
      }                          
    },
    
  // debut method
  // @todo - to be removed    
  _showMap : function(){
    for(var i = 0;i<this._map.length;i++){
    if(this._map[i] != undefined && this._map[i].struct != undefined){
      Widget.log("GImageArray", "index = "+i+" x = "+this._map[i].struct.x+", y = "+this._map[i].struct.y)
    }  
  }
  }
    
});

/**
 * Class responsible for displaying map on browser. This class doesn't contain any
 * logic. It servs only for display purposes. Calculations and communication is done
 * in Widget.GoogleMap. 
 * This class holds img area and is responsible for refresh view when all new images are available.
 * 
 * Main area means set of images that are fixed on page (some of them are visible - central part of image)
 * and some of them hidden (border part of image)
 * 
 * Movalbe area means set of images used to scroll map and generally all images are visible
 * but part of them is overflown by hidden value. 
 */
Widget.GMapView = Class.define(Widget.CallbackMixin,{
  IMG_LOADING : 0,
  IMG_LOADED : 1,
  IMG_ERROR : 2,
  IMG_EMPTY : 3,
  
  _isMoveSupported : false,

  _isCacheOn : true,

  initialize: function() {  
    this.imgList = [];
    this.movableImgList = [];
        
    // list of visible images indices in array
    this._visibleIndices = Widget.GReqCode.CETRAL_MAP_AREA;
        
    // list of hidden images indices in array
    this._hiddenIndices =  Widget.GReqCode.BG_MAP_AREA;    
    
    this._imgState = new Array(Widget.GConst.MAP_SIZE*Widget.GConst.MAP_SIZE);
    for(var i = 0;i<this._imgState.length;i++){
      this._imgState[i] = this.IMG_LOADED;
    }
    
    this.lastOperation = 0;
    this.moveInProgress = false;
    
    this.callbackHandler = null;
 },  
 
    addCallback : function(funct){
      this.callbackHandler = funct;  
    },
    
    registerVisibleImgs : function(idList){
      for(var i = 0;i<idList.length;i++){
        this.imgList[this._visibleIndices[i]] = this._registerImg(idList[i],this._visibleIndices[i]);
        this.imgList[this._visibleIndices[i]].firstChild.onload = this._visibleImgOnLoad.bind(this,this._visibleIndices[i]);
        this.imgList[this._visibleIndices[i]].firstChild.onerror = this._imgOnError.bind(this,this._visibleIndices[i]);
        this.imgList[this._visibleIndices[i]].onclick = this._imgOnClick.bind(this,this._visibleIndices[i]);
      }
    },

    registerHiddenImgs : function(idList){
    for(var i = 0;i<idList.length;i++){
    this.imgList[this._hiddenIndices[i]] = this._registerImg(idList[i],this._hiddenIndices[i]);
        this.imgList[this._hiddenIndices[i]].firstChild.onload = this._hiddenImgOnLoad.bind(this,this._hiddenIndices[i]);
        this.imgList[this._hiddenIndices[i]].firstChild.onerror = this._imgOnError.bind(this,this._hiddenIndices[i]);
      }        
    },

    registerMovableImgs : function(idList){
    for(var i = 0;i<idList.length;i++){
    this.movableImgList[Widget.GReqCode.FULL_MAP_AREA[i]] = this._registerImg(idList[i],Widget.GReqCode.FULL_MAP_AREA[i]);
      }        
    },
    
  /**
   * set some style properties on img object and return it;
   * @param {Object} id - id of table that holds images area.
   */ 
  _registerImg: function(id,index){
    var table = $(id);
    table.setStyle({ padding : '0px'});
    var a = $(table.firstChild);
    var img = $(a.firstChild)
    img.setStyle({ display : 'block'});
    return a;
  },
  
  /**
   * Set support for move effect. If move effect is supported movable area will be used
   * to show map move effect, otherwise there won't be any move effect. Next view with 
   * new display will be showed just after image loading starts. 
   * @param {Object} isMoveEffectSupported
   */
  setSupportsMoveEffect : function(isMoveEffectSupported){
    this._isMoveSupported = isMoveEffectSupported;
  },
  
  /**
   * Set link to image that will be displayed when map/satellite image is not retrieved.
   * @param {Object} errorImgSrc
   */
  setErrorImg : function(errorImgSrc){
    this.errorImage = errorImgSrc;
  },

  setCacheOn : function (cacheOn){
    this._isCacheOn = cacheOn;
  },

  /**
   * Scroll efect doesn't apply when:
   * - there is no support vor move/scroll
   * - there is no support for caching - then there is no additional images to scroll. 
   */
  _isMoveEffectSupported : function(){
    return this._isCacheOn && this._isMoveSupported;
  },
  
  /**
   * Set id for div that contains main map area with displayable images.
   * Beacause of MCS style optimization border spacing is set there to remove
   * any spaces from table cells.  
   * @param {Object} element
   */
  setMainAreaElement : function(element){
    this.mainAreaElement = $(element)
    var tableElement = $(this.mainAreaElement.firstChild)
    tableElement.setStyle({ borderSpacing : '0px' })
    tableElement.cellSpacing = "0"
    tableElement.cellPadding = "0"
    this.mainAreaElement.setStyle({ position: 'absolute' })
    this.mainAreaElementParent = $(this.mainAreaElement.parentNode)
  },
  
  /**
   * Set id for div that contains movable area. Movable area is used to present
   * move effect when user use pan operation.
   * @param {Object} element
   */
  setMovableAreaElement : function (element){
    this.movableAreaElement = $(element);
    this.movableInnerElement = $(this.movableAreaElement.firstChild);
    var tableElement = $(this.movableInnerElement.firstChild);
    tableElement.setStyle({ borderSpacing : '0px' });
  },
  
  /**
   * 
   * @param {Object} img
   */
  setImgSize : function(img){
    this.img = img;
    this.originalMovableTop = -1*img.height;
    this.originalMovableLeft = -1*img.width;
  },
    
   /**
    * update movable images after refresh
    */
   _updateMovableImgSrc : function(){
     Widget.log("GMapView","_updateMovableImgSrc")     
     for(var i = 0;i<this.imgList.length;i++){
       if(this.imgList[i] != undefined){
         this.movableImgList[i].firstChild.src = this.imgList[i].firstChild.src;
         
       }
     }
   },

  /**
   * switch to movable area before area will be moved to emulate scrolling. 
   */
  switchToMovable : function(){
    this.mainAreaElement.setStyle({ display : 'none'});
    this.movableAreaElement.setStyle({ display : 'block'});
  },
  
  
  /**
   * switch to main are after scrolling completed.  
   */
  switchToMain : function(){
    this.movableAreaElement.setStyle({display : 'none', position : 'relative'});
    $(this.movableInnerElement.firstChild).cellSpacing  = "0";
    
    this.movableInnerElement.setStyle({top : this.originalMovableTop+'px', left: this.originalMovableLeft+'px'});
    this.mainAreaElement.setStyle({display : 'block'});
    $(this.mainAreaElementParent.parentNode).setStyle({display : 'block'})
  },
      
  /**
   * Public method
   * @param {Object} srcList
   */
  refreshVisibleImgs : function(srcList, operation){
    Widget.log("GMapView","reresh visible images: "+srcList.toString());
    this.lastOperation = operation;
    if(this._isMoveEffectSupported()){
      // if supported then start scroll
      // otherwise only src will be replaced
      
      this._startUpdateView();
    }
    for(var i = 0;i< srcList.length;i++){
      this._imgState[this._visibleIndices[i]] = this.IMG_LOADING;
      Widget.log("GMapView","new src: "+srcList[i].src);
      this.imgList[this._visibleIndices[i]].firstChild.src = srcList[i].src;
    }
  },
  
  /**
   * Public 
   * @param {Object} srcList
   */
  refreshHiddenImgs : function(srcList){
    Widget.log("GMapView","refresh hidden images");
    for(var i = 0;i< srcList.length;i++){
      this._imgState[this._hiddenIndices[i]] = this.IMG_LOADING;
      this.imgList[this._hiddenIndices[i]].firstChild.src = srcList[i].src;
    }
  },
  
  /**
   * move movable part of map by calculated value. 
   */
  _moveArea : function (){
    var curMilis = (new Date()).getTime()
    if(curMilis - this.startMilis < Widget.GConst.MAP_MOVE_DURATION) {
      var progress = (curMilis - this.startMilis)/Widget.GConst.MAP_MOVE_DURATION;
      var ctop = this.originalMovableTop-progress*this.finalPosition.top;
      var cLeft = this.originalMovableLeft-progress*this.finalPosition.left;
      this.movableInnerElement.setStyle({top : ctop+'px', left: cLeft+'px'})
    } else {
      this._stopMoveArea();
    }
  },
  
  /**
   * init area scrolling and calculate where it should stop
   */
  _startMoveArea : function(){
    this.startMilis = (new Date()).getTime();
    this.finalPosition = this._calculateFinalPosition(this.lastOperation);
    this.moveInProgress = true;
    if(!this.timer){
      this.timer = setInterval(this._moveArea.bind(this), 40);
    }
  },
  
  /**
   * stop area scrolling
   */
  _stopMoveArea : function(){
    if(this.timer){
      clearInterval(this.timer);
      this.timer = undefined;
    }
    this.movableInnerElement.setStyle({top:  this.originalMovableTop-this.finalPosition.top+"px",left : this.originalMovableLeft-this.finalPosition.left+"px"});
    this.moveInProgress = false;
    if(this.isImgDownloadFinished(this._visibleIndices)){
      this._finishUpdateView();
    }      
  },
  
  /**
   * calculate position where area should be stopped. 
   * @param {Object} operation
   */
  _calculateFinalPosition : function(operation){
    var finalPosition = { top: 0, left: 0};
    switch(operation){
      case Widget.GReqCode.GO_LEFT:
        finalPosition.left = -1*this.img.width; 
      break;
      case Widget.GReqCode.GO_RIGHT:
        finalPosition.left = this.img.width; 
      break
      case Widget.GReqCode.GO_UP:
        finalPosition.top = -1*this.img.height;
      break
      case Widget.GReqCode.GO_DOWN:
        finalPosition.top = this.img.height;
      break;  
      default:
      break;   
    }
    return finalPosition;
  },
  
  /**
   * Private
   * Start move area if feature supported. If not nothing happends. 
   */
  _startUpdateView : function(){
    this.switchToMovable();
    this._startMoveArea();
  },
  
  /**
   * Private 
   * Replace back movable area with main area. Main area should be completed
   */
  _finishUpdateView : function(){
    this.switchToMain();
    this._updateMovableImgSrc();
  },
  
  /**
   * @param {Object} indices
   */
  isImgDownloadFinished : function(indices){
    var result = false;
    Widget.log(this._imgState.toString());
    for(i = 0;i<indices.length;i++){
      if(this._imgState[indices[i]] != this.IMG_LOADED){
        return result;
      }
    }
    result = true;
    return result;
  },
  
  _visibleImgOnLoad : function(poolIndex){
    this._imgState[poolIndex] = this.IMG_LOADED;
  if(!this.moveInProgress && this.isImgDownloadFinished(this._visibleIndices)){
    this._finishUpdateView();
  }
  },

  _hiddenImgOnLoad : function(poolIndex){
    Widget.log("GMapView","hidden OnLoad: img number = "+poolIndex)
    this._imgState[poolIndex] = this.IMG_LOADED;
  },

  _imgOnError : function(number){
    if(this.imgList[number].src != this.errorImage){
      this.imgList[number].src = this.errorImage;
    }
    // else any image can not be loaded
  },
  
  /**
   * call callback function
   * @param {Object} number
   */
  _imgOnClick : function(number){
    this.callbackHandler.apply(this, [number]);  
  }
      
});


/*------------------ Widget.GoogleMap ----------------------------*/
/**
 * Main class responsible for all Google Map widget activities. 
 */
Widget.GoogleMap = Widget.define(Widget.Appearable, Widget.Disappearable, Widget.ActionOwner, Widget.PropertyOwner, Widget.InputContainer,Widget.CallbackMixin , {
  /** default value for service name can be set by server-side */
  serviceName: "/services/map",
  /** devalut value for service lcoation - must be set by server-side because for sure server-side dependend */
  pageBase: "/mcs",

  _cacheOn : true,
  
  errorImageSrc : "",
  imageSize : "",
  
  icsMapHostPrefix : "",
  icsMapHostSuffix : "",
  icsSatHostPrefix : "",
  icsSatHostSuffix : "",
  
  longitude: null,
  latitude: null,
  zoom: null,

  _icsPrefix : "v",

  _MAX_OFFSET : 3,
  
  mode : Widget.GConst.MAP_MODE,

   
  initialize: function(id, options, initialLists) {
    this.initializeWidget(id, options);

    this.id = id;
    this.element = $(id);
  
    this.currZoom = this.zoom;
    this.currMode = this.mode;
    this.currOpCode = 0;
    this.state = Widget.GReqCode.NO_REQUEST;
    this._centralImgId = -1;
  
    this._imgArray = new Widget.GImageArray(Widget.GConst.MAP_SIZE);
    
    this._AJAXHandler = new Widget.GAJAXHandler(this.pageBase+this.serviceName);
    this._AJAXHandler.setCallbackOnSuccess(this._onSuccess.bind(this));
    this._AJAXHandler.setCallbackOnFailure(this._onFailure.bind(this));
  
    this._initializeGMapView(initialLists);
    this._addActionsAndProperties();
    
    this._startWithInitialLocation(options);

    this._disableMovableActions();
  },
  
  _startWithInitialLocation : function(options){
    if (options.query != null && options.query != "") {
      this.setQuery(options.query);
      this.searchInitial();
    } else if (options.initialLocation != null) {
      this.currOpCode = Widget.GReqCode.INITIAL_REQUEST;
      this.state = Widget.GReqCode.INITIAL_REQUEST;
      this._handleResponse(options.initialLocation);
    }      
  },

  _addActionsAndProperties : function(){
    this.actionsList = ['pan-right','pan-left','pan-up','pan-down','zoom-in','zoom-out',
      'search','set-map-style-map','set-map-style-photo'];

    this.movableActionsList = ['pan-right','pan-left','pan-up','pan-down','zoom-in','zoom-out',
      'set-map-style-map','set-map-style-photo'];

    for(var i = 0;i<this.actionsList.length;i++){
      this.addAction(this.actionsList[i]);    
    }; 
    this.addProperty('query');
    this.addProperty('longitude');
    this.addProperty('latitude');
    this.addProperty('zoom');
    this.addEvent('changed');
    
  },
  
  /**
   * Initialize gMapView object
   * and register all handled images for main area and hidden area - used only for
   * move effect 
   * @param {Object} listStruct
   */
  _initializeGMapView : function(listStruct){
    this.gMapView = new Widget.GMapView();
    this.gMapView.setImgSize({ width: this.imageSize, height: this.imageSize});
    this.gMapView.setErrorImg(this.errorImageSrc);
    this.gMapView.setSupportsMoveEffect(Prototype.operaMobile() !== true);
    this.gMapView.setCacheOn(this._cacheOn);
    this.gMapView.addCallback(this._zoomOnClickCallback.bind(this));

    this.gMapView.setMainAreaElement(listStruct.mainAreaDivId);
    this.gMapView.setMovableAreaElement(listStruct.movableAreaDivId); 
    this.gMapView.registerVisibleImgs(listStruct.mainIdList);
    this.gMapView.registerHiddenImgs(listStruct.bgIdList);
    this.gMapView.registerMovableImgs(listStruct.movableIdList);
  },
  
  _zoomOnClickCallback: function(number) {
    this._imgArray.setCentralImage(number);
    this.zoomIn();    
  },
  

  _calculateImageTileCoordinates : function(offsetX, offsetY){
    // default value is 3
    var result = {};
    result.left = offsetX/this._MAX_OFFSET;
    result.top = offsetY/this._MAX_OFFSET;
    result.bottom = (offsetY+1)/this._MAX_OFFSET;
    result.right = (offsetX+1)/this._MAX_OFFSET;
    return result;
  },

  /**
   * Calculate image name for given parameters and ICS hosts parameters.
   * @param {Object} src
   */
  _getImgSrc : function(src){
    var imgSrc;
    if(src['t'] != undefined){
      // image is in photo mode
      
      var offsets = this._calculateImageTileCoordinates(src.offx,src.offy);
      Widget.log(offsets);
      // 
      imgSrc = this.icsSatHostPrefix+"&t="+src.t+"&"+this.icsSatHostSuffix;
      imgSrc = imgSrc+ "&"+this._icsPrefix+".top.y="+offsets.top;
      imgSrc = imgSrc+ "&"+this._icsPrefix+".left.x="+offsets.left;
      imgSrc = imgSrc+ "&"+this._icsPrefix+".bottom.y="+offsets.bottom;
      imgSrc = imgSrc+ "&"+this._icsPrefix+".right.x="+offsets.right;
    } else {
      // image in map mode
      var imgX = Math.floor(src.x/this._MAX_OFFSET);
      var imgY = Math.floor(src.y/this._MAX_OFFSET);
      var offsetX = src.x % this._MAX_OFFSET;
      var offsetY = src.y % this._MAX_OFFSET;
      var offsets = this._calculateImageTileCoordinates(offsetX,offsetY);

      imgSrc = this.icsMapHostPrefix+"&x="+imgX+"&y="+imgY+"&zoom="+this.currZoom+"&"+this.icsMapHostSuffix;
      imgSrc = imgSrc+"&"+this._icsPrefix+".top.y="+offsets.top;
      imgSrc = imgSrc+"&"+this._icsPrefix+".left.x="+offsets.left;
      imgSrc = imgSrc+"&"+this._icsPrefix+".bottom.y="+offsets.bottom;
      imgSrc = imgSrc+"&"+this._icsPrefix+".right.x="+offsets.right;
    }
    return imgSrc;
  },

  /**
   * when ajax response fails
   * @param {Object} request
   */
  _onFailure: function(request){
  alert('Response from service failed.');
    // free lock when response failed
  this._enableActions();
  },

 
  /**
   * when ajax response success
   * @param {Object} request
   */
  _onSuccess: function(request){
    var jsonResponse = request.responseText;
  eval("responseStruct = "+jsonResponse);
  this._handleResponse(responseStruct);
  },


  /**
   * get value of query property
   */
  getQuery : function(){
    return this.query;
  },

  /**
   * set value of query property
   */
  setQuery : function(query){
    this.query = query;
  },    

  /**
   * get value of longitude property
   */
  getLongitude: function() {
    return this.longitude
  },
  
  /**
   * get value of latitude property
   */
  getLongitude: function() {
    return this.latitude
  },
  
  /**
   * get value of initial zoom property
   */
  getZoom: function() {
    return this.zoom
  },
  
  /**
   * Method responsible for moving image src into right place in _imgArray
   * It works only for move operations because for zoomIn and zoomOut, all
   * Images needs to be downloaded so old are out of dated.
   * 
   * @param {Object} opCode
   */
  _moveImgs : function(opCode){
    switch(opCode){
    case Widget.GReqCode.GO_DOWN:
      this._imgArray.moveDown();
    break;
    case Widget.GReqCode.GO_UP:
      this._imgArray.moveUp();
    break;
    case Widget.GReqCode.GO_LEFT:
      this._imgArray.moveLeft();
    break;
    case Widget.GReqCode.GO_RIGHT:
      this._imgArray.moveRight();
    break;
  }
    this.gMapView.refreshVisibleImgs(this._imgArray.getCentralArea(),opCode);
  },

  /**
   * Update Image list in imageMap when response is received.
   * It depends on operation which part of image map is filled with new images
   * For operations like zoomIn, zoomOut all images are replaced with new ones. 
   * 
   * @param {Object} requestStruct
   */    
  _updateImgArray : function(requestStruct){
    Widget.log("opCode: "+this.currOpCode);
    // operation code: up/down/left/right/center/bg
    var opCode = this.currOpCode;
    var receivedImageList = [];
    for(var i = 0;i<requestStruct.imgList.length;i++){  
      receivedImageList[i] = { src : null, struct : {}};  
    receivedImageList[i].src =  this._getImgSrc(requestStruct.imgList[i]);
    receivedImageList[i].struct =  requestStruct.imgList[i];
  }  
    ;;;Widget.log("GoogleMap","image list created");
    // put images list into array
    switch(opCode){
    case Widget.GReqCode.GO_DOWN:
      this._imgArray.updateAfterDown(receivedImageList);
    break;
    case Widget.GReqCode.GO_UP:
      this._imgArray.updateAfterUp(receivedImageList);
    break;
    case Widget.GReqCode.GO_LEFT:
      this._imgArray.updateAfterLeft(receivedImageList);
    break;
    case Widget.GReqCode.GO_RIGHT:
      this._imgArray.updateAfterRight(receivedImageList);
        break;
    case Widget.GReqCode.INITIAL_REQUEST:
    case Widget.GReqCode.SEARCH:
      this._imgArray.setCentralArea(receivedImageList);
        break;
    case Widget.GReqCode.BG_REQUEST:
      this._imgArray.setBorderArea(receivedImageList);
        break;
    } 
  },
  
  /**
   * Handle Ajax response - send next if necessary.
   * 
   * @param {Object} responseStruct
   */
  _handleResponse: function(responseStruct){
    if (this.currMode === Widget.GConst.SAT_MODE) {
      this.currZoom = Widget.GConst.MAX_ZOOM - responseStruct.zoom;
    } else {
      this.currZoom = responseStruct.zoom;
    }
    
    this._updateImgArray(responseStruct);
    if(this.state == Widget.GReqCode.INITIAL_REQUEST) {  
      // send request for background images with exactly the same parameters
      this.gMapView.refreshVisibleImgs(this._imgArray.getCentralArea(),this.currOpCode);
      if(this._cacheOn){
        this.state = Widget.GReqCode.BG_REQUEST;
        this.updateView(Widget.GReqCode.BG_REQUEST);
      } else {
        this._enableActions();
      }
    } else {
      Widget.log("GoogleMap", "try to refresh hidden image list");
      this.gMapView.refreshHiddenImgs(this._imgArray.getBorderArea());
      this._enableActions();
    }
    
    this.notifyObservers('changed',this.currZoom, this.getCenterImgCoord());
    
  },
  
  
  getCenterImgCoord: function() {    
    var centralPoint = this._imgArray.getCentralImage();
    var x = centralPoint.struct.x;
    var y = centralPoint.struct.y;
    var t = centralPoint.struct.t;
    var offx = centralPoint.struct.offx;
    var offy = centralPoint.struct.offy;
    
    if (x == null) {
      //photo
      var p = this.fromGeoStringToGoogleCoord(t, offx, offy)
      x = p.x
      y = p.y
    }
    return {x: x, y: y};
      
  },
  
    /**
   * 'trtqtrsrgrr' -> {x: 571, y: 336 }
   */
  fromGeoStringToGoogleCoord: function(str, offx, offy) {
    var xx = 0,yy = 0;
    var number;
    var withoutInitial = str.substr(1);
    for(var i = 0; i < withoutInitial.length ; i++){
      number = this.numberExtractor(withoutInitial.charAt(i));
      xx = Math.floor((xx << 1) + number / 2);
      yy = (yy << 1) + (number % 2);
    }
    return {x: xx*3 + offx, y: yy*3 + offy}
  },

  numberExtractor: function(chr){
    if(chr == 'q') return 0;
    if(chr == 't') return 1;
    if(chr == 'r') return 2;
    return 3; 
  },
  

  /**
   * Perform operation that is requested by user - this code is not responsible 
   * for performing initial request - with search geographical coordinates request.
   * 
   * opCode, x, y,t, zoom,d,m
   * 
   * @param {Object} opCode
   */
  _performOperation : function(op){
    var opName = "";
    var func;
    var d=null, m = null;
    var centralPoint = this._imgArray.getCentralImage();
    var x = centralPoint.struct.x;
    var y = centralPoint.struct.y;
    var t = centralPoint.struct.t;
    var offx = centralPoint.struct.offx;
    var offy = centralPoint.struct.offy;
    var zoom = this.currZoom;
    switch (op){
      case Widget.GReqCode.GO_DOWN:
  d = "b";
        opName = "pan";
    break;
    case Widget.GReqCode.GO_LEFT:
      d = "l";
            opName = "pan";
    break;
    case Widget.GReqCode.GO_UP:
      d = "u";
            opName = "pan";
    break;
    case Widget.GReqCode.GO_RIGHT:
      d = "r";
            opName = "pan";
    break;  
    case Widget.GReqCode.ZOOM_IN:
      d = "i";
            opName = "zoom";
    break;  
    case Widget.GReqCode.ZOOM_OUT:
      d = "o"
            opName = "zoom";
    break;  
    case Widget.GReqCode.TO_MAPS:
            opName = "switchMode";
      m = "map"
    break;  
    case Widget.GReqCode.TO_SATELLITE:
            opName = "switchMode";
      m = "photo"
    break;  
    case Widget.GReqCode.BG_REQUEST:
            opName = "bg";              
    break;      
    default:
      alert("FATAL ERROR: unexpected operation: "+opCode);
    break;    
  }
    this._AJAXHandler.sendRequest(opName,null,x,y,t,zoom,d,m,offx,offy);
  },  
     
  /**
   * Triggered by calling any operation. First check if it is possible to show new view immediately
   * If not AJAX request must be doe first to get necessary image links ( for example when user
   * is changing view from satellite to maps). Otherwise view should be updated now and silent
   * AJAX request should be send to be prepared for next user operation.
   * 
   * @param {Object} opCode
   */
  updateView : function(opCode){
    // first we have to check operation - for some operation widget mode 
  // must be changed
    this._disableActions();
    if(this._cacheOn){
    switch (opCode){
      case Widget.GReqCode.TO_MAPS:
      case Widget.GReqCode.TO_SATELLITE:
      case Widget.GReqCode.ZOOM_IN:
      case Widget.GReqCode.ZOOM_OUT:
          this.currOpCode = Widget.GReqCode.INITIAL_REQUEST;
        this.state = Widget.GReqCode.INITIAL_REQUEST;
        break;
        
      case Widget.GReqCode.GO_DOWN:   
      case Widget.GReqCode.GO_UP:   
      case Widget.GReqCode.GO_LEFT:   
      case Widget.GReqCode.GO_RIGHT:
        this.currOpCode = opCode;
        this._moveImgs(opCode);
        this.state = Widget.GReqCode.BG_REQUEST;    
      break   
      case Widget.GReqCode.INITIAL_REQUEST:    
        alert("updateView: operation has code of initial request - it shouldn\'t happend")
      default:
        // we can refresh img list from background images
        // and background request should be send
        this.currOpCode = opCode;
        this.state = Widget.GReqCode.BG_REQUEST;
      break;
    } 
    this._performOperation(opCode);
  } else {
    // we have to make request to get new images links to display
    this._performOperation(opCode);
    this.state = Widget.GReqCode.INITIAL_REQUEST;
  }
  },

  /**
   * Operation methods
   */
  panLeft: function(){
    this.updateView(Widget.GReqCode.GO_LEFT);
  },

  panRight: function(){
    this.updateView(Widget.GReqCode.GO_RIGHT);
  },
    
  panUp: function(){
    this.updateView(Widget.GReqCode.GO_UP);
  },
  
  panDown: function(){
    this.updateView(Widget.GReqCode.GO_DOWN);
  },
  
  setMapStylePhoto: function(){
  if(this.currMode == Widget.GConst.MAP_MODE){
    this.currMode = Widget.GConst.SAT_MODE;
    this.updateView(Widget.GReqCode.TO_SATELLITE);
  }
  },
  
  setMapStyleMap: function(){
  if(this.currMode == Widget.GConst.SAT_MODE){
    this.currMode = Widget.GConst.MAP_MODE;
    this.updateView(Widget.GReqCode.TO_MAPS);
  }    
  },
  
  zoomIn : function(){
    if(this.currZoom > Widget.GConst.MIN_ZOOM){
      this.updateView(Widget.GReqCode.ZOOM_IN);
    }
  },
  
  zoomOut : function(){
    if(this.currZoom <  Widget.GConst.MAX_ZOOM){
      this.updateView(Widget.GReqCode.ZOOM_OUT);
    }
  },
  
  /**
   * Perform search request - send location name only. 
   */
  search : function(){
    this.searchOperation(null);
  },
  
  /**
   * Perform search initial request - send location name only. 
   */
  searchInitial : function(){
    this.searchOperation(this.currZoom);
  },
  
  searchOperation : function(zoom) {
    var search = this.getQuery();
    if(search != ""){            
      this.currOpCode = Widget.GReqCode.SEARCH;
      this.state = Widget.GReqCode.INITIAL_REQUEST;
        this._AJAXHandler.sendRequest("search",search,null,null,null,zoom,null,this.currMode);
      this._disableActions();
    } else {
      alert('You must provide query to input field');
    }
  },
  
  /**
   * disable actions and notify buttons that actions are disabled
   */
  _disableActions : function(){
    this._disableArrayActions(this.actionsList);
  },


  /**
   * disable actions (without search action) and notify buttons that actions are disabled
   */
  _disableMovableActions : function(){
    this._disableArrayActions(this.movableActionsList);
  },
  
  _disableArrayActions : function(actions){
    for(var i = 0;i< actions.length;i++){
      this[("can-"+actions[i]).camelize()] = function(){
        return false;         
      }
      this.notifyObservers(("can-"+actions[i]+"-changed").camelize());
    }    
  },
  
  /**
   * enable actions and notify buttons that actions are now enabled
   */  
  _enableActions : function(){
    for(var i = 0;i< this.actionsList.length;i++){
      this[("can-"+this.actionsList[i]).camelize()] = function(){
        return true;  
      }  
      this.notifyObservers(("can-"+this.actionsList[i]+"-changed").camelize());
    }
  }
})

/*------------------ Widget.MapLocationMarkers ----------------------------*/
/**
 * Class responsible for managing map locator markers. 
 */
Widget.MapLocationMarkers = Widget.define(Widget.Appearable, Widget.Disappearable, Widget.ActionOwner, Widget.PropertyOwner, {
  markers: null,
  mapId: null, 

  initialize: function(id, options) {
    this.initializeWidget(id, options)
    this.displayedMarkersList = new Array()
    
    this.markersCreated = false;
    
    this.mapElement = $W(this.mapId)
    
    this.areaElement = this.mapElement.gMapView.mainAreaElement
    this.width = this.mapElement.gMapView.img.width
    this.zoom = this.mapElement.currZoom
    this.centerImgCoord = {struct: {x: -1, y: -1, t: null}, src: null}
    //NOTE: this.mapElement.getCenterImgCoord() here sometimes returns struct=null
    
    this.setMarkerManager()
 
    this.addAction('show')
    this.addAction('hide')
    this.addProperty('current')
    this.observe(this.mapElement, 'changed', 'show')
    this.updateDisplayedMarksList();
    
  },
  
  setMarkerManager: function() {
    for (var i = 0 ; i < this.markers.length ; i++) {
      $W(this.markers[i]).setMarkerManager(this.id);
    }
  },

  /**
   * Shows all markers.
   * @volantis-api-include-in PublicAPI
   *
   * @param element (this.gMapView.mainAreaElement) element contains table with displayed map images
   * @param zoom value of map zoom
   * @param map (this.imageMap)   
   */
  show: function(zoom, centerImgCoord) {
    if (centerImgCoord == null) {
      this.zoom = this.mapElement.currZoom
      this.centerImgCoord = this.mapElement.getCenterImgCoord()
      this.updateDisplayedMarksList();
    } else if (this.zoom != zoom 
            || this.centerImgCoord.x != centerImgCoord.x 
            || this.centerImgCoord.y != centerImgCoord.y) {
        this.zoom = zoom;
        this.centerImgCoord.x = centerImgCoord.x
        this.centerImgCoord.y = centerImgCoord.y
        this.updateDisplayedMarksList();        
    } 
    this.showMarks()
  },
  
  /**
   * Hides all the markers.
   * @volantis-api-include-in PublicAPI
   */
  hide: function() {
    for (var i = 0 ; i < this.displayedMarkersList.length ; i++) {
      $W(this.displayedMarkersList[i]).hide();
    }
  },

  updateDisplayedMarksList: function() {

    var div = Math.pow(2, this.zoom)

    this.hideMarks()

    //range (x, y) of the visible images  
    var minX = this.centerImgCoord.x - 1
    var minY = this.centerImgCoord.y - 1
    var maxX = minX + 2
    var maxY = minY + 2  

    //google coords of the marker in the current scale  
    var xf, yf;

    //intvalue of the above
    var xi, yi;

    //coord of the marker in px (left top corner of the map has coord 0px, 0px)
    var tx, ty;

    for (var i = 0 ; i < this.markers.length ; i++) {
      var mark = $W(this.markers[i])
      if (mark.minZoom <= this.zoom && this.zoom <= mark.maxZoom) {

        xf = mark.lng / div;
        yf = mark.lat / div;

        xi = Math.floor(xf);
        yi = Math.floor(yf);

        tx = Math.round( (xf - xi)*this.width );
        ty = Math.round( (yf - yi)*this.width );

        if (xi >= minX && xi <= maxX && yi >= minY && yi <= maxY) {
          //marker translation relatively to the top-left corner of the top-left
          //visible square
          tx += (xi - minX) * this.width ;
          ty += (yi - minY) * this.width;

          mark.setLocation(this.areaElement, new Widget.MarkerPosition(tx, ty));
          this.displayedMarkersList.push(this.markers[i]);
        }
      }
    }
  },
   
  showMarks: function() {
    var d = '';
    for (var i = 0 ; i < this.displayedMarkersList.length ; i++) {
      $W(this.displayedMarkersList[i]).show(this.areaElement);
      
      var mark = $W(this.displayedMarkersList[i])
      d += "(" + mark.position.left + ", " + mark.position.top + "), ";
    }
    Widget.log("marks", d);  
  
  },
  
  hideMarks: function() {
    for (var i = 0 ; i < this.displayedMarkersList.length ; i++) {
      $W(this.displayedMarkersList[i]).hide();
    }
    this.displayedMarkersList = new Array();      
  },
  
  /**
   *  value of current property
   */
  getCurrent : function(){
    return this.current
  },
  
  
  /**
   * set value of current property
   * @param current Widget.MapLocationMarker
   */
  setCurrent : function(current){
    if (this.current != current) {
    //do nothing if current is already selected
      if (this.current != null) {
        this.current.setSelected(false)
        this.current.notifyObservers('deselected', this.current)
      } 
      this.current = current
      if (this.current != null) {
        this.current.setSelected(true)
        this.current.notifyObservers('selected', this)
        Widget.log("Marks", "current: " + this.current.id)
      }
    }
  }
})

/*------------------ Widget.MapLocationMarker ----------------------------*/
/**
 * Class responsible for single markers activities
 */

Widget.MapLocationMarker = Widget.define(Widget.Appearable, Widget.Disappearable, Widget.ActionOwner, Widget.PropertyOwner, {
  lng: null,
  lat: null,
  src: null,
  minZoom: null,
  maxZoom: null,
  markers: null, //id of MapLocationMarkers widget
  
  initialize: function(id, options) {
    this.initializeWidget(id, options);
  
    this._nodeCreated = false
    this.element = null; //parentElement - where to put marker image
    
    this.nodeElem = null; //element with marker image
    this.position = new Widget.MarkerPosition(-1, -1)
    this._isSelected = false
  
    this.addAction("select")
    this.addAction("deselect")
    this.addEvent("selected")
    this.addEvent("deselected")
  },
  
  setMarkerManager: function(markerManager) {
    this.markerManagerId = markerManager;
  },
  
  /**
   * @volantis-api-include-in PublicAPI
   */  
  select: function() {
    if (!this._isSelected) {
      this._isSelected = true;
      var a = $W(this.markerManagerId);
      a.setCurrent(this);
    }
  },
  
  /**
   * @volantis-api-include-in PublicAPI
   */  
  deselect: function() {
    if (this._isSelected) {
      this._isSelected = false;
      $W(this.markerManagerId).setCurrent(null);
      this.notifyObservers('deselected', this.id);        
    }
  },

  setSelected: function(selected) {
    this._isSelected = selected;
  },

  show: function(parentElement) {
    this.createNode(parentElement)
    if (this._nodeCreated) {
      this.nodeElem.setStyle({visibility: 'visible'})
    }
  },
  
  

  hide: function() {
    if (this._nodeCreated) {
      this.nodeElem.setStyle({visibility: 'hidden'})
    }
  },

  
  createNode: function(parentElement) {
    if (!this._nodeCreated && parentElement != null) {
      var newElem=document.createElement("div");
      var ie = $(newElem)
      ie.setStyle({left: this.position.left + 'px'});
      ie.setStyle({top: this.position.top+'px'});
      ie.setStyle({position: 'absolute'});
      newElem.innerHTML = '<a href="javascript:void(0)"><img src="' + this.src + '" style="z-index: 3" border="0"/></a>';
      newElem.onclick = this.clickMarker.bind(this)
      parentElement.appendChild(newElem);
      this.nodeElem = newElem;
      this._nodeCreated = true;
      return newElem;
    } else {
      return this.nodeElem;
    }
  },
  
  clickMarker: function() {
    if (this._isSelected) {
      this.deselect();
    } else {
      this.select();
    }
  }, 
  
  setLocation: function(parentElement, position) {
    this.position = position;
    this.createNode(parentElement);
    this.nodeElem.setStyle({left: this.position.left + 'px'});
    this.nodeElem.setStyle({top: this.position.top+'px'});
  }
})

Widget.MarkerPosition = Class.define({
  initialize: function(left, top) {
    this.left = left
    this.top = top
  }
})
