class RegisterRequestDto {
    constructor(body) {
        this.email = body.email?.trim().toLowerCase() || '';
        this.password = body.password?.trim() || '';               
        this.name = body.name?.trim() || '';
    }

    validate() {
        if (!this.email || !this.password) {
            throw new Error("Email và password là bắt buộc");
        }   
        
        if(!this.name) {
            throw new Error("Tên không được rỗng")
        }

        if (this.name.length > 50) {
            throw new Error("Tên quá dài");
        }

        // validate email format
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(this.email)) {
            throw new Error("Email không hợp lệ");
        }
        
        if (this.password.length < 8 || this.password.length > 32) {
            throw new Error("Mật khẩu phải từ 8 đến 32 ký tự");
        }

        if (this.password.includes(' ')) {
            throw new Error("Password không được chứa khoảng trắng");
        }

        return true; // Nếu tất cả validate đều pass   
        } 
}

module.exports = RegisterRequestDto;