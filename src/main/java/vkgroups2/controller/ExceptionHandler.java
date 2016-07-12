package vkgroups2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionHandler implements ErrorController {
    


    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String message = "Внутренняя ошибка сервера";
        if (throwable != null) {
            message = throwable.getMessage();
        }
        String url = "";
        if (auth == null || !auth.isAuthenticated() || "unknown".equals(auth.getName())) {
            url = "/";
        } else {
            url = "/dashboard";
        }
        
        model.addAttribute("title", "Произошла ошибка!")
        .addAttribute("error", message)
            .addAttribute("url", url);
        return "error";
    }
    
    
    @Override
    public String getErrorPath() {
        return "error";
    }

}
