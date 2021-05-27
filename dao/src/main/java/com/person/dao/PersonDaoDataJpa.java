package com.person.dao;

import com.person.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDaoDataJpa extends CrudRepository<Integer, Person> {

}
