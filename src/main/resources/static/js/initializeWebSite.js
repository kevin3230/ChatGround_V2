var submitFlag = false;
var isAccountValid = false;
var isEmailValid = false;
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function checkAccount(){
    $.ajax({
        url: "/member/checkAccountAndEmail",
        type: "POST",
        datatype: "json",
        data: {
            action: "checkAccount",
            account: $("#account").val(),
        },
        beforeSend: function(request){
          request.setRequestHeader(header, token);
        },
        success: function(data) {
            var obj = JSON.parse(data);
			let reg = /^\w+$/;
			let account= $("#account").val();
            // console.log(obj);
            if(false === obj.error){
				if(reg.test(account) === false){
					$("#account_hint").text(messages[lang].memberSignUp_account_format_error);
					submitFlag = false;
					isAccountValid = false;
				}else if(true === obj.unique){
                    $("#account_hint").text(messages[lang].memberSignUp_account_available);
					isAccountValid = true;
					if(isAccountValid && isEmailValid){
	                    submitFlag = true;
					}
                }else{
                    $("#account_hint").text(messages[lang].memberSignUp_account_not_available);
                    submitFlag = false;
					isAccountValid = false;
                }
            }else{
                submitFlag = false;
                console.log(obj.message);
            }
        },
        error: function(error){
            submitFlag = false;
            console.log(error);
        }
    });
}

function checkEmail(){
    $.ajax({
        url: "/member/checkAccountAndEmail",
        type: "POST",
        datatype: "json",
        data: {
            action: "checkEmail",
            email: $("#email").val(),
        },
        beforeSend: function(request){
            request.setRequestHeader(header, token);
        },
        success: function(data) {
            var obj = JSON.parse(data);
			var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
			var email= $("#email").val();
            //console.log(obj);
            if(false === obj.error){
				if(reg.test(email) === false){
					$("#email_hint").text(messages[lang].memberSignUp_email_format_error);
					submitFlag = false;
					isEmailValid = false;
				}else if(true === obj.unique){
                    $("#email_hint").text(messages[lang].memberSignUp_email_available);
					isEmailValid = true;
					if(isAccountValid && isEmailValid) {
	                    submitFlag = true;
					}
					
                }else{
                    $("#email_hint").text(messages[lang].memberSignUp_email_not_available);
                    submitFlag = false;
					isEmailValid = false;
                }
            }else{
                submitFlag = false;
                console.log(obj.message);
            }
        },
        error: function(error){
            submitFlag = false;
            console.log(error);
        }
    });
}

function addAccountListener(){
    $("#account").blur(function(){
        if($("#account").val().trim() != ""){
            checkAccount();
        }else{//清除錯誤訊息
			$("#account_hint").text("");
		}
    });
}

function addEmailListener(){
    $("#email").blur(function(){
        if($("#email").val().trim() != ""){
            checkEmail();
        }else{//清除錯誤訊息
			$("#email_hint").text("");
		}
    });
}

function addSubmitListener(){
    $("form").submit(function(e){
        checkAccount();
        checkEmail();
		
        if(submitFlag == false || !confirmPwd()){
            e.preventDefault();
            alert(messages[lang].memberSignUp_confirm_format);
        }
    });
}

function confirmPwd(){
    if($("#pwd1").val() === $("#pwd2").val()){
        $("#pwd_hint").text(messages[lang].memberSignUp_password_identical);
        return true;
    }else{
        $("#pwd_hint").text(messages[lang].memberSignUp_password_different);
        return false;
    }
}

function addConfirmPwdListener(){
    $("#pwd2").blur(function(){
        confirmPwd();
    });
}


$(document).ready(function(){
    addAccountListener();
    addEmailListener();
    addConfirmPwdListener();
    addSubmitListener();
});