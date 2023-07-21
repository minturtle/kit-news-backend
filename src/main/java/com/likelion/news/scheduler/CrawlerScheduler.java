package com.likelion.news.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrawlerScheduler {

    /**
     * @methodName runCrawlingAndSummaryTask
     * Author : Minseok Kim
     * @description 지정된 시간마다 크롤링 후, 요약을 수행해 DB에 저장합니다.
     * crone을 사용해 얼마마다 Scheduler를 실행할지 지정할 수 있으며, 아래의 링크를 참고해 cron을 설정할 수 있습니다.
     * <a href="https://m.blog.naver.com/deeperain/221609802306">...</a>
     * @return 함수의 Return 값은 없으나, 하루의 4개의 뉴스를 요약한 후 DB에 저장합니다.
     */
    @Scheduled(cron = "0 * * * * *")
    public void test(){
        log.info("hello test");
    }

}
