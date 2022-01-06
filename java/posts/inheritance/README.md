# 자바가 다중상속 문제를 해결하는 방식

다른 객체지향언어인 C++에서는 여러 조상 클래스로부터 상속받는 것이 가능한 **'다중상속(multiple inheritance)'** 을 허용하지만 자바에서는 오직 단일 상속만을 허용합니다. 사실 다중상속 문제는 수년동안 논쟁의 여지가 있는 문제로 다루어져 왔습니다. 
지금부터 다중 상속의 가장 대표적인 문제로 꼽히는 **다이아몬드 문제(Diamond Problem)**을 통해서 그 이유에 대해 살펴보겠습니다.

## The diamond problem
다음과 같이 추상 메서드를 가진 `Sample` 추상 클래스가 있습니다.
```java
public abstract class Sample {
    public abstract void demo();
}
``` 
여기서`Sample` 클래스를 상속받아 `demo` 메서드를 구현하는 두 개의 클래스가 있습니다.
```java
public class Super1 extends Sample {
   public void demo() {
      System.out.println("demo method of super1");
   }
}
```
```java
public class Super2 extends Sample {
   public void demo() {
      System.out.println("demo method of super2");
   }
}
```
자바가 다중상속을 지원한다는 가정하에 `Super1` 및 `Super2` 클래스 모두를 상속받는 자식 클래스를 하나 만들어봅니다.
```java
public class SubClass extends Super1, Super2 {
    
   public static void main(String args[]) {
      SubClass obj = new SubClass();
      obj.demo();
   }
}
```

