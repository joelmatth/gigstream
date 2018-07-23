var v = document.querySelector("video");
var t = document.querySelector("track");
var b = document.querySelector("#tracks");

t.addEventListener('load',render,false);

function play() {
    if(v.paused) { v.play(); } else { v.pause(); }
}

function render() {
    var c = v.textTracks[0].cues;
    for (var i=0; i<c.length; i++) {
        var s = document.createElement("div");
        s.innerHTML = c[i].text;
        s.setAttribute('data-start',c[i].startTime);
        s.addEventListener("click",seek);
        b.appendChild(s);
    }
}

function seek(e) {
    v.currentTime = this.getAttribute('data-start');
    if(v.paused) { v.play(); }
}