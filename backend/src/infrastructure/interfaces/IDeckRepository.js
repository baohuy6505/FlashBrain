class IDeckRepository {
  async createDeck(deck) {
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

  async updateDeck(deckId, userId, deckUpdate) {
    throw new Error("Method not implemented");
  }

  async deleteSoftDeck(deckId, userId) {
    throw new Error("Method not implemented");
  }
  async deleteDeck(deckId, userId) {
    throw new Error("Method not implemented");
  }
}

module.exports = IDeckRepository;
