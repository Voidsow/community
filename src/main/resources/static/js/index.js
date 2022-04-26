function publish() {
    $("#publishModal").modal("hide");

    let title = document.querySelector("#recipient-name").value;
    let content = document.querySelector("#message-text").value;
    let hitBody = document.querySelector("#hintBody")
    fetch("/post", {
        method: "POST",
        headers:{
          "Content-Type":"application/json"
        },
        body: JSON.stringify({
            "title": title,
            "content": content
        })
    })
        .then(response => response.json())
        .then(data => {
            hitBody.textContent = data.message
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                if (data.code === 0)
                    window.location.reload();
            }, 5000);
        })

}
var publishBtn = document.querySelector("#publishBtn");
publishBtn.onclick = publish