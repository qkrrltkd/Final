package com.gdu.pupo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface CartService {
  
  public void addCart(HttpServletRequest request, Model model);
  public void getCartList(HttpServletRequest request, Model model);
  public void removeCart(HttpServletRequest request, HttpServletResponse response);
  
  
	
	
	/*
	 * public Map<String, Object> addCart(HttpSession session, CartDTO cartDTO);
	 * 
	 * public List<CartDTO> getCartById(HttpSession session, String id);
	 * 
	 * public int deleteCart(HttpSession session, int cartId);
	 * 
	 */
}