
$(function() {
    $(".kfleft").click(function(){
		var i=$("#haiiskefu").css("right");
		if (i=='0px'){
			$('#haiiskefu').animate({right:-351}, 200);
		} else {
			$('#haiiskefu').animate({right:0}, 200);
		}
	});
});
