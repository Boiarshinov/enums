package benchmarks.byname;

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
@OperationsPerInvocation(100) //Столько, сколько значений в перечислении
public class Enum100Benchmark {

    //Нефинальное поле для того чтобы JIT не оптимизировал выполнение метода
    private String[] names;

    @Setup
    public void prepare() {
        //Будем проходиться по всем возможным значениям, раскиданным в рандомном порядке
        List<String> nameList = EnumSet.allOf(Enum100.class).stream()
            .map(Enum100::name)
            .collect(Collectors.toList());
        Collections.shuffle(nameList);
        names = nameList.toArray(new String[]{});

        //Некоторые из методов создают кэш при первом вызове. Пусть лучше это делается здесь.
        Enum100.diyValueOf("any");
        Enum100.diyStaticValueOf("any");
        Enum100.apacheValueOf("any");
        Enum100.guavaValueOf("any");
    }

    @Benchmark
    public void diy(Blackhole bh) {
        for(String name: names) {
            bh.consume(Enum100.diyValueOf(name));
        }
    }

    @Benchmark
    public void diyStatic(Blackhole bh) {
        for(String name: names) {
            bh.consume(Enum100.diyStaticValueOf(name));
        }
    }

    @Benchmark
    public void apache(Blackhole bh) {
        for(String name: names) {
            bh.consume(Enum100.apacheValueOf(name));
        }
    }

    @Benchmark
    public void guava(Blackhole bh) {
        for(String name: names) {
            bh.consume(Enum100.guavaValueOf(name));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(Enum100Benchmark.class.getName())
            .operationsPerInvocation(100)
            .forks(1)
            .build();

        new Runner(options).run();
    }
}
