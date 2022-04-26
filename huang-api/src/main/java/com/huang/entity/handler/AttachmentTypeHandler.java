package com.huang.entity.handler;

import com.huang.entity.enums.AttachmentType;
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
 * @Time 2022-04-25 19:50
 * Created by Huang
 * className: AttachmentTypeHandler
 * Description:
 */
@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(AttachmentType.class)
@Slf4j
public class AttachmentTypeHandler extends BaseTypeHandler<AttachmentType> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, AttachmentType attachmentType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,attachmentType.getValue());
    }

    /***
     * @Description: 默认处理入口
     * @param resultSet:
     * @param s:
     * @return: com.huang.entity.enums.PostStatus
     **/
    @Override
    public AttachmentType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return get(resultSet.getInt(s));
    }

    @Override
    public AttachmentType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return get(resultSet.getInt(i));
    }

    @Override
    public AttachmentType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return get(callableStatement.getInt(i));
    }

    private AttachmentType get(Integer code){
        return Arrays.stream(AttachmentType.values()).filter(attachmentType -> attachmentType.getValue().equals(code)).findFirst().orElse(null);
    }
}
