# Вступление для доклада

Многие пришедшие на этот доклад могут думать: "Что нового можно сказать про перечисления? Я пользуюсь ими каждый день и знаю о них наверное все".
Позволю себе усомниться в этом.

Для того чтобы заинтересовать вас, я задам вам несколько вопросов о перечислениях.
Приводить ответы на эти вопросы я не буду (а то вдруг вы начнете задавать такие вопросы на собеседованиях c:), но если вы сильно захотите узнать ответы, то можете подойти ко мне после доклада.

## Порядок выполнения кодовых блоков

Что будет выведено в консоль?
```java
public class FirstQuiz {
    public static void main(String[] args) {
        System.out.print("A ");
        var enumInit = EnumInit.ONE;
    }

    enum EnumInit {
        ONE;

        static {
            System.out.print("B ");
        }

        {
            System.out.print("C ");
        }

        EnumInit() {
            System.out.print("D ");
        }
    }
}
```

```
1) A B C D
2) B A C D
3) B C D A
4) A C D B
5) Никакой из перечисленных
```

Правильный ответ - 4.
Это значит, что перечисления инициализируются лениво, а обычные кодовые блоки и конструкторы исполняются раньше статических блоков.
Но почему так?

## Несуществующие методы

Если вы откроете класс `Enum`, то увидите в нем следующие публичные методы:
```java
String name();
int ordinal();
Class<E> getDeclaringClass();
int compareTo(E o);
static <T extends Enum<T>> T valueOf(Class<T> enumType, String name);
/* Методы класса Object */
```
То же самое мы увидим в [документации Oracle](https://docs.oracle.com/javase/8/docs/api/java/lang/Enum.html).

Подождите, но я точно помню, что использовал еще и статические методы `values()`, `valueOf(String name)`
```java
DayOfWeek[] values = DayOfWeek.values();
DayOfWeek sunday = DayOfWeek.valueOf("SUNDAY");
```
Куда же они пропали?

## Рекурсивная типизация

Дженерики и перечисления появились в Java одновременно.
Джошуа Блох оторвался и сделал класс `Enum` рекурсивно обобщенным:
```java
public abstract class Enum<E extends Enum<E>>
```
Это вообще как?
Как компилятор не спотыкается, пытаясь разрулить рекурсию типов?
Что изменится, если убрать одно из обобщений?
```java
public abstract class Enum<E extends Enum>
```

## Ответы:
### Порядок выполнения кодовых блоков
Дело в том, что при компиляции все конструкторы и нестатические блоки 
в перечислениях преобразуются в статические блоки, которые устанавливаются в самой вершине генерируемого класса.
Поэтому они исполняются раньше статического блока.
Примерно так будет выглядеть 
```java
class EnumInit extends Enum<EnumInit> {
    static { //Преобразовано из нестатического блока
        System.out.print("C ");
    }
    public static EnumInit ONE = new EnumInit();
    static { //Преобразовано из конструктора
        System.out.print("D ");
    }
    
    static { //Оставшийся статический блок
        System.out.print("B ");
    }
    /* ... */
}
```

### Несуществующие методы
Согласно [спецификации Java](https://docs.oracle.com/javase/specs/jls/se11/html/jls-8.html#jls-8.9.3) эти два метода являются неявно объявленными.
Их реализация создается на этапе компиляции отдельно для каждого перечисления.

Если заглянуть в байткод, то можно увидеть, что `valueOf(String)` на самом деле является синтаксическим сахаром над статическим методом `valueOf(Clacc, String)` в `Enum`:
```java
public final enum singleton/EnumSingleton extends java/lang/Enum {
  //...  
    
  // access flags 0x9
  public static values()[Lsingleton/EnumSingleton;
   L0
    LINENUMBER 5 L0
    GETSTATIC singleton/EnumSingleton.$VALUES : [Lsingleton/EnumSingleton;
    INVOKEVIRTUAL [Lsingleton/EnumSingleton;.clone ()Ljava/lang/Object;
    CHECKCAST [Lsingleton/EnumSingleton;
    ARETURN
    MAXSTACK = 1
    MAXLOCALS = 0

  // access flags 0x9
  public static valueOf(Ljava/lang/String;)Lsingleton/EnumSingleton;
   L0
    LINENUMBER 5 L0
    LDC Lsingleton/EnumSingleton;.class
    ALOAD 0
    INVOKESTATIC java/lang/Enum.valueOf (Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
    CHECKCAST singleton/EnumSingleton
    ARETURN
   L1
    LOCALVARIABLE name Ljava/lang/String; L0 L1 0
    MAXSTACK = 2
    MAXLOCALS = 1
    
  // ...  
}
```

### Рекурсивная типизация
Чтобы понять, почему компилятор не уходит в бесконечную рекурсивную проверку, рассмотрим пример. 
Есть класс `E`, который унаследован от класса `Clazz<E>`:
```java
final class E extends Clazz<E> {}
```
Компилятор запомнил, что `E extends Clazz<E>`.
Теперь он переходит к проверке типов, объявленной в `Clazz`:
```java
abstract class Clazz<E extends Clazz<E>> {}
```
Теперь ему достаточно сравнить, что `E extends Clazz<E>` == `E extends Clazz<E>`. 
И никакой рекурсии.

#### Что произойдет, если убрать рекурсивную типизацию?
Если убрать рекурсивную типизацию, то для методов, объявленных в классе `Enum` ничего не изменится.
```java
abstract class Enum<E extends Enum> { /* ... */} 
```
Все методы будут работать корректно, даже `compareTo(E)`.
Подробное обсуждение этого вопроса можно найти на [StackOverflow](https://stackoverflow.com/questions/3067891/what-would-be-different-in-java-if-enum-declaration-didnt-have-the-recursive-pa).