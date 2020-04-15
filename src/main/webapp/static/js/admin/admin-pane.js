var userData = null;
var blogData = null;
var tagData = null;
var categoryData = null;
var tagCategoryData = null;
var commentData = null;
var allTagCategoryData = null;


$(function () {
    getTagCategory();

    $('#save-tag').click(function () {
        $('#add-tag').modal('hide');
    });
    $('.nav-pills').children('li').each(function (i, v) {
        $(v).click(function () {
            $(this).addClass('active');
            $(this).siblings('li').each(function (i1, v1) {
                if ($(v1).hasClass('active')) {
                    $(v1).removeClass('active');
                }
            });
        });
    });

    $('#sureDeleteUser').click(function () {
        $('#deleteUser').modal('hide');
        //todo 执行删除用户操作， 博客不能删除，删除相关的评论

    });

    $('#sureDeleteBlog').click(function () {
        console.log('确认删除');
        $('#deleteBlog').modal('hide');
        //todo 执行删除博客操作， 删除相关的评论， 清除标签map表中相关的，删除已读表中和当前博客id相同的

    });

});
function logout() {
    var username = $('.user').text();
    username = username.substr(username.indexOf(',') + 1).trim()
    console.log(username)
}

function getTagCategory() {
    $.ajax({
        type: 'post',
        url: '/admin/tag-category/fetch-all-tag-category/all',
        success: function (data) {
            checkIsTimeout(data);
            allTagCategoryData = data;
        }
    });
}
function selectAllRegister() {
    $('#select-all').click(function () {
        if ($(this).prop('checked')) {
            selectAll(true);
            console.log($(this).val())
        } else {
            selectAll(false);
        }
    });
}

var deleteBlogIndex = -1;
function allBlog() {
    var bloghead = '<div class="form-inline">\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="author">作者</label>\n' +
        '                        <input id="author" class="form-control" type="text">\n' +
        '                    </div>\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="title">标题</label>\n' +
        '                        <input id="title" class="form-control" type="email">\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="create-time">创建时间</label>\n' +
        '                        <input id="create-time" class="form-control" type="datetime-local">\n' +
        '                    </div>\n' +
        '                    <div class="input-group">\n' +
        '                        <button class="btn btn-default" type="button">\n' +
        '                            <span class="glyphicon glyphicon-search"></span>\n' +
        '                        </button>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>博客标识</th>\n' +
        '                            <th>标题</th>\n' +
        '                            <th>创建时间</th>\n' +
        '                            <th>作者</th>\n' +
        '                            <th>博客分类</th>\n' +
        '                            <th>点赞数</th>\n' +
        '                            <th>评论数</th>\n' +
        '                            <th>阅读量</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n' +
        '<tbody id="tbody">';
    var foot = '\n' +
        '                    </tbody></table>\n' +
        '                </div>' +
        '<div class="col-lg-12" style="display: flex;justify-content: flex-start;">\n' +
        '                    <div class="col-lg-4"\n' +
        '                         style="padding:0;margin-top:20px;vertical-align: middle; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-5">\n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="width: 40px">\n' +
        '                        <span style="width:100%;margin: 25px 0 0 0;display: inline-block;" id="page-size"></span>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="margin-top:20px;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline" style="width: 100%">\n' +
        '                            <input class="form-control" style="width: 50%" type="text" placeholder="输入页数">\n' +
        '                            <button style="width: 30%" class="btn btn-info" onclick="" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '\n' +
        '                </div>';

    $('#pane').html(bloghead + foot);
    selectAllRegister();
    queryList(1, '/admin/blog/fetch-all-blog', 'post', blogOption);
    $('#sureDeleteBlog').click(function () {
        if (deleteBlogIndex === -1){
            alert('删除失败！');
            return;
        }

        $.ajax({
            type:'delete',
            url:'/admin/blog/delete/' + blogData.data.blogList[deleteBlogIndex].id,
            success: function (data) {
                checkIsTimeout(data);
                if (data.status === 0){
                    console.log('删除成功！');
                    allBlog();
                }else
                    alert('删除失败！');
            }
        });
        deleteBlogIndex = -1;
        return false;
    });
}
function blogOption(pageTotalSize, ret) {
    $('#tbody').html(blogTableInit(ret));
    $('#page-size').html('共' + pageTotalSize + '页');
}
function blogTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.blogList.length; i++) {
        var s = '<tr>';
        s += '<td><input type="checkbox"></td>';
        s += '<td>' + ret.data.blogList[i].id + '</td>';
        s += '<td>' + ret.data.blogList[i].mark + '</td>';
        s += '<td>' + ret.data.blogList[i].title + '</td>';
        s += '<td>' + ret.data.blogList[i].mark + '</td>';
        s += '<td>' + timestampToTime(ret.data.blogList[i].createTime) + '</td>';
        s += '<td>' + ret.data.blogList[i].author + '</td>';
        s += '<td>' + ret.data.blogList[i].category + '</td>';
        s += '<td>' + ret.data.blogList[i].loveNumber + '</td>';
        s += '<td>' + ret.data.blogList[i].commentNumber + '</td>';
        s += '<td>' + ret.data.blogList[i].readNumber + '</td>';
        s += '<td><button type="button" class="btn btn-danger" onclick="deleteBlog('+i+')" data-toggle="modal"\n' +
            '                                data-target="#deleteBlog">删除</button></td>';
        s += '</tr>';
        strs += s;
    }
    return strs;
}
function deleteBlog(index) {
    deleteBlogIndex = index;
}

