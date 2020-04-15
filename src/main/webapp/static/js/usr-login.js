$(function () {
    $('[data-toggle="popover"]').popover();

    $('#usernameOrEmail').bind('blur', function () {
        if ($(this).val() === ''){
            $(this).popover('show');
            $(this).css('border-color', 'red');
        }
    }).on('focus', function () {
        $(this).popover('hide');
        $(this).css('border-color', '#66afe9');
    });

    $('#password').bind('blur', function () {
        if ($(this).val() === ''){
            $(this).popover('show');
            $(this).css('border-color', 'red');
        }
    }).on('focus', function () {
        $(this).popover('hide');
        $(this).css('border-color', '#66afe9');
    });
});

$('#login-button').click(function (event) {
    event.preventDefault();
    var nameOrEmail = $('#usernameOrEmail');
    var password= $('#password');

    if (nameOrEmail.val() === ''){
        nameOrEmail.popover('show');
        nameOrEmail.css('border-color', 'red');
        return;
    }
    if (password.val() === ''){
        password.popover('show');
        password.css('border-color', 'red');
        return;
    }

    $.ajax({
        type:'post',
        url: '/usr/login',
        data: 'username=' + nameOrEmail.val() + '&password='+password.val() + '&email=' + nameOrEmail.val(),
        success: function (ret) {
            if (ret.status === 0 && ret.data === true){
                $('h1').html('Welcome');
                $('form').fadeOut(500);
                $('.wrapper').addClass('form-success');

                setTimeout(function () {
                    window.location = '/';
                }, 1000);
            }else {
                if (typeof(ret.message) === 'undefined'){
                    nameOrEmail.attr('data-content', '用户名或邮箱错误');
                    nameOrEmail.css('border-color', 'red');
                    nameOrEmail.popover('show');
                    nameOrEmail.attr('data-content', '请输入用户名或邮箱登录');
                }else {
                    password.attr('data-content', ret.message);
                    password.css('border-color', 'red');
                    password.popover('show');
                    password.attr('data-content', '登录博客密码为必需');
                }
            }
        },
        error: function () {
            alert('后台错误或网络错误')
        }
    })
});