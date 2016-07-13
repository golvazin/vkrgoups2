package vkgroups2.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionHandler implements ErrorController {
    


    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String message = "Внутренняя ошибка сервера";
        if (throwable != null) {
            message = throwable.getMessage();
        }
        String url = "";
        HttpSession session = request.getSession(true);
        OAuth2ClientContext oAuth2ClientContext = (OAuth2ClientContext) session.getAttribute("scopedTarget.oauth2ClientContext");
        if (oAuth2ClientContext == null) {
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
