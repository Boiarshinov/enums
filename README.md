# Ссылки 
- [Статья про перечисления][enums]
- Тесты на методы класса [EnumSet][enum-set] 
- Тесты на методы класса [EnumMap][enum-map]

# Чеклист

- [x] написать тесты на статические методы генерации EnumSet
- [x] описать их
- [x] написать тесты на индивидуальные методы EnumSet
- [x] описать их
- [ ] рассмотреть внутреннее устройство EnumSet
- [ ] описать их, понять как они увеличивают быстродействие
- [x] поменять класс перечисления на что-то простое и понятное
- [ ] сделать UML-диаграмму EnumSet 
- [ ] enum как singleton
- [x] как парсить перечисления из чего-либо
- [x] не генерировать перечисления рандомно в тестах 

## Разные статейки про перечисления
- Статья со странным слогом про некоторые фишки enum: https://habr.com/ru/post/321152/
- Подход к использованию enum'ов в качестве стратегии (не работает для DI-контейнеров): https://habr.com/ru/post/101280/
- 8 недостатков колонки типа enum в MySQL (2011 г.): http://komlenic.com/244/8-reasons-why-mysqls-enum-data-type-is-evil/
- Преимущество enum колонок в PostgreSQL (2018 г.): https://habr.com/ru/post/353556/
- Про недостатки enum'ов (2019 г.): https://habr.com/ru/post/476650/

[enums]: /docs/enums.md
[enum-set]: /enums-in-java/src/test/java/collections/collections.EnumSetTest.java