var deleteUserIndex = -1;
function allUser() {
    var content = '<div class="form-inline">\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="username">昵称</label>\n' +
        '                        <input id="username" class="form-control" type="text">\n' +
        '                    </div>\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="email">邮箱</label>\n' +
        '                        <input id="email" class="form-control" type="email">\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="create-time">创建时间</label>\n' +
        '                        <input id="create-time" class="form-control" type="datetime-local">\n' +
        '                    </div>\n' +
        '                    <div class="form-group has-success" style="padding-right: 10px">\n' +
        '                        <label for="status">状态</label>\n' +
        '                        <select id="status" class="form-control">\n' +
        '                            <option>正常</option>\n' +
        '                            <option>封禁</option>\n' +
        '                        </select>\n' +
        '                    </div>\n' +
        '                    <div class="input-group">\n' +
        '                        <button class="btn btn-default" type="button">\n' +
        '                            <span class="glyphicon glyphicon-search"></span>\n' +
        '                        </button>\n' +
        '                    </div>\n' +
        '                </div>\n';
    var mid = '<div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>昵称</th>\n' +
        '                            <th>性别</th>\n' +
        '                            <th>年龄</th>\n' +
        '                            <th>创建时间</th>\n' +
        '                            <th>状态</th>\n' +
        '                            <th>管理员</th>\n' +
        '                            <th>博客数量</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n';

    var foot = '<tbody id="tbody">\n' +
        '                        </tbody>\n' +
        '                    </table>\n' +
        '                </div>\n' + '                <div style="width:78%;position:absolute; bottom: 0;margin: 0 auto;display: flex; justify-content: flex-start">\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="vertical-align: middle;margin-top: 45px;padding: 0 8px; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-5" style="margin-top:30px;"> \n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-4"\n' +
        '                         style="margin: 20px 40px 20px 0;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline">\n' +
        '<span style="width:50px;margin: 25px 40px 20px 0;display: inline-block;text-align: right" id="page-size">共3页</span>' +
        '                            <input class="form-control" style="width: 100px" type="text" placeholder="输入页数">\n' +
        '                            <button class="btn btn-info" onclick="" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '\n' +
        '                </div>';
    $('#pane').html(content + mid + foot);
    selectAllRegister();
    queryList(1, '/admin/usr/fetch-user/page', 'post', userOption);
    $('#sureDeleteUser').click(function () {
        if (deleteUserIndex === -1){
            alert('删除失败！');
            return;
        }

        $.ajax({
            type:'delete',
            url:'/admin/usr/delete/'+userData.data.userList[deleteUserIndex].id,
            success: function (data) {
                checkIsTimeout(data);
                if (data.status === 0){
                    console.log('删除成功！');
                    allUser();
                }else {
                    alert('删除失败！');
                    deleteUserIndex = -1;
                }
            }
        });
    });
}
function userTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.userList.length; i++){
        strs += '<tr>';
        strs += '<td><input type="checkbox"></td>';
        strs += '<td>'+ret.data.userList[i].id+'</td>';
        strs += '<td>'+ret.data.userList[i].username+'</td>';
        if (ret.data.userList[i].gender === 0)
            strs += '<td>男</td>';
        else
            strs += '<td>女</td>';
        strs += '<td>'+ret.data.userList[i].age+'</td>';
        strs += '<td>'+timestampToTime(ret.data.userList[i].createTime)+'</td>';
        if (ret.data.userList[i].status === 0)
            strs += '<td>正常</td>';
        else
            strs += '<td>封禁</td>';
        if (ret.data.userList[i].isAdmin === 0)
            strs += '<td>no</td>';
        else
            strs += '<td>yes</td>';
        strs += '<td>'+ret.data.userList[i].blogNumber+'</td>';
        strs += '<td><button class="btn btn-danger" onclick="deleteUser('+i+')" data-toggle="modal" +\n' +
            '                                        data-target="#deleteUser">删除</button></td>';
        strs +='</tr>'
    }
    return strs;
}
function userOption(totalPage, ret) {
    userData = ret;
    $('#tbody').html(userTableInit(ret)).fadeIn('slow');
    $('#page-size').html('共' + totalPage + '页');
}
function deleteUser(index) {
    deleteUserIndex = index;
}

