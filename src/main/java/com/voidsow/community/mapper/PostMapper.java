package com.voidsow.community.mapper;

import com.voidsow.community.entity.Post;
import com.voidsow.community.entity.PostExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

@Mapper
public interface PostMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    long countByExample(PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int deleteByExample(PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int insert(Post row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int insertSelective(Post row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    List<Post> selectByExampleWithBLOBsWithRowbounds(PostExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    List<Post> selectByExampleWithBLOBs(PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    List<Post> selectByExampleWithRowbounds(PostExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    List<Post> selectByExample(PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    Post selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByExampleSelective(@Param("row") Post row, @Param("example") PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("row") Post row, @Param("example") PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByExample(@Param("row") Post row, @Param("example") PostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByPrimaryKeySelective(Post row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(Post row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table post
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    int updateByPrimaryKey(Post row);
}