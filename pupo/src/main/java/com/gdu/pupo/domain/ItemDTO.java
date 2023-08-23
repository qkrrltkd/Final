package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
  
  private int itemId;
  private String itemName;
  private int price;
  private String description;
  private String cateCode;
  private int stock;
  
  private CategoryDTO categoryDTO;
  
}