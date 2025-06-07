package br.com.jeftavares.jbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {

    @GetMapping()
    public ResponseEntity<String> hello() {
        System.out.println("insede controller");
        return ResponseEntity.ok().build();
    }
}