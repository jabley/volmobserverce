
var Package={define:function(content){if(content==null){return{};}else{return content;}}};Object.copyFields=function(destination,source){for(var property in source){if(destination[property]!==undefined&&typeof(destination[property])!='function'){destination[property]=source[property];}}
return destination;};Element.addMethods({vfcReplaceStyle:function(element,style){element=$(element);element.vfcRevertStyle();element.vfcReplacedStyle={};for(var name in style){cname=name.camelize();element.vfcReplacedStyle[name]=element.style[cname];element.style[cname]=style[name];}},vfcRevertStyle:function(element){element=$(element);style=element.vfcReplacedStyle;if(style){for(var name in style){cname=name.camelize();element.style[cname]=style[name];}
element.replacedStyle=null;}},vfcGetDimensions:function(element){element=$(element);if(Element.getStyle(element,'display')!='none')
return{width:element.offsetWidth,height:element.offsetHeight};var els=element.style;var originalVisibility=els.visibility;var originalPosition=els.position;els.visibility='hidden';els.position='absolute';els.display='';var originalWidth=element.offsetWidth;var originalHeight=element.offsetHeight;els.display='none';els.position=originalPosition;els.visibility=originalVisibility;return{width:originalWidth,height:originalHeight};},vfcGetRealDimensions:function(element){var result={};var isInDom=true;element=$(element);if(Element.getStyle(element,'display')=='none'){isInDom=false;element.vfcReplaceStyle({visibility:'hidden',position:'absolute',display:'block'})}
result.width=element.offsetWidth;result.height=element.offsetHeight;var borderLeftWidthOld=0;var borderRightWidthOld=0;var borderTopWidthOld=0;var borderBottomWidthOld=0;var paddingLeftOld=0;var paddingRightOld=0;var paddingTopOld=0;var paddingBottomOld=0;if(Element.getStyle(element,'border-left-width')!=undefined){borderLeftWidthOld=Element.getStyle(element,'border-left-width');Element.setStyle(element,{borderLeftWidth:'0px'});}
result.borderLeftWidth=result.width-element.offsetWidth;if(Element.getStyle(element,'border-right-width')!=undefined){borderRightWidthOld=Element.getStyle(element,'border-right-width');Element.setStyle(element,{borderRightWidth:'0px'});}
result.borderRightWidth=result.width-element.offsetWidth-result.borderLeftWidth;if(Element.getStyle(element,'border-top-width')!=undefined){borderTopWidthOld=Element.getStyle(element,'border-top-width');Element.setStyle(element,{borderTopWidth:'0px'});}
result.borderTopWidth=result.height-element.offsetHeight;if(Element.getStyle(element,'border-bottom-width')!=undefined){borderBottomWidthOld=Element.getStyle(element,'border-bottom-width');Element.setStyle(element,{borderBottomWidth:'0px'});}
result.borderBottomWidth=result.height-element.offsetHeight-result.borderTopWidth;if(Element.getStyle(element,'padding-left')!=undefined){paddingLeftOld=Element.getStyle(element,'padding-left');Element.setStyle(element,{paddingLeft:'0px'});}
result.paddingLeft=result.width-element.offsetWidth-result.borderLeftWidth-result.borderRightWidth;if(Element.getStyle(element,'padding-right')!=undefined){paddingRightOld=Element.getStyle(element,'padding-right');Element.setStyle(element,{paddingRight:'0px'});}
result.paddingRight=result.width-element.offsetWidth-result.borderLeftWidth-result.borderRightWidth-result.paddingLeft;if(Element.getStyle(element,'padding-top')!=undefined){paddingTopOld=Element.getStyle(element,'padding-top');Element.setStyle(element,{'padding-top':'0px'});}
result.paddingTop=result.height-element.offsetHeight-result.borderTopWidth-result.borderBottomWidth;if(Element.getStyle(element,'padding-bottom')!=undefined){paddingBottomOld=Element.getStyle(element,'padding-bottom');Element.setStyle(element,{paddingBottom:'0px'});}
result.paddingBottom=result.height-element.offsetHeight-result.borderTopWidth-result.borderBottomWidth-result.paddingTop;result.contentWidth=element.offsetWidth;result.contentHeight=element.offsetHeight;Element.setStyle(element,{borderLeftWidth:borderLeftWidthOld});Element.setStyle(element,{borderRightWidth:borderRightWidthOld});Element.setStyle(element,{borderTopWidth:borderTopWidthOld});Element.setStyle(element,{borderBottomWidth:borderBottomWidthOld});Element.setStyle(element,{paddingLeft:paddingLeftOld});Element.setStyle(element,{paddingRight:paddingRightOld});Element.setStyle(element,{paddingTop:paddingTopOld});Element.setStyle(element,{paddingBottom:paddingBottomOld});if(!isInDom){element.vfcRevertStyle();}
return result;},isVisible:function(element){var visible=true;if(Element.isParentVisible==undefined){Element.isParentVisible=function(element){if(document.defaultView&&document.defaultView.getComputedStyle){var css=document.defaultView.getComputedStyle(element,null);if((null==css)&&(Prototype.nokiaOSSBrowser())){return false;}
var display=css?css.getPropertyValue('display'):null;if(display=='none'){return false;}}else if(element.currentStyle['display']=='none'){return false;}
if(element.parentNode==null||element.parentNode.style==undefined){return true;}else{return Element.isParentVisible(element.parentNode);}}}
visible=Element.isParentVisible(element);if(visible){if(document.defaultView&&document.defaultView.getComputedStyle){var css=document.defaultView.getComputedStyle(element,null);var visibility=css?css.getPropertyValue('visibility'):null;if(visibility=='hidden'){visible=false;}}else{if(element.currentStyle['visibility']=='hidden'){visible=false;}}}
return visible;},getNotNullStyle:function(element,style){element=$(element);var value=element.style[style.camelize()];var isStyleChanged=false;if((Prototype.nokiaOSSBrowser()||Prototype.firefoxBrowser()||Prototype.konquerorBrowser()||Prototype.operaPC())&&element.style.display=='none'){element.style.visibility='hidden';element.style.display='block';isStyleChanged=true;}
if(!value){if(document.defaultView&&document.defaultView.getComputedStyle){var css=document.defaultView.getComputedStyle(element,null);if(/Opera\/9.00|Opera\/9.01|Opera\/9.02/.test(navigator.userAgent)){if(!value){var now=new Date();var exitTime=now.getTime()+300;while(true){value=css?css.getPropertyValue(style):null;now=new Date();if(value!='0px'||now.getTime()>exitTime){break;}}}}else{value=css?css.getPropertyValue(style):null;}}else if(element.currentStyle){value=element.currentStyle[style.camelize()];}}
if(window.opera&&['left','top','right','bottom'].include(style))
if(Element.getStyle(element,'position')=='static')value='auto';if(isStyleChanged){element.style.visibility='visible';element.style.display='none';}
return value;}});Object.extend(Position,{vfcClone:function(source,target){source=$(source);target=$(target);target.style.position='absolute';var offsets=this.cumulativeOffset(source);target.style.top=offsets[1]+source.offsetHeight+'px';target.style.left=offsets[0]+'px';if(!target.style.width){target.style.width=source.offsetWidth+'px';}},fixedOffset:function(element){var valueT=0,valueL=0;do{valueT+=element.offsetTop||0;valueL+=element.offsetLeft||0;element=element.offsetParent;if(element){p=Element.getStyle(element,'position');if(p=='relative'||p=='absolute'||p=='fixed')break;}}while(element);return[valueL,valueT];}});var Widget=Package.define({init:function(){if(!this.internalRegister&&!this.internalGetInstance){var registry={};this.internalRegister=function(id,widget){registry[id]=widget;widget._widgetId=id};this.internalGetInstance=function(id){return registry[id];};}},register:function(id,widget){this.init();this.internalRegister(id,widget);return widget},getInstance:function(id){this.init();return this.internalGetInstance(id);},createString:function(id){var element=$(id)
var nodes=element.childNodes;var string="";for(var i=0;i<nodes.length;i++)
string+=nodes[i].nodeValue
return string},isPollingEnabled:function(){return(this.internalPollingDisabled===undefined)||!this.internalPollingDisabled},setPollingEnabled:function(enabled){var old=this.internalPollingDisabled
this.internalPollingDisabled=!enabled
if(old!=this.internalPollingDisabled){if(this.pollingEnabledCallbackHandler!=null){this.pollingEnabledCallbackHandler.invoke(enabled)}}},addPollingEnabledCallback:function(callback){if(this.pollingEnabledCallbackHandler==null){this.pollingEnabledCallbackHandler=new Widget.CallbackHandler()}
this.pollingEnabledCallbackHandler.add(callback)},addStartupItem:function(func){if(!this.internalStartupItems){this.internalStartupItems=new Array()}
this.internalStartupItems.push(func)},startup:function(){if(this.internalStartupItems){for(var index=0;index<this.internalStartupItems.length;index++){this.internalStartupItems[index]()}}},addObserversToFocusableElements:function(element,event,func){var focusableElementsCounter=0;var focusableElementsList=['a','area','button','input','object','select','textarea','label'];var nodes;for(var i=0;i<focusableElementsList.length;i++){nodes=element.getElementsByTagName(focusableElementsList[i]);for(var j=0;j<nodes.length;j++){focusableElementsCounter++;Widget.addElementObserver(nodes[j],event,func);}}
return focusableElementsCounter;},addElementObserver:function(element,event,func){if(Prototype.useMouseAsSelect()){var new_event=event;if(Prototype.nokiaOSSBrowser()){if(event==Widget.BLUR){new_event=Widget.MOUSEOUT;}else if(event==Widget.FOCUS){new_event=Widget.MOUSEOVER;}}else if(Prototype.operaMobile()){if((element.nodeName.toLowerCase()=='input')&&(element.type.toLowerCase()=='text')){if(event==Widget.BLUR){new_event=Widget.MOUSEOUT;}else if(event==Widget.FOCUS){new_event=Widget.MOUSEOVER;}else if(event==Widget.CLICK){new_event=Widget.FOCUS;}}}
Event.observe(element,new_event,func);}else{Event.observe(element,event,func);}},observeFocusableElement:function(element,event,func){if(Prototype.netFront()){this.addObserversToFocusableElements(element,event,func);}else{Widget.addElementObserver(element,event,func);}},removeElementObserver:function(element,event,func){if(Prototype.useMouseAsSelect()){var new_event=event;if(Prototype.nokiaOSSBrowser()){if(event==Widget.BLUR){new_event=Widget.MOUSEOUT;}else if(event==Widget.FOCUS){new_event=Widget.MOUSEOVER;}}else if(Prototype.operaMobile()){if((element.nodeName.toLowerCase()=='input')&&(element.type.toLowerCase()=='text')){if(event==Widget.BLUR){new_event=Widget.MOUSEOUT;}else if(event==Widget.FOCUS){new_event=Widget.MOUSEOVER;}else if(event==Widget.CLICK){new_event=Widget.FOCUS;}}}
Event.stopObserving(element,new_event,func);}else{Event.stopObserving(element,event,func);}},removeObserversFromFocusableElements:function(element,event,func){var focusableElementsCounter=0;var focusableElementsList=['a','area','button','input','object','select','textarea','label'];var nodes;for(var i=0;i<focusableElementsList.length;i++){nodes=element.getElementsByTagName(focusableElementsList[i]);for(var j=0;j<nodes.length;j++){focusableElementsCounter++;Widget.removeElementObserver(nodes[j],event,func);}}
return focusableElementsCounter;},stopObservingFocusableElement:function(element,event,func){if(Prototype.netFront()){this.removeObserversFromFocusableElements(element,event,func);}else{Widget.removeElementObserver(element,event,func);}},stopEventPropagation:function(evt){if(Prototype.msieBrowser()){evt.cancelBubble=true;}else{evt.stopPropagation();}},makeFocusable:function(el){var imp;if(Prototype.netFront()){imp=document.createElement("input");imp.type="button";imp.style.width='1px';imp.style.height='1px';imp.style.backgroundColor='transparent';imp.style.border='0px';el.appendChild(imp);}else if(Prototype.firefoxBrowser()||Prototype.msieBrowser()){el.tabIndex="0"
imp=el;}
if(Prototype.operaMobile()){Widget.addElementObserver(el,Widget.CLICK,void(0));imp=el;}
return imp;},getPXDimension:function(otherUnitSize){var resultPX=0;this.mElement=document.createElement("div")
this.mElement.style.visibility="hidden";this.mElement.style.display="block";this.mElement.style.position="absolute";this.mElement.style.padding="0px";this.mElement.style.border="0px";this.mElement.style.margin="0px";if(otherUnitSize!==undefined){this.mElement.style.width=otherUnitSize;}else{this.mElement.style.width="0px";}
this.mElement.appendChild(document.createTextNode(""));document.body.appendChild(this.mElement);resultPX=this.mElement.scrollWidth;this.mElement.style.display="none";document.body.removeChild(this.mElement)
return resultPX;},log:function(){},isLogEnabled:function(){return false},supportsFixed:function(){return!(Prototype.msieBrowser()||Prototype.operaMobile());},FOCUS:'focus',BLUR:'blur',MOUSEOVER:'mouseover',MOUSEOUT:'mouseout',CLICK:'click',KEYDOWN:'keydown',KEYUP:'keyup',KEYPRESS:'keypress',CHANGE:'change'});Widget.Response=Package.define()
Widget.Internal=Package.define()
$W=function(id){return Widget.getInstance(id)}
$RW=function(id,widget){return Widget.register(id,widget)}
Class.ObjectMixin={__object:true,toString:function(){var s=""
for(var name in this){if(name!="__object"){var value=this[name]
if(typeof(value)=="string"||typeof(value)=="boolean"||typeof(value)=="number"){s+=name+"="+value+", "}}}
return s}}
Class.define=function(){var clazz=function(){if(this.initialize!=null){this.initialize.apply(this,arguments)}}
var extend=function(clazz,superClazz){if(!superClazz){}else if(typeof(superClazz)=="object"){Object.extend(clazz.prototype,superClazz)}else if(typeof(superClazz)=="function"){Object.extend(clazz.prototype,superClazz.prototype)}}
for(var i=0;i<arguments.length;i++){extend(clazz,arguments[i])}
extend(clazz,Class.ObjectMixin)
return clazz}
Widget.InputContainer=Class.define({inputFields:{},registerInputId:function(fieldName,fieldId){this.inputFields[fieldName]=fieldId},getInputId:function(fieldName){return this.inputFields[fieldName]},getAllInputs:function(){return this.inputFields}})
Widget.OptionsContainer=Class.define({options:null,installOptions:function(options){this.options=options
if(options){for(var option in options){if(!(this[option]===undefined)&&typeof(this[option])!='function'){this[option]=options[option];}}}},getOption:function(name,def){return this.options?(this.options[name]||def):def}})
Widget.Focusable=Class.create()
Widget.Focusable.ACCEPT='accept'
Widget.Focusable.IGNORE='ignore'
Object.extend(Widget.Focusable.prototype,{focusable:Widget.Focusable.ACCEPT,setFocus:function(element,funcFocus,funcBlur){var focusableElementsCount=Widget.addObserversToFocusableElements(element,Widget.FOCUS,funcFocus);if(this.focusable.toLowerCase()==Widget.Focusable.ACCEPT){if(Prototype.operaMobile()){this.setFocusOnOpera(element,funcFocus,funcBlur);}else if(Prototype.nokiaOSSBrowser()){this.setFocusOnNokia(element,funcFocus,funcBlur);}else if(Prototype.netFront()){this.setFocusOnNetFront(element,funcFocus,funcBlur);}else{this.createFocusableElement(element,funcFocus);}}
Widget.addObserversToFocusableElements(element,Widget.BLUR,funcBlur);},setFocusOnNokia:function(element,funcFocus,funcBlur){Widget.addElementObserver(element,Widget.FOCUS,funcFocus);Widget.addElementObserver(element,Widget.BLUR,funcBlur);},setFocusOnOpera:function(element,funcFocus,funcBlur){this.createFocusableElement(element,funcFocus);},setFocusOnNetFront:function(element,funcFocus,funcBlur){this.createFocusableElement(element,funcFocus);},unsetFocus:function(element,funcFocus,funcBlur){if(Prototype.operaMobile()){this.unsetFocusOnOpera(element,funcFocus,funcBlur);}else if(Prototype.nokiaOSSBrowser()){this.unsetFocusOnNokia(element,funcFocus,funcBlur);}else if(Prototype.netFront()){this.unsetFocusOnNetFront(element,funcFocus,funcBlur);}else{this.unsetFocusOnFocusableElement(element,funcFocus);}
Widget.removeObserversFromFocusableElements(element,Widget.FOCUS,funcFocus);Widget.removeObserversFromFocusableElements(element,Widget.BLUR,funcBlur);},unsetFocusOnNetFront:function(element,funcFocus,funcBlur){this.unsetFocusOnFocusableElement(element,funcFocus);},unsetFocusOnOpera:function(element,funcFocus,funcBlur){this.unsetFocusOnFocusableElement(element,funcFocus);},unsetFocusOnNokia:function(element,funcFocus,funcBlur){Widget.removeElementObserver(element,Widget.FOCUS,funcFocus);Widget.removeElementObserver(element,Widget.BLUR,funcBlur);},createFocusableElement:function(element,funcFocus){var fakeFocusID=element.getAttribute('id')+'_f';var fakeFocusDIV=document.createElement('div');fakeFocusDIV.setAttribute('id',fakeFocusID);Element.setStyle(fakeFocusDIV,{'white-space':'nowrap','width':'1px','height':'1px','background-color':'transparent','line-height':'1px','margin':'0px','padding':'0px','border':'0px'});element.appendChild(fakeFocusDIV);var fakeFocusElement=document.createElement("input");Element.setStyle(fakeFocusElement,{'width':'1px','height':'1px','overflow':'hidden','background-color':'transparent','border':'0px'});fakeFocusElement.type="button";fakeFocusDIV.appendChild(fakeFocusElement);if(typeof(funcFocus)=='function'){Widget.addElementObserver(fakeFocusElement,Widget.FOCUS,funcFocus);}
return fakeFocusElement;},unsetFocusOnFocusableElement:function(element,funcFocus,funcBlur){var fakeFocusID=element.getAttribute('id')+'_f';var fakeFocusElement=$(fakeFocusID);if(fakeFocusElement!=null){if(typeof(funcFocus)=='function'){Widget.removeElementObserver(fakeFocusElement,Widget.FOCUS,funcFocus);}
if(typeof(funcBlur)=='function'){Widget.removeElementObserver(fakeFocusElement,Widget.BLUR,funcBlur);}
element.removeChild(fakeFocusElement);}}})
Function.prototype.andThen=function(anotherFunction){var thisFunction=this;return function(){thisFunction.apply(this,arguments)
anotherFunction.apply(this,arguments)}};Widget.AjaxRequest=Class.define({initialize:function(url,options){new Ajax.Request(url,options)}})
Widget.AjaxUpdater=Class.define({initialize:function(container,url,options){new Ajax.Updater(container,url,options)}})
Widget.CallbackHandler=Class.define({add:function(callback){if(!this.callbacks){this.callbacks=callback
this.singleCallback=true}else{if(this.singleCallback){this.callbacks=new Array(this.callbacks,callback)
this.singleCallback=false}else{this.callbacks.push(callback)}}
return callback},remove:function(callback){if(this.callbacks){if(this.singleCallback){if(this.callbacks===callback){delete this.callbacks
return true}}else{for(var i=0;i<this.callbacks.length;i++){if(this.callbacks[i]===callback){this.callbacks.splice(i,1)
return true}}}}
return false},invoke:function(){if(this.callbacks){if(this.singleCallback){this.invokeCallback(this.callbacks,arguments)}else{for(var i=0;i<this.callbacks.length;i++){this.invokeCallback(this.callbacks[i],arguments)}}}},invokeCallback:function(callback,args){try{callback.apply(this,args)}catch(error){}}})
Widget.DelayCallbackHandler=Class.define({initialize:function(interval){this.baseCallbackHandler=new Widget.CallbackHandler()
this.interval=interval?interval:1000;},add:function(callback){this.baseCallbackHandler.add(callback)},remove:function(callback){this.baseCallbackHandler.remove(callback)},invoke:function(){if(this.timer==null){this.timer=setTimeout(this.invokeNow.bind(this),this.interval)}},invokeNow:function(){this.timer=null
this.baseCallbackHandler.invoke()}})
Widget.CallbackMixin=Class.define({addCallback:function(callback){this.callbackHandler.add(callback)},removeCallback:function(callback){this.callbackHandler.remove(callback)}})
Widget.Internal.ObserverHandler=Class.define({addObserver:function(notificationName,observer,handlerName){var observersArray=this._getObserversArray(notificationName,true)
var index=this._getObserverIndex(observersArray,observer,handlerName)
if(index==null){observersArray.push(observer)
observersArray.push(handlerName)
if(arguments.length<=3){observersArray.push(null)}else{observersArray.push($A(arguments).slice(3))}}},removeObserver:function(notificationName,observer,handlerName){var observersArray=this._getObserversArray(notificationName)
var index=this._getObserverIndex(observersArray,observer,handlerName)
if(index!=null){observersArray.splice(i,3)}},notifyObservers:function(notificationName){var observersArray=this._getObserversArray(notificationName)
if(observersArray!=null){this.notifyObserversArray(observersArray,arguments)}
var observersArray=this._getObserversArray(null)
if(observersArray!=null){this.notifyAllObserversArray(observersArray,arguments)}},notifyObserversArray:function(observersArray,args){for(var index=0;index<observersArray.length;index=index+3){var observer=observersArray[index]
var handlerName=observersArray[index+1]
var additionalArguments=observersArray[index+2]
if(additionalArguments==null){if(args.length==1){observer[handlerName]()}else{observer[handlerName].apply(observer,$A(args).slice(1))}}else{if(arguments.length==1){observer[handlerName].apply(observer,additionalArguments)}else{observer[handlerName].apply(observer,additionalArguments.concat($A(args).slice(1)))}}}},notifyAllObserversArray:function(observersArray,args){for(var index=0;index<observersArray.length;index=index+3){var observer=observersArray[index]
var handlerName=observersArray[index+1]
var additionalArguments=observersArray[index+2]
if(additionalArguments==null){observer[handlerName].apply(observer,args)}else{var argsArray=$A(args)
observer[handlerName].apply(observer,[argsArray[0]].concat(additionalArguments).concat(argsArray.slice(1)))}}},_getObserversArray:function(notificationName,create){notificationName=(notificationName==null)?"":notificationName
var observers=this.observers
if(observers==null&&create==true){observers={}
this.observers=observers}
if(observers!=null){var notificationObservers=observers[notificationName]
if(notificationObservers==null&&create){notificationObservers=[]
observers[notificationName]=notificationObservers}}
return notificationObservers},_getObserverIndex:function(observersArray,observer,handlerName){for(var index=0;index<observersArray;index=index+3){if(observersArray[index]===observer&&observersArray[index+1]==handlerName){return index}}}})
Widget.Observable=Class.define({_getObserverHandler:function(){if(this.observerHandler==null){this.observerHandler=new Widget.Internal.ObserverHandler()}
return this.observerHandler},notifyObservers:function(notificationName){var observerHandler=this._getObserverHandler()
observerHandler.notifyObservers.apply(observerHandler,arguments)}})
Widget.Observer=Class.define({observe:function(object,notificationName,handlerName){var handler=object._getObserverHandler()
if(handler!=null){if(arguments.length<=3){handler.addObserver(notificationName,this,handlerName)}else{handler.addObserver.apply(handler,[notificationName,this,handlerName].concat($A(arguments).slice(3)))}}},stopObserving:function(object,notificationName,handlerName){var handler=object._getObserverHandler()
if(handler!=null){handler.removeObserver(notificationName,this,handlerName)}}})
Widget.ActionOwner=Class.define({getAction:function(name){if(this.actions!=null){return this.actions[name]}},addAction:function(name,options){if(this.actions==null){this.actions={}}
this.actions[name]=new Widget.Action(name,this,options)}})
Widget.PropertyOwner=Class.define({getProperty:function(name){if(this.properties!=null){return this.properties[name]}},addProperty:function(name,options){if(this.properties==null){this.properties={}}
this.properties[name]=new Widget.Property(name,this,options)}})
Widget.EventOwner=Class.define({getEvent:function(name){if(name!=""){if(this.events!=null){return this.events[name]}}else{if(this.metaEvent==null){this.metaEvent=new Widget.Event("",this)}
return this.metaEvent}},addEvent:function(name,options){var event=new Widget.Event(name,this,options)
if(this.events==null){this.events={}}
this.events[name]=event
if(event.notificationName!=null){if(this.notification2EventNameMap==null){this.notification2EventNameMap={}}
this.notification2EventNameMap[event.notificationName]=name}},getEventNameForMetaEvent:function(notificationName){if(this.notification2EventNameMap!=null){return this.notification2EventNameMap[notificationName]}}})
Widget.MemberOwner=Class.define(Widget.ActionOwner,Widget.PropertyOwner,Widget.EventOwner,{getMember:function(name){var member=this.getAction(name)
if(member==null){member=this.getProperty(name)
if(member==null){member=this.getEvent(name)}}
return member}})
Widget.Internal.MemberTypeConverter=Class.define({validate:function(type,value){if(type=="boolean"){return(value=="yes")||(value=="no")}else if(type=="int"){return!isNaN(parseInt(value))}else if(type=="number"){return!isNaN(parseFloat(value))}else if(type=="string"){return true}else if(type=="widget"){return(Widget.getInstance(value)!=null)}else if(type=="list"){return true;}else{throw"Invalid type: "+type;}},convertForGet:function(type,value){if(type=="boolean"){return value?"yes":"no"}else if(type=="int"){return value.toString()}else if(type=="number"){return value.toString()}else if(type=="string"){return value}else if(type=="widget"){return(value._widgetId==null)?"":value._widgetId}else if(type=="list"){return value.toString()}else{throw"Invalid type: "+type;}},convertForSet:function(type,value){if(type=="boolean"){return(value=="yes")}else if(type=="int"){return parseInt(value)}else if(type=="number"){return parseFloat(value)}else if(type=="string"){return value}else if(type=="widget"){return Widget.getInstance(value)}else if(type=="list"){return value.split(',');}else{throw"Invalid type: "+type;}}})
Widget.Internal.getMemberTypeConverter=function(){if(this._memberTypeConverter==null){this._memberTypeConverter=new Widget.Internal.MemberTypeConverter()}
return this._memberTypeConverter}
Widget.Action=Class.define(Widget.OptionsContainer,Widget.Observable,Widget.Observer,Widget.MemberOwner,{methodName:null,canMethodName:null,canChangedNotificationName:null,initialize:function(name,owner,options){this.installOptions(options)
this.name=name
this.owner=owner
if(this.methodName==null){this.methodName=this.name.camelize()}
if(this.canMethodName==null){this.canMethodName=("can-"+this.name).camelize()}
if(this.canChangedNotificationName==null){this.canChangedNotificationName=("can-"+this.name+"-changed").camelize()}
this.observe(this.owner,this.canChangedNotificationName,"isEnabledChanged")
this.addProperty("is-enabled")
if(Widget.isLogEnabled()){Widget.log("Action","Created action: "+this.toString())}},invoke:function(){if(this.isEnabled()){this.owner[this.methodName].apply(this.owner,arguments)}},isEnabled:function(){var isEnabled=true
var canFunction=this.owner[this.canMethodName]
if(canFunction!=null){isEnabled=canFunction.apply(this.owner)}
return isEnabled},isEnabledChanged:function(){this.notifyObservers('isEnabledChanged')}})
Widget.Property=Class.define(Widget.OptionsContainer,Widget.Observable,Widget.Observer,Widget.MemberOwner,{type:null,getMethodName:null,setMethodName:null,changedNotificationName:null,defaultValue:"",writable:null,readable:null,validator:null,initialize:function(name,owner,options){this.installOptions(options)
this.name=name
this.owner=owner
this.superGetMember=Widget.MemberOwner.prototype.getMember.bind(this)
if(this.getMethodName==null){if(this.name.substring(0,3)=="is-"||this.name.substring(0,4)=="can-"||this.name.substring(0,4)=="has-"||this.name.substring(0,5)=="does-"){this.getMethodName=this.name.camelize()}else{this.getMethodName=("get-"+this.name).camelize()}}
if(this.setMethodName==null){if(this.name.substring(0,3)=="is-"){this.setMethodName=("set-"+this.name.slice(3)).camelize()}else if(this.name.substring(0,4)=="can-"){this.setMethodName=("set-"+this.name.slice(4)).camelize()}else if(this.name.substring(0,4)=="has-"){this.setMethodName=("set-"+this.name.slice(4)).camelize()}else if(this.name.substring(0,5)=="does-"){this.setMethodName=("set-"+this.name.slice(5)).camelize()}else{this.setMethodName=("set-"+this.name).camelize()}}
if(this.changedNotificationName==null){this.changedNotificationName=(this.name+"-changed").camelize()}
if(this.type==null){if(this.name.substring(0,3)=="is-"||this.name.substring(0,4)=="can-"||this.name.substring(0,4)=="has-"||this.name.substring(0,5)=="does-"){this.type="boolean"}else if(this.name.substring(this.name.length-6)=="-count"||this.name.substring(this.name.length-7)=="-number"){this.type="int"}else{this.type="string"}}
if(this.readable==null){this.readable=(this.owner[this.getMethodName]!=null)}
if(this.writable==null){this.writable=(this.owner[this.setMethodName]!=null)}
this.observe(this.owner,this.changedNotificationName,"ownerChanged")
this.addEvent("value-changed")
if(Widget.isLogEnabled()){Widget.log("Property","Created property: "+this.toString())}},getValue:function(){if(!this.readable){return this.defaultValue}
var value=this.owner[this.getMethodName]()
if(value==null){value=this.defaultValue}else{value=Widget.Internal.getMemberTypeConverter().convertForGet(this.type,value)}
return value},setValue:function(value){if(!this.writable){return}
var valueToSet=null
if(value!=this.defaultValue){var converter=Widget.Internal.getMemberTypeConverter()
if(!converter.validate(this.type,value)){return}
valueToSet=converter.convertForSet(this.type,value)}
this.owner[this.setMethodName](valueToSet)},isWritable:function(){return this.writable},isReadable:function(){return this.readable},valueChanged:function(){this.notifyObservers('valueChanged')},ownerChanged:function(){this.valueChanged()},getMember:function(name){var member=this.superGetMember(name)
if(member==null&&this.type=="widget"&&this.readable){var widget=this.owner[this.getMethodName]()
if(widget!=null){member=widget.getMember(name)}}
return member}})
Widget.Event=Class.define(Widget.OptionsContainer,Widget.Observable,Widget.Observer,Widget.MemberOwner,{notificationName:null,initialize:function(name,owner,options){this.installOptions(options)
this.name=name
this.owner=owner
if(this.notificationName==null){if(this.name!=""){this.notificationName=this.name.camelize()}else{}}
if(this.notificationName!=null){this.observe(this.owner,this.notificationName,"handleOwnerEvent")}else{this.observe(this.owner,null,"handleOwnerMetaEvent")}},handleOwnerEvent:function(){if(arguments.length==0){this.notifyObservers("sent")}else{this.notifyObservers.apply(this,["sent"].concat($A(arguments)))}},handleOwnerMetaEvent:function(){var eventName=this.owner.getEventNameForMetaEvent(arguments[0])
if(eventName!=null){var args=$A(arguments)
args[0]=eventName
this.notifyObservers.apply(this,["sent"].concat(args))}}})
Widget.Widget=Class.define(Widget.MemberOwner,Widget.OptionsContainer,Widget.Observable,Widget.Observer,{initializeWidget:function(id,options){this.installOptions(options)
this.id=id
this.element=$(this.id)
this.callbackHandler=new Widget.CallbackHandler()},getElement:function(){return this.element}})
Widget.Internal.Widget=Class.define(Widget.OptionsContainer,Widget.Observable,Widget.Observer,{initializeWidget:function(id,options){this.installOptions(options)
this.id=id
this.element=$(this.id)
this.callbackHandler=new Widget.CallbackHandler()},getElement:function(){return this.element}})
Widget.define=function(a1,a2){return Class.define.apply(Class,[Widget.Widget].concat($A(arguments)))}
Widget.Internal.define=function(a1,a2){return Class.define.apply(Class,[Widget.Internal.Widget].concat($A(arguments)))}
Widget.Dismiss=Widget.define({initialize:function(id,type,button,dismissable){this.initializeWidget(id);this.type=type;this.button=button;this.action=dismissable.getAction("dismiss");this.observe(button,"pressed","_buttonPressed");if(this.action!=null){this.observe(this.action,"isEnabledChanged","_updateButtonWithAction")
this._updateButtonWithAction()}},_buttonPressed:function(){if(this.action!=null){this.action.invoke(this.type);}},_updateButtonWithAction:function(){this.button.setEnabled(this.action.isEnabled());}});Widget.Internal.Button=Class.define(Widget.OptionsContainer,Widget.Observable,{enabled:true,disabledStyle:{},initialize:function(id,options){this.installOptions(options)
this.id=id
var element=this.getElement()
if(this.isNative()&&(Prototype.nokiaOSSBrowser()||Prototype.firefoxBrowser())){Widget.addElementObserver(element,"DOMActivate",this.press.bindAsEventListener(this));}else{Widget.addElementObserver(element,Widget.CLICK,this.press.bindAsEventListener(this));Widget.addElementObserver(element,Widget.KEYPRESS,this.keyPressed.bindAsEventListener(this));Widget.makeFocusable(element);}
this.update()},getElement:function(){return $(this.id)},press:function(){if(this.enabled){this.notifyObservers('pressed')}},click:function(){this.press()},keyPressed:function(ev){if(!this.isNative()){if(ev.keyCode==13){this.press()}}},setEnabled:function(enabled){this.enabled=enabled
this.update()
this.notifyObservers('isEnabledChanged')},isEnabled:function(){return this.enabled},update:function(){if(this.enabled){if(this.isNative()){this.getElement().disabled=false}
this.getElement().vfcRevertStyle()}else{if(this.isNative()){this.getElement().disabled=true}
this.getElement().vfcReplaceStyle(this.disabledStyle)}},isNative:function(){return this.getElement().tagName.toUpperCase()=="BUTTON"}})
Widget.Button=Widget.define({action:null,initialize:function(id,options){this.initializeWidget(id,options)
this.button=new Widget.Internal.Button(id,options)
this.observe(this.button,"pressed","internalButtonPressed")
this.observe(this.button,"isEnabledChanged","internalButtonEnabledChanged")
if(this.action!=null){this.observe(this.action,"isEnabledChanged","updateButtonWithAction")
this.updateButtonWithAction()}
this.addAction("press")
this.addAction("enable")
this.addAction("disable")
this.addProperty("is-enabled")
this.addEvent("pressed")},press:function(){this.button.press()},canPress:function(){return this.isEnabled()},internalButtonPressed:function(){this.notifyObservers("pressed")
if(this.action!=null){this.action.invoke()}},internalButtonEnabledChanged:function(){this.notifyObservers("isEnabledChanged")
this.notifyObservers("canPressChanged")
this.notifyObservers("canEnableChanged")
this.notifyObservers("canDisableChanged")},isEnabled:function(){return this.button.isEnabled()},setEnabled:function(enabled){if(this.action==null){this.button.setEnabled(enabled)}},enable:function(){this.setEnabled(true)},canEnable:function(){return!this.isEnabled()},disable:function(){this.setEnabled(false)},canDisable:function(){return this.isEnabled()},updateButtonWithAction:function(){this.button.setEnabled(this.action.isEnabled())}})
Widget.Internal.Display=Class.define(Widget.OptionsContainer,Widget.Observable,{content:"",initialize:function(id,options){this.installOptions(options)
this.id=id
this.updateHTML()},getElement:function(){return $(this.id)},setContent:function(content){if(this.content!=content){this.content=content
this.updateHTML()
this.notifyObservers("contentChanged")
this.notifyObservers("canClearContentChanged")}},getContent:function(){return this.content},clearContent:function(){this.setContent("")},canClearContent:function(){return this.content!=""},updateHTML:function(){this.getElement().innerHTML=this.content.escapeHTML()}})
Widget.Display=Widget.define({property:null,initialize:function(id,options){this.initializeWidget(id,options)
this.display=new Widget.Internal.Display(id,options)
this.observe(this.display,"contentChanged","contentChanged")
this.observe(this.display,"canClearContentChanged","canClearContentChanged")
if(this.property!=null){this.observe(this.property,"valueChanged","updateWithProperty")
this.updateWithProperty()}
this.addAction("clear-content")
this.addProperty("content")},setContent:function(string){if(this.property==null){this.display.setContent(string)}},getContent:function(){return this.display.getContent()},clearContent:function(){this.display.clearContent()},canClearContent:function(){return this.display.canClearContent()},canClearContentChanged:function(){this.notifyObservers("canClearContentChanged")},contentChanged:function(){this.notifyObservers("contentChanged")},updateWithProperty:function(){this.display.setContent(this.property.getValue())}})
Widget.Internal.Control=Class.define(Widget.OptionsContainer,Widget.Observable,{id:null,initializeControl:function(id,options){this.installOptions(options);this.id=id;var element=this.getElement();this.value=this.getValueFromControl();Widget.addElementObserver(element,Widget.CHANGE,this.controlValueChanged.bindAsEventListener(this));},getValueFromControl:function(){return this.getElement().value},getValue:function(){return this.value;},setValue:function(string){this.getElement().value=string;this.checkValueChanged();},checkValueChanged:function(){if(this.value!=this.getElement().value){this.value=this.getElement().value;this.valueChanged();}},valueChanged:function(){this.notifyObservers('valueChanged')},controlValueChanged:function(){this.checkValueChanged()},getElement:function(){return $(this.id)}});Widget.Internal.Input=Class.define(Widget.Internal.Control,{initialize:function(id,options){this.initializeControl(id,options);this.lastPartialValue=this.getPartialValue();setInterval(this.checkPartialValueChanged.bind(this),100);},getPartialValue:function(){return this.getElement().value;},partialValueChanged:function(){this.lastPartialValue=this.getPartialValue()
this.notifyObservers('partialValueChanged')},checkPartialValueChanged:function(){if(this.getPartialValue()!=this.lastPartialValue){this.partialValueChanged()}}});Widget.Internal.Select=Class.define(Widget.Internal.Control,{internalValue:[],initialize:function(id,options){this.initializeControl(id,options);},getValueFromControl:function(){if(this.multiple==false){return this.getElement().value}else{this.value=$A();var options=this.getElement().options;for(var i=0;i<options.length;i++){this.value[i]=options[i].selected;}
return this.value;}},setValue:function(value){if(this.multiple==false){this.getElement().value=value;}else{var options=this.getElement().options;for(var i=0;i<options.length;i++){if(value.indexOf(options[i].value)!=-1){options[i].selected=true;}else{options[i].selected=false;}}}
this.checkValueChanged();},getValue:function(){if(this.multiple==false){return this.value;}else{var array=$A();var options=this.getElement().options;for(var i=0;i<this.value.length;i++){if(this.value[i]){array.push(options[i].value);}}
return array;}},checkValueChanged:function(){if(this.multiple==false){if(this.value!=this.getElement().value){this.value=this.getElement().value;this.valueChanged();}}else{var options=this.getElement().options;var changed=false;for(var i=0;i<options.length;i++){if(options[i].selected!=this.value[i]){changed=true;this.value[i]=options[i].selected;}}
if(changed){this.valueChanged();}}}});Widget.Control=Widget.define({property:null,initializeControl:function(internalControl,valueType,options){this.initializeWidget(null,options)
this.internalControl=internalControl
this.isUpdatingFromProperty=false
if(this.property!=null){this.observe(this.property,"valueChanged","propertyValueChanged")
this.updateFromProperty()}
this.observe(this.internalControl,"valueChanged","internalControlValueChanged")
this.addProperty("value",{type:valueType})},getValue:function(){return this.internalControl.getValue()},setValue:function(string){this.internalControl.setValue(string)},internalControlValueChanged:function(){if(this.property!=null&&!this.isUpdatingFromProperty){this.updateProperty()}
this.valueChanged()},propertyValueChanged:function(){this.updateFromProperty()},updateFromProperty:function(){this.isUpdatingFromProperty=true
this.setValue(this.property.getValue())
this.isUpdatingFromProperty=false},updateProperty:function(){this.property.setValue(this.getValue())},valueChanged:function(){this.notifyObservers("valueChanged")}});Widget.Input=Class.define(Widget.Control,{initialize:function(id,options){this.initializeControl(new Widget.Internal.Input(id,options),"string",options);this.observe(this.internalControl,"partialValueChanged","partialValueChanged");this.addProperty("partial-value")},getPartialValue:function(){return this.internalControl.getPartialValue()},partialValueChanged:function(){this.notifyObservers("partialValueChanged")}});Widget.Select=Class.define(Widget.Control,{initialize:function(id,options){if($(id).getAttribute('multiple')=='multiple'){this.initializeControl(new Widget.Internal.Select(id,options),"list",options)}else{this.initializeControl(new Widget.Internal.Select(id,options),"string",options)}}});Widget.Script=Widget.define({initialize:function(content,options){this.initializeWidget(null,options)
eval("this.func = function(){"+content+"}")
this.addAction("invoke")},invoke:function(){this.func.apply(this,arguments)}})
Widget.Handler=Widget.define({initialize:function(event,action,options){this.initializeWidget(null,options)
this.enabled=true
this.event=event
this.action=action
if(this.getOption("isEnabled")!=null){this.setEnabled(this.getOption("isEnabled"))}
this.observe(event,"sent","eventSent")
this.addProperty("is-enabled")},isEnabled:function(){return this.enabled},setEnabled:function(enabled){if(this.enabled!=enabled){this.enabled=enabled
this.notifyObservers("isEnabledChanged")}},eventSent:function(){if(this.enabled){this.action.invoke.apply(this.action,arguments)}}})
Widget.Internal.Content=Widget.define({initializeContent:function(id,html,parent,script,options){this.initializeWidget(id,options)
this.html=html
this.parent=parent
this.script=script
if(id==null){if(typeof(html)=="string"){var tempElement=document.createElement("div")
tempElement.innerHTML=html.stripScripts()
var scripts=html.extractScripts()
var script=""
for(var i=0;i<scripts.length;i++){script+=scripts[i]+";"}
this.script=script
this.element=tempElement.firstChild}else{this.element=html}}else{this.parent.children.push(this)}},getContainer:function(){return this.parent},runScript:function(){if(this.script!=null){eval(this.script)}},adding:function(){this.notifyObservers("adding")},added:function(){this.runScript()
this.notifyObservers("added")
this.notifyObservers("containerChanged")},removing:function(){this.notifyObservers("removing")},removed:function(){this.notifyObservers("removed")}})
Widget.Internal.BlockContent=Class.define(Widget.Internal.Content,{initialize:function(id,html,parent,script,options){this.initializeContent(id,html,parent,script,options)},clone:function(){return new Widget.Internal.BlockContent(null,this.getElement().cloneNode(true),null,null,this)}})
Widget.Internal.InlineContent=Class.define(Widget.Internal.Content,{initialize:function(id,html,parent,script,options){this.initializeContent(id,html,parent,script,options)},clone:function(){return new Widget.Internal.InlineContent(null,this.getElement().cloneNode(true),null,null,this)}})
Widget.Internal.PCDATAContent=Class.define(Widget.Internal.Content,{initialize:function(id,string,parent,options){if(id!=null){this.initializeContent(id,null,parent,options)}else{this.initializeContent(null,"<span>"+string.escapeHTML()+"</span>",parent,options)}},clone:function(){return new Widget.Internal.PCDATAContent(null,this.getElement().innerHTML.unescapeHTML(),null,this)}})
Widget.Internal.Container=Widget.define({initializeContainer:function(id,options){this.initializeWidget(id,options)
this.children=[]
if(this.getOption("content")!=null){this.setContent(this.getOption("content"))}},getAt:function(index){if(index>=1&&index<=this.children.length){return this.children[index-1]}},addLast:function(content){this.addAt(content,this.children.length+1)},addFirst:function(content){this.addAt(content,1)},addAt:function(content,index){if(index<1||index>this.children.length+1){return}
if(content.parent!=null){return}
content.adding()
this.children.splice(index-1,0,content)
content.parent=this
var parentElement=this.getElement()
var addedElement=content.getElement()
if(index<this.children.length){var existingElement=this.children[index].getElement()
parentElement.insertBefore(addedElement,existingElement)}else{parentElement.appendChild(addedElement)}
this.notifyObservers("sizeChanged")
content.added()},remove:function(content){for(var i=0;i<this.children.length;i++){if(this.children[i]===content){this.removeAt(i+1)
return}}},removeFirst:function(){this.removeAt(1)},removeLast:function(){this.removeAt(this.children.length)},removeAt:function(index){if(index<1||index>this.children.length){return}
var content=this.children[index-1]
if(content.parent!==this){return}
content.removing()
this.children.splice(index-1,1)
content.parent=null
var parentElement=this.getElement()
var childElement=content.getElement()
parentElement.removeChild(childElement)
this.notifyObservers("sizeChanged")
content.removed()},getSize:function(){return this.children.length},setContent:function(content){if(content==null||content.parent==null){while(this.getSize()>0){this.removeLast()}
if(content!=null){this.addLast(content)}
this.content=content
this.notifyObservers("contentChanged")}},getContent:function(){return this.content}})
Widget.Internal.BlockContainer=Class.define(Widget.Internal.Container,{initialize:function(id,options){this.initializeContainer(id,options)
if(this.getOption("content")!=null){this.setContent(this.getOption("content"))}}})
Widget.Internal.InlineContainer=Class.define(Widget.Internal.Container,{initialize:function(id,options){this.initializeContainer(id,options)
if(this.getOption("content")!=null){this.setContent(this.getOption("content"))}}})
Prototype.isEmulatedOpacity=null;Prototype.netFront=function(){return(/NetFront/.test(navigator.userAgent));};Prototype.nokiaOSSBrowser=function(){return(/AppleWebKit/.test(navigator.userAgent)&&/SymbianOS/.test(navigator.userAgent));};Prototype.firefoxBrowser=function(){return(/Firefox/.test(navigator.userAgent));};Prototype.konquerorBrowser=function(){return(/Konqueror/.test(navigator.userAgent));};Prototype.operaPC=function(){return(/Opera\/9./.test(navigator.userAgent));};Prototype.useEmulatedOpacity=function(){if(this.netFront()){this.isEmulatedOpacity=true;}
if(this.nokiaOSSBrowser()){this.isEmulatedOpacity=true;}
if(this.msieBrowser()){this.isEmulatedOpacity=false;}
if(this.isEmulatedOpacity==null){var element=document.documentElement;if(element.getStyle('opacity')!=1){this.isEmulatedOpacity=true;}else{this.isEmulatedOpacity=false;}}
return(this.isEmulatedOpacity);};Prototype.useMouseAsSelect=function(){return this.nokiaOSSBrowser()||this.operaMobile();};var Table={createRowChildren:function(content){var div=document.createElement('div');div.innerHTML='<table><tbody><tr>'+content+'</tr></tbody></table>';return $A(div.childNodes[0].childNodes[0].childNodes[0].childNodes);},createTbodyChildren:function(content){var div=document.createElement('div');div.innerHTML='<table><tbody>'+content+'</tbody></table>';return $A(div.childNodes[0].childNodes[0].childNodes);},createColGroupChildren:function(content){var div=document.createElement('div');div.innerHTML='<table><colgroup>'+content+'</colgroup></table>';return $A(div.childNodes[0].childNodes[0].childNodes);},createTableChildren:function(content){var div=document.createElement('div');div.innerHTML='<table>'+content+'</table>';return $A(div.childNodes[0].childNodes);},isParentOfTableElements:function(tagName){return tagName=="table"||tagName=="tbody"||tagName=="tfoot"||tagName=="thead"||tagName=="tr"||tagName=="colgroup";},createChildren:function(tagName,content){var subnodes;if(tagName=='table'){subnodes=this.createTableChildren(content)}else if(tagName=='tbody'||tagName=='tfoot'||tagName=='thead'){subnodes=this.createTbodyChildren(content)}else if(tagName=='tr'){subnodes=this.createRowChildren(content)}else if(tagName=='colgroup'){subnodes=this.createColGroupChildren(content)}
return subnodes}}
Abstract.Insertion.prototype.originalInitialize=Abstract.Insertion.prototype.initialize
Object.extend(Abstract.Insertion.prototype,{initialize:function(element,content){this.element=$(element);this.content=content.stripScripts();if(this.element.insertAdjacentHTML||this.element.ownerDocument.createRange){this.originalInitialize(element,content);}else{this.insertHTML();setTimeout(function(){content.evalScripts()},10);}},getFirstTag:function(parentNode,startIdx){if(startIdx==parentNode.innerHTML.length-1){return"";}
var singleQuotationOpened=false;var doubleQuotationOpened=false;var htmlStr=parentNode.innerHTML;var currIdx=startIdx+1;var currentChar;do{currentChar=htmlStr.charAt(currIdx);if(currentChar==">"){if(!(singleQuotationOpened||doubleQuotationOpened)){break;}}
if(currentChar=="'"&&!doubleQuotationOpened){singleQuotationOpened=!singleQuotationOpened;}
if(currentChar=='"'&&!singleQuotationOpened){doubleQuotationOpened=!doubleQuotationOpened;}
currIdx++;}while(currIdx<htmlStr.length)
return htmlStr.substring(startIdx,currIdx+1);},getElementAsString:function(parentNode,childNode,currIdx){var ret;if(childNode.innerHTML){ret=this.getFirstTag(parentNode,currIdx);ret=ret+childNode.innerHTML;var firstClosingTagOccur=parentNode.innerHTML.indexOf("</",currIdx+ret.length);if(firstClosingTagOccur==currIdx+ret.length){var endOfClosingTag=parentNode.innerHTML.indexOf(">",currIdx+ret.length);ret=ret+parentNode.innerHTML.substring(firstClosingTagOccur,endOfClosingTag+1);}}else if(childNode.data){ret=childNode.data;}else{ret=this.getFirstTag(parentNode,currIdx);var firstClosingTagOccur=parentNode.innerHTML.indexOf("</",currIdx+ret.length);if(firstClosingTagOccur==currIdx+ret.length){var endOfClosingTag=parentNode.innerHTML.indexOf(">",currIdx+ret.length);ret=ret+parentNode.innerHTML.substring(firstClosingTagOccur,endOfClosingTag+1);}}
return ret}});Object.extend(Insertion.Before.prototype,{insertHTML:function(){var parentNode=this.element.parentNode;var tagName=parentNode.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,this.content);subnodes.reverse(false).each(function(sn){parentNode.insertBefore(sn,this.element);}.bind(this));}else{var str="";var currIdx=0;var currentChild=parentNode.firstChild;while(currentChild&&currentChild!=this.element){var elemAsStr=this.getElementAsString(parentNode,currentChild,currIdx);str=str+elemAsStr;currIdx=currIdx+elemAsStr.length;currentChild=currentChild.nextSibling;}
str=str+this.content;str=str+parentNode.innerHTML.substring(currIdx);parentNode.innerHTML=str;}}});Object.extend(Insertion.After.prototype,{insertHTML:function(){var parentNode=this.element.parentNode;var tagName=parentNode.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,this.content);if(this.element.nextSibling){subnodes.each(function(sn){parentNode.insertBefore(sn,this.element.nextSibling)}.bind(this));}else{subnodes.each(function(sn){parentNode.appendChild(sn)});}}else{var str="";var currIdx=0;var currentChild=parentNode.firstChild;do{var elemAsStr=this.getElementAsString(parentNode,currentChild,currIdx);str=str+elemAsStr;currIdx=currIdx+elemAsStr.length;currentChild=currentChild.nextSibling;}while(currentChild&&currentChild.previousSibling!=this.element)
str=str+this.content;str=str+parentNode.innerHTML.substring(currIdx);parentNode.innerHTML=str;}}});Object.extend(Insertion.Top.prototype,{insertHTML:function(){var tagName=this.element.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,this.content);this.insertContent(subnodes);}else{var str=this.element.innerHTML;this.element.innerHTML=this.content+str;}}});Object.extend(Insertion.Bottom.prototype,{insertHTML:function(){var tagName=this.element.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,this.content);this.insertContent(subnodes);}else{var str=this.element.innerHTML;this.element.innerHTML=str+this.content;}}});Element.originalUpdate=Element.update;Element.originalReplace=Element.replace;Object.extend(Element,{update:function(element,html){if(Prototype.netFront()){var wholeElement=$(element);var tagName=wholeElement.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,html.stripScripts());wholeElement.innerHTML="";subnodes.each(function(s){wholeElement.appendChild(s);});setTimeout(function(){html.evalScripts()},10);}else{this.originalUpdate(element,html);}}else{this.originalUpdate(element,html);}},replace:function(element,html){if(Prototype.netFront()){var wholeElement=$(element);var parentNode=wholeElement.parentNode;var tagName=parentNode.tagName.toLowerCase();if(Table.isParentOfTableElements(tagName)){var subnodes=Table.createChildren(tagName,html.stripScripts());if(subnodes.length==0){parentNode.removeChild(wholeElement);}else{var lastSubnode=subnodes[subnodes.length-1];parentNode.replaceChild(lastSubnode,wholeElement);for(var i=0;i<=subnodes.length-2;i++){parentNode.insertBefore(lastSubnode);}}
setTimeout(function(){html.evalScripts()},10);}else{this.originalReplace(element,html);}}else{this.originalReplace(element,html);}}});function Color(rgbString)
{this._rgba=false;if(rgbString=="transparent")
{this.toString=function(){return"transparent"};this.transparent=true;return this;}
var color=namedColors[rgbString];if(color)
rgbString=color;if(rgbString[0]=='#')
{var rex=/^#(\w{2})(\w{2})(\w{2})$/;var nums=rex.exec(rgbString);if(!nums)
{nums=rex.exec(rgbString.parseColor());}
this.red=parseInt(nums[1],16);this.green=parseInt(nums[2],16);this.blue=parseInt(nums[3],16);this.transparent=false;}else if(rgbString.indexOf('rgba')!=-1){var rex=/^rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;var nums=rex.exec(rgbString);if(!nums)
throw"invalid color: "+rgbString;this.red=parseInt(nums[1]);this.green=parseInt(nums[2]);this.blue=parseInt(nums[3]);this.alpha=parseInt(nums[4]);this.transparent=false;this._rgba=true;this.toString=function(){return"rgba("+this.red+","+this.green+","+this.blue+","+this.alpha+")";};}else if(rgbString[0]=='r'){var rex=/^rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;var nums=rex.exec(rgbString);if(!nums)
throw"invalid color: "+rgbString;this.red=parseInt(nums[1]);this.green=parseInt(nums[2]);this.blue=parseInt(nums[3]);this.transparent=false;this.toString=function(){return"rgb("+this.red+","+this.green+","+this.blue+")";};}
return this;}
function BlendColor(fg,bg,opacity)
{if(typeof opacity!="number"||opacity<0||opacity>1){opacity=1.0;}
if(!fg||opacity<0.001){return bg;}
if(fg.transparent){this.red=Math.floor((1-opacity)*bg.red);this.green=Math.floor((1-opacity)*bg.green);this.blue=Math.floor((1-opacity)*bg.blue);}else if(bg.transparent){this.red=Math.floor(opacity*fg.red);this.green=Math.floor(opacity*fg.green);this.blue=Math.floor(opacity*fg.blue);}else{this.red=Math.floor(opacity*fg.red+(1-opacity)*bg.red);this.green=Math.floor(opacity*fg.green+(1-opacity)*bg.green);this.blue=Math.floor(opacity*fg.blue+(1-opacity)*bg.blue);}
if(fg._rgba){this.toString=function(){return"rgba("+this.red+","+this.green+","+this.blue+","+fg.alpha+")";}}else{this.toString=function(){return"rgb("+this.red+","+this.green+","+this.blue+")";}}
return this;}
Element.saveStylesFakeOpacity=function(element){element=$(element);if(!element._madeFakeOpacity){element._madeFakeOpacity=true;element._fo_foreground=Element.getStyle(element,"background-color");element._fo_fg=new Color(element._fo_foreground);element._fo_text=Element.getStyle(element,"color");element._fo_tx=new Color(element._fo_text);element._fo_border_left=Element.getStyle(element,"border-left-color");element._fo_bo=new Color(element._fo_border_left);element._fo_border_right=Element.getStyle(element,"border-right-color");element._fo_border_top=Element.getStyle(element,"border-top-color");element._fo_border_bottom=Element.getStyle(element,"border-bottom-color");element._fo_backgroud=Element.getNonTransparentBackground(element.parentNode);element._fo_bg=new Color(element._fo_backgroud);}}
Element.getNonTransparentBackground=function(element){var node=element;var background=Element.getStyle(node,"background-color");while(((background=="transparent")||(background=="rgba(0, 0, 0, 0)"))&&(node.parentNode!=null)&&(node.nodeName!=="HTML")){node=node.parentNode;background=Element.getStyle(node,"background-color");}
if((background==="transparent")||(node.nodeName==="HTML")){background="rgb(255,255,255)";}
if(background==="rgba(0, 0, 0, 0)"){background="rgba(255,255,255,0)";}
return background;}
Element.restoreStylesFakeOpacity=function(element){element=$(element);if(element._madeFakeOpacity){element._madeFakeOpacity=undefined;element.style.backgroundColor=element._fo_foreground;element.style.color=element._fo_text;element.style.borderLeftColor=element._fo_border;element.style.borderLeftColor=element._fo_border_left;element.style.borderRightColor=element._fo_border_right;element.style.borderTopColor=element._fo_border_top;element.style.borderBottomColor=element._fo_border_bottom;}}
Element.fakeOpacity=function(element,opacity){element=$(element);var i=0;var childList=element.getElementsByTagName("a");for(i=0;i<childList.length;i++){Element.aFakeOpacity(childList[i],opacity);}
if(opacity){Element.normalFakeOpacity(element,opacity);}};Element.aFakeOpacity=function(element,opacity){element=$(element);var faked=new BlendColor(element._fo_tx,element._fo_bg,opacity);element.style.color=faked.toString();element.style.opacity=opacity.toString();};Element.normalFakeOpacity=function(element,opacity){element=$(element);Element.saveStylesFakeOpacity(element);var faked=new BlendColor(element._fo_fg,element._fo_bg,opacity);element.style.backgroundColor=faked.toString();faked=new BlendColor(element._fo_tx,element._fo_bg,opacity);element.setStyle({color:faked.toString()});faked=new BlendColor(element._fo_bo,element._fo_bg,opacity);element.style.borderColor=faked.toString();element.style.opacity=opacity.toString();};function SetFakeOpacity(id,opacity)
{var el=$(id);if(!el)
throw"unknown element id: "+id;if(!el.getStyle)
{el.getStyle=function(style){return computedStyle(this,style);};el.parentNode.getStyle=function(style){return computedStyle(this,style);};}
if(opacity<0.99)
{Element.fakeOpacity(el,opacity);}}
var namedColors={aliceblue:'#f0f8ff',antiquewhite:'#faebd7',aqua:'#00ffff',aquamarine:'#7fffd4',azure:'#f0ffff',beige:'#f5f5dc',bisque:'#ffe4c4',black:'#000000',blanchedalmond:'#ffebcd',blue:'#0000ff',blueviolet:'#8a2be2',brown:'#a52a2a',burlywood:'#deb887',cadetblue:'#5f9ea0',chartreuse:'#7fff00',chocolate:'#d2691e',coral:'#ff7f50',cornflowerblue:'#6495ed',cornsilk:'#fff8dc',crimson:'#dc143c',cyan:'#00ffff',darkblue:'#00008b',darkcyan:'#008b8b',darkgoldenrod:'#b8860b',darkgray:'#a9a9a9',darkgreen:'#006400',darkkhaki:'#bdb76b',darkmagenta:'#8b008b',darkolivegreen:'#556b2f',darkorange:'#ff8c00',darkorchid:'#9932cc',darkred:'#8b0000',darksalmon:'#e9967a',darkseagreen:'#8fbc8f',darkslateblue:'#483d8b',darkslategray:'#2f4f4f',darkturquoise:'#00ced1',darkviolet:'#9400d3',deeppink:'#ff1493',deepskyblue:'#00bfff',dimgray:'#696969',dodgerblue:'#1e90ff',feldspar:'#d19275',firebrick:'#b22222',floralwhite:'#fffaf0',forestgreen:'#228b22',fuchsia:'#ff00ff',gainsboro:'#dcdcdc',ghostwhite:'#f8f8ff',gold:'#ffd700',goldenrod:'#daa520',gray:'#808080',green:'#008000',greenyellow:'#adff2f',honeydew:'#f0fff0',hotpink:'#ff69b4',indianred:'#cd5c5c',indigo:'#4b0082',ivory:'#fffff0',khaki:'#f0e68c',lavender:'#e6e6fa',lavenderblush:'#fff0f5',lawngreen:'#7cfc00',lemonchiffon:'#fffacd',lightblue:'#add8e6',lightcoral:'#f08080',lightcyan:'#e0ffff',lightgoldenrodyellow:'#fafad2',lightgrey:'#d3d3d3',lightgreen:'#90ee90',lightpink:'#ffb6c1',lightsalmon:'#ffa07a',lightseagreen:'#20b2aa',lightskyblue:'#87cefa',lightslateblue:'#8470ff',lightslategray:'#778899',lightsteelblue:'#b0c4de',lightyellow:'#ffffe0',lime:'#00ff00',limegreen:'#32cd32',linen:'#faf0e6',magenta:'#ff00ff',maroon:'#800000',mediumaquamarine:'#66cdaa',mediumblue:'#0000cd',mediumorchid:'#ba55d3',mediumpurple:'#9370d8',mediumseagreen:'#3cb371',mediumslateblue:'#7b68ee',mediumspringgreen:'#00fa9a',mediumturquoise:'#48d1cc',mediumvioletred:'#c71585',midnightblue:'#191970',mintcream:'#f5fffa',mistyrose:'#ffe4e1',moccasin:'#ffe4b5',navajowhite:'#ffdead',navy:'#000080',oldlace:'#fdf5e6',olive:'#808000',olivedrab:'#6b8e23',orange:'#ffa500',orangered:'#ff4500',orchid:'#da70d6',palegoldenrod:'#eee8aa',palegreen:'#98fb98',paleturquoise:'#afeeee',palevioletred:'#d87093',papayawhip:'#ffefd5',peachpuff:'#ffdab9',peru:'#cd853f',pink:'#ffc0cb',plum:'#dda0dd',powderblue:'#b0e0e6',purple:'#800080',red:'#ff0000',rosybrown:'#bc8f8f',royalblue:'#4169e1',saddlebrown:'#8b4513',salmon:'#fa8072',sandybrown:'#f4a460',seagreen:'#2e8b57',seashell:'#fff5ee',sienna:'#a0522d',silver:'#c0c0c0',skyblue:'#87ceeb',slateblue:'#6a5acd',slategray:'#708090',snow:'#fffafa',springgreen:'#00ff7f',steelblue:'#4682b4',tan:'#d2b48c',teal:'#008080',thistle:'#d8bfd8',tomato:'#ff6347',turquoise:'#40e0d0',violet:'#ee82ee',violetred:'#d02090',wheat:'#f5deb3',white:'#ffffff',whitesmoke:'#f5f5f5',yellow:'#ffff00',yellowgreen:'#9acd32'};if(typeof(Effect)!='undefined'){Object.extend(Effect.Scale.prototype,{vfcSetup:function(){if(!(/^content/.test(this.options.scaleMode))){this.borderLeftWidthOld='0px';this.borderRightWidthOld='0px';this.borderTopWidthOld='0px';this.borderBottomWidthOld='0px';this.paddingLeftOld='0px';this.paddingRightOld='0px';this.paddingTopOld='0px';this.paddingBottomOld='0px';this.contentWidth=0;this.contentHeight=0;var divHeight=this.element.vfcGetDimensions().height;var divWidth=this.element.vfcGetDimensions().width;var border=Element.getStyle(this.element,'border-top-width');if(parseInt(border)!=0&&this.options.scaleY){this.borderTopWidthOld=border;Element.setStyle(this.element,{borderTopWidth:'0px'});}
border=Element.getStyle(this.element,'border-bottom-width');if(parseInt(border)!=0&&this.options.scaleY){this.borderBottomWidthOld=border;Element.setStyle(this.element,{borderBottomWidth:'0px'});}
this.bordersHeight=divHeight-this.element.vfcGetDimensions().height;this.bordersWidth=0;if(Prototype.msieBrowser()){this.bordersWidth=this.element.offsetWidth-this.element.clientWidth;}else{border=Element.getStyle(this.element,'border-left-width');if(parseInt(border)!=0&&this.options.scaleX){this.borderLeftWidthOld=border;this.borderRightWidthOld=Element.getStyle(this.element,'border-right-width');this.bordersWidth=parseInt(this.borderLeftWidthOld);}
border=Element.getStyle(this.element,'border-right-width');if(parseInt(border)!=0&&this.options.scaleX){this.borderRightWidthOld=border;this.bordersWidth+=parseInt(this.borderRightWidthOld);}}
var divHeightBefore=this.element.vfcGetDimensions().height;this.paddingsWidth=0;var padding=Element.getStyle(this.element,'padding-left');if(parseInt(padding)!=0&&this.options.scaleX){this.paddingLeftOld=padding;if(Prototype.msieBrowser()){this.paddingsWidth=Widget.getPXDimension(this.paddingLeftOld);}else{this.paddingsWidth=parseInt(this.paddingLeftOld);}}
padding=Element.getStyle(this.element,'padding-right');if(parseInt(padding)!=0&&this.options.scaleX){if(Prototype.msieBrowser()){this.paddingsWidth+=Widget.getPXDimension(this.paddingRightOld);}else{this.paddingsWidth+=parseInt(this.paddingRightOld);}}
padding=Element.getStyle(this.element,'padding-top');if(parseInt(padding)!=0&&this.options.scaleY){this.paddingTopOld=padding;Element.setStyle(this.element,{paddingTop:'0px'});}
padding=Element.getStyle(this.element,'padding-bottom');if(parseInt(padding)!=0&&this.options.scaleY){this.paddingBottomOld=padding;Element.setStyle(this.element,{paddingBottom:'0px'});}
var divHeightAfter=this.element.vfcGetDimensions().height;this.paddingsHeight=divHeightBefore-divHeightAfter;this.paddingsHeight=divHeightBefore-divHeightAfter;this.contentWidth=divWidth-this.paddingsWidth-this.bordersWidth;this.contentHeight=divHeight-this.paddingsHeight-this.bordersHeight;}},recursiveCollectOriginalStyle:function(childNodes){var i=0;for(i=0;i<childNodes.length;i++){var child=childNodes[i];if(child.hasChildNodes()){this.recursiveCollectOriginalStyle(child.childNodes);}
var styleArray=['top','left','height','fontSize','width'];child._originalStyle={};var l=0;for(l=0;l<styleArray.length;l++){try{child._originalStyle[styleArray[l].camelize()]=child.style[styleArray[l].camelize()];}catch(e){}}
if(child['getDimensions']!=undefined){var dim=child.getDimensions();child._height=dim.height;child._width=dim.width;}
child._originalTop=child.offsetTop;child._originalLeft=child.offsetLeft;try{var lineHeight=child.getStyle('line-height')||'100%';child._lineHeightValue=lineHeight;if(lineHeight=='normal'){lineHeight='100%';}
['em','px','%'].each(function(lineHeightType){if(lineHeight.indexOf(lineHeightType)>0){child._lineHeight=parseInt(lineHeight);child._lineHeightUnit=lineHeightType;}});}catch(e){}}},vfcFinish:function(position){if(!(/^content/.test(this.options.scaleMode))){if(this.options.scaleX){if(this.bordersWidth>0){Element.setStyle(this.element,{borderLeftWidth:this.borderLeftWidthOld+'px'});Element.setStyle(this.element,{borderRightWidth:this.borderRightWidthOld+'px'});}
if(this.paddingsWidth>0){Element.setStyle(this.element,{paddingLeft:this.paddingLeftOld+'px'});Element.setStyle(this.element,{paddingRight:this.paddingRightOld+'px'});}}
if(this.options.scaleY){if(this.bordersHeight>0){Element.setStyle(this.element,{borderTopWidth:this.borderTopWidthOld+'px'});Element.setStyle(this.element,{borderBottomWidth:this.borderBottomWidthOld+'px'});}
if(this.paddingsHeight>0){Element.setStyle(this.element,{paddingTop:this.paddingTopOld+'px'});Element.setStyle(this.element,{paddingBottom:this.paddingBottomOld+'px'});}}}
if(this.restoreAfterFinish){if(Prototype.operaMobile()&&this.element.hasChildNodes()){this.recursiveRestoreOriginalStyle(this.element.childNodes)}else{this.element.setStyle(this.originalStyle);}}
if(Prototype.netFront()){var netFrontBadBehaveElementsList=['area','button','input','select','textarea'];var nodes;for(var i=0;i<netFrontBadBehaveElementsList.length;i++){nodes=this.element.getElementsByTagName(netFrontBadBehaveElementsList[i]);for(var j=0;j<nodes.length;j++){nodes[j].style.visibility='visible';}}}},recursiveSetSize:function(childNodes,currentScale){var i=0;var child;for(i=0;i<childNodes.length;i++){child=childNodes[i];if(child.nodeType==3){continue;}
if(child.hasChildNodes()){this.recursiveSetSize(childNodes[i].childNodes,currentScale);}
if(child._lineHeight&&child._lineHeightUnit){child.setStyle({lineHeight:parseInt(child._lineHeight*currentScale)+child._lineHeightUnit});}}
return;},recursiveRestoreOriginalStyle:function(childNodes){var i=0;for(i=0;i<childNodes.length;i++){if(childNodes[i].hasChildNodes()){this.recursiveRestoreOriginalStyle(childNodes[i].childNodes);}
if(childNodes[i]['setStyle']!=undefined){childNodes[i].setStyle(childNodes[i]._originalStyle);}
if(childNodes[i]._lineHeightValue!=undefined){childNodes[i].setStyle({'line-height':childNodes[i]._lineHeightValue});}}},vfcSetDimensions:function(height,width){var d={};height=parseInt(height);width=parseInt(width);var currentScale=height/this.dims[0];if(this.options.scaleX){d.width=width+'px';if(!(/^content/.test(this.options.scaleMode))){if(this.bordersWidth>0){Element.setStyle(this.element,{borderLeftWidth:parseInt((this.bordersWidth/2)*currentScale)+'px'});Element.setStyle(this.element,{borderRightWidth:parseInt((this.bordersWidth/2)*currentScale)+'px'});}
if(this.paddingsWidth>0){Element.setStyle(this.element,{paddingLeft:parseInt((this.paddingsWidth/2)*currentScale)+'px'});Element.setStyle(this.element,{paddingRight:parseInt((this.paddingsWidth/2)*currentScale)+'px'});}}}
if(this.options.scaleY){d.height=height+'px';if(!(/^content/.test(this.options.scaleMode))){if(this.bordersHeight>0){Element.setStyle(this.element,{borderTopWidth:parseInt((this.bordersHeight/2)*currentScale)+'px'});Element.setStyle(this.element,{borderBottomWidth:parseInt((this.bordersHeight/2)*currentScale)+'px'});}
if(this.paddingsHeight>0){Element.setStyle(this.element,{paddingTop:parseInt((this.paddingsHeight/2)*currentScale)+'px'});Element.setStyle(this.element,{paddingBottom:parseInt((this.paddingsHeight/2)*currentScale)+'px'});}}}
return d;}});Effect.Revert=function(element){element=$(element);var options=Object.extend({from:(element.getStyle('display')=='none'?0.0:element.getOpacity()||0.0),to:element.initialOpacity||1.0,afterFinishInternal:function(effect){effect.element.forceRerendering();},beforeSetup:function(effect){effect.element.setOpacity(effect.options.from);effect.element.show();}},arguments[1]||{});return new Effect.Opacity(element,options);}}
Widget.Load=Widget.define({src:null,when:null,owner:null,initialize:function(options){this.initializeLoad(options)},initializeLoad:function(options){this.initializeWidget(null,options)
this.addAction("execute")
this.addProperty("src")
this.addEvent("succeeded")
this.addEvent("failed")},setOwner:function(owner){this.owner=owner},getSrc:function(){return this.src},setSrc:function(src){if(this.src!=src){this.src=src
this.notifyObservers("srcChanged")
this.notifyObservers("canExecuteChanged")}},getWhen:function(){return this.when},execute:function(){if(this.canExecute()){this.doExecute()}},doExecute:function(){throw"Method doExecute() is not implemented. Overwrite it in subclass.";},canExecute:function(){return this.src!=null}})
Widget.Fetch=Widget.define({src:null,when:null,transformation:null,pageBase:null,service:"/services/fetch",transformCompile:null,transformCache:null,owner:null,initializeFetch:function(options){this.initializeWidget(null,options)
this.addAction("execute")
this.addProperty("src")
this.addProperty("transformation")
this.addEvent("succeeded")
this.addEvent("failed")},setOwner:function(owner){this.owner=owner;},isTransformCompiled:function(){return this.transformCompile},isTransformCached:function(){return this.transformCache},getTransformation:function(){return this.transformation},getSrc:function(){return this.src},setSrc:function(src){if(this.src!=src){this.src=src
this.notifyObservers("srcChanged")
this.notifyObservers("canExecuteChanged")}},getWhen:function(){return this.when},execute:function(){if(this.canExecute()){this.doExecute()}},doExecute:function(){throw"Method doExecute() is not implemented. Overwrite it in subclass.";},canExecute:function(){return this.src!=null}})
Widget.Refresh=Widget.define({src:null,interval:null,owner:null,initializeRefresh:function(options){this.initializeWidget(null,options)
this.addAction("execute")
this.addProperty("src")
this.addProperty("interval")},setOwner:function(owner){this.owner=owner;},getInterval:function(){return this.interval},_setInterval:function(interval){this.transformation=interval
this.notifyObservers("intervalChanged")},getSrc:function(){return this.src},setSrc:function(src){if(this.src!=src){this.src=src
this.notifyObservers("srcChanged")
this.notifyObservers("canExecuteChanged")}},execute:function(){if(this.canExecute()){this.doExecute()}},doExecute:function(){throw"Method doExecute() is not implemented. Overwrite it in subclass.";},canExecute:function(){return this.src!=null}})
Widget.Internal.Client=Widget.define({url:null,parameters:null,json:false,initialize:function(options){this.initializeWidget(null,options)},getURL:function(){return this.url},setURL:function(url){Widget.log("Client","Changing URL to: "+url)
this.url=url
this.notifyObservers("urlChanged")
this.notifyObservers("canSendRequestChanged")},getParameters:function(){return this.parameters},setParameters:function(parameters){Widget.log("Client","Changing parameters to: "+parameters)
this.parameters=parameters
this.notifyObservers("parametersChanged")},expectsJSONResponse:function(){return this.json},setExpectsJSONResponse:function(expectsJSONResponse){this.json=expectsJSONResponse},isBusy:function(){return this.pendingAJAXRequest!=null},sendRequest:function(){if(this.canSendRequest()){this.interruptRequest()
Widget.log("Client","Sending AJAX request")
this.setPendingAJAXRequest(new Ajax.Request(this.url,{parameters:$H(this.parameters||{}).toQueryString(),method:"get",onSuccess:this.ajaxRequestSucceeded.bind(this),onFailure:this.ajaxRequestFailed.bind(this),onException:this.ajaxRequestThrownException.bind(this)}))}},canSendRequest:function(){return this.url!=null},interruptRequest:function(){if(this.canInterruptRequest()){Widget.log("Client","Interrupting AJAX request")
this.pendingAJAXRequest.options['onSuccess']=null
this.pendingAJAXRequest.options['onFailure']=null
this.pendingAJAXRequest.options['onException']=null
this.setPendingAJAXRequest(null)
this.notifyObservers("requestInterrupted")}},canInterruptRequest:function(){return this.pendingAJAXRequest!=null},ajaxRequestSucceeded:function(transport){Widget.log("Client","AJAX request succeeded")
setTimeout(this.processAjaxRequestSuccess.bind(this,transport),0)},processAjaxRequestSuccess:function(transport){this.setPendingAJAXRequest(null)
if(this.json){try{this.responseContent=eval(transport.responseText)}catch(e){this.notifyObservers("requestFailed","Error evaluating JSON. "+e)
return}}else{var html=transport.responseText
this.getResponseArea().innerHTML=html.stripScripts()
this.responseContent=null
try{var scripts=html.extractScripts()
for(var i=0;i<scripts.length;i++){eval(scripts[i])}}catch(e){this.notifyObservers("requestFailed","Error evaluating response scripts. "+e)
return}
this.getResponseArea().innerHTML=""}
this.notifyObservers("requestSucceeded",this.responseContent)
if(this.responseContent!=null&&this.responseContent._widgetResponseErrorToken==true){this.notifyObservers("requestSucceededWithError",this.responseContent)}else{this.notifyObservers("requestSucceededWithoutError",this.responseContent)}},ajaxRequestFailed:function(transport){Widget.log("Client","AJAX request failed")
setTimeout(this.processAjaxRequestFailure.bind(this,transport),0)},processAjaxRequestFailure:function(transport){this.setPendingAJAXRequest(null)
this.notifyObservers("requestFailed","AJAX Request failed. HTTP Status: "+transport.status)},ajaxRequestThrownException:function(e){Widget.log("Client","AJAX request exception")
setTimeout(this.processAjaxRequestException.bind(this,e),0)},processAjaxRequestException:function(e){this.setPendingAJAXRequest(null)
this.notifyObservers("requestFailed","AJAX Request thrown exception: "+e)},createResponseArea:function(){var responseArea=document.createElement("div")
var responseAreaElement=Element.extend(responseArea)
responseAreaElement.setStyle({display:'none'})
responseAreaElement.setStyle({visibility:'hidden'})
document.body.appendChild(responseArea)
return responseAreaElement},getResponseArea:function(){if(!this.responseArea){this.responseArea=this.createResponseArea()}
return this.responseArea},setPendingAJAXRequest:function(request){this.pendingAJAXRequest=request
this.notifyObservers("isBusyChanged")
this.notifyObservers("canInterruptRequestChanged")}})
Widget.Response.Error=new Class.define({initialize:function(message){this._widgetResponseErrorToken=true
this.message=message},getMessage:function(){return this.message}})