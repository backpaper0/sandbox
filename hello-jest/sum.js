//@flow
export default (a: number, b: number): number => a + b;

export const fn1 = (a: 'foo' | 'bar'): string => {
  switch (a) {
    case 'foo': return 'FOO';
    case 'bar': return 'BAR';
    default: throw Error();
  }
};

export const id = <T>(t: T): T => t;

export const fn2 = (opt: ?string): string => {
  if (opt != null) {
    return opt;
  }
  return '';
};
