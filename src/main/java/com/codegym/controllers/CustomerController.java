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
}