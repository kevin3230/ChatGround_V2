package com.chatground.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatground.entity.CustomProperties;
import com.chatground.entity.CustomProperties.CustomPropertiesId;

public interface CustomPropertiesRepository extends JpaRepository<CustomProperties, CustomPropertiesId>{

}
