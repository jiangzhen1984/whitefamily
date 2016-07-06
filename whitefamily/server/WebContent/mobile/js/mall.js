function popupDialog(cu,ge) {

    var $mask = $("<div>")
    $mask.addClass("mask js-mask").css({
        'height': $(document).height(),
        'width': $(document).width()
    });
    $mask.appendTo($("body"));
    var tmpl = "" +
        "<div>" +
        "  <div class='dialog-head' style='text-align:center;font-weight:bold;margin-bottom:5px;height:30px;line-height:30px;'>" +
        "   <span></span>" +
        "  </div>" +
        "  <div class='dialog-body p10'>";
    if (cu!=null && cu!='') {
        tmpl = tmpl + "<p style='line-height: 28px;padding: 5px;'><span style='margin-left:10px;'>" + cu + "</span></p>";
    }
    if (ge!=null && ge!='') {
        tmpl = tmpl + "<p style='line-height: 28px;padding: 5px;'><span style='margin-left:10px;'>" + ge + "</span></p>";
    }
    tmpl=tmpl+ "  </div>" +
        "  <div class='dialog-footer' style='table-layout: fixed;display: table;border-collapse: collapse;margin: 10px 0 0 0;width: 100%;'>" +
        "    <a class='btn-dialog-left js-cancel'>知道了</a>" +
        "  </div>" + "</div>";
    var $dialog = $(tmpl);
    var top = ($("body").height() - 300) / 2;
    $dialog.addClass('dialog js-dialog').css({
        width: $(document).width() - 20,
        left: "10px",
        top: top + "px"
    });
    $dialog.appendTo($("body"));
    var $select = $dialog.find(".js-problem-type");
    var $trQuantity = $dialog.find(".js-quantity-tr");
    $select.change(function () {
        var $this = $(this);
        if ($this.val() == '3') {
            $trQuantity.removeClass('hide');
        } else {
            $trQuantity.addClass('hide');
        }
    });
    var $cancel = $dialog.find(".js-cancel");
    var $sure = $dialog.find(".js-sure");
    $cancel.click(function () {
        $("body").find(".js-mask").remove();
        $("body").find(".js-dialog").remove();
    });
}




(function() {
	
    $(".js-plus").click(function () { 
        var $tr = $(this).closest(".calculate");
        var $gid =  $(this).attr("gid");
        $.ajax({
        	url: 'mall.ajax?action=cartAction&subaction=plus&goodscount=1&goodsid='+$gid,
        	success :function(data) {
        		$(".js-item-count").html(data.itemcount);
        		popupDialog('添加到进货清单',null)
        	},
        });
        $(".selected-info-box").show();
       
    });
    
    $(".js-minus").click(function () { 
        var $tr = $(this).closest(".calculate");
        var $gid =  $(this).attr("gid");
        $.ajax({
        	url: 'mall.ajax?action=cartAction&subaction=minus&goodscount=1&goodsid='+$gid,
        	success :function(data) {
        		$(".js-item-count").html(data.itemcount);
        	},
        });
        $(".selected-info-box").show();
       
    });
    

})();

