

$(function () {

    $('#q-con').keyup(function (e) {
        if (e.keyCode === 13){
            window.localStorage.setItem('kw',$(this).val());
            window.location = '/blog/query?kw=' + $(this).val();
        }
    });
    $('#q-action').click(function () {
        var q = $('#q-con');
        window.localStorage.setItem('kw',q.val());
        window.location = '/blog/query?kw=' + q.val();
    });

    var url = window.location.href;
    url = url.substr(url.lastIndexOf('/')+1);
    url = decodeURI(url);

    $('.nav-tabs').each(function (i1, v1) {
        if ($(v1).children('li').hasClass('active'))
            $(v1).children('li').removeClass('active');
    }).each(function (i, v) {
        var tag = $(v).children('li').children('a').text();
        if ( tag === url){
            $(v).children('li').addClass('active');
            return  false;
        }else if (tag === '首页'){
            $(v).children('li').addClass('active');
            return false;
        }
    });
});
$('.dropdown-toggle').dropdown();
function switchAccount() {
    justLogout = false;
    userLogout();
}
var justLogout = true;
function userLogout(){
    var username = $('.user').text();

    $.ajax({
       headers:{
           'Do-Logout-Action':'true'
       },
       type:'delete',
       url:'/usr/logout/'+ username,
       success:function (ret) {
           //todo 查看是否登录信息过期
           if (ret.message === 'NEED_LOGIN') {
               //need login
               alert('用户已下线！');
               window.location = ret.data;
           }
           if (ret.status === 0 && ret.data === true && !justLogout){
               console.log('下线成功！');
               window.location = '/login.html';
           }
           if (ret.status === 0 && ret.data === true && justLogout){
               console.log('下线成功！');
               window.location = '/';
           }

       },
        error: function (e) {
            console.log(e)
        }
    });
}

$('.dropdown span').on('mouseover',function () {
    $(this).parent().addClass('open');
});
$('.nav > li > a').click(function () {

    $(this).parent().parent().children().each(function (i, v) {
        if ($(v).hasClass('active')){
            $(v).removeClass('active');
            $(v).children().css('color', 'black');
            $(v).children().children().css('color', 'black');
        }
    });

    $(this).parent().addClass('active');
    $(this).css('color', '#0F769F');
    $(this).children().css('color', '#0F769F');
    //todo 执行切换分类
});
