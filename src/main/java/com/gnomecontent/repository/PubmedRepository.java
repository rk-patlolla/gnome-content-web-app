package com.gnomecontent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gnomecontent.entity.Pubmed;

public interface PubmedRepository extends MongoRepository<Pubmed,String> {

}
