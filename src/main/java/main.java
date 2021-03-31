import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.query.*;

public class main {

    public static void main(String[] args) {
        QueryBuilder query = QueryBuilders.termsQuery("aa","ss","ss");


        System.out.println(query);
    }
}
