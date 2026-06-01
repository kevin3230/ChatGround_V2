package com.chatground.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.chatground.utility.Gender;
import com.chatground.utility.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 客製化網站參數
 */
@Entity
@IdClass(CustomProperties.CustomPropertiesId.class)
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomProperties {

	/**
     *主要分類
     */
	@Id
    @Column(nullable = false)
    @Size(max = 10)
    private String domain;
    
    /**
     *次要分類
     */
	@Id
    @Column(nullable = false)
    @Size(max = 255)
    private String type;
    
    
    /**
     *通用名稱
     */
    @Column(name = "common_name", nullable = false)
    @Size(max = 255)
    private String commonName;
    
    /**
     *描述
     */
    @Column
    @Size(max = 500)
    private String description;
    
    /**
     *是否為Hard Code
     */
    @Column(name = "hard_code", nullable = false)
    @Size(max = 5)
    private String hardCode;
    
    /**
     *新增者
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private long createdBy;
    
    /**
     *編輯者
     */
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private long modifiedBy;
    
    /**
     *新增時間
     */
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;
    
    /**
     *修改時間
     */
    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time", nullable = false)
    private Timestamp modifiedTime;
    
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomPropertiesId implements Serializable	{

		private static final long serialVersionUID = 7587940393662787741L;
    	
		private String domain;
		private String type;
		
    }
    
}
