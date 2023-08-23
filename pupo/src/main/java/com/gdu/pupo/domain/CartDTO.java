package com.gdu.pupo.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
  
//  private int cartId;
//  private String id;
//  private LocalDate createdAt;
//  private List<CartItemDTO> cartList;
	
	private String cartId;
	private String id;
	private int itemId;
	private int quantity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
	private int totalPrice;
	private ItemDTO itemDTO;
	
	private List<String> cartIdList;
}