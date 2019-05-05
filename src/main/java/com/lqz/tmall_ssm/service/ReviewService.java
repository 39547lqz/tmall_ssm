package com.lqz.tmall_ssm.service;

import com.lqz.tmall_ssm.pojo.Review;

import java.util.List;

public interface ReviewService {

    int add(Review review);

    int delete(int id);

    int update(Review review);

    Review get(int id);

    List<Review> list(int pid);

    int getCount(int pid);
}
