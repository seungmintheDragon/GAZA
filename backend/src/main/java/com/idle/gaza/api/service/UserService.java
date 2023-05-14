package com.idle.gaza.api.service;

import com.idle.gaza.api.request.UserUpdateRequest;
import com.idle.gaza.api.response.GuideDocumentResponse;
import com.idle.gaza.db.entity.GuideDocument;
import com.idle.gaza.db.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User join(User user);

    Optional<User> login(String id);

    User searchUserById(String id);

    User searchUser(String id);

    int changeState(String id, String state);

    int registerGuide(String id, GuideDocument guideDocument);

    List<GuideDocumentResponse> searchGuideRegisterList();

    int updateUser(String id, UserUpdateRequest userUpdateRequest);

    int updatePassword(String id, String password);

    int deleteUser(String id);
}
