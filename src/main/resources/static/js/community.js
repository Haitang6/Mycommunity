function postComment() {
    var questionId = $("#question_id").val();
    var context = $("#comment_context").val();
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":questionId,
            "context":context,
            "type":1
        }),

        success: function (response) {
            if (response.code == 200){
                $("#comment").hide();
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