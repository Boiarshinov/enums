# Загадки Enum'ов

Но у перечислений есть странные особенности, обнаружив которые, хочется спросить: "Почему так?".

Давайте попробуем разобраться.
<cut />

## Странность 1. Порядок инициализации

В отличие от некоторых других языков программирования в Java перечисления являются полноценными классами.
Конечно, есть некоторые особенности, например:
- нельзя наследоваться от классов (но можно реализовывать интерфейсы);
- нельзя объявлять класс финальным или абстрактным;
- нельзя создавать конструкторы с модификаторами `public` или `protected`;
- множество других ограничений (полный список можно найти в [документации](https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.9.2)).

Хорошо, смирились с запретами.
Но можем ли мы ожидать, что остальные языковые конструкции работают так же, как в остальной Java?
Например, порядок инициализации объектов.

Давайте проверим.
Для этого напишем такое перечисление:
```java
public enum Pine {
    FIR, CEDAR;

    static {
        System.out.println("Static block");
    }

    {
        System.out.println("Code block");
    }

    Pine() {
        System.out.println("Constructor");
    }
}
```

Остановитесь на секунду и попробуйте предположить что выведется в консоль при обращении к любому из значений перечисления.
```java
var fir = Pine.Fir;
```

В обычных классах при инициализации первого объекта кодовые блоки выполняются в следующем порядке:
```
Статический блок -> Кодовый блок -> Конструктор 
```

Для перечисления же мы увидим в консоли следующее:
```
> Code block
> Constructor
> Code block
> Constructor
> Static block
```

Как же так? 
Почему статический блок был вызван последним?

Для ответа на этот вопрос давайте прогоним скомпилированный класс через [Java Class File Disassembler](https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javap.html) и вручную переведем дизассемблированный код в java код.
Дизассемблинг выполняется командой:
```shell
$ javap -c Pine
```

Для самых любопытных привожу результат исполнения команды.

<spoiler title="Результат исполнения команды">

```
public final class dev.boiarshinov.enumsinjava.initorder.Pine extends java.lang.Enum<dev.boiarshinov.enumsinjava.initorder.Pine> {
  public static final dev.boiarshinov.enumsinjava.initorder.Pine FIR;

  public static final dev.boiarshinov.enumsinjava.initorder.Pine CEDAR;

  public static dev.boiarshinov.enumsinjava.initorder.Pine[] values();
    Code:
       0: getstatic     #1                  // Field $VALUES:[Ldev/boiarshinov/enumsinjava/initorder/Pine;
       3: invokevirtual #2                  // Method "[Ldev/boiarshinov/enumsinjava/initorder/Pine;".clone:()Ljava/lang/Object;
       6: checkcast     #3                  // class "[Ldev/boiarshinov/enumsinjava/initorder/Pine;"
       9: areturn

  public static dev.boiarshinov.enumsinjava.initorder.Pine valueOf(java.lang.String);
    Code:
       0: ldc           #4                  // class dev/boiarshinov/enumsinjava/initorder/Pine
       2: aload_0
       3: invokestatic  #5                  // Method java/lang/Enum.valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
       6: checkcast     #4                  // class dev/boiarshinov/enumsinjava/initorder/Pine
       9: areturn

  static {};
    Code:
       0: new           #4                  // class dev/boiarshinov/enumsinjava/initorder/Pine
       3: dup
       4: ldc           #11                 // String FIR
       6: iconst_0
       7: invokespecial #12                 // Method "<init>":(Ljava/lang/String;I)V
      10: putstatic     #13                 // Field FIR:Ldev/boiarshinov/enumsinjava/initorder/Pine;
      13: new           #4                  // class dev/boiarshinov/enumsinjava/initorder/Pine
      16: dup
      17: ldc           #14                 // String CEDAR
      19: iconst_1
      20: invokespecial #12                 // Method "<init>":(Ljava/lang/String;I)V
      23: putstatic     #15                 // Field CEDAR:Ldev/boiarshinov/enumsinjava/initorder/Pine;
      26: iconst_2
      27: anewarray     #4                  // class dev/boiarshinov/enumsinjava/initorder/Pine
      30: dup
      31: iconst_0
      32: getstatic     #13                 // Field FIR:Ldev/boiarshinov/enumsinjava/initorder/Pine;
      35: aastore
      36: dup
      37: iconst_1
      38: getstatic     #15                 // Field CEDAR:Ldev/boiarshinov/enumsinjava/initorder/Pine;
      41: aastore
      42: putstatic     #1                  // Field $VALUES:[Ldev/boiarshinov/enumsinjava/initorder/Pine;
      45: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
      48: ldc           #16                 // String Static block
      50: invokevirtual #9                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      53: return
}
```

</spoiler>

После ручного перевода в Java код получим следующее (не имеющий отношения к рассматриваемой теме код опущен):

```java
public class PineIsNotEnum extends Enum<PineIsNotEnum> {
    
    public static final PineIsNotEnum FIR;
    public static final PineIsNotEnum CEDAR;

    protected PineIsNotEnum(String name, int ordinal) {
        super(name, ordinal);
        System.out.println("Code block");
        System.out.println("Constructor");
    }

    static {
        FIR = new PineIsNotEnum("FIR", 0);
        CEDAR = new PineIsNotEnum("CEDAR", 0);
        System.out.println("Static block");
    }
}
```



## Странность 2. Отсутствующие методы


## Рекурсивная типизация


## Источники
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.9)
- [Статья о порядке инициализации перечислений](https://blogs.oracle.com/javamagazine/java-quiz-enums-initialization) в блоге Oracle
- [Ответ о порядке инициализации перечислений](https://stackoverflow.com/a/50184535/12684864) на StackOverflow