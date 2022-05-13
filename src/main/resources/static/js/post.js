function like(element, type, id) {
    console.log(element)
    fetch(CONTEXT_PATH + "/like", {
        method: "POST",
        headers: {
            'Content-Type': MEDIA_TYPE.JSON
        },
        body: JSON.stringify({
            type, id
        })
    }).then(resp => resp.json())
        .then(data => {
            console.log(data)
            if (data.code === 0) {
                element.children[0].textContent = data.data.like === "yes" ? "已赞" : "赞";
                element.children[1].textContent = data.data.num;
            }
        })
}