package com.gdu.pupo.service;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.pupo.domain.BuyDTO;
import com.gdu.pupo.domain.BuyDetailDTO;
import com.gdu.pupo.domain.CartDTO;
import com.gdu.pupo.domain.CategoryDTO;
import com.gdu.pupo.domain.ItemDTO;
import com.gdu.pupo.domain.ItemImgDTO;
import com.gdu.pupo.domain.ItemImgDetailDTO;
import com.gdu.pupo.mapper.ItemMapper;
import com.gdu.pupo.util.MyFileUtil;
import com.gdu.pupo.util.PageUtil;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;


@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
  
  private final ItemMapper itemMapper;
  private final MyFileUtil myFileUtil;
  private final PageUtil pageUtil;
  
  // 상품 조회
  @Override
  public void getAllItems(HttpServletRequest request, Model model) {
    Optional<String> opt1 = Optional.ofNullable(request.getParameter("cateCode"));
    String cateCode = opt1.orElse("0");
    
    Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
    String query = opt2.orElse("");
    
    Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt3.orElse("1"));
    
    Map<String, Object> map = new HashMap<>();
    map.put("query", query);
    map.put("cateCode", cateCode);
    int totalRecord = itemMapper.getItemCount();
    
    int recordPerPage = 10;
    
    int totalPage = (int) Math.ceil((double) totalRecord / recordPerPage);
    if ((page - 1) * recordPerPage >= totalRecord) {
      page = Math.max(totalPage, 1);
    }
    
    pageUtil.setPageUtil(page, totalRecord, recordPerPage);
    
    int begin = pageUtil.getBegin();
    
    map.put("begin", begin);
    map.put("recordPerPage", recordPerPage);
    
    List<ItemDTO> itemList = itemMapper.getAllItems(map);
    List<ItemImgDTO> itemImgList = itemMapper.itemImgList();
    
    model.addAttribute("itemList", itemList);
    model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/item/itemList.html?query=" + query + "&cateCode=" + cateCode));
    model.addAttribute("itemImgList", itemImgList);
  }
  
  // 카테고리 조회
  @Override
  public List<ItemDTO> getItemsByCategoryId(int categoryId) {
    return itemMapper.getItemsByCategoryId(categoryId);
  }
  
  // 상품 등록
  @Transactional
  @Override
  public int insertItem(MultipartHttpServletRequest multipartRequest) {
    String itemName = multipartRequest.getParameter("itemName");
    String description = multipartRequest.getParameter("description");
    int price = Integer.parseInt(multipartRequest.getParameter("price"));
    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
    String cateCode = multipartRequest.getParameter("cateCode");
    
    ItemDTO itemDTO = new ItemDTO();
    itemDTO.setItemName(itemName);
    itemDTO.setDescription(description);
    itemDTO.setPrice(price);
    itemDTO.setStock(stock);
    itemDTO.setCateCode(cateCode);
    
    int registerResult = itemMapper.insertItem(itemDTO);
    
    List<MultipartFile> files = multipartRequest.getFiles("files");
    for (MultipartFile multipartFile : files) {
      if (multipartFile != null && !multipartFile.isEmpty()) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if (!dir.exists()) {
            dir.mkdirs();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          if (hasThumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
          }
          ItemImgDTO itemImgDTO = new ItemImgDTO();
          itemImgDTO.setPath(path);
          itemImgDTO.setOriginName(originName);
          itemImgDTO.setFilesystemName(filesystemName);
          itemImgDTO.setHasThumbnail(registerResult);
          itemImgDTO.setItemId(itemDTO.getItemId());
          itemMapper.insertImg(itemImgDTO);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    
    List<MultipartFile> detailFiles = multipartRequest.getFiles("detailFiles");
    for (MultipartFile multipartFile : detailFiles) {
      if (multipartFile != null && !multipartFile.isEmpty()) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if (!dir.exists()) {
            dir.mkdirs();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          if (hasThumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
          }
          ItemImgDetailDTO itemImgDetailDTO = new ItemImgDetailDTO();
          itemImgDetailDTO.setPathDetail(path);
          itemImgDetailDTO.setOriginDetailName(originName);
          itemImgDetailDTO.setFilesystemDetailName(filesystemName);
          itemImgDetailDTO.setHasDetailThumbnail(registerResult);
          itemImgDetailDTO.setItemId(itemDTO.getItemId());
          itemMapper.insertDetailImg(itemImgDetailDTO);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    
    return registerResult;
  }
  
  public void getItem(int itemId, Model model) {
    model.addAttribute("item", itemMapper.getItem(itemId));
    model.addAttribute("img", itemMapper.getImg(itemId));
    model.addAttribute("imgDetail", itemMapper.getDetailImg(itemId));
     
  }
  
  // 상품 수정
  @Override
  public int itemUpdate(MultipartHttpServletRequest multipartRequest) {
    int itemId = Integer.parseInt(multipartRequest.getParameter("itemId"));
    String itemName = multipartRequest.getParameter("itemName");
    String description = multipartRequest.getParameter("description");
    int price = Integer.parseInt(multipartRequest.getParameter("price"));
    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
    String cateCode = multipartRequest.getParameter("cateCode");
    
    ItemDTO itemDTO = new ItemDTO();
    itemDTO.setItemId(itemId);
    itemDTO.setItemName(itemName);
    itemDTO.setDescription(description);
    itemDTO.setPrice(price);
    itemDTO.setStock(stock);
    itemDTO.setCateCode(cateCode);
    
    int updateResult = itemMapper.updateItem(itemDTO);
    
    List<MultipartFile> files = multipartRequest.getFiles("files");
    for (MultipartFile multipartFile : files) {
      if (multipartFile != null && !multipartFile.isEmpty()) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if (!dir.exists()) {
            dir.mkdirs();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          if (hasThumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
          }
          ItemImgDTO itemImgDTO = new ItemImgDTO();
          itemImgDTO.setPath(path);
          itemImgDTO.setOriginName(originName);
          itemImgDTO.setFilesystemName(filesystemName);
          itemImgDTO.setHasThumbnail(updateResult);
          itemImgDTO.setItemId(itemDTO.getItemId());
          itemMapper.insertImg(itemImgDTO);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    
    List<MultipartFile> detailFiles = multipartRequest.getFiles("detailFiles");
    for (MultipartFile multipartFile : detailFiles) {
      if (multipartFile != null && !multipartFile.isEmpty()) {
        try {
          String path = myFileUtil.getPath();
          File dir = new File(path);
          if (!dir.exists()) {
            dir.mkdirs();
          }
          String originName = multipartFile.getOriginalFilename();
          originName = originName.substring(originName.lastIndexOf("\\") + 1);
          String filesystemName = myFileUtil.getFilesystemName(originName);
          File file = new File(dir, filesystemName);
          multipartFile.transferTo(file);
          String contentType = Files.probeContentType(file.toPath());
          boolean hasThumbnail = contentType != null && contentType.startsWith("image");
          if (hasThumbnail) {
            File thumbnail = new File(dir, "s_" + filesystemName);
            Thumbnails.of(file).size(50, 50).toFile(thumbnail);
          }
          ItemImgDetailDTO itemImgDetailDTO = new ItemImgDetailDTO();
          itemImgDetailDTO.setPathDetail(path);
          itemImgDetailDTO.setOriginDetailName(originName);
          itemImgDetailDTO.setFilesystemDetailName(filesystemName);
          itemImgDetailDTO.setHasDetailThumbnail(updateResult);
          itemImgDetailDTO.setItemId(itemDTO.getItemId());
          itemMapper.insertDetailImg(itemImgDetailDTO);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return updateResult;
  }
  
  // 상품 삭제
  @Override
  public int itemDelete(int itemId) {
    return itemMapper.deleteItem(itemId);
  }
  
  // 이미지
  @Override
  public ResponseEntity<byte[]> itemImgDisplay(int itemId) {
    ItemImgDTO itemImgDTO = itemMapper.getImg(itemId);
    ResponseEntity<byte[]> image = null;
    
    try {
      File thumbnail = new File(itemImgDTO.getPath(), itemImgDTO.getFilesystemName());
      image = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnail), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
  
  // 상세 이미지 
  @Override
  public ResponseEntity<byte[]> itemImgDetailDisplay(int itemId) {
    ItemImgDetailDTO itemImgDetailDTO = itemMapper.getDetailImg(itemId);
    ResponseEntity<byte[]> image = null;
    try {
      File thumbnail = new File(itemImgDetailDTO.getPathDetail(), itemImgDetailDTO.getFilesystemDetailName());
      image = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnail), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
  
  // 카테고리 조회
  @Override
  public List<CategoryDTO> getCateList() {
    return itemMapper.getCateList();
  }
  
  // 카테고리 등록
  @Override
  public int setCategory(String cateName) {
    return itemMapper.setCategory(cateName);
  }
  
  // 카테고리 이름 체크
  @Override
  public int checkCateName(String cateName) {
    return itemMapper.checkCateName(cateName);
  }
  
  // 카테고리 삭제
  @Override
  public void deleteCate(String cateCode) {
    itemMapper.deleteCate(cateCode);
  }
  
  // 장바구니 추가
  @Override
  public void addCart(HttpServletRequest request) {
      HttpSession session = request.getSession();
      String id = (String) session.getAttribute("loginId");
      CartDTO cartDTO = new CartDTO();
      cartDTO.setId(id);
      cartDTO.setItemId(Integer.parseInt(request.getParameter("itemId")));
      cartDTO.setQuantity(Integer.parseInt(request.getParameter("quantity")));
      itemMapper.addCart(cartDTO);
  }
  
  // 장바구니 조회
  @Override
  public String cartList(HttpServletRequest request, Model model) {
      HttpSession session = request.getSession();
      String id = (String) session.getAttribute("loginId");
      List<CartDTO> cartList = itemMapper.getCartList(id);
      model.addAttribute("cartList", cartList);

      int total = 0;
      for (CartDTO cart : cartList) {
          total += cart.getTotalPrice();
      }
      model.addAttribute("total", total);
      return "item/cartList";
  }
  
  // 장바구니 수정
  @Override
  public String cartUpdate(CartDTO cartDTO) {
    itemMapper.updateCart(cartDTO);
    return "redirect:/item/cartList.html";
  }
  
  // 장바구니 삭제
  @Override
  public String deleteCart(String cartId) {
      itemMapper.deleteCart(cartId);
      return "redirect:/item/cartList.html";
  }
  
  // 장바구니 선택 삭제
  @Override
  public String deleteCarts(List<String> cartIds) {
      if (cartIds == null || cartIds.isEmpty()) {
          return "redirect:/item/cartList.html";
      }

      CartDTO cartDTO = new CartDTO();
      cartDTO.setCartIdList(cartIds);

      itemMapper.deleteCarts(cartDTO);

      return "redirect:/item/cartList.html";
  }
  
  // 상품 구매
  @Override
  public String itemBuys(HashMap<String, Object> map, HttpServletRequest request) {
      String buyId = getBuyId();

      HttpSession session = request.getSession();
      String id = (String) session.getAttribute("loginId");

      int buyPrice = Integer.parseInt(map.get("final_price").toString());

      BuyDTO buyDTO = new BuyDTO();
      buyDTO.setBuyId(buyId);
      buyDTO.setId(id);
      buyDTO.setBuyPrice(buyPrice);

      ObjectMapper mapper = new ObjectMapper();
      BuyDetailDTO[] buyDetailArr = mapper.convertValue(map.get("detail_info_arr"), BuyDetailDTO[].class);
      List<BuyDetailDTO> buyDetailList = Arrays.asList(buyDetailArr);

      for (BuyDetailDTO e : buyDetailList) {
          e.setBuyId(buyId);
      }
      buyDTO.setItemBuyDetailList(buyDetailList);

      itemMapper.itemBuy(buyDTO);

      return "item/itemBuys.html";
  }
  
  //구매 아이디 조회
  @Override
  public String getBuyId() {
    return itemMapper.getBuyId();
  }
}