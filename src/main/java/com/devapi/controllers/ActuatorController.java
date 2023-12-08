package com.devapi.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@RestController
@RequestMapping("/api-info")
public class ActuatorController {


        private final RequestMappingHandlerMapping handlerMapping;

        public ActuatorController(@Qualifier("controllerEndpointHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
            this.handlerMapping = handlerMapping;
        }

        @GetMapping("/endpoints")
        public Map<RequestMappingInfo, HandlerMethod> getAllEndpoints() {
            return handlerMapping.getHandlerMethods();
        }

}
