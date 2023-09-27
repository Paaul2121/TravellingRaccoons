package com.example.demo.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/locations")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @GetMapping(path = "/getAllLocations")
    public List<Location> getLocations() {
        return locationService.getLocations();
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @PostMapping(path = "/createLocation")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location newLocation = locationService.addLocation(location);
        return ResponseEntity.ok(newLocation);
    }

    @DeleteMapping(path = "{locationId}")
    public void deleteLocation(@PathVariable("locationId") Long locationId) {
        locationService.deleteLocation(locationId);
    }

    @PutMapping(path = "{locationId}")
    public void updateLocation(
            @PathVariable("locationId") Long locationId,
            @RequestBody(required = false) Location locationUpdate) {
        String locationName = locationUpdate.getLocationName();
        String type = locationUpdate.getType();
        String locationAddress = locationUpdate.getLocationAddress();
        Boolean visited = locationUpdate.isVisited();
        String notes = locationUpdate.getNotes();
        locationService.updateLocationDetails(locationId, type, locationName, locationAddress, visited, notes);

    }
}
