
document.forms.chatForm.addEventListener("submit", (evt) => {
    evt.preventDefault();
    const chatForm = evt.currentTarget;
    const query = chatForm.query.value;
    const answer = document.querySelector("#answer");
    answer.textContent = "";
    const appendAnswer = (data) => {
        answer.textContent += data;
    };
    const controls = [chatForm.query, chatForm.send]
    controls.forEach(control => {
        control.disabled = true;
    })
    const enableForm = () => {
        controls.forEach(control => {
            control.disabled = false;
        })
    };
    const es = new EventSource(`/chat/stream?query=${encodeURIComponent(query)}`);
    es.addEventListener("open", (evt) => {
        console.log("OPEN");
    });
    es.addEventListener("message", (evt) => {
        console.log("MESSAGE");
        appendAnswer(evt.data);
    });
    es.addEventListener("end", (evt) => {
        console.log("CLOSE");
        es.close();
        enableForm();
    });
});

