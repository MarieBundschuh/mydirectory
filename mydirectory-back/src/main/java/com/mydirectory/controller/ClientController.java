package com.mydirectory.controller;

import com.mydirectory.entity.Client;
import com.mydirectory.repository.ClientRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Api( description =  "Api pour CRUD sur des clients DynamoDB")
@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @ApiOperation(value = "Récupère un client selon un id")
    @GetMapping("/clients/{id}")
    public Client getClientById(@PathVariable("id") String id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            return client.get();
        }
        throw new EntityNotFoundException("Cant find any client under given ID");
    }

    @ApiOperation(value = "Récupère la liste de tous les clients")
    @GetMapping("/clients/")
    public Iterable<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @ApiOperation(value = "Sauvegarde un client")
    @PostMapping("/clients/")
    public Client createClient(@RequestBody Client client){
        return clientRepository.save(client);
    }

    @ApiOperation(value = "MAJ un client")
    @PutMapping("/clients/")
    public Client updateClient(@RequestBody Client client){
        Optional<Client> original = clientRepository.findById(client.getId());
        if (original.isPresent()) {
            if (client.getId().equals(original.get().getId())) {
                return clientRepository.save(client);
            } else {
                throw new EntityNotFoundException("Ids not corresponding between the two entities");
            }
        } else {
            throw new EntityNotFoundException("Cant find any client under given ID");
        }
    }

    @ApiOperation(value = " Supprime un client selon un id")
    @DeleteMapping("/clients/{id}")
    public void deleteById(@PathVariable("id") String id){
        clientRepository.deleteById(id);
    }
}
