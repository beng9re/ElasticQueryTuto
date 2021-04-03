import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.query.*;

public class QueryGuide {

    public static void main(String[] args) {
//        example1.json 참조 하여 query{} 부분 부터 시작

/* "bool" : {                                                                                                */
        BoolQueryBuilder query = QueryBuilders.boolQuery();

// must [term,range,term] 시작
/* [                                                                                                         */
/* term : {                                                                                                 */
        query.must(
                QueryBuilders.termQuery("LIBRARY_SOURCE","example").boost(1f)
        );
/* }, range : {                                                                                               */
        query.must(
                QueryBuilders
                        .rangeQuery("CREATE_DT")
                        .from("2015-01-01 00:00:00")
                        .to("now")
                        .includeLower(true)
                        .includeUpper(true)
                        .format("yyyy-MM-dd HH:mm:ss")
                        .boost(1.0f)
        );
/* }                                                                                                        */

/* } , term : {                                                                                              */
        query.must(
                //key Value
                QueryBuilders.termQuery("DATA_TYPE","ARTICLE")
        );
        System.out.println(query);

/* } , bool: {              */
// 예제 31번째 라인
        BoolQueryBuilder mustBoolQuery = QueryBuilders.boolQuery();

        BoolQueryBuilder mustShouldBoolQuery = QueryBuilders.boolQuery(); //31 Line
        //bool : { should
        mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("LIBRARY_TITLE.korean","이상돈").boost(1f));
        mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("LIBRARY_TITLE.korean","이상돈").boost(2f));
        mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("REGISTERED_BY","이상돈").boost(3f));

        mustBoolQuery.should(mustShouldBoolQuery);
// 예제 59번째 라인
        System.out.println(mustShouldBoolQuery);

/* }                                                        */
        query.mustNot(
                QueryBuilders.termQuery("LIBRARY_IS_DEPLOY_CANCELED","Y")
        );
// 예제 70번째 까지 bool 마무리

    }
}