function checkIsTimeout(data) {
    //todo 查看是否登录信息过期
    if (data.message === 'NEED_LOGIN') {
        //need login
        alert('用户已超时下线！');
        window.location = data.data;
    }
}

/*分页查询*/
function queryList(page, url, method, option) {

    $.ajax({
        cache: false,
        type: method,
        url: url,
        data: {
            /*当前页码*/
            page: page
        },
        async: false,
        dataType: "json",
        contentType: "application/x-www-form-urlencoded",
        error: function (request) {
        },
        success: function (data) {
            checkIsTimeout(data);

            $(".pagination").pagination({
                /*当前页码*/
                currentPage: page,
                /*总共有多少页*/
                totalPage: Math.ceil(data.data.total / 8),
                /*是否显示首页、尾页 true：显示 false：不显示*/
                isShow: true,
                /*分页条显示可见页码数量*/
                count: 3,
                /*第一页显示文字*/
                homePageText: '首页',
                /*最后一页显示文字*/
                endPageText: '尾页',
                /*上一页显示文字*/
                prevPageText: '上一页',
                /* 下一页显示文字*/
                nextPageText: '下一页',
                /*点击翻页绑定事件*/
                callback: function (page) {
                    queryList(page, url, method, option);
                }
            });
            //todo 填数据
            option(Math.ceil(data.data.total / 8), data);
        }
    });
}

var categoryDeleteIndex = -1;
var categoryState = 'delete';
function allBlogCategory() {

    var s = ' <div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>分类名称</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n' +
        '<tbody id="tbody"></tbody>' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="col-lg-12" style="display: flex;justify-content: flex-start;">\n' +
        '                    <div class="col-lg-2"\n' +
        '                         style="padding:0;margin-top:20px;vertical-align: middle; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '                    <div style="margin-top:20px;">\n' +
        '                        <button class="btn btn-info" id="add-Category" type="button" data-toggle="modal"\n' +
        '                                data-target="#addCategory">新增\n' +
        '                        </button>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-5">\n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="width: 40px">\n' +
        '                        <span style="width:100%;margin: 25px 0 0 0;display: inline-block;" id="page-size"></span>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="margin-top:20px;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline" style="width: 100%">\n' +
        '                            <input class="form-control" style="width: 50%" type="text" placeholder="输入页数">\n' +
        '                            <button style="width: 30%" class="btn btn-info" id="change-category-page" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>';

    $('#pane').html(s);
    selectAllRegister();
    queryList(1, '/admin/category/fetch-all-category', 'post', categoryOptions);
    $('#save-category').click(function () {
        $('#addCategory').modal('hide');
        $.ajax({
            type: 'post',
            url: '/admin/category/add',
            contentType: 'application/json',
            data: JSON.stringify({
                id: null,
                name: $("#category-name").val()
            }),
            success: function (ret) {
                checkIsTimeout(ret);

                if (ret.status === 0) {
                    console.log('添加成功！');
                    allBlogCategory();
                } else {
                    alert('添加失败！');
                }
            }
        });
    });
    // 删除
    $('#sureDeleteBlogCategory').click(function () {
        $("#deleteBlogCategory").modal('hide');
        if (categoryState === 'delete') {
            if (categoryDeleteIndex !== -1) {
                $.ajax({
                    type: 'delete',
                    url: '/admin/category/delete/' + categoryData.data.categoryList[categoryDeleteIndex].id,
                    success: function (ret) {
                        checkIsTimeout(ret);
                        if (ret.status === 0) {

                            allBlogCategory();
                            console.log('删除成功！');

                        } else
                            alert('删除失败！');
                    }
                });
            }
        } else { // 更新
            var value = $("#update-category-name").val();
            if (value === '' || value === categoryData.data.categoryList[categoryDeleteIndex].name) {
                alert('分类未更新!');
            } else {
                $.ajax({
                    type: 'put',
                    url: '/admin/category/update/' + value + '/' + categoryData.data.categoryList[categoryDeleteIndex].name,
                    success: function (ret) {
                        checkIsTimeout(ret);
                        if (ret.status === 0) {
                            $("#deleteBlogCategory").modal('hide');
                            allBlogCategory();
                            console.log('更新成功！');
                        } else
                            alert('更新失败！');
                    }
                });
            }
        }
        categoryDeleteIndex = -1;
    });
    // 跳转
    $('#change-category-page').click(function () {
        console.log($(this).siblings('input').val());
    });
}
function categoryTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.categoryList.length; i++) {
        strs += '<tr>';
        strs += '<td><input type="checkbox"></td>';
        strs += '<td>' + ret.data.categoryList[i].id + '</td>';
        strs += '<td>' + ret.data.categoryList[i].name + '</td>';
        strs += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#deleteBlogCategory" onclick="updateCategory(' + i + ')">更新</button><button type="button" class="btn btn-danger" data-toggle="modal"' +
            '                                data-target="#deleteBlogCategory" onclick="deleteCategory(' + i + ');">删除</button></td>';
        strs += '</tr>';
    }
    return strs;
}
function categoryOptions(totalPage, ret) {
    categoryData = ret;
    $('#tbody').html(categoryTableInit(ret));
    $('#page-size').html('共' + totalPage + '页');
}
function deleteCategory(index) {
    $("#category-message-content").html('<span style="margin-left: 20px">确定删除此分类？</span>');
    $('#sureDeleteBlogCategory').text('确定删除');
    categoryState = 'delete';
    categoryDeleteIndex = index;
}
function updateCategory(index) {
    categoryState = 'update';
    $('#category-message-content').html('<span style="margin-left: 20px;margin-bottom: 10px">更新分类</span><form class="form-inline" style="margin-left: 20px">\n' +
        '                        <input class="form-control" placeholder="请输入分类名称" id="update-category-name">\n' +
        '                    </form>');
    $("#update-category-name").val(categoryData.data.categoryList[index].name);
    $('#sureDeleteBlogCategory').text('确定更新');
    categoryDeleteIndex = index;
}

