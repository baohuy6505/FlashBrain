class IDeckService {
   async createDeck(deckData) {
        throw new Error("Method not implemented");
    }
    async getDeckById(deckId, userId) {
        throw new Error("Method not implemented");
    }
    async getAllDecksByUserId(userId) {
        throw new Error("Method not implemented");
    }
    async getPublicDecks(userId) {
        throw new Error("Method not implemented");
    }
    async updateDeck(deckId, userId, deckUpdateData) {
        throw new Error("Method not implemented");
    }
    async hardDeleteDeck(deckId, userId) {
        throw new Error("Method not implemented");
    }
    async softDeleteDeck(deckId, userId) {
        throw new Error("Method not implemented");
    }
    async restoreDeck(deckId, userId) {
        throw new Error("Method not implemented");
    }
}
module.exports = IDeckService;