## 정적 코드 분석
정적 코드 분석은 코드내에서 발견할 수 있는 코드 스멜, 잠재적인 결함, 컨벤션 체크, 보안 취약점 등을 코드 레벨에서 분석해서 레포팅 해준다.  

컨벤션 교정 수준이 아니라, 잠재적 문제가 될만한 코드, 안티패턴의 코드들을 다 시스템이 찾아주고 수정안을 제안하기 때문에 코드리뷰에 들어가는 비용도 줄일 수 있다.  

이런 정적 코드 분석 도구에는 대표적으로 pmd, SonarQube, cppcheck, checkstyle 등 여러가지가 있지만, 가장 많은 사용자들이 사용하는 도구는 **SonarQube(소나큐브)** 이다.  

![출처: sonarqube.org](./images/1.png)

이 글에서는 AWS EC2 인스턴스에 SonarQube 서버를 설치하는 방법을 설명하려고 한다. AMI(Amazon Machine Image)는 Amazon Linux 2로 진행한다.  

### 요구 사항
SonarQube Docs를 보고 설치를 위한 필요 조건을 정리해 보았다. ([sonarqube Docs 9.7](https://docs.sonarqube.org/latest/requirements/prerequisites-and-overview/))
- 효율적으로 실행하기 위해 최소 2GB의 RAM과 OS용으로 1GB의 여유 RAM이 필요하다.  
  필자는 EC2 프리티어 버전을 사용중으로 1GB RAM을 가지고 설치를 진행하려 했으나 원활하게 되지 않아서 swap 메모리 설정을 통해 어렵게나마 진행할 수 있었다. (참고: [AWS 관련 자료](https://aws.amazon.com/ko/premiumsupport/knowledge-center/ec2-memory-swap-file/)) 
- **Java 11**(Oracle JRE or OpenJDK)이 설치되어야 한다.
- 지원되는 여러 데이터베이스(PostgreSQL, Microsoft SQL Server, Oracle) 중 하나가 설치되어야 한다. 필자는 설정의 편의를 위해 PostgreSQL을 사용했으며 **PostgreSQL의 경우 9.6 버전 이상**이어야 한다.
- 리눅스를 사용할 경우 `vm.max_map_count`가 524288 보다 크거나 같아야 한다. 
- 리눅스를 사용할 경우`fs.file-max`가 131072 보다 크거나 같아야 한다.  
  ```shell
  # 다음 명령어로 값들을 확인 할 수 있다.고
  sysctl vm.max_map_count
  sysctl fs.file-max 
  
  # root로 현재 세션에 대해 동적으로 값들을 설정할 수 있다.
  sysctl -w vm.max_map_count=524288
  sysctl -w fs.file-max=131072
  
  # 영구적으로 설정할 경우 /etc/sysctl.conf 를 수정해서 설정할 수 있다.
  sysctl -w vm.max_map_count=524288
  sysctl -w fs.file-max=131072
  
  # 커널 설정 반영  
  sudo sysctl -p 
  ```

### 소나큐브용 database와 user 생성
```sql
CREATE USER sonar WITH ENCRYPTED PASSWORD ‘sonar_password’;
    
CREATE DATABASE sonarqube;
    
GRANT ALL PRIVILEGES ON DATABASE sonarqube to sonar; 
```

### 소나큐브용 system user 생성  
ElasticSearch를 분석 엔진으로 사용하기 때문에 SonarQube를 실행하려면 루트가 아닌 시스템 사용자가 필요하다. SonarQube는 root 권한을 가진 사용자로 실행할 수 없다.  
```shell
sudo groupadd sonar

sudo useradd -c “Sonar System User” -d /opt/sonarqube -g sonar -s /bin/bash sonar # Create a user named sonar without sign in options

sudo passwd sonar # Activate sonar user by setting a password to it

sudo usermod -a -G sonar ec2-user # Add sonar group to ec2-user
```

### 소나큐브 서버 설치 
```shell
wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.7.1.62043.zip

unzip sonarqube-9.7.1.62043.zip

sudo mv -v sonarqube-9.7.1.62043/* /opt/sonarqube # Move SonarQube to a file path of choosing (normally /opt/sonarqube)

sudo chown -R sonar:sonar /opt/sonarqube 

sudo chmod -R 775 /opt/sonarqube
```

### 소나큐브 설정(/opt/sonarqube/conf/sonar.properties)
DB 접속정보 설정
```shell
sonar.jdbc.username=sonar
sonar.jdbc.password=sonar_password
sonar.jdbc.url=jdbc:postgresql://localhost/sonarqube
```
ElasticSearch 데이터 저장을 위한 빠른 io 전용 볼륨 경로 설정
```shell
sonar.path.data=/path/to/fast/io/volume/data
sonar.path.temp=/path/to/fast/io/volume/temp
```
기타 주요 설정
```shell
sonar.web.port=8080 # must be a value higher than 1000, default value is 9000
sonar.web.context=/sonarqube # sonar access url will be http://<host_ip>:<webport>/sonarqube
```

### 소나큐브 서버 시작
```shell
$SONAR_HOME/bin/linux-x86–64/sonar.sh console
```
기본 사용자 비밀번호를 사용자 이름과 비밀번호를 사용하여 로그인한 후 비밀번호 변경합니다.
- username: admin
- password: admin

### 주의할 점
설치중 이슈가 발생할 경우 [리눅스 설치시 필요조건](#요구-사항)을 모두 만족했는지 확인해볼 필요가 있습니다. 
