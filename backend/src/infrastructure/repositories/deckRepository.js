const IDeckRepository = require("../interfaces/IDeckRepository");
const DeckModel = require("../../domain/models/Deck");

class DeckRepository extends IDeckRepository {
  async create(deck) {
    const newDeck = new DeckModel(deck);
    return await newDeck.save();
  }
  async findById(deckId, userId) {
    const deck = await DeckModel.findOne({
      _id: deckId,
      is_deleted: false,
      $or: [{ is_public: true }, { user_id: userId }],
    });
    return deck;
  }
  async findAll(userId) {
    const decks = await DeckModel.find({ user_id: userId, is_deleted: false });
    return decks;
  }
  async findPublic(userId) {
    const publicDecks = await DeckModel.find({
      is_public: true,
      is_deleted: false,
      user_id: { $ne: userId }, //Loại bỏ các deck của chính user đó
    });
    return publicDecks;
  }
  async updateById(deckId, userId, deckUpdate) {
    const updateData = {
      title: deckUpdate.title,
      is_public: deckUpdate.is_public,
    };
    const updatedDeck = await DeckModel.findOneAndUpdate(
      { _id: deckId, user_id: userId },
      { $set: updateData },
      {
        // returnDocument: 'after', // SỬA 'new: true'
        new: true, // Trả về bản sau khi update (thay vì bản cũ)
        runValidators: true, // Đảm bảo dữ liệu mới vẫn đúng Schema (giống ASP.NET Validation)
      },
    );
    return updatedDeck;
  }
  async restoreById(deckId, userId) {
    const restoredDeck = await DeckModel.findOneAndUpdate(
      { _id: deckId, user_id: userId },
      { $set: { is_deleted: false } },
      { new: true },
    );
    return restoredDeck;
  }
  async softDelete(deckId, userId) {
    const deletedDeck = await DeckModel.findOneAndUpdate(
      { _id: deckId, user_id: userId },
      { $set: { is_deleted: true } },
      { new: true },
    );
    return deletedDeck;
  }
  async hardDelete(deckId, userId) {
    const deletedDeck = await DeckModel.findOneAndDelete({
      _id: deckId,
      user_id: userId,
      is_deleted: true // CHỈ CHO PHÉP xóa vĩnh viễn những bộ bài đã nằm trong thùng rác
    });
    return deletedDeck;
  }
}
module.exports = DeckRepository;