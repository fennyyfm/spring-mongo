package com.javatpoint.springbootcrud.controller;

import com.javatpoint.springbootcrud.model.News;
import com.javatpoint.springbootcrud.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/news")
    public ResponseEntity<List<News>> getAllNews(@RequestParam(required = false) String title) {
        try {
            List<News> news;
            news = new ArrayList<News>();

            if (title == null)
                newsRepository.findAll().forEach(news::add);
            else
                newsRepository.findByTitleContaining(title).forEach(news::add);

            if (news.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(news, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable("id") String id) {
        Optional<News> newsData = newsRepository.findById(id);

        if (newsData.isPresent()) {
            return new ResponseEntity<>(newsData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        try {
            News _news = newsRepository.save(new News(news.getTitle(), news.getDescription(), false));
            return new ResponseEntity<>(_news, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable("id") String id, @RequestBody News news) {
        Optional<News> newsData = newsRepository.findById(id);

        if (newsData.isPresent()) {
            News _news = newsData.get();
            _news.setTitle(news.getTitle());
            _news.setDescription(news.getDescription());
            _news.setPublished(news.isPublished());
            return new ResponseEntity<>(newsRepository.save(_news), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable("id") String id) {
        try {
            newsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/news")
    public ResponseEntity<HttpStatus> deleteAllNews() {
        try {
            newsRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/published")
    public ResponseEntity<List<News>> findByPublished() {
        try {
            List<News> newss = newsRepository.findByPublished(true);

            if (newss.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(newss, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}