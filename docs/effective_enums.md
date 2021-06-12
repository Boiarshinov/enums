# Effective enums

<mark>todo введение</mark>

Содержание:
- [ ] Перечисления в публичном API
- [ ] Перечисления в БД
- [X] Получение перечисления по значению
- [ ] Разделение слоев
- [ ] Коллекции с перечислениями
- [ ] Перечисление как Singleton
- [ ] Внутреннее перечисление как способ организации бизнес-логики
- [X] Использование перечислений в тестах

## Перечисления в публичном API
<mark>todo</mark>

### JSON
К сожалению, в синтаксисе JSON нет возможности выделить перечисления, и в большинстве случаев приходится передавать значение enum в виде строки.
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
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys"); //Не описанный в документации, но существующий код ошибки

    @JsonValue
    private final String value;
}
```
Теперь при парсинге JSON Jackson будет автоматически переводить строковое значение в перечисление.
То же работает и в обратную сторону: при сериализации такого перечисления в JSON он будет приведен к значению, помеченному `@JsonValue`.

### XML
<mark>todo</mark>

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

## Получение перечисления по параметру

<mark>Переписать с учетом предыдущих глав</mark>

Перечисления - замечательная вещь. 
Они позволяют описать набор констант (дни недели, месяца).
Еще они отлично помогают задать возможные состояния конечного автомата.
И очень часто перечисления опираются на какое-либо значение.
Например, перечисление `ChronoUnit` из стандартной библиотеки опирается на значение промежутка времени.

В мире web-разработки, где балом правит JSON, тоже было бы полезно использовать перечисления, но JSON позволяет орудовать только строками.
Поэтому нам зачастую приходится писать перечисление, базирующиеся на литералах.
В качестве примера рассмотрим перечисление, инкапсулирующее коды ошибок сервиса Google ReCaptcha: (здесь и далее геттеры и конструкторы в коде не приводятся)
```java
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys"); //Не описанный в документации, но существующий код ошибки
    
    private final String value;
}
```
При получении ответа от сервиса Google ReCaptcha строковое значение кода ошибки преобразуется в перечисление. 
Дальнейшая бизнес-логика обработки ответа будет построена уже не на сравнении строк, а на типобезопасном сравнении перечислений.
К примеру, при получении кода `timeout-or-duplicate` можно попросить пользователя повторно пройти проверку.

Еще один кейс использования перечислений, инкапсулирующих какое-либо значение, - это код ответа на запросы.
Такие перечисления рождаются в тех случаях, когда стандартных HTTP кодов перестает хватать для описания API.
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

При работе с такими перечислениями всем разработчикам приходится решать одну и ту же задачу - получение нужного перечисления из значения.
Причем при выполнении даже простейшего запроса таких преобразований может быть выполнено несколько.
В JSON перечисление приходит в виде значения (чаще всего строки), в базе данных перечисление тоже зачастую лежит в виде значения (не все СУБД умеют работать с перечислениями, и [многие разработчики не советуют затаскивать перечисления в DDL][so_enums_in_db]).

И тут возникает вопрос: а как правильно получить перечисление по значению?
Зачастую можно встретить подобный код:
```java
public static ErrorCode fromCount(String value) {
    return Arrays.stream(ErrorCode.values())
    .filter(it -> it.getValue().equals(value))
    .findFirst()
    .orElseThrow(() -> new IllegalArgumentException(String.format("Have no code for value '%s'", value)));
}
```

В легаси проектах такой код можно встретить очень часто.
Хотя подобный подход имеет сложность O(N), заставляя каждый раз проходить по всем элементам перечисления, пока не встретится с нужным значением.

Гораздо лучше заранее подготовить мапу 'значение' к 'перечислению'.
Запишется это так:
```java
private static final Map<String, ErrorCode> VALUE_TO_ENUM = EnumSet.allOf(ErrorCode.class).stream()
    .collect(Collectors.toMap(ErrorCode::getValue, Function.identity()));

public static ErrorCode byCount(String value) {
    return Optional.ofNullable(VALUE_TO_ENUM.get(value))
        .orElseThrow(() -> new IllegalArgumentException(String.format("Have no code for value '%s'", value)));
}
```

Таким образом при старте приложения будет подготовлена таблица соответствия значений к перечислениям, и получение перечисления будет иметь алгоритмическую сложность O(1).

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

## Перечисление как Singleton
<mark>todo</mark>

## Внутреннее перечисление как способ организации бизнес-логики
<mark>todo</mark>

## Использование перечислений в юнит-тестах
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

[springdoc-github]: https://github.com/springdoc/springdoc-openapi
[yandex-api]: https://yandex.ru/dev/market/partner/doc/dg/reference/get-campaigns.html
[recaptcha-api]: https://developers.google.com/recaptcha/docs/verify#error_code_reference
[so_enums_in_db]: https://stackoverflow.com/questions/2318123/postgresql-enum-what-are-the-advantages-and-disadvantages/2322214
[so_enum_by_value_1]: https://stackoverflow.com/questions/4886973/the-proper-way-to-look-up-an-enum-by-value
[so_enum_by_value_2]: https://stackoverflow.com/questions/55591953/java-enum-with-constructor-best-way-to-get-value-by-constructor-argument