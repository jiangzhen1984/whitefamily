// JavaScript Document
jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    return this;
}


var Page = {Object: {}, Static: { browserH:0, broswerW:0 }, animationSpeed : 1200, fadeInSpeed: 2000, cycleSpeed : 1000 };

var SectionInfo = {"section1" : [1], "section2" : [1,2,3,4], "section3" :[1,2,3,4], "section4" : [1,2,3,4], "section5" : [1,2,3],  "section6" : [1,2,3]};

Page.Object.BeforeAnimation = function(objSection){
	$(".maskWrapper", objSection).center();
	objSection.css('background-image', 'none');
	$(".vdo-container", objSection).fadeOut(10);
	$(".maskWrapper", objSection).show();
}

Page.Object.CircleSize1 = 322;
Page.Object.CircleSize2 = 836;



Page.Object.StartAnimation = function(obj){
	
	var objId = $(obj).attr("id");

	$(obj).queue("animation", function(){
		Page.Object.CenterInfo(obj);
		var self = this;
	  	setTimeout(function() {
	    	$(self).dequeue("animation");
	  	}, Page.fadeInSpeed);
	});


	$("#next").removeClass().addClass("Slide_" + objId);
	$("#prev").removeClass().addClass("Slide_" + objId);
	switch(objId){
		case "section1":
			$("#next").fadeIn(Page.fadeInSpeed);
			$("#prev").hide();
			break;
		case "section6":
			$("#next").hide();
			$("#prev").fadeIn(Page.fadeInSpeed);	
			break;
		default:
			$("#next").fadeIn(Page.fadeInSpeed);
			$("#prev").fadeIn(Page.fadeInSpeed);
			break;
	}

	$(obj).queue("animation", function(){
		Page.Object.EndLine(objId, false);
		var self = this;
	  	setTimeout(function() {
	    	$(self).dequeue("animation");
	  	}, Page.animationSpeed);
	});
	
	$(obj).queue("animation", function(){
		Page.Object.AfterEndLine(obj);
		var self = this;
	  	setTimeout(function() {
	    	$(self).dequeue("animation");
	  	}, Page.animationSpeed);
	});
	

	$(obj).dequeue("animation");
	
}

Page.Object.PlayBgVdo = function(objId){
	$("#" + objId + " .vdo-container").show();
	$(".vdo-container video").removeClass('play');
	switch(objId){
		case "section1":
			$("#bgvdo1").addClass('play');
			$("#bgvdo1")[0].pause();
			$("#bgvdo1")[0].currentTime = '0';
			$("#bgvdo1")[0].play();
			break;
		case "section2":
			$("#bgvdo2").addClass('play');
			$("#bgvdo2")[0].pause();
			$("#bgvdo2")[0].currentTime = '0';
			$("#bgvdo2")[0].play();
			$("#bgvdo2").bind("ended", function() {
				Page.Object.DisplayPanel.next();	
			});
			break;
		case "section3":
			$("#bgvdo3").addClass('play');
			$("#bgvdo3")[0].pause();
			$("#bgvdo3")[0].currentTime = '0';
			$("#bgvdo3")[0].play();
			$("#bgvdo3").bind("ended", function() {
				Page.Object.DisplayPanel.next();	
			});
			break;
		case "section4":
			$("#bgvdo4").addClass('play');
			$("#bgvdo4")[0].pause();
			$("#bgvdo4")[0].currentTime = '0';
			$("#bgvdo4")[0].play();
			$("#bgvdo4").bind("ended", function() {
				Page.Object.DisplayPanel.next();	
			});
			break;
		case "section5":
			$("#bgvdo5").addClass('play');
			$("#bgvdo5")[0].pause();
			$("#bgvdo5")[0].currentTime = '0';
			$("#bgvdo5")[0].play();
			$("#bgvdo5").bind("ended", function() {
				Page.Object.DisplayPanel.next();
			});
			break;
		case "section6":
			$("#bgvdo6").addClass('play');
			$("#bgvdo6")[0].pause();
			$("#bgvdo6")[0].currentTime = '0';
			$("#bgvdo6")[0].play();
			break;
	}
}


Page.Object.CenterInfo = function(obj){
	var objId = $(obj).attr("id");
	switch(objId){
		case "section1":
			break;
		case "section2":
		case "section3":
		case "section4":
		case "section5":
		case "section6":
			
			$(".NonCircleTextViewPort .relativePanel div", obj).hide();
			$(".NonCircleTextViewPort .relativePanel div", obj).each(function(i, v) {
                $(v).delay(i*Page.fadeInSpeed).fadeIn(Page.fadeInSpeed);
			});
			
			break;
	}

}

Page.Object.EndLine = function(currentSection, isStatic){

	var startPoint = 0;
	var lineH = 0;
	var haveCircle = false;
	
	var targetHeight = -1
	var diff = 0;
	var endFrame = 0;
	switch (currentSection){
		case "section1":
			startPoint = (Page.Static.browserH / 2) + (Page.Object.CircleSize1 / 2);
			lineH = 78;
			$(".endDot", "#" + currentSection).css({"top":startPoint+lineH});
			break;
	}

	if (isStatic == false){
		$(".dotline.end", "#" + currentSection).css({"top":startPoint}).animate({"height":lineH}, 1000);
	}else{
		$(".dotline.end", "#" + currentSection).css({"top":startPoint, "height":lineH});
	}
}

Page.Object.AfterEndLine = function(obj){
	var objId = $(obj).attr("id");
	switch(objId){
		case "section1":
			$(obj).find(".endDot").delay(Page.animationSpeed).fadeIn(Page.fadeInSpeed);
			$(obj).find(".text1").delay(Page.animationSpeed).fadeIn(Page.fadeInSpeed);
			$(obj).find(".text2").delay(Page.animationSpeed).fadeIn(Page.fadeInSpeed);
			break;
	}
}


Page.Object.resetBroswerWH = function(){
	Page.Static.broswerW = $(".mainContent").width();
	Page.Static.browserH = $(".mainContent").height();
	Page.Object.EndLine($(".section.cycle-slide-active").attr("id"), true);
	var diff = Page.Static.browserH - $(".CircleCtrl.w1056").height();
	if (diff < 0){
		$(".CircleCtrl.w1056").css({top:diff/2, bottom:"auto"});
	}
	$(".relativePanel").each(function(i,v){
		$(v).parent().height($(v).height());
	})
	var diff2 = (Page.Static.broswerW/2) - ($(".CircleCtrl.w1056").width() / 2);
	diff2 = Math.round(diff2);
	$(".naviControlWrapper").css({left:diff2, top:0});
	var tmp2 = $(".naviControlWrapper .naviControl").height();
	$(".naviControlWrapper").height(tmp2);
}

Page.Object.GoPrevious= function(){
	/*if ($("#prev").is(":visible")){
   		$("#prev").click();
	}*/
};

Page.Object.GoNext = function(){
	/*if ($("#next").is(":visible")){
	 	$("#next").click();
   	}*/
}

