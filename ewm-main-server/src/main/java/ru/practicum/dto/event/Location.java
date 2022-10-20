package ru.practicum.dto.event;

import lombok.Getter;

import javax.validation.ValidationException;

@Getter
public class Location {
    private Double lat;
    private Double lon;

    public void setLat(double lat) {
        if (lat > 90 || lat < -90)
            throw new ValidationException("Latitude couldn't be greater than 90 and smaller than -90");
        this.lat = lat;
    }

    public void setLon(double lon) {
        if (lon > 180 || lon < -180)
            throw new ValidationException("Longitude couldn't be greater than 180 and smaller than -180");
        this.lon = lon;
    }
}
