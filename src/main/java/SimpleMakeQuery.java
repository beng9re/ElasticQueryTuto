import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
*  쿼리 만드는 두가지의 방식을 사용
*/
public class SimpleMakeQuery {
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
    }
}
