const video = document.querySelector('video');

document.querySelector('track')
    .addEventListener('load', render, false);

function render() {
    const tracks = document.querySelector('#tracks');

    Array.from(video.textTracks[0].cues)
        .map(seeker)
        .forEach(s => tracks.appendChild(s));
}

function seeker(cue) {
    const s = document.createElement('a');
    s.classList = 'track list-group-item list-group-item-action py-1';
    s.innerHTML = cue.text;
    s.setAttribute('data-start', cue.startTime);
    s.addEventListener('click', seek);
    return s;
}

function seek(e) {
    video.currentTime = this.getAttribute('data-start');
    if (video.paused) video.play();
}
