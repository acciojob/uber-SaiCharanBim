package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.*;

import static com.driver.model.TripStatus.*;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Optional<Customer> cur = customerRepository2.findById(customerId);
		Customer existingAdmin = cur.get();
		customerRepository2.delete(existingAdmin);

	}
	public Customer getCustomer ( int customerId){
		Optional<Customer> cur = customerRepository2.findById(customerId);
		Customer existingAdmin = cur.get();
		return existingAdmin;
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver>driverList = driverRepository2.findAll();
		Collections.sort(driverList,(a, b)->{
			return a.getDriverId()-b.getDriverId();
		});
		boolean got = false;
		TripBooking tripBooking= new TripBooking();
		for( Driver driver : driverList){
			if(driver.getCab().isAvailable()){
				got = true ;
				tripBooking.setCustomer(getCustomer(customerId));
				tripBooking.setFromLocation(fromLocation);
				tripBooking.setToLocation(toLocation);
				tripBooking.setDistanceInKm(distanceInKm);
				tripBooking.setStatus(CONFIRMED);
				driver.getCab().setAvailable(false);
				break;
			}
		}
		if (!got){
			throw new Exception("No cab available!");
		}

		return tripBooking;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> cur = tripBookingRepository2.findById(tripId);
		TripBooking existingTrip = cur.get();
		existingTrip.getDriver().getCab().setAvailable(true);
		existingTrip.setStatus(CANCELED);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> cur = tripBookingRepository2.findById(tripId);
		TripBooking existingTrip = cur.get();
		existingTrip.getDriver().getCab().setAvailable(true);
		existingTrip.setStatus(COMPLETED);

	}
}
