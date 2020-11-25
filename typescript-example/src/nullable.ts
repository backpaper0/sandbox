function foo(bar?: string) {
  console.log('[foo]', bar);
}

foo('hello');
foo();
foo(undefined);
foo(null);
foo('');
//foo(123); //compile error

