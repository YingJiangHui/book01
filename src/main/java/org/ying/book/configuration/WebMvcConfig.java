package org.ying.book.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ying.book.interceptor.AuthInterceptor;
import org.ying.book.interceptor.LibraryAdminInterceptor;
import org.ying.book.interceptor.ReaderInterceptor;
import org.ying.book.interceptor.SystemAdminInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final SystemAdminInterceptor systemAdminInterceptor;
    private final LibraryAdminInterceptor libraryAdminInterceptor;
    private final ReaderInterceptor readerInterceptor;

    @Autowired
    public WebMvcConfig(AuthInterceptor authInterceptor, SystemAdminInterceptor systemAdminInterceptor, LibraryAdminInterceptor libraryAdminInterceptor, ReaderInterceptor readerInterceptor) {
        this.authInterceptor = authInterceptor;
        this.systemAdminInterceptor = systemAdminInterceptor;
        this.libraryAdminInterceptor = libraryAdminInterceptor;
        this.readerInterceptor = readerInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("addInterceptors");
        registry.addInterceptor(authInterceptor).addPathPatterns("/books/borrowing").addPathPatterns("/book-shelf").addPathPatterns("/books/reservation").addPathPatterns("/users/**");
        registry.addInterceptor(systemAdminInterceptor).addPathPatterns("/users");
//        registry.addInterceptor(libraryAdminInterceptor).addPathPatterns("/books");
//        registry.addInterceptor(readerInterceptor).addPathPatterns("/books/**");
    }
}
