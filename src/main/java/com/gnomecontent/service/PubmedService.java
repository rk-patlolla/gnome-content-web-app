package com.gnomecontent.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gnomecontent.documents.PubmedArticles;
import com.gnomecontent.entity.Pubmed;

public interface PubmedService {
	
	public Page<Pubmed> getPubmedArticles(int page);
	public Page<Pubmed> searchArticlesByPmid(String searchTerm,int page);
	public Page<PubmedArticles> find(Pageable pageable);
	public Page<PubmedArticles> getArticlesBySearch(String searchKeyword,Pageable pageable);
	public Page<Pubmed> getCardiologyArticles(Pageable pageable);
	
	

}
