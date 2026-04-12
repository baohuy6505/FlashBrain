const flashcardRepository = require('../../infrastructure/repositories/flashcardRepository');
const DeckRepository = require('../../infrastructure/repositories/deckRepository');
const deckRepository = new DeckRepository(); // Dùng 'new' để tạo ra thực thể
const FlashcardResponseDto = require('../dtos/flashcard/flashcardResponseDto');
const { v4: uuidv4 } = require('uuid');

class FlashcardService {
    async getFlashcardsByDeck(deckId, userId) {
        const deck = await deckRepository.findById(deckId, userId);
        if (!deck) {
            throw new Error("Không tìm thấy bộ bài hoặc bộ bài này đang ở chế độ riêng tư!");
        }

        const cards = await flashcardRepository.findByDeckId(deckId);
        return cards.map(card => new FlashcardResponseDto(card));
    }

    async createFlashcard(userId, cardDto) {
        const deck = await deckRepository.findById(cardDto.deckId, userId);
        if (!deck) {
            throw new Error("Không tìm thấy bộ bài hoặc bạn không có quyền thêm thẻ vào đây!");
        }

        const newCard = {
            _id: uuidv4(),
            deck_id: cardDto.deckId,
            front_text: cardDto.frontText,
            back_text: cardDto.backText,
        };

        const createdCard = await flashcardRepository.create(newCard);
        return new FlashcardResponseDto(createdCard);
    }

    async updateFlashcard(cardId, userId, updateDto) {
        const card = await flashcardRepository.findById(cardId);
        if (!card) throw new Error("Không tìm thấy thẻ Flashcard");

        const deck = await deckRepository.findById(card.deck_id, userId);
        if (!deck || deck.user_id !== userId) {
            throw new Error("Bạn không có quyền chỉnh sửa thẻ trong bộ bài này!");
        }

        const updateData = {};
        if (updateDto.frontText) updateData.front_text = updateDto.frontText;
        if (updateDto.backText) updateData.back_text = updateDto.backText;

        const updatedCard = await flashcardRepository.updateById(cardId, updateData);
        return new FlashcardResponseDto(updatedCard);
    }

    async _checkPermission(cardId, userId) {
        const card = await flashcardRepository.findById(cardId);
        if (!card) throw new Error("Không tìm thấy thẻ Flashcard");

        const deck = await deckRepository.findById(card.deck_id, userId);
        // Chỉ chủ sở hữu (người tạo ra Deck) mới được xóa/sửa thẻ
        if (!deck || deck.user_id !== userId) {
            throw new Error("Bạn không có quyền thao tác trên thẻ này!");
        }
        return card;
    }

    async softDeleteFlashcard(cardId, userId) {
        await this._checkPermission(cardId, userId);
        const deletedCard = await flashcardRepository.softDelete(cardId);
        if (!deletedCard) throw new Error("Thẻ đã bị xóa hoặc không tồn tại");
        return new FlashcardResponseDto(deletedCard);
    }

    async restoreFlashcard(cardId, userId) {
        await this._checkPermission(cardId, userId);
        const restoredCard = await flashcardRepository.restore(cardId);
        if (!restoredCard) throw new Error("Thẻ không nằm trong thùng rác hoặc không tồn tại");
        return new FlashcardResponseDto(restoredCard);
    }

    async hardDeleteFlashcard(cardId, userId) {
        await this._checkPermission(cardId, userId);
        const result = await flashcardRepository.hardDelete(cardId);
        return { id: cardId, message: "Đã xóa vĩnh viễn thẻ khỏi hệ thống" };
    }

}
module.exports = new FlashcardService();