var tagState = 'add';
var tagDeleteAndUpdateIndex = -1;
function allTag() {
    var s = ' <div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>标签名称</th>\n' +
        '                            <th>标签分类</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n' +
        '<tbody id="tbody"></tbody>' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="col-lg-12" style="display: flex;justify-content: flex-start;">\n' +
        '                    <div class="col-lg-2"\n' +
        '                         style="padding:0;margin-top:20px;vertical-align: middle; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '\n' + '<div style="margin-top:20px;">' +
        '                              <button class="btn btn-info" onclick="addTag()" type="button" data-toggle="modal" +\n' +
        '                                        data-target="#add-tag">新增' +
        '                              </button>' +
        '                         </div>' +
        '                    <div class="col-lg-5">\n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="width: 50px">\n' +
        '                        <span style="width:100%;margin: 25px 0 0 0;display: inline-block;" id="page-size"></span>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="margin-top:20px;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline" style="width: 100%">\n' +
        '                            <input class="form-control" style="width: 50%" type="text" placeholder="输入页数">\n' +
        '                            <button style="width: 30%" class="btn btn-info" onclick="" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>';
    $('#pane').html(s);
    selectAllRegister();
    queryList(1, '/admin/tag/fetch-all-tag', 'post', tagOptions);

    $('#save-tag').click(function (e) {
        if (!e.isPropagationStopped()) {
            if (tagState === 'add') {
                var value = $('#tag-name').val();
                var category = $('#tag-category').val();
                if (value === '' || category === '') {
                    alert('请输入标签名称或选择标签分类！');
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: '/admin/tag/add?tagCategoryName=' + category + '&tagName=' + value,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            console.log('添加标签成功！');
                            allTag();
                            return
                        }
                        alert('添加失败！');
                    }
                });

            } else if (tagState === 'delete') {

                if (tagDeleteAndUpdateIndex === -1) {
                    alert('内部错误！');
                    return;
                }
                $.ajax({
                    type: "delete",
                    url: '/admin/tag/delete/' + tagData.data.tagList[tagDeleteAndUpdateIndex].id,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            console.log('删除标签成功！');
                            allTag();
                            return;
                        }
                        alert('删除失败！');
                    }
                });
            } else if (tagState === 'update') {
                value = $('#tag-name').val();
                category = $('#tag-category').val();
                if (tagDeleteAndUpdateIndex === -1 || value === '' || category === '') {
                    alert('内部错误！');
                    return;
                }
                $.ajax({
                    type: "put",
                    url: '/admin/tag/update/' + value + '/' + tagData.data.tagList[tagDeleteAndUpdateIndex].name + '/' + category,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            console.log('更新标签成功！');
                            allTag();
                            return;
                        }
                        alert('更新失败！');
                    }
                });
            }
            return false;
        }
    });
}
function tagTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.tagList.length; i++) {
        strs += '<tr>';
        strs += '<td><input type="checkbox"></td>';
        strs += '<td>' + ret.data.tagList[i].id + '</td>';
        strs += '<td>' + ret.data.tagList[i].name + '</td>';
        strs += '<td>' + ret.data.tagList[i].category + '</td>';
        strs += '<td><button type="button" class="btn btn-primary" onclick="updateTag(' + i + ')" data-toggle="modal" +\n' +
            '                                        data-target="#add-tag">更新</button><button type="button" class="btn btn-danger" data-toggle="modal" +\n' +
            '                                        data-target="#add-tag" onclick="deleteTag(' + i + ')">删除</button></td>';
        strs += '</tr>';
    }
    return strs;
}
function tagOptions(totalPage, ret) {
    tagData = ret;
    $("#tbody").html(tagTableInit(ret));
    $('#page-size').html('共' + totalPage + '页');
}
function addTag() {
    $('#tag-message-body').html('<form class="form-inline">\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-name">标签名称：\n' +
        '                                <input type="text" id="tag-name" class="form-control" placeholder="输入标签的名称">\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-category">标签类型：\n' +
        '                                <select id="tag-category" class="form-control">\n' +
        '                                </select>\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                    </form>');
    tagState = 'add';
    $('#tag-name').val('');
    $('#tag-title').text('新增标签');
    var strs = '<option></option>';
    for (var i = 0; i < allTagCategoryData.data.length; i++) {
        strs += '<option>' + allTagCategoryData.data[i].tagCategoryName + '</option>';
    }
    $('#save-tag').text('保存').removeClass('btn-danger').addClass('btn-primary');
    $('#tag-category').html(strs);
}
function deleteTag(index) {
    tagState = 'delete';
    tagDeleteAndUpdateIndex = index;
    $('#tag-message-body').html('<span>你确定要删除当前标签吗？</span>');
    $('#save-tag').text('确定并删除').removeClass('btn-primary').addClass('btn-danger');
}
function updateTag(index) {
    $('#tag-message-body').html('<form class="form-inline">\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-name">标签名称：\n' +
        '                                <input type="text" id="tag-name" class="form-control" placeholder="输入标签的名称">\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-category">标签类型：\n' +
        '                                <select id="tag-category" class="form-control">\n' +
        '                                </select>\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                    </form>');
    tagDeleteAndUpdateIndex = index;
    tagState = 'update';
    $('#tag-name').val(tagData.data.tagList[index].name);
    $('#tag-title').text('更新标签');
    var strs = '<option>' + tagData.data.tagList[index].category + '</option>';
    for (var i = 0; i < allTagCategoryData.data.length; i++){
        if (allTagCategoryData.data[i].tagCategoryName !== tagData.data.tagList[index].category){
            strs += '<option>' + allTagCategoryData.data[i].tagCategoryName + '</option>'
        }
    }
    $('#tag-category').html(strs);
    $('#save-tag').text('更新').removeClass('btn-primary').addClass('btn-danger');
}

