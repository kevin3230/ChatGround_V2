var submitFlag = true;
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
            // console.log(obj);
            if(false === obj.error){
                if(true === obj.unique){
                    $("#account_hint").text("此帳號可以使用");
                    submitFlag = true;
                }else{
                    $("#account_hint").text("此帳號已被使用");
                    submitFlag = false;
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
            // console.log(obj);
            if(false === obj.error){
                if(true === obj.unique){
                    $("#email_hint").text("此信箱可以使用");
                    submitFlag = true;
                }else{
                    $("#email_hint").text("此信箱已被使用");
                    submitFlag = false;
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
        }
    });
}

function addEmailListener(){
    $("#email").blur(function(){
        if($("#email").val().trim() != ""){
            checkEmail();
        }
    });
}

function addSubmitListener(){
    $("form").submit(function(e){
        checkAccount();
        checkEmail();

        if(submitFlag == false || !confirmPwd()){
            e.preventDefault();
            alert("請確認填寫格式正確");
        }
    });
}

function confirmPwd(){
    if($("#pwd1").val() === $("#pwd2").val()){
        $("#pwd_hint").text("密碼相同");
        return true;
    }else{
        $("#pwd_hint").text("密碼不同");
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