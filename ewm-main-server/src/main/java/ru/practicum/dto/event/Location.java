package ru.practicum.dto.event;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class Location {
    private Double lat;
    private Double lon;

    // TODO причина закоментированного кода - тесты POSTMAN дают неадекватные значения координат
//    public void setLat(double lat) {
//        if (lat > 90 || lat < -90)
//            throw new ValidationException(
//                    "Validation failed",
//                    "Latitude couldn't be greater than 90 and smaller than -90"
//            );
//        this.lat = lat;
//    }
//
//    public void setLon(double lon) {
//        if (lon > 180 || lon < -180)
//            throw new ValidationException(
//                    "Invalid method argument",
//                    "Longitude couldn't be greater than 180 and smaller than -180"
//            );
//        this.lon = lon;
//    }
}
