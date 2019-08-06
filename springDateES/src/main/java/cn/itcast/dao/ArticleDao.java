package cn.itcast.dao;

import cn.itcast.pojo.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDao extends ElasticsearchRepository<Article,Long> {


}
