package com.idle.gaza.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuideDocumentResponse {
    String id;

    String idFile;

    String certificateResidence;

    String certificate;
}
