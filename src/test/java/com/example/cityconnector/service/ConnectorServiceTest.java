package com.example.cityconnector.service;

import com.example.cityconnector.core.CityDataHandler;
import com.example.cityconnector.value.CityMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Slf4j
public class ConnectorServiceTest {
    CityDataHandler dataHandler = mock(CityDataHandler.class);

    @Test
    public void testCityConnectionsQuerying(){
        CityMapper cityMapper = new CityMapper("Boston", "Newark");
        given(this.dataHandler.checkCityMapperConnection(cityMapper))
                .willReturn(Boolean.TRUE);
        ConnectorService connectorService = new ConnectorService(dataHandler);
        boolean expected = connectorService.checkCityConnections(cityMapper);
        assertThat(true, Matchers.equalTo(expected));
    }

    @Test
    public void testCityConnectionsAddition(){
        CityMapper cityMapper = new CityMapper("Boston", "Newark");
        given(this.dataHandler.addCityMapperConnection(cityMapper))
                .willReturn(Boolean.TRUE);
        ConnectorService connectorService = new ConnectorService(dataHandler);
        boolean expected = connectorService.addCityConnections(cityMapper);
        assertThat(true, Matchers.equalTo(expected));
    }

    @Test
    public void testCityConnectionsRemoval(){
        CityMapper cityMapper = new CityMapper("Boston", "Newark");
        given(this.dataHandler.removeCityMapperConnection(cityMapper))
                .willReturn(Boolean.TRUE);
        ConnectorService connectorService = new ConnectorService(dataHandler);
        boolean expected = connectorService.removeCityConnections(cityMapper);
        assertThat(true, Matchers.equalTo(expected));
    }
}
