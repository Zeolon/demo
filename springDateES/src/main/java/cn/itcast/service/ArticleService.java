package cn.itcast.service;

import cn.itcast.pojo.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    //保存
    public void save(Article article);

    //查询全部
    List<Article> findAll();

    //查询单个
    Article findOne(Long id);

    //分页查询
    List<Article> findPage(Pageable pageable);

    //删除
    void deleteOne(Long id);
}
