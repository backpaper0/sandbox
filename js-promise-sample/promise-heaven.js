function h() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('h');
            resolve();
        }, 100);
    });
}
function e() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('e');
            resolve();
        }, 100);
    });
}
function l() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('l');
            resolve();
        }, 100);
    });
}
function o() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('o');
            resolve();
        }, 100);
    });
}
function w() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('w');
            resolve();
        }, 100);
    });
}
function r() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('r');
            resolve();
        }, 100);
    });
}
function d() {
    return new Promise(function(resolve) {
        setTimeout(function() {
            console.log('d');
            resolve();
        }, 100);
    });
}
h().then(e).then(l).then(l).then(o).then(w).then(o).then(r).then(l).then(d)

