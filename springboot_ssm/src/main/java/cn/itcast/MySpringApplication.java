package cn.itcast;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("cn.itcast.mapper")
public class MySpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringApplication.class,args);
    }
}
