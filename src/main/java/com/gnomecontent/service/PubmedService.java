package com.gnomecontent.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gnomecontent.documents.PubmedArticles;
import com.gnomecontent.documents.PubmedExcelPhraseQuery;
import com.gnomecontent.documents.PubmedUmlsKeywords;
import com.gnomecontent.documents.PubmedUmlsPhraseQuery;
import com.gnomecontent.entity.Pubmed;

public interface PubmedService {
	
	public Page<Pubmed> getPubmedArticles(int page);
	public Page<Pubmed> searchArticlesByPmid(String searchTerm,int page);
	public Page<PubmedArticles> find(Pageable pageable);
	public Page<PubmedArticles> getArticlesBySearch(String searchKeyword,Pageable pageable);
	public Page<Pubmed> getCardiologyArticles(Pageable pageable);
	
	public Page<PubmedUmlsPhraseQuery> getUmlsPhraseArticles(Pageable pageable);
	public Page<PubmedExcelPhraseQuery> getExcelPhraseArticles(Pageable pageable);
	
	public Page<PubmedUmlsPhraseQuery> searchUmlsPhraseArticles(String searchKeyword,Pageable pageable);
	public Page<PubmedExcelPhraseQuery> searchExcelPhraseArticles(String searchKeyword,Pageable pageable);
	
	
	public Page<Pubmed> getArticlesByKeyword(String searchKeyword,Pageable pageable);
	public Page<PubmedUmlsKeywords> getUmlsArticlesByKeyword(Pageable pageable);
	public Page<PubmedUmlsKeywords> searchByUmlKeyword(String searchKeyword,Pageable pageable);
	

}
