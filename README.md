

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

# 구현에 대한 생각
```
1. 일단 기본적인 기능에 대한 리펙토링을 하고 이걸 배치로 구현하는 가능성 까지는 확인함.
2. 대량의 데이터 처리는 비실시간(non-realtime) 인경우는 배치로 구현  
   즉시성과 정합성을 고려한다면 비동기 큐를 구현을 하는게 목표

```



# ETC
```
1. 헥사고날 아키텍처는 마치 잠수함의 격벽시스템 같아보인다
잠수함의 격벽이 한 구역의 침수가 전체로 퍼지는 것을 막는 것처럼, 
헥사고날 아키텍처는 외부 기술(데이터베이스, 외부 API, UI)의 변화나 장애가 
내부 핵심 로직(도메인)으로 번지는 것을 차단

2. 어댑터(Adapter): 외부의 충격을 흡수하고 내부 형식으로 변환해주는 장치입니다.
DB가 바뀌거나 외부 라이브러리에 보안 취약점이 발견되어도, 
해당 어댑터만 수리(교체)하면 되며 내부 비즈니스 로직은 안전하게 보호됩니다.

3. 의존성 역전: 핵심 로직은 외부를 알 필요가 없습니다. 
오직 자신이 정의한 포트(Port)의 규격에만 맞으면 누구든 연결될 수 있게 설계하여, 
각 모듈이 정해진 권한 내에서만 동작하도록 강제합니다.

```


# 대규모 데이터 처리 두가지 방법 정리 
```
1. 스케줄링 + 메시지 큐 (Event-Driven 방식)
특정 시간에 이벤트를 발생시켜, 실제 처리는 메시지 큐(Kafka, RabbitMQ 등)를 통해 분산 처리하는 방식입니다.
동작 원리: 스케줄러가 실행되면 발송 대상자의 '키(ID)' 정보만 큐에 빠르게 집어넣습니다(Produce). 이후 여러 대의 워커 서버가 큐에서 메시지를 가져와 실제 메일을 발송합니다(Consume).

장점:
확장성(Scalability): 워커 서버(Consumer)만 늘리면 초당 발송 속도를 무한정 늘릴 수 있습니다.
부하 분산: 발송 서버에 급격한 부하가 걸려도 큐가 완충 작용을 하여 시스템이 뻗지 않습니다.

단점:
상태 관리 어려움: 전체 100만 건 중 몇 건이 성공했는지 실시간으로 집계하기가 복잡합니다.
인프라 비용: 메시지 큐 시스템을 별도로 구축하고 관리해야 합니다.
적합한 사례: 초당 수만 건 이상의 폭발적인 트래픽 처리가 필요한 마케팅 푸시 알림 등.

2. 스케줄링 + 배치 (Bulk-Process 방식)
특정 시간에 대량의 데이터를 읽어와서 일정한 덩어리(Chunk) 단위로 순차적으로 처리하는 방식입니다.
동작 원리: Spring Batch 같은 프레임워크를 사용하며, '읽기(Read) -> 가공(Process) -> 쓰기(Write)' 단계를 거칩니다. 데이터베이스와 밀접하게 연동됩니다.

장점:
견고한 관리: 실패 시 지점에서 재시작(Restart), 중복 처리 방지, 성공/실패 통계 기록 등이 프레임워크 차원에서 기본 제공됩니다.
자원 효율: 메모리에 모든 데이터를 올리지 않고 1,000건씩 끊어서 처리하므로 메모리 관리가 효율적입니다.

단점:
단일 처리 한계: 보통 하나의 배치 프로세스 내에서 순차 처리되므로, 메시지 큐 방식보다 개별 발송 속도는 느릴 수 있습니다.
적합한 사례: 정기적인 금융 명세서 발송, 정산 처리, 데이터 통계 등 정확성과 정교한 관리가 필요한 작업.



비교 항목	스케줄링 + 메시지 큐	         |  스케줄링 + 배치
핵심 키워드	실시간성, 확장성,              |   분산	안정성, 데이터 무결성, 로깅
처리 속도	매우 빠름 (병렬 처리 용이)      | 	보통 (순차적/청크 단위 처리)
실패 복구	큐의 Dead Letter Queue(DLQ) 활용	 |  배치 메타데이터 기반 재시작(Restart)
복잡도	    인프라 구조가 복잡함	         |     비즈니스 로직 구현이 상대적으로 단순함
트랜잭션	    개별 메시지 단위로 관리	     |  청크(Chunk) 단위로 묶어서 관리

결론: 무엇을 선택해야 할까?
"전달 속도가 생명이다": 100만 명에게 최대한 빨리 메일을 꽂아 넣어야 한다면 스케줄링 + 메시지 큐를 선택하세요. Apache Kafka 공식 문서에서 분산 처리 원리를 참고할 수 있습니다.
"정확한 결과 보고와 안정성이 생명이다": 실패한 사람을 정확히 골라내서 재발송해야 하고, DB 상태와 연동이 중요하다면 스케줄링 + 배치가 정답입니다. Spring Batch 가이드를 통해 체계적인 관리 방법을 확인해 보세요.

```
