package com.mitrais.rms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mitrais.rms.entity.User;
import com.mitrais.rms.form.UserForm;
import com.mitrais.rms.service.UserService;

@Controller
public class MemberController {
	
	static final Logger logger = Logger.getLogger(MemberController.class);

	static final String DELETE_SUCC_MESS = "The User Successfully Deleted!";
	static final String DELETE_ERR_MESS = "Failed to Delete User!";
	static final String UPDATE_SUCC_MESS = "The User Successfully Updated!";
	static final String UPDATE_ERR_MESS = "Failed to Update User!";
	static final String REGISTER_SUCC_MESS = "The User Successfully Registered!";
	static final String REGISTER_ERR_MESS = "Failed to Register User!";
	
	@Autowired
	private UserService userService;
	
    @GetMapping(value = "/allUser")
    public ModelAndView infoPage(@RequestParam(value = "search", required = false) String query, Pageable pageable) {
    	ModelAndView mav = new ModelAndView("/allUser");
    	Page<User> allUser = null;
    	try {
    		if(StringUtils.isEmpty(query))
    			allUser = userService.getAllUser(pageable);
    		else 
    			allUser = userService.searchByUsername(query, pageable);
    		mav.addObject("listUser", allUser);
		} catch (Exception e) {
			logger.error(e); 
		}
        return mav;
    }
    
    @GetMapping(value = "/userInfo")
    public ModelAndView userInfoPage(HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView mav = new ModelAndView("/userInfo");
    	String username = request.getUserPrincipal().getName();
    	User user = userService.findUserAccount(username);
    	if(user != null)
    		mav.addObject("user", user);
    	return mav;
    }
    
    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ModelAndView("redirect:/");
    }
    
    @PostMapping("/createUser")
    public ModelAndView createUser(@ModelAttribute("registerForm") UserForm userForm, RedirectAttributes redirAttrs) {
    	ModelAndView mav = new ModelAndView("/registerPage");
    	try {
    		boolean addUser = userService.createUser(userForm);
    		if(addUser) {
    			mav = new ModelAndView("redirect:/register");
        		redirAttrs.addFlashAttribute("message", REGISTER_SUCC_MESS);
    		} else {
    			mav.addObject("registerForm", userForm);
    			mav.addObject("message", REGISTER_ERR_MESS);
    		}
		} catch (Exception e) {
			logger.error(e); 
			mav.addObject("registerForm", userForm);
			mav.addObject("message",  returnExceptionMessage(e, REGISTER_ERR_MESS));
		}
    	return mav;
    }
    
    @PostMapping("/updateUser")
    public ModelAndView updateUser(@ModelAttribute("registerForm") UserForm userForm, RedirectAttributes redirAttrs) {
    	ModelAndView mav =  new ModelAndView("redirect:/updateUser/" + userForm.getId());
    	try {
    		boolean addUser = userService.updateUser(userForm.convertToUser());
    		if(addUser) {
        		redirAttrs.addFlashAttribute("message", UPDATE_SUCC_MESS);
    		}else {
        		redirAttrs.addFlashAttribute("message", UPDATE_ERR_MESS);
    		}
		} catch (Exception e) {
			logger.error(e); 
    		redirAttrs.addFlashAttribute("message", returnExceptionMessage(e, UPDATE_ERR_MESS));
		}
    	return mav;
    }

    @GetMapping(value = "/deleteUser/{id}")
    public ModelAndView deleteUserById(@PathVariable Long id, RedirectAttributes redirAttrs) {
    	ModelAndView mav = null;
    	try {
			mav = new ModelAndView("redirect:/allUser");
			userService.deleteUser(id);
			redirAttrs.addFlashAttribute("message", DELETE_SUCC_MESS);
		} catch (Exception e) {
			logger.error(e); 
			redirAttrs.addFlashAttribute("message", DELETE_ERR_MESS);
		}
    	return mav;
    }
    
    private String returnExceptionMessage(Exception e, String defaulMessage)
    {
    	String errorMessage = defaulMessage;
    	if("001".equals(e.getMessage()))
    		errorMessage = "Username is empty!";
    	if("002".equals(e.getMessage()))
    		errorMessage = "Password is empty!";
    	if("003".equals(e.getMessage()))
    		errorMessage = "Confirm password is empty!";
    	if("004".equals(e.getMessage()))
    		errorMessage = "Confirm password must be equal to password!";
    	if("005".equals(e.getMessage()))
    		errorMessage = "Username is already used!";
    	return errorMessage;
    }
}
