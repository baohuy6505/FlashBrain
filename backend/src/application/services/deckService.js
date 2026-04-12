const IDeckService = require('../interfaces/IDeckService');
// const DeckRepository = require('../../infrastructure/interfaces/IDeckRepository');
const DeckRepository = require('../../infrastructure/repositories/deckRepository');
const deckRequestDto = require('../dtos/deck/deckRequestDto');
const deckResponseDto = require('../dtos/deck/deckResponseDto');
const { v4: uuidv4 } = require('uuid');

class DeckService extends IDeckService {
    constructor() {
        super();
        this.deckRepository = new DeckRepository();
    }
    async createDeck(deckData) {
        if(!deckData.title || !deckData.userId) {
            throw new Error("Tên bộ thẻ và ID người dùng là bắt buộc");
        }
        const newDeck = {
            _id: deckData.id || uuidv4(),
            user_id: deckData.userId,
            title: deckData.title,
            is_public: deckData.isPublic || false,
            is_deleted: false
        };
        const createdDeck = await this.deckRepository.create(newDeck);  
        return new deckResponseDto(createdDeck);
    }

    async getDeckById(deckId, userId) {
        if(!deckId) {
            throw new Error("ID bộ thẻ là bắt buộc");
        }
        if(!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const deck = await this.deckRepository.findById(deckId, userId);
        if(!deck) {
            throw new Error("Không tìm thấy bộ thẻ hoặc bạn không có quyền truy cập");
        }
        return new deckResponseDto(deck);
    }

    async getAllDecksByUserId(userId) {
        if(!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const decks = await this.deckRepository.findAll(userId);
        return decks.map(deck => new deckResponseDto(deck));
    }

    async getPublicDecks(userId) {
        if(!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const publicDecks = await this.deckRepository.findPublic(userId);
        return publicDecks.map(deck => new deckResponseDto(deck));
    }

    async updateDeck(deckId, userId, deckUpdateData) {
        if (!deckId) {
            throw new Error("ID bộ thẻ là bắt buộc");
        }
        if (!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const deckUpdate = {
            title: deckUpdateData.title,
            is_public: deckUpdateData.isPublic
        };
        const updatedDeck = await this.deckRepository.updateById(deckId, userId, deckUpdate);
        if (!updatedDeck) {
            throw new Error("Không tìm thấy bộ thẻ hoặc bạn không có quyền chỉnh sửa");
        }
        return new deckResponseDto(updatedDeck);
    }

    async restoreDeck(deckId, userId) {
        if (!deckId) {
            throw new Error("ID bộ thẻ là bắt buộc");
        }
        if (!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const restoredDeck = await this.deckRepository.restoreById(deckId, userId);
        if (!restoredDeck) {
            throw new Error("Không tìm thấy bộ thẻ hoặc bạn không có quyền khôi phục");
        }
        return new deckResponseDto(restoredDeck);
    }

    async softDeleteDeck(deckId, userId) {
        if (!deckId) {
            throw new Error("ID bộ thẻ là bắt buộc");
        }
        if (!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const deletedDeck = await this.deckRepository.softDelete(deckId, userId);
        if (!deletedDeck) {
            throw new Error("Không tìm thấy bộ thẻ hoặc bạn không có quyền xóa");
        }
        return new deckResponseDto(deletedDeck);
    }

    async hardDeleteDeck(deckId, userId) {
        if (!deckId) {
            throw new Error("ID bộ thẻ là bắt buộc");
        }
        if (!userId) {
            throw new Error("ID người dùng là bắt buộc");
        }
        const deletedDeck = await this.deckRepository.hardDelete(deckId, userId);
        if (!deletedDeck) {
            throw new Error("Không tìm thấy bộ thẻ hoặc bạn không có quyền xóa");
        }
        return new deckResponseDto(deletedDeck);
    }
}
module.exports = DeckService;