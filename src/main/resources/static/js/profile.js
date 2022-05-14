$(function () {
    $(".follow-btn").click(follow);
});

function follow() {
    var btn = this;
    let followee = document.querySelector("#id").value;
    var followed = $(btn).hasClass("btn-info");
    fetch(CONTEXT_PATH + "/follow", {
        method: "POST",
        headers: {"Content-Type": MEDIA_TYPE.JSON},
        body: JSON.stringify({followee, followed})
    }).then(resp => resp.json())
        .then(data => {
            if (data.code === 0) {
                window.location.reload();
            } else {
                alert(followed ? "" : "取消" + "关注失败");
            }
        })
}