package com.gdu.pupo.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyDTO {
  
	private String buyId;
	private String id;
	private int buyPrice;
	private LocalDateTime buyDate;
	
	List<BuyDetailDTO> itemBuyDetailList;
}