package org.ying.book.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/books")
public class BookController {
    @RequestMapping(method = RequestMethod.GET)
    public String getBooks(@RequestParam("id") Integer id) {
        return id.toString();
    }

}
