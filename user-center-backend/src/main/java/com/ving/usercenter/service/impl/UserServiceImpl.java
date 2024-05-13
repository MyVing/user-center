package com.ving.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.ving.usercenter.common.ErrorCode;
import com.ving.usercenter.constant.UserConstant;
import com.ving.usercenter.exception.BusinessException;
import com.ving.usercenter.service.UserService;
import com.ving.usercenter.model.domain.User;
import com.ving.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ving.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.ving.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 *用户服务实现类
 *
* @author ving
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-01-26 18:56:04
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;


    /**
     * 盐值：混淆密码
     */
    private static final String SALT = "ving";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String schoolCode) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,schoolCode)){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if(schoolCode.length() != 10 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学号格式错误");
        }
        //账户不能包含特殊字符
        String validPattern="[^\\w\\s]";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号输入有误");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"校验密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) { //表明已经有人注册了
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //学号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schoolCode",schoolCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0) { //表明已经有人注册了
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学号重复");
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3. 插入数据
        User user= new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setSchoolCode(schoolCode);
        boolean saveResult = this.save(user);
        if (!saveResult){
           throw new BusinessException(ErrorCode.SYSTEM_ERROR ,"注册失败");
        }

        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR,"账号密码不能为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        if (userPassword.length() < 8 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"输入账号不合法");
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if(user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码错误!");
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登陆态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public List<User> searchUsers(String username,HttpServletRequest request) {
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        List<User> userList = list(queryWrapper);
        List<User> list = userList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
        return list;
    }

    /**
     * 根据Id删除用户
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public Boolean removeById(long id, HttpServletRequest request) {
        if(isAdmin(request)){
            throw  new BusinessException(ErrorCode.NO_AUTH,"无权限");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该用户不存在");
        }
        boolean removed = removeById(id);
        return removed;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    public boolean isAdmin( HttpServletRequest request){
        //仅管理员可查询
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if(user == null || user.getUserRole() != ADMIN_ROLE){
            throw new BusinessException(ErrorCode.NO_AUTH,"无权限访问");
        }
        return true ;
    }

    /**
     * 是否为管理员
     *
     * @param loginUser
     * @return
     */
    public boolean isAdmin(User loginUser){
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setSchoolCode(originUser.getSchoolCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(0);
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登陆态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户 (内存过滤)
     *
     * @param tagNameList 用户要拥有的标签
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 先查询所有用户
        QueryWrapper queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        //2. 在内存中判断是否包含要求的标签
        return userList.stream().filter(user -> {
                String tagsStr = user.getTags();
                if(StringUtils.isBlank(tagsStr)){
                    return false;
                }
                Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {}.getType());
                tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
                for (String tagName : tempTagNameSet) {
                    if(!tempTagNameSet.contains(tagName)){
                        return false;
                    }
                }
                return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
        //return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }

    @Override
    public int updateUser(@RequestBody User user, User loginUser) {
        Long userId = user.getId();
        if(userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //如果是管理员，允许更新任意用户
        //如果不是管理员，只允许更新当前（自己的）信息
        if(!isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if(oldUser == null){
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        return userMapper.updateById(user);



    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if(userObj == null){
            throw  new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }


    /**
     * 根据标签搜索用户(SQL查询)
     * @param tagNameList
     * @return
     */
    @Deprecated
    private List<User> searchUserByTagsBySQL(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //拼接 and 查询
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags",tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
        //return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }



}




