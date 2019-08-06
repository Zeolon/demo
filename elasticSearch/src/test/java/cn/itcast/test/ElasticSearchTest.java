package cn.itcast.test;

import cn.itcast.pojo.Article;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ElasticSearchTest {

    private TransportClient transportClient;

    @Before
    public void init() throws UnknownHostException {
        //创建客户端访问对象
        transportClient = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @After
    public void destroy(){
        //关闭资源
        transportClient.close();
    }

    @Test
    public void createIndex() throws IOException {
        //使用XContextFactory方式准备文档内容
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", 3)
                .field("title", "这是一个傻狗")
                .field("context", "不错,他的名字就叫骚敏")
                .endObject();
        //创建索引,类型,文档
        IndexResponse indexResponse = transportClient.prepareIndex("bitch", "description", "2")
                .setSource(xContentBuilder)
                .get();
        System.out.println(indexResponse.status());

    }
    @Test
    public void createIndex1() throws IOException {
        //使用map方式准备文档内容
        HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("id", 2);
                map.put("title", "这是一个傻*");
                map.put("context", "不错,他的名字就叫王旭");

        //创建索引,类型,文档
        IndexResponse indexResponse = transportClient.prepareIndex("saohuo", "why", "2")
                .setSource(map)
                .get();
        System.out.println(indexResponse.status());

    }

    /**
     * 根据索引查询全部/查询数据库所有
     */
    @Test
    public void searchAll(){
        //构建搜索内容
        SearchResponse response = transportClient.prepareSearch("bitch")
                .setTypes("description")
                .setQuery(QueryBuilders.matchAllQuery())
                .get();
        //获取搜索结果
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 字符串查询
     */
    @Test
    public void queryToString(){
        //构建搜索内容
        SearchResponse response = transportClient.prepareSearch("bitch")
                .setTypes("description")
                .setQuery(QueryBuilders.queryStringQuery("这是"))
                .get();
        //获取搜索结果
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        Iterator<SearchHit> iterator = hits.iterator();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }
    /**
     * 模糊查询
     */
    @Test
    public void fuzzyQuery(){
        //构建搜索内容
        SearchResponse response = transportClient.prepareSearch("bitch")
                .setTypes("description")
                .setQuery(QueryBuilders.wildcardQuery("title","这*"))
                .get();
        //获取搜索结果
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    /**
     * 词条查询
     */
    @Test
    public void termQuery(){
        //构建搜索内容
        SearchResponse response = transportClient.prepareSearch("bitch")
                .setTypes("description")
                .setQuery(QueryBuilders.termQuery("context","名"))
                .get();
        //获取搜索结果
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }
    /**
     * 单独创建索引
     */
    @Test
    public void createIndex3(){
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate("demo1").get();
        System.out.println(response.isAcknowledged());
        System.out.println(response.isShardsAcked());
    }

    /**
     * 删除索引操作
     */
    @Test
    public void delIndex(){
        DeleteIndexResponse response = transportClient.admin().indices().prepareDelete("demo1").get();
        System.out.println(response.isAcknowledged());
    }

    /**
     * 创建映射
     */
    @Test
    public void createMapper() throws ExecutionException, InterruptedException {
        //创建索引
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate("demo01").get();
        //构造map
        Map id = new HashMap();
        id.put("type","long");
        id.put("store","yes");

        Map content = new HashMap();
        content.put("type","string");
        content.put("store","yes");
        content.put("analyzer","ik_smart");

        Map title = new HashMap();
        title.put("type","string");
        title.put("store","yes");
        title.put("analyzer","ik_smart");

        Map properties = new HashMap();
        properties.put("id",id);
        properties.put("content",content);
        properties.put("title",title);

        Map article = new HashMap();
        article.put("properties",properties);

        Map mappings = new HashMap();
        mappings.put("article",article);

        PutMappingRequest request = Requests.putMappingRequest("demo01").type("article").source(mappings);
        PutMappingResponse mappingResponse = transportClient.admin().indices().putMapping(request).get();
        System.out.println(mappingResponse.isAcknowledged());
    }

    /**
     * 通过XContentBuilder方式创建Document
     */
    @Test
    public void creatDocument() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject()
                .field("id","1")
                .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
                .field("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是 用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能 够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                .endObject();
        transportClient.prepareIndex("demo01","article","1").setSource(builder).get();
    }

    /**
     * 通过对象创建Document
     */
    @Test
    public void createDocumentByProject(){
        Article article = new Article();
        article.setId(2l);
        article.setTitle("搜索工作其实很快乐");
        article.setContent("我们希望我们的搜索解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP的索引数据，我们希望我们的搜索服务器始终可用，我们希望能够一台开始并扩展到数百，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。Elasticsearch旨在解决所有这些问题和更多的问题。");

        //将数据转化为fastjson格式
        String jsonString = JSON.toJSONString(article);
        transportClient.prepareIndex("demo01","article",article.getId().toString()).setSource(jsonString, XContentType.JSON).get();
    }

    /**
     * 修改文档
     */
    @Test
    public void updateDocument() throws ExecutionException, InterruptedException {
        Article article = new Article();
        article.setId(2l);
        article.setTitle("对于我们来讲,搜索工作其实很快乐");
        article.setContent("我们希望我们的搜索解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP的索引数据，我们希望我们的搜索服务器始终可用，我们希望能够一台开始并扩展到数百，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。Elasticsearch旨在解决所有这些问题和更多的问题。");
        //将数据转化为fastjson格式
        String jsonString = JSON.toJSONString(article);
        transportClient.update(new UpdateRequest("demo01","article",article.getId().toString()).doc(jsonString,XContentType.JSON)).get();
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocument() throws ExecutionException, InterruptedException {

        transportClient.prepareDelete("demo01","article","1").get();
    }

    /**
     * 批量插入数据
     */
    @Test
    public void addData(){
        for (int i = 0; i < 100; i++) {
            Article article = new Article();
            article.setId((long)i);
            article.setTitle(i + "好的修改搜索工作其实很快乐");
            article.setContent(i+"我们希望我们的搜索解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP的索引数据，我们希望我们的搜索服务器始终可用，我们希望能够一台开始并扩展到数百，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。Elasticsearch旨在解决所有这些问题和更多的问题。");

            transportClient.prepareIndex("demo01","article",article.getId().toString()).setSource(JSON.toJSONString(article),XContentType.JSON).get();
        }
    }

    /**
     * 分页排序查询
     */
    @Test
    public void searchPage(){
        SearchRequestBuilder builder = transportClient.prepareSearch("demo01").setTypes("article").setQuery(QueryBuilders.matchAllQuery());
        builder.setFrom(0).setSize(10).addSort("id", SortOrder.DESC);
        SearchResponse response = builder.get();
        SearchHits hits = response.getHits();
        System.out.println("总共查询到的条数:"+hits.totalHits);
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            System.out.println(hit.getSource().get("title"));
        }
    }

    /**
     * 高亮显示
     */
    @Test
    public void highLightQuery(){
        SearchRequestBuilder builder = transportClient.prepareSearch("demo01").setTypes("article").setQuery(QueryBuilders.termQuery("content", "搜索"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font style='color:red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("content");
        builder.highlighter(highlightBuilder);
        SearchResponse response = builder.get();
        SearchHits hits = response.getHits();
        System.out.println("共查询到:"+hits.totalHits);
        for (SearchHit hit : hits) {
            Text[] contents = hit.getHighlightFields().get("content").getFragments();
            for (Text content : contents) {
                System.out.println(content);
            }
        }
    }
}
