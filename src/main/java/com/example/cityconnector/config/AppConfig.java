package com.example.cityconnector.config;

import com.example.cityconnector.core.CityDataHandler;
import com.example.cityconnector.value.CityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@EnableSwagger2
@Slf4j
public class AppConfig {
    @Bean
    public CityDataHandler getCityDataHandler(){
        return new CityDataHandler();
    }

    @Bean
    CommandLineRunner init(CityDataHandler cityDataHandler){
        return args -> {
            if(args.length > 0){
                String fileName = args[0];
                try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                    stream.forEach(s->{
                        String[] cities = s.split(",");
                        CityMapper mapper = new CityMapper(cities[0], cities[1]);
                        cityDataHandler.addCityMapperConnection(mapper);
                    });
                } catch (Exception e) {
                   log.error("Error in loading the input file "+fileName, e);
                }
            }else {
                // Adding default 4 cities mapping
                cityDataHandler.addCityMapperConnection(new CityMapper("Boston", "New York"));
                cityDataHandler.addCityMapperConnection(new CityMapper("Philadelphia", "Newark"));
                cityDataHandler.addCityMapperConnection(new CityMapper("Newark", "Boston"));
                cityDataHandler.addCityMapperConnection(new CityMapper("Trenton", "Albany"));
            }
        };
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.cityconnector"))
                .paths(PathSelectors.any())
                .build();
    }
}
