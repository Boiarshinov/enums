# Мастерское использование перечислений

Содержание:
- [X] Получение перечисления по параметру
- [ ] Не использовать одно и то же перечисление на разных слоях приложения  
- [ ] Перечисление как Singleton
- [ ] Внутреннее перечисление как способ организации бизнес-логики
- [ ] Использование перечислений в тестах
    * [ ] Не генерировать их рандомно
    * [ ] Как тестировать по нескольким значениям перечислений

<mark>А как работает @JsonValue?</mark>

## Получение перечисления по параметру

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



[so_enums_in_db]: https://stackoverflow.com/questions/2318123/postgresql-enum-what-are-the-advantages-and-disadvantages/2322214
[so_enum_by_value_1]: https://stackoverflow.com/questions/4886973/the-proper-way-to-look-up-an-enum-by-value
[so_enum_by_value_2]: https://stackoverflow.com/questions/55591953/java-enum-with-constructor-best-way-to-get-value-by-constructor-argument