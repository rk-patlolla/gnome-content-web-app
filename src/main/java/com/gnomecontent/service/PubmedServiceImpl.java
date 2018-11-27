package com.gnomecontent.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQuery.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.gnomecontent.documents.PubmedArticles;
import com.gnomecontent.documents.PubmedExcelPhraseQuery;
import com.gnomecontent.documents.PubmedUmlsKeywords;
import com.gnomecontent.documents.PubmedUmlsPhraseQuery;
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
		
		/*  BoolQueryBuilder query = QueryBuilders.boolQuery()
	                .should(
	                        QueryBuilders.queryStringQuery(searchKeyword)
	                                .lenient(true)
	                                .field("articleTitle")
	                                .field("abstractText")
	                                .field("articleText")
	                );
	*/
		
	/* // Match Phrase Query
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withPageable(pageable)
				  .withQuery(QueryBuilders.matchPhraseQuery("abstractText", searchKeyword))
				  .build();
		
		*/
		
		
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.should(QueryBuilders.matchPhraseQuery("pmid", searchKeyword));
		query.should(QueryBuilders.matchPhraseQuery("articleTitle", searchKeyword));
		query.should(QueryBuilders.matchPhraseQuery("abstractText", searchKeyword));
		query.should(QueryBuilders.matchPhraseQuery("articleText", searchKeyword));
		
		/*SearchQuery searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(QueryBuilders.multiMatchQuery(searchKeyword)
				    .field("articleTitle")
				    .field("abstractText")
				    .field("articleText")
				    .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
				  .build();*/
			
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
				  .withQuery(query)
				  .build();
		
		
		return pubRepository.search(searchQuery);
		//return pubRepository.search(queryStringQuery(searchKeyword),pageable);
	}


	@Override
	public Page<Pubmed> getCardiologyArticles(Pageable pageable) {
		
		
		Query query = new Query().with(pageable);
	
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, Pubmed.class));
		
		return page;
		
	}


	@Override
	public Page<Pubmed> getArticlesByKeyword(String searchKeyword, Pageable pageable) {
		Query query = new Query();
		//query.addCriteria(Criteria.where("keywords").regex(searchKeyword,"i")).with(pageable);
		query.addCriteria((new Criteria().orOperator(Criteria.where("pmid").regex(searchKeyword, "i"),
                            Criteria.where("journalTitle").regex(searchKeyword, "i"),
                            Criteria.where("articleTitle").regex(searchKeyword, "i"), 
                            Criteria.where("keywords").regex(searchKeyword, "i")))).with(pageable);
		List<Pubmed> list = mongoTemp.find(query, Pubmed.class);
		
		Page<Pubmed> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, Pubmed.class));
		
		return page;
	}


	@Override
	public Page<PubmedUmlsKeywords> getUmlsArticlesByKeyword(Pageable pageable) {
		Query query = new Query().with(pageable);
		
		List<PubmedUmlsKeywords> list = mongoTemp.find(query, PubmedUmlsKeywords.class);
		
		Page<PubmedUmlsKeywords> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedUmlsKeywords.class));
		
		return page;
		
	}


	@Override
	public Page<PubmedUmlsKeywords> searchByUmlKeyword(String searchKeyword, Pageable pageable) {
		Query query = new Query();
		//query.addCriteria(Criteria.where("keywords").regex(searchKeyword,"i")).with(pageable);
		query.addCriteria((new Criteria().orOperator(Criteria.where("pmid").regex(searchKeyword, "i"),
                Criteria.where("journalTitle").regex(searchKeyword, "i"),
                Criteria.where("articleTitle").regex(searchKeyword, "i"), 
                Criteria.where("keywords").regex(searchKeyword, "i")))).with(pageable);
		List<PubmedUmlsKeywords> list = mongoTemp.find(query, PubmedUmlsKeywords.class);
		
		Page<PubmedUmlsKeywords> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedUmlsKeywords.class));
		
		return page;
	}


	@Override
	public Page<PubmedUmlsPhraseQuery> getUmlsPhraseArticles(Pageable pageable) {
		Query query = new Query().with(pageable);
		
		List<PubmedUmlsPhraseQuery> list = mongoTemp.find(query, PubmedUmlsPhraseQuery.class);
		
		Page<PubmedUmlsPhraseQuery> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedUmlsPhraseQuery.class));
		
		return page;
	}


	@Override
	public Page<PubmedExcelPhraseQuery> getExcelPhraseArticles(Pageable pageable) {
		Query query = new Query().with(pageable);
		
		List<PubmedExcelPhraseQuery> list = mongoTemp.find(query, PubmedExcelPhraseQuery.class);
		
		Page<PubmedExcelPhraseQuery> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedExcelPhraseQuery.class));
		
		return page;
	}


	@Override
	public Page<PubmedUmlsPhraseQuery> searchUmlsPhraseArticles(String searchKeyword, Pageable pageable) {
		Query query = new Query();
		//query.addCriteria(Criteria.where("keywords").regex(searchKeyword,"i")).with(pageable);
		query.addCriteria((new Criteria().orOperator(Criteria.where("pmid").regex(searchKeyword, "i"),
                Criteria.where("journalTitle").regex(searchKeyword, "i"),
                Criteria.where("articleTitle").regex(searchKeyword, "i"), 
                Criteria.where("keywords").regex(searchKeyword, "i")))).with(pageable);
		List<PubmedUmlsPhraseQuery> list = mongoTemp.find(query, PubmedUmlsPhraseQuery.class);
		
		Page<PubmedUmlsPhraseQuery> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedUmlsPhraseQuery.class));
		
		return page;
	}


	@Override
	public Page<PubmedExcelPhraseQuery> searchExcelPhraseArticles(String searchKeyword, Pageable pageable) {
		Query query = new Query();
		//query.addCriteria(Criteria.where("keywords").regex(searchKeyword,"i")).with(pageable);
		query.addCriteria((new Criteria().orOperator(Criteria.where("pmid").regex(searchKeyword, "i"),
                Criteria.where("journalTitle").regex(searchKeyword, "i"),
                Criteria.where("articleTitle").regex(searchKeyword, "i"), 
                Criteria.where("keywords").regex(searchKeyword, "i")))).with(pageable);
		List<PubmedExcelPhraseQuery> list = mongoTemp.find(query, PubmedExcelPhraseQuery.class);
		
		Page<PubmedExcelPhraseQuery> page = PageableExecutionUtils.getPage(list, pageable, ()->mongoTemp.count(query, PubmedExcelPhraseQuery.class));
		
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
