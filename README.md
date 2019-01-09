# Mockito Kt

[![Apache License 2.0](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/paslavsky/mockito-kt.svg?branch=master)](https://travis-ci.org/paslavsky/mockito-kt)
[ ![Download](https://api.bintray.com/packages/paslavsky/maven/mockito-kt/images/download.svg) ](https://bintray.com/paslavsky/maven/mockito-kt/_latestVersion)

## Purpose

This library provides some code-style cookies and tries to solve the problem with `null` values on [*matching*](http://docs.mockito.googlecode.com/hg/1.9.5/org/mockito/Matchers.html)

## Example 1
```
val list = mock<MutableList<String>>()

list.add("String 1")
list.add("String 2")

list.verify {
    times(2).add(any())
}
```

## Example 2
```
val list = MutableList::class.mock {
    on { size }.thenReturn(5)
    on { this[eq(3)] }.thenReturn("String 4")
}
assertEquals(5, list.size)
assertEquals("String 4", list[3])
```

## Example 3
```
val list = ArrayList<String>().spy()
val set = spy<HashSet>() {
    ...
}
```
## Example 4
```
// Mocking
val mockService = mock<ServiceClass>()
// Test
mockService.foo()
mockService.bar(SomeData("Test", 1))
// Verification
mockService.verifyOnce { match ->
    foo()
    bar(match.eq(SomeData("Test", 1)))
}

```

## Matching and null values
Standard Mockito API does not always work fine due to the strict control `null` references in Kotlin. `Mockito Kt` tries to find default `not null` value, but in case when it impossible developer can register his own defaults manually. There is two options: local - for one mock object or globally - for all cases.

### Local default value
```
mock<Foo> {
    defaults.register<Bar>(someBarValue)
    ...
}
```

### Global default value
```
Defaults.Global.register<Bar>(someBarValue)
```

## Using Maven
### System requirements
|        |            |
| ------ | :--------: |
| Java   | 1.8+       |
| Maven  | v3+        |
| Kotlin | 1.3        |

### Repository settings
```
    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>bintray-paslavsky-maven</id>
            <name>bintray</name>
            <url>https://dl.bintray.com/paslavsky/maven</url>
        </repository>
    </repositories>
```

### Artifact
```
    <dependency>
        <groupId>net.paslavsky.kotlin</groupId>
        <artifactId>mockito-kt</artifactId>
        <version>2.0.0</version>
        <scope>test</scope>
    </dependency>
```

## Using Gradle
### System requirements
|        |            |
| ------ | :--------: |
| Java   | 1.8+       |
| Gradle | v3+        |
| Kotlin | 1.3        |

### Repository settings
```
repositories {
    maven {
        url  "https://dl.bintray.com/paslavsky/maven" 
    }
}
```

### Artifact
```
dependencies {
    testCompile 'net.paslavsky.kotlin:mockito-kt:2.0.0'
}
```

## Migration from 1.0.0 to 2.0.0

In the second version, I changed the signature of the following methods:

|                                                                               |       |                                                                     |
| ----------------------------------------------------------------------------- | :---: | ------------------------------------------------------------------- |
| `fun <T : Any> mock(kClass: KClass<T>, setup: Mock<T>.() -> Unit = {}): T`    |   ->  | `fun <T : Any> KClass<T>.mock(setup: Mock<T>.() -> Unit = {}): T`   |
| `fun <T : Any> spy(obj: T, setup: Mock<T>.() -> Unit = {}): T`                |   ->  | `fun <T : Any> T.spy(setup: Mock<T>.() -> Unit = {}): T`            |
| `fun <T : Any> spy(classToSpy: KClass<T>, setup: Mock<T>.() -> Unit = {}): T` |   ->  | `fun <T : Any> KClass<T>.spy(setup: Mock<T>.() -> Unit = {}): T`    |
| `fun <T : Any> verify(mock: T, verify: Verification<T>.() -> Unit)`           |   ->  | `fun <T : Any> T.verify(verify: Verification<T>.() -> Unit)`        |
| `fun <T: Any> verifyOnce(mock: T, checks: T.(match: MatchersKt) -> Unit)`     |   ->  | `fun <T : Any> T.verifyOnce(checks: T.(match: MatchersKt) -> Unit)` |

It impossible to override old methods and mark as `@Deprecated` because from Java byte code perspective it's 
the same signature.