Page.Object.DisplayPanel = {
	totalSection: 7,
	keyCssIdPrefix: "section",
	currentDisplaySection:1,
	isTransiting : 0,
	init:function(){
		for (i=1; i<=this.totalSection; i++){
			$("#" + this.keyCssIdPrefix + i).css("z-index", this.totalSection - (i-1));	
			$("#" + this.keyCssIdPrefix + i).attr('data-oImage', $("#" + this.keyCssIdPrefix + i).css('background-image'));
		}
	},
	next:function(){
		var targetSection = this.currentDisplaySection + 1;
		if (targetSection <= this.totalSection && this.isTransiting == 0){
			this.isTransiting = 1;
			var thatCssIdPrefix = this.keyCssIdPrefix;
			var thatisTransiting = this.isTransiting;
			$("." + thatCssIdPrefix).css("z-index", 0);
			$("#" + this.keyCssIdPrefix + this.currentDisplaySection).css("z-index", this.totalSection);
			$($("." + thatCssIdPrefix)[targetSection-1]).css("z-index", this.totalSection-1);
			$(".NonCircleTextViewPort .relativePanel div").hide();
			$(".section").css("visibility", "hidden");
			$("#section" + this.currentDisplaySection).css("visibility", "visible");
			$("#section" + targetSection).css("visibility", "visible");
			
			Page.Object.BeforeAnimation($("#" + this.keyCssIdPrefix + this.currentDisplaySection));
			
			Page.Object.PlayBgVdo(this.keyCssIdPrefix + targetSection);
			
			$("#" + this.keyCssIdPrefix + this.currentDisplaySection).transition({ scale: 0 }, 1500, 'ease', function(){
				Page.Object.StartAnimation($("." + thatCssIdPrefix)[targetSection-1]);
				$(".maskWrapper").hide();
				Page.Object.DisplayPanel.isTransiting = 0;
			});
			$("#" + this.keyCssIdPrefix + targetSection).transition({ scale: 1 }, 0);
			$("#" + this.keyCssIdPrefix + targetSection).css("background-image", $("#" + this.keyCssIdPrefix + targetSection).attr("data-oImage"));
			this.currentDisplaySection = targetSection;
			if (this.currentDisplaySection >= 2){
				$(".naviControlWrapper").css("visibility", "visible");
				$(".naviUL li").removeClass("active");
				$("#navi" + this.currentDisplaySection).addClass("active");
			}else{
				$(".naviUL li").removeClass("active");
				$(".naviControlWrapper").css("visibility", "hidden");
			}
		}
		
	
	},
	reset:function(){
		$(".section").transition({ scale: 1 }, 0);
		for (i=1; i<=this.totalSection; i++){
			$("#" + this.keyCssIdPrefix + i).css("z-index", this.totalSection - (i-1));	
		}
		$(".section").each(function(i, v){
			$(v).css("background-image", $(v).attr("data-oImage"));
		});  
		$(".vdo-container").hide();
		$(".maskWrapper").hide();
		this.currentDisplaySection = 1;
		$(".naviControlWrapper").css("visibility", "hidden");
		this.isTransiting = 0;
		$(".section").css("visibility", "hidden");
		$("#section" + this.currentDisplaySection).css("visibility", "visible");
		Page.Object.PlayBgVdo(this.keyCssIdPrefix + "1");
		Page.Object.StartAnimation($(".section")[0]);
	},
	changeByNum:function(num){
		var targetSection = num
		if (targetSection >= 1 && targetSection <= this.totalSection && targetSection != this.currentDisplaySection && this.isTransiting == 0){
			this.isTransiting = 1;
			var thatCssIdPrefix = this.keyCssIdPrefix;
			var thatisTransiting = this.isTransiting;
			$("." + thatCssIdPrefix).css("z-index", 0);
			$("#" + this.keyCssIdPrefix + this.currentDisplaySection).css("z-index", this.totalSection);
			$($("." + thatCssIdPrefix)[targetSection-1]).css("z-index", this.totalSection-1);
			$(".NonCircleTextViewPort .relativePanel div").hide();
			$(".section").css("visibility", "hidden");
			$("#section" + this.currentDisplaySection).css("visibility", "visible");
			$("#section" + targetSection).css("visibility", "visible");
			
			Page.Object.BeforeAnimation($("#" + this.keyCssIdPrefix + this.currentDisplaySection));
			Page.Object.PlayBgVdo(this.keyCssIdPrefix + targetSection);
			
			$("#" + this.keyCssIdPrefix + this.currentDisplaySection).transition({ scale: 0 }, 1500, 'ease', function(){
				Page.Object.StartAnimation($("." + thatCssIdPrefix)[targetSection-1]);
				$(".maskWrapper").hide();
				Page.Object.DisplayPanel.isTransiting = 0;
			});
			$("#" + this.keyCssIdPrefix + targetSection).transition({ scale: 1 }, 0);
			$("#" + this.keyCssIdPrefix + targetSection).css("background-image", $("#" + this.keyCssIdPrefix + targetSection).attr("data-oImage"));
			this.currentDisplaySection = targetSection;
			if (this.currentDisplaySection >= 2){
				$(".naviControlWrapper").css("visibility", "visible");
				$(".naviUL li").removeClass("active");
				$("#navi" + this.currentDisplaySection).addClass("active");
			}else{
				$(".naviUL li").removeClass("active");
				$(".naviControlWrapper").css("visibility", "hidden");
			}
		}
		
	}
}

