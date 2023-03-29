package fr.croixrouge.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ResourceService {

    public List<String> getResources() {
        return Arrays.asList("Resource 1", "Resource 2", "Resource 3");
    }
}
