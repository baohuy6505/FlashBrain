class FlashcardUpdateRequestDto {
    constructor(data) {
        this.frontText = data.frontText;
        this.backText = data.backText;
    }

    async validate() {
        // Nếu có gửi lên mặt trước, thì mặt trước không được rỗng
        if (this.frontText !== undefined && this.frontText.trim().length === 0) {
            throw new Error("Mặt trước không được để trống.");
        }
        // Nếu có gửi lên mặt sau, thì mặt sau không được rỗng
        if (this.backText !== undefined && this.backText.trim().length === 0) {
            throw new Error("Mặt sau không được để trống.");
        }
    }
}
module.exports = FlashcardUpdateRequestDto;