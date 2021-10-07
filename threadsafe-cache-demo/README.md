# threadsafe-cache-demo

```console
mvn package

# benchmark
java -jar target/benchmarks.jar

# help
java -jar target/benchmarks.jar -h
```

```
Result "com.example.CacheBenchmark.synchronizedCache":
  287446308.060 ±(99.9%) 2536733.648 ops/s [Average]
  (min, avg, max) = (275795887.114, 287446308.060, 290338621.538), stdev = 3386466.844
  CI (99.9%): [284909574.411, 289983041.708] (assumes normal distribution)


# Run complete. Total time: 00:33:26

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                Mode  Cnt          Score         Error  Units
CacheBenchmark.atomicBooleanCache       thrpt   25  397792878.797 ± 7672808.077  ops/s
CacheBenchmark.doubleCheckLockingCache  thrpt   25  429571538.895 ± 4260406.743  ops/s
CacheBenchmark.futureTaskCache          thrpt   25  337751773.404 ± 4025801.779  ops/s
CacheBenchmark.synchronizedCache        thrpt   25  287446308.060 ± 2536733.648  ops/s
```

