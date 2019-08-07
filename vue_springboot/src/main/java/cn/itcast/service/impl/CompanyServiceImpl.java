package cn.itcast.service.impl;

import cn.itcast.dao.CompanyMapper;
import cn.itcast.pojo.SsCompany;
import cn.itcast.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public List<SsCompany> findAll() {
        return companyMapper.selectList(null);
    }

    @Override
    public SsCompany findOne(String id) {
        return companyMapper.selectById(id);
    }

    @Override
    public void update(SsCompany ssCompany) {
        companyMapper.updateById(ssCompany);
    }

    @Override
    public void add(SsCompany ssCompany) {
        companyMapper.insert(ssCompany);
    }

    @Override
    public void deleteById(String id) {
        companyMapper.deleteById(id);
    }
}
