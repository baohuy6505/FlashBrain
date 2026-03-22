const mongoose = require('mongoose');

const flashcardSchema = new mongoose.Schema({
  _id: { type: String, required: true }, 
  deck_id: { type: String, ref: 'Deck', required: true },
  
  front_text: { type: String, required: true },
  back_text: { type: String, required: true },
  
  // Các chỉ số SM-2
  interval: { type: Number, default: 0 },  //
  repetition: { type: Number, default: 0 },//
  ease_factor: { type: Number, default: 2.5 },//
  
  next_review_date: { type: Date, default: null },
  last_reviewed_at: { type: Date, default: null },
  isDeleted: { type: Boolean, default: false }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' }
});

flashcardSchema.index({ deck_id: 1, is_deleted: 1, next_review_date: 1 });

module.exports = mongoose.model('Flashcard', flashcardSchema);