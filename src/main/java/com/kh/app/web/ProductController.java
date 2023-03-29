package com.kh.app.web;

import com.kh.app.domain.entity.Product;
import com.kh.app.domain.product.svc.ProductSVC;
import com.kh.app.web.exception.BizException;
import com.kh.app.web.form.product.DetailForm;
import com.kh.app.web.form.product.SaveForm;
import com.kh.app.web.form.product.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor  // final멤버 필드를 매개값으로하는 생성자를 자동 생성
public class ProductController {

  private final ProductSVC productSVC;

//  public ProductController(ProductSVC productSVC) {
//    this.productSVC = productSVC;
//  }

  //등록양식
  @GetMapping("/add")
  public String saveForm(Model model){
    SaveForm saveForm = new SaveForm();
    model.addAttribute("saveForm",saveForm);
    return "product/saveForm";
  }

  //등록처리
  @PostMapping("/add")
  public String save(
//      @Param("pname") String pname,
//      @Param("quantity") Long quantity,
//      @Param("price") Long price
//      @ModelAttribute:1.요청데이터를 자바객체로 바인딩,2.Model객체에 추가
      //@Valid:데이터 검증을 할때 값을 채워주겠다
      @Valid @ModelAttribute SaveForm saveForm,

      //검증 결과를 담는 객체(@Valid 바로 뒤에 위치해야함)
      BindingResult bindingResult,  
      RedirectAttributes redirectAttributes
  ){
//    log.info("pname={}, quantity={}, price={}",pname,quantity,price);
    log.info("saveForm={}",saveForm);

    //어노테이션 기반 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";  //에러시 등록양식으로 반환
    }

    //필드오류 rejectValue사용
    if(saveForm.getQuantity() == 100){
      bindingResult.rejectValue("quantity","product");  //오류메세지는errors.properties에서 출력
    }

    //글로벌오류 reject사용
    //총액(상품수량*단가) 1000만원 초과금지(비즈니스룰-데이터검증 업무 규칙위반)
    if(saveForm.getQuantity() * saveForm.getPrice() > 10_000_000L){
      //reject 메소드에 2개 이상의 메시지를 넣을 수 있음
      bindingResult.reject("totalprice",new String[]{"1000"},"");
    }
    if(saveForm.getQuantity() > 1 && saveForm.getQuantity() < 10){
      bindingResult.reject("quantity",new String[]{"1","2"},"");
    }
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";  //에러시 등록양식으로 반환
    }

    
    //데이터 검증
    //등록
    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Long savedProductId = productSVC.save(product);
    redirectAttributes.addAttribute("id",savedProductId);
    return "redirect:/products/{id}/detail";
  }

  //조회
  @GetMapping("/{id}/detail")
  public String findById(
      @PathVariable("id") Long id,
      Model model
      //ctrl + space로 Model이라는 객체 받기
  ){
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();
    //값 존재 시 값을 반환

    DetailForm detailForm = new DetailForm();
    detailForm.setProductId(product.getProductId());
    detailForm.setPname(product.getPname());
    detailForm.setQuantity(product.getQuantity());
    detailForm.setPrice(product.getPrice());

    model.addAttribute("detailForm",detailForm);
    return "product/detailForm";
  }

  //수정양식
  @GetMapping("/{id}/edit")
  public String updateForm(
      @PathVariable("id") Long id,
      Model model
  ){
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setProductId(product.getProductId());
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());

    model.addAttribute("updateForm",updateForm);
    return "product/updateForm";
  }

  //수정
  @PostMapping("/{id}/edit")
  public String update(
      @PathVariable("id") Long productId,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    //데이터 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}",bindingResult);
      return "product/updateForm";
    }

    //정상 처리
    Product product = new Product();
    product.setProductId(productId);
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());

    productSVC.update(productId, product);

    redirectAttributes.addAttribute("id",productId);
    return "redirect:/products/{id}/detail";
  }

  //삭제
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long productId){

    productSVC.delete(productId);

    return "redirect:/products";
  }

  //목록
  @GetMapping
  public String findAll(Model model){

    List<Product> products = productSVC.findAll();
    model.addAttribute("products",products);
    if(products.size() == 0) {
      throw new BizException("등록된 상품정보가 없습니다."); //오류가 없을 경우 화면에 메세지만 전달
//      throw new RuntimeException("오류발생");
    }
    return "product/all";
  }

  //선택삭제
  @PostMapping("/items/del")
  public String deleteParts(@RequestParam("chk") List<Long> productIds){
    log.info("productIds={}", productIds);
    if(productIds.size() > 0) {
      productSVC.deleteParts(productIds);
    }else {
      return "product/all";
    }
    return "redirect:/products";
  }
}