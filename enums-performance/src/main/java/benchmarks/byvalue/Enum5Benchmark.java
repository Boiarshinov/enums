package benchmarks.byvalue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//Настройки, распространяющиеся на все бенчмарки
@Fork(1)
@State(Scope.Thread) //Для каждого треда свой стейт
@BenchmarkMode(Mode.AverageTime) //
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@OperationsPerInvocation(5) //Столько, сколько значений в перечислении
public class Enum5Benchmark {

    //Нефинальное поле для того чтобы JIT не оптимизировал выполнение метода
    private String[] values;

    @Setup
    public void prepare() {
        //Будем проходиться по всем возможным значениям, раскиданным в рандомном порядке
        List<String> valueList = EnumSet.allOf(Enum5.class).stream()
            .map(Enum5::getValue)
            .collect(Collectors.toList());
        Collections.shuffle(valueList);
        values = valueList.toArray(new String[]{});

        //Некоторые из методов создают кэш при первом вызове. Пусть лучше это делается здесь.
        Enum5.parseByForLoop("any");
        Enum5.parseByStream("any");
        Enum5.parseByMap("any");
    }

    @Benchmark
    public void forLoop(Blackhole bh) {
        for(String value: values) {
            bh.consume(Enum5.parseByForLoop(value));
        }
    }

    @Benchmark
    public void stream(Blackhole bh) {
        for(String value: values) {
            bh.consume(Enum5.parseByStream(value));
        }
    }

    @Benchmark
    public void mapCache(Blackhole bh) {
        for(String value: values) {
            bh.consume(Enum5.parseByMap(value));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(Enum5Benchmark.class.getName())
            .operationsPerInvocation(5)
            .forks(1)
            .build();

        new Runner(options).run();
    }
}