//todo 标签分类管理
var tagCategoryState = 'add';
var updateAndDelIndex = -1;

function allTagCategory() {
    var s = ' <div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>标签分类名称</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n' +
        '<tbody id="tbody"></tbody>' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="col-lg-12" style="display: flex;justify-content: flex-start;">\n' +
        '                    <div class="col-lg-2"\n' +
        '                         style="padding:0;margin-top:20px;vertical-align: middle; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="margin-top:20px;">\n' +
        '                        <button class="btn btn-info" onclick="addTagCategory()" type="button" data-toggle="modal"\n' +
        '                                data-target="#addTagCategory">新增\n' +
        '                        </button>\n' +
        '                    </div>\n' +
        '                    <div class="col-lg-5">\n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="width: 40px">\n' +
        '                        <span style="width:100%;margin: 25px 0 0 0;display: inline-block;" id="page-size"></span>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="margin-top:20px;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline" style="width: 100%">\n' +
        '                            <input class="form-control" style="width: 50%" type="text" placeholder="输入页数">\n' +
        '                            <button style="width: 30%" class="btn btn-info" onclick="" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>';
    $('#pane').html(s);
    selectAllRegister();
    queryList(1, '/admin/tag-category/fetch-all-tag-category', 'post', tagCategoryOptions);
    $('#save-tag-category').one('click',function () {
        $('#addTagCategory').modal('hide');
        if (tagCategoryState === 'add') {
            var value = $('#tag-category-name').val();
            console.log();
            //todo check if is reasonable
            if (value === '') {
                alert('请输入标签分类名');
            } else {
                $.ajax({
                    type: 'post',
                    url: '/admin/tag-category/add?tagCategoryName=' + value,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            console.log('插入成功');
                            allTagCategory();
                            getTagCategory();
                        } else {
                            alert('插入失败：' + data.message);
                        }
                    }
                });
            }
        } else if (tagCategoryState === 'delete') {
            //todo 删除
            if (updateAndDelIndex !== -1 && updateAndDelIndex < tagCategoryData.data.tagCategoryList.length) {
                $.ajax({
                    type: 'delete',
                    url: '/admin/tag-category/delete?tag-category-name=' + tagCategoryData.data.tagCategoryList[updateAndDelIndex].tagCategoryName,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            getTagCategory();
                            allTagCategory();
                            console.log('删除成功');
                        } else
                            alert('删除失败：' + data.message);
                    }
                });
            }
        } else if (tagCategoryState === 'update') {
            //todo 更新
            var input = $('#tag-category-name');
            value = input.val();
            if (updateAndDelIndex !== -1 && updateAndDelIndex < tagCategoryData.data.tagCategoryList.length && value !== '') {
                $.ajax({
                    type: 'put',
                    url: '/admin/tag-category/update/' + value + '===>' + tagCategoryData.data.tagCategoryList[updateAndDelIndex].tagCategoryName,
                    success: function (data) {
                        checkIsTimeout(data);
                        if (data.status === 0) {
                            getTagCategory();
                            allTagCategory();
                            console.log('更新成功');
                        } else
                            alert('更新失败：' + data.message);
                    }
                });
            } else
                alert('更新失败');
        }
        updateAndDelIndex = -1;
        return false;
    });
}
function tagCategoryTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.tagCategoryList.length; i++) {
        strs += '<tr>';
        strs += '<td><input type="checkbox"></td>';
        strs += '<td>' + ret.data.tagCategoryList[i].id + '</td>';
        strs += '<td>' + ret.data.tagCategoryList[i].tagCategoryName + '</td>';
        strs += '<td><button type="button" class="btn btn-primary" data-toggle="modal"\n' +
            '                                data-target="#addTagCategory" onclick="updateTagCategory(' + i + ')">更新</button> <button type="button" data-toggle="modal"\n' +
            '                                data-target="#addTagCategory" class="btn btn-danger" onclick="deleteTagCategory(' + i + ')">删除</button></td>';
        strs += '</tr>';
    }
    return strs;
}
function tagCategoryOptions(totalPage, ret) {
    tagCategoryData = ret;
    $("#tbody").html(tagCategoryTableInit(ret));
    $('#page-size').html('共' + totalPage + '页');
}
function addTagCategory() {
    tagCategoryState = 'add';
    $('#tag-category-name').val('');
    $('#tag-category-title').text('新增标签分类');
    $('#tag-category-modal-body').html('<form class="form-inline">\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-name">标签分类名称：\n' +
        '                                <input type="text" id="tag-category-name" class="form-control" placeholder="输入分类的名称">\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                    </form>');
    $('#save-tag-category').removeClass('btn-danger').addClass('btn-primary').text('保存');
}
function updateTagCategory(i) {
    tagCategoryState = 'update';
    updateAndDelIndex = i;
    $('#tag-category-modal-body').html('<form class="form-inline">\n' +
        '                        <div class="form-group">\n' +
        '                            <label for="tag-name">标签分类名称：\n' +
        '                                <input type="text" id="tag-category-name" class="form-control" placeholder="输入分类的名称">\n' +
        '                            </label>\n' +
        '                        </div>\n' +
        '                    </form>');
    $('#tag-category-name').val(tagCategoryData.data.tagCategoryList[i].tagCategoryName);
    $('#tag-category-title').text('更新标签分类');
    $('#save-tag-category').removeClass('btn-primary').addClass('btn-danger').text('更新');
}
function deleteTagCategory(i) {
    tagCategoryState = 'delete';
    updateAndDelIndex = i;
    $('#tag-category-modal-body').html('<span style="margin-left: 20px">确定删除此分类？</span><br>\n' +
        '                    <span style="margin-left: 20px">删除之后会删除相应的标签！！</span>');
    $('#tag-category-title').text('删除确认');
    $('#save-tag-category').removeClass('btn-primary').addClass('btn-danger').text('确认并删除');
}

//todo 评论管理
var deleteCommentIndex = -1;
function allComment() {
    var s = ' <div class="table-responsive" style="margin-top: 10px;height: 450px;overflow: scroll">\n' +
        '                    <table class="table table-hover">\n' +
        '                        <thead>\n' +
        '                        <tr>\n' +
        '                            <th></th>\n' +
        '                            <th>id</th>\n' +
        '                            <th>回复内容</th>\n' +
        '                            <th>用户</th>\n' +
        '                            <th>所属博客id</th>\n' +
        '                            <th>回复的评论id</th>\n' +
        '                            <th>ops</th>\n' +
        '                        </tr>\n' +
        '                        </thead>\n' +
        '<tbody id="tbody"></tbody>' +
        '                    </table>\n' +
        '                </div>\n' +
        '                <div class="col-lg-12" style="display: flex;justify-content: flex-start;">\n' +
        '                    <div class="col-lg-2"\n' +
        '                         style="padding:0;margin-top:20px;vertical-align: middle; border-radius: 4px;display: inline-block;">\n' +
        '                        <input type="checkbox" id="select-all">&nbsp;全选&nbsp;\n' +
        '                        <button class="btn btn-danger">删除</button>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-5">\n' +
        '                        <nav aria-label="Page navigation" style="text-align: center">\n' +
        '                            <ul class="pagination">\n' +
        '                            </ul>\n' +
        '                        </nav>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div style="width: 40px">\n' +
        '                        <span style="width:100%;margin: 25px 0 0 0;display: inline-block;" id="page-size"></span>\n' +
        '                    </div>\n' +
        '\n' +
        '                    <div class="col-lg-3"\n' +
        '                         style="margin-top:20px;display: inline-block;float: right;text-align: right">\n' +
        '                        <div class="form-inline" style="width: 100%">\n' +
        '                            <input class="form-control" style="width: 50%" type="text" placeholder="输入页数">\n' +
        '                            <button style="width: 30%" class="btn btn-info" onclick="" title="跳转">跳转</button>\n' +
        '                        </div>\n' +
        '                    </div>';
    $('#pane').html(s);
    selectAllRegister();
    queryList(1, '/admin/comment/fetch-all-comment', 'post', commentOptions);
    $('#sureDeleteComment').click(function () {
        if (deleteCommentIndex === -1){
            alert('删除失败！');
            return;
        }
        
        $.ajax({
            type:'delete',
            url:'/admin/comment/delete/'+commentData.data.commentList[deleteCommentIndex].id,
            success: function (data) {
                checkIsTimeout(data);
                if (data.status === 0){
                    console.log('删除成功！');
                    allComment();
                }else {
                    alert('删除失败');
                }
            }
        });
        return false;
    });
}
function commentTableInit(ret) {
    var strs = '';
    for (var i = 0; i < ret.data.commentList.length; i++) {
        strs += '<tr>';
        strs += "<td><input type='checkbox'></td>";
        strs += "<td>" + ret.data.commentList[i].id + '</td>';
        strs += "<td>" + ret.data.commentList[i].content + '</td>';
        strs += "<td>" + ret.data.commentList[i].username + '</td>';
        strs += "<td>" + ret.data.commentList[i].blogId + '</td>';
        strs += "<td>" + ret.data.commentList[i].replyId + '</td>';
        strs += "<td><button type='button' onclick='deleteComment('+i+')' class='btn btn-danger'>删除</button></td>";
        strs += '</tr>';
    }
    return strs;
}
function commentOptions(totalPage, ret) {
    commentData = ret;
    $("#tbody").html(commentTableInit(ret));
    $('#page-size').html('共' + totalPage + '页');
}
function deleteComment(index) {
    deleteCommentIndex = index;
}

function selectAll(flag) {
    $('td input[type=checkbox]').each(function (i, v) {
        $(v).attr('checked', flag)
    });
}

$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('.dropdown-toggle').dropdown();
});

$('.dropdown span').on('mouseover', function () {
    $(this).parent().addClass('open');
});