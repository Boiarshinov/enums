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
- [Enum-Всемогущий](https://habr.com/ru/post/321152/) - Статья со странным слогом про некоторые фишки `enum` - 2017 г.
- [Несколько слов об использовании перечислений в изменяющейся среде](https://habr.com/ru/post/101280/) - Подход к использованию enum'ов в качестве стратегии (не работает для DI-контейнеров) - 2010 г.
- [8 Reasons Why MySQL's ENUM Data Type Is Evil](http://komlenic.com/244/8-reasons-why-mysqls-enum-data-type-is-evil/) - 8 недостатков колонки типа `enum` в MySQL - 2011 г.
- [Postgres Enum](https://habr.com/ru/post/353556/) - Преимущество enum колонок в PostgreSQL - 2018 г.
- [Место enum в современном изменчивом мире](https://habr.com/ru/post/476650/) - Про недостатки enum'ов - 2019 г. 
- [Groking Enum (aka Enum>)](http://madbean.com/2004/mb2004-3/) - Грокаем перечисления - 2004 г. 
- [How do I decrypt "Enum<E extends Enum<E>>"?](http://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html#FAQ106) - Почему перечисление имеет рекурсивное обобщение
- [Enums - static and instance blocks](https://stackoverflow.com/questions/11419519/enums-static-and-instance-blocks) - Объяснение почему в перечислениях статические блоки выполняются позже кодовых блоков и конструкторов 
- [Правильный Singleton в Java](https://habr.com/ru/post/129494/) - Различные способы реализации Singleton (в том числе с помощью `enum`) - 2011 г.
- [Enum Mappings with Hibernate – The Complete Guide](https://thorben-janssen.com/hibernate-enum-mappings/) - Маппинг перечислений в записи в БД с помощью JPA и Hibernate 

[enums]: /docs/enums_collections.md
[effective-enums]: /docs/effective_enums.md
[enum-set]: /enums-in-java/src/test/java/dev/boiarshinov/enumsinjava/collections/EnumSetTest.java