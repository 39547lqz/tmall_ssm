package com.lqz.tmall_ssm.service.impl;

import com.lqz.tmall_ssm.mapper.ReviewMapper;
import com.lqz.tmall_ssm.pojo.Review;
import com.lqz.tmall_ssm.pojo.ReviewExample;
import com.lqz.tmall_ssm.pojo.User;
import com.lqz.tmall_ssm.service.ReviewService;
import com.lqz.tmall_ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private UserService userService;

    @Override
    public int add(Review review) {
        return reviewMapper.insert(review);
    }

    @Override
    public int delete(int id) {
        return reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Review review) {
        return reviewMapper.updateByPrimaryKeySelective(review);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Review> list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");

        List<Review> reviews = reviewMapper.selectByExample(example);
        setUser(reviews);
        return reviews;
    }

    private void setUser(List<Review> reviews) {
        for (Review review : reviews) {
            setUser(review);
        }
    }

    private void setUser(Review review) {
        Integer uid = review.getUid();
        User user = userService.get(uid);
        review.setUser(user);
    }

    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}
