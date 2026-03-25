const DeckService = require("../../application/services/deckService");
const deckRequestDto = require("../../application/dtos/deck/deckRequestDto");
class DeckController {
  constructor() {
    this.deckService = new DeckService();
  }
  async createDeck(req, res) {
    try {
      const userId = req.user.userId;
      const deckData = new deckRequestDto({ ...req.body, userId: userId });
      await deckData.validate();
      const createdDeck = await this.deckService.createDeck(deckData);
      res.status(201).json(createdDeck);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }

  async getDeckById(req, res) {
    try {
      const deckId = req.params.id;
      const userId = req.user.userId;
      const deck = await this.deckService.getDeckById(deckId, userId);
      res.status(200).json(deck);
    } catch (error) {
      res.status(404).json({ error: error.message });
    }
  }

  async getAllDecksByUserId(req, res) {
    try {
      const userId = req.user.userId;
      const decks = await this.deckService.getAllDecksByUserId(userId);
      res.status(200).json(decks);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }

  async updateDeck(req, res) {
    try {
      const deckId = req.params.id;
      const userId = req.user.userId;
      const deckUpdateData = new deckRequestDto({
        ...req.body,
        userId: userId,
      });
      await deckUpdateData.validate();
      const updatedDeck = await this.deckService.updateDeck(
        deckId,
        userId,
        deckUpdateData,
      );
      res.status(200).json(updatedDeck);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }
  async softDeleteDeck(req, res) {
    try {
      const deckId = req.params.id;
      const userId = req.user.userId;
      const deletedDeck = await this.deckService.softDeleteDeck(deckId, userId);
      res.status(200).json(deletedDeck);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }
  async restoreDeck(req, res) {
    try {
      const deckId = req.params.id;
      const userId = req.user.userId;
      const restoredDeck = await this.deckService.restoreDeck(deckId, userId);
      res.status(200).json(restoredDeck);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }
  async hardDeleteDeck(req, res) {
    try {
      const deckId = req.params.id;
      const userId = req.user.userId;
      const deletedDeck = await this.deckService.hardDeleteDeck(deckId, userId);
      res.status(200).json(deletedDeck);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  }
}

module.exports = new DeckController();
