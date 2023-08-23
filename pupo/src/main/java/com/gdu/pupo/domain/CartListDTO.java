package com.gdu.pupo.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartListDTO {
  private int cartListNo;
  private String cartCode;
  private int itemNo;
  private int itemCount;
}
