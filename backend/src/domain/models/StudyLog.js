const mongoose = require('mongoose');

const studyLogSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  user_id: { type: String, ref: 'User', required: true },
  flashcard_id: { type: String, ref: 'Flashcard', required: true },
  
  grade: { type: Number, required: true, min: 1, max: 5 }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: false } 
});

studyLogSchema.index({ user_id: 1, created_at: -1 });

module.exports = mongoose.model('StudyLog', studyLogSchema);