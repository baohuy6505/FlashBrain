const IFlashcardRepository = require('../../infrastructure/interfaces/IFlashcardRepository')
const FlashcardModel = require('../../domain/models/Flashcard');

class FlashcardRepository extends IFlashcardRepository {

    async findByDeckId(deckId) {
        return await FlashcardModel.find({ 
            deck_id: deckId, 
            is_deleted: false 
        });
    }

    async create(flashcardData) {
        const newCard = new FlashcardModel(flashcardData);
        return await newCard.save();
    }
    
    async findById(cardId) {
        const card = await FlashcardModel.findById(cardId);
        return card;
    }

    async updateById(cardId, updateData) {
        return await FlashcardModel.findOneAndUpdate(
            { _id: cardId, is_deleted: false },
            { $set: updateData },
            { returnDocument: 'after', runValidators: true }
        );
    }

    async softDelete(cardId) {
        return await FlashcardModel.findOneAndUpdate(
            { _id: cardId, is_deleted: false },
            { $set: { is_deleted: true } },
            { returnDocument: 'after' }
        );
    }

    async restore(cardId) {
        return await FlashcardModel.findOneAndUpdate(
            { _id: cardId, is_deleted: true },
            { $set: { is_deleted: false } },
            { returnDocument: 'after' }
        );
    }

    async hardDelete(cardId) {
        return await FlashcardModel.findOneAndDelete({ _id: cardId, is_deleted: true });
    }

    // Lấy thống kê thẻ bài dựa trên danh sách Deck ID
    async getStatsByDeckIds(deckIds) {
        const now = new Date();

        // 1. Tổng số thẻ (Chưa bị xóa)
        const totalCards = await FlashcardModel.countDocuments({ 
            deck_id: { $in: deckIds }, 
            is_deleted: false 
        });

        // 2. Số thẻ cần ôn tập (Lịch ôn tập <= thời điểm hiện tại)
        const cardsToReview = await FlashcardModel.countDocuments({
            deck_id: { $in: deckIds },
            is_deleted: false,
            next_review_date: { $lte: now }
        });

        // 3. Số thẻ mới tinh (chưa học lần nào, repetition = 0)
        const newCards = await FlashcardModel.countDocuments({
            deck_id: { $in: deckIds },
            is_deleted: false,
            repetition: 0
        });

        return {
            totalCards,
            cardsToReview,
            newCards,
            learnedCards: totalCards - newCards // Thẻ đã học = Tổng - Thẻ mới
        };
    }
}
module.exports = new FlashcardRepository();