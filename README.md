#### overview
jvm language demo projects, such as java kotlin

#### demo list
- [springboot-data-jpa-test](./springboot-data-jpa-test) demo for spring data jpa, it contains cascade operation on mysql database
```
./gradlew clean :springboot-data-jpa-test:build -x test
```
#### jib打包
```bash
./gradlew jib -x test 
```