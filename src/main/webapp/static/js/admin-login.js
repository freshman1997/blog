$(function () {
    $('form').addClass('box');
    $('[data-toggle="popover"]').popover();

    $('#admin-username').bind('blur', function () {
        if ($(this).val() === ''){
            $(this).popover('show');
            $(this).css('border-color', 'red');
        }
    }).on('focus', function () {
        $(this).popover('hide');
        $(this).css('border-color', '#66afe9');
    });

    $('#admin-password').bind('blur', function () {
        if ($(this).val() === ''){
            $(this).popover('show');
            $(this).css('border-color', 'red');
        }
    }).on('focus', function () {
        $(this).popover('hide');
        $(this).css('border-color', '#66afe9');
    });
});

$('input[type=submit]').click(function (event) {
    event.preventDefault();
    var AdminNameOrEmail = $('#admin-username');
    var adminPassword= $('#admin-password');


    if (AdminNameOrEmail.val() === ''){
        AdminNameOrEmail.popover('show');
        AdminNameOrEmail.css('border-color', 'red');
        return;
    }
    if (adminPassword.val() === ''){
        adminPassword.popover('show');
        adminPassword.css('border-color', 'red');
        return;
    }
    $.ajax({
        type:'post',
        url: '/admin/usr/login',
        data: 'username=' + AdminNameOrEmail.val() + '&password='+ adminPassword.val() + '&email=' + AdminNameOrEmail.val(),
        success: function (ret) {
            if (ret.status === 0){
                $('h1').html('Welcome');
                $('form').fadeOut(500);
                $('.wrapper').addClass('form-success');

                setTimeout(function () {
                    $(window).attr('location','/admin/pane');
                }, 1000);
            }else {
                if (typeof(ret.message) === 'undefined'){
                    AdminNameOrEmail.attr('data-content', '用户名或邮箱错误');
                    AdminNameOrEmail.css('border-color', 'red');
                    AdminNameOrEmail.popover('show');
                    AdminNameOrEmail.attr('data-content', '请输入用户名或邮箱登录');
                }else {
                    adminPassword.attr('data-content', ret.message);
                    adminPassword.css('border-color', 'red');
                    adminPassword.popover('show');
                    adminPassword.attr('data-content', '登录博客密码为必需');
                }
            }
        },
        error: function (e) {
            console.log('后台错误或网络错误'+ e);
        }
    })
});