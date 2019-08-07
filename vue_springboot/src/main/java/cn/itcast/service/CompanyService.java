package cn.itcast.service;

import cn.itcast.pojo.SsCompany;

import java.util.List;

public interface CompanyService {
    List<SsCompany> findAll();

    SsCompany findOne(String id);

    void update(SsCompany ssCompany);

    void add(SsCompany ssCompany);

    void deleteById(String id);
}
