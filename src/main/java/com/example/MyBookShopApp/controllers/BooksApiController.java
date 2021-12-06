package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.dto.RecommendedBooksDto;
import com.example.MyBookShopApp.exception.RecentBookException;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.MediaTypeUtils;
import com.example.MyBookShopApp.service.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import java.io.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000/")
public class BooksApiController {

    @Value("${download.path}")
    private String PATH_DOWNLOAD;

    private final BookService bookService;
    private final ServletContext servlet;
    private final ResourceStorage resourceStorage;

    @Autowired
    public BooksApiController(BookService bookService, ServletContext servletContext, ResourceStorage resourceStorage) {
        this.bookService = bookService;
        this.servlet = servletContext;
        this.resourceStorage = resourceStorage;
    }

    @GetMapping("/recent")
    @ResponseBody
    public RecommendedBooksDto getFilterBooksRecent(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam(value = "to") @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
            @RequestParam(value = "offset") Integer offset,
            @RequestParam(value = "limit") Integer limit
    ) throws RecentBookException {
        if(to.before(from))
            throw new RecentBookException();
        List<Book> books = bookService.getFilterBooksByDate(from,to,offset,limit);
        return new RecommendedBooksDto(books);
    }

    @GetMapping("/tag/{slug}")
    public List<Book> getAllBooksByTag(
            @PathVariable("slug")String slug,
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit){
        return bookService.getAllBooksByTag(slug, offset, limit);
    }

    @GetMapping("/download/{slug}")
    public void downloadFile(
            HttpServletResponse response,
            @PathVariable("slug") String slug,
            @RequestParam("type")Integer type
    ) throws IOException
    {
        String fileName = resourceStorage.findBookDownloadFile(slug,type);
        if(fileName != null){
            MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servlet, fileName);
            File file = new File( PATH_DOWNLOAD+ "/" + fileName);
            // Content-Type
            // application/pdf
            response.setContentType(mediaType.getType());

            // Content-Disposition
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

            // Content-Length
            response.setContentLength((int) file.length());

            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            inStream.close();
        }
    }
}
