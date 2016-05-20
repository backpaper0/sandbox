function word(x) {
    return () => {
        return new Promise(resolve => {
            setTimeout(() => {
                console.log(x);
                resolve();
            }, 100);
        });
    }
}

var h = word('h');
var e = word('e');
var l = word('l');
var o = word('o');
var w = word('w');
var r = word('r');
var d = word('d');

h().then(e).then(l).then(l).then(o).then(w).then(o).then(r).then(l).then(d)

