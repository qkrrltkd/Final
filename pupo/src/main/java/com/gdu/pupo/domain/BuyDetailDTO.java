package com.gdu.pupo.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyDetailDTO {
  
	private String buyDetailId;
	private int itemId;
	private int buyQuantity;
	private int detailBuyPrice;
	private String buyId;
}