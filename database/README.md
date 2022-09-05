# MySQL(Maria DB)에 이모지(Emoji) 저장하기

프로젝트 진행 중 이모지(Emoji)를 DB에 저장해야 했다. MySQL에서 한글 및 다른 언어가 깨지는 것을 처리하려면 문자셋(Charset)을 utf-8로 설정하면 해결할 수 있었다. 그렇다면 이모지 문자열은 어떨까? 결과는 다음과 같이 필드에 적합하지 않은 데이터 문제라는 에러 메시지와 함께 1366 에러 코드를 출력한다.
![](https://images.velog.io/images/lake/post/ab2bb2d2-8b09-4220-9c9e-b99513381e0e/image.png)

## ❗️ Problem
이모지 문자열은 utf-8로 인코딩 되는 경우 글자당 최대 4 bytes까지 필요하지만 안타깝게도 MySQL의 경우 utf-8을 3바이트 가변 인자로 구현하고 있다.

- MySQL/MariaDB의 utf-8 문자셋이 최대 3바이트까지만 지원하게 설계된 이유([참고 글](https://blog.lael.be/post/917))
> 1) 다국어를 처리할 수 있는 UTF-8 이라는 저장방식이 있음. 원래 설계는 가변 4바이트임.
2) 전 세계 모든 언어문자를 다 카운트해봤는데 3바이트가 안됨.
3) MYSQL/MariaDB 에서는 공간절약+속도향상을 위해서 utf8 을 가변 3바이트로 설계함.
4) Emoji 같은 새로 나온 문자가 UTF-8의 남은 영역을 사용하려함 (4바이트 영역).
5) MYSQL/MariaDB 에서 가변 4바이트 자료형인 utf8mb4 를 추가함. (2010년 3월에).

- 이 문제는 MySQL/MariaDB에서만 일어난다.

## ✔️ Solution

### MySQL Server를 직접 운영하는 경우
1) 쿼리 실행을 통해 데이터베이스의 문자셋과 정렬방식을 각각 'utf8mb4', 'utf8mb4_unicode_ci'로 변경한다.
```sql
ALTER DATABASE `테이블명`
CHARACTER SET = utf8mb4 
COLLATE = utf8mb4_unicode_ci;
```
2) 환경파일 수정을 통해 DBMS의 파라미터 정보를 변경한다.
![2](images.png)

### AWS RDS를 사용하는 경우
1) AWS RDS 콘솔의 구성 파라미터 그룹으로 이동하여 해당 속성을 찾아 변경한다.
> character_set_client     = utf8mb4            
character_set_connection = utf8mb4            
character_set_database   = utf8mb4            
character_set_filesystem = binary             
character_set_results    = utf8mb4            
character_set_server     = utf8mb4            
character_set_system     = utf8               
collation_connection     = utf8mb4_unicode_ci
collation_database       = utf8mb4_unicode_ci
collation_server         = utf8mb4_unicode_ci

- 참고: 아래 두 설정 값은 아래의 설정 값이 맞으며 utf8mb4로 변경할 필요가 없다.
  character_set_filesystem = binary
  character_set_system = utf8
- 완료 후, 인스턴스를 재시작 해야함을 잊지말자
![3](images.png)


## 🔎 참고자료
[3-Byte UTF-8 Unicode Encoding](https://dev.mysql.com/doc/refman/8.0/en/charset-unicode-utf8mb3.html)
[MySQL/MariaDB - utf8mb4 언어셋 소개 및 표현범위](https://blog.lael.be/post/917)
[MySQL utf8에서 utf8mb4로 마이그레이션 하기](https://www.letmecompile.com/mysql-utf8-utf8mb4-migration)