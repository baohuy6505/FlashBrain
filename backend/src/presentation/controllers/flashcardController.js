const flashcardService = require('../../application/services/flashcardService');
const FlashcardRequestDto = require('../../application/dtos/flashcard/flashcardRequestDto');
const FlashcardUpdateRequestDto = require('../../application/dtos/flashcard/flashcardUpdateRequestDto');
class FlashcardController {
    async getFlashcardsByDeck(req, res) {
        try {
            const deckId = req.params.deckId;
            const userId = req.user.userId;
    
            const cards = await flashcardService.getFlashcardsByDeck(deckId, userId);
    
            return res.status(200).json({
                success: true,
                message: "Lấy danh sách thẻ thành công",
                data: cards
            });
        } catch (error) {
            return res.status(403).json({ success: false, message: error.message });
        }
    }

    async createFlashcard(req, res) {
        try {
            const userId = req.user.userId;
            const cardDto = new FlashcardRequestDto(req.body);
            await cardDto.validate();

            const createdCard = await flashcardService.createFlashcard(userId, cardDto);

            return res.status(201).json({
                success: true,
                message: "Tạo thẻ Flashcard thành công",
                data: createdCard
            });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async updateFlashcard(req, res) {
        try {
            const cardId = req.params.id;
            const userId = req.user.userId;
            const updateDto = new FlashcardUpdateRequestDto(req.body);
            await updateDto.validate();

            const updatedCard = await flashcardService.updateFlashcard(cardId, userId, updateDto);

            return res.status(200).json({
                success: true,
                message: "Cập nhật thẻ thành công",
                data: updatedCard
            });
        } catch (error) {
            return res.status(403).json({ success: false, message: error.message });
        }
    }

    async softDelete(req, res) {
        try {
            const cardId = req.params.id;
            const userId = req.user.userId;
            const data = await flashcardService.softDeleteFlashcard(cardId, userId);
            return res.status(200).json({ success: true, message: "Đã đưa thẻ vào thùng rác", data });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async restore(req, res) {
        try {
            const cardId = req.params.id;
            const userId = req.user.userId;
            const data = await flashcardService.restoreFlashcard(cardId, userId);
            return res.status(200).json({ success: true, message: "Khôi phục thẻ thành công", data });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }

    async hardDelete(req, res) {
        try {
            const cardId = req.params.id;
            const userId = req.user.userId;
            const data = await flashcardService.hardDeleteFlashcard(cardId, userId);
            return res.status(200).json({ success: true, message: data.message });
        } catch (error) {
            return res.status(400).json({ success: false, message: error.message });
        }
    }
}
module.exports = new FlashcardController();