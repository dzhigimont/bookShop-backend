package com.zhigimont.bookstore.resource;

import com.zhigimont.bookstore.domain.Book;
import com.zhigimont.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookResource {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Book addBook(@RequestBody Book book){
        return bookService.save(book);
    }

    @RequestMapping(value = "/add/image", method = RequestMethod.POST)
    public ResponseEntity upload(
            @RequestParam("id") Long id,
            HttpServletResponse response, HttpServletRequest request
    ){
        try {
            Book book = bookService.findOne(id);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> it = multipartRequest.getFileNames();
            if (it.hasNext()){
                MultipartFile file = multipartRequest.getFile(it.next());
                String fileName = id+".png";
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/book/" + fileName)));
                stream.write(bytes);
                stream.close();
                return new ResponseEntity("Upload Success!", HttpStatus.OK);
            }
            else {
                return new ResponseEntity("Upload failed", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("Upload failed", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/bookList", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBookList(){
        return bookService.findAll();
    }
    @RequestMapping(value = "/searchBook", method = RequestMethod.POST)
    public List<Book> searchBookByTitle(@RequestBody String keyword){
        return bookService.blurrySearch(keyword);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Book getBook(@PathVariable Long id){
        return bookService.findOne(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@RequestBody Book book){

        return bookService.save(book);
    }



    @RequestMapping(value = "/update/image", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateImage(
            @RequestParam("id") Long id,
            HttpServletResponse response, HttpServletRequest request
    ){
        try {
            Book book = bookService.findOne(id);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator<String> it = multipartRequest.getFileNames();
            MultipartFile file = multipartRequest.getFile(it.next());
            String fileName = id+".png";

            Path path = Paths.get("src/main/resources/static/image/book/" + fileName);
            if (path==null){
                Files.delete(path);
            }else {
                System.out.println(fileName + " this file don't exist");
            }
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/book/" + fileName)));
            stream.write(bytes);
            stream.close();
            return new ResponseEntity("Upload Success!", HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("Upload failed", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void   deleteBook(@RequestBody Long id) throws IOException {
        bookService.removeOne(id);
            String fileName = id+".png";
            Path path = Paths.get("src/main/resources/static/image/book/" + fileName);
            if (path==null){
                Files.delete(path);
            }else {
                System.out.println(fileName + " this file doesn't exist");
            }
    }



}
