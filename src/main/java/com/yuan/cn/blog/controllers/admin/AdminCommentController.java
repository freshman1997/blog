package com.yuan.cn.blog.controllers.admin;

import com.yuan.cn.blog.commons.BlogResponse;
import com.yuan.cn.blog.commons.ErrorType;
import com.yuan.cn.blog.commons.ResponseCode;
import com.yuan.cn.blog.commons.SuccessType;
import com.yuan.cn.blog.pojo.Comment;
import com.yuan.cn.blog.service.CommentService;
import com.yuan.cn.blog.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    @DeleteMapping("/delete/{commentId}")
    public BlogResponse<Integer> deleteComment(@PathVariable int commentId){
        int ret = commentService.deleteComment(commentId);
        if (ret > 0){
            return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, ret);
        }
        return BlogResponse.ERROR(ErrorType.ERROR_WITH_CODE_MESSAGE, ResponseCode.ERROR.getCode(), "删除"+commentId+"评论失败！", null);
    }

    @PostMapping("/fetch-all-comment")
    public BlogResponse<CommentVo> fetchAllComment(int page){
        return BlogResponse.SUCCESS(SuccessType.SUCCESS_WITH_DATA, null, new CommentVo(commentService.fetchAllComment((page - 1) * 8, page * 8), commentService.totalNumber()));
    }
}
