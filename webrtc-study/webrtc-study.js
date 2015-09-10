var video = document.getElementById("video");
navigator.webkitGetUserMedia({ video: true, audio: true }, function(stream) {
    video.src = window.URL.createObjectURL(stream);
    var recorder = new MediaStreamRecorder(stream);
    recorder.mimeType = "video/webm";
    recorder.ondataavailable = function(blob) {
        var blobUrl = URL.createObjectURL(blob);
        var records = document.getElementById("records");
        var li = document.createElement("li");
        var a = document.createElement("a");
        a.innerText = blobUrl;
        a.href = blobUrl;
        li.appendChild(a);
        records.appendChild(li);
    };
    recorder.start(6000);
}, function() {});
