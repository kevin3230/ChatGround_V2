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

function restrictMaxDate(){ //生日不可大於等於今天
    let today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //january is 0
    let yyyy = today.getFullYear();
    if(dd < 10){
        dd = '0' + dd;
    }
    if(mm < 10){
        mm = '0' + mm;
    }
    today = yyyy + '-' + mm + '-' + dd;
    $("#birthday").attr("max", today);
}

function previewMem_pic(){
    $("#mem_pic").change(function(){
        $("#previewMem_pic").empty();  //清空圖片
        let files = $("#mem_pic").prop("files");

        // console.log(files);

        if(files.length != 0){  //有選取檔案

            // console.log(files[0]);

            let file = files[0];
            if(file.type.indexOf('image') > -1){
                let reader = new FileReader();
                reader.addEventListener('load', function(){

                    // console.log(reader.result);

                    $("#previewMem_pic").append(
                        $('<img>').attr("src", reader.result)
                    );
                });
                reader.readAsDataURL(file);
            }else{
                alert("請上傳圖片");
            }
        }else{
            $("#previewMem_pic").empty();  //清空圖片
        }
    });
}

$(document).ready(function(){
    addAccountListener();
    addEmailListener();
    addConfirmPwdListener();
    addSubmitListener();
    restrictMaxDate();
    previewMem_pic();
});