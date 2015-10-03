package exercise6

trait RNG {
  def nextInt: (Int, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5deece66dL + 0xbL) & 0xffffffffffffL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }
}

object RNG {
  def nonNegativeInt(rng: RNG): (Int, RNG) = rng.nextInt match {
    case (n, r) if n == Int.MinValue => (0, r)
    case (n, r) if n < 0 => (n * -1, r)
    case x => x
  }
  def double(rng: RNG): (Double, RNG) = map(nonNegativeInt)(i => i.toDouble / Int.MaxValue)(rng)
  def intDouble(rng: RNG): ((Int, Double), RNG) = {
    val (i, rng2) = rng.nextInt
    val (d, rng3) = double(rng2)
    ((i, d), rng3)
  }
  def doubleInt(rng: RNG): ((Double, Int), RNG) = {
    val (d, rng2) = double(rng)
    val (i, rng3) = rng2.nextInt
    ((d, i), rng3)
  }
  def double3(rng: RNG): ((Double, Double, Double), RNG) = {
    val (d1, rng2) = double(rng)
    val (d2, rng3) = double(rng2)
    val (d3, rng4) = double(rng3)
    ((d1, d2, d3), rng4)
  }
  def ints(count: Int)(rng: RNG): (List[Int], RNG) = sequence(List.fill(count)(int))(rng)

  type State[+A, S] = S => (A, S)
  type Rand[+A] = State[A, RNG]

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)
  def map[A, B](ra: Rand[A])(f: A => B): Rand[B] = flatMap(ra)(a => rng => (f(a), rng))
  def nonNegativeEven: Rand[Int] = map(nonNegativeInt)(i => i - i % 2)
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = flatMap(ra)(a => flatMap(rb)(b => rng => (f(a, b), rng)))
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = rng => {
    @annotation.tailrec
    def f(rng2: RNG, rs: List[Rand[A]], as: List[A]): (List[A], RNG) = rs match {
      case Nil => (as.reverse, rng2)
      case h :: t => {
        val (a, rng3) = h(rng2)
        f(rng3, t, a :: as)
      }
    }
    f(rng, fs, Nil)
  }
  def nonNegativeLessThan(n: Int): Rand[Int] = flatMap(nonNegativeInt) { i =>
    val mod = i % n
    if (i + (n - 1) - mod >= 0) rng2 => (mod, rng2)
    else nonNegativeLessThan(n)
  }
  def flatMap[A, B](ra: Rand[A])(f: A => Rand[B]): Rand[B] = rng => {
    val (a, rng2) = ra(rng)
    f(a)(rng2)
  }
}

