package com.example.demo.trip;

import com.example.demo.location.Location;
import com.example.demo.location.LocationRepository;
import com.example.demo.memento.Memento;
import com.example.demo.memento.MementoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final LocationRepository locationRepository;
    private final MementoRepository mementoRepository;

    @Autowired
    public TripService(TripRepository tripRepository, LocationRepository locationRepository, MementoRepository mementoRepository) {
        this.tripRepository = tripRepository;
        this.locationRepository = locationRepository;
        this.mementoRepository = mementoRepository;
    }

    public List<Trip> getTrips() {
        return tripRepository.findAll();
    }

    public Trip addTrip(Trip trip) {
        Optional<Trip> tripOptional = tripRepository.findTripById(trip.getId());
        if(tripOptional.isPresent()) {
            throw new IllegalStateException("trip already exists");
        }
        LocalDate departureDate = trip.getDepartureDate();
        LocalDate arrivalDate = trip.getArrivalDate();
        if (departureDate != null && arrivalDate != null) {
            if (departureDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Departure date must be in the future");
            }
            if(departureDate.isBefore(arrivalDate)) {
                throw new IllegalArgumentException("Departure date must be after the arrival date");
            }
            if (arrivalDate.isBefore(departureDate)) {
                throw new IllegalArgumentException("Arrival date must be after the departure date");
            }
        } else {
            throw new IllegalStateException("Dates cannot be null");
        }
        tripRepository.save(trip);
        System.out.println(trip);
        return trip;
    }

    public void deleteTripById(Long tripId) {
        if(!tripRepository.existsById(tripId)) {
            throw new IllegalStateException("trip with id " + tripId + " does not exist");
        }
        tripRepository.deleteById(tripId);
    }

    public void removeLocationFromTrip(Long tripId, Long locationId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("trip with id " + tripId + " does not exist"));
        for(Location location : trip.getLocationList()) {
            if (location.getId().equals(locationId)) {
                trip.getLocationList().remove(location);
                tripRepository.save(trip);
            }
        }
    }

    public void removeMementoFromTrip(Long tripId, Long mementoId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("trip with id " + tripId + " does not exist"));
        for(Memento memento : trip.getMementos()) {
            if (memento.getId().equals(mementoId)) {
                trip.getMementos().remove(memento);
                tripRepository.save(trip);
            }
        }
    }

    @Transactional
    public void addLocationToTrip(Long tripId, Location location) {
        boolean isNotPresentAndNull = false;
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("trip with id " + tripId + " does not exist"));
        for(Location location1 : trip.getLocationList()) {
            if (location1 == location || location == null) {
                isNotPresentAndNull = true;
                break;
            }
        }
        if(!isNotPresentAndNull) {
            trip.getLocationList().add(location);
            locationRepository.save(location);
        }
    }

    @Transactional
    public void addMementoToTrip(Long tripId, Memento memento) {
        boolean isNotPresentAndNull = false;
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("trip with id " + tripId + " does not exist"));
        for(Memento memento1 : trip.getMementos()) {
            if (memento1 == memento || memento1 == null) {
                isNotPresentAndNull = true;
                break;
            }
        }
        if(!isNotPresentAndNull) {
            trip.getMementos().add(memento);
            mementoRepository.save(memento);
        }
    }

    @Transactional
    public void editDatesForTrip(Long tripId, LocalDate departureDate, LocalDate arrivalDate) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("trip with id " + tripId + " does not exist"));
        if(!departureDate.equals(trip.getDepartureDate())
                && departureDate.isAfter(trip.getArrivalDate())) {
            trip.setDepartureDate(departureDate);
        }
        if(!arrivalDate.equals(trip.getArrivalDate())
                && arrivalDate.isBefore(trip.getDepartureDate())) {
            trip.setArrivalDate(arrivalDate);
        }
        tripRepository.save(trip);
    }

}
