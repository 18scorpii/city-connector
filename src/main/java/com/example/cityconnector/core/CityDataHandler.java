package com.example.cityconnector.core;

import com.example.cityconnector.value.CityMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CityDataHandler {
    //the data structure is made private and can be onoy accessed by the public methods listed below
    private Map<String, List<String>> dataMap = null;

    /*
    uses a map structure to store both origin & destination as individual keys
    the value is a list of allowed destination routes
    the map is created as ConcurrentHashMap to handle read-write contentions
    By duplicating the mapping (by adding both origin & destination) we make search simpler
     */
    public CityDataHandler(){
        dataMap = new ConcurrentHashMap<String, List<String>>();
    }

    /*
    Adds the origin and destination as separate keys by checking if the value is already present
    If its present, then value list is added with a additional destination option from that origin city
     */
    public boolean addCityMapperConnection(CityMapper cityMapper){
        normalizeCityNames(cityMapper);
        boolean fromToAdded = addEachCityMapperConnection(cityMapper.getOrigin(), cityMapper.getDestination());
        boolean toFromAdded = addEachCityMapperConnection(cityMapper.getDestination(), cityMapper.getOrigin());
        return (fromToAdded && toFromAdded);
    }
    private boolean addEachCityMapperConnection(String source, String target) {
        boolean result = false;
        if(dataMap.containsKey(source)){
            if(!dataMap.get(source).contains(target)){
                result = dataMap.get(source).add(target);
            }
        }else{
            List<String> targetList = new ArrayList<String>();
            result = targetList.add(target);
            dataMap.put(source, targetList);
        }
        log.info(String.format("Adding source %s to target %s, result is %s", source, target, result));
        return result;
    }


    /*
    Removed the origin and destination as separate keys by checking if the value is present
    If its present, then value list is added with a additional destination option from that origin city
     */
    public boolean removeCityMapperConnection(CityMapper cityMapper){
        normalizeCityNames(cityMapper);
        boolean fromToRemoved = removeEachCityMapperConnection(cityMapper.getOrigin(), cityMapper.getDestination());
        boolean toFromRemoved = removeEachCityMapperConnection(cityMapper.getDestination(), cityMapper.getOrigin());
        return (fromToRemoved && toFromRemoved);
    }
    private boolean removeEachCityMapperConnection(String source, String target) {
        boolean result = false;
        if(dataMap.containsKey(source)){
            if(dataMap.get(source).contains(target)) {
                result = dataMap.get(source).remove(target);
                //remove the mapping if its the last mapped city
                if(dataMap.get(source).size() == 0){
                    dataMap.remove(source);
                }
            }
        }
        log.info(String.format("Removed source %s to target %s, result is %s", source, target, result));
        return result;
    }

    /*
    Uses recursion to find mapping between origin to destination, in-case there are intermediate cities
    Always we look for origin to destination as we have data mapping for both ways
     */
    public boolean checkCityMapperConnection(CityMapper cityMapper){
        normalizeCityNames(cityMapper);
        //we need to look from source to destination only as we added both ways as separate entries in the map
        List<String> alreadyVisitedCities = new ArrayList<String>();
        boolean connected = checkEachIntermediateCityMapperConnection(cityMapper.getOrigin(), cityMapper.getDestination(), alreadyVisitedCities);
        log.info(String.format("Connection between Origin %s to Destination %s, result is %s", cityMapper.getOrigin(),
                cityMapper.getDestination(), connected));
        return connected;
    }
    private boolean checkEachIntermediateCityMapperConnection(String source, String target, List<String> alreadyVisitedCities){
        boolean result = false;
        if(dataMap.containsKey(source)){
            alreadyVisitedCities.add(source);
            List<String> possibleDestinationConnectors = dataMap.get(source);
            if(possibleDestinationConnectors.contains(target)){
                //we found a route path
                result = true;
            }else{
                //we need to check if each of this destinations can potentially lead to the final destination
                for(String newSource : possibleDestinationConnectors){
                    //avoid already visited cities as it will cause an endless loop
                    if(!alreadyVisitedCities.contains(newSource)){
                        result = checkEachIntermediateCityMapperConnection(newSource, target, alreadyVisitedCities);
                        if(result){
                            //we found a successful route path
                            break;
                        }
                    }
                }
            }
        }
        log.info(String.format("Checking Connection between source %s to Destination %s, result is %s",
                source, target, result));
        return result;
    }

    /*
    City names are trimmed of spaces and lower cased to avoid user error
    Value object already ensures they are not null values
     */
    private void normalizeCityNames(CityMapper cityMapper) {
        cityMapper.setOrigin(cityMapper.getOrigin().trim().toLowerCase().replaceAll("\\s", ""));
        cityMapper.setDestination(cityMapper.getDestination().trim().toLowerCase().replaceAll("\\s", ""));
    }

    /*
    Used to visualize the data structure anytime
     */
    public String dumpMap() {
        StringBuffer buffer =  new StringBuffer();
        for (String source : dataMap.keySet()){
            buffer.append(String.format("Mappings : %s -> %s\n", source, dataMap.get(source)));
        }
        return buffer.toString();
    }
    public void cleanMap() {
        dataMap.clear();
    }
    public int countMap() {
        return dataMap.size();
    }
}
