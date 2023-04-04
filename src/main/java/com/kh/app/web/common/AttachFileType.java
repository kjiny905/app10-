package com.kh.app.web.common;

// enum:한정된 값의 타입을 정의할때 사용 ex.요일
public enum AttachFileType {

  F010301("상품첨부파일"),
  F010302("상품이미지파일");

  private String description;

  AttachFileType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
