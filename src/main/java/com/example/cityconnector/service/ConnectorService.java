package com.example.cityconnector.service;

import com.example.cityconnector.core.CityDataHandler;
import com.example.cityconnector.value.CityMapper;
import org.springframework.stereotype.Service;

@Service
public class ConnectorService {
    CityDataHandler cityDataHandler;

    public ConnectorService(CityDataHandler cityDataHandler){
        this.cityDataHandler = cityDataHandler;
    }

    public boolean addCityConnections(CityMapper cityMapper){
        return this.cityDataHandler.addCityMapperConnection(cityMapper);
    }

    public boolean removeCityConnections(CityMapper cityMapper){
        return this.cityDataHandler.removeCityMapperConnection(cityMapper);
    }

    public boolean checkCityConnections(CityMapper cityMapper){
        return this.cityDataHandler.checkCityMapperConnection(cityMapper);
    }

    public String dumpCityConnections(){
        return this.cityDataHandler.dumpMap();
    }
}
