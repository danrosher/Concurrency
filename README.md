
## Lockfree v's ReentrantLock Priority Queue

The lockfree implementation was taken from code from ["Multiprocessor Synchronization and Concurrent Data Structures" 2007](https://booksite.elsevier.com/9780123705914/?ISBN=9780123705914)

Here when we do a bench,mark we see that even with thread (10 concurrent) contention on a benchmark
the reentrant version is far superior to the lockfree one. 

Perhaps the implementation needs improving but it shows that Benchmarking is always important
```

Benchmark                        (num_adds)  Mode  Cnt   Score   Error  Units
PQBenchMark.concurrentAdds            10000  avgt    2   1.235          ms/op
PQBenchMark.concurrentRemoveMin       10000  avgt    2   6.161          ms/op
PQBenchMark.lockFreeAdds              10000  avgt    2  22.318          ms/op
PQBenchMark.lockFreeRemoveMin         10000  avgt    2  39.626          ms/op
```