
var Gallery={createItems:function(options){var items=new Gallery.Items(options)
if(this.defaultItems===undefined){this.defaultItems=items}else if(this.defaultItems!=null){this.defaultItems=null}
return items},getItems:function(reference){var items=null
if(reference==null){items=this.defaultItems}else if(typeof(reference)=='string'){items=Widget.getInstance(reference)}else{items=reference}
return items},createGallery:function(options){var displays=options.displays
for(var index=0;index<displays.length;index++){displays[index]=Widget.getInstance(displays[index])}
return new Gallery.Gallery(this.getItems(options.items),displays,options)},createSlideshow:function(options){var display=options.display
if(display!=null){display=Widget.getInstance(display)}
return new Gallery.Slideshow(this.getItems(options.items),display,options)},getItemsRequest:function(){return this.itemsRequest}}
Gallery.Slide=Class.define(Widget.OptionsContainer,{duration:5,initialize:function(content,options)
{this.installOptions(options)
this.content=content},getContent:function(){return this.content},getDuration:function(){return this.duration}})
Gallery.Item=Class.define(Widget.OptionsContainer,{initialize:function(summarySlide,detailSlide,options)
{this.installOptions(options)
this.summarySlide=summarySlide
this.detailSlide=detailSlide},getSummarySlide:function(){return this.summarySlide},getDetailSlide:function(){return this.detailSlide}})
Gallery.InfiniteArray=Class.define(Widget.OptionsContainer,{initialize:function(options){this.installOptions(options)
this.storage={}},get:function(index){var element=this.storage[index.toString()]
return element},put:function(index,element){this.storage[index.toString()]=element},remove:function(index){delete this.storage[index.toString()]}})
Gallery.Items=Widget.define({loadURL:null,loadOnDemand:false,initialize:function(options){this.initializeWidget(null,options)
this.storage=new Gallery.InfiniteArray()
if(this.loadURL==null){this.size=0
var items=this.getOption('items')
if(items!=null){for(var i=0;i<items.length;i++){this.storage.put(i+1,items[i])
this.size++}}}else{this.size=null
if(!this.loadOnDemand){this.sendImmediateRequest()}}},isReady:function(){return this.size!=null},setSize:function(size){this.size=size
this.notifyObservers("sizeChanged")},putItem:function(index,item){this.storage.put(index,item)
this.notifyObservers("itemPut",index)},sendImmediateRequest:function(){new Gallery.ItemsRequest(this.loadURL,{onSuccess:this.immediateRequestSucceeded.bind(this),onFailure:this.immediateRequestFailed.bind(this)})},immediateRequestSucceeded:function(items,size){this.setSize(size)
itemsCount=items.length
itemsCount=Math.min(itemsCount,this.size)
for(var index=0;index<itemsCount;index++){this.putItem(index+1,items[index])}},immediateRequestFailed:function(){},requestItem:function(index){this.requestItems(index,index)},requestItems:function(from,to){if(this.loadURL==null||!this.loadOnDemand){return}
from=Math.max(from,1)
to=(this.size!=null)?Math.min(to,this.size):to
if(from>to)return
var requestFrom=from
while(requestFrom<=to){if(this.storage.get(requestFrom)!=null){requestFrom++}else{var requestTo=requestFrom
while(requestTo<to&&this.storage.get(requestTo+1)==null){requestTo++}
this.sendDemandRequest(requestFrom,requestTo)
requestFrom=requestTo+2}}},sendDemandRequest:function(from,to){this.markItemsRequested(from,to)
new Gallery.ItemsRequest(this.loadURL,{from:from,to:to,onSuccess:this.demandRequestSucceeded.bind(this,from,to),onFailure:this.demandRequestFailed.bind(this,from,to)})},demandRequestSucceeded:function(from,to,items,size){this.setSize(size)
to=Math.min(to,this.size)
itemsIndex=0
storageIndex=from
while((itemsIndex<=items.length)&&(storageIndex<=to)){this.putItem(storageIndex,items[itemsIndex])
itemsIndex++
storageIndex++}
this.unmarkItemsRequested(from,to)},demandRequestFailed:function(from,to){this.unmarkItemsRequested(from,to)},markItemsRequested:function(from,to){for(var index=from;index<=to;index++){this.storage.put(index,'requesting')}},unmarkItemsRequested:function(from,to){for(var index=from;index<=to;index++){if(this.storage.get(index)=='requesting'){this.storage.remove(index)}}},getItem:function(index){var item=this.storage.get(index)
if(item=='requesting'){item=null}
return item},getSize:function(){return(this.size!=null)?this.size:0}})
Gallery.Response=Class.define({initialize:function(items,count){this.items=items
this.count=count},getItems:function(){return this.items},getCount:function(){return this.count}})
Gallery.ItemsRequest=Class.define(Widget.OptionsContainer,{from:1,to:null,onSuccess:null,onFailure:null,initialize:function(url,options){this.installOptions(options)
this.url=url
this.items=[]
this.createAJAXRequest()},createAJAXRequest:function(){var parameters=null
if((this.from!=null)||(this.to!=null)){parameters={}
if(this.from!=null)parameters['mcs-start']=this.from
if(this.to!=null)parameters['mcs-end']=this.to
parameters=$H(parameters).toQueryString()}
return new Widget.AjaxRequest(this.url,{parameters:parameters,method:"get",onSuccess:this.ajaxRequestSucceeded.bind(this),onFailure:this.ajaxRequestFailed.bind(this),onException:this.ajaxRequestFailed.bind(this)})},ajaxRequestSucceeded:function(request){var responseText=request.responseText
this.getResponseArea().innerHTML=responseText.stripScripts()
Gallery.itemsRequest=this
responseText.evalScripts()
Gallery.itemsRequest=null
this.requestSucceeded()},ajaxRequestFailed:function(){},requestSucceeded:function(){if(this.onSuccess!=null){this.onSuccess(this.response.getItems(),this.response.getCount())}},requestFailed:function(){if(this.onFailure!=null){this.onFailure()}},setResponse:function(response){this.response=response},createResponseArea:function(){var responseArea=document.createElement("div")
var responseAreaElement=Element.extend(responseArea)
responseAreaElement.setStyle({display:'none'})
responseAreaElement.setStyle({visibility:'hidden'})
document.body.appendChild(responseArea)
return responseAreaElement},getResponseArea:function(){if(!this.responseArea){this.responseArea=this.createResponseArea()}
return this.responseArea}})
Gallery.ItemDisplay=Widget.define({item:null,initialize:function(block,mode,options){this.initializeWidget(null,options)
this.block=block
this.mode=mode
this.updateBlock()
this.observe(this.block,"contentShown","contentShown")
this.observe(this.block,"contentHidden","contentHidden")
this.observe(this.block,"displayedContentChanged","displayedContentChanged")},setItem:function(item){if(this.item!==item){this.item=item
this.updateBlock()}},updateBlock:function(){if(this.item==null){this.block.setContent(null)}else{if(this.mode=='summary'){this.block.setContent(this.item.getSummarySlide().getContent())}else{this.block.setContent(this.item.getDetailSlide().getContent())}}},getItem:function(){return this.item},getDisplayedItem:function(){return this.displayedItem},setMode:function(mode){if(this.mode!=mode){this.mode=mode
this.updateBlock()}},getMode:function(){return this.mode},contentShown:function(){this.notifyObservers('itemAppeared')},contentHidden:function(){this.notifyObservers('itemDisappeared')},displayedContentChanged:function(){this.displayedItem=this.item
this.notifyObservers('displayedItemChanged')},getElement:function(){return this.block.getElement()}})
Gallery.ItemDisplayController=Widget.define({autoRequest:true,initialize:function(items,display,options){this.initializeWidget(null,options)
this.items=items
this.display=display
this.index=null
this.observe(this.items,"itemPut","itemPut")
this.updateDisplay()},setItem:function(index){this.index=index
if(index==null||index<1||index>this.items.getSize()){this.updateDisplay()}else{var item=this.items.getItem(this.index)
if(item!=null){this.updateDisplay()}else{if(this.autoRequest){this.items.requestItem(index)}
this.updateDisplay()}}},itemPut:function(index){if(this.index==index){this.updateDisplay()}},updateDisplay:function(){var item=null
if(this.index!=null){item=this.items.getItem(this.index)}
this.display.setItem(item)}})
Gallery.Gallery=Widget.define({items:null,displays:null,slideshow:null,slideshowPopup:null,slideshowLaunchDelay:null,initialize:function(items,displays,options){this.initializeWidget(null,options)
this.items=items
this.index=1
this.pageNumber=1
this.observe(this.items,"sizeChanged","itemsSizeChanged")
this.controllers=[]
for(var i=0;i<displays.length;i++){this.controllers[i]=new Gallery.ItemDisplayController(this.items,displays[i],{autoRequest:false})}
for(var i=0;i<displays.length;i++){var button=new Widget.Internal.Button(displays[i].getElement())
this.observe(button,"pressed","sendToSlideshow",i+1)}
this.scheduleSlideshowLaunch()
this.update()
this.addAction('next')
this.addAction('previous')
this.addProperty('start-item-number')
this.addProperty('end-item-number')
this.addProperty('items-count')
this.addProperty('page-number')
this.addProperty('pages-count')},itemsSizeChanged:function(){this.notifyChanges()},notifyChanges:function(){this.notifyObservers('startItemNumberChanged')
this.notifyObservers('endItemNumberChanged')
this.notifyObservers('itemsCountChanged')
this.notifyObservers('pageNumberChanged')
this.notifyObservers('pagesCountChanged')
this.notifyObservers('canNextChanged')
this.notifyObservers('canPreviousChanged')},previous:function(){if(this.canPrevious()){this.skipSlideshowLaunch()
this.index-=this.controllers.length
this.pageNumber--
this.update()
this.notifyChanges()}},canPrevious:function(){return this.getStartItemNumber()>1},next:function(){if(this.canNext()){this.skipSlideshowLaunch()
this.index+=this.controllers.length
this.pageNumber++
this.update()
this.notifyChanges()}},canNext:function(){return this.getEndItemNumber()<this.getItemsCount()},getWidget:function(idOrWidget){if(typeof(idOrWidget)=='string'){return Widget.getInstance(idOrWidget)}else{return idOrWidget}},getSlideshow:function(){return this.getWidget(this.slideshow)},getSlideshowPopup:function(){if(this.popupWidget==null){this.popupWidget=this.getWidget(this.slideshowPopup);var slideshow=this.getSlideshow();}
this.observe(this.popupWidget,"dismissed","stop");return this.popupWidget},getPageNumber:function(){return this.pageNumber},getPageSize:function(){return this.controllers.length},getPagesCount:function(){if(this.getPageSize()!=0){return Math.ceil(this.getItemsCount()/this.getPageSize())}},getStartItemNumber:function(){return this.index},getEndItemNumber:function(){return Math.min(this.index+this.controllers.length-1,this.items.getSize())},getItemsCount:function(){return this.items.getSize()},sendToSlideshow:function(itemNumber){this.skipSlideshowLaunch()
var slideshow=this.getSlideshow()
if(slideshow!=null){var index=this.index+itemNumber-1
if(index<=this.items.getSize()){var popup=this.getSlideshowPopup()
if(popup!=null){popup.show()}
setTimeout(slideshow.gotoItem.bind(slideshow,this.index+itemNumber-1),100)}}},launchSlideshow:function(){var slideshow=this.getSlideshow()
if(slideshow!=null){var popup=this.getSlideshowPopup()
if(popup!=null){popup.show()}
slideshow.play()}},scheduleSlideshowLaunch:function(){if(this.slideshowLaunchDelay!=null){this.slideshowTimer=setTimeout(this.launchSlideshow.bind(this),this.slideshowLaunchDelay*1000)}},skipSlideshowLaunch:function(){if(this.slideshowTimer!=null){this.slideshowTimer=clearTimeout(this.slideshowTimer)}},update:function(){this.items.requestItems(this.index,this.index+this.controllers.length-1)
for(var i=0;i<this.controllers.length;i++){this.controllers[i].setItem(this.index+i)}}})
Gallery.Slideshow=Widget.define({items:null,display:null,order:'normal',repetitions:1,autoPlay:true,initialize:function(items,display,options){this.initializeWidget(null,options)
this.items=items
this.observe(this.items,"itemPut","itemsChanged")
this.observe(this.items,"sizeChanged","itemsChanged")
this.display=display
this.controller=new Gallery.ItemDisplayController(items,display)
this.playing=false
this.index=null
this.displayedIndex=null
this.slideCounter=null
this.observe(display,"itemAppeared","displayItemAppeared")
this.update()
if(this.autoPlay){this.play();}
this.addAction('next')
this.addAction('previous')
this.addAction('play')
this.addAction('pause')
this.addAction('stop')
this.addProperty('item-number')
this.addProperty('items-count')},notifyChanges:function(){this.notifyObservers('canNextChanged')
this.notifyObservers('canPreviousChanged')
this.notifyObservers('canPlayChanged')
this.notifyObservers('canPauseChanged')
this.notifyObservers('canStopChanged')
this.notifyObservers('itemNumberChanged')
this.notifyObservers('itemsCountChanged')},stop:function(){if(this.canStop()){this.gotoItem(null)}},canStop:function(){return this.index!=null},play:function(){if(this.canPlay()){this.playing=true
if(this.items.isReady()){this.doPlay()}else{}
this.notifyChanges()}},doPlay:function(){if(this.repetitions!='infinite'){this.slideCounter=this.getItemsCount()*this.repetitions}else{this.slideCounter=null}
this.nextSlide()},canPlay:function(){return!this.playing},pause:function(){if(this.canPause()){this.playing=false
this.slideCounter=null
this.timer=clearTimeout(this.timer)
this.notifyChanges()}},canPause:function(){return this.playing},previous:function(){if(this.canPrevious()){this.pause()
this.index-=1
this.update()}},canPrevious:function(){return(this.index!=null)&&(this.index>1)},next:function(){if(this.canNext()){this.pause()
this.index+=1
this.update()}},canNext:function(){return(this.index!=null)&&(this.index<this.getItemsCount())},gotoItem:function(index){if(this.isIndexValid(index)){this.pause()
this.index=index
this.update()}},isIndexValid:function(index){return(index==null)||((index>=1)&&(index<=this.getItemsCount()))},nextSlide:function(){var nextIndex=this.nextSlideIndex()
if(nextIndex==this.index){this.pause()}else{this.index=nextIndex
if(this.slideCounter!=null){this.slideCounter--}
this.nextRandomIndex=null
this.update()
this.requestNextSlide()}},requestNextSlide:function(){var nextItem=this.nextSlideIndex()
if(nextItem!=null){this.items.requestItem(nextItem)}},nextSlideIndex:function(){var currentIndex=this.index
if(this.getItemsCount()==0){nextIndex=null}else if(this.slideCounter==0){nextIndex=currentIndex}else{if(this.order=='normal'){if(currentIndex==null){nextIndex=1}else{nextIndex=currentIndex+1
if(nextIndex>this.getItemsCount()){nextIndex=1}}}else if(this.order=='reverse'){if(currentIndex==null){nextIndex=this.getItemsCount()}else{nextIndex=currentIndex-1
if(nextIndex<1){nextIndex=this.getItemsCount()}}}else if(this.order=='random'){if(this.nextRandomIndex==null){this.nextRandomIndex=Math.max(Math.ceil(Math.random()*this.getItemsCount()),1)}
nextIndex=this.nextRandomIndex}}
return nextIndex},isPlaying:function(){return this.playing},getItemNumber:function(){return this.displayedIndex},getItemsCount:function(){return this.items.getSize()},update:function(){if(this.index==null){if(this.playing&&this.items.isReady()){this.doPlay()
return}}
this.controller.setItem(this.index)
this.displayedIndex=this.index
this.notifyChanges()},displayItemAppeared:function(){if(this.playing){this.scheduleNextSlide()}},getInterval:function(){var item=this.display.getItem()
var interval=5
if(item!=null){var duration=item.getDetailSlide().getDuration()
if(duration!='normal'){interval=duration}}
return interval},scheduleNextSlide:function(){this.timer=setTimeout(this.nextSlide.bind(this),this.getInterval()*1000)},itemsChanged:function(){this.update()}})