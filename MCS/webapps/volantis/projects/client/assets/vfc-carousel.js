
Widget.Swapable={delay:3,carWidth:0,carHeight:0,maxNodeSize:0,effectState:'idle',storage:null,removeOldContent:false,moveItemToView:function(storage,view,next){if(next){var newNode=storage.removeChild(storage.firstChild);this.setElementOppositeSize(newNode,this.maxNodeSize);view.appendChild(newNode);return newNode;}else{var newNode=storage.removeChild(storage.lastChild);this.setElementOppositeSize(newNode,this.maxNodeSize);view.insertBefore(newNode,view.firstChild);return newNode;}},clearContent:function(element){while(element.firstChild){element.removeChild(element.firstChild);}},addObservers:function(element){Widget.addObserversToFocusableElements(element,Widget.BLUR,this.start.bindAsEventListener(this));return Widget.addObserversToFocusableElements(element,Widget.FOCUS,this.stop.bindAsEventListener(this));},getOrigVisibleProps:function(element){return{visibility:element.style.visibility,display:element.style.display,position:element.style.position};},getSize:function(){return this.maxCarouselHeight;},getElementOppositeSize:function(element){var dimension=Element.getDimensions(element);return dimension.width;},setElementOppositeSize:function(element,size){Element.setStyle(element,{'width':size+'px'});},getElementMainSize:function(element){var dimension=Element.getDimensions(element);return dimension.height;},setElementMainSize:function(element,size){Element.setStyle(element,{'height':size});},setCarouselStyle:function(element){element.setStyle({'height':'auto','width':this.carWidth});},fillSwapBlockContent:function(view,reverse){var ulSize=this.getElementMainSize(view);var overflow=false;var prevDis=Element.getStyle(this.effectElement,'display');this.effectElement.setStyle({'visibility':'hidden','display':'block'});while(!overflow)
{if(this.storage.hasChildNodes()){var addedNode=this.moveItemToView(this.storage,view,reverse);ulSize=this.getElementMainSize(view);if(ulSize>this.getSize()){overflow=true;if(reverse){this.putAsFirstToStorage(view,this.storage,addedNode)}else{this.putAsLastToStorage(view,this.storage,addedNode);}}}else{overflow=true;}}
if(!view.hasChildNodes()){if(this.storage.hasChildNodes()){this.moveItemToView(this.storage,view,reverse);}}
Element.setStyle(this.effectElement,{'visibility':'inherit','display':prevDis});},putAsFirstToStorage:function(view,storage,element){var removedElement=view.removeChild(element);storage.insertBefore(removedElement,storage.firstChild);},putAsLastToStorage:function(view,storage,element){var removedElement=view.removeChild(element);storage.appendChild(removedElement);},moveItemsToStorage:function(src,dest,show){while(src.firstChild){if(show){size=this.getElementOppositeSize(src.firstChild);if(size>this.maxNodeSize){this.maxNodeSize=size;}
Element.setStyle(src.firstChild,{'display':'block'});}
var newElement=src.removeChild(src.firstChild);dest.appendChild(newElement);}},getRealItemElement:function(el){return el.firstChild.firstChild;},clearWhitespaces:function(el){Element.cleanWhitespace(el);}};Object.extend(Widget.Swapable,Widget.Appearable);Object.extend(Widget.Swapable,Widget.Disappearable);Widget.CarouselSwap=Class.create();Object.extend(Widget.CarouselSwap.prototype,{swapable:{},initialize:function(swapable){this.swapable=swapable;this.realElement=swapable.realElement;this.swapStopped=false;this.swapable.effectState='idle';this.timeoutID=setTimeout(this.doSwap.bind(this,true),1000*this.swapable.delay);},doSwap:function(nextPage){var sw=this.swapable;var visible=sw.element.isVisible()
if(!visible){this.timeoutID=setTimeout(this.doSwap.bind(this,nextPage),1000*sw.delay);sw.effectState='idle';return;}
if(this.swapStopped)return;if(sw.effectState=='idle')
{this.doDisapearApear(nextPage);}
if(sw.effectState=='finished')
{this.timeoutID=setTimeout(this.doSwap.bind(this,nextPage),1000*sw.delay);sw.effectState='idle';}},stop:function(event){this.swapStopped=true;if(this.timeoutID!='undefined')
clearTimeout(this.timeoutID);this.timeoutID='undefined';},start:function(event){if(!this.swapStopped)
return;this.swapStopped=false;this.timeoutID=setTimeout(this.doSwap.bind(this,true),1000);},changePageInternal:function(finalState,nextPage){if(this.swapable.effectState!='idle')return;this.swapable.effectState='running';Widget.TransitionFactory.createDisappearEffect(this.swapable.effectElement,this.swapable,{swapable:this.swapable,effectElement:this.swapable.effectElement,afterFinish:function(effect){if(this.swapable.element.isVisible()){var liNodes=this.swapable.realElement.childNodes.length
if(this.swapable.removeOldContent){this.swapable.clearContent(this.swapable.realElement);this.swapable.removeOldContent=false;}else{this.swapable.moveItemsToStorage(this.swapable.realElement,this.swapable.storage,false);}
this.swapable.fillSwapBlockContent(this.swapable.realElement,nextPage);}
Widget.TransitionFactory.createAppearEffect(this.effectElement,this.swapable,{swapable:this.swapable,effectElement:this.swapable.effectElement,afterFinish:function(effect){this.swapable.unsetFocus(this.swapable.element,this.swapable.stop.bindAsEventListener(this.swapable),this.swapable.start.bindAsEventListener(this.swapable));this.swapable.setFocus(this.swapable.element,this.swapable.stop.bindAsEventListener(this.swapable),this.swapable.start.bindAsEventListener(this.swapable));this.swapable.effectState=finalState;}});}});},doDisapearApear:function(nextPage){switch(this.swapable.effectState)
{case'idle':this.changePageInternal('finished',nextPage);setTimeout(this.doDisapearApear.bind(this,nextPage),20);break;case'running':setTimeout(this.doDisapearApear.bind(this,nextPage),20);break;case'finished':setTimeout(this.doSwap.bind(this,nextPage),20);break;}}});Widget.Carousel=Class.define(Widget.Swapable,Widget.Refreshable,Widget.Focusable,{height_properties:['padding-top','padding-bottom','border-bottom-width','border-top-width'],width_properties:['padding-right','padding-left','border-right-width','border-left-width'],initialize:function(id,options){this.id=id;this.element=$(id);this.clearWhitespaces(this.element);Object.copyFields(this,options||{});this.excludeEffects();this.startRefresh();this.setup();this.doSwap();},excludeEffects:function(){if(Prototype.operaMobile()){var excludedEffectsList=new Array();excludedEffectsList['puff']='none';if(excludedEffectsList[this.disappearEffect]!=undefined){this.disappearEffect=excludedEffectsList[this.disappearEffect];}
if(excludedEffectsList[this.appearEffect]!=undefined){this.appearEffect=excludedEffectsList[this.appearEffect];}}},getInitialCarouselDimensions:function(){var dims=this.element.getDimensions();this.maxCarouselWidth=dims.width-
this.getPaddingAndBorder(this.element,this.width_properties)
this.maxCarouselHeight=dims.height-
this.getPaddingAndBorder(this.element,this.height_properties)
if(this.maxCarouselHeight==0){this.initialSize=false}else{this.initialSize=true}},setup:function(){this.effectElement=this.element.firstChild;this.realElement=this.getRealItemElement(this.element);this.clearWhitespaces(this.realElement);this.storage=this.realElement.cloneNode(false);this.moveItemsToStorage(this.realElement,this.storage,true);this.getInitialCarouselDimensions();if(this.initialSize){this.prepareBlockSwapLayout(true);}else{while(this.storage.hasChildNodes()){this.moveItemToView(this.storage,this.realElement,true);}
this.fixCarouselDimensions();}},doSwap:function(){this.carouselEffect=new Widget.CarouselSwap(this);},nextPage:function(){if(this.carouselEffect.swapable.effectState=='running'){return;}
if(this.carouselEffect.timeoutID!='undefined'){clearTimeout(this.carouselEffect.timeoutID);}
this.carouselEffect.changePageInternal('finished',true)
setTimeout(this.startTimeout.bind(this),1000*this.carouselEffect.swapable.delay);},previousPage:function(){if(this.carouselEffect.swapable.effectState=='running'){return;}
var swapable=this.carouselEffect.swapable
if(this.carouselEffect.timeoutID!='undefined'){clearTimeout(this.carouselEffect.timeoutID);}
this.carouselEffect.changePageInternal('finished',false)
setTimeout(this.startTimeout.bind(this),1000*this.carouselEffect.swapable.delay);},startTimeout:function(){switch(this.carouselEffect.swapable.effectState)
{case'running':setTimeout(this.startTimeout.bind(this),20);break;case'finished':this.carouselEffect.swapable.effectState='idle'
this.carouselEffect.timeoutID=setTimeout(this.carouselEffect.doSwap.bind(this.carouselEffect,true),1000*this.carouselEffect.swapable.delay);break;}},prepareBlockSwapLayout:function(pageNext){this.fillSwapBlockContent(this.realElement,pageNext);var counter=this.addObservers(this.element)
this.setFocus(this.element,this.stop.bindAsEventListener(this),this.start.bindAsEventListener(this));},getPaddingAndBorder:function(element,propertiesArray){var result=0;var i=0;for(i=0;i<propertiesArray.length;i++){var v=parseInt(this.element.getStyle(propertiesArray[i]));if(v>0){result+=v;}}
return result;},fixCarouselDimensions:function(){if(!this.initialSize){var carouselDimension=this.element.getDimensions();carouselDimension.height+=this.getPaddingAndBorder(this.element,this.height_properties);this.maxCarouselHeight=carouselDimension.height;this.setElementMainSize(this.element,carouselDimension.height+'px');}},processAJAXResponse:function(originalRequest){this.setContentInternal(originalRequest.responseText);},setContentInternal:function(htmlText){var tmpStorage=document.createElement('div');tmpStorage.innerHTML=htmlText;this.clearWhitespaces(tmpStorage);this.setContentElementsInternal(tmpStorage.childNodes)},setContentElementsInternal:function(elements){var wasStopped=this.carouselEffect.swapStopped;this.stop();var tmpStorage=document.createElement('div');tmpStorage.style.visibility='hidden';for(var i=0;i<elements.length;i++){tmpStorage.appendChild(elements[i])}
this.element.appendChild(tmpStorage);this.element.removeChild(this.element.lastChild);this.clearContent(this.storage);this.moveItemsToStorage(tmpStorage,this.storage,true);this.removeOldContent=true;if(!wasStopped)
{this.start();}},setContent:function(htmlItems){contentInternal=""
for(var i=0;i<htmlItems.length;i++){contentInternal+="<li>"+htmlItems[i]+"</li>"}
this.setContentInternal(contentInternal)},setContentNow:function(htmlItems){this.setContent(htmlItems)},setContentElements:function(elements){var liElements=[]
for(var i=0;i<elements.length;i++){var liElement=document.createElement("li")
liElement.appendChild(elements[i])
liElements.push(liElement)}
this.setContentElementsInternal(liElements)},setContentElementsNow:function(elements){this.setContentElements(elements)},stop:function(){this.carouselEffect.stop();},start:function(){this.carouselEffect.start();}});