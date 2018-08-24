package com.example.cityconnector.controller;

import com.example.cityconnector.controller.ConnectorController;
import com.example.cityconnector.service.ConnectorService;
import com.example.cityconnector.value.CityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import  static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConnectorController.class)
public class CityConnectorApplicationIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ConnectorService connectorService;

	@Test
	public void checkConnectionBetweenTwoLinkedCities() throws Exception {
		given(this.connectorService.checkCityConnections(new CityMapper("Boston", "Newark")))
				.willReturn(Boolean.TRUE);
		this.mvc.perform(get("/connected")
				.param("origin", "Boston")
				.param("destination", "Newark")
				.accept(MediaType.TEXT_PLAIN_VALUE))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("yes"));
	}

	@Test
	public void checkConnectionBetweenTwoDifferentCities() throws Exception {
		given(this.connectorService.checkCityConnections(new CityMapper("New York", "Newark")))
				.willReturn(Boolean.TRUE);
		this.mvc.perform(get("/connected")
				.param("origin", "Boston")
				.param("destination", "Newark")
				.accept(MediaType.TEXT_PLAIN_VALUE))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("no"));
	}

	@Test
	public void addConnectionBetweenTwoDifferentCities() throws Exception {
		given(this.connectorService.addCityConnections(new CityMapper("Boston", "Newark")))
				.willReturn(Boolean.TRUE);
		this.mvc.perform(post("/connected")
				.param("origin", "Boston")
				.param("destination", "Newark")
				.accept(MediaType.TEXT_PLAIN_VALUE))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("added"));
	}

	@Test
	public void removeConnectionBetweenTwoDifferentCities() throws Exception {
		given(this.connectorService.removeCityConnections(new CityMapper("Boston", "Newark")))
				.willReturn(Boolean.TRUE);
		this.mvc.perform(delete("/connected")
				.param("origin", "Boston")
				.param("destination", "Newark")
				.accept(MediaType.TEXT_PLAIN_VALUE))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("removed"));
	}
}
