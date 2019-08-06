package cn.itcast.service.impl;

import cn.itcast.dao.ArticleDao;
import cn.itcast.pojo.Article;
import cn.itcast.service.ArticleService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 实现类
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public void save(Article article) {
        articleDao.save(article);
    }

    @Override
    public List<Article> findAll() {
        Iterator<Article> iterator = articleDao.findAll(Sort.by(Sort.Order.asc("id"))).iterator();
        return Lists.newArrayList(iterator);
    }

    @Override
    public Article findOne(Long id) {
        return articleDao.findById(id).get();
    }

    @Override
    public List<Article> findPage(Pageable pageable) {
        Page<Article> pages = articleDao.findAll(pageable);
        System.out.println("总记录数:"+pages.getTotalElements());
        System.out.println("当前页记录数"+pages.getNumberOfElements());
        return pages.getContent();
    }

    @Override
    public void deleteOne(Long id) {
        articleDao.deleteById(id);
    }
}
