package com.gdu.pupo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.pupo.domain.BuyDTO;
import com.gdu.pupo.domain.CartDTO;
import com.gdu.pupo.domain.CategoryDTO;
import com.gdu.pupo.domain.ItemDTO;
import com.gdu.pupo.domain.ItemImgDTO;
import com.gdu.pupo.domain.ItemImgDetailDTO;

@Mapper
public interface ItemMapper {
  
  public List<ItemDTO> getItemsByCategoryId(int categoryId);          // 카테고리별 리스트
  public List<ItemDTO> getAllItems(Map<String, Object> map);          // 상품 리스트
  public int getItemCount();                                          // 총 상품 갯수
  public List<ItemImgDTO> itemImgList();                              // 상품 이미지 리스트
  public List<ItemImgDetailDTO> itemImgDetailList();                  // 상품 상세 이미지 리스트
  public int insertItem(ItemDTO itemDTO);                             // 상품 등록
  public int insertImg(ItemImgDTO itemImgDTO);                        // 이미지 등록
  public int insertDetailImg(ItemImgDetailDTO itemImgDetailDTO);      // 상세 이미지 등록
  public ItemDTO getItem(int itemId);                                 // 상품 상세보기
  public ItemImgDTO getImg(int itemId);                               // 이미지 보기
  public ItemImgDetailDTO getDetailImg(int itemId);                   // 상세 이미지 보기
  public int updateItem(ItemDTO itemDTO);                             // 상품 수정
  public int editItemImg(ItemImgDTO itemImgDTO);                      // 이미지 수정
  public int editItemImgDetail(ItemImgDetailDTO itemImgDetailDTO);    // 디테일 이미지 수정
  public int deleteItem(int itemId);                                  // 상품 삭제
  public List<CategoryDTO> getCateList();                             // 카테고리 목록 조회
  public int setCategory(String cateName);                            // 카테고리 등록
  public int checkCateName(String cateName);                          // 카테고리 중복 조회
  public void deleteCate(String cateCode);                            // 카테고리 삭제
  public int addCart(CartDTO cartDTO);                                // 장바구니 등록
  public List<CartDTO> getCartList(String id);                        // 장바구니 조회
  public void updateCart(CartDTO cartDTO);                            // 장바구니 수량 수정
  public void deleteCart(String cartId);                              // 장바구니 삭제
  public void deleteCarts(CartDTO cartDTO);                           // 장바구니 여러 개 삭제
  public void itemBuy(BuyDTO buyDTO);                                 // 구매 정보 등록
  public void itemBuyDetail(BuyDTO buyDTO);                           // 구매 상세 정보 등록
  public String getBuyId();                                           // 구매 아이디 조회
}