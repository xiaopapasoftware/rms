<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<#assign updNotNeed = ["createBy", "createDate"]>
<#assign whereNotNeed = ["createBy", "createDate","updateBy","updateDate","remarks"]>
<mapper namespace="${basePackage}.${moduleName}.dao.${table.className}Dao">

    <resultMap id="BaseResultMap"  type="${basePackage}.${moduleName}.entity.${table.className}">
    <#list table.columns as col>
        <result column="${col.columnName}" property="${col.javaProperty}" jdbcType="${col.mybatisJdbcType}"/>
    </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list table.columns as column> ${column.columnName}<#if column_has_next>,</#if></#list>
    </sql>

    <sql id="Insert_Columns">
    <#list table.columns as column>
        <if test="${column.javaProperty}!=null"> ${column.columnName},</if>
    </#list>
    </sql>
    <sql id="Insert_Values">
    <#list table.columns as column>
        <if test="${column.javaProperty}!=null">${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}},</if>
    </#list>
    </sql>
    <sql id="Batch_Insert_Values">
    <#list table.columns as column>
        ${r'#{item.'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}},
    </#list>
    </sql>

    <sql id="Update_Set_From_Bean">
    <#list table.baseColumns as column>
        <#if updNotNeed?seq_contains(column.javaProperty)><#else>
        <if test="${column.javaProperty}!=null"> ${column.columnName}=${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}},</if>
        </#if>
    </#list>
    </sql>
    <sql id="BatchUpdate_Set_From_Bean">
    <#list table.baseColumns as column>
        <#if updNotNeed?seq_contains(column.javaProperty)><#else>
        ${column.columnName}=${r'#{item.'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}},
        </#if>
    </#list>
    </sql>

    <!--where-->
    <sql id="where">
        <where>
        <#list table.columns as col>
        <#if whereNotNeed?seq_contains(col.javaProperty)><#else>
        <#if col.javaType== 'java.lang.Integer' ||  col.columnClass == 'java.lang.Double' ||  col.columnClass == 'java.lang.Long' ||col.columnClass == 'java.lang.BigDecimal' >
            <if test="${col.javaProperty}!=null">AND ${col.columnName}=${"#"}{${col.javaProperty}}</if>
        <#elseif col.javaProperty = 'delFlag'>
            AND ${col.columnName} = 0
        <#else>
            <if test="${col.javaProperty}!=null and ${col.javaProperty}!=''"> AND ${col.columnName}=${"#"}{${col.javaProperty}}</if>
        </#if>
        </#if>
        </#list>
        </where>
    </sql>
    <!--end where-->

    <!--insert-->
    <insert id="insert" parameterType="${basePackage}.${moduleName}.entity.${table.className}" useGeneratedKeys="true" keyProperty="<#list table.primaryKeys as column>${column.javaProperty}</#list>">
        insert into ${table.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <include refid="Insert_Columns"/>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <include refid="Insert_Values"/>
        </trim>
    </insert>
    <insert id="batchInsert" parameterType="java.util.List">
        insert into ${table.tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <include refid="Base_Column_List"/>
        </trim>
        values
        <foreach collection="list" item="item" index="index" separator=",">
            <trim prefix="(" suffix=")" suffixOverrides=",">
                <include refid="Batch_Insert_Values"/>
            </trim>
        </foreach>
    </insert>
    <!-- end insert -->

    <!-- delete -->
    <delete id="deleteById" parameterType="<#list table.primaryKeys as key>${key.fullJavaType}</#list>">
        delete from ${table.tableName}
        where <#list table.primaryKeys as column> ${column.columnName} = ${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}} <#if column_has_next>and</#if> </#list>
    </delete>

    <delete id="delete" parameterType="${basePackage}.${moduleName}.entity.${table.className}">
        delete from ${table.tableName}
        <include refid="where"/>
    </delete>
    <!-- end delete -->

    <!-- update -->
    <update id="update" parameterType="${basePackage}.${moduleName}.entity.${table.className}">
        update ${table.tableName}
        <set>
            <include refid="Update_Set_From_Bean"/>
        </set>
        where <#list table.primaryKeys as column> ${column.columnName} = ${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}} <#if column_has_next>and</#if> </#list>
    </update>

    <update id="batchUpdate" parameterType="java.util.List">
        <foreach collection="list" item="item" index="index" open="" close="" separator=";">
        update ${table.tableName}
        <set>
            <include refid="BatchUpdate_Set_From_Bean"/>
        </set>
        where
            <#list table.primaryKeys as column> ${column.columnName} = ${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}} <#if column_has_next>and</#if> </#list>
        </foreach>
    </update>
    <!-- end update -->

    <!-- select -->
    <select id="get" resultMap="BaseResultMap" parameterType="<#list table.primaryKeys as key>${key.fullJavaType}</#list>">
        select <include refid="Base_Column_List"/> from ${table.tableName}
        where
            <#list table.primaryKeys as column> ${column.columnName} = ${r'#{'}${column.javaProperty},jdbcType=${column.mybatisJdbcType}} <#if column_has_next>and</#if> </#list>
    </select>

    <select id="findAllList" resultMap="BaseResultMap" parameterType="${basePackage}.${moduleName}.entity.${table.className}">
        select <include refid="Base_Column_List"/> from ${table.tableName}
        <include refid="where"/>
    </select>

    <select id="findList" resultMap="BaseResultMap" parameterType="${basePackage}.${moduleName}.entity.${table.className}">
        select <include refid="Base_Column_List"/> from ${table.tableName}
        <include refid="where"/>
    </select>
    <!-- end select -->
</mapper>
