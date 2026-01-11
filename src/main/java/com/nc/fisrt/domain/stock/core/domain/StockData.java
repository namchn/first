package com.nc.fisrt.domain.stock.core.domain;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

//domain/StockData.java
public class StockData {
	private final String symbol;
	private final Map<LocalDate, Double> dailyCloses; // 파싱된 순수 데이터

	public StockData(String symbol, Map<LocalDate, Double> dailyCloses) {
		this.symbol = symbol;
		this.dailyCloses = dailyCloses;
	}

	// 도메인 핵심 로직: n일 이동평균 계산 (프레임워크 의존성 제로)
	public double calculateSMA(int days) {
	     return dailyCloses.values().stream()
	             .limit(days)
	             .mapToDouble(Double::doubleValue)
	             .average()
	             .orElse(0.0);
	}

	public String generateReport() {
		double sma220 = calculateSMA(220);
		// ... 리포트 문자열 생성 로직
		return String.format("%s 리포트: 220일선 %.2f", symbol, sma220);
	}
	
	// 실제 가공 처리 (예: 종가만 추출)
	public String processAnalysis( String symbol, Map<String, Object> response) {
		// 기존 processResponse의 복잡한 계산 로직 수행
		// (데이터 파싱 -> 평균 계산 -> 결과 문자열 포맷팅)
		if (response == null || !response.containsKey("Time Series (Daily)")) {
            return "";
        }
        
        Map<String, Map<String, String>> timeSeries =
                (Map<String, Map<String, String>>) response.get("Time Series (Daily)");

        //symbol
        Map<String, Map<String, String>> metaData =
                (Map<String, Map<String, String>>) response.get("Meta Data");

        
        //Map<String, String> a = metaData.getOrDefault("2. Symbol", null);
        //a.toString();
        
        String metaDataStr = metaData.toString();
        
        // 날짜 정렬 (최신 → 오래된 순)
        //List<String> dates = new ArrayList<>(timeSeries.keySet());
        //Collections.sort(dates, Collections.reverseOrder());

        // 날짜 정렬 (최신 → 오래된 순)
        List<String> dates = timeSeries.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        
        // 오늘 종가
        String today = dates.get(0);
        double close = Double.parseDouble(timeSeries.get(today).get("4. close"));

        // 최근 200일 종가 합산
        /*
        double sum = 0;
        for (int i = 0; i < Math.min(200, dates.size()); i++) {
            sum += Double.parseDouble(timeSeries.get(dates.get(i)).get("4. close"));
        }
        double sma200 = sum / Math.min(200, dates.size());
        */
        double sma200 = dates.stream()
                .limit(200)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);  // 데이터가 없으면 NaN 반환
        
        
        // 최근 2일 종가 합산
        /*
        double sum2 = 0;
        for (int i = 0; i < Math.min(2, dates.size()); i++) {
            sum2 += Double.parseDouble(timeSeries.get(dates.get(i)).get("4. close"));
        }
        double sma2 = sum2 / Math.min(2, dates.size());
        */
        double sma2 = dates.stream()
                .limit(2)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);
        
        double sma220 = dates.stream()
                .limit(220)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);
        
        double sma3 = dates.stream()
                .limit(3)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);
        
        double sma5 = dates.stream()
                .limit(5)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);
        
        double sma10 = dates.stream()
                .limit(10)
                .mapToDouble(date -> Double.parseDouble(timeSeries.get(date).get("4. close")))
                .average()
                .orElse(Double.NaN);

        //log.info("today:"+today);
        //log.info("close:"+close);
        //log.info("sma200:"+sma200);	
        //log.info("sma220:"+sma220);
        //log.info("sma2:"+sma2);
        //log.info("sma3:"+sma3);
        //log.info("sma5:"+sma5);
        //log.info("sma10:"+sma10);
        
        double  closePerRatioSma200 = (close/sma200)*100; //200일선 대비 종가 비율 
        double  closePerRatioSma220 = (close/sma220)*100; //220일선 대비 종가 비율 
        double  Sma2PerRatioSma220 = (sma2/sma220)*100; //220일선 대비 3일선 비율
        double  Sma3PerRatioSma220 = (sma3/sma220)*100; //220일선 대비 3일선 비율
        double  Sma5PerRatioSma220 = (sma5/sma220)*100; //220일선 대비 5일선 비율
        double  Sma10PerRatioSma220 = (sma10/sma220)*100; //220일선 대비 10일선 비율
        //double  closePerRatioSma3 = (close/sma3)*100; //3일선 대비 종가 비율
        //double  closePerRatioSma2 = (close/sma2)*100; //2일선 대비 종가 비율

        
        String result= "\n*중요한것은 잃지 않는것*.\n\n";
        
        String template_sub = "\n*효과는 3일,10일선이 220일선 위에 있을때 가장 좋았다.*"
       		 			  +String.format("\n\n 220일선(%.2f), "
        									+ "\n2일선(%.2f), "
        									+ "\n3일선(%.2f), "
        									+ "\n5일선(%.2f), "
        									+ "\n10일선(%.2f)"
       		 							,sma220, sma2, sma3,sma5,sma10)
							  +String.format("\n\n 200일선대비종가비율:(%.2f/100), "
							  				+ "\n220일선대비종가비율:(%.2f/100), "
							  				+ "\n220일선대비2일선비율:(%.2f/100), "
							  				+ "\n220일선대비3일선비율:(%.2f/100), "
							  				+ "\n220일선대비5일선비율:(%.2f/100), "
							  				+ "\n220일선대비10일선비율:(%.2f/100)"
									  		,closePerRatioSma200,closePerRatioSma220,Sma2PerRatioSma220,Sma3PerRatioSma220,Sma5PerRatioSma220,Sma10PerRatioSma220);
        
        String template_above= metaDataStr+"\n\n"+String.format("%s: %s 종가(%.2f)가 200일선(%.2f) **위에(탑승신호)** 있습니다.",today, symbol, close, sma200)+template_sub;		
        String template_below= metaDataStr+"\n\n"+String.format("%s: %s 종가(%.2f)가 200일선(%.2f) **아래에(하차신호)** 있습니다.",today, symbol, close, sma200)+template_sub;;
        String template_eqaul= metaDataStr+"\n\n"+String.format("%s: %s 종가(%.2f)가 200일선(%.2f) **동일** 합니다.",today, symbol, close, sma200)+template_sub;;
        if (close > sma200) {
       	 result=template_above;
       	 result="";//template_above;  // 종가가 선 아래에 있을때만 확인
       	 if(sma200*1.10 > close  ) { //종가가 200일 선의 110% 이하 이면 알림있음.
       		 result= template_above;
       	 }
       	 if(close > sma200*1.50  && close < sma200*1.70 ) { //종가가 200일 선의 150% 이상 이면 알림있음.
       		 result= template_above+"\n --극단적 상승중! -분할익절의 관점에서 접근..상승장의 시작일수도 있고..욕심부리지 않기.";
       	 }
       	 
       	 if(close > sma200*2.0   ) {//극단적 광기
       		 result= template_above+"\n --극단적 광기의 시작.. 비정상은 결국 정상화 된다.  ";
       	 }
       	 
        } else if (close < sma200) {
       	 result= template_below;
       	 if(close  < sma200*0.75) { //종가가 200일 선의 75% 이하이면 알림 없음. //상당한 하락
       		 result="";
       	 }
       	 if(close  < sma200*0.59 && close  > sma200*0.4) { //종가가 200일 선의 59% 이하이면 알림 있음. //극단적 하락
       		 result=template_below+"\n --극단적 하락중! - 과대 낙폭시 qqq 분할 매수하다가 qqq가 점점 tqqq로 스위칭 하는거지. 첫 터치시에는 다음에 반등하고 그대로 올라가거나 다시 내려갈수 있음";
       	 }
       	 if(close  < sma200*0.25) { //절망적 상황
       		 result="\n 세상이 망하고 있나? 이또한 지나갈 뿐이라네.";
       	 }
        } else {
       	 result= template_eqaul;
        }
        
        if (sma200*1.05 > close && close>sma200*0.95) {
       	 result +="\n <200선에 근접함>..매도매수신호!!!!!!!";
        }
        
        // 주기적으로 api 체크
        if(today.substring(8).contains("10")
       	||today.substring(8).contains("19")
       	||today.substring(8).contains("28")
       	) {
       	 result +="\n\n\n * 올라갈지 내려갈지 알수 없다 단지 200일선에 의지하고 과열과 과대낙폭의 정도를 확인하며 다음을 생각함.";
        }
        
        //log.info("result:"+result);
        return result;
	}
}