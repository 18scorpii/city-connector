package com.example.cityconnector.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CityMapper {
    @NonNull
    String origin;
    @NonNull
    String destination;
}
