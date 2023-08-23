package com.gdu.pupo.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.pupo.domain.CartDTO;
import com.gdu.pupo.domain.CategoryDTO;
import com.gdu.pupo.domain.ItemDTO;

public interface ItemService {
  
    public List<ItemDTO> getItemsByCategoryId(int categoryId); 
    public void getAllItems(HttpServletRequest request, Model model);         // 상품 목록
    public int insertItem(MultipartHttpServletRequest multipartRequest);      // 상품 등록
    public ResponseEntity<byte[]> itemImgDisplay(int itemId);                 // 상품 이미지 출력
    public ResponseEntity<byte[]> itemImgDetailDisplay(int itemId);           // 상품 이미지 상세 출력
    public void getItem(int itemId, Model model);                             // 상품 상세보기
    public int itemUpdate(MultipartHttpServletRequest multipartRequest);      // 상품 수정
    public int itemDelete(int itemId);                                        // 상품 삭제
    public List<CategoryDTO> getCateList();                                   // 카테고리 목록 조회
    public int setCategory(String cateName);                                  // 카테고리 등록
    public int checkCateName(String cateName);                                // 카테고리 중복 체크
    public void deleteCate(String cateCode);                                  // 카테고리 삭제
    // public List<ItemDTO> selectItem();                                     // 아이템 조회
    public void addCart(HttpServletRequest request);                                    // 장바구니 등록
    public String cartList(HttpServletRequest request, Model model);                              // 장바구니 조회
    public String cartUpdate(CartDTO cartDTO);                                  // 장바구니 수량 수정
    public String deleteCart(String cartId);                                    // 장바구니 삭제
    public String deleteCarts(List<String> cartIds);                                 // 장바구니 선택 삭제
    public String itemBuys(HashMap<String, Object> map, HttpServletRequest request);                                       // 장바구니 선택 구매
    public String getBuyId();                                                 // 구매자 아이디 반환
}