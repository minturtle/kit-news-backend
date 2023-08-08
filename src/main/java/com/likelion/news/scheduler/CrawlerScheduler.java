package com.likelion.news.scheduler;


import com.likelion.news.dto.ClovaSummaryRequest;
import com.likelion.news.dto.RefinedNewsContentDto;
import com.likelion.news.entity.CrawledNews;
import com.likelion.news.entity.enums.ArticleCategory;
import com.likelion.news.exception.ExceptionMessages;
import com.likelion.news.exception.InternalServerException;
import com.likelion.news.service.ClovaSummaryApiCallService;
import com.likelion.news.service.NewsService;
import com.likelion.news.service.ShellRunnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CrawlerScheduler {

    private final ShellRunnerService shellRunnerService;
    private final ClovaSummaryApiCallService clovaService;
    private final NewsService newsService;

    private final Environment env;

    /**
     * @methodName runCrawlingAndSummaryTask
     * @author Minseok Kim
     * @description 지정된 시간마다 크롤링 후, 요약을 수행해 DB에 저장합니다.
     * crone을 사용해 얼마마다 Scheduler를 실행할지 지정할 수 있으며, 아래의 링크를 참고해 cron을 설정할 수 있습니다.
     * <a href="https://m.blog.naver.com/deeperain/221609802306">...</a>
     * @return 함수의 Return 값은 없으나, 하루의 4개의 뉴스를 요약한 후 DB에 저장합니다.
     */
    @Scheduled(cron = "0 00 18 * * *")
    public void runCrawling(){
        try{
            log.info("Crawler Scheduler Started");
            String command = createCommand();

            shellRunnerService.executeShell(command);
            log.info("Crawler Scheduler Ended");
        }catch (Exception e){
            throw new InternalServerException("Summary API 호출중 오류가 발생했습니다.",e);
        }
    }

    @Scheduled(cron = "0 20 18 * * *")
    public void runSummary(){
        try{
            log.info("Summary Scheduler Started");
            Integer summarizationSize = env.getProperty("clova.summary.size", Integer.class);

            // 모든 카테고리에서 Summarization 진행
            ArticleCategory[] articleTypes = ArticleCategory.values();


            List<CrawledNews> newsList
                    = newsService.getRandomNews(summarizationSize, List.of(articleTypes), LocalDate.now());


            List<RefinedNewsContentDto> resultList = new ArrayList<>();

            // 구해온 모든 news에 대해 요약 진행
            for(CrawledNews news : newsList){
                ClovaSummaryRequest clovaSummaryRequest
                        = clovaService.createDefaultNewsRequest(news.getArticleTitle(), news.getArticleContent());


                String summary = clovaService.getSummary(clovaSummaryRequest);

                RefinedNewsContentDto result = RefinedNewsContentDto.builder()
                        .crawledNews(news)
                        .summary(summary)
                        .build();

                resultList.add(result);

                log.info("Summary Complete : ID : {}", news.getCrawledNewsId());
            }

            // 요약 완료된 뉴스를 DB에 저장
            newsService.saveRefinedNewsList(resultList);
            log.info("Summary Scheduler Ended");
            }catch (Exception e){
            throw new InternalServerException("Summary API 호출중 오류가 발생했습니다.",e);
        }

    }

    private String createCommand() {

        String executeCommand = env.getProperty("crawler.shell.execute-command");
        List<String> args = env.getProperty("crawler.shell.args", List.class);


        StringBuilder sb = new StringBuilder(executeCommand);

        for(String arg : args){
            sb.append(" ");
            sb.append(arg);
        }

        return sb.toString();
    }

}
