package com.example.cityconnector.controller;

import com.example.cityconnector.service.ConnectorService;
import com.example.cityconnector.value.CityMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
public class ConnectorController {
    @Autowired
    ConnectorService connectorService;

    @ApiOperation(value = "Find the connection between two cities", response = String.class)
    @GetMapping("/connected")
    public String getConnector(@RequestParam("origin") String origin,
                               @RequestParam("destination") String destination){
        boolean connected = connectorService.checkCityConnections(new CityMapper(origin, destination));
        log.info(String.format("Query from %s to %s, result is %s", origin, destination, connected));
        return (connected)?"yes":"no";
    }

    @ApiOperation(value = "Add an in-memory connection between two cities", response = String.class)
    @PostMapping("/connected")
    public String addConnector(@RequestParam("origin") String origin,
                               @RequestParam("destination") String destination){
        boolean added = connectorService.addCityConnections(new CityMapper(origin, destination));
        log.info(String.format("Add from %s to %s, result is %s", origin, destination, added));
        return (added)?"added":"not added";
    }

    @ApiOperation(value = "Remove an in-memory connection between two cities", response = String.class)
    @DeleteMapping("/connected")
    public String removeConnector(@RequestParam("origin") String origin,
                               @RequestParam("destination") String destination){
        boolean removed = connectorService.removeCityConnections(new CityMapper(origin, destination));
        log.info(String.format("Remove from %s to %s, result is %s", origin, destination, removed));
        return (removed)?"removed":"not removed";
    }

    @ApiOperation(value = "View all in-memory connections cities", response = String.class)
    @GetMapping("/connected/all")
    public String getConnector(){
        return connectorService.dumpCityConnections();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity onException(Exception e)  {
        String msg = Optional.of(e.getMessage()).get();
        log.error("Error in processing request", e);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }
}
