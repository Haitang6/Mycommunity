
/*
* 一级评论请求
* */
function postComment() {
    var questionId = $("#question_id").val();
    var context = $("#comment_context").val();
    comment(questionId,1,context);
}
/*
* 二级回复
* */

function replay(e) {
    var commentId =e.getAttribute("data_id");
    var context = $("#input_" + commentId).val();
    comment(commentId,2,context);
    doubleComment(e);
}

function comment(targetId,type,context) {
    if (!context){
        alert("回复不能为空");
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":targetId,
            "context":context,
            "type":type
        }),

        success: function (response) {
            if (response.code == 200){
                //隐藏回复输入框
                // $("#comment").hide();
                //回复成功之后，直接刷新，加载出新回复内容。
                window.location.reload();
            }else {
                if (response.code == 2001) {
                    //    登录操作
                    var islogin = confirm(response.message);
                    //     确定登录
                    if (islogin){
                        window.open("https://github.com/login/oauth/authorize?client_id=7f42e4939e2024478664&rediect_url=http://localhost:9999/callback&scope=user&state=1");

                        window.localStorage.setItem("closable",true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType:"json"
    });
}


function doubleComment(e) {
    /*
    * 获取二级评论数据
    * */
    var id=e.getAttribute("data_id")
    // var subCommentContainer = $("#comment-" + id);

    $.getJSON("/comment/"+id,function (data) {
       var subCommentContainer = $("#comment-"+id);
        $.each(data.data.reverse(), function (index, comment) {
            var mediaLeftElement = $("<div/>", {
                "class": "media-left"
            }).append($("<img/>", {
                "class": "media-object img-rounded",
                "src": comment.user.avatarUrl
            }));

            var mediaBodyElement = $("<div/>", {
                "class": "media-body"
            }).append($("<h5/>", {
                "class": "media-heading",
                "html": comment.user.name
            })).append($("<div/>", {
                "html": comment.context
            })).append($("<div/>", {
                "class": "menu"
            }).append($("<span/>", {
                "class": "pull-right",
                "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
            })));

            var mediaElement = $("<div/>", {
                "class": "media"
            }).append(mediaLeftElement).append(mediaBodyElement);

            var commentElement = $("<div/>", {
                "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
            }).append(mediaElement);

            subCommentContainer.prepend(commentElement);
        });
        subCommentContainer.toggle();
    });
}