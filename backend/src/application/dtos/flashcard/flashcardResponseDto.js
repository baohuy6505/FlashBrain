class FlashcardResponseDto {
    constructor(card) {
        this.id = card._id;
        this.deckId = card.deck_id;
        this.frontText = card.front_text;
        this.backText = card.back_text;
        
        // Trả về thêm thời gian ôn tập tiếp theo để Android hiển thị "Đến hạn"
        this.nextReviewDate = card.next_review_date; 
        this.easeFactor = card.ease_factor;
    }
}
module.exports = FlashcardResponseDto;