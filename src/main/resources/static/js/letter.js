$(function () {
    $("#sendBtn").click(send_letter);
    $(".close").click(delete_msg);
});

function send_letter() {
    $("#sendModal").modal("hide");

    let toUsername = document.querySelector("#recipient-name").value;
    let content = document.querySelector("#message-text").value;
    let hintBody = document.querySelector("#hintBody");
    fetch(CONTEXT_PATH + "/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({toUsername, content})
    })
        .then(responese => responese.json())
        .then(data => {
            if (data.code === 0)
                hintBody.textContent = "发送成功";
            else
                hintBody.textContent = "发送失败," + data.message;
            $("#hintModal").modal("show");
            setTimeout(() => {
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        })

}

function delete_msg() {
    // TODO 删除数据
    $(this).parents(".media").remove();
}