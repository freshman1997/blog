var token = null;

function fetchEmailCode(){
    var targetEmail = $('#email').val();
    var regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (regEmail.test(targetEmail)) {
        $('#desc-code').attr({'disabled':'true'});
        $.ajax({
            type:'post',
            url:'/usr/send-email-code?e='+targetEmail,
            success: function (ret) {
                if (ret.status === 0){
                    //todo 设置2分钟倒计时
                    second = 0;
                    interval = setInterval(changeCodeDescText, 1000);
                }else{
                    alert('发送失败！请检查邮箱是否正确！');
                    $('#desc-code').removeAttr('disabled');
                }

            }
        });
    }else{
        alert("邮箱不正确！");
    }
}
var second = 0;
var interval = null;
function changeCodeDescText(){
    second++;
    var desc = $('#desc-code');
    if (second === 120) {
        desc.removeAttr('disabled').text('获取邮箱验证码');
        clearInterval(interval);
        return;
    }
    desc.attr({'disabled':'true'}).text((120 - second ) + '秒后重试');
}
$(function () {
    $.ajax({
        type:'post',
        url:'/usr/code/generate-user-register-token',
        headers:{'Generate-User-Register-Token':true},
        success:function (ret) {
            token = ret.data;
        }
    });

    var size = 512; //500k
    var fileExtensions = ".jpg|.jpeg|.png";
    $("#headPhoto").change(function () {
        checkFiles(size, fileExtensions, $(this));
    });
    function checkFiles(filesMaxSize, filesAllow, _this) {
        var fileObjs = _this;
        var maxSize = 512;//500k
        var fileExtensionArr = filesAllow.split("|");
        var fileExtensionAllow = false;
        if (fileObjs.length > 0) {
            fileObjs.each(function () {
                var $fileObj = $(this);
                for (var i = 0; i <= this.files.length; i++) {
                    var file = this.files.item(i);
                    if (file) {
                        var size = file.size / 1024;
                        //1.0-过滤文件大小
                        if (size > maxSize) {
                            alert("文件大小超过限制，不能超过" + filesMaxSize + "k");
                            $fileObj.val("");
                            return false;
                        }
                        //2.0 检查扩展名
                        var pos = file.name.lastIndexOf(".");
                        var extension = file.name.substring(pos, file.name.length);
                        fileExtensionArr.forEach(function (n) {
                            if (n.toLowerCase() === extension.toLowerCase()) {
                                fileExtensionAllow = true;
                            }
                        });
                        if (!fileExtensionAllow) {
                            alert("该文件类型不支持，仅支持以下文件类型" + filesAllow);
                            $fileObj.val("");
                            return false;
                        }
                    }
                }
            });
        } else {
            alert('所选图片文件为空文件！');
        }
    }

    $('#submit').click(function () {
        var ret = submitCheckout();
        if (ret !== 'null') {
            ajaxReqRegister(ret)
        }
    });

    $('#username').focus(function () {
        $(this).css('border-color', '#66afe9').popover('hide');
    }).bind('blur', function () {
        if ($(this).val() === '') {
            $(this).popover('show');
            $(this).css('border-color', 'red');
            return;
        }
        var _this = $(this);
        $.ajax({
            type: 'get',
            url: '/usr/check-unique-username?username=' + $(this).val(),
            contentType: 'application/json;charset=utf-8',
            success: function (ret) {
                if (ret.data !== 0) {

                    _this.attr('data-content', '该用户名已存在');
                    _this.popover('show');
                    _this.css('border-color', 'red');
                }
            },
            error: function (e) {
                alert('请求错误' + e);
            }
        });
    });

    $('#password').focus(function () {
        $(this).css('border-color', '#66afe9').popover('hide');
    }).bind('blur', function () {
        if ($(this).val() === '') {
            $(this).popover('show');
            $(this).css('border-color', 'red');
        }
    });

    $('#email').focus(function () {
        $(this).css('border-color', '#66afe9').popover('hide');
    }).bind('blur', function () {
        if ($(this).val().trim() === '') {
            $(this).popover('show');
            $(this).css('border-color', 'red');
            return;
        }
        var _this = $(this);
        // todo 确保邮箱格式正确
        var regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        if (regEmail.test($(this).val())) {
            // todo 一个账号绑定一个邮箱
            $.ajax({
                type: 'get',
                url: '/usr/check-unique-email?email=' + _this.val(),
                contentType: 'application/json;charset=utf-8',
                success: function (data) {
                    if (data.data !== 0) {
                        _this.attr('data-content', '该邮箱已被注册');
                        _this.popover('show');
                        _this.css('border-color', 'red');
                    }
                },
                error: function (e) {
                    alert('请求错误' + e);
                }
            });
        } else {
            $(this).css('border-color', 'red');
            $(this).attr('data-content', '邮箱格式错误！');
            $(this).popover('show');
        }
        $(this).attr('data-content', '邮箱为必需，用于找回密码和接收验证码');
    });

});
function ajaxReqRegister(data) {

    if (token === null){
        alert('发布失败，未能获取token，请刷新后尝试！');
        return;
    }

    $.ajax({
        type: 'post',
        url: '/usr/register',
        processData: false,
        contentType: false,
        data: data,
        success: function (ret) {
            if (ret.status === 0){
                var time = 0;
                $('.alert').removeClass('hide');
                setInterval(function () {
                    time ++;

                    $('#control').html(5 - time);
                    if (time === 5){
                        $('.alert').addClass('hide');
                        $(window).attr('location','/');
                    }
                }, 1000);
            }else {
                alert(ret.message);
            }
        },
        error: function (e) {
            alert('请求错误' + e);
        }
    });
}
function submitCheckout() {
    var username = $('#username');
    var password = $('#password');
    var email = $('#email');
    var gender = $('#gender');
    var birthday = $('#birthday');
    var headPhoto = $('#headPhoto');

    if (username.val() === '') {
        username.popover('show');
        username.css('border-color', 'red');
        return 'null'
    }

    if (password.val() === '') {
        password.popover('show');
        password.css('border-color', 'red');
        return 'null'
    }

    if (email.val() === '') {
        email.popover('show');
        email.css('border-color', 'red');
        return 'null';
    }
    if ($("#code").val() === '')
    {
        alert('请填写验证码！');
        return 'null';
    }

    // todo 发送注册请求

    var formData = new FormData();

    if (token != null)
        formData.append("token", token);

    formData.append("code", $('#code').val());
    formData.append('username', username.val());
    formData.append('password', password.val());
    formData.append('email', email.val());
    formData.append('gender', gender.val());
    formData.append('birthday', birthday.val());
    formData.append('headPhoto', headPhoto[0].files[0]);
    return formData;
}