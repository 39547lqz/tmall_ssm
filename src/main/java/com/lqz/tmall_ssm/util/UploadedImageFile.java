package com.lqz.tmall_ssm.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UploadedImageFile {

    MultipartFile image;
}
