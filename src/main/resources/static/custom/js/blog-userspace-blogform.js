/*!
 * blogedit.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";

/**
 * userspace->blogform.html
 * 添加/修改博客共用此页面
 */
$(function() {

    /**
	 *  初始化 md 编辑器
     *
     *	$("#md").markdown({
	 *		language: 'zh',
	 *		fullscreen: {
	 *			enable: true
	 *		},
	 *		resize:'vertical',
	 *		localStorage:'md',
	 *		imgurl: 'http://localhost:8081',
	 *		base64url: 'http://localhost:8081'
	 *	});
	 *
	 **/

    /**
	 *  初始化标签控件
     */
	$('.form-control-tag').tagEditor({
		initialTags: [],
		maxTags: 8,
		delimiter: ', ',
		forceLowercase: false,
		animateDelete: 0,
		placeholder: '请输入标签'
	});



    /**
	 * 初始化分类(可手动输入进行选择)
     */
    $('.form-control-chosen').chosen();

    /**
	 * 文件上传
     */
 	$("#uploadImage").click(function() {
		$.ajax({
		    url: 'http://localhost:8081/upload',
		    type: 'post',
		    cache: false,
		    data: new FormData($('#uploadformid')[0]),
		    processData: false,
		    contentType: false,
		    success: function(data){
		    	var mdcontent=$("#md").val();
		    	 $("#md").val(mdcontent + "\n![]("+data +") \n");
 
	         }
		}).done(function(res) {
			$('#file').val('');
		}).fail(function(res) {});
 	})

    /**
	 * 添加/修改博客
     */
 	$("#submitBlog").click(function() {
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		/** 异步提交 */
		$.ajax({
		    url: '/u/'+ $(this).attr("userName") + '/blogs/saveOrUpdate',
		    type: 'post',
			contentType: "application/json; charset=utf-8",
		    data:JSON.stringify({"id"		: $('#id').val(),
								 "title"	: $('#title').val(),
								 "summary"	: $('#summary').val() ,
								 "content"	: $('#md').val(),
                				 "catalog":{"id":$('#catalogSelect').val()},
                				 "tags":$('.form-control-tag').val()}),
			beforeSend: function(request) {
			    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
			},
			 success: function(data){
				 if (data.success) {
					// 成功后，重定向
					 window.location = data.body;
				 } else {
                     var errorArr = data.message.split(";");
                     for(var i=0; i<errorArr.length; i++){
                         toastr.error(errorArr[i]);
                     }
				 }
		     },
		     error : function() {
		    	 toastr.error("抱歉，系统错误！");
		     }
		})
 	})
 	
});