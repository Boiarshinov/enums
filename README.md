# Effective enums
В данном репозитории представлены мои размышления по поводу использования перечислений в Java коде.

Репозиторий состоит из нескольких подпроектов
- `docs` - мои заметки про перечисления: [стандартные коллекции перечислений][enums] 
  и [использование перечислений в веб-приложениях][effective-enums]
- `enums-in-java` - примеры использования перечислений и работы с ними в ванильной Java
- `enums-in-libs` - исследование различных библиотек, дополняющих работу с перечислениями в Java
- `enums-in-api` - примеры применения перечислений в публичном API
- `enums-in-db` - примеры использования перечислений в базах данных и сравнение производительности
- `enums-in-test` - примеры использования перечислений в тестах
- `enums-performance` - сравнение производительности различных операций с перечислениями
- `enum-singleton` - использование перечисления в качестве спрингового бина.

## Разные статейки про перечисления
- Статья со странным слогом про некоторые фишки enum: https://habr.com/ru/post/321152/
- Подход к использованию enum'ов в качестве стратегии (не работает для DI-контейнеров): https://habr.com/ru/post/101280/
- 8 недостатков колонки типа enum в MySQL (2011 г.): http://komlenic.com/244/8-reasons-why-mysqls-enum-data-type-is-evil/
- Преимущество enum колонок в PostgreSQL (2018 г.): https://habr.com/ru/post/353556/
- Про недостатки enum'ов (2019 г.): https://habr.com/ru/post/476650/
- Грокаем перечисления (2004 г.): http://madbean.com/2004/mb2004-3/
- Почему перечисление имеет рекурсивное обобщение: http://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html#FAQ106
- Объяснение почему в перечислениях статические блоки выполняются позже кодовых блоков и конструкторов: https://stackoverflow.com/questions/11419519/enums-static-and-instance-blocks
- Различные способы реализации Singleton (enum ошибочно назван неленивым): https://habr.com/ru/post/129494/

[enums]: /docs/enums_collections.md
[effective-enums]: /docs/effective_enums.md
[enum-set]: /enums-in-java/src/test/java/collections/EnumSetTest.java