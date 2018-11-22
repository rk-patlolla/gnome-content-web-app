/*package com.gnomecontent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnomecontent.entity.Pubmed;

@RestController
public class SearchRestController {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	
	@GetMapping(value="/getArticles")
	public Page<Pubmed> getArticlesList() {
		
		Pageable pageble=PageRequest.of(1, 10);
		Query query = new Query().with(pageble);
		
		List<Pubmed> list = mongoTemplate.find(query, Pubmed.class);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageble, ()->mongoTemplate.count(query, Pubmed.class));
		
		System.out.println("Total Elemets"+page.getTotalElements());
		System.out.println(""+page.getTotalPages());
	
		
		return page;
		//return mongoTemplate.findAll(Pubmed.class);
		
		
		
		

		
	}
	

}
*/