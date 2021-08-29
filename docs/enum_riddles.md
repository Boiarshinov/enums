# Загадки Enum'ов

Перечисления появились в пятой версии Java и с тех пор крепко обосновались в наших приложениях.
Работа с перечислениями почти не отличается от работы с любыми другими классами в Java.
Но есть несколько особенностей, которые вызывают удивление.
Каждый раз сталкиваясь с ними, хочется спросить: "Почему так?".

Давайте попробуем разобраться.
<cut />

## Порядок инициализации

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

Что же мы видим?
Значения перечисления превратились в статические финальные поля.
Выражения из кодового блока и конструктора переехали в конструктор.
Выражения из статического блока остались в статическом блоке, но до их вызова добавился код создания экземпляров.

Получается, что при первом обращении к классу перечисления, первым делом исполняется статический блок.
Все как в обычных классах.
Первое, что делается в статическом блоке - инициализируются финальные поля класса.
Это влечет за собой вызов кода, объявленного в конструкторе, и в консоль дважды выводится 
```
> Code block
> Constructor
```
И только после этого исполняется код из статического блока оригинального класса.
```
> Static block
```

Разобравшись в какой класс преобразуется `enum` при компиляции, становится понятен порядок исполнения кодовых блоков.
Для того чтобы не держать в памяти последовательность преобразований, приводящую к странному поведению, предлагаю запомнить следующее:
> Значения, объявленные в перечислении - это статические финальные поля того же типа, что и класс.
> Инициализация этих полей происходит в статическом блоке до всех остальных статических выражений.


## Отсутствующие методы

Все перечисления неявно унаследованы от абстрактного класса `Enum`.
Если заглянуть в [javadoc](https://docs.oracle.com/javase/7/docs/api/java/lang/Enum.html) на этот класс, то можно увидеть следующие методы:
```java
String name() { /* ... */ }
int ordinal() { /* ... */ }
Class<E> getDeclaringClass() { /* ... */ }
int compareTo(E o) { /* ... */ }
static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) { /* ... */ }
/* Методы класса Object */
```

Чего-то не хватает.

Если попробовать в IDE написать любое перечисление, поставить точку и вызвать автодополнение, то он предложит еще два метода:
```java
Pine[] values = Pine.values();
Pine cedar = Pine.valueOf("CEDAR");
```

В исходниках класса `Enum` таких методов нет, но они как-то появляются в каждом перечислении.

Чтобы разобраться, обратимся к документации.
[Из нее](https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.9.3) мы узнаем, что два этих метода объявлены неявно.
Почему неявно?
Дело в том, что в отличие от других методов класса `Enum` эти методы не получается реализовать в абстрактном классе.
Метод `values()` возвращает массив со всеми значениями перечисления, а класс `Enum` о них ничего не знает.
Метод `valueOf(String)` возвращает конкретное значение перечисления по его названию. Можно было бы в нем вызвать метод `valueOf(Class, String)`:
```java
public static E valueOf(String name) {
    return valueOf(E, name);
}
```
Но ничего не выходит из-за того, что класс `E` невозможно извлечь в статическом контексте.

Почему же нельзя было объявить эти методы абстрактными в `Enum`, чтобы разработчики могли хотя бы ознакомиться с их контрактом в javadoc?
Это невозможно из-за того, что методы не могут быть одновременно статическими и абстрактными.
Компилятор не поймет. А методы `valueOf(String)` и `values()` по своей природе статические.

Теперь мы понимаем, что данные методы генерируются компилятором.
Но какая же у них реализация?
В JLS она не приведена, и в исходниках JDK ее тоже не найти.

Здесь нам поможет тот же трюк с дизассемблированием.
В первой части статьи я сознательно не стал транслировать дизассемблированный код в Java-код полностью, чтобы не отвлекать внимание от инициализации.
Если же пристальнее взглянуть на фрагмент под спойлером, то можно увидеть в дополнение к константам, описывающим значения перечисления, еще одну - `VALUES`.
Она содержит в себе все значения перечисления в виде массива.
Массив заполняется сразу после инициализации значений.
Этот же массив возвращается при вызове метода `values()`:
```java
/* ... */    
private static final PineIsNotEnum[] VALUES;

static {
    FIR = new PineIsNotEnum("FIR", 0);
    CEDAR = new PineIsNotEnum("CEDAR", 1);
    VALUES = new PineIsNotEnum[] {FIR, CEDAR};
}
   
public static PineIsNotEnum[] values() {
    return VALUES;
}
```

Метод `valueOf(String)` реализуется с помощью вызова тезки:
```java
public static PineIsNotEnum valueOf(String name) {
    return Enum.valueOf(PineIsNotEnum.class, name);
}
```

Обобщая знания о неявных методах и порядке инициализации, давайте запишем как может быть представлено перечисление `Pine` из начала статьи в виде обычного класса:  
```java
public class PineIsNotEnum extends Enum<PineIsNotEnum> {

    public static final PineIsNotEnum FIR;
    public static final PineIsNotEnum CEDAR;
    private static final PineIsNotEnum[] VALUES;

    protected PineIsNotEnum(String name, int ordinal) {
        super(name, ordinal);
        System.out.println("Code block");
        System.out.println("Constructor");
    }

    static {
        FIR = new PineIsNotEnum("FIR", 0);
        CEDAR = new PineIsNotEnum("CEDAR", 1);
        VALUES = new PineIsNotEnum[] {FIR, CEDAR};
        System.out.println("Static block");
    }

    public static PineIsNotEnum[] values() {
        return VALUES;
    }

    public static PineIsNotEnum valueOf(String name) {
        return Enum.valueOf(PineIsNotEnum.class, name);
    }
}
```


## Заключение

Странности в перечислениях вызваны архитектурными решениями и ограничениями, выбранными разработчиками Java.
С помощью дизассемблирования нам удалось узнать, как перечисления инициализируются, и как в них реализованы неявные методы. 

Надеюсь, что теперь, столкнувшись с необычным поведением перечислений, вы сможете мысленно преобразовать перечисление в обычный класс и разобраться. 


## Источники
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.9);
- [Статья о порядке инициализации перечислений](https://blogs.oracle.com/javamagazine/java-quiz-enums-initialization) в блоге Oracle;
- [Ответ о порядке инициализации перечислений](https://stackoverflow.com/a/50184535/12684864) на StackOverflow.