package org.licensetracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.entity.Vendor;
import org.licensetracker.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vendors")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors(){
        return ResponseEntity.ok(vendorRepository.findAll());
    }
}
