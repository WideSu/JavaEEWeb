function getServerIP(url){
    var ip = '';
    $.ajax({
        url: url,
        type: 'POST',
        dataType:'text',
        cache: true,
        async:false,
        success:function (data) {
            ip = data;
        },
        error:function(){
            ip = 'localhost';
        }
    })
    return ip;
}

function hasExpResult(url, expId){
    var flag = false;
    $.ajax({
        url: url,
        type: 'POST',
        data:{expId:expId},
        cache: true,
        async:false,
        success:function (data) {
            flag = data;
        },
        error:function(data){
            flag = data;
        }
    })
    return flag;
}

function delPreExpResult(url, expId){
    $.ajax({
        url: url,
        type: 'POST',
        data:{expId:expId},
        cache: true,
        async:false,
        success:function () {
        },
        error:function(){
        }
    })
}