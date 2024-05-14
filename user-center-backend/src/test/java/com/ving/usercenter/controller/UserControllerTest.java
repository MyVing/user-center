package com.ving.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ving.usercenter.model.domain.User;
import com.ving.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    void testUserRegister() throws Exception {
        // Setup
        when(mockUserService.userRegister("testVing", "12345678", "12345678", "3122004630")).thenReturn(0L);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/user/register")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUserLogin() throws Exception {
        // Setup
        // Configure UserService.userLogin(...).
        final User user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setUserAccount("userAccount");
        user.setAvatarUrl("avatarUrl");
        user.setUserRole(0);
        when(mockUserService.userLogin(eq("userAccount"), eq("userPassword"),
                any(HttpServletRequest.class))).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/user/login")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUserLogin_UserServiceReturnsNull() throws Exception {
        // Setup
        when(mockUserService.userLogin(eq("userAccount"), eq("userPassword"),
                any(HttpServletRequest.class))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/user/login")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testUserLogout() throws Exception {
        // Setup
        when(mockUserService.userLogout(any(HttpServletRequest.class))).thenReturn(0);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/user/logout")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetCurrentUser() throws Exception {
        // Setup
        // Configure UserService.getById(...).
        final User user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setUserAccount("userAccount");
        user.setAvatarUrl("avatarUrl");
        user.setUserRole(0);
        when(mockUserService.getById(0L)).thenReturn(user);

        // Configure UserService.getSafetyUser(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setUsername("username");
        user1.setUserAccount("userAccount");
        user1.setAvatarUrl("avatarUrl");
        user1.setUserRole(0);
        final User originUser = new User();
        originUser.setId(0L);
        originUser.setUsername("username");
        originUser.setUserAccount("userAccount");
        originUser.setAvatarUrl("avatarUrl");
        originUser.setUserRole(0);
        when(mockUserService.getSafetyUser(originUser)).thenReturn(user1);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/user/current")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSearchUsers() throws Exception {
        // Setup
        // Configure UserService.list(...).
        final User user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setUserAccount("userAccount");
        user.setAvatarUrl("avatarUrl");
        user.setUserRole(0);
        final List<User> userList = Arrays.asList(user);
        when(mockUserService.list(any(QueryWrapper.class))).thenReturn(userList);

        // Configure UserService.getSafetyUser(...).
        final User user1 = new User();
        user1.setId(0L);
        user1.setUsername("username");
        user1.setUserAccount("userAccount");
        user1.setAvatarUrl("avatarUrl");
        user1.setUserRole(0);
        final User originUser = new User();
        originUser.setId(0L);
        originUser.setUsername("username");
        originUser.setUserAccount("userAccount");
        originUser.setAvatarUrl("avatarUrl");
        originUser.setUserRole(0);
        when(mockUserService.getSafetyUser(originUser)).thenReturn(user1);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/user/search")
                        .param("username", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testSearchUsers_UserServiceListReturnsNoItems() throws Exception {
        // Setup
        when(mockUserService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/user/search")
                        .param("username", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testDeleteUser() throws Exception {
        // Setup
        when(mockUserService.removeById(eq(0L), any(HttpServletRequest.class))).thenReturn(false);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/user/delete")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
