# Effective enums

<mark>todo введение. Тут нужно объявить в какой предметной области будут приведены примеры</mark>

Содержание:
- [X] [Введение](talk_intro.md)
- [X] Перечисления в публичном API
  - [X] JSON
  - [ ] Бинарные форматы  
- [ ] Перечисления в БД
- [X] Получение перечисления по значению
  - [X] Сравнение перфоманса
- [ ] Разделение слоев
- [ ] Коллекции с перечислениями
- [ ] Перечисление как Singleton
- [ ] Внутреннее перечисление как способ организации бизнес-логики
- [X] Использование перечислений в тестах
  - [X] Проход по значениям перечисления
  - [ ] Генерация тестовых моделей
- [ ] Выводы

## Перечисления в публичном API
По поводу использования перечислений в публичном API сломано немало копий.
Например, на последнем JPoint был [доклад][enums-in-api-jpoint], осуждающий использование перечислений в API в большинстве случаев.

При рассмотрении использования перечислений в публичном API нужно разделять получение и отдачу перечислений.
Отдавать перечисления - хорошо. 
Клиенты вашего API будут рады, что часть полей может принимать только ограниченное количество значений.
Это позволяет клиентам заложить различную логику обработки приходящих значений.
Коды состояния http и MIME-типы тоже являются примерами перечислений в API.

Принимать перечисления - тоже хорошо (но уже не так сильно).
Когда вы выступаете в качестве клиента чужого API или принимаете значения от пользователя, нужно провести анализ: каким образом обрабатывать входящие значения.
То, что поле имеет строго определенный перечень возможных значений, еще не значит, что вы обязательно должны использовать перечисление.
Я считаю, что если логика вашего приложения не разветвляется в зависимости от значения перечисления, то вполне можно использовать для передачи и хранения поля обычные строки.
В любом ином случае лучше использовать перечисления, при этом всегда стоит перестраховаться на получение неожиданных значений.

Рассмотрим в качестве примера интеграцию с Google ReCaptcha.
В официальной документации написано, что API может возвращать список ошибок, часть из которых является восстанавливаемыми, а часть нет.
Список возможных ошибок приведен на картинке:

![recaptcha-errors](img/recaptcha_errors.png)

Логика обработки ответа будет зависеть от конкретного типа ошибки, поэтому мы используем перечисление:
```java
@RequiredArgsConstructor
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate");

    private final String value; //Логика получения перечисления из строки опущена

    public static final EnumSet<ErrorCode> RECOVERABLE_ERRORS = EnumSet.of(
            MISSING_INPUT_RESPONSE,
            INVALID_INPUT_RESPONSE,
            TIMEOUT_OR_DUPLICATE
    );
}
```
Для того чтобы заложить логику обработки ошибок, мы даже создали множество восстанавливаемых ошибок.

Но вот беда, когда мы начали тестировать интеграцию с сервисом Google ReCaptcha, оказалось, что у него есть незадокументированное поведение.
В ряде случаев их API возвращает в коде ошибки значение `invalid-keys`.
Худшее, что можно сделать в этой ситуации - ограничиться добавлением нового значения `INVALID_KEYS` в перечисление.
```java
@RequiredArgsConstructor
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys");

    private final String value; //Логика получения перечисления из строки опущена
    /* ... */
}
```

Возможно это не единственное незадокументированное значение, а другие просто не были обнаружены при тестировании.

Что же делать?
На мой взгляд, лучшее решение - создать отдельное перечисление для всех незадокументированных значений.
При парсинге входящей строки любое незнакомое значение будет преобразовано в специальное перечисление.
Обработка этого перечисления будет реализована в сервисе, ответственном за обработку ответа от внешнего сервиса.
```java
@RequiredArgsConstructor
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys"),
    UNEXPECTED("unexpected");

    private final String value; //Логика получения перечисления из строки опущена
    /* ... */
}
```

Сейчас я умышленно не показываю каким образом происходит преобразование строки в перечисление. 
Этому посвящен отдельный раздел чуть ниже.

А теперь рассмотрим какие существуют инструменты для преобразования перечислений в различные форматы представления данных и обратно.

### JSON
К сожалению, в синтаксисе JSON нет возможности выделить перечисления, поэтому приходится передавать значение enum в виде строки или целого числа.
Но это не повод не использовать перечисления в Java коде.
Если вы используете Jackson для сериализации в JSON, то он прекрасно справляется с преобразованием перечислений в строки.
```java
// DayOfWeek - стандартное перечисление из пакета java.time
assertEquals("\"MONDAY\"", objectMapper.writeValueAsString(DayOfWeek.MONDAY));
```

Все клиенты вашего API будут бесконечно благодарны, если в документации на API вы опишете все возможные значения перечисляемого поля. 
Многие openApi генераторы (я использую [springdoc][springdoc-github]), преобразующие DTO в openApi схему, умеют работать с перечислениями. 
Например, вот такой DTO
```java
public class Lesson {

    private String discipline;
    private DayOfWeek dayOfWeek; //Стандартное перечисление из java.time
    private int order;
    private String cabinet;
}
```

будет автоматически преобразован в 
```yaml
Lesson:
  type: object
  properties:
    discipline:
      type: string
    dayOfWeek:
      type: string
      enum:
      - MONDAY
      - TUESDAY
      - WEDNESDAY
      - THURSDAY
      - FRIDAY
      - SATURDAY
      - SUNDAY
    order:
      type: integer
      format: int32
    cabinet:
      type: string
```
<mark>todo</mark>

#### Почему ваш JSON на меня орет?

При интеграции с каким-либо сервисом перечисления в его API могут быть заданы с помощью магических чисел или непривычным для Java кодстайла способом.
Например, в [API Яндекс.Маркета][yandex-api] статусы задаются в числовом виде, а в [API Google ReCaptcha][recaptcha-api] коды ошибок задаются в кебаб-кейсе. 
В своем коде же мы хотели бы видеть эти значения в виде перечислений.

Для перевода значения в перечисление можно было бы написать конвертер, но Jackson предоставляет более удобный способ.
Достаточно поставить над полем в вашем перечислении аннотацию `@JsonValue`.
```java
@Getter
@RequiredArgsConstructor
public enum Grade {
  EXCELLENT('A'),
  SATISFACTORY('B'),
  MEDIOCRE('C'),
  INSUFFICIENT('D'),
  FAILURE('F');
    
  @JsonValue
  private final char mark;
}
```
Теперь при сериализации такого перечисления в JSON он будет приведен к значению, помеченному `@JsonValue`.
То же работает и в обратную сторону: при парсинге JSON Jackson будет автоматически переводить строковое значение в перечисление.

Ранее мы разобрали, что при получении данных из внешних источников в большинстве случаев стоит закладывать отдельное перечисление для незадокументированных значений.
Поэтому при получении данных для парсинга лучше использовать не `@JsonValue`, а статический метод генерации, помеченный аннотацией `@JsonCreator`:
```java
@JsonCreator
public static ErrorCode parse(String value) {
    return Optional.ofNullable(VALUE_TO_ENUM_MAP.get(value))
        .orElse(ErrorCode.UNEXPECTED);
}
```

### Protobuf и Avro
<mark>todo</mark>

## Перечисления в БД
Многие СУБД позволяют создавать в таблицах колонки с перечисляемым типом.
Но это влечет за собой некоторые проблемы:
- Добавление нового значения перечисления влечет за собой изменение схемы
- Liquibase не поддерживает перечисляемые типы при описании миграции с помощью xml. Из-за этого такие миграции приходится писать на SQL, что требует написания еще и down миграций.

Зато взамен вы получаете увеличенный перфоманс при фильтрации по этой колонке и существенно сокращаете размер индекса по этой колонке.

При использовании JPA можно над полем перечисляемого типа в сущности поставить аннотацию `@Enumerated`.
```java
@Enumerated(EnumType.STRING)
private Type type;
```
После этого обращаться с такими перечислениями нужно вдвойне осторожно, т.к. при изменении названия одного из значений, можно получить ошибки в рантайме при переводе `ResultSet` в объект доменной области.

## Разделение слоев
<mark>todo</mark>

## Получение перечисления по значению

В предыдущих разделах мы увидели, что при использовании перечислений постоянно приходится преобразовывать перечисления в строки и наоборот.
Если с прямым преобразованием все понятно - достаточно вызвать метод `name()` или дернуть нужный геттер, то с обратным не все так просто.

Здесь хотелось бы рассмотреть отдельно парсинг значения перечисления по названию этого значения и по отдельному полю.

### Получение перечисления по названию

На первый взгляд ответ кажется очевидным: нужно использовать метод `valueOf()`, существующий во всех перечислениях, и дело с концом.
```java
var dayOfWeek = DayOfWeek.valueOf("SUNDAY");
```
Здесь мы встречаемся с небольшой проблемой - метод `valueOf()` падает с `IllegalArgumentException` при попытке распарсить несуществующее значение.
Но в ряде случаев мы хотим, чтобы все несуществующие значения парсились в одно конкретное перечисление.
Для обработки таких ситуаций рождается следующий код:
```java
public static ErrorCode valueOfOrDefault(String name) {
    try {
        return ErrorCode.valueOf(name);
    } catch (IllegalArgumentException ignored) {
        return ErrorCode.UNEXPECTED;
    }
}
```

Ах, если бы `valueOf()` возвращал `Optional`!

Здесь нам на выручку приходят библиотеки Apache Commons и Guava.
В них есть утилитные классы `EnumUtils` и `Enums` соответственно, которые предоставляют различные удобства для работы с перечислениями.
В Guava есть метод, возвращающий `Optional`. Правда это Guav'овский `Optional`, поэтому выглядит немного непривычно:
```java
public static ErrorCode valueOfOrDefault(String name){
    return Enums.getIfPresent(ErrorCode.class, name)
        .or(ErrorCode.UNEXPECTED);
}
```

С использованием Apache Commons это будет выглядеть так:
```java
public static ErrorCode valueOfOrDefault(String name){
    return EnumUtils.getEnum(ErrorCode.class, name, ErrorCode.UNEXPECTED);
}
```

А есть ли разница между этими тремя подходами?
Ну и что с того, что самописная реализация получилась с наибольшим количеством строк, зато своя.
Но может быть разработчики Apache Commons или Guava учли какие-то подводные камни или их реализации намного более производительные?
Давайте заглянем внутрь.

Как уже отмечалось в начале доклада, реализация метода `valueOf()` в классе `Enum` нам неизвестна.
Дело в том, что она генерируется для каждого конкретного перечисления на этапе компиляции.
Но мы можем посмотреть на реализации в библиотеках и попробовать сравнить производительность.

Реализация Apache Commons написана в лоб и по сути повторяет нашу собственную:
```java
public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName, final E defaultEnum) {
    if (enumName == null) {
        return defaultEnum;
    }
    try {
        return Enum.valueOf(enumClass, enumName);
    } catch (final IllegalArgumentException ex) {
        return defaultEnum;
    }
}
```
А вот реализация Guava уже интереснее: в утилитном классе `Enums` прячется кэш слабых ссылок, который заполняется при первом обращении к конкретному перечислению:
```java
private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>>
      enumConstantCache = new WeakHashMap<>();

private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
    Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<>();
    for (T enumInstance : EnumSet.allOf(enumClass)) {
      result.put(enumInstance.name(), new WeakReference<Enum<?>>(enumInstance));
    }
    enumConstantCache.put(enumClass, result);
    return result;
}
```
Кэш представляет собой мапу 'имя перечисления' - 'значение'.

А помните, в начале доклада я упоминал, что в классе `Enum` есть еще один метод `valueOf()` - статический?
Так вот его реализацию мы можем посмотреть и там внутри будет так же кэш в виде мапы:
```java
private transient volatile Map<String, T> enumConstantDirectory;

Map<String, T> enumConstantDirectory() {
    Map<String, T> directory = enumConstantDirectory;
    if (directory == null) {
        T[] universe = getEnumConstantsShared();
        if (universe == null)
            throw new IllegalArgumentException(
                getName() + " is not an enum type");
        directory = new HashMap<>((int)(universe.length / 0.75f) + 1);
        for (T constant : universe) {
            directory.put(((Enum<?>)constant).name(), constant);
        }
        enumConstantDirectory = directory;
    }
    return directory;
}
```

Возможно кэши будут отрабатывать быстрее родной реализации `valueOf()`.
Для того чтобы проверить это, я написал [бенчмарки с использованием JMH](/enums-performance/src/main/java/benchmarks/byname).
Сравнивалась производительность для различных размеров перечислений: с 5 и 100 значениями.
Бенчмарки гонялись на моем личном компьютере с процессором Intel Core i5-8400 и 16 ГБ оперативной памяти.
Результаты представлены ниже:

Для 5 элементов:
```
Benchmark                 Mode  Cnt   Score   Error  Units
Enum5Benchmark.apache     avgt    5   7,088 ± 0,101  ns/op
Enum5Benchmark.diy        avgt    5   6,482 ± 0,027  ns/op
Enum5Benchmark.diyStatic  avgt    5   6,231 ± 0,041  ns/op
Enum5Benchmark.guava      avgt    5  12,538 ± 0,027  ns/op
```

Для 100 элементов:
```
Benchmark                   Mode  Cnt   Score   Error  Units
Enum100Benchmark.apache     avgt    5   7,947 ± 1,008  ns/op
Enum100Benchmark.diy        avgt    5   7,144 ± 0,822  ns/op
Enum100Benchmark.diyStatic  avgt    5   7,225 ± 0,874  ns/op
Enum100Benchmark.guava      avgt    5  13,271 ± 2,412  ns/op
```
<mark>нарисовать графики</mark>

Как видно из результатов: Guava переиграла саму себя со своим оверинжинирингом, 
а Apach'евский `getEnum()` оказался чуть медленнее стандартных Jav'овых `valueOf()`.
Разница же в различных методах `valueOf()` незначительна и победа находится то на стороне статической реализации, 
то на стороне нестатической в зависимости от количества значений в перечислении.


### Получение перечисления по параметру

Теперь рассмотрим случай, когда нам необходимо получиться значение перечисления по параметру.
Для этого обратимся к рассмотренному ранее классу `ErrorCode`: 
```java
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys"),
    UNEXPECTED("unexpected");
    
    private final String value;
}
```

В приведенном примере параметр перечисления почти полностью соответствует его значению: нужно только заменить дефисы на нижние подчеркивания и написать все капсом.
Но часто встречаются случаи, когда название перечисления разительно отличается от значения внутреннего параметра:
```java
public enum ResponseCode {
    SUCCESS("RC0000"),
    INVALID_RECAPTCHA("RC4001"),
    AGENT_NOT_FOUND("RC4002"),
    /* ... */
    ILLEGAL_INN("RC4053"),
    ERROR("RC9999");
    
    private final String code;
}
```
или
```java
public enum QrType {
    STATIC('S'),
    DYNAMIC('D'),
    SUBSCRIPTION('B');
    
    private final char mark;
}
```

Зачастую можно встретить подобный код:
```java
public static ErrorCode from(String value) {
  return Arrays.stream(values())
    .filter(it -> it.getValue().equals(value))
    .findFirst()
    .orElse(ErrorCode.UNEXPECTED);
}
```

Подобный подход имеет сложность O(N), заставляя каждый раз проходить по всем элементам перечисления, пока не встретится нужное значение.

Можно ли сделать это эффективнее?
Конечно, можно!
Предлагаю вам вспомнить те уроки, которые мы извлекли, изучая реализации `valueOf(Class, String)` и Guava `Enums.getIfPresent()`.
Хочешь улучшить производительность - наверни кэш в виде мапы!
Запишется это так:
```java
private static final Map<String, ErrorCode> VALUE_TO_ENUM = EnumSet.allOf(ErrorCode.class).stream()
    .collect(Collectors.toMap(ErrorCode::getValue, Function.identity()));

public static ErrorCode parse(String value) {
    return Optional.ofNullable(VALUE_TO_ENUM.get(value))
        .orElse(ErrorCode.UNEXPECTED);
}
```

Таким образом при старте приложения (при первом обращении к перечислению) будет подготовлена таблица соответствия значений к перечислениям.
А получение перечисления по параметру будет иметь алгоритмическую сложность O(1).

Вопрос это не новый и не раз уже обсуждался на StackOverflow: [раз][so_enum_by_value_1], [два][so_enum_by_value_2].

Помимо алгоритмической сложности этот подход имеет еще одно преимущество.
Наличие мапы позволяет быстро отдавать множество всех значений.
```java
public static Set<String> getCodes() {
    return VALUE_TO_ENUM.keySet();
}
```

Множество это удобно использовать в валидаторах:
```java
public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    
    return ErrorCode.getCodes().contains(value);
}
```

Но стоит ли овчинка выделки?
Для ответа на этот вопрос я вновь написал [бенчмарки с использованием JMH](/enums-performance/src/main/java/benchmarks/byvalue).
В них я рассмотрел три варианта: последовательный проход до первого совпадения в цикле, последовательный проход до первого совпадения в стриме и получение закешированного значения из внутренней мапы.
Результаты представлены ниже:

Для 5 элементов:
```
Benchmark                Mode  Cnt   Score   Error  Units
Enum5Benchmark.forLoop   avgt    5  13,730 ± 0,496  ns/op
Enum5Benchmark.mapCache  avgt    5   6,263 ± 0,613  ns/op
Enum5Benchmark.stream    avgt    5  67,962 ± 8,707  ns/op
```

Для 100 элементов:
```
Benchmark                  Mode  Cnt    Score     Error  Units
Enum100Benchmark.forLoop   avgt    5  219,239 ±   1,211  ns/op
Enum100Benchmark.mapCache  avgt    5    6,885 ±   0,394  ns/op
Enum100Benchmark.stream    avgt    5  678,155 ± 104,548  ns/op
```

Как видно из результатов: производительность варианта с кэшом почти не зависит от количества значений.
При этом производительность кэша по сравнению со стримом на порядок выше при небольшом количестве значений и на два порядка выше при большом количестве значений. 

По результатам проведенных измерений хочется дать следующие рекомендации:
- нужно получить значение перечисления по имени, а перечисление никогда не изменится - используй `valueOf()`;
- если оно может расширяться, то лучше обернуть `valueOf()` в try-catch блок;
- если же нужно получаться значение перечисления по параметру, то лучше всего навернуть внутри перечисления небольшой кэш.

![enum_from_value](img/enum_from_value.jpg)

## Перечисление как Singleton
<mark>todo</mark>

## Внутреннее перечисление как способ организации бизнес-логики
<mark>todo</mark>

## Использование перечислений в юнит-тестах
### 
В первую очередь, не стоит генерировать перечисление для тестовой DTO рандомно.
Зачастую перечисления используются для разветвлений в бизнес-логике, поэтому при изменении этой самой бизнес-логики некоторые тесты могут начать рандомно падать.
В результате можно получить ситуацию, когда локально тесты прошли, в CI они тоже прошли, а после мержа в master ветку CI падает, потому что зарандомилось неподходящее перечисление.

Лучше при создании DTO в тестах хардкодить значение перечислений. 
А в некоторых случаях стоит даже проходиться по всем доступным значениям перечисления.

В JUnit для прогонки параметрического теста по значениям перечисления существует аннотация `@EnumSource`.
```java
@ParameterizedTest
@EnumSource
void test(DayOfWeek dayOfWeek) { /* ... */ }
```
По аргументу тестового метода аннотация сама поймет тип перечисления и тест будет проведен для всех его значений.

К сожалению, этой аннотацией становится неудобно пользоваться, когда необходимо исключить какие-то значения перечисления из теста.
Неудобство заключается в том, что конкретные значения перечисления передаются в виде строк, что может грозить ошибками при рефакторинге.
```java
@ParameterizedTest
@EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"SUNDAY"})
void test(DayOfWeek dayOfWeek) { /* ... */ }
```

Для того чтобы избежать ошибок при рефакторинге лучше пользоваться аннотацией `@MethodSource`, а необходимые значения перечисления описывать с помощью `EnumSet`.
```java
private static Stream<Arguments> dayOfWeekProvider() {
    EnumSet<DayOfWeek> exclusionSet = EnumSet.of(DayOfWeek.SUNDAY);
    return EnumSet.complementOf(exclusionSet).stream()
        .map(Arguments::of);
}
        
@ParameterizedTest
@MethodSource("dayOfWeekProvider")
void test(DayOfWeek dayOfWeek) { /* ... */ }
```

Примечание - рекомендация 'не генерировать значения для тестов рандомно' применима не только к перечислениям, но и к полям любых других типов.

### Генерация тестовых моделей
<mark>todo</mark>

## Выводы
<mark>todo</mark>

[enums-in-api-jpoint]: https://jpoint.ru/2021/talks/5mcrhi5tcv6kmbccdnfpte/
[springdoc-github]: https://github.com/springdoc/springdoc-openapi
[yandex-api]: https://yandex.ru/dev/market/partner/doc/dg/reference/get-campaigns.html
[recaptcha-api]: https://developers.google.com/recaptcha/docs/verify#error_code_reference
[so_enums_in_db]: https://stackoverflow.com/questions/2318123/postgresql-enum-what-are-the-advantages-and-disadvantages/2322214
[so_enum_by_value_1]: https://stackoverflow.com/questions/4886973/the-proper-way-to-look-up-an-enum-by-value
[so_enum_by_value_2]: https://stackoverflow.com/questions/55591953/java-enum-with-constructor-best-way-to-get-value-by-constructor-argument