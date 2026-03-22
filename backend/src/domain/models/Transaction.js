const mongoose = require('mongoose');

const transactionSchema = new mongoose.Schema({
  _id: { type: String, required: true },
  user_id: { type: String, ref: 'User', required: true },
  type: { type: String, enum: ['DEPOSIT', 'BUY_PACKAGE'], required: true },
  amount: { type: Number, required: true },
  
  package_id: { type: String, ref: 'ProPackage', default: null },
  status: { type: String, enum: ['PENDING', 'SUCCESS', 'FAILED'], default: 'PENDING' }
}, { 
  timestamps: { createdAt: 'created_at', updatedAt: false } 
});

transactionSchema.index({ user_id: 1, created_at: -1 });

module.exports = mongoose.model('Transaction', transactionSchema);