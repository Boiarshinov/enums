package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JmhHelloWorld {

    @Benchmark
    public void helloWorld() {

    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(JmhHelloWorld.class.getName())
                .forks(1)
                .build();

        new Runner(options).run();
    }
}
