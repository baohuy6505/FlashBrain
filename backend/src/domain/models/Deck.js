const mongoose = require('mongoose');

const deckSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  user_id: { type: String, ref: 'User', required: true },
  title: { type: String, required: true },
  is_public: { type: Boolean, default: false },
  is_deleted: { type: Boolean, default: false }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' } 
});

deckSchema.index({ user_id: 1, is_deleted: 1 });

module.exports = mongoose.model('Deck', deckSchema);