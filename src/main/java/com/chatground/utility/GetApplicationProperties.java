package com.chatground.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("getApplicationProperties")
//@ConfigurationProperties(prefix="web.security")
public class GetApplicationProperties {
	
	//自訂WebSecurity的debug開關，找不到參數預設false
	@Value("${web.security.security-debg:false}")
	private boolean securityDebug;
	
	public boolean getSecurityDebug() {
		return securityDebug;
	}
	
	
}
