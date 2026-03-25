class deckResponseDto {
    constructor(deck) {
        this.id = deck.id;
        this.title = deck.title;
        this.isPublic = deck.is_public;
        this.createdAt = deck.created_at;
        this.updatedAt = deck.updated_at;
    }
}

module.exports = deckResponseDto;