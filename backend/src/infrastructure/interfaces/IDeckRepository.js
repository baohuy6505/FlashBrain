class IDeckRepository {
  async create(deck) {
    throw new Error("Method not implemented");
  }

  async findById(deckId, userId) {
    throw new Error("Method not implemented");
  }

  async findAll(userId) {
    throw new Error("Method not implemented");
  }

  async findPublic(userId) {
    throw new Error("Method not implemented");
  }

  async updateById(deckId, userId, deckUpdate) {
    throw new Error("Method not implemented");
  }

  async restoreById(deckId, userId) {
    throw new Error("Method not implemented");
  }
  
  async softDelete(deckId, userId) {
    throw new Error("Method not implemented");
  }
  async hardDelete(deckId, userId) {
    throw new Error("Method not implemented");
  }
}

module.exports = IDeckRepository;
