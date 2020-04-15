function search(kw){
    $('#preset').remove();
    $.ajax({
        type:'post',
        url:'/blog/query?kw=' + kw,
        success: function (ret) {
            $('.loading').css({'display': 'none', 'visibility': 'hidden'});
            if (ret.status === 0){
                if (ret.data.length > 0) {
                    var str = '';
                    for (var i = 0; i < ret.data.length; i++) {
                        str += '<div class="row">';
                        for (var j = 0; j < ret.data[i].length; j++) {
                            if (j === 0)
                                str += '<div style="width: 20px"></div>';
                            str += '<div class="item">\n' +
                                '                <a href="/blog/article/' + ret.data[i][j].mask + '">\n' +
                                '                    <img class="inner" src="' + ret.data[i][j].picUrl + '" title="' + ret.data[i][j].title + '" alt="' + ret.data[i][j].title + '"/>\n' +
                                '                </a>\n' +
                                '                <div style="justify-content: space-between">\n' +
                                '                    <span>' + timeParse(ret.data[i][j].createTime) + '</span>\n' +
                                '                    <h4><a class="b-title" href="/blog/article/' + ret.data[i][j].mask + '" title="' + ret.data[i][j].title + '" alt="' + ret.data[i][j].title + '" >' + ret.data[i][j].title + '</a></h4>\n' +
                                '                    <div style="display:inline-block;margin-bottom: 5px">\n';

                            for (var k = 0; k < ret.data[i][j].tags.length; k++) {
                                str += '<span class="label label-success" style="margin-right:5px">' + ret.data[i][j].tags[k] + '</span>'
                            }
                            str += '</div></div></div><div style="width: 20px"></div>';
                        }
                        str += '</div><div style="height: 20px"></div>';
                    }
                    console.log(str);
                    $('#no-data').css({'display': 'none', 'visibility': 'hidden'});
                    var p = $('.parent');
                    p.html('');
                    p.html(str);
                }else {
                    $('#no-data').css({'display': 'block', 'visibility': 'visible'});
                }
            }else {
                $('#no-data').css({'display': 'block', 'visibility': 'visible'});
            }
        }
    });
}
function timeParse(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '年';
    var M = date.getMonth() + 1 + '月';
    var D = date.getDate() + '日';
    return M + D + ' · ' + Y;
}

$('#search-action').click(function () {
    $('.parent').html('');
    $('.loading').css({'display':'block','visibility':'visible'});
    $('#no-data').css({'display': 'none', 'visibility': 'hidden'});
    search($('.form-control').val());
});

$('.form-control').keyup(function (e) {
    if (e.keyCode === 13){
        $('.loading').css({'display':'block','visibility':'visible'});
        $('#no-data').css({'display': 'none', 'visibility': 'hidden'});
        $('.parent').html('');
        search($(this).val());
    }
});

$(function () {
    $('.form-control').val(window.localStorage.getItem('kw'));
    window.localStorage.setItem('kw', '');

});