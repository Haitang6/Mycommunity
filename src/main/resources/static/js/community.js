
/*
* 一级评论请求
* */
function postComment() {
    var questionId = $("#question_id").val();
    var context = $("#comment_context").val();
    comment(questionId,1,context);
}

function replay(e) {
    var commentId =e.getAttribute("data_id");
    var context = $("#input_" + commentId).val();
    comment(commentId,2,context);

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

/*
* 二级评论,打开二级评论框框。
* */

function doubleComment(e) {
    /*
    * 获取二级评论数据
    * */
    var id=e.getAttribute("data-id")

    $.getJSON("/comment/"+id,function (data) {
       console.log(data);
       var comment = $("#comment-"+id);
       comment.toggle();
    });

}