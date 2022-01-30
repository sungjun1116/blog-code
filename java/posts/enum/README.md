# 자바에서 Enum 의 비교는 '==' 인가? 'equals' 인가?

자바에서 Enum은 클래스의 인스턴스가 JVM 내에 하나만 존재한다는 것이 100% 보장되는 싱글톤을 만드는 가장 좋은 방법입니다. ([JLS, 8.9 Enum Types](https://docs.oracle.com/javase/specs/jls/se9/html/jls-8.html#jls-8.9))
그렇다면 Enum 비교시 **equals 메서드** 대신 간단히 **== 비교**를 사용하면 어떨까요?

### 1. == 비교는 NullPointerException을 발생시키지 않습니다.
```java
enum Color { BLACK, WHITE };

Color nothing = null;
if (nothing == Color.BLACK);      // runs fine
if (nothing.equals(Color.BLACK)); // throws NullPointerException
```
equals 메서드를 사용해 비교하면 런타임에 `NullPointerException`가 발생할 가능성이 있습니다. (물론 equals 메서드도 `Color.BLACK.equals(nothing)`처럼 `null`이 아닌 값에서 메서드를 수행하면 NPE에 안전합니다.)

### 2. == 비교는 컴파일 타임에 타입 호환성 검사를 지원합니다.
```java
enum Color { BLACK, WHITE };
enum Chiral { LEFT, RIGHT };

if (Color.BLACK.equals(Chiral.LEFT)); // compiles fine
if (Color.BLACK == Chiral.LEFT);      // DOESN'T COMPILE!!! Incompatible types!
```
== 비교를 사용하면 프로그래머가 실수로 잘못된 타입 비교를 작성해도 컴파일 타임에 검사되지만 equals 메서드를 사용하면 그대로 컴파일되버립니다.
 
컴파일 타임에 타입 호환성 검사를 지원하는 것은 개인적으로 아주 큰 장점이라고 생각합니다. 실제로 회사에서 equals 메서드를 사용해 잘못 작성된 코드로 인해 버그가 발생한 적 있습니다. 최대한 비슷하게 예제를 만들어 보았습니다.
```java
public boolean compare(Color color) {
    return Color.Black.name().equals(color);
}
```
문자열과 Enum을 비교하고 있으니 항상 `false` 를 반환하는 코드입니다... 아마 개발하신 분의 의도는 `Color.Black` 과 `color` Enum들을 서로 비교하고 싶으셨을텐데 실수로 `.name()`을 추가하신 것으로 보입니다. 만약 == 비교를 사용했다면 컴파일 자체가 되지 않아서 실수할 일이 없었을 것입니다. 
그 다음부터 저희 팀 내에서는 Enum 비교할때는 == 비교를 사용하도록 팀 컨벤션을 추가했습니다.

### 마무리 
== 비교가 코드도 간결해지고 직관적이기 때문에 정답이라고 생각했으나 반대의 생각을 가지신 분들도 많은 주제인 것 같습니다.
(참고: [stackoverflow/comparing-java-enum-members-or-equals](https://stackoverflow.com/questions/1750435/comparing-java-enum-members-or-equals))

그러나 현재까지 저의 경험으로는 == 비교가 가진 장점이 실무에서 더 유용하게 작용한다고 생각합니다.

