package cn.itcast.test;


import cn.itcast.pojo.Article;
import cn.itcast.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ESTest {

    @Autowired
    private ArticleService articleService;

    /**
     * 测试新增
     */
    @Test
    public void teatAdd(){
        Article article=new Article();
        article.setId(1L);
        article.setTitle("elasticSearch 3.0版本发布...更新");
        article.setContent("ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜 索引擎，基于RESTful web接口");
        articleService.save(article);
    }

    /**
     * 测试更新
     */
    @Test
    public void update(){
        Article article=new Article();
        article.setId(1L);
        article.setTitle("elasticSearch 3.0版本发布...更新");
        article.setContent("ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜 索引擎，基于RESTful web接口");
        articleService.save(article);
    }

    /**
     * 批量插入
     */
    @Test
    public void saveArticleList(){
        for (int i = 2; i <=100 ; i++) {
            Article article=new Article();
            article.setId((long)i);
            article.setTitle(i+"elasticSearch 3.0版本发布...更新");
            article.setContent(i+"ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜 索引擎，基于RESTful web接口");
            articleService.save(article);
        }
    }

    /**
     * 测试删除
     */
    @Test
    public void delete(){

        articleService.deleteOne(5l);

    }

}
