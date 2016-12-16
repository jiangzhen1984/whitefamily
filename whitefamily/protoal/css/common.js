$(document).ready(function () {
	var currentPage = $("body").attr("id");

	$(".navigationWrapper .nav_main1 .link").removeClass('active');
	$(".navigationWrapper .nav_main1 .link#" + currentPage).addClass("active");
	if (currentPage == "contact"){
		$(".navigationWrapper .nav_main1 .link").each(function(i,v){
			$(v).addClass("black");
		});
	}

	$( "#tnc, #wechat, #privacy, .btnright, #ab1" ).hover(function() {
		$( this ).css( 'cursor', 'pointer' );
	});

	$( "#tnc, #wechat, #privacy ,#ab1, #who, #zhici, #linian, #biaodan" ).click(function() {
		callOL($(this).attr("id"));
	});

	$( ".btnright" ).click(function() {
		$( ".dim" ).fadeOut( "1000" );
	});


	function callOL(showOL){
		var realOL = "#overlay"+showOL;
		$( realOL ).fadeIn( "1000", function() {
		});
	}


});
