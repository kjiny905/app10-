package com.kh.app.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//제너릭타입
public class RestResponse<T> {  //<>다이아몬드연산자에 타입은 대문자 1글자 사용
  private Header header;
  private T data; //유동적 제너릭타입을 사용해 유동적으로 data 타입이 바뀜.고정타입을 하게되면 개별적으로 다 정의

  @Data
  @AllArgsConstructor
  public static class Header {
    private String rtcd;
    private String rtmsg;
  }
//제너릭메소드
  public static <T> RestResponse<T> createRestResponse(String rtcd, String rtmsg, T data){
    return new RestResponse<>(new Header(rtcd, rtmsg),data);
  }
}
