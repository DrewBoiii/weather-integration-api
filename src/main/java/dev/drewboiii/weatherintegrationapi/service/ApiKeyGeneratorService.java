package dev.drewboiii.weatherintegrationapi.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApiKeyGeneratorService {

    public String generate() {
        return UUID.randomUUID().toString();
    }

}
