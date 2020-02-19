/**
 *
 */
function changeVerifyCode(img) {
    //生成随机数
    img.src="../Kaptcha?"+Math.floor(Math.random()*100);
}