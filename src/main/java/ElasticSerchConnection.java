import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * 엘라스틱 커넥션 연결부터 Request 과정
 * */
public class ElasticSerchConnection {
    /**
     * 엘라스틱 서치 host 정보
     */
    private final static String HOST = "localhost";
    /**
     * 엘라스틱 서치 port 정보
     */
    private final static int REST_PORT = 9200;
    /**
     * 엘라스틱 서치 index 정보
     */
    private final static String ES_LIBRARY_SEARCH_INDEX ="ibrary_search";
    /**
     * 엘라스틱 서치 type 정보
     */
    private final static String ES_LIBRARY_SEARCH_TYPE ="search";


    public static void main(String[] args) throws IOException {
        //커넥션 연결
        RestHighLevelClient restClient =
                new RestHighLevelClient(
                        RestClient.builder(new HttpHost(HOST, REST_PORT, "http")));

        //AggreeGation
        AggregationBuilder aggregationBuilder =
                AggregationBuilders.terms("section").field("LIBRARY_ARTICLE_SECTION_CATEGORY");

        System.out.println(aggregationBuilder);


        //하이라이트 만들기
        HighlightBuilder highlightBuilder = new HighlightBuilder().preTags("|S|").postTags("|/S|")
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS").fragmentSize(1000).requireFieldMatch(false))
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS.korean").fragmentSize(1000).requireFieldMatch(false))
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS.english").fragmentSize(1000).requireFieldMatch(false));

        // sourceBuilder 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery())
                .sort("CREATED_DT", SortOrder.DESC)
                .aggregation(aggregationBuilder)
                .highlighter(highlightBuilder)
                .from(0)
                .size(10);

        // Reqest 객체 생성
        SearchRequest request = new SearchRequest(ES_LIBRARY_SEARCH_INDEX)
                .types(ES_LIBRARY_SEARCH_TYPE)
                .source(sourceBuilder);

        // 엘라스틱 Search 요청
        SearchResponse response = restClient.search(request,RequestOptions.DEFAULT);

        restClient.close();

    }

}
