// boolean

const isDone: boolean = false;
console.dir({ isDone });

// number

const decimal: number = 6;
const hex: number = 0xf00d;
const binary: number = 0b1010;
const octal: number = 0o744;
console.dir({ decimal, hex, binary, octal });

// string

const fullName: string = "Bob Bobbington";
const age: number = 37;
const sentence: string = `Hello, my name is ${fullName}.

I'll be ${age + 1} years old next month.`;
console.dir({ sentence });

// Array

const list1: number[] = [1, 2, 3];

const list2: Array<number> = [1, 2, 3];

console.dir({ list1, list2 });

// Tuple

const tuple: [string, number] = ["hello", 10];

const [first, second] = tuple;

console.dir({ tuple, first, second });

// Enum

enum Color {
  Red,
  Green,
  Blue,
}

const color: Color = Color.Green;
const colorName: string | undefined = Color[2];
console.log({ color, colorName });
