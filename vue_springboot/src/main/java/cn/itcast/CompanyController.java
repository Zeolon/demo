package cn.itcast;

import cn.itcast.pojo.Result;
import cn.itcast.pojo.SsCompany;
import cn.itcast.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping
    public List<SsCompany> findAll(){
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public SsCompany findOne(@PathVariable String id){

        return companyService.findOne(id);
    }

    @PutMapping
    public Result update(@RequestBody SsCompany ssCompany){
        try {
            companyService.update(ssCompany);
            return new Result(true,"update ok...");
        }catch (Exception e){
            return new Result(false,"update false...");
        }
    }

    @PostMapping
    public Result add(@RequestBody SsCompany ssCompany){
        try {
            companyService.add(ssCompany);
            return new Result(true,"add ok...");
        }catch (Exception e){
            return new Result(false,"add false...");
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id){
        try {
            companyService.deleteById(id);
            return new Result(true,"delete ok...");
        }catch (Exception e){
            return new Result(false,"delete false...");
        }
    }
}
