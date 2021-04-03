import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class ExampleQueryGuide {
    private final static String HOST = "localhost";
    private final static int REST_PORT = 9200;
    private final static String ES_LIBRARY_SEARCH_INDEX ="ibrary_search";
    private final static String ES_LIBRARY_SEARCH_TYPE ="search";

    private QueryBuilder query;
    // 생성자
    ExampleQueryGuide(){
        query = makeQuery();
        System.out.println("\"query\":");
        System.out.println(query);

    }

    /**
     * 엘라스틱 쿼리 리퀘스트
     * */
    public void elasticReqest(){
        //커넥션 연결
        RestHighLevelClient restClient =
                new RestHighLevelClient(
                        RestClient.builder(
                                new HttpHost(HOST, REST_PORT, "http")));

        //AggreeGation
        AggregationBuilder aggregationBuilder =
                AggregationBuilders.terms("section").field("LIBRARY_ARTICLE_SECTION_CATEGORY");

        System.out.println(aggregationBuilder);


        //하이라이트 만들기
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .preTags("|S|")
                .postTags("|/S|")
                .field(new HighlightBuilder
                        .Field("LIBRARY_CONTENTS")
                        .fragmentSize(1000)
                        .requireFieldMatch(false))
                .field(new HighlightBuilder
                        .Field("LIBRARY_CONTENTS.korean")
                        .fragmentSize(1000)
                        .requireFieldMatch(false))
                .field(new HighlightBuilder
                        .Field("LIBRARY_CONTENTS.english")
                        .fragmentSize(1000)
                        .requireFieldMatch(false));

        // sourceBuilder ==> 엘라스틱 쿼리 생성
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .from(0)
                .size(10)
                .query(query) //make Query 에서 Query 생성
                .sort("CREATED_DT", SortOrder.DESC)
                .aggregation(aggregationBuilder)
                .highlighter(highlightBuilder);

        // Reqest 객체 생성
        SearchRequest request = new SearchRequest(ES_LIBRARY_SEARCH_INDEX)
                .types(ES_LIBRARY_SEARCH_TYPE)
                .source(sourceBuilder);
        try {
            SearchResponse response =
                    //리퀘스트 시작
                    restClient.search(request, RequestOptions.DEFAULT);
            System.out.println(response);
            restClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * exapmle1.query 만들기
     * line 4 ~ line 69
     * */
    public QueryBuilder makeQuery(){
        BoolQueryBuilder rootBoolQuery = new BoolQueryBuilder();
        BoolQueryBuilder boolShouldQuery = new BoolQueryBuilder();
        BoolQueryBuilder boolShouldBoolShould = new BoolQueryBuilder();
        //exaple1.json 5 : line
        rootBoolQuery.must(new TermQueryBuilder("LIBRARY_SOURCE","example").boost(1f));
        //exaple1.json 14 : line
        rootBoolQuery.must(
                new RangeQueryBuilder("CREATED_DT")
                        .from("2015-01-01 00:00:00").to("now")
                        .includeLower(true).includeUpper(true)
                        .format("yyyy-MM-dd HH:mm:ss").boost(1f));
        //exaple1.json 25 : line
        rootBoolQuery.must(new TermQueryBuilder("DATA_TYPE","ARTICLE"));

        //exaple1.json 33 : line
        boolShouldBoolShould.should(new MatchPhraseQueryBuilder("LIBRARY_TITLE.korean","이상돈").boost(3f));
        boolShouldBoolShould.should(new MatchPhraseQueryBuilder("LIBRARY_CONTENTS.korean","이상돈").boost(2f));
        boolShouldBoolShould.should(new MatchPhraseQueryBuilder("REGISTERED_BY","이상돈").boost(3f));

        //exaple1.json 32 : line
        boolShouldQuery.should(boolShouldBoolShould);

        //exaple1.json 31 : line
        rootBoolQuery.must(boolShouldQuery);

        //exaple1.json 60 : line
        rootBoolQuery.mustNot(new TermQueryBuilder("LIBRARY_IS_DEPLOY_CANCELED","Y")).boost(1f);
        //exaple1.json 69 : end

        return rootBoolQuery;
    }



    public static void main(String[] args) throws Exception{
        ExampleQueryGuide em = new ExampleQueryGuide();
        em.elasticReqest();


    }
}
