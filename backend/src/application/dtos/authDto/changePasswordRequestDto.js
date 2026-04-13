class ChangePasswordRequestDto {
    constructor(body) {
        this.oldPassword = body.oldPassword;
        this.newPassword = body.newPassword;
    }

    validate() {
        if(!this.oldPassword) {
            throw new Error("Vui lòng nhập mật khẩu cũ");
        }

        if(!this.newPassword || this.newPassword.length < 8 || this.newPassword.length > 32) {
            throw new Error("Mật khẩu mới phải có độ dài từ 8 đến 32 ký tự");
        }

        if(this.newPassword == this.oldPassword) {
            throw new Error("Mật khẩu mới phải khác mật khẩu cũ");
        } 

        return true;
    }
}

module.exports = ChangePasswordRequestDto;