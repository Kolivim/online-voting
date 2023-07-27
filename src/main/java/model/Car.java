package model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Car {
    @NonNull
    private String brand;
    @NonNull
    private String model;
    @NonNull
    private String subModel;
}