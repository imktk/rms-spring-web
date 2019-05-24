package com.mitrais.rms.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.form.UserForm;
import com.mitrais.rms.service.UserService;

@Controller
public class PageController {

	@Autowired
	private UserService userService;
	
	static final String LOGIN_ERROR = "Cannot login, Please verify your account information!";
	
    @GetMapping(value = "/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
       return new ModelAndView("/index");
    }
    
    @GetMapping(value = "/register")
    public ModelAndView registerPage() {
    	ModelAndView mav =  new ModelAndView("/registerPage");
    	mav.addObject("registerForm", new UserForm());
    	return mav;
    }
    
    @GetMapping(value = "/updateUser/{id}")
    public ModelAndView updatePage(@PathVariable Long id) {
    	ModelAndView mav =  new ModelAndView("/updatePage");
    	User findById = userService.findById(id);
    	mav.addObject("updateForm", new UserForm(findById));
    	return mav;
    }
    
    @GetMapping(value = "/login")
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response, String error) {
    	ModelAndView mav = null;
    	Principal userPrincipal = request.getUserPrincipal();
    	if(userPrincipal == null)
    		mav = new ModelAndView("/loginPage.html");
    	else 
    		mav = new ModelAndView("redirect:/");
    	if (error != null)
    		mav.addObject("message", LOGIN_ERROR);
       return mav;
    }
}
