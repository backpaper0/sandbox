import sort, { insertionSort2, search } from './insertion-sort';

test('ソートされる', () => {
    const a = [5, 2, 4, 6, 1, 3];
    sort(a);
    expect(a).toEqual([1, 2, 3, 4, 5, 6]);
});

test('空の配列', () => {
    const a = [];
    sort(a);
    expect(a).toEqual([]);
});

test('長さが1の配列', () => {
    const a = [0];
    sort(a);
    expect(a).toEqual([0]);
});

describe('2.1-2 非増加順', () => {

    test('非増加順でソートされる', () => {
        const a = [5, 2, 4, 6, 1, 3];
        const iterator = insertionSort2(a);

        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 4, 6, 1, 3]);

        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 4, 1, 1, 3]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 4, 1, 3, 3]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 4, 1, 3, 6]);

        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 1, 1, 3, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 1, 3, 3, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 2, 1, 3, 4, 6]);

        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 1, 1, 3, 4, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([5, 1, 2, 3, 4, 6]);

        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([1, 1, 2, 3, 4, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([1, 2, 2, 3, 4, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([1, 2, 3, 3, 4, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([1, 2, 3, 4, 4, 6]);
        expect(iterator.next().done).toBeFalsy();
        expect(a).toEqual([1, 2, 3, 4, 5, 6]);

        expect(iterator.next().done).toBeTruthy();
    });
});

describe('2.1-3 探索問題', () => {

    test('探索できる', () => {
        expect(search([5, 2, 4, 6, 1, 3], 6)).toEqual(3);
    });

    test('存在しない', () => {
        expect(search([5, 2, 4, 6, 1, 3], 7)).toBeNull();
    });

    test('空の配列', () => {
        expect(search([], 0)).toBeNull();
    });
});