const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  _id: { type: String, required: true }, 
  email: { type: String, required: true, unique: true },
  name: { type: String, default: '' },
  passwordHash: { type: String, required: true },
  image: { type: String, default: '' },
  role: { type: String, enum: ['USER', 'ADMIN'], default: 'USER' },
  subscriptionType: { type: String, enum: ['FREE', 'PRO'], default: 'FREE' },
  
  balance: { type: Number, default: 0 },
  proExpireAt: { type: Date, default: null },
  isDeleted: { type: Boolean, default: false }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' } 
});

module.exports = mongoose.model('User', userSchema);