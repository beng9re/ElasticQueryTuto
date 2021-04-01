import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.imageio.IIOException;
import java.io.IOException;


public class ElasticSerchConnection {
    private final static String HOST = "";
    private final static int REST_PORT = 0;
    private final static String ES_LIBRARY_SEARCH_INDEX ="";
    private final static String ES_LIBRARY_SEARCH_TYPE ="";

    public static void main(String[] args) throws IOException {
        //커넥션 연결
        RestHighLevelClient restClient =
                new RestHighLevelClient(RestClient.builder(
                        new HttpHost(ElasticSerchConnection.HOST, ElasticSerchConnection.REST_PORT, "http")));

        //AggreeGation
        AggregationBuilder aggregationBuilder =
                AggregationBuilders.terms("section").field("LIBRARY_ARTICLE_SECTION_CATEGORY");

        //하이라이트 만들기
        HighlightBuilder highlightBuilder = new HighlightBuilder().preTags("|S|").postTags("|/S|")
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS").fragmentSize(1000).requireFieldMatch(false))
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS.korean").fragmentSize(1000).requireFieldMatch(false))
                .field(new HighlightBuilder.Field("LIBRARY_CONTENTS.english").fragmentSize(1000).requireFieldMatch(false));


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

        // Search 요청
        SearchResponse response = restClient.search(request,RequestOptions.DEFAULT);

        restClient.close();




    }

}
