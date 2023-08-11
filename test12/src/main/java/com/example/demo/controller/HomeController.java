package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.USER;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor//레포지토리 가져다 쓸 때 필요, getter, setter 안 선언해도 됨
@Controller
public class HomeController {
	private final UserRepository userRepository;
	
	//show
	@GetMapping("/home")
	public String goshow(Model model) {
		List<USER> users = userRepository.findAll();
        model.addAttribute("userList", users);
        System.out.println(users);
        return "content/home";
	}
	
	@GetMapping("/add")
	public String addUser(Model model) {
        model.addAttribute("user", new USER());
        return "content/add";
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute USER user) {
	    userRepository.save(user);
	    return "redirect:/home"; // Redirect to the user list page after creating
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable String id) {
	    userRepository.deleteById(id);
	    return "redirect:/home"; // Redirect to the user list page after deleting
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable String id, Model model) {
	    USER user = userRepository.findById(id).orElse(null);
	    model.addAttribute("user", user);
	    return "content/edit"; // Create an edit form template
	}

	@PostMapping("/updateUser/{id}")
	public String updateUser(@PathVariable String id, @ModelAttribute USER updatedUser) {
	    userRepository.findById(id).ifPresent(user -> {
	    	user.setName(updatedUser.getName());
	        user.setPassword(updatedUser.getPassword());
	        // Set other properties as needed
	        userRepository.save(user);
	    });
	    return "redirect:/home"; // Redirect to the user list page after updating
	}


	
}