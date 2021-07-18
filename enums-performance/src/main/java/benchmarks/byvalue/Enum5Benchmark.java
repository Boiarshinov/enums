package benchmarks.byvalue;

import com.google.common.base.Enums;
import org.apache.commons.lang3.EnumUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

//Настройки, распространяющиеся на все бенчмарки
@Fork(1)
@State(Scope.Thread) //Для каждого треда свой стейт
@BenchmarkMode(Mode.AverageTime) //
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class Enum5Benchmark {

    //Нефинальное поле для того чтобы JIT не оптимизировал выполнение метода
    private String enumName = "ENUM_5";

    @Benchmark
    public Enum5 byValueOf() {
        return Enum5.valueOf(enumName);
    }

    @Benchmark
    public Enum5 byStaticValueOf() {
        return Enum.valueOf(Enum5.class, enumName);
    }

    @Benchmark
    public Enum5 byMap() {
        return Enum5.parseByMap(enumName);
    }

    @Benchmark
    public Enum5 byStream() {
        return Enum5.parseByStream(enumName);
    }

    @Benchmark
    public Enum5 byApache() {
        return EnumUtils.getEnum(Enum5.class, enumName);
    }

    @Benchmark
    public Enum5 byGuava() {
        return Enums.getIfPresent(Enum5.class, enumName).get();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(Enum5Benchmark.class.getName())
            .forks(1)
            .build();

        new Runner(options).run();
    }
}
