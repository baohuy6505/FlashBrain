class IFlashcardRepository {
    async findByDeckId(deckId) {
        throw new Error("Method not implemented");
    }

    async create(flashcardData) {
        throw new Error("Method not implemented");
    }

    async findById(cardId) {
        throw new Error("Method not implemented");
    }

    async updateById(cardId, updateData) {
        throw new Error("Method not implemented");
    }

    async softDelete(cardId) {
       throw new Error("Method not implemented");
    }   

    async restore(cardId) {
        throw new Error("Method not implemented");
    }

    async hardDelete(cardId) {
        throw new Error("Method not implemented");
    }

}

module.exports = IFlashcardRepository