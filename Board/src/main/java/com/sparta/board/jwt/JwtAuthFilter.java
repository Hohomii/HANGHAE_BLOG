package com.sparta.board.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.ErrorResponse;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getHeaderToken(request, JwtUtil.ACCESS_TOKEN);
        String refreshToken = jwtUtil.getHeaderToken(request, JwtUtil.REFRESH_TOKEN);
//        String token = jwtUtil.resolveToken(request);

        if (accessToken != null) {
            //accessToken이 유효하면 setAuthentication을 통해 security context에 인증 정보 저장
            if (jwtUtil.validateToken(accessToken)) {
                setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));
            }
            //accessToken 만료 && refreshToken 존재하는 상황
            else if (refreshToken != null) {
                //refreshToken 검증 && DB에서 refreshToken 존재 유무 확인
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
                //refreshToken 유효 && DB의 토큰값과 일치하면
                if (isRefreshToken) {
                    //refreshToken으로 정보 가져오기
                    String username = jwtUtil.getUserInfoFromToken(refreshToken);
                    User user = userRepository.findByUsername(username).orElseThrow(
                            () -> new CustomException(StatusCode.USER_NOT_FOUND)
                    );
                    //new accessToken 발급
                    String newAccessToken = jwtUtil.createToken(username, user.getRole(), JwtUtil.ACCESS_TOKEN);
                    //헤더에 accessToken 추가
                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
                    //Security context에 인증 정보 넣기
                    setAuthentication(jwtUtil.getUserInfoFromToken(newAccessToken));
                }
                //refreshToken 만료 || DB의 토큰값과 다르다면
                else {
                    response.setContentType("application/json; charset=utf8");
                    String json = new ObjectMapper().writeValueAsString(ErrorResponse.responseEntity(StatusCode.INVALID_TOKEN).getBody());
                    response.getWriter().write(json);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    //SecurityContext에 Authentication 객체를 저장
    private void setAuthentication(String username) {
        Authentication authentication = jwtUtil.createAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}