class UserStatsResponseDto {
    constructor(data) {
        this.totalDecks = data.totalDecks || 0;
        this.totalCards = data.totalCards || 0;
        this.cardsToReview = data.cardsToReview || 0; // Số thẻ cần học hôm nay
        this.newCards = data.newCards || 0;           // Số thẻ chưa học bao giờ
        this.learnedCards = data.learnedCards || 0;   // Số thẻ đang trong chu trình ôn tập
    }
}

module.exports = UserStatsResponseDto;