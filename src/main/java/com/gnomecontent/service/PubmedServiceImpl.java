package com.gnomecontent.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.gnomecontent.documents.PubmedArticles;
import com.gnomecontent.entity.Pubmed;
import com.gnomecontent.repository.PubmedArticlesRepository;

@Service
public class PubmedServiceImpl implements PubmedService{
	
	@Autowired
	private MongoTemplate mongoTemp;
	@Autowired
	private PubmedArticlesRepository pubRepository;

	@Override
	public Page<Pubmed> getPubmedArticles(int pageNo) {
			
		Pageable pageble=PageRequest.of(pageNo, 10);
		Query query = new Query().with(pageble);
	
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageble, ()->mongoTemp.count(query, Pubmed.class));
	
		return page;
		
	}


	@Override
	public Page<Pubmed> searchArticlesByPmid(String searchTerm,int pageNo) {
		
		Pageable pageble=PageRequest.of(pageNo, 10);
		
		Query query = new Query().with(pageble).addCriteria(Criteria.where("pmid").is(searchTerm));
		//Query query1 = new Query().addCriteria(Criteria.where("pmid").is(searchTerm).orOperator(Criteria.where("articleTitle").is(searchTerm)));
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		System.out.println("List"+list);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageble, ()->mongoTemp.count(query, Pubmed.class));
		
		System.out.println(page.getTotalElements());

		return  page;
	}


	@Override
	public Page<PubmedArticles> find(Pageable pageable) {
		
		return pubRepository.findAll(pageable);
	}


	@Override
	public Page<PubmedArticles> getArticlesBySearch(String searchKeyword,Pageable pageable) {
		
		return pubRepository.search(queryStringQuery(searchKeyword),pageable);
	}


	@Override
	public Page<Pubmed> getCardiologyArticles(Pageable pageable) {
		
		
		Query query = new Query().with(pageable);
	
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, Pubmed.class));
		
		return page;
		
	}


/*	@Override
	public Page<Pubmed> find(Pageable pageable) {
		Query query = new Query().with(pageable);
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, Pubmed.class));
		return page;
	}
*/
}
