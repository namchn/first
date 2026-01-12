

헥사고날 아키텍처 을 적용하여 리펙토링 중..





# 리펙토링 목적
```
1. core/ 디렉토리 소스는 외부와 분리 (api, web,DB 와 분리되어 캡슐화 되도록 함) 
2. 최적화 코드 짜기

```

# 프로젝트 설명
```

본 프로젝트는 Hexagonal Architecture(Ports & Adapters)를 기반으로
외부 주식 API로부터 데이터를 수집하고, 이를 가공하여
스케줄링 기반 이메일 리포트로 발송하는 시스템입니다.

비즈니스 로직은 Core Domain에 집중시키고,
외부 시스템(API, Mail, DB)은 Port 인터페이스를 통해 추상화하여
확장성과 테스트 용이성을 확보했습니다.

```

# 헥사고날 아키텍처 흐름
```
[ Web / Scheduler ]
        |
[ Application / UseCase ]
        |
[ Domain ]
        |
[ Port (Interface) ]
        |
[ Adapter / Infrastructure ]
```




# 프로젝트 디렉토리 구조 
```
project/
└── main/
    ├── java/
    │   └── com/nc/fisrt/
    │       ├── FisrtApplication.java
    │		│
    │       ├── common/
    │       │   ├── dto/
    │       │   │   ├── ResponseDTO.java
    │       │   │   └── ResponseStringDTO.java
    │       │   ├── error/
    │       │   ├── request/
    │       │   ├── service/
    │       │   │   └── EmailService.java
    │       │   └── util/
    │		│
    │       ├── domain/
    │       │   ├── stock/
    │       │   │   ├── adapter/
    │       │   │   │   ├── entity/
    │       │   │   │   ├── out/
	│	    │   │   │   │   ├── external/
	│		│   │   │   │   │   └── AlphaVantageAdapter
	│	    │   │   │   │   └── persistence/
	│		│   │   │   │       └── KiwoomLogAdapter
	│       │   │   │   ├── scheduler/
	│	    │   │   │   │   └── ApiSchedulerServiceImpl
    │       │   │   │   └── web/
	│	    │   │   │       ├── api/
	│		│   │   │       │   └── EmailController
	│	    │   │   │       └── view/
    │       │   │   │
    │       │   │   ├── core/
    │       │   │   │   ├── domain/
    │       │   │   │   │   ├── StockData
    │       │   │   │   │   └── StockReport
    │       │   │   │   │
    │       │   │   │   ├── policy/
    │       │   │   │   └── port/
    │       │   │   │       ├── in/
    │       │   │   │       │   └── GetStockReportUseCase
    │       │   │   │       └── out/
    │       │   │   │           ├── StockExchangePort
    │       │   │   │           └── LogPersistencePort
    │       │   │   │
    │       │   │   └── infrastructure/
    │       │   │
    │       │   └── user/
    │       │
    │       └── 
    │
    └── resources/
        ├── application-dev.yaml
        └── application.properties
```



#깃 기본 명령어 모음
```

git init
git remote add origin https://github.com/namchn/first.git
git pull origin main --rebase
git add .
git commit -m "1st commit"
git push origin main

```