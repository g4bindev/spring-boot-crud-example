package com.example.crud.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crud.dto.ClientDTO;
import com.example.crud.entities.Client;
import com.example.crud.repositories.ClientRepository;
import com.example.crud.services.exceptions.DatabaseException;
import com.example.crud.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO client) {
		Client entity = new Client();
		
		entity.setName(client.getName());
		entity.setCpf(client.getCpf());
		entity.setIncome(client.getIncome());
		entity.setBirthDate(client.getBirthDate());
		entity.setChildren(client.getChildren());
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO client) {
		try {
			Client entity = repository.getReferenceById(id);
			
			entity.setName(client.getName());
			entity.setCpf(client.getCpf());
			entity.setIncome(client.getIncome());
			entity.setBirthDate(client.getBirthDate());
			entity.setChildren(client.getChildren());
			
			entity = repository.save(entity);
			
			return new ClientDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);			
		} catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}