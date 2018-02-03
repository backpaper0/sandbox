//挿入ソート
export default a => {
    for (let j = 1; j < a.length; j++) {
        const key = a[j];
        let i = j - 1;
        while (i >= 0 && a[i] > key) {
            a[i + 1] = a[i];
            i = i - 1;
        }
        a[i + 1] = key;
    }
};

export const insertionSort2 = function* (a) {
    for (let j = a.length - 2; j >= 0; j--) {
        const key = a[j];
        let i = j + 1;
        while (j < a.length && a[i] < key) {
            a[i - 1] = a[i];
            yield;
            i = i + 1;
        }
        a[i - 1] = key;
        yield;
    }
};