$(document).ready(function(){


	$(window).load(function(){
		Page.Object.resetBroswerWH();
		Page.Object.StartAnimation($(".section")[0]);
		
		$("#section1").css("visibility", "visible");
		$("#bgvdo1").addClass('play');
		
		
		$("#bgvdo1").attr("src","vdo/story1.mp4");
		$("#bgvdo2").attr("src","vdo/story2.mp4");
		$("#bgvdo3").attr("src","vdo/story3.mp4");
		$("#bgvdo4").attr("src","vdo/story4.mp4");
		$("#bgvdo5").attr("src","vdo/story6.mp4");
		$("#bgvdo6").attr("src","vdo/story7.mp4");
		
		//var myVideo=document.getElementById("bgvdo1"); 
		//$("#bgvdo1")[0].play()
		Page.Object.PlayBgVdo("section1");
	});
	
	$("#section1").css("visibility", "visible");
	$("#section1").find(".CircleCtrl").fadeIn(Page.fadeInSpeed);
	$(".section").css("visibility", "hidden");

	$(window).resize(function(){
		Page.Object.resetBroswerWH();
	});
	
	$(window).bind('mousewheel DOMMouseScroll', function(event){
		if (event.originalEvent.wheelDelta > 0 || event.originalEvent.detail < 0) {
			// scroll up
			Page.Object.GoPrevious();
		}
	else {
			// scroll down
			Page.Object.GoNext();
	}
	});
	
	Page.Object.DisplayPanel.init();
	// 1. hide all mask DIVs
	$(".maskWrapper").hide();
	
	// 2. set the size of mask and make it center of the browser
	$( ".mySelector" ).css('height', $(window).height()+'px');
	$( ".mySelector" ).css('width', $(window).height()+'px');
	//$( ".mySelector" ).imageMask("img/mask.png");
	
	
	$(".nextBtn").click(function(){
		callunbind();
		Page.Object.DisplayPanel.next();	
	});

	$(".resetBtn").click(function(){
		callunbind();
		Page.Object.DisplayPanel.reset();
	});
	
	$(".naviUL li").click(function(){
		callunbind();
		var tmpId = $(this).attr("id");
		tmpId = tmpId.replace("navi", "");
		tmpId = parseInt(tmpId);
		Page.Object.DisplayPanel.changeByNum(tmpId);
	})
	
	//Page.Object.BeforeAnimation();

});


function callunbind(){
		$("#bgvdo1").unbind( "ended" );
		$("#bgvdo2").unbind( "ended" );
		$("#bgvdo3").unbind( "ended" );
		$("#bgvdo4").unbind( "ended" );
		$("#bgvdo5").unbind( "ended" );
		$("#bgvdo6").unbind( "ended" );
		//alert("unbind done");
}

function detectmob() { 
 if( navigator.userAgent.match(/Android/i)
	 || navigator.userAgent.match(/webOS/i)
	 || navigator.userAgent.match(/iPhone/i)
	 || navigator.userAgent.match(/iPad/i)
	 || navigator.userAgent.match(/iPod/i)
	 || navigator.userAgent.match(/BlackBerry/i)
	 || navigator.userAgent.match(/Windows Phone/i)
 ){
    return true;
  }
 else {
    return false;
  }
}




