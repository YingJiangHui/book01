package org.ying.book.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.ying.book.interceptor.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final SystemAdminInterceptor systemAdminInterceptor;
    private final LibraryAdminInterceptor libraryAdminInterceptor;
    private final ReaderInterceptor readerInterceptor;
    private final RegisterUserContextInterceptor registerUserContextInterceptor;

    @Autowired
    public WebMvcConfig(AuthInterceptor authInterceptor, SystemAdminInterceptor systemAdminInterceptor, LibraryAdminInterceptor libraryAdminInterceptor, ReaderInterceptor readerInterceptor,RegisterUserContextInterceptor registerUserContextInterceptor) {
        this.authInterceptor = authInterceptor;
        this.systemAdminInterceptor = systemAdminInterceptor;
        this.libraryAdminInterceptor = libraryAdminInterceptor;
        this.readerInterceptor = readerInterceptor;
        this.registerUserContextInterceptor = registerUserContextInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("addInterceptors");

        registry.addInterceptor(authInterceptor).addPathPatterns("/search-history").addPathPatterns("/books/borrowing").addPathPatterns("/book-shelf").addPathPatterns("/books/reservation").addPathPatterns("/users/**");
        registry.addInterceptor(registerUserContextInterceptor);
//        registry.addInterceptor(systemAdminInterceptor).addPathPatterns("/users");
//        registry.addInterceptor(libraryAdminInterceptor).addPathPatterns("/books");
//        registry.addInterceptor(readerInterceptor).excludePathPatterns("/users");
    }
}
