import sort from './insertion-sort';

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
