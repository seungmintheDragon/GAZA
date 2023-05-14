package com.idle.gaza.api.service;

import com.idle.gaza.api.request.UserUpdateRequest;
import com.idle.gaza.api.response.GuideDocumentResponse;
import com.idle.gaza.db.entity.GuideDocument;
import com.idle.gaza.db.entity.User;
import com.idle.gaza.db.repository.GuideDocumentRepository;
import com.idle.gaza.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    GuideDocumentRepository guideDocumentRepository;

    public User join(User user) {
        return userRepository.save(user);
    }

    /**
     * 로그인 구현체
     *
     * @param id String
     * @return Optional<UserDto>
     */
    @Override
    public Optional<User> login(String id) {
        return userRepository.login(id);
    }

    @Override
    public User searchUser(String id) {
        //유저 정보 리턴
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public int changeState(String id, String state) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return 0;

        User updateUser = user.get();

        updateUser.setState(state);

        userRepository.save(updateUser);

        return 1;
    }

    @Override
    public int registerGuide(String id, GuideDocument guideDocument) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return 0;

        User resultUser = user.get();

        guideDocument.setUser(resultUser);

        guideDocumentRepository.save(guideDocument);

        return 1;
    }

    @Override
    public List<GuideDocumentResponse> searchGuideRegisterList() {
        List<GuideDocumentResponse> result = new ArrayList<>();

        Optional<List<GuideDocument>> list = guideDocumentRepository.searchGuideRegisterList();

        if(list.isPresent()){
            List<GuideDocument> documentList = list.get();

            for(int i = 0; i < documentList.size(); i++) {
                Optional<User> user = userRepository.findByUserId(documentList.get(i).getUserId());

                String id = null;
                if (user.isPresent()) {
                    id = user.get().getId();
                }

                result.add(new GuideDocumentResponse(id, documentList.get(i).getIdFile(), documentList.get(i).getCertificateResidence(), documentList.get(i).getCertificate()));
            }
        }

        return result;
    }

    @Override
    public User searchUserById(String id) {
        //유저 정보 리턴
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public int updateUser(String id, UserUpdateRequest userUpdateRequest) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return 0;

        User updateUser = user.get();

        log.debug("-------------------- update Info + " + userUpdateRequest.getName() + "    " + userUpdateRequest.getPicture());

        if(userUpdateRequest.getPicture() != null) {
            updateUser.setPicture(userUpdateRequest.getPicture());
        }

        updateUser.setName(userUpdateRequest.getName());
        updateUser.setPhone_number(userUpdateRequest.getPhone_number());
        updateUser.setEmail(userUpdateRequest.getEmail());
        updateUser.setEmail_domain(userUpdateRequest.getEmail_domain());

        userRepository.save(updateUser);

        return 1;
    }

    @Override
    public int updatePassword(String id, String password) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return 0;

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        password = bCryptPasswordEncoder.encode(password);

        User updateUser = user.get();

        updateUser.setPassword(password);

        userRepository.save(updateUser);

        return 1;
    }

    @Override
    public int deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) return 0;

        User updateUser = user.get();

        updateUser.setState("US5");

        userRepository.save(updateUser);

        return 1;
    }
}
