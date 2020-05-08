Simditor.locale = 'zh-CN';//设置中文
var editor = new Simditor({
    textarea: $('#content'),
    pasteImage: true,
    tabIndent: true,
    upload: {
        type: 'post',
        url: '/blog/images', //文件上传的接口地址
        params: null, //键值对,指定文件上传接口的额外参数,上传的时候随文件一起提交
        fileKey: 'image', //服务器端获取文件数据的参数名
        connectionCount: 3,
        leaveConfirm: '正在上传文件...'
    },
    toolbar: ['title', 'bold', 'italic', 'underline', 'strikethrough', 'fontScale', 'color', 'ol',
        'ul',
        'blockquote',
        'code',
        'table',
        'link',
        'image',
        'hr',
        'indent',
        'outdent',
        'alignment'
    ]
});
$(".simditor input[type='file']").attr('accept', 'image/jpg,image/jpeg,image/png,image/gif');

$('.dropdown-toggle').dropdown();
$(function () {
    $.ajax({
        type: 'post',
        url: '/category/fetch-all-category',
        success: function (ret) {
            if (ret.status === 0) {
                var strs = '<option></option>';
                for (var i = 0; i < ret.data.categoryList.length; i++) {
                    strs += '<option>' + ret.data.categoryList[i].name + '</option>';
                }
                $('#article-type').html(strs);
            }
        }
    });
});

//在关闭页面时弹出确认提示窗口
$(window).bind('beforeunload', function () {
    if ($('#title').val() !== '' && editor.getValue() !== '') {
        return '您可能有数据没有保存';
    }
});

$('.dropdown span').on('mouseover', function () {
    $(this).parent().addClass('open');
});

$('#close').click(function () {
    $('#back-option').modal('hide')
});
$('#cancel').click(function () {
    $('#back-option').modal('hide')
});
$('#leave').click(function () {
    history.go(-1);
    $('#back-option').modal('hide')
});

$('#title').bind('input propertychange', function () {
    $('#str-counter').text($('#title').val().length + '/30');
});

var tagCategoryTagMap = new Map();
var token = null;
$(function () {
    //todo 从后台获取标签分类和对应的标签
    $.ajax({
        type: 'post',
        url: '/t-tc/all',
        success: function (data) {
            if (data.status === 0) {
                var strs = '';
                for (var k in data.data) {
                    strs += '<span class="tag-span">' + k + '</span>';
                    tagCategoryTagMap.set(k, data.data[k]);
                }
                var categories = $('#tag-category');
                categories.html(strs);


                $('.tag-span').click(function () {
                    categories.children().each(function (i, v) {
                        if ($(v).hasClass('test'))
                            $(v).removeClass('test');
                    });

                    $(this).addClass("test");
                    var tagArray = tagCategoryTagMap.get($(this).text());
                    var strs = '<br/><br/><br/>';
                    for (var i = 0; i < tagArray.length; i++) {
                        var tagAdded = $('#tag-added');
                        var flag = false;
                        tagAdded.children().each(function (i1, v){
                            if ($(v).children('.tag-added-close').prev().text() === tagArray[i].name)
                                flag = true;
                        });
                        if (flag){
                            strs += '<span class="tag-details test">' + tagArray[i].name + '</span>';
                        }else
                            strs += '<span class="tag-details">' + tagArray[i].name + '</span>';
                    }
                    $('#tag-content').html(strs);

                    $('.tag-details').click(function () {

                        var html = '<span class="tag-added">\n' +
                            '                                        <span>' + $(this).text() + '</span>\n' +
                            '                                        <span class="tag-added-close">x</span>\n' +
                            '                                    </span>';

                        var s = $(this);
                        var tagAdded = $('#tag-added');
                        if (tagAdded.children().length < 5) {
                            var flag = false;
                            s.addClass('test');
                            tagAdded.children().each(function (i, v) {
                                if ($(v).children('.tag-added-close').prev().text() === s.text()) {
                                    $(v).remove();
                                    s.removeClass('test');
                                    flag = false;
                                    return false;
                                } else {
                                    flag = true;
                                }
                            });
                            if (flag)
                                tagAdded.prepend(html);
                        }

                        $('.tag-added-close').click(function () {
                            var str = $(this).siblings('span').text();
                            $('#tag-content').children().each(function (i, v) {
                                if ($(v).text() === str) {
                                    $(v).removeClass('test');
                                }
                            });
                            $(this).parent().remove();
                        });
                    });
                });
            }
        }
    });
    // todo 从后台获取token
    $.ajax({
        type: 'post',
        url:'/blog/generate-blog-token',
        headers:{
            'Generate-Token':true
        },
        success: function (ret) {
            if (ret.status === 0){
                token = ret.data;
            }
        }
    });
});


$('.dropdown-menu').on("click", function (e) {
    e.stopPropagation();
});

$('#tag-confirmed').click(function () {
    $('.dropdown').children('.tag-added').remove();
    $('.dropdown').removeClass('open').prepend($('#tag-added').children('.tag-added').clone(true)).addClass('added-tag');
});
$('#tag-cancel').click(function () {
    $('.dropdown').removeClass('open');
});

$('#publish').click(function () {
    var tags = [];
    $('.dropdown').children('span').each(function (i, v) {
        tags.push($(v).children('.tag-added-close').prev().text());
    });
    var formData = new FormData(); //创建一个 form 类型的数据
    formData.append('title', $('#title').val());
    formData.append('introducePhoto', $('#introducePhoto')[0].files[0]);
    formData.append('content', editor.getValue());
    formData.append('tags', tags);
    formData.append('articleType', $('#article-type').val());
    if (token === null){
        alert('发布失败，未能获取token，请刷新后尝试！');
        return;
    }else
        formData.append('token', token);
    if ($('#title').val() === '') {
        alert('请输入文章标题！');
        return;
    }
    if ($('#introducePhoto').val() === '') {
        alert('请选择博客图片');
        return;
    }

    if (tags.length === 0) {
        alert('请选择文章的标签');
        return;
    }
    $('.modal-blog').css({'display':'block', 'visibility':'visible'});
    $('.loading').css({'display':'block', 'visibility':'visible'});

    //todo 没有错误，发送ajax请求
    $.ajax({
        type: 'post',
        url: '/blog/accept',
        data: formData,
        processData: false,
        contentType: false,
        success: function (ret) {
            if (ret.status === 0){
                //todo 发布成功，跳转到对应的博客页面
                $('#title').val('');
                editor.setValue('');
                window.location = ret.data;
            }else{
                if (ret.message === 'NEED_LOGIN'){
                    alert('请登录之后在尝试！');
                    return;
                }
                alert("发布失败！后台错误，请通知管理员排除，qq：1905269298");
            }
        }
    });
});