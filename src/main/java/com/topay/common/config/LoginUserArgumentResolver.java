package com.topay.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 메소드 호출 전에, 특정 어노테이션이 붙은 파라미터에 대해 해당 파라미터에 맞는 값을 주입하는 역할
 */
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    // TODO: 구현 예정
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    // TODO: 구현 예정
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        
        return null;
    }

}
