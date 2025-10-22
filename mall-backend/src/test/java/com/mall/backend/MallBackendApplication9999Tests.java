package com.mall.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.backend.model.entity.SysRoleMenu;
import com.mall.backend.service.system.RoleMenuService;
import com.mall.backend.util.JwtUtil;
import com.mall.backend.util.Result;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class MallBackendApplication9999Tests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private JwtUtil jwtUtil;
    @Test
    void contextLoads() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Test
    void redisTest() {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImp0aSI6ImRmODEyMmVjLTgyNWMtNGRlMS05MGY0LTQ2YjYxYTE1Nzc3YiIsInR5cGUiOiJyZWZyZXNoIiwiaWF0IjoxNzYwMTAyOTIxLCJleHAiOjE3NjAxODkzMjF9.wLAZJv8o0EKYbufHBGm7zRuU1Bx2FtoIt4On8es8DRI";
        String username = jwtUtil.getClaim(refreshToken, Claims::getSubject);
        System.out.println(username);
        String jti = jwtUtil.getJtiFromToken(refreshToken);
        System.out.println(jti);

        // 新增版本号验证
        Long tokenVersion = jwtUtil.getVersionFromToken(refreshToken);
        String versionStr = redisTemplate.opsForValue().get("user:login_version:" + username);
        System.out.println("versionStr:"+Long.parseLong(versionStr));
        System.out.println("tokenVersion:"+tokenVersion);
    }

    @Test
    void menuTest() {
        List<Object> list = roleMenuService.listObjs(new QueryWrapper<SysRoleMenu>().eq("role_id", 2).select("menu_id"));
        System.out.println( list);
    }

    //模拟用户下单
    @Test
    void testOrder() {

    }


}
