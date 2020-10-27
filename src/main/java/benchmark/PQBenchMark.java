package benchmark;

import concurrent.PriorityQueue;
import lockfree.SkipQueue;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1) 		// Warmup Iteration = 1
@Measurement(iterations = 2) 	// Iteration = 2
public class PQBenchMark {

    @Param({"10000"})
    private int num_adds;

    int[][] instances;
    static Random random = new Random();
    static final int RANGE = Integer.MAX_VALUE;

    @Setup
    public void setup() {
        instances = new int[num_adds][2];
        int key, value;
        for (int i = 0; i < num_adds; i++) {
            key = random.nextInt(RANGE);
            value = random.nextInt();
            instances[i] = new int[]{key,value};
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(PQBenchMark.class.getSimpleName())
            .forks(1)
            .build();
        new Runner(opt).run();
    }

    @Threads(10)
    @Benchmark
    public void lockFreeAdds(Blackhole bh){
        SkipQueue<Integer> instance = new SkipQueue<Integer>();
        for (int i = 0; i < num_adds; i++) {
            bh.consume(instance.add(instances[i][0],instances[i][1]));
        }
    }

    @Threads(10)
    @Benchmark
    public void concurrentAdds(Blackhole bh){
        PriorityQueue<Integer> instance = new PriorityQueue<Integer>();
        for (int i = 0; i < num_adds; i++) {
            bh.consume(instance.add(instances[i][0],instances[i][1]));
        }
    }

    @Threads(10)
    @Benchmark
    public void lockFreeRemoveMin(Blackhole bh){
        SkipQueue<Integer> instance = new SkipQueue<Integer>();
        for (int i = 0; i < num_adds; i++) {
            instance.add(instances[i][0],instances[i][1]);
        }
        for (int i = 0; i < num_adds; i++) {
            bh.consume(instance.removeMin());
        }
    }

    @Threads(10)
    @Benchmark
    public void concurrentRemoveMin(Blackhole bh){
        PriorityQueue<Integer> instance = new PriorityQueue<Integer>();
        for (int i = 0; i < num_adds; i++) {
            instance.add(instances[i][0],instances[i][1]);
        }
        for (int i = 0; i < num_adds; i++) {
            bh.consume(instance.removeMin());
        }
    }

}
