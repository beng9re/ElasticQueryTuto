import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.query.*;

public class QueryGuide {

    public static void main(String[] args) {
        /*
        *  QueryBuilders 클래스를 이용하여 부가 쿼리 EX)...TermQuery 등을 만드나 직접 new xxxxQueryBuilder를 하나 동일함
        *  예시) {
                  "bool" : {
                    "must" : [
                      {
                        "term" : {
                          "LIBRARY_SOURCE" : {
                            "value" : "example",
                            "boost" : 1.0
                          }
                        }
                      }
                    ],
                    "adjust_pure_negative" : true,
                    "boost" : 1.0
                  }
                }
        * */
        BoolQueryBuilder boolQueryBuilder =
                new BoolQueryBuilder().must(new TermQueryBuilder("LIBRARY_SOURCE","example"));

        System.out.printf("boolQueryBuilder ==> \n %s \n" ,boolQueryBuilder);


        BoolQueryBuilder boolQueryBuilder2 =
                QueryBuilders.boolQuery().must(QueryBuilders.termQuery("LIBRARY_SOURCE","example"));

        System.out.printf("boolQueryBuilder2 ==> \n %s \n" ,boolQueryBuilder2);

        if(boolQueryBuilder.toString().equals(boolQueryBuilder2.toString())){
            System.out.println("서로 같음");
        }else{
            System.out.println("다름");
        }

//        example1.json 참조 query{} 부분 부터 시작

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

        BoolQueryBuilder mustBoolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder mustShouldBoolQuery = QueryBuilders.boolQuery();
        mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("LIBRARY_TITLE.korean","이상돈").boost(1f));



        System.out.println(mustShouldBoolQuery);
        //mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("LIBRARY_TITLE.korean","이상돈"));
//            mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("LIBRARY_CONTENTS.korean","이상돈").boost(2.0f));
//            mustShouldBoolQuery.should(QueryBuilders.matchPhraseQuery("REGISTERED_BY","이상돈").boost(3.0f));
//
//        query.must(mustBoolQuery);
//        System.out.println(query);
/*  }                        */
    }
}
