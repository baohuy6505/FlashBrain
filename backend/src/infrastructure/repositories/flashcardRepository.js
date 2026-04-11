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

}
module.exports = new FlashcardRepository();