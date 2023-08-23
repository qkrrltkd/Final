package com.gdu.pupo.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
  
//  private int categoryid;
//  private String categoryName;
	private String cateCode;
	private String cateName;
	private int orderNum;
	private String isUse;
}
