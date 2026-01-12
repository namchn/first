

헥사고날 아키텍처 을 적용하여 리펙토링 중..




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