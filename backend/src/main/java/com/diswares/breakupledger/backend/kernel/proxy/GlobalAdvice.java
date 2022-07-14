package com.diswares.breakupledger.backend.kernel.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Data;
import core.MultipartData;
import com.diswares.breakupledger.backend.kernel.constants.ResponseCode;
import com.diswares.breakupledger.backend.kernel.exception.BundledException;
import com.diswares.breakupledger.backend.kernel.extend.ProtocolDataPlugin;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.OriginalResponse;
import com.diswares.breakupledger.backend.kernel.proxy.response.strategy.StrategyDispatcher;
import com.diswares.breakupledger.backend.kernel.vo.ProtocolResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 填充策略转发器
     */
    private final StrategyDispatcher dispatcher = new StrategyDispatcher();
    private static final Map<Integer, String> TIP = new HashMap<>();

    /**
     * 客户端错误
     */
    private static final Integer ERROR_CLIENT = 0xa1;

    /**
     * 自动装配的 ObjectMapper, 保留了项目的个性化设置
     */
    private final ObjectMapper mapper;

    static {
        TIP.put(ERROR_CLIENT, "请检查本次请求路径、参数是否正确");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Data> exceptionHandler(Exception e) {
        ResponseCode responseCode = ResponseCode.ERROR_SERVICE;

        log.info("", e);

        if (e instanceof BundledException) {
            responseCode = ((BundledException) e).getResponse().getCode();
        }

        AtomicReference<HttpStatus> status = new AtomicReference<>(responseCode.status);
        AtomicReference<String> msg = new AtomicReference<>(e.getLocalizedMessage());

        final ProtocolResponse protocolResponse = new ProtocolResponse();

        Function<BindingResult, Void> handlerBindings = br -> {
            if (!br.getFieldErrors().isEmpty()) {
                final FieldError error = br.getFieldErrors().get(0);
                msg.set(error.getDefaultMessage());
                status.set(HttpStatus.BAD_REQUEST);
            }
            return null;
        };

        if (e instanceof BindException) {
            BindException exception = (BindException) e;
            handlerBindings.apply(exception.getBindingResult());
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            handlerBindings.apply(exception.getBindingResult());
        } else if (e instanceof HttpMessageNotReadableException) {
            msg.set("缺少必填参数或读取参数时遇到错误");
            log.error("", e);
            responseCode = ResponseCode.ERROR_QUERY;
        } else if (e instanceof DuplicateKeyException) {
            log.error("", e);
            String pattern = "\\((.*?)\\)=\\((.*?)\\)";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(e.getMessage());
            if (m.find()) {
                String column = m.group(1);
                String value = m.group(2);
                msg.set(column + "字段不可重复，(" + value + ")已存在");
            }
            responseCode = ResponseCode.UNIQUE_KEY;
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            log.error("", e);
            String pattern = "'(.*?)'.*?'(.*?)'.*?\\{(.*?)}";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            //现在创建 matcher 对象
            Matcher m = r.matcher(e.getMessage());
            if (m.find()) {
                msg.set("无法将" + m.group(3) + "从" + m.group(1) + "类型转换为" + m.group(2) + "类型");
            }
            responseCode = ResponseCode.TRANSFORM_ERROR;
        } else {
            final Throwable cause = e.getCause();
            if (null != cause) {
                final Throwable superCause = cause.getCause();
                if (null != superCause && BundledException.class.isAssignableFrom(superCause.getClass())) {
                    msg.set(superCause.getMessage());
                    final ProtocolResponse response = ((BundledException) superCause).getResponse();
                    responseCode = response.getCode();
                    status.set(responseCode.status);
                }
            } else {
                // 仅打印非参数错误异常
                log.error("", e);
            }
        }


        return ResponseEntity
                .status(status.get())
                .body(protocolResponse
                        .setMsg(msg.get())
                        .setCode(responseCode)
                        .toData());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Object beforeBodyWrite(Object response, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        final HttpServletResponse servletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        final OriginalResponse originalResponse = Objects.requireNonNull(methodParameter.getMethod()).getAnnotation(OriginalResponse.class);

        // 原路返回
        if (null != originalResponse) {
            return response;
        }

        // 返回了协议，直接返回原始对象
        if (null != response && ProtocolResponse.class.isAssignableFrom(response.getClass())) {
            final ProtocolResponse protocolResponse = (ProtocolResponse) response;
            serverHttpResponse.setStatusCode(protocolResponse.getCode().status);
            return protocolResponse.toData();
        }

        final Inclusion inclusion = Objects.requireNonNull(methodParameter.getMethod()).getAnnotation(Inclusion.class);
        // 构造协议
        MultipartData data = new ProtocolResponse().toData();

        // 仅在请求成功时进行填充处理
        final int status = servletResponse.getStatus();
        if (null != response && status >= HttpServletResponse.SC_OK && status < HttpServletResponse.SC_MULTIPLE_CHOICES) {

            // 初始化 mapper
            if (null == StrategyDispatcher.mapper) {
                StrategyDispatcher.mapper = this.mapper;
            }

            if (null != inclusion) {
                // 转发至填充策略
                dispatcher.dispatch(inclusion, data, response);
            } else {
                data.include("data", response);
            }
            return data;
        }

        // 设置为请求失败
        if (null != response) {
            data.plugin(ProtocolDataPlugin.class);
        }

        // 发生错误的情况下，先合并原始报文
        if (null != response && MultipartData.class.isAssignableFrom(response.getClass())) {
            data.parties(response);
        }

        // 发生客户端错误
        if (status >= HttpServletResponse.SC_BAD_REQUEST && status < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            final ProtocolDataPlugin plugin = data.plugin(ProtocolDataPlugin.class);
            String key = "msg";
            final String msg = data.getStringPart(key);
            if (Strings.isBlank(msg) || ResponseCode.SUCCESS.msg.equals(msg)) {
                plugin.msg(TIP.get(ERROR_CLIENT));
            }
            return plugin
                    .code(ResponseCode.ERROR_QUERY_FORMAT.value)
                    .attach();
        }

        // 服务端异常已在异常处理器中进行处理
        return data;
    }
}
