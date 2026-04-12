class IFlashcardService {
    async getFlashcardsByDeck(deckId, userId) {
        throw new Error("Method not implemented");
    }

    async createFlashcard(userId, cardDto) {
        throw new Error("Method not implemented");
    }

    async updateFlashcard(cardId, userId, updateDto) {
        throw new Error("Method not implemented");
    }

    async deleteFlashcard(cardId, userId) {
        throw new Error("Method not implemented");
    }

    async _checkPermission(cardId, userId) {
        throw new Error("Method not implemented");
    }
    
    async softDeleteFlashcard(cardId, userId) {
       throw new Error("Method not implemented");
    }

    async restoreFlashcard(cardId, userId) {
        throw new Error("Method not implemented");
    } 

    async hardDeleteFlashcard(cardId, userId) {
        throw new Error("Method not implemented");
    }   
}

module.exports = IFlashcardService;