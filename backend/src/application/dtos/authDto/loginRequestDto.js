class LoginRequestDto {
    constructor(body) {
        this.email = body.email?.trim().toLowerCase() || '';
        this.password = body.password?.trim() || '';
    }

    validate() {
        if (!this.email) {
            throw new Error("Vui lòng nhập email");
        }

        if (!this.password) {
            throw new Error("Vui lòng nhập mật khẩu");
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(this.email)) {
            throw new Error("Email không hợp lệ");
        }

        return true;
    }

}

module.exports = LoginRequestDto;