package com.javatpoint.springbootcrud.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.javatpoint.springbootcrud.model.News;

public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findByTitleContaining(String title);
    List<News> findByPublished(boolean published);
}