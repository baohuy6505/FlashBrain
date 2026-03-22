const mongoose = require('mongoose');

const userProgressSchema = new mongoose.Schema({
  _id: { type: String, ref: 'User', required: true }, 
  total_learned: { type: Number, default: 0 },
  streak_days: { type: Number, default: 0 },
  last_study_date: { type: Date, default: null }
}, { 
  timestamps: { createdAt: false, updatedAt: 'updated_at' } 
});

module.exports = mongoose.model('UserProgress', userProgressSchema);