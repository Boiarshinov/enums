# Effective enums

<mark>todo введение. Тут нужно объявить в какой предметной области будут приведены примеры</mark>

Содержание:
- [X] Перечисления в публичном API
  - [X] JSON
  - [ ] XML
  - [ ] Бинарные форматы  
- [ ] Перечисления в БД
- [X] Получение перечисления по значению
  - [ ] Сравнение перфоманса
- [ ] Разделение слоев
- [ ] Коллекции с перечислениями
- [ ] Перечисление как Singleton
- [ ] Внутреннее перечисление как способ организации бизнес-логики
- [X] Использование перечислений в тестах

## Перечисления в публичном API
По поводу использования перечислений в публичном API сломано немало копий.
Например, на последнем JPoint был [доклад][enums-in-api-jpoint], осуждающий использование перечислений в API.

При рассмотрении использования перечислений в публичном API нужно разделять получение и отдачу перечислений.
Отдавать перечисления - хорошо. 
Клиенты вашего API будут рады, что часть полей может принимать только ограниченное количество значений.
Это позволяет клиентам заложить различную логику обработки приходящих значений.
Коды состояния http и MIME-типы тоже являются примерами перечислений в API.

Принимать перечисления - тоже хорошо (но уже не так хорошо).
Когда вы выступаете в качестве клиента чужого API или принимаете значения от пользователя, нужно провести анализ каким образом обрабатывать входящие значения.
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
    INVALID_KEYS("invalid-keys"),
    UNEXPECTED("unexpected");
    
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
public static ErrorCode from(String value) {
  return Arrays.stream(ErrorCode.values())
    .filter(it -> it.getValue().equals(value))
    .findFirst()
    .orElse(ErrorCode.UNEXPECTED);
}
```

В легаси проектах такой код можно встретить очень часто.
Хотя подобный подход имеет сложность O(N), заставляя каждый раз проходить по всем элементам перечисления, пока не встретится с нужным значением.

Гораздо лучше заранее подготовить мапу 'значение' к 'перечислению'.
Запишется это так:
```java
private static final Map<String, ErrorCode> VALUE_TO_ENUM = EnumSet.allOf(ErrorCode.class).stream()
    .collect(Collectors.toMap(ErrorCode::getValue, Function.identity()));

public static ErrorCode parse(String value) {
    return Optional.ofNullable(VALUE_TO_ENUM.get(value))
        .orElse(ErrorCode.UNEXPECTED);
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

[enums-in-api-jpoint]: https://jpoint.ru/2021/talks/5mcrhi5tcv6kmbccdnfpte/
[springdoc-github]: https://github.com/springdoc/springdoc-openapi
[yandex-api]: https://yandex.ru/dev/market/partner/doc/dg/reference/get-campaigns.html
[recaptcha-api]: https://developers.google.com/recaptcha/docs/verify#error_code_reference
[so_enums_in_db]: https://stackoverflow.com/questions/2318123/postgresql-enum-what-are-the-advantages-and-disadvantages/2322214
[so_enum_by_value_1]: https://stackoverflow.com/questions/4886973/the-proper-way-to-look-up-an-enum-by-value
[so_enum_by_value_2]: https://stackoverflow.com/questions/55591953/java-enum-with-constructor-best-way-to-get-value-by-constructor-argument