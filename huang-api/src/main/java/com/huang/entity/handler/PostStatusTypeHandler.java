package com.huang.entity.handler;

import com.huang.entity.enums.PostStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @Time 2022-04-19 8:50
 * Created by Huang
 * className: PostStatusTypeHandler
 * Description:
 */
@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(PostStatus.class)
@Slf4j
public class PostStatusTypeHandler extends BaseTypeHandler<PostStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, PostStatus postStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,postStatus.getValue());
    }

    /***
     * @Description: 默认处理入口
     * @param resultSet:
     * @param s:
     * @return: com.huang.entity.enums.PostStatus
     **/
    @Override
    public PostStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return get(resultSet.getInt(s));
    }

    @Override
    public PostStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return get(resultSet.getInt(i));
    }

    @Override
    public PostStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return get(callableStatement.getInt(i));
    }

    private PostStatus get(Integer code){
        return Arrays.stream(PostStatus.values()).filter(postStatus -> postStatus.getValue().equals(code)).findFirst().orElse(null);
    }
}
