/*!
  * Bolg main JS.
 * 
 * @since: 1.0.0 2017/3/9
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=main.js

// DOM 加载完再执行
$(function () {

    var _pageSize; // 存储用于搜索

    /**
     * 根据用户名、页面索引、页面大小获取用户列表
     * @param pageIndex
     * @param pageSize
     */
    function getUersByName(pageIndex, pageSize) {
        $.ajax({
            url: "/users",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "name": $("#searchName").val()
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    /**
     * 分页操作
     */
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getUersByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    /**
     * 搜索按钮事件
     */
    $("#searchNameBtn").click(function () {
        getUersByName(0, _pageSize);
    });

    /**
     * 获取添加用户的界面，将页面内容追加到#userFormContainer元素中
     */
    $("#addUser").click(function () {
        $.ajax({
            url: "/users/add",
            type: "get",
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function (data) {
                toastr.error("error!");
            }
        });
    });

    /**
     * 获取编辑用户的界面，将页面内容追加到#userFormContainer元素中
     */
    $("#rightContainer").on("click", ".blog-edit-user", function () {
        $.ajax({
            url: "/users/edit/" + $(this).attr("userId"),
            success: function (data) {
                $("#userFormContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    /**
     * 添加/修改操作
     */
    $("#submitEdit").click(function () {
        $.ajax({
            url: "/users/add",
            type: 'post',
            data: $('#userForm').serialize(),
            success: function (data) {
                //$('#userForm')[0].reset();
                if (data.success) {
                    $("#flipFlop").modal('hide');
                    // 重新刷新主界面
                    getUersByName(0, _pageSize);
                } else {
                    var errorArr = data.message.split(";");
                    for(var i=0; i<errorArr.length; i++){
                        toastr.error(errorArr[i]);
                    }
                }
            },
            error: function () {
                toastr.error("抱歉，系统错误！");
            }
        });
    });

    // TODO 删除用户，待完善
    $("#rightContainer").on("click", ".blog-delete-user", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/users/" + $(this).attr("userId"),
            type: 'delete',
            beforeSend: function(request) {
                // 添加  CSRF Token
                request.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (data) {
                if (data.success) {
                    getUersByName(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("抱歉，系统错误！");
            }
        });
    });
});