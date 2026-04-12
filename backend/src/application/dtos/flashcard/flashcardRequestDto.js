class FlashcardRequestDto {
    constructor(data) {
        this.deckId = data.deckId;
        this.frontText = data.frontText;
        this.backText = data.backText;
    }

    async validate() {
        if (!this.deckId || typeof this.deckId !== 'string') {
            throw new Error("ID bộ bài (deckId) là bắt buộc.");
        }
        if (!this.frontText || this.frontText.trim().length === 0) {
            throw new Error("Mặt trước của thẻ không được để trống.");
        }
        if (!this.backText || this.backText.trim().length === 0) {
            throw new Error("Mặt sau của thẻ không được để trống.");
        }
    }
}
module.exports = FlashcardRequestDto;