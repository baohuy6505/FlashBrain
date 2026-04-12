class DeleteAccountRequestDto {
    constructor(body) {
        this.password = body.password;
    }

    validate() {
        if (!this.password) {
            throw new Error("Vui lòng nhập mật khẩu để xác nhận xóa tài khoản!");
        }
        return true;
    }
}
module.exports = DeleteAccountRequestDto;