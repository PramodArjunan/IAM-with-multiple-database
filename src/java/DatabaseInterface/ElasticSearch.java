package DatabaseInterface;


import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ElasticSearch implements Database
{
	public void insertIntoUserInformation(String user_email,String user_name,long user_number,String user_password,String user_hid,int user_recharge,int user_zid,String user_access) throws Exception
	{
            RestHighLevelClient client=ConnectionForElasticSearch.getConnection();
            IndexRequest indexRequest = new IndexRequest("userinfo").type("_doc").id(user_email).source("name",user_name,"email",user_email,"phone",user_number,"psw",user_password,"hid",user_hid,"rc",user_recharge,"zid",user_zid,"access",user_access);
            client.index(indexRequest, RequestOptions.DEFAULT);
        
	}
        
	public void insertIntoTransaction(int user_email,String trans,int deposit_amount,int rc) throws Exception	
        {
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            IndexRequest indexRequest = new IndexRequest("trans").type("_doc").source("zid",user_email,"transaction",trans,"amount",deposit_amount,"balance",rc);
            client.index(indexRequest, RequestOptions.DEFAULT);
            
	}
	
        public void updateRcValue(int rc,String user_email) throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            UpdateRequest updateRequest = new UpdateRequest("userinfo","_doc",user_email);
            updateRequest.doc("rc",rc);
            client.update(updateRequest, RequestOptions.DEFAULT);
            
	}
	
        public void updatePassword(String user_newpassword,String user_email) throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            UpdateRequest updateRequest = new UpdateRequest("userinfo","_doc",user_email);
            updateRequest.doc("psw",user_newpassword);
            client.update(updateRequest, RequestOptions.DEFAULT);
            
	}
	
        public void updateAccess(String email) throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            UpdateRequest updateRequest = new UpdateRequest("userinfo","_doc",email);
            updateRequest.doc("access","yes");
            client.update(updateRequest, RequestOptions.DEFAULT);
            
         }
	
        public SearchHit[] selectDetailsBasedOnEmail(String user_email) throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            SearchRequest searchRequest = new SearchRequest();
 	    searchRequest.indices("userinfo");
 	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
 	    searchSourceBuilder.query(QueryBuilders.matchQuery("email",user_email));
 	    searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = null;
 	    searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);   
 	    SearchHit[] searchHit = searchResponse.getHits().getHits();

            return searchHit;
	 }
        
        public SearchHit[] selectUserBasedOnZid(String index,int user_zid) throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            SearchRequest searchRequest = new SearchRequest();
 	    searchRequest.indices(index);
 	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
 	    searchSourceBuilder.query(QueryBuilders.matchQuery("zid",user_zid));
 	    searchRequest.source(searchSourceBuilder);
 	    SearchResponse searchResponse = null;
 	    searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);   
 	    SearchHit[] searchHit = searchResponse.getHits().getHits();
 	    
            return searchHit;
        }
         
        public SearchHit[] selectAllUsers() throws Exception
	{
            RestHighLevelClient client =ConnectionForElasticSearch.getConnection();
            SearchRequest searchRequest = new SearchRequest();
 	    searchRequest.indices("userinfo");
 	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
 	    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
 	    searchRequest.source(searchSourceBuilder);
 	    SearchResponse searchResponse = null;
 	    searchResponse =client.search(searchRequest, RequestOptions.DEFAULT);   
 	    SearchHit[] searchHit = searchResponse.getHits().getHits();
 	    
            return searchHit;
	 }
}
