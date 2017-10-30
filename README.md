# Mockito Kt

[![Apache License 2.0](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Purpose

This library provides some code-style cookies and tries to solve the problem with `null` values on [*matching*](http://docs.mockito.googlecode.com/hg/1.9.5/org/mockito/Matchers.html)

## Example 1
```
val list = mock(MutableList::class)

list.add("String 1")
list.add("String 2")

verify(list) {
    times(2).add(anyString())
}
```

## Example 2
```
val list = mock(MutableList::class) {
    on {
        size()
    }.thenReturn(5)

    on {
        get(eq(3))
    }.thenReturn("String 4")
}
assertEquals(5, list.size())
assertEquals("String 4", list.get(3))
```

## Example 3
```
val list = spy(ArrayList<String>())
val set = spy(HashSet::class) {
    ...
}
```
## Example 4
```
// Mocking
val mockService = mock(ServiceClass::class)
// Test
mockService.foo()
mockService.bar(SomeData("Test", 1))
// Verification
verifyOnce(mockService) { match ->
    foo()
    bar(match.eq(SomeData("Test", 1)))
}

```

## Matching and null values
Standard Mockito API does not always work fine due to the strict control `null` references in Kotlin. `Mockito Kt` tries to find default `not null` value, but in case when it impossible developer can register his own defaults manually. There is two options: local - for one mock object or globally - for all cases.

### Local default value
```
mock(Foo::class) {
    defaults.register(Bar::class to someBarValue)
    ...
}
```

### Global default value
```
Defaults.Global.register(Bar::class to someBarValue)
```

## Using Maven
### System requirements
|        |            |
| ------ | :--------: |
| Java   | 1.7+       |
| Maven  | v3+        |
| Kotlin | 1.0        |

### Repository settings
```
    <repositories>
        <repository>
            <id>paslavsky.repo</id>
            <url>http://paslavsky.homeunix.net:8000/nexus/content/repositories/releases</url>
        </repository>
    </repositories>
```

### Artifact
```
    <dependency>
        <groupId>net.paslavsky.kotlin</groupId>
        <artifactId>mockito-kt</artifactId>
        <version>0.0.5-beta</version>
        <scope>test</scope>
    </dependency>
```
