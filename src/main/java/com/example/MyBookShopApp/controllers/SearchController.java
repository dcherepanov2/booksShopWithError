package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
@Scope("request")
public class SearchController {

    private int limit = 0;

    private final BookService bookService;

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{name}")
    public String searchBooks(@PathVariable("name")String name, Model model){
        List<Book> books = bookService.getSearchQuery(name, limit);
        limit += 20;
        System.out.println(limit);
        model.addAttribute("booksBySearch",bookService.getSearchQuery(name, limit));
        return "/search/index";
    }
}
