
class Fish {
  swim() {
    console.log('swim');
  }
}

class Bird {
  fly() {
    console.log('fly');
  }
}

/*
 * User-Defined Type Guardsの例
 *
 *   https://www.typescriptlang.org/docs/handbook/advanced-types.html#user-defined-type-guards
 *
 * 戻り値の定義を次のようにしてしまうと、
 *
 *   function isFish(pet: Fish | Bird): boolean {
 * 
 * 次のようなコンパイルエラーになる。
 *
 *   src/advanced-types.ts:23:9 - error TS2339: Property 'swim' does not exist on type 'Fish | Bird'.
 *     Property 'swim' does not exist on type 'Bird'.
 *   
 *   23     pet.swim();
 *              ~~~~
 *   
 *   src/advanced-types.ts:25:9 - error TS2339: Property 'fly' does not exist on type 'Fish | Bird'.
 *     Property 'fly' does not exist on type 'Fish'.
 *   
 *   25     pet.fly();
 *              ~~~
 */
function isFish(pet: Fish | Bird): pet is Fish {
  return (pet as Fish).swim !== undefined;
}

function main(getSmallPet: () => Fish | Bird) {
  const pet = getSmallPet();

  if (isFish(pet)) {
    pet.swim();
  } else {
    // ifがFishなのでelseはBirdであることが保証されるため、flyを呼び出していてもコンパイルが通る。
    pet.fly();
  }
}

main(() => new Fish());
main(() => new Bird());
