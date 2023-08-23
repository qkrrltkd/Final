package com.gdu.pupo.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.pupo.domain.CartDTO;
import com.gdu.pupo.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/item")
@Controller
public class ItemController {

    private final ItemService itemService;

    // 스토어 메인
    @GetMapping("/storeList.html")
    public String storeList(HttpServletRequest request, Model model) {
        itemService.getAllItems(request, model);
        return "item/storeList";
    }

    @GetMapping("/storeDetail.html")
    public String storeDetail(int itemId, Model model) {
        itemService.getItem(itemId, model);
        return "item/storeDetail";
    }

    // 판매자 페이지
    @GetMapping("/itemManage.html")
    public String Manage(Model model) {
        return "item/itemManage";
    }

    // 카테고리 관리 페이지
    @GetMapping("/itemCategory.html")
    public String itemCategory(Model model) {
        return "item/itemCategory";
    }

    // 상품 등록 페이지
    @GetMapping("/itemRegister.html")
    public String itemRegist(Model model) {
        model.addAttribute("cateList", itemService.getCateList());
        return "item/itemRegister";
    }

    // 카테고리 목록 조회
    @GetMapping("/cateManage.html")
    public String cateManage(Model model) {
        model.addAttribute("cateList", itemService.getCateList());
        return "item/itemCategory.html";
    }

    // 카테고리 등록
    @PostMapping("/goodCategory.do")
    public int goodCategory(@RequestParam("cateName") String cateName) {
        return itemService.setCategory(cateName);
    }

    // 카테고리 중복 체크
    @PostMapping("/checkCateName")
    public int checkCateName(String cateName) {
        return itemService.checkCateName(cateName);
    }

    // 카테고리 삭제
    @GetMapping("/deleteCategory")
    public String delete(String cateCode) {
        itemService.deleteCate(cateCode);
        return "redirect:/item/cateManage.html";
    }

    // 장바구니 등록
    @ResponseBody
    @PostMapping("/addCart.do")
    public void addCart(HttpServletRequest request) {
        itemService.addCart(request);
    }

    // 장바구니 이동
    @GetMapping("/cartList.html")
    public String cartList(HttpServletRequest request, Model model) {
        return itemService.cartList(request, model);
    }

    // 장바구니 수량 수정
    @PostMapping("/updateQuantity")
    public String cartUpdate(CartDTO cartDTO) {
        return itemService.cartUpdate(cartDTO);
    }

    // 장바구니 삭제
    @PostMapping("/deleteCart")
    public String deleteCart(@RequestParam("cartId") String cartId) {
        return itemService.deleteCart(cartId);
    }

    // 장바구니 선택삭제
    @PostMapping("/deleteCarts")
    public String deleteCarts(@RequestBody List<String> cartIds) {
        return itemService.deleteCarts(cartIds);
    }

    // 장바구니 선택구매
    @PostMapping("/itemBuys")
    public String itemBuys(@RequestBody HashMap<String, Object> map, HttpServletRequest request) {
        return itemService.itemBuys(map, request);
    }

    // 상품 리스트 페이지
    @GetMapping("/itemList.html")
    public String itemList(HttpServletRequest request, Model model) {
        model.addAttribute("cateList", itemService.getCateList());
        itemService.getAllItems(request, model);
        /*
        List<ItemDTO> itemList = itemService.getAllItems();
        model.addAttribute("itemList", itemList);
        */
        return "item/itemList";
    }

    // 상품 수정 페이지
    @GetMapping("/itemModify.html")
    public String modify(int itemId, Model model) {
        itemService.getItem(itemId, model);
        model.addAttribute("cateList", itemService.getCateList());
        return "item/itemModify";
    }

    // 상품 등록
    @PostMapping("/itemRegister.do")
    public String insertItem(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {
        int registerResult = itemService.insertItem(multipartRequest);
        redirectAttributes.addFlashAttribute("registerResult", registerResult);
        return "redirect:/item/itemList.html";
    }

    // 상품 상세
    @GetMapping("/itemDetail.html")
    public String itemDetail(int itemId, Model model) {
        itemService.getItem(itemId, model);
        return "item/itemDetail";
    }

    // 상품 이미지
    @GetMapping("/itemImgDisplay.do")
    public ResponseEntity<byte[]> itemDisplay(@RequestParam("itemId") int itemId){
        return itemService.itemImgDisplay(itemId);
    }

    // 상품 상세 이미지
    @GetMapping("/itemImgDetailDisplay.do")
    public ResponseEntity<byte[]> itemDetailDisplay(@RequestParam("itemId") int itemId){
        return itemService.itemImgDetailDisplay(itemId);
    }

    // 상품 수정
    @PostMapping("/itemModify.do")
    public String itemModify(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttribute) {
        int updateResult = itemService.itemUpdate(multipartRequest);
        redirectAttribute.addFlashAttribute("updateResult", updateResult);
        return "redirect:/item/itemDetail.html?itemId=" + multipartRequest.getParameter("itemId");
    }

    // 상품 삭제
    @PostMapping("/itemRemove.do")
    public String remove(int itemId, RedirectAttributes redirectAttribute) {
        int deleteResult = itemService.itemDelete(itemId);
        redirectAttribute.addFlashAttribute("deleteResult", deleteResult);
        return "redirect:/item/itemList.html";
    }

}