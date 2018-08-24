package com.example.cityconnector.core;

import com.example.cityconnector.value.CityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;

@Slf4j
public class CityDataHandlerTest {
    CityDataHandler cityDataHandler = new CityDataHandler();
    @Before
    public void setUp() {
        cityDataHandler.addCityMapperConnection(new CityMapper("Boston", "New York"));
        cityDataHandler.addCityMapperConnection(new CityMapper("Philadelphia", "Newark"));
        cityDataHandler.addCityMapperConnection(new CityMapper("Newark", "Boston"));
        cityDataHandler.addCityMapperConnection(new CityMapper("Trenton", "Albany"));
        log.info(String.format("\nData Dump\n%s\n",cityDataHandler.dumpMap()));
    }
    @After
    public void tearDown() {
        cityDataHandler.cleanMap();
    }

    @Test
    public void testCityConnectionsBetweenDirectCityRoute(){
        boolean connected = cityDataHandler.checkCityMapperConnection(new CityMapper("Boston", "Newark"));
        assertThat(true, Matchers.equalTo(connected));
    }

    @Test
    public void testCityConnectionsBetweenInDirectCityRoutes(){
        boolean connected = cityDataHandler.checkCityMapperConnection(new CityMapper("Boston", "Philadelphia"));
        assertThat(true, Matchers.equalTo(connected));
    }

    @Test
    public void testCityConnectionsBetweenUnConnectedCityRoutes(){
        boolean connected = cityDataHandler.checkCityMapperConnection(new CityMapper("Philadelphia", "Albany"));
        assertThat(false, Matchers.equalTo(connected));
    }

    @Test
    public void testCityConnectionsAddition(){
        int dataMapSize = cityDataHandler.countMap();
        assertThat(6, Matchers.equalTo(dataMapSize));
    }

    @Test
    public void testCityConnectionsRemoval(){
        //this is not exhaustive test, it does not test for cities that have multiple mappings
        boolean removed = cityDataHandler.removeCityMapperConnection(new CityMapper("Trenton", "Albany"));
        assertThat(true, Matchers.equalTo(removed));
        int dataMapSize = cityDataHandler.countMap();
        assertThat(4, Matchers.equalTo(dataMapSize));
    }
}
