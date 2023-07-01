
function f1(a) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve(`resolved ${a}`);
        }, 100);
    });
}

function f2(a) {
    return new Promise((_, reject) => {
        setTimeout(() => {
            reject(`rejected ${a}`);
        }, 100);
    });
}

async function g1() {
    const a = ["g1"];
    try {
        // rejectした値はcatchへ。
        a.push(await f2(1));
    } catch (e) {
        a.push(e);
        a.push(await f1(2));
    } finally {
        a.push(await f1(3));
    }
    return a;
}

async function g2() {
    const a = ["g2"];
    try {
        // スローした値はcatchへ。
        throw "thrown 1";
    } catch (e) {
        a.push(e);
        a.push(await f1(2));
    } finally {
        a.push(await f1(3));
    }
    return a;
}

(async () => {
    console.log(await g1());
    console.log(await g2());
})();

