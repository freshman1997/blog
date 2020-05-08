// 时间戳转时间
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + 1 < 10 ? '0' + (date.getHours()) + ':' : date.getHours() + ':';
    var m = date.getMinutes() + 1 < 10 ? '0' + (date.getMinutes()) + ':' : date.getMinutes() + ':';
    var s = date.getSeconds() + 1 < 10 ? '0' + (date.getSeconds()) : date.getSeconds();
    return Y + M + D + h + m + s;
}

$('.row').each(function (i, v) {
    var len = $(v).children('.item').length;
    $(v).children('.item').each(function (i1, v1) {
        if (i1 === 0 || i1 === len){
            $(v1).before('<div style="width: 20px"></div>');
            $(v1).after('<div style="width: 20px"></div>');
        }else
            $(v1).after('<div style="width: 20px"></div>');
    });
});