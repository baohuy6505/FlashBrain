const mongoose = require('mongoose');

const proPackageSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  name: { type: String, required: true },
  price: { type: Number, required: true },
  duration_days: { type: Number, required: true },
  is_active: { type: Boolean, default: true }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: 'updated_at' } 
});

module.exports = mongoose.model('ProPackage', proPackageSchema);