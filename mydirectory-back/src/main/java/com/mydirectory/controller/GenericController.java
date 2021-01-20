package com.mydirectory.controller;

import com.mydirectory.repository.GenericRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api( description =  "Api pour CRUD sur des items DynamoDB")
@RestController
public class GenericController {

    @Autowired
    private GenericRepository genericRepository;

    @ApiOperation(value = "Récupère un item selon un id")
    @GetMapping("/items/{id}")
    public String getItem(@PathVariable("id") String id){
        return genericRepository.getById(id);
    }

    @ApiOperation(value = "Récupère la liste de tous les items")
    @GetMapping("/items/")
    public List<String> getAllItems(){
        return genericRepository.getAll();
    }

    @ApiOperation(value = "Sauvegarde un item")
    @PostMapping("/items/")
    public String saveItem(@RequestBody String jsonString){
        genericRepository.save(jsonString);
        return jsonString;
    }


}
