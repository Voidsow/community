function like(element, type, id, likedUid) {
    console.log(element)
    fetch(CONTEXT_PATH + "/like", {
        method: "POST",
        headers: {
            'Content-Type': MEDIA_TYPE.JSON
        },
        body: JSON.stringify({
            type, id, likedUid
        })
    }).then(resp => resp.json())
        .then(data => {
            console.log(data)
            if (data.code === 0) {
                element.children[0].textContent = data.data.like === true ? "已赞" : "赞";
                element.children[1].textContent = data.data.num;
            }
        })
}