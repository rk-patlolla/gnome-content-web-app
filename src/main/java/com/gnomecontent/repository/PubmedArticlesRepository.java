package com.gnomecontent.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.gnomecontent.documents.PubmedArticles;

public interface PubmedArticlesRepository extends ElasticsearchRepository<PubmedArticles, String> {

}
