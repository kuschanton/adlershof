
# adlershof
Fuel price notificator backend service

Next steps:
- [ ] Linter for code style
- [ ] E2E test
- [ ] Decouple station use case from price usecase
- [ ] No exceptions
- [x] Finish persistence
- [x] Cover persistence with tests
- [x] @DynamicPropertySource in persistence tests
- [ ] Cleanup persistence
- [ ] Make feeder work with persistence
- [ ] Cover feeder with tests
- [ ] Cleanup feeder



# Refactorings:
- [ ] Use spring boot r2dbc starter (https://spring.io/blog/2019/05/15/spring-data-r2dbc-1-0-m2-and-spring-boot-starter-released)
- [x] Use CodecRegistrar to remove self written override classes https://github.com/r2dbc/r2dbc-postgresql/pull/164/files