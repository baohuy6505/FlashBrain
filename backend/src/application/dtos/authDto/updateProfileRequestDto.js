class UpdateProfileRequestDto {
    constructor(body) {
        this.name = body.name?.trim() || '';
        this.image = body.image;

    }

    validate() {   
        if(!this.name) {
            throw new Error("Tên không được rỗng")
        }

        if(this.name.length > 50 || this.name.length < 2) {
            throw new Error("Tên phải có độ dài từ 2 đến 50 ký tự");
        }

        return true; // Nếu tất cả validate đều pass
    }
}

module.exports = UpdateProfileRequestDto;