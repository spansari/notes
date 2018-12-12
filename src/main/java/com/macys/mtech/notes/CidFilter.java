package com.macys.mtech.notes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CidFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

//        if (req instanceof HttpServletRequest) {
//
//            HttpServletRequest request = (HttpServletRequest) req;
//
//            String requestCid = request.getHeader("CID");
//
//            if (null == requestCid) {
//                requestCid = UUID.randomUUID().toString();
//            }
//
//            System.out.println("Correlation ID:"+requestCid);
//
//            // add cid to the MDC
//            MDC.put("CID", requestCid);
//        }
//
//        try {
//            // call filter(s) upstream for the real processing of the request
//            chain.doFilter(req, res);
//        } finally {
//            // it's important to always clean the cid from the MDC,
//            // this Thread goes to the pool but it's loglines would still contain the cid.
//            MDC.remove("CID");
//        }

    }

    @Override
    public void destroy() {
        // nothing
    }
    @Override
    public void init(FilterConfig fc) throws ServletException {
        // nothing
    }
}