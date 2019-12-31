package com.codegym.controllers;

import com.codegym.models.Customer;
import com.codegym.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/user/customer/")
    ResponseEntity<List<Customer>> findAllCustomer(){
        List<Customer> customers = (List<Customer>) customerService.findAll();
        if(customers.isEmpty()){
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/customer/", method = RequestMethod.POST)
    ResponseEntity<Customer> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder){
        customerService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/admin/customer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Customer>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/admin/customer/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getCustomer(@PathVariable ("id") Long id){
        Optional <Customer> customer = customerService.findById(id);
        if(!customer.isPresent()){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/customer/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> editCustomer(@PathVariable ("id") Long id, @RequestBody Customer customer){
        Optional<Customer> currentCustomer = customerService.findById(id);
        if(!currentCustomer.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentCustomer.get().setFirstName(customer.getFirstName());
        currentCustomer.get().setLastName(customer.getLastName());
        currentCustomer.get().setId(customer.getId());
        customerService.save(currentCustomer.get());

        return new ResponseEntity <>( currentCustomer, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/customer/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Customer> removeCustomer(@PathVariable ("id") Long id){
        Optional<Customer> customer = customerService.findById(id);
        if(!customer.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        customerService.remove(id);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }
}