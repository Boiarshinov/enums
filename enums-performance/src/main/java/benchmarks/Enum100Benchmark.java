package benchmarks;

import com.google.common.base.Enums;
import org.apache.commons.lang3.EnumUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
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
@State(Scope.Thread) //Для каждого треда свой стейт
@BenchmarkMode(Mode.AverageTime) //
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class Enum100Benchmark {

    //Нефинальное поле для того чтобы JIT не оптимизировал выполнение метода
    private String enumName = "ENUM_70";

    @Benchmark
    public Enum100 byValueOf() {
        return Enum100.valueOf(enumName);
    }

    @Benchmark
    public Enum100 byStaticValueOf() {
        return Enum.valueOf(Enum100.class, enumName);
    }

    @Benchmark
    public Enum100 byMap() {
        return Enum100.parseByMap(enumName);
    }

    @Benchmark
    public Enum100 byStream() {
        return Enum100.parseByStream(enumName);
    }

    @Benchmark
    public Enum100 byApache() {
        return EnumUtils.getEnum(Enum100.class, enumName);
    }

    @Benchmark
    public Enum100 byGuava() {
        return Enums.getIfPresent(Enum100.class, enumName).get();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(Enum100Benchmark.class.getName())
            .forks(1)
            .build();

        new Runner(options).run();
    }
}
