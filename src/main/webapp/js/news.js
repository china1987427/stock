var t = 60;
$(function() {
	$('.active ul li').on('click', function() {
		$(this).addClass("on");
		$(this).siblings().removeClass("on");
		var param = $(this).find("a").attr("class");
		if (param == "main") {
			clearInterval(timer);
			$(".refresh_area").hide();
			$(this).parents(".active").siblings().find(".link_news").show();
			$(".scroll_box").hide();
		} else {
			window.timer = setInterval("count()", 1000); // 定义一个定时器
			$(".refresh_area").show();
			$(".scroll_box").show();
			$(this).parents(".active").siblings().find(".link_news").hide();
			if ($("#toScroll").val() != "true") {
				var page = $("#page").val();
				$(".scroll_box").load("news/scrollNews", {
					"page" : page
				}, function() {

				});
				$("#toScroll").val("true");
			}
		}
	});
	$(".refresh_area input").on('click', function() {
		if ($(this).prop("checked")) {
			timer = setInterval("count()", 1000);
		} else {
			clearInterval(timer);
		}
	});
	$(".refresh").on('click', function() {
		var page = $("#page").val();
		page++;
		$("#page").val(page);
		$(".scroll_box").load("news/scrollNews", {
			"page" : page
		}, function() {

		});
	});

	$("#global").on('click', function() {
		$(".showS").hide();
		$(".showG").show();
	});
	$("#showStock").on('click', function() {
		$(".showS").show();
		$(".showG").hide();
	});
});
function count() {
	t--; // 秒数自减
	if (t >= 0) {
		$(".ft_refresh").html(t); // 刷新当前的秒数，重新显示秒数
	} else {
		t = 60;
		$(".ft_refresh").html(t);
		clearInterval(timer);// 这个可以不用，因为页面都要跳转了，要了也没多大差别
		var page = $("#page").val();
		page++;
		$("#page").val(page);
		$(".scroll_box").load("news/scrollNews", {
			"page" : page
		}, function() {

		});
		if ($(".refresh_area input[checked='checked']").prop("checked")) {
			timer = setInterval("count()", 1000);
		}
	}
}