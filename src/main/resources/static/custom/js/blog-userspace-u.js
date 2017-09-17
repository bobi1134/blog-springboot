/*!
 * u main JS.
 * 
 * @since: 1.0.0 2017/3/9
 * @author Way Lau <https://waylau.com>
 */
"use strict";

/**
 * userspace->u.html
 * 个人主页业务逻辑
 */
$(function() {

	/** 存储用于关键字搜索 */
	var _pageSize;

    /**
	 * 根据用户名、页面索引、页面大小获取用户列表
     * @param pageIndex
     * @param pageSize
     */
	function getBlogsByName(pageIndex, pageSize) {
		 $.ajax({ 
			 url: "/u/"+  username  +"/blogs", 
			 contentType : 'application/json',
			 data:{"async":true,
                   "pageIndex":pageIndex,
                   "pageSize":pageSize,
                   "catalogId":catalogId,
                   "keyword":$("#keyword").val()},
			 success: function(data){
			 	/** 将结果返回到mainContainer节点 */
				$("#mainContainer").html(data);
		     },
		     error : function() {
		    	 toastr.error("抱歉，系统错误！");
		     }
		 });
	}

    /**
	 * 分页工具
     */
	$.tbpage("#mainContainer", function (pageIndex, pageSize) {
		getBlogsByName(pageIndex, pageSize);
		_pageSize = pageSize;
	});

    /**
	 * 关键字搜索
     */
	$("#searchBlogs").click(function() {
		getBlogsByName(0, _pageSize);
	});

    $('#keyword').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            getBlogsByName(0, _pageSize);
        }
    });

    /**
	 * 最新\最热切换事件
     */
	$(".nav .nav-link").click(function() {
		var url = $(this).attr("url");
		
		// 先移除其他的点击样式，再添加当前的点击样式
		$(".nav .nav-link").removeClass("active");
		$(this).addClass("active");  
 
		// 加载其他模块的页面到右侧工作区
		 $.ajax({ 
			 url: url+'&async=true', 
			 success: function(data){
				 $("#mainContainer").html(data);
			 },
			 error : function() {
				 toastr.error("error!");
			 }
		 })
		 // 清空搜索框内容
		 $("#keyword").val('');
	});





    /**
     * 获取分类列表
     */
    getCatalogs(username);
    function getCatalogs(username) {
        $.ajax({
            url: '/catalogs',
            type: 'get',
            data:{"username":username},
            success: function(data){
                $("#catalogMain").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }

    /**
     * 打开编辑分类页面(模态窗口)
     */
    $(".blog-content-container").on("click",".blog-edit-catalog", function () {
        $.ajax({
            url: '/catalogs/edit/'+$(this).attr('catalogId'),
            type: 'GET',
            success: function(data){
                $("#catalogFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    /**
     * 打开添加分类页面(模态窗口)
     */
    $(".blog-content-container").on("click",".blog-add-catalog", function () {
        $.ajax({
            url: '/catalogs/edit',
            type: 'get',
            success: function(data){
                $("#catalogFormContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    /**
     * 添加/修改分类
     */
    $("#submitEditCatalog").click(function() {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/catalogs',
            type: 'post',
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify({"username":username, "catalog":{"id":$('#catalogId').val(), "name":$('#catalogName').val()}}),
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(data){
                if (data.success) {
                    toastr.info(data.message);
                    // 成功后，刷新列表
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    /**
     * 删除分类
     */
    $(".blog-content-container").on("click",".blog-delete-catalog", function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: '/catalogs/'+$(this).attr('catalogid')+'?username='+username,
            type: 'DELETE',
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(data){
                if (data.success) {
                    toastr.info(data.message);
                    // 成功后，刷新列表
                    getCatalogs(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    // 根据分类查询
    $(".blog-content-container").on("click",".blog-query-by-catalog", function () {
        catalogId = $(this).attr('catalogId');
        getBlogsByName(0, _pageSize);
    });
});