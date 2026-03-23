const IDeckService = require('./interfaces/IDeckService');
const DeckRepository = require('../repositories/DeckRepository');

class DeckService extends IDeckService {
    constructor() {
        super();
        this.deckRepository = new DeckRepository();
    }
    async createDeck(deckData) {
        return await this.deckRepository.createDeck(deckData);
    }

    async getDeckById(deckId) {
        return await this.deckRepository.getDeckById(deckId);
    